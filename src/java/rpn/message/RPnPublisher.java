/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.message;


import javax.naming.*;
import javax.jms.*;


/**
 *
 * @author mvera
 */
public class RPnPublisher {


    private TopicConnection connection = null;
    private MessageProducer publisher = null;
    private TopicConnectionFactory cf = null;
    private javax.jms.Topic topic = null;
    private TopicSession topicSession = null;


    public RPnPublisher(String topicName) {


        try {

            RPnNetworkStatus.instance().log("initiating the publish context...");

            final Context context = RPnSender.getInitialMDBContext();

            cf = (TopicConnectionFactory) context.lookup("jms/RemoteConnectionFactory");            
            topic = (Topic) context.lookup(topicName);

            //connection = cf.createTopicConnection("rpn","rpn.fluid");
            connection = cf.createTopicConnection("rpn","rpn.fluid");
            //TopicSession topicSession = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
            topicSession = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);

            publisher = topicSession.createPublisher(topic);

            connection.start();

        } catch (Exception exc) {

            exc.printStackTrace();

        } 
    }

    public void publish(String msg) {

       try {

  

            TextMessage messageTo = topicSession.createTextMessage(msg);
            publisher.send(messageTo);

            RPnNetworkDialog.infoText.append("Message sent to the JMS Provider : " + messageTo + '\n');

        } catch (Exception exc) {

            exc.printStackTrace();

        } 
    }

    public void close() {

        try {

            connection.close();


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
}
