/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.controller.ui;

import rpn.command.BifurcationPlotCommand;
import rpn.command.BuckleyLeverettiInflectionCommand;
import rpn.command.ChangeFluxParamsCommand;
import rpn.command.CoincidenceExtensionCurvePlotAgent;
import rpn.command.CoincidencePlotCommand;
import rpn.command.DoubleContactCommand;
import rpn.command.BoundaryExtensionCurveCommand;
import rpn.command.EllipticBoundaryExtensionCommand;
import rpn.command.EnvelopeCurveCommand;
import rpn.command.HysteresisPlotCommand;
import rpn.command.InflectionPlotCommand;
import rpn.command.SecondaryBifurcationCurveCommand;

import rpn.command.SubInflectionExtensionCurveCommand;
import rpn.command.SubInflectionPlotCommand;

import wave.util.RealVector;

public class ExtensionCurvesConfig extends UI_ACTION_SELECTED {

    public ExtensionCurvesConfig() {

        super(BifurcationPlotCommand.instance());
        CoincidencePlotCommand.instance().setEnabled(true);
        SubInflectionPlotCommand.instance().setEnabled(true);
        BuckleyLeverettiInflectionCommand.instance().setEnabled(true);
        DoubleContactCommand.instance().setEnabled(true);
        BoundaryExtensionCurveCommand.instance().setEnabled(true);
        SubInflectionExtensionCurveCommand.instance().setEnabled(true);
        CoincidenceExtensionCurvePlotAgent.instance().setEnabled(true);
        ChangeFluxParamsCommand.instance().setEnabled(true);
        InflectionPlotCommand.instance().setEnabled(true);
        HysteresisPlotCommand.instance().setEnabled(true);
        EllipticBoundaryExtensionCommand.instance().setEnabled(true);
        EnvelopeCurveCommand.instance().setEnabled(true);
        SecondaryBifurcationCurveCommand.instance().setEnabled(true);
        
//        GenericExtensionCurveCommand.instance().setState(new ExtensionCalcWaitState());

    }

    @Override
    public void userInputComplete(rpn.controller.ui.UIController ui,
            RealVector userInput) {

    }

}
