/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.controller.ui;

import wave.util.RealVector;

/** This interface declares basic methods to start actions after the user inputs. */


public interface UserInputHandler {

    /** When all user inputs required to do a action is done this method is invoked. */
 
    void userInputComplete(rpn.controller.ui.UIController ui, RealVector userInput);

    /** This method returns all user inputs .*/
    RealVector[] userInputList(rpn.controller.ui.UIController ui);
}
