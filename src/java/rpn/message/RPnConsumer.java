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
import org.hornetq.api.jms.management.JMSServerControl;

/**
 *
 * @author mvera
 */
public class RPnConsumer  {

    public static boolean end = false;
    public static QueueConnection queueConnection = null;
    public static QueueReceiver receiver = null;
    public static QueueConnectionFactory cf = null;
    public static javax.jms.Queue queue = null;
    

    public static void main(String[] args) {
               
        //RPnConsumer.startsListening("jms/queue/rpnCommand");
    }

    public static void init(String queueName) {



        try {

            final Context context = RPnSender.getInitialMDBContext();

            cf = (QueueConnectionFactory) context.lookup("jms/RemoteConnectionFactory");
            queue = (javax.jms.Queue) context.lookup(queueName);

            JMSServerControl controller = (JMSServerControl) context.lookup("jms.server");
            controller.createQueue("MYQUEUE");
            
    
        } catch (Exception exc) {

            exc.printStackTrace();

        } finally {

            if (queueConnection != null) {
                try {
                    queueConnection.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void startsListening() {
           
        try {

            queueConnection = cf.createQueueConnection("rpn", "rpn.fluid");
            //QueueSession queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);

            // this will keep the messages on the queue...
            QueueSession queueSession = queueConnection.createQueueSession(false, Session.CLIENT_ACKNOWLEDGE);

            receiver = queueSession.createReceiver(queue);
            
            queueConnection.start();

            while (!end) {         

                RPnNetworkDialog.infoText.append("Will now listen to rpn command queue..." + '\n');

                Message message = receiver.receive((long)15000);                        
                String text;

                if (message instanceof TextMessage) {

                    RPnNetworkDialog.infoText.append("Message recieved from rpn command queue..." + '\n');
                    
                    text = ((TextMessage) message).getText();

                    // checks if CONTROL MSG or COMMAND MSG

                    if (text.startsWith("MASTER_ACK")) {

                        RPnNetworkStatus.instance().disconnect();

                        // I am Master of the session now...
                        RPnNetworkStatus.instance().connect(RPnNetworkStatus.instance().clientID(),true);


                    } else if (text.startsWith("MASTER_REQUEST")) {

                        // should I give control away... ?
                        RPnNetworkStatus.instance().disconnect();

                        RPnSender.send(RPnNetworkStatus.MASTER_ACK_MSG);

                        // I am following the new Master now...
                        RPnNetworkStatus.instance().connect(RPnNetworkStatus.instance().clientID(),false);

                    } else {

                        // parses the stream...
                        RPnCommandModule.init(XMLReaderFactory.createXMLReader(), new StringBufferInputStream(text));

                        // updates the PhaseSpace Frames...
                        rpn.RPnPhaseSpaceFrame[] framesList = rpn.RPnUIFrame.getPhaseSpaceFrames();

                        for (int i = 0; i < framesList.length; i++) {
                            framesList[i].invalidate();
                            framesList[i].repaint();
                        }
                    }
                } 
            }

        } catch (Exception exc) {

            exc.printStackTrace();

        } finally {

            if (queueConnection != null) {
                try {
                    queueConnection.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void stopsListening() {

        end = false;

        if (queueConnection != null) {
            try {
                queueConnection.close();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }       
    }
}
