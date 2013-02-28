/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.controller.ui;

import rpn.command.AreaSelectionCommand;
import rpn.command.ChangeFluxParamsCommand;
import rpn.command.ChangeDirectionCommand;
import rpn.command.ChangeOrbitLevelCommand;
import rpn.command.CompositePlotCommand;
import rpn.command.CurveRefineCommand;
import rpn.command.EllipticBoundaryCommand;
import rpn.command.HugoniotPlotCommand;
import rpn.command.IntegralCurvePlotCommand;
import rpn.command.LevelCurvePlotCommand;
import rpn.command.PhysicalBoundaryPlotCommand;
import rpn.command.PointLevelCurvePlotCommand;
import rpn.command.RarefactionExtensionCurvePlotCommand;
import rpn.command.RarefactionCurvePlotCommand;
import rpn.command.RiemannProfileCommand;
import rpn.command.ShockCurvePlotCommand;
import rpn.command.WaveCurvePlotCommand;
import wave.util.RealVector;

public class RAREFACTION_CONFIG extends UI_ACTION_SELECTED {

    public RAREFACTION_CONFIG() {

        super(ChangeDirectionCommand.instance());//Dummy

        RarefactionCurvePlotCommand.instance().setEnabled(true);
        HugoniotPlotCommand.instance().setEnabled(true);
        ShockCurvePlotCommand.instance().setEnabled(true);
        ChangeDirectionCommand.instance().setEnabled(true);
        ChangeFluxParamsCommand.instance().setEnabled(true);
        RarefactionExtensionCurvePlotCommand.instance().setEnabled(true);
        IntegralCurvePlotCommand.instance().setEnabled(true);
        AreaSelectionCommand.instance().setEnabled(false);
        CompositePlotCommand.instance().setEnabled(true);
        PointLevelCurvePlotCommand.instance().setEnabled(true);
        LevelCurvePlotCommand.instance().setEnabled(true);
        ChangeOrbitLevelCommand.instance().setEnabled(true);
        EllipticBoundaryCommand.instance().setEnabled(true);
        PhysicalBoundaryPlotCommand.instance().setEnabled(true);
        RiemannProfileCommand.instance().setEnabled(false);
        WaveCurvePlotCommand.instance().setEnabled(true);
        CurveRefineCommand.instance().setEnabled(false);

       



    }

  

    @Override
    public void userInputComplete(rpn.controller.ui.UIController ui,
            RealVector userInput) {

        ui.setState(new GEOM_SELECTION());

    }
}
