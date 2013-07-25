/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.message;


import javax.naming.*;
import javax.jms.*;
import java.util.*;

/**
 *
 * @author mvera
 */
public class RPnSender {

    public Connection connection = null;
    public Session session = null;
    public Context context = null;
    public MessageProducer sender = null;
    public javax.jms.Queue queue = null;
    public QueueConnectionFactory cf = null;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {

        //send("jms/queue/rpnCommand","Hello JMS !");
        

    }


    public RPnSender(String destinationName) {
                            

        try {


            //RPnNetworkStatus.instance().log("initiating the send context...");
            //String destinationName = ;


            //QueueConnectionFactory cf = null;


            context = getInitialMDBContext();

            
            //cf = (QueueConnectionFactory) context.lookup("java:jboss/exported/jms/RemoteConnectionFactory");

            // REMOTE CONNECTION
            cf = (QueueConnectionFactory) context.lookup("jms/RemoteConnectionFactory");

            // LOCAL CONNECTION
            //cf = (QueueConnectionFactory) context.lookup("java:/ConnectionFactory");

            queue = (javax.jms.Queue) context.lookup(destinationName);
            connection = cf.createConnection("rpn","rpn.fluid");
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            sender = session.createProducer(queue);

            connection.start();                       

    
        } catch (Exception exc) {

            exc.printStackTrace();

        } 

    }

    public void send(String messageToSend) {
       
        try {

            

            TextMessage messageTo = session.createTextMessage(messageToSend);
            sender.send(messageTo);

            RPnNetworkDialog.infoText.append("Message sent to the JMS Provider : " + messageTo + '\n');
                                       
        } catch (Exception exc) {

            exc.printStackTrace();

        } 
    }
    
    public void close() {

        try {

            session.close();
            connection.close();
            context.close();

        } catch (Exception exc) {

            exc.printStackTrace();

        } finally {


            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public static Context getInitialMDBContext()
            throws javax.naming.NamingException {


        final Hashtable jndiProperties = new Hashtable();
        //jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY,"org.jboss.naming.remote.client.InitialContextFactory");
        //jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY,"org.hornetq.core.remoting.impl.netty.NettyConnectorFactory");

        
        jndiProperties.put(Context.PROVIDER_URL, "remote://147.65.7.10:4447");
        jndiProperties.put(Context.SECURITY_PRINCIPAL, "rpn");
        jndiProperties.put(Context.SECURITY_CREDENTIALS, "rpn.fluid");



        return new InitialContext(jndiProperties);
    }
}