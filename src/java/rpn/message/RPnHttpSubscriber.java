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
import java.util.Vector;
/**
 *
 * @author mvera
 */
public class RPnHttpSubscriber extends RPnSubscriber {

    private TopicConnection topicConnection = null;
    private MessageConsumer subscriber = null;
    private TopicConnectionFactory cf = null;
    private javax.jms.Topic topic = null;

    // TEMPORARY
    public static Vector commandQueue_ = new Vector();


    private boolean end_ = false;

    private String listeningName_;


    public RPnHttpSubscriber(String topicName)  {

        

        listeningName_ = topicName;

        try {

            final Context context = new InitialContext();

            cf = (TopicConnectionFactory) context.lookup("java:/ConnectionFactory");
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

   


    public synchronized void onMessage(Message message) {

        try {

            if (message instanceof TextMessage) {

                System.out.println("Message recieved from rpn command topic..." + '\n');

                String text = ((TextMessage) message).getText();

                commandQueue_.add(text.toString());

            }

        } catch (Exception exc) {

            exc.printStackTrace();

        }
    }

    public void parseMessageText(String text) {

      // DO NOTHING...
    }

    public String listeningName() {
        return listeningName_;
    }


}