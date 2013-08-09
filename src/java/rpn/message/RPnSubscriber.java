/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.message;

import javax.naming.*;
import javax.jms.*;
import java.io.StringBufferInputStream;
import rpn.parser.*;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 *
 * @author mvera
 */
public class RPnSubscriber implements MessageListener,RPnMessageListener {

    private TopicConnection topicConnection = null;
    private MessageConsumer subscriber = null;
    private TopicConnectionFactory cf = null;
    private javax.jms.Topic topic = null;
    
    protected boolean end_ = false;

    private String listeningName_;


    public RPnSubscriber() {}
    
    public RPnSubscriber(String topicName)  {

        listeningName_ = topicName;

        try {

            final Context context = RPnSender.getInitialMDBContext();

            cf = (TopicConnectionFactory) context.lookup("jms/RemoteConnectionFactory");
            topic = (Topic) context.lookup(topicName);

            topicConnection = cf.createTopicConnection("rpn", "rpn.fluid");

            TopicSession topicSession = topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);

            subscriber = topicSession.createSubscriber(topic);

            subscriber.setMessageListener(this);

            topicConnection.start();
            

        } catch (Exception exc) {

            exc.printStackTrace();

        } 
    }

    public void subscribe() {

        try {

            while (!end_)
                Thread.sleep((long)3000);

            


        } catch (Exception exc) {

            exc.printStackTrace();

        } 
    }

    public void startsListening() {
        subscribe();
    }

    public void stopsListening() {
        unsubscribe();
    }

    public void unsubscribe() {

        end_ = true;

        if (topicConnection != null) {
            try {
                topicConnection.close();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void onMessage(Message message) {

        try {

            if (message instanceof TextMessage) {

                RPnNetworkDialog.infoText.append("Message recieved from rpn command topic..." + '\n');

                String text = ((TextMessage) message).getText();

                parseMessageText(text);

            }

        } catch (Exception exc) {

            exc.printStackTrace();

        } 
    }

    public void parseMessageText(String text) {

        try {


            /*
             * checks if CONTROL MSG or COMMAND MSG
             */

            RPnNetworkStatus.instance().log("Will now parse the message received... " + '\n' + text);

            
            // CONTROL MESSAGES PARSING
            if (text.startsWith(RPnNetworkStatus.SLAVE_ACK_LOG_MSG)) {


                RPnNetworkStatus.instance().ackSlaveRequest(RPnNetworkStatus.filterClientID(text));


            } else if (text.startsWith(RPnNetworkStatus.MASTER_ACK_LOG_MSG)) {

                RPnNetworkStatus.instance().ackMasterRequest(RPnNetworkStatus.filterClientID(text));

            } else if (text.startsWith(RPnNetworkStatus.MASTER_REQUEST_LOG_MSG)) {

                if (RPnNetworkStatus.instance().isMaster()) {

                    RPnMasterReqDialog reqDialog = new RPnMasterReqDialog(RPnNetworkStatus.filterClientID(text));
                    reqDialog.setVisible(true);

                }

            } else if (text.startsWith(RPnNetworkStatus.RPN_COMMAND_PREFIX)) {

                // COMMAND MESSAGES PARSING
                RPnCommandModule.init(XMLReaderFactory.createXMLReader(), new StringBufferInputStream(text));

                // updates the PhaseSpace Frames...
                rpn.RPnPhaseSpaceFrame[] framesList = rpn.RPnUIFrame.getPhaseSpaceFrames();

                for (int i = 0; i < framesList.length; i++) {
                    framesList[i].invalidate();
                    framesList[i].repaint();
                }
            }

        } catch (Exception exc) {

            exc.printStackTrace();

        }
    }


    public String listeningName() {
        return listeningName_;
    }
}
