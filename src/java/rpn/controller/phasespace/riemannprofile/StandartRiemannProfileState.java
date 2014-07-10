package rpn.controller.phasespace.riemannprofile;

import rpn.command.RiemannProfileCommand;
import rpn.component.DiagramGeom;
import rpn.component.RpDiagramFactory;
import rpn.component.RpGeometry;
import rpn.component.WaveCurveGeom;
import rpn.component.util.AreaSelected;
import rpn.component.util.GraphicsUtil;
import rpnumerics.Area;
import rpnumerics.RiemannProfileCalc;
import rpnumerics.WaveCurve;

public class StandartRiemannProfileState implements RiemannProfileState, RiemannProfileReady {

    private GraphicsUtil area_;
    private final WaveCurveGeom firstWaveCurve_;
    private final WaveCurveGeom secondWaveCurve_;

    public StandartRiemannProfileState(WaveCurveGeom firstWaveCurve, WaveCurveGeom secondWaveCurve, GraphicsUtil area) {

        firstWaveCurve_ = firstWaveCurve;
        secondWaveCurve_ = secondWaveCurve;
        RiemannProfileCommand.instance().setEnabled(true);
        area_ = area;

    }

    @Override
    public void add(RpGeometry geom) {

    }

    public void remove(RpGeometry geom) {

    }

    public void select(GraphicsUtil area) {

        area_ = area;

    }

    public WaveCurveGeom getFirstWaveCurve() {
        return firstWaveCurve_;
    }

    public WaveCurveGeom getSecondWaveCurve() {
        return secondWaveCurve_;
    }

    public GraphicsUtil getSelection() {
        return area_;
    }

    public DiagramGeom calcProfile() {

        WaveCurve firstWaveCurve = (WaveCurve) getFirstWaveCurve().geomFactory().geomSource();
        WaveCurve secondWaveCurve = (WaveCurve) getSecondWaveCurve().geomFactory().geomSource();

        AreaSelected selection = (AreaSelected) getSelection();

        int[] waveCurvesID = new int[2];
        waveCurvesID[0] = firstWaveCurve.getId();
        waveCurvesID[1] = secondWaveCurve.getId();
        RiemannProfileCalc rc = new RiemannProfileCalc(new Area(selection), waveCurvesID);

        RpDiagramFactory factory = new RpDiagramFactory(rc);
        DiagramGeom geom = (DiagramGeom) factory.geom();

        return geom;

    }

}
