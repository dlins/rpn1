/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.controller.ui;

import java.util.ArrayList;
import java.util.Set;
import rpn.usecase.BifurcationPlotAgent;
import rpn.usecase.BuckleyLeverettiInflectionAgent;
import rpn.usecase.ChangeFluxParamsAgent;
import rpn.usecase.CoincidenceExtensionCurvePlotAgent;
import rpn.usecase.CoincidencePlotAgent;
import rpn.usecase.DoubleContactAgent;
import rpn.usecase.BoundaryExtensionCurveAgent;
import rpn.usecase.HysteresisPlotAgent;
import rpn.usecase.InflectionPlotAgent;
import rpn.usecase.RpModelActionAgent;
import rpn.usecase.SubInflectionExtensionCurveAgent;
import rpn.usecase.SubInflectionPlotAgent;
import rpnumerics.RPNUMERICS;
import wave.util.RealVector;

public class BIFURCATION_CONFIG extends UI_ACTION_SELECTED {

    public BIFURCATION_CONFIG() {

        super(BifurcationPlotAgent.instance());
        CoincidencePlotAgent.instance().setEnabled(true);
        SubInflectionPlotAgent.instance().setEnabled(true);
        BuckleyLeverettiInflectionAgent.instance().setEnabled(true);
        DoubleContactAgent.instance().setEnabled(true);
        BoundaryExtensionCurveAgent.instance().setEnabled(true);
        SubInflectionExtensionCurveAgent.instance().setEnabled(true);
        CoincidenceExtensionCurvePlotAgent.instance().setEnabled(true);
        ChangeFluxParamsAgent.instance().setEnabled(true);
        InflectionPlotAgent.instance().setEnabled(true);
        HysteresisPlotAgent.instance().setEnabled(true);




    }

   

    @Override
    public void userInputComplete(rpn.controller.ui.UIController ui,
            RealVector userInput) {
        System.out.println("chamando userinput complete de bifurcation config");
    }
//    @Override
//     public int actionDimension(){
//         return rpnumerics.RPNUMERICS.domainDim() * 2;
//     }
}
