/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.controller.ui;

import rpnumerics.ShockFlow;
import rpn.controller.phasespace.*;
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

    public void userInputComplete(rpn.controller.ui.UIController ui, RealVector userInput) {
//

    }
}
