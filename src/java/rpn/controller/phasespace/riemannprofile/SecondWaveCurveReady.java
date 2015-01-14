package rpn.controller.phasespace.riemannprofile;

import rpn.command.DomainSelectionCommand;
import rpn.command.RiemannProfileCommand;
import rpn.component.RpGeometry;
import rpn.component.WaveCurveGeom;
import rpn.component.util.GraphicsUtil;

public class SecondWaveCurveReady extends FirstWaveCurveReady {

    private final WaveCurveGeom secondWaveCurve_;

    SecondWaveCurveReady(WaveCurveGeom firstWaveCurve_, WaveCurveGeom secondWaveCurve) {
        super(firstWaveCurve_);

        secondWaveCurve_ = secondWaveCurve;

        DomainSelectionCommand.instance().setEnabled(true);

    }

    @Override
    public void add(rpn.component.WaveCurveGeom geom) {

    }

    @Override
    public void select(GraphicsUtil area) {

        StandardRiemannProfileState standardState = new StandardRiemannProfileState(getFirstWaveCurve(), secondWaveCurve_, area);
        RiemannProfileCommand.instance().changeState(standardState);

    }

}
