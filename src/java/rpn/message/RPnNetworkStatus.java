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

    private RPnSender slaveRequestSender_ = null;
    private RPnPublisher masterRequestPublisher_ = null;

    private RPnConsumerThread slaveReqConsumerThread_ = null;
    private RPnPublisher commandPublisher_ = null;
    private RPnPublisher masterAckPublisher_ = null;
    private RPnSubscriberThread slaveSubscriberThread_ = null;
    private RPnSubscriberThread masterAckSubscriberThread_ = null;
    private RPnSubscriberThread slaveAckSubscriberThread_ = null;

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

    // FLUID MASTER QUEUEs and CONSUMERS

    // GLUCK
    public static String RPN_MASTER_QUEUE_NAME_GLUCK = new String("jms/queue/RPN_MASTER_QUEUE_1234_GLUCK");
    private RPnConsumer masterReqConsumerGluck_ = null;
    private RPnSender masterAckSenderGluck_ = null;

    // SIBELIUS
    public static String RPN_MASTER_QUEUE_NAME_SIBELIUS = new String("jms/queue/RPN_MASTER_QUEUE_1234_SIBELIUS");
    private RPnConsumer masterReqConsumerSibelius_ = null;
    private RPnSender masterAckSenderSibelius_ = null;
    
    public static String RPN_SLAVE_ACK_TOPIC_NAME = new String("jms/topic/RPN_SLAVE_ACK_TOPIC_1234");
    public static String RPN_MASTER_ACK_TOPIC_NAME = new String("jms/topic/RPN_MASTER_ACK_TOPIC_1234");

    /*
     * RPN CONTROL MESSAGES
     */
    public static String MASTER_REQUEST_LOG_MSG = new String ("MASTER_REQUEST");
    public static String MASTER_ACK_LOG_MSG = new String ("MASTER_ACK");
    public static String SLAVE_REQ_LOG_MSG = new String ("SLAVE_REQ");
    public static String SLAVE_ACK_LOG_MSG = new String ("SLAVE_ACK");
    public static String NULL_MESSAGE = new String("");

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
        isOnline_ = true;
        isFirewalled_ = isFirewalled;

        //masterRequestSender_ = new RPnSender(RPN_MASTER_REQ_QUEUE_NAME);

        if (!isMaster_) {

            subsSlaveAckRequest();
            sendSlaveRequest();
            log("RPn user : " +  clientID_ + " has requested to follow RPNSESSION with ID : " + rpn.parser.RPnCommandModule.SESSION_ID_ + '\n');            
        }
        else {

            subsMasterAckRequest();
            sendMasterRequest();
            log("RPn user : " +  clientID_ + " has requested MASTER access to RPNSESSION with ID : " + rpn.parser.RPnCommandModule.SESSION_ID_ + '\n');
        }

        log("Connected to JBoss server : " + SERVERNAME);
    }

    public void disconnect() {

        if (isMaster_) {
               
                commandPublisher_.close();
        }

        else {


                slaveSubscriberThread_.unsubscribe();

                try {

                    slaveSubscriberThread_.join();

                } catch (InterruptedException ex) {

                    log("Connection closed for RPnSubscriber...");
                }
            }

        isOnline_ = false;        
    }

    public void subsMasterAckRequest() {

        if (masterAckSubscriberThread_ == null)
            masterAckSubscriberThread_ = new RPnSubscriberThread(RPN_MASTER_ACK_TOPIC_NAME);

        masterAckSubscriberThread_.start();
        log("RPn user : " +  clientID_ + " is holding MASTER lock for RPNSESSION with ID : " + rpn.parser.RPnCommandModule.SESSION_ID_ + '\n');

    }

    public void sendMasterRequest() {

        // REQUESTS TO BECOME MASTER WILL BE A POLLING METHOD TO ALL POSSIBLE CLIENTs

        // GLUCK
        if (masterReqConsumerGluck_ == null)
            masterReqConsumerGluck_ = new RPnConsumer(RPN_MASTER_QUEUE_NAME_GLUCK,Session.CLIENT_ACKNOWLEDGE);
        Message masterMsgFromGluck = masterReqConsumerGluck_.consume();

        // SIBELIUS
        if (masterReqConsumerSibelius_ == null)
            masterReqConsumerSibelius_ = new RPnConsumer(RPN_MASTER_QUEUE_NAME_SIBELIUS,Session.CLIENT_ACKNOWLEDGE);
        Message masterMsgFromSibelius = masterReqConsumerSibelius_.consume();


        // IF ALL NULL ... NO MASTER IN THE HOUSE !!!
        if ((masterMsgFromGluck == null) &&
            (masterMsgFromSibelius == null)) {

            // PUBLISHEs A MASTER ACK
            if (masterAckPublisher_ == null)
                masterAckPublisher_ = new RPnPublisher(RPN_MASTER_ACK_TOPIC_NAME);

            masterAckPublisher_.publish(MASTER_ACK_LOG_MSG + '|' + clientID_);

        } else {

            // THERE IS A MASTER...
            if (masterRequestPublisher_ == null)
                masterRequestPublisher_ = new RPnPublisher(RPN_MASTER_REQ_TOPIC_NAME);

            masterRequestPublisher_.publish(MASTER_REQUEST_LOG_MSG + '|' + clientID_);

        }


    }

    public void ackMasterRequest(String clientID) {

        log(clientID + " has being acknowledged as MASTER of RPNSESSION with ID : " + rpn.parser.RPnCommandModule.SESSION_ID_ + '\n');

        if (clientID.compareTo(clientID_) == 0) {


            // RPN COMMAND PUBLISH
            if (commandPublisher_ == null)
                commandPublisher_ = new RPnPublisher(RPN_COMMAND_TOPIC_NAME);

            // SLAVE REQ RECEIVE
            if (slaveReqConsumerThread_ == null) {
                slaveReqConsumerThread_ = new RPnConsumerThread(RPN_SLAVE_REQ_QUEUE_NAME);
                slaveReqConsumerThread_.start();
            }

            // FILLs UP THE LOCAL MASTER QUEUE
            if (clientID_.compareTo("Gluck") == 0) {

                if (masterAckSenderGluck_ == null)
                    masterAckSenderGluck_ = new RPnSender(RPN_MASTER_QUEUE_NAME_GLUCK);

                masterAckSenderGluck_.send(MASTER_ACK_LOG_MSG);

            } else if (clientID_.compareTo("Siberius") == 0) {

                if (masterAckSenderSibelius_ == null)
                    masterAckSenderSibelius_ = new RPnSender(RPN_MASTER_QUEUE_NAME_SIBELIUS);

                masterAckSenderSibelius_.send(MASTER_ACK_LOG_MSG);

            }
            
        }


    }

    public void ackSlaveRequest(String clientID) {


        log(clientID + " has being acknowledged as SLAVE of RPNSESSION with ID : " + rpn.parser.RPnCommandModule.SESSION_ID_ + '\n');

        if (clientID.compareTo(clientID_) == 0) {

            // SLAVE SUBS to RPN COMMAND TOPIC
            slaveSubscriberThread_ = new RPnSubscriberThread(RPN_COMMAND_TOPIC_NAME);
            slaveSubscriberThread_.start();
            log("RPn user : " +  clientID_ + " is now following RPNSESSION with ID : " + rpn.parser.RPnCommandModule.SESSION_ID_ + '\n');
        }

    }

    public void subsSlaveAckRequest() {

        if (slaveAckSubscriberThread_ == null)
            slaveAckSubscriberThread_ = new RPnSubscriberThread(RPN_SLAVE_ACK_TOPIC_NAME);

        slaveAckSubscriberThread_.start();
        log("RPn user : " +  clientID_ + " is holding SLAVE acknowledge for RPNSESSION with ID : " + rpn.parser.RPnCommandModule.SESSION_ID_ + '\n');
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
