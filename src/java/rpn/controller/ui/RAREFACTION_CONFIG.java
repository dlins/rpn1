/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.controller.ui;

import java.util.ArrayList;
import rpn.usecase.AreaSelectionAgent;
import rpn.usecase.ChangeFluxParamsAgent;
import rpn.usecase.ChangeDirectionAgent;
import rpn.usecase.ChangeOrbitLevel;
import rpn.usecase.CompositePlotAgent;
import rpn.usecase.HugoniotPlotAgent;
import rpn.usecase.IntegralCurvePlotAgent;
import rpn.usecase.RarefactionExtensionCurvePlotAgent;
import rpn.usecase.RarefactionOrbitPlotAgent;
import rpn.usecase.RpModelActionAgent;
import rpn.usecase.ShockCurvePlotAgent;
import wave.util.RealVector;

public class RAREFACTION_CONFIG extends UI_ACTION_SELECTED {

    public RAREFACTION_CONFIG() {

        super(ChangeDirectionAgent.instance());//Dummy 

        RarefactionOrbitPlotAgent.instance().setEnabled(true);
        HugoniotPlotAgent.instance().setEnabled(true);
        ShockCurvePlotAgent.instance().setEnabled(true);
        ChangeDirectionAgent.instance().setEnabled(true);
        ChangeFluxParamsAgent.instance().setEnabled(true);
        RarefactionExtensionCurvePlotAgent.instance().setEnabled(true);
        IntegralCurvePlotAgent.instance().setEnabled(true);
        AreaSelectionAgent.instance().setEnabled(true);
        CompositePlotAgent.instance().setEnabled(true);
        ChangeOrbitLevel.instance().setEnabled(true);



    }

    @Override
    public ArrayList<RpModelActionAgent> getAgents() {

        ArrayList<RpModelActionAgent> returnedArray = new ArrayList<RpModelActionAgent>();

        returnedArray.add(HugoniotPlotAgent.instance());

        returnedArray.add(ShockCurvePlotAgent.instance());

        returnedArray.add(RarefactionOrbitPlotAgent.instance());

        returnedArray.add(CompositePlotAgent.instance());

        return returnedArray;
    }

    @Override
    public void userInputComplete(rpn.controller.ui.UIController ui,
            RealVector userInput) {

        ui.setState(new GEOM_SELECTION());

    }
}
