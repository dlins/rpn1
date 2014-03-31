/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.controller.ui;

import rpn.command.AnnotationSelectionCommand;
import rpn.command.DomainSelectionCommand;
import rpn.command.BoundaryExtensionCurveCommand;
import rpn.command.BuckleyLeverettiInflectionCommand;
import rpn.command.ChangeFluxParamsCommand;
import rpn.command.ChangeDirectionCommand;
import rpn.command.ChangeOrbitLevelCommand;
import rpn.command.ChooseAreaCommand;
import rpn.command.CoincidenceExtensionCurvePlotAgent;
import rpn.command.CoincidencePlotCommand;
import rpn.command.CurveSelectionCommand;
import rpn.command.CompositePlotCommand;
import rpn.command.CurveRefineCommand;
import rpn.command.DerivativeDiscriminantLevelCurvePlotCommand;
import rpn.command.DiscriminantLevelCurvePlotCommand;
import rpn.command.DiscriminantPointLevelCurvePlotCommand;
import rpn.command.DoubleContactCommand;
import rpn.command.EllipticBoundaryCommand;
import rpn.command.EllipticBoundaryExtensionCommand;
import rpn.command.EnvelopeCurveCommand;
import rpn.command.ImageSelectionCommand;
import rpn.command.HugoniotContinuationPlotCommand;
import rpn.command.HugoniotPlotCommand;
import rpn.command.HysteresisPlotCommand;
import rpn.command.InflectionPlotCommand;
import rpn.command.IntegralCurvePlotCommand;
import rpn.command.LevelCurvePlotCommand;
import rpn.command.PhysicalBoundaryPlotCommand;
import rpn.command.PointLevelCurvePlotCommand;
import rpn.command.RarefactionExtensionCurvePlotCommand;
import rpn.command.RarefactionCurvePlotCommand;
import rpn.command.RiemannProfileCommand;
import rpn.command.SecondaryBifurcationCurveCommand;
import rpn.command.ShockCurvePlotCommand;
import rpn.command.SubInflectionExtensionCurveCommand;
import rpn.command.SubInflectionPlotCommand;
import rpn.command.WaveCurvePlotCommand;
import rpn.command.ZoomPlotCommand;
import rpn.command.ZoomingAreaCommand;
import wave.util.RealVector;

public class CurvesConfig extends UI_ACTION_SELECTED {

    public CurvesConfig() {

        super(ChangeDirectionCommand.instance());//Dummy

        RarefactionCurvePlotCommand.instance().setEnabled(true);
        HugoniotPlotCommand.instance().setEnabled(true);
        ShockCurvePlotCommand.instance().setEnabled(true);
        HugoniotContinuationPlotCommand.instance().setEnabled(true);
        ChangeDirectionCommand.instance().setEnabled(true);
        ChangeFluxParamsCommand.instance().setEnabled(true);
        RarefactionExtensionCurvePlotCommand.instance().setEnabled(true);
        IntegralCurvePlotCommand.instance().setEnabled(true);
        DomainSelectionCommand.instance().setEnabled(true);
        CompositePlotCommand.instance().setEnabled(true);
        PointLevelCurvePlotCommand.instance().setEnabled(true);
        DiscriminantLevelCurvePlotCommand.instance().setEnabled(true);
        DiscriminantPointLevelCurvePlotCommand.instance().setEnabled(true);
        DerivativeDiscriminantLevelCurvePlotCommand.instance().setEnabled(true);
        LevelCurvePlotCommand.instance().setEnabled(true);
        ChangeOrbitLevelCommand.instance().setEnabled(true);
        EllipticBoundaryCommand.instance().setEnabled(true);
        PhysicalBoundaryPlotCommand.instance().setEnabled(true);
        RiemannProfileCommand.instance().setEnabled(false);
        WaveCurvePlotCommand.instance().setEnabled(true);
        CurveRefineCommand.instance().setEnabled(false);
        ChooseAreaCommand.instance().setEnabled(true);
        ImageSelectionCommand.instance().setEnabled(true);
        CurveSelectionCommand.instance().setEnabled(true);
       
        ZoomPlotCommand.instance().setEnabled(true);
        ZoomingAreaCommand.instance().setEnabled(true);
        AnnotationSelectionCommand.instance().setEnabled(true);
        
        
        
        
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
        



    }

  

    @Override
    public void userInputComplete(rpn.controller.ui.UIController ui,
            RealVector userInput) {

        ui.setState(new GEOM_SELECTION());

    }
}
