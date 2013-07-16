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
public class RPnSubscriberThread extends Thread {

    private RPnSubscriber subscriber_ = null;

    public RPnSubscriberThread(String topicName) {

        subscriber_ = new RPnSubscriber(topicName);



    }

    public void run() {

        subscriber_.subscribe();
    }

    public void unsubscribe() {

        subscriber_.unsubscribe();
    }
}

