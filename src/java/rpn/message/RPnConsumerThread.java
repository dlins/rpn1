/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.message;

/**
 *
 * @author mvera
 */
public class RPnConsumerThread extends Thread {

    private String queueName_;


    public RPnConsumerThread(String queueName) {

        queueName_ = queueName.toString();
    }

    public void run() {

        RPnConsumer.init(queueName_);
        RPnConsumer.startsListening();
    }
}
