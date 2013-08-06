/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rpn.message;

/**
 *
 * @author mvera
 */
public interface RPnMessageListener {

    void parseMessageText(String text);
    String listeningName();
    void startsListening();
    void stopsListening();

}
