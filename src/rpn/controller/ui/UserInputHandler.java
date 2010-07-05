/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.controller.ui;

import wave.util.RealVector;

/** This interface declares basic methods to start actions after the user inputs. */


public interface UserInputHandler {

    /** When a user input is done this method is invoked. */
 
    void userInputComplete(rpn.controller.ui.UIController ui, RealVector userInput);

    /** When no inputs are required to do a action this method is invoked. */

    void userInputComplete(rpn.controller.ui.UIController ui);

    /** This method returns all user inputs .*/
    RealVector[] userInputList(rpn.controller.ui.UIController ui);
}
