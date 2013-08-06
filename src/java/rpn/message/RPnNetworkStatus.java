/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.message;

import javax.jms.*;

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
    private boolean isFirewalled_;

    // queues up a slave request (SLAVE)
    private RPnSender slaveRequestSender_ = null;
    // listens to the slave request queue (MASTER)
    private RPnConsumerThread slaveReqConsumerThread_ = null;
    // subscription for an acknowledge given to a SLAVE to join the session (ALL)
    private RPnSubscriberThread slaveAckSubscriberThread_ = null;

    // subscription to RPn COMMANDs topic (SLAVE)
    private RPnSubscriberThread commandSubscriberThread_ = null;
    // publisher for RPn COMMAND (MASTER)
    private RPnPublisher commandPublisher_ = null;

    // publisher for MASTER request
    private RPnPublisher masterRequestPublisher_ = null;
    // subscription to MASTER request
    private RPnSubscriberThread masterReqSubscriberThread_ = null;
    // subscription to MASTER acknowledge (SLAVE)
    private RPnSubscriberThread masterAckSubscriberThread_ = null;

    // TODO : this is not necessary
    private RPnConsumer masterResetConsumer_ = null;
    private RPnPersistentConsumer masterCheckConsumer_ = null;
    private RPnSender masterSender_ = null;


    public static String  SERVERNAME = new String(" heitor.fluid.impa.br ");
    //public static String  RPN_COMMAND_QUEUE_NAME = new String("jms/queue/rpnCommand");
    //public static String  RPN_CONTROL_QUEUE_NAME = new String("jms/queue/rpnMaster");

    // LOCAL JNDI DOES NOT USE jms/ prefix...
    public static String RPN_COMMAND_TOPIC_NAME_LOCAL   = new String("topic/RPN_COMMAND_TOPIC_1234");
    public static String RPN_SLAVE_REQ_QUEUE_NAME_LOCAL = new String("queue/RPN_SLAVE_REQ_QUEUE_1234");

    /*
     * MASTER command publishing TOPIC
     */
    public static String  RPN_COMMAND_TOPIC_NAME = new String("jms/topic/RPN_COMMAND_TOPIC_1234");
    /*
     * MASTER listening on SLAVE REQ QUEUE and publishing on SLAVE ACK
     */
    public static String RPN_SLAVE_REQ_QUEUE_NAME = new String("jms/queue/RPN_SLAVE_REQ_QUEUE_1234");
    public static String RPN_MASTER_REQ_TOPIC_NAME = new String("jms/topic/RPN_MASTER_REQ_TOPIC_1234");
    /*
     * MASTER ACKNOWLEDGE
     */
    public static String RPN_MASTER_QUEUE_NAME = new String("jms/queue/RPN_MASTER_QUEUE_1234");
    
    
    public static String RPN_SLAVE_ACK_TOPIC_NAME = new String("jms/topic/RPN_SLAVE_ACK_TOPIC_1234");
    public static String RPN_MASTER_ACK_TOPIC_NAME = new String("jms/topic/RPN_MASTER_ACK_TOPIC_1234");

    /*
     * RPN CONTROL MESSAGES
     */
    public static String MASTER_REQUEST_LOG_MSG = new String ("MASTER_REQUEST");
    public static String MASTER_ACK_LOG_MSG = new String ("MASTER_ACK");
    public static String SLAVE_REQ_LOG_MSG = new String ("SLAVE_REQ");
    public static String SLAVE_ACK_LOG_MSG = new String ("SLAVE_ACK");
    //public static String NO_MASTER_MSG = new String ("NO MASTER");
    public static String NULL_MSG = new String("");
    public static String RPN_COMMAND_PREFIX = new String("<COMMAND");

    /*
     * RPN CLIENT/SERVER CONTROL MESSAGES
     */
    public static String RPN_MEDIATORPROXY_REQ_ID_TAG="REQ_ID";
    public static String RPN_MEDIATORPROXY_COMMAND_TAG="RPN_COMMAND";
    public static String RPN_MEDIATORPROXY_CLIENT_ID_TAG="CLIENT_ID";
    public static String RPN_MEDIATORPROXY_SESSION_ID_TAG="SESSION_ID";
    public static String RPN_MEDIATORPROXY_POLL_TAG="RPN_POLL";

    //
    // Constructors/Initializers
    //
    private RPnNetworkStatus() {

        isOnline_ = false;
        isFirewalled_ = false;
    }

    //
    // Accessors/Mutators
    //
    public boolean isMaster() {
        return isMaster_;
    }

    public boolean isOnline() {
        return isOnline_;
    }

    public String clientID() {
        return clientID_;
    }

    public boolean isFirewalled() {
        return isFirewalled_;
    }
    //
    // Methods
    //

    public String log() {
        return RPnNetworkDialog.infoText.getText();
    }

    public void log(String logMessage) {

        RPnNetworkDialog.infoText.append(logMessage + '\n');
    }


    public void connect(String clientID,boolean isMaster,boolean isFirewalled) {

        clientID_ = clientID;
        isMaster_ = isMaster;        
        isFirewalled_ = isFirewalled;



        
        
        // EVERYONE is notified for a MASTER change !!!
        subsMasterAck();

        // EVERYONE is notified of a SLAVE joining the session !!!
        subsSlaveAck();

        if (!isMaster_) {

            log("RPn user : " +  clientID_ + " will request to follow RPNSESSION with ID : " + rpn.parser.RPnCommandModule.SESSION_ID_ + '\n');            
            sendSlaveRequest();
            
        }
        else {

            log("RPn user : " +  clientID_ + " will request MASTER access to RPNSESSION with ID : " + rpn.parser.RPnCommandModule.SESSION_ID_ + '\n');
            sendMasterRequest();
            
        }

        isOnline_ = true;
        log("Connected to JBoss server : " + SERVERNAME);
    }

    public void disconnect() {


        if (commandPublisher_ != null) {
            commandPublisher_.close();
            commandPublisher_ = null;            
        }
        
        if (commandSubscriberThread_ != null) {
            commandSubscriberThread_.unsubscribe();
            commandSubscriberThread_ = null;
        }
        
        if (masterAckSubscriberThread_ != null) {
            masterAckSubscriberThread_.unsubscribe();
            masterAckSubscriberThread_ = null;
        }

        if (masterReqSubscriberThread_ != null) {
            masterReqSubscriberThread_.unsubscribe();
            masterReqSubscriberThread_ = null;
        }


        if (masterCheckConsumer_ != null) {
            masterCheckConsumer_.stopsListening();
            masterCheckConsumer_ = null;
        }
        
        if (masterResetConsumer_ != null) {
            masterResetConsumer_.stopsListening();
            masterResetConsumer_ = null;
        }

        if (masterSender_ != null) {
            masterSender_.close();
            masterSender_ = null;
        }
        
        if (slaveRequestSender_ != null) {
            slaveRequestSender_.close();
            slaveRequestSender_ = null;
        }
        
        if (masterRequestPublisher_ != null) {
            masterRequestPublisher_.close();
            masterRequestPublisher_ = null;
        }

        if (slaveReqConsumerThread_ != null) {
            slaveReqConsumerThread_.stopsListening();
            slaveReqConsumerThread_ = null;
        }
        
        if (slaveAckSubscriberThread_ != null) {
            slaveAckSubscriberThread_.unsubscribe();
            slaveAckSubscriberThread_ = null;
        }

        if (isMaster_)
            log("All Connections closed for MASTER session ...");
        else
            log("All Connections closed for SLAVE session ...");

        try {
            Thread.sleep((long)5000);        
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        isOnline_ = false;




    }

    public void subsMasterAck() {

        if (masterAckSubscriberThread_ == null)
            masterAckSubscriberThread_ = new RPnSubscriberThread(RPN_MASTER_ACK_TOPIC_NAME);

        masterAckSubscriberThread_.start();
        

        log("Will be listening to MASTER REQ now...");

    }

    public void updateMasterQueue() {


        /*
         *  UPDATES THE MASTER_QUEUE STATUS
         */


        // FILLs UP THE MASTER_QUEUE with proper CONTROL MSGs
        if (masterSender_ == null) {
            masterSender_ = new RPnSender(RPN_MASTER_QUEUE_NAME);
        }

        // CHECK IF FIFO really ??
        masterSender_.send(MASTER_ACK_LOG_MSG + '|' + clientID_);
        
    }


    public void resetMasterQueue() {


        /*
         *  RESETs THE MASTER_QUEUE STATUS
         */
        if (masterResetConsumer_ == null)
            // NON PERSISTENT
            masterResetConsumer_ = new RPnConsumer(RPN_MASTER_QUEUE_NAME);

        masterResetConsumer_.consume();

        // releases the MASTER_QUEUE for others to listen to...
        masterResetConsumer_.stopsListening();
        masterResetConsumer_ = null;
        log("MASTER QUEUE has being reset...");

    }

    public boolean checkMasterQueue() {

        // REQUESTS TO BECOME MASTER WILL BE A QUICK ACCESS METHOD TO THE MASTER_QUEUE
        boolean gotMaster = false;
        if (masterCheckConsumer_ == null)
            // PERSISTENT !
            masterCheckConsumer_ = new RPnPersistentConsumer(RPN_MASTER_QUEUE_NAME);
  
        if (masterCheckConsumer_.consume() != null)
                gotMaster = true;
            
        // releases the MASTER_QUEUE for others to listen to...
        masterCheckConsumer_.stopsListening();
        masterCheckConsumer_ = null;
        log("CHECK MASTER QUEUE has returned : " + gotMaster + " for RPNSESSION with ID : " + rpn.parser.RPnCommandModule.SESSION_ID_ + '\n');


        return gotMaster;

    }

    public void sendMasterRequest() {


        boolean gotMaster = checkMasterQueue();

        if (!gotMaster) {

            // I AM THE MASTER NOW...
            ackMasterRequest(clientID_);

        } else {

            // THERE IS A MASTER...SO ASK FOR THE LOCK...
            if (masterRequestPublisher_ == null)
                masterRequestPublisher_ = new RPnPublisher(RPN_MASTER_REQ_TOPIC_NAME);

            masterRequestPublisher_.publish(MASTER_REQUEST_LOG_MSG + '|' + clientID_);

            log(clientID_ + " has requested MASTER lock for SESSION with ID : " + rpn.parser.RPnCommandModule.SESSION_ID_ + '\n');

        }
    }

    public void ackMasterRequest(String clientID) {

        

        if (clientID.compareTo(clientID_) == 0) {


            // TODO : this should be a reconnect !
            if (isOnline()) {

                // leaves the session as SLAVE
                disconnect();
                subsMasterAck();
                subsSlaveAck();

            }


            // and now BECOMES MASTER
            log(clientID + " is now being configured as MASTER for SESSION with ID : " + rpn.parser.RPnCommandModule.SESSION_ID_ + '\n');

            /*
             * RPN COMMAND PUBLISH
             */
            if (commandPublisher_ == null)
                commandPublisher_ = new RPnPublisher(RPN_COMMAND_TOPIC_NAME);

            /*
             * SLAVE REQ RECEIVE
             */
            
            if (slaveReqConsumerThread_ == null) {
                slaveReqConsumerThread_ = new RPnConsumerThread(RPN_SLAVE_REQ_QUEUE_NAME);
                slaveReqConsumerThread_.start();
            }

            /*
             * MASTER REQ SUBS
             */
            if (masterReqSubscriberThread_ == null) {
                masterReqSubscriberThread_ = new RPnSubscriberThread(RPN_MASTER_REQ_TOPIC_NAME);
                // SETs THE MASTER_QUEUE EMPTY
                masterReqSubscriberThread_.start();
            }

             resetMasterQueue();
             updateMasterQueue();

             isMaster_ = true;
             isOnline_ = true;

        } else 
            log(clientID + " has being acknowledged as MASTER for SESSION with ID : " + rpn.parser.RPnCommandModule.SESSION_ID_ + '\n');

    }

    public void ackSlaveRequest(String clientID) {


        log(clientID + " has being acknowledged as SLAVE of RPNSESSION with ID : " + rpn.parser.RPnCommandModule.SESSION_ID_ + '\n');

        if (clientID.compareTo(clientID_) == 0) {

            // SLAVE SUBS to RPN COMMAND TOPIC
            if (commandSubscriberThread_ == null)
                commandSubscriberThread_ = new RPnSubscriberThread(RPN_COMMAND_TOPIC_NAME);

            commandSubscriberThread_.start();

            log("RPn user : " +  clientID_ + " is now following RPNSESSION with ID : " + rpn.parser.RPnCommandModule.SESSION_ID_ + '\n');
        }

    }

    public void subsSlaveAck() {

        if (slaveAckSubscriberThread_ == null)
            slaveAckSubscriberThread_ = new RPnSubscriberThread(RPN_SLAVE_ACK_TOPIC_NAME);

        slaveAckSubscriberThread_.start();        
    }

    public void sendSlaveRequest() {

        // REQUESTS TO BECOME A SLAVE
        if (slaveRequestSender_ == null)
            slaveRequestSender_ = new RPnSender(RPN_SLAVE_REQ_QUEUE_NAME);

        slaveRequestSender_.send(SLAVE_REQ_LOG_MSG + '|' + clientID_);
        log(SLAVE_REQ_LOG_MSG + '|' + clientID_);
    }

    public void sendCommand(String commandDesc) {

        
        commandPublisher_.publish(commandDesc);
        log(commandDesc);
    }


    public static RPnNetworkStatus instance() {

        if (instance_ == null) 
            instance_ = new RPnNetworkStatus();

        return instance_;
    }

    public static String filterClientID(String text) {

        String clientID = text.substring(text.indexOf('|') + 1);
        return clientID;
    }
}
