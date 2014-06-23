package rpn.controller.phasespace.riemannprofile;

import rpn.RPnPhaseSpaceAbstraction;

import rpn.component.RpGeometry;
import rpn.component.WaveCurveGeom;

import rpn.controller.phasespace.PhaseSpaceState;

import wave.util.RealVector;

public class RiemannProfileState implements PhaseSpaceState {

    @Override
    public void plot(RPnPhaseSpaceAbstraction phaseSpace, RpGeometry geom) {

        phaseSpace.join(geom);
        
        if (geom instanceof WaveCurveGeom) {

            WaveCurveGeom firstWaveCurve = (WaveCurveGeom) geom;
            
            FirstWaveCurveReady firstWaveCurveState = new FirstWaveCurveReady(firstWaveCurve);
            
            phaseSpace.changeState(firstWaveCurveState);
            

        }

    }

    @Override
    public void delete(RPnPhaseSpaceAbstraction phaseSpace, RpGeometry geom) {
        // TODO Implement this method

    }

    @Override
    public void select(RPnPhaseSpaceAbstraction phaseSpace, RealVector coords) {
        // TODO Implement this method

    }
}
