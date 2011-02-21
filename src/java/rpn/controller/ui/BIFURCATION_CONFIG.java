/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.controller.ui;

import rpn.RPnCurvesConfigPanel;
import rpn.usecase.BifurcationPlotAgent;
import rpn.usecase.BuckleyLeverettiInflectionAgent;
import rpn.usecase.CoincidencePlotAgent;
import rpn.usecase.DoubleContactAgent;
import rpn.usecase.SubInflectionPlotAgent;
import wave.util.RealVector;

public class BIFURCATION_CONFIG extends UI_ACTION_SELECTED {

    public BIFURCATION_CONFIG() {

        super(BifurcationPlotAgent.instance());
        CoincidencePlotAgent.instance().setEnabled(true);
        SubInflectionPlotAgent.instance().setEnabled(true);
        BuckleyLeverettiInflectionAgent.instance().setEnabled(true);
        DoubleContactAgent.instance().setEnabled(true);




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
