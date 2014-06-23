package rpn.controller.phasespace.riemannprofile;

import rpn.RPnPhaseSpaceAbstraction;
import rpn.component.RpGeometry;
import rpn.component.WaveCurveGeom;
import rpnumerics.Orbit;
import rpnumerics.WaveCurve;
import wave.util.RealVector;

public class SecondWaveCurveReady extends FirstWaveCurveReady {

    private WaveCurveGeom secondWaveCurve_;

    public SecondWaveCurveReady(WaveCurveGeom firstWaveCurve) {
        super(firstWaveCurve);

    }

    @Override
    public void plot(RPnPhaseSpaceAbstraction phaseSpace, RpGeometry geom) {

        phaseSpace.join(geom);

        if (geom instanceof WaveCurveGeom) {

            WaveCurveGeom secondWaveCurveGeom = (WaveCurveGeom) geom;

            secondWaveCurve_ = (WaveCurveGeom) geom;

            WaveCurve firstWaveCurveSource = (WaveCurve) getFirstWaveCurve().geomFactory().geomSource();

            WaveCurve secondWaveCurveSource = (WaveCurve) secondWaveCurveGeom.geomFactory().geomSource();
            
            if(checkStandartRiemmanProfileState(firstWaveCurveSource, secondWaveCurveSource)){
                phaseSpace.changeState(new StandartRiemannProfileState(getFirstWaveCurve(),secondWaveCurve_));
                
            }



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

    public WaveCurveGeom getSecondWaveCurve() {
        return secondWaveCurve_;
    }

    private boolean checkStandartRiemmanProfileState(WaveCurve firstWaveCurve, WaveCurve secondWaveCurve) {

        int firstWaveCurveFamily = firstWaveCurve.getFamily();
        int secondWaveCurveFamily = secondWaveCurve.getFamily();

        int firstWaveCurveDirection = firstWaveCurve.getDirection();

        int secondWaveCurveDirection = secondWaveCurve.getDirection();

        if (firstWaveCurveDirection == Orbit.FORWARD_DIR && firstWaveCurveFamily == 0) {
            if (secondWaveCurveDirection == Orbit.BACKWARD_DIR
                    && secondWaveCurveFamily == 1) {
                return true;
            }

        }

        return false;
    }
}
