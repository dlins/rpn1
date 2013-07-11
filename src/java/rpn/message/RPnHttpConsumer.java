/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.message;

import javax.naming.*;
import javax.jms.*;
import java.io.*;
import java.net.*;
import rpn.parser.*;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 *
 * @author mvera
 */
public class RPnHttpConsumer  {

    public static boolean end = false;
    public static QueueConnection queueConnection = null;
    public static QueueReceiver receiver = null;
    public static QueueConnectionFactory cf = null;
    public static javax.jms.Queue queue = null;



    public static void startsListening() {
           
        try {


            while (!end) {         

                RPnNetworkDialog.infoText.append("Will now hit RPn Mediator URL..." + '\n');

		URL rpnMediatorURL = new URL("http://heitor:8080/rpnmediatorproxy/rpnmediatorproxy?REQ_ID=RPN_POLL");

		URLConnection rpnMediatorConn = rpnMediatorURL.openConnection();


		BufferedReader buffReader = new BufferedReader(new InputStreamReader(rpnMediatorConn.getInputStream()));

		String text;
		StringBuffer fullText = new StringBuffer();
		Boolean buffFlag = false;
		while ((text = buffReader.readLine()) != null) {
			buffFlag = true;
			fullText.append(text);
		}

		if (buffFlag) {

                	// parses the stream...
			RPnCommandModule.init(XMLReaderFactory.createXMLReader(), new StringBufferInputStream(fullText.toString()));


             	   	// updates the PhaseSpace Frames...
			rpn.RPnPhaseSpaceFrame[] framesList = rpn.RPnUIFrame.getPhaseSpaceFrames();

			for (int i = 0; i < framesList.length; i++) {

               		      framesList[i].invalidate();
               		      framesList[i].repaint();
			}

               	 	// this is for not bringing JBoss down !!!
               	 	Thread.sleep((long)500);
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
