/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.message;

import javax.jms.*;

public class RPnConsumerThread extends Thread {

    private RPnMessageListener consumer_ = null;


    public RPnConsumerThread(String queueName) {

        consumer_ = new RPnConsumer(queueName);
    }

    public RPnConsumerThread(String queueName,boolean persistent) {

        if (persistent)
            consumer_ = new RPnPersistentConsumer(queueName);
        else
            consumer_ = new RPnConsumer(queueName);
    }

    public void run() {

        
        consumer_.startsListening();
        
    }

    public void stopsListening() {
        consumer_.stopsListening();
    }
}
