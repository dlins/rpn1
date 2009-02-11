/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.controller.ui;


import rpn.usecase.ChangeRarefactionXZeroAgent;
import wave.util.RealVector;
import rpn.usecase.RarefactionForwardOrbitPlotAgent;
import rpn.usecase.RarefactionBackwardOrbitPlotAgent;


public class RAREFACTION_CONFIG extends UI_ACTION_SELECTED {

    public RAREFACTION_CONFIG() {

        super(ChangeRarefactionXZeroAgent.instance());
        

    }

    @Override
    public void userInputComplete(rpn.controller.ui.UIController ui,
                                  RealVector userInput) {

//        super.userInputComplete(ui, userInput);

        RarefactionForwardOrbitPlotAgent.instance().setEnabled(true);
        RarefactionBackwardOrbitPlotAgent.instance().setEnabled(true);
        ChangeRarefactionXZeroAgent.instance().setEnabled(true);
        ui.setState(new GEOM_SELECTION());

    }
}
