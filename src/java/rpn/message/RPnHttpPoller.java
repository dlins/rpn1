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
public class RPnHttpPoller {

    public static boolean end = false;

    private RPnMessageListener messageParser_ = null;


    public RPnHttpPoller(RPnMessageListener messageParser) {
        messageParser_ = messageParser;
    }


    public void startsListening() {
           
        try {


            while (!end) {         

                RPnNetworkDialog.infoText.append("Will now hit RPn Mediator URL..." + '\n');

		URL rpnMediatorURL = new URL("http://heitor:8080/rpnmediatorproxy/rpnmediatorproxy?REQ_ID=" + messageParser_.listeningName());

		URLConnection rpnMediatorConn = rpnMediatorURL.openConnection();


		BufferedReader buffReader = new BufferedReader(new InputStreamReader(rpnMediatorConn.getInputStream()));

		String text;
		StringBuffer fullText = new StringBuffer();
		Boolean buffFlag = false;
		while ((text = buffReader.readLine()) != null) {
			buffFlag = true;
			fullText.append(text);
		}

		if (buffFlag)               
                    messageParser_.parseMessageText(fullText.toString());

                // TODO ? closeConnection URL ???
                
                // this is for not bringing JBoss down !!!
                Thread.sleep((long)500);
            }

        } catch (Exception exc) {

            exc.printStackTrace();

        } 
    }

    public void stopsListening() {

        end = false;

    }
}
