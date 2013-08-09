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

        if (RPnNetworkStatus.instance().isFirewalled()) {

            RPnNetworkStatus.instance().log("WARN : a Http Polling context will be started...");
            subscriber_ = new RPnHttpPoller(topicName,new RPnSubscriber());
        }

        else
            subscriber_ = new RPnSubscriber(topicName);



    }

    public RPnSubscriberThread(RPnSubscriber subscriber) {

        subscriber_ = subscriber;
    }


    public void run() {

        subscriber_.subscribe();
    }

    public void unsubscribe() {

        subscriber_.unsubscribe();
    }
}

