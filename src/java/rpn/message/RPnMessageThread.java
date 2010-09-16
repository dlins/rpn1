/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.message;

import javax.jms.*;

/**
 *
 * <p>A thread class used to send new messages </p>
 *
 */

public class RPnMessageThread extends Thread {


    QueueSender sender_;

    Message msg_;

    public RPnMessageThread (QueueSender sender , Message msg){

	sender_=sender;
	msg_=msg;

    }

    public void run(){
	try{

	sender_.send(msg_);

	}

	catch (javax.jms.JMSException ex ){
	    System.out.println ("Erro no envio da mensagem:Erro provocado por JMS Exception");
	    System.out.println (ex);

	}


        catch (NullPointerException ex){

            ex.printStackTrace();

            if (sender_==null) System.out.println("Sender eh nulo em Message Thread");
            if (msg_==null) System.out.println("Message eh nulo em Message Thread");

        }


    }

























}
