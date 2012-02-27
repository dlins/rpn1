/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.controller.ui;

import java.util.ArrayList;
import rpn.usecase.BackwardManifoldPlotAgent;
import rpn.usecase.HugoniotPlotAgent;
import rpnumerics.PhasePoint;
import rpnumerics.RPNUMERICS;
import wave.util.RealVector;
import rpn.usecase.ChangeDirectionAgent;
import rpn.usecase.ForwardManifoldPlotAgent;
import rpn.usecase.PoincareSectionPlotAgent;
import rpn.usecase.RpModelActionAgent;
import rpn.usecase.StationaryPointPlotAgent;

public class SHOCK_CONFIG extends UI_ACTION_SELECTED {

    //
    // Constructors
    //
    public SHOCK_CONFIG() {
        super(HugoniotPlotAgent.instance());

    }

   

    //
    // Methods
    //
    @Override
    public void userInputComplete(rpn.controller.ui.UIController ui,
            RealVector userInput) {

        RPNUMERICS.getShockProfile().setXZero(new PhasePoint(userInput));

        super.userInputComplete(ui, userInput);

        ChangeDirectionAgent.instance().setEnabled(true);

        ui.setState(new SIGMA_CONFIG());
    }
}
