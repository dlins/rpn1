/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.controller.ui;

import rpn.usecase.BackwardShockCurvePlotAgent;
import rpn.usecase.ChangeFluxParamsAgent;
import rpn.usecase.ChangeDirectionAgent;
import rpn.usecase.ForwardShockCurvePlotAgent;
import rpn.usecase.HugoniotPlotAgent;
import rpn.usecase.RarefactionOrbitPlotAgent;
import wave.util.RealVector;

public class RAREFACTION_CONFIG extends UI_ACTION_SELECTED {

    public RAREFACTION_CONFIG() {

        super(ChangeDirectionAgent.instance());//Dummy 

        RarefactionOrbitPlotAgent.instance().setEnabled(true);
        HugoniotPlotAgent.instance().setEnabled(true);
        BackwardShockCurvePlotAgent.instance().setEnabled(true);
        ForwardShockCurvePlotAgent.instance().setEnabled(true);
        ChangeDirectionAgent.instance().setEnabled(true);
        ChangeFluxParamsAgent.instance().setEnabled(true);



    }

    @Override
    public void userInputComplete(rpn.controller.ui.UIController ui,
            RealVector userInput) {

        ui.setState(new GEOM_SELECTION());

    }
}
