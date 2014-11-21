/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpn.controller.phasespace;

import rpn.RPnPhaseSpaceAbstraction;
import rpn.command.BoundaryExtensionCurveCommand;
import rpn.command.BuckleyLeverettiInflectionCommand;
import rpn.command.ChangeFluxParamsCommand;
import rpn.command.CoincidenceExtensionCurvePlotAgent;
import rpn.command.CoincidencePlotCommand;
import rpn.command.DoubleContactCommand;
import rpn.command.EllipticBoundaryExtensionCommand;
import rpn.command.EnvelopeCurveCommand;
import rpn.command.HysteresisPlotCommand;
import rpn.command.ImageSelectionCommand;
import rpn.command.InflectionPlotCommand;
import rpn.command.RiemannProfileCommand;
import rpn.command.RiemannResetCommand;
import rpn.command.SecondaryBifurcationCurveCommand;
import rpn.command.SubInflectionExtensionCurveCommand;
import rpn.command.SubInflectionPlotCommand;
import rpn.component.RpGeometry;
import rpn.component.WaveCurveGeom;
import wave.util.RealVector;

/**
 *
 * @author edsonlan
 */
public class RiemanProfileState implements PhaseSpaceState {

    public RiemanProfileState() {

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
        ImageSelectionCommand.instance().setEnabled(false);


        RiemannResetCommand.instance().execute();

    }

    @Override
    public void plot(RPnPhaseSpaceAbstraction phaseSpace, RpGeometry geom) {

        phaseSpace.join(geom);
        RiemannProfileCommand.instance().getState().add((WaveCurveGeom) geom);

    }

    @Override
    public void delete(RPnPhaseSpaceAbstraction phaseSpace, RpGeometry geom) {

    }

    @Override
    public void select(RPnPhaseSpaceAbstraction phaseSpace, RealVector coords) {

    }

}
