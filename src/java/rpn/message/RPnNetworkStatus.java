/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.message;


/**
 *
 *
 * <p>The class that stores and manages the network communications parameters </p>

 */
public class RPnNetworkStatus {


    private static RPnNetworkStatus instance_= null;

    private String clientID_;
    private boolean isMaster_;
    private boolean isOnline_;
    private RPnConsumerThread consumerThread_ = null;
    

    public static String  SERVERNAME = new String("heitor");
    public static String  RPN_COMMAND_QUEUE_NAME = new String("jms/queue/rpnCommand");
    public static String RPN_COMMAND_QUEUE_NAME_LOCAL   = new String("queue/rpnCommand");
    public static String  RPN_CONTROL_QUEUE_NAME = new String("jms/queue/rpnMaster");

    public static String MASTER_REQUEST_MSG = new String ("MASTER_REQUEST");
    public static String MASTER_ACK_MSG = new String ("MASTER_ACK");


    /*
     * THE COMMAND TAGS FOR CLIENT/SERVER COMMUNICATION
     */
    public static String RPN_MEDIATORPROXY_REQ_ID_TAG="REQ_ID";
    public static String RPN_MEDIATORPROXY_COMMAND_TAG="RPN_COMMAND";
    public static String RPN_MEDIATORPROXY_CLIENT_ID_TAG="CLIENT_ID";
    public static String RPN_MEDIATORPROXY_SESSION_ID_TAG="SESSION_ID";
    public static String RPN_MEDIATORPROXY_POLL_TAG="RPN_POLL";


    private RPnNetworkStatus() {

        isOnline_ = false;
        //connect("localhost",false);
    }



    public void connect(String clientID,boolean isMaster) {
                       
        clientID_ = clientID;
        isMaster_ = isMaster;
        isOnline_ = true;

        if (!isMaster_) {

            RPnSender.init(RPN_CONTROL_QUEUE_NAME);
            consumerThread_ = new RPnConsumerThread(RPN_COMMAND_QUEUE_NAME);

            MASTER_REQUEST_MSG += '|' + clientID_;

        }
        else {

            RPnSender.init(RPN_COMMAND_QUEUE_NAME);
            consumerThread_ = new RPnConsumerThread(RPN_CONTROL_QUEUE_NAME);
        }
            
        // we will always be listening to either COMMANDs or CONTROLs
        consumerThread_.start();

        log("Connected to JBoss server : " + SERVERNAME);
    }

    public void disconnect() {

        if (isMaster_)
                RPnSender.close();
            else {               
                
                RPnConsumer.end = true;

                try {

                    consumerThread_.join();

                } catch (InterruptedException ex) {
                    
                    log("Connection closed for RPnConsumer...");
                }
            }

        isOnline_ = false;
        RPnConsumer.stopsListening();
    }

    public boolean isMaster() {
        return isMaster_;
    }

    public boolean isOnline() {
        return isOnline_;
    }

    public String clientID() {
        return clientID_;
    }

    public String log() {
        return RPnNetworkDialog.infoText.getText();
    }

    public void log(String logMessage) {

        RPnNetworkDialog.infoText.append(logMessage + '\n');
    }

      
    public void sendCommand(String commandDesc) {

        log(commandDesc);
        RPnSender.send(commandDesc);
    }

    public void sendMasterRequest() {

        log (MASTER_REQUEST_MSG);
        RPnSender.send(MASTER_REQUEST_MSG);

    }


    public static RPnNetworkStatus instance() {

        if (instance_ == null) 
            instance_ = new RPnNetworkStatus();

        return instance_;
    }
}
