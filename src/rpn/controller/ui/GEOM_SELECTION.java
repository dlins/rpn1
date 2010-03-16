/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.controller.ui;

import wave.util.RealVector;
import java.util.ArrayList;
import java.util.List;

public class GEOM_SELECTION implements UserInputHandler {
    //
    // Members
    //
    private List userInputList_;

    public GEOM_SELECTION() {
        userInputList_ = new ArrayList();
    }

    public RealVector[] userInputList(UIController ui) {
        return UIController.inputConvertion(userInputList_);
    }

    public void userInputComplete(UIController ui, RealVector userInput) {
        // worst case scenario it is XZeroGeom (Homoclinics connection case)
        rpn.parser.RPnDataModule.PHASESPACE.select(userInput);
        ui.panelsBufferClear();
    }
}
