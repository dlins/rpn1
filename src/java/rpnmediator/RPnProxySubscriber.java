/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnmediator;

import rpn.message.RPnSubscriber;



import java.util.*;


public class RPnProxySubscriber extends RPnSubscriber {


    private HashMap subsDatalog_;


    public RPnProxySubscriber(String topicName,HashMap subsDatalog)  {

        super(topicName,true);
        subsDatalog_ = subsDatalog;
    }



    public void parseMessageText(String text) {


        System.out.println("Subscriber Proxy will parse msgs now..." + text);
        Set entries = subsDatalog_.entrySet();

        Iterator it = entries.iterator();
        while (it.hasNext()) {

            System.out.println("Map for subs has entries...");
            Map.Entry entry = (Map.Entry) it.next();
            Vector data = (Vector) entry.getValue();
            data.add(text);



        }
    }
}