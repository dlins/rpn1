/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.controller.ui;

import rpn.usecase.ChangeSigmaAgent;
import wave.util.RealVector;

public class CURVES_CONFIG extends UI_ACTION_SELECTED {

    //
    // Members
    //
    //
    // Constructors
    //
    public CURVES_CONFIG() {
        super(ChangeSigmaAgent.instance());
    }

    @Override
    public void userInputComplete(rpn.controller.ui.UIController ui, RealVector userInput) {

    }
}
