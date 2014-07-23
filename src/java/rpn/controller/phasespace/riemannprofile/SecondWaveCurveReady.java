package rpn.controller.phasespace.riemannprofile;

import rpn.command.DomainSelectionCommand;
import rpn.command.RiemannProfileCommand;
import rpn.component.RpGeometry;
import rpn.component.WaveCurveGeom;
import rpn.component.util.GraphicsUtil;
import rpnumerics.Orbit;
import rpnumerics.WaveCurve;

public class SecondWaveCurveReady extends FirstWaveCurveReady {

    private final WaveCurveGeom secondWaveCurve_;

    SecondWaveCurveReady(WaveCurveGeom firstWaveCurve_, WaveCurveGeom secondWaveCurve) {
        super(firstWaveCurve_);

        secondWaveCurve_ = secondWaveCurve;

        DomainSelectionCommand.instance().setEnabled(true);

    }

    @Override
    public void add(RpGeometry geom) {

    }

    @Override
    public void select(GraphicsUtil area) {

        StandardRiemannProfileState standardState = new StandardRiemannProfileState(getFirstWaveCurve(), secondWaveCurve_, area);
        RiemannProfileCommand.instance().changeState(standardState);

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
