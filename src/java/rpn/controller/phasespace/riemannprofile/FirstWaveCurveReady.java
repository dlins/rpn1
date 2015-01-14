package rpn.controller.phasespace.riemannprofile;

import rpn.command.RiemannProfileCommand;
import rpn.component.RpGeometry;
import rpn.component.WaveCurveGeom;
import rpn.component.util.GraphicsUtil;
import rpn.controller.ui.UIController;
import rpnumerics.Orbit;
import rpnumerics.WaveCurve;

public class FirstWaveCurveReady implements RiemannProfileState {

    private final WaveCurveGeom firstWaveCurve_;

    public FirstWaveCurveReady(WaveCurveGeom firstWaveCurve) {
        firstWaveCurve_ = firstWaveCurve;
    }

    @Override
    public void add(rpn.component.WaveCurveGeom geom) {

        WaveCurveGeom secondWaveCurve = (WaveCurveGeom) geom;

        WaveCurve secondWaveCurveSource = (WaveCurve) secondWaveCurve.geomFactory().geomSource();

        if (secondWaveCurveSource.getDirection() == Orbit.BACKWARD_DIR && secondWaveCurveSource.getFamily() == 1) {
            SecondWaveCurveReady secondWaveCurveReady = new SecondWaveCurveReady(firstWaveCurve_, secondWaveCurve);

            RiemannProfileCommand.instance().changeState(secondWaveCurveReady);

        }

        if (secondWaveCurveSource.getDirection() == Orbit.FORWARD_DIR && secondWaveCurveSource.getFamily() == 1) {
            AllIncreaseRimannProfileState allIncreaseState = new AllIncreaseRimannProfileState(firstWaveCurve_, secondWaveCurve);
            secondWaveCurve.setSelected(true);
            firstWaveCurve_.setSelected(false);
            UIController.instance().getSelectedGeometriesList().remove(firstWaveCurve_);
            UIController.instance().getSelectedGeometriesList().add(secondWaveCurve);

            RiemannProfileCommand.instance().changeState(allIncreaseState);

        }

    }

    public WaveCurveGeom getFirstWaveCurve() {
        return firstWaveCurve_;
    }

    public void remove(RpGeometry geom) {

        if (geom == firstWaveCurve_) {
            RiemannProfileCommand.instance().changeState(new RiemannProfileWaitState());
        }

    }

    public void select(GraphicsUtil area) {

    }

}
