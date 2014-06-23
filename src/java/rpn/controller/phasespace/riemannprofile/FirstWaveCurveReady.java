package rpn.controller.phasespace.riemannprofile;

import rpn.RPnPhaseSpaceAbstraction;
import rpn.component.RpGeometry;
import rpn.component.WaveCurveGeom;
import wave.util.RealVector;

public class FirstWaveCurveReady extends RiemannProfileState {

    private WaveCurveGeom firstWaveCurve_;

    public FirstWaveCurveReady(WaveCurveGeom firstWaveCurve) {
        firstWaveCurve_ = firstWaveCurve;
    }

    @Override
    public void plot(RPnPhaseSpaceAbstraction phaseSpace, RpGeometry geom) {
    
        phaseSpace.join(geom);
    
        if (geom instanceof WaveCurveGeom) {

            WaveCurveGeom secondWaveCurveGeom = (WaveCurveGeom) geom;
            
            
            

            SecondWaveCurveReady secondWaveCurveReady = new SecondWaveCurveReady(secondWaveCurveGeom);
            
            phaseSpace.changeState(secondWaveCurveReady);

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
    
    
     public WaveCurveGeom getFirstWaveCurve() {
        return firstWaveCurve_;
     }
    
    
    
    

}
