package rpn.controller.phasespace.riemannprofile;

import rpn.command.RiemannProfileCommand;
import rpn.component.RpGeometry;
import rpn.component.WaveCurveGeom;
import rpn.component.util.GraphicsUtil;
import rpn.controller.ui.UIController;
import rpnumerics.Orbit;
import rpnumerics.WaveCurve;

public class RiemannProfileWaitState implements RiemannProfileState {

    public RiemannProfileWaitState() {

    }

    public void add(RpGeometry geom) {

        if (UIController.instance().getState() instanceof RiemannProfileCommand) {
            if (geom instanceof WaveCurveGeom) {

                WaveCurveGeom waveCurve = (WaveCurveGeom) geom;

                WaveCurve firstWaveCurveSource = (WaveCurve) geom.geomFactory().geomSource();

                if (firstWaveCurveSource.getDirection() == Orbit.FORWARD_DIR && firstWaveCurveSource.getFamily() == 0) {

                    FirstWaveCurveReady firstWaveCurveState = new FirstWaveCurveReady(waveCurve);
                    RiemannProfileCommand.instance().changeState(firstWaveCurveState);

                }
            }
        }

    }

    public void remove(RpGeometry geom) {

    }

    public void select(GraphicsUtil area) {

    }
}
