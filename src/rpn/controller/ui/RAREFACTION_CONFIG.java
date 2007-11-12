/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.controller.ui;

import wave.util.RealVector;

import rpnumerics.RpNumerics;
import rpnumerics.PhasePoint;

import rpn.usecase.ChangeXZeroAgent;
import rpn.usecase.RarefactionForwardOrbitPlotAgent;
import rpn.usecase.RarefactionBackwardOrbitPlotAgent;


public class RAREFACTION_CONFIG extends UI_ACTION_SELECTED {

    public RAREFACTION_CONFIG() {

        super(ChangeXZeroAgent.instance());

    }

    public void userInputComplete(rpn.controller.ui.UIController ui,
                                  RealVector userInput) {

        RpNumerics.setRarefactionFlow(new PhasePoint(userInput));

        super.userInputComplete(ui, userInput);

        RarefactionForwardOrbitPlotAgent.instance().setEnabled(true);
        RarefactionBackwardOrbitPlotAgent.instance().setEnabled(true);
        ChangeXZeroAgent.instance().setEnabled(true);

        ui.setState(new GEOM_SELECTION());

    }
}
