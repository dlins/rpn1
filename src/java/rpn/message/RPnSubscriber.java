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
public class RPnSubscriber implements MessageListener {

    private TopicConnection topicConnection = null;
    private MessageConsumer subscriber = null;
    private TopicConnectionFactory cf = null;
    private javax.jms.Topic topic = null;
    
    private boolean end_ = false;


    public RPnSubscriber(String topicName) {

        try {

            final Context context = RPnSender.getInitialMDBContext();

            cf = (TopicConnectionFactory) context.lookup("jms/RemoteConnectionFactory");
            topic = (Topic) context.lookup(topicName);

        } catch (Exception exc) {

            exc.printStackTrace();

        } finally {

            if (topicConnection != null) {

                try {
                    topicConnection.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void subscribe() {

        try {

            topicConnection = cf.createTopicConnection("rpn", "rpn.fluid");

            TopicSession topicSession = topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);

            subscriber = topicSession.createSubscriber(topic);

            subscriber.setMessageListener(this);

            topicConnection.start();

            while (!end_)
                Thread.sleep((long)3000);

            


        } catch (Exception exc) {

            exc.printStackTrace();

        } finally {

            if (topicConnection != null) {
                try {
                    topicConnection.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }
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

                // parses the stream...
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
}
