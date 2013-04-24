package rpnmediator;

import java.util.*;
import javax.jms.*;
import javax.jms.Queue;
import javax.management.*;
import javax.naming.*;
import java.io.*;

public class RPnMediator
        implements RPnMediatorMBean {
    
    private Hashtable env_, clients_;
    private ObjectName destinationBean_;
    
    private ObjectName queueCommandBackupObject_;
    
    private Queue queueCommand_, queueCommandBackup_;
    
    private MBeanServerConnection server_;
    
    private QueueSession session_;
    
    private Queue qSend_;
    private QueueSender sender_;
    
    private QueueConnection conn_;
    
    private String[] sig_, params_;
    
    private ArrayList domains_;
    
    private InitialContext ic_;
    
    //Constants
    
    
    private final static String DOMAINS_OBJECT = new String("rpnDomains");
    
    private final static String COMMAND_QUEUE = new String(
            "queue/rpn/queueCommand");
    
    private final static String BACKUP_QUEUE = new String(
            "queue/rpn/backup/queueCommandBackup");
    
    private final static String RPN_QUEUE_DIRECTORY = new String("queue/rpn/");
    
    public void start() throws Exception {
        
        System.out.println("Rodando o start()");
        params_ = new String[1];
        sig_ = new String[1];
        env_ = new Hashtable();
        env_.put(Context.INITIAL_CONTEXT_FACTORY,
                "org.jnp.interfaces.NamingContextFactory");
        ic_ = new InitialContext(env_);
        domains_ = new java.util.ArrayList();
        ic_.rebind(DOMAINS_OBJECT, domains_);
        ic_.rebind("rpnMasterID", new String("no Master"));
        server_ = (MBeanServerConnection) ic_.lookup("jmx/invoker/RMIAdaptor");
        destinationBean_ = new ObjectName("jboss.mq:service=DestinationManager");
        try {
            queueCommand_ = (Queue) ic_.lookup(COMMAND_QUEUE);
            
        } catch (NamingException ex) {
            System.out.println("Erro no lookup da queueCommand. Criando-a !!!");
            params_[0] = "rpn/queueCommand";
            sig_[0] = "java.lang.String";
            server_.invoke(destinationBean_, "createQueue", params_, sig_);
        }
        
        try {
            queueCommandBackup_ = (Queue) ic_.lookup(
                    BACKUP_QUEUE);
            queueCommandBackupObject_ = new ObjectName(
                    "jboss.mq.destination:name=rpn/backup/queueCommandBackup,service=Queue");
            
        } catch (NamingException ex1) {
            System.out.println("Erro no lookup da queueCommandBackup. Criando-a !!!");
            params_[0] = "rpn/backup/queueCommandBackup";
            sig_[0] = "java.lang.String";
            server_.invoke(destinationBean_, "createQueue", params_, sig_);
        }
//        initJMS();        

    }
    
    private void initJMS() {
        
        try {
            
            Object tmp = ic_.lookup("ConnectionFactory");
            
//            Object tmp = ic_.lookup("QueueConnectionFactory");
            QueueConnectionFactory qcf = (QueueConnectionFactory) tmp;
            conn_ = qcf.createQueueConnection();
            System.out.println("Chamando createQueueSession");
            session_ = conn_.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
            conn_.start();
            
        } catch (Exception ex) {
            System.out.println("Error in initJMS");
            System.out.println(ex);
            
        }
    }
    
    private void sendMessage(Message msg, String queueName) {
        
        try {
            
            if (session_==null)
                initJMS();
            
            Object temp = ic_.lookup(queueName);
            qSend_ = (Queue) temp;
            sender_ = session_.createSender(qSend_);
            sender_.send(msg);
        } catch (Exception ex) {
            System.out.println("Error in sendMessage");
            ex.printStackTrace();
            
        }
        
    }
    
    public void writeBackupQueue(Message msg) {
        
        try {
            String mastername = (String) ic_.lookup("rpnMasterID");
            // Only master messages are send to backup
            
            if (msg instanceof ObjectMessage &&
                    msg.getStringProperty("From").equals(mastername)) {
                
                sendMessage(msg, BACKUP_QUEUE);
            }
            
            if (msg instanceof TextMessage) {
                
                if (msg.getStringProperty("MRequest").equals("State") &&
                        msg.getStringProperty("From").equals(mastername)) {
                    
                    sendMessage(msg, BACKUP_QUEUE);
                }
                
            }
            
        }
        
        catch (Exception ex) {
            ex.printStackTrace();
            
        }
        
    }
    
    public void removeClient(String clientID) {
        
        try {
            
            if (session_==null)
                initJMS();
            
            params_[0] = "rpn/" + clientID;
            
            sig_[0] = "java.lang.String";
            
            server_.invoke(destinationBean_, "destroyQueue", params_, sig_);
            
            System.out.println("Removendo o cliente: " + clientID);
            
            if (clientID.equals(ic_.lookup("rpnMasterID"))) {
                
                System.out.println("Limpando a queue de backup");
                
                clearBackupQueue();
                
                //Notifica os outros clientes da saida do master
                
                TextMessage message = session_.createTextMessage();
                
                message.setText("Master Offline");
                
                message.setStringProperty("MRequest", "MOffline");
                
                sendMessage(message, COMMAND_QUEUE);
                ////
                
            }
            
        } catch (Throwable t) {
            
            t.printStackTrace();
            
            System.out.println("Erro em removeClient()" +
                    t.getMessage());
        }
        
    }
    
    public void addClient(String clientID) {
        
        try {
            params_[0] = "rpn/" + clientID;
            
            sig_[0] = "java.lang.String";
            server_.invoke(destinationBean_, "createQueue", params_, sig_);

            queueCommandBackupObject_ = new ObjectName(
                    "jboss.mq.destination:name=rpn/backup/queueCommandBackup,service=Queue");
            
            List messageList = (List) server_.invoke(queueCommandBackupObject_,
                    "listMessages", null, null);
            
            ListIterator iterator = messageList.listIterator();
            
            while (iterator.hasNext()) {
                
                sendMessage( (Message) iterator.next(), "queue/rpn/" + clientID);
                
            }
            
        } catch (IOException ex) {
            System.out.println("Erro em addClient()");
            ex.printStackTrace();
        } catch (ReflectionException ex) {
            System.out.println("Erro em addClient()");
            ex.printStackTrace();
        } catch (MBeanException ex) {
            System.out.println("Erro em addClient()");
            ex.printStackTrace();
        } catch (InstanceNotFoundException ex) {
            System.out.println("Erro em addClient()");
            
            ex.printStackTrace();
        } catch (MalformedObjectNameException ex) {
            System.out.println("Erro em addClient()");
            ex.printStackTrace();
            
        }
        
    }
    
    public void stop() throws Exception {
        System.out.println("Rodando stop()");
        try {
            if (session_ != null) {
                session_.close();
            }
            
            if (conn_ != null) {
                conn_.close();
            }
            
        } catch (JMSException e) {
            e.printStackTrace();
        }
        
    }
    
    public void setAsMaster(String clientID) {
        
        try {
            
            ic_.rebind("rpnMasterID", clientID);
            System.out.println("Cliente: " + clientID + " setado como master");
            
        }
        
        catch (Exception e) {
            System.out.println("Erro no metodo setAsMaster");
            e.printStackTrace();
        }
        
    }
    
    public void addDomain(String domain) {
        
        try {
            ArrayList tempArray = (ArrayList) ic_.lookup(DOMAINS_OBJECT);
            
            tempArray.add(domain);
            ic_.rebind(DOMAINS_OBJECT, tempArray);
            System.out.println("Adicionando dominio: " + domain);
            
            mostraDominios();
        } catch (NamingException ex) {
            
            ex.printStackTrace();
        }
        
    }
    
    public void removeDomain(String domain) {
        
        try {
            
            ArrayList tempArray = (ArrayList) ic_.lookup(DOMAINS_OBJECT);
            
            for (int i = 0; i < tempArray.size(); i++) {
                
                String tempDomain = (String) tempArray.get(i);
                
                if (tempDomain.equals(domain)) {
                    tempArray.remove(i);
                }
            }
            
            ic_.rebind(DOMAINS_OBJECT, tempArray);
            
        }
        
        catch (NamingException ex) {
            
            ex.printStackTrace();
        }
        
    }
    
    private void mostraDominios() {
        
        System.out.println("Dominios registrados: ");
        
        try {
            ArrayList tempArray = (ArrayList) ic_.lookup(DOMAINS_OBJECT);
            
            for (int i = 0; i < tempArray.size(); i++) {
                
                String tempDomain = (String) tempArray.get(i);
                
                System.out.println(tempDomain);
                
            }
        } catch (NamingException ex) {
            ex.printStackTrace();
        }
        
    }
    
    public Queue receiveQueue(String clientID) {
        
        try {
            return (Queue) ic_.lookup("queue/rpn/" + clientID);
        } catch (NamingException ex) {
            
            ex.printStackTrace();
            
            return null;
        }
        
    }
    
    public Queue sendQueue() {
        
        try {
            return (Queue) ic_.lookup(COMMAND_QUEUE);
        } catch (NamingException ex) {
            
            ex.printStackTrace();
            return null;
        }
        
    }
    
    public void clearBackupQueue() {
        try {
            server_.invoke(queueCommandBackupObject_, "removeAllMessages", null, null);
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ReflectionException ex) {
            ex.printStackTrace();
        } catch (MBeanException ex) {
            ex.printStackTrace();
        } catch (InstanceNotFoundException ex) {
            ex.printStackTrace();
        }
        
    }
    
}
