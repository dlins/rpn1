/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.message;

import javax.jms.*;

public class RPnConsumerThread extends Thread {

    private RPnConsumer consumer_ = null;


    public RPnConsumerThread(String queueName) {

        consumer_ = new RPnConsumer(queueName,Session.AUTO_ACKNOWLEDGE);
    }

    public void run() {

        
        consumer_.startsListening();
        
    }
}
