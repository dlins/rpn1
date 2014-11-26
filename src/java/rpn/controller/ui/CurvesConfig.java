/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.controller.ui;

import rpn.command.AnnotationSelectionCommand;
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
import rpn.command.HugoniotPlotCommand;
import rpn.command.HysteresisPlotCommand;
import rpn.command.InflectionPlotCommand;
import rpn.command.IntegralCurvePlotCommand;
import rpn.command.LevelCurvePlotCommand;
import rpn.command.PhysicalBoundaryPlotCommand;
import rpn.command.PointLevelCurvePlotCommand;
import rpn.command.RarefactionExtensionCurvePlotCommand;
import rpn.command.RarefactionCurvePlotCommand;
import rpn.command.ReferencePointSelectionCommand;
import rpn.command.RiemannResetCommand;
import rpn.command.RiemannProfileCommand;
import rpn.command.SecondaryBifurcationCurveCommand;
import rpn.command.ShockCurvePlotCommand;
import rpn.command.SubInflectionExtensionCurveCommand;
import rpn.command.SubInflectionPlotCommand;
import rpn.command.TransitionalLinePlotCommand;
import rpn.command.WaveCurvePlotCommand;
import rpn.command.WaveCurveRRegionsPlotCommand;
import rpn.command.ZoomPlotCommand;
import rpn.command.ZoomingAreaCommand;
import wave.util.RealVector;

public class CurvesConfig extends UI_ACTION_SELECTED {

    public CurvesConfig() {

        super(ChangeDirectionCommand.instance());//Dummy

        RarefactionCurvePlotCommand.instance().setEnabled(true);
        HugoniotPlotCommand.instance().setEnabled(true);
        ShockCurvePlotCommand.instance().setEnabled(true);
        ChangeDirectionCommand.instance().setEnabled(true);
        ChangeFluxParamsCommand.instance().setEnabled(true);
        RarefactionExtensionCurvePlotCommand.instance().setEnabled(false);
        IntegralCurvePlotCommand.instance().setEnabled(false);
        CompositePlotCommand.instance().setEnabled(true);
        PointLevelCurvePlotCommand.instance().setEnabled(true);
        DiscriminantLevelCurvePlotCommand.instance().setEnabled(false);
        DiscriminantPointLevelCurvePlotCommand.instance().setEnabled(false);
        DerivativeDiscriminantLevelCurvePlotCommand.instance().setEnabled(false);
        LevelCurvePlotCommand.instance().setEnabled(true);
        ChangeOrbitLevelCommand.instance().setEnabled(true);
        EllipticBoundaryCommand.instance().setEnabled(true);
        PhysicalBoundaryPlotCommand.instance().setEnabled(false);
        RiemannProfileCommand.instance().setEnabled(false);
        WaveCurvePlotCommand.instance().setEnabled(true);
        WaveCurveRRegionsPlotCommand.instance().setEnabled(false);
        CurveRefineCommand.instance().setEnabled(false);
        ChooseAreaCommand.instance().setEnabled(true);

        CurveSelectionCommand.instance().setEnabled(true);

        
        ZoomPlotCommand.instance().setEnabled(true);
        ZoomingAreaCommand.instance().setEnabled(true);
        AnnotationSelectionCommand.instance().setEnabled(true);
 
        
        ReferencePointSelectionCommand.instance().setEnabled(true);
                
        
        CoincidencePlotCommand.instance().setEnabled(false);
        SubInflectionPlotCommand.instance().setEnabled(false);
        BuckleyLeverettiInflectionCommand.instance().setEnabled(false);
        DoubleContactCommand.instance().setEnabled(true);
        BoundaryExtensionCurveCommand.instance().setEnabled(true);
        SubInflectionExtensionCurveCommand.instance().setEnabled(false);
        CoincidenceExtensionCurvePlotAgent.instance().setEnabled(false);
        ChangeFluxParamsCommand.instance().setEnabled(true);
        InflectionPlotCommand.instance().setEnabled(true);
        HysteresisPlotCommand.instance().setEnabled(true);
        EllipticBoundaryExtensionCommand.instance().setEnabled(true);
        EnvelopeCurveCommand.instance().setEnabled(false);
        SecondaryBifurcationCurveCommand.instance().setEnabled(true);

        
        
        if(rpnumerics.RPNUMERICS.getTransisionalLinesNames().isEmpty()){
            TransitionalLinePlotCommand.instance().setEnabled(false);            
        }
        else {
            TransitionalLinePlotCommand.instance().setEnabled(true);
        }

        
        
        RiemannResetCommand.instance().setEnabled(true);


    }

  

    @Override
    public void userInputComplete(rpn.controller.ui.UIController ui,
            RealVector userInput) {

        ui.setState(new GEOM_SELECTION());

    }
}
