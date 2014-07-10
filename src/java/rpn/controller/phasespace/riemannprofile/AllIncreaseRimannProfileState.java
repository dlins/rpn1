package rpn.controller.phasespace.riemannprofile;

import java.util.List;
import rpn.command.PointMarkCommand;
import rpn.command.RiemannProfileCommand;
import rpn.component.DiagramGeom;
import rpn.component.RpDiagramFactory;
import rpn.component.RpGeometry;
import rpn.component.WaveCurveGeom;
import rpn.component.util.GraphicsUtil;
import rpn.component.util.Point;
import rpn.controller.ui.UIController;
import rpn.controller.ui.UI_ACTION_SELECTED;
import rpnumerics.RiemannProfileCalc;
import rpnumerics.WaveCurve;
import wave.util.RealVector;

public class AllIncreaseRimannProfileState implements RiemannProfileState, RiemannProfileReady {

    private GraphicsUtil point_;
    private final WaveCurveGeom firstWaveCurve_;
    private final WaveCurveGeom secondWaveCurve_;

    public AllIncreaseRimannProfileState(WaveCurveGeom firstWaveCurve_, WaveCurveGeom secondWaveCurve_) {
        this.firstWaveCurve_ = firstWaveCurve_;
        this.secondWaveCurve_ = secondWaveCurve_;
        RiemannProfileCommand.instance().setEnabled(true);
        UI_ACTION_SELECTED actionSelected = new UI_ACTION_SELECTED(PointMarkCommand.instance());
        UIController.instance().setState(actionSelected);


    }

    public void add(RpGeometry geom) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void remove(RpGeometry geom) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void select(GraphicsUtil point) {
        point_ = point;
    }

    public WaveCurveGeom getFirstWaveCurve() {

        return firstWaveCurve_;

    }

    public WaveCurveGeom getSecondWaveCurve() {
        return secondWaveCurve_;
    }

    public GraphicsUtil getSelection() {
        return point_;
    }

    public DiagramGeom calcProfile() {
        
        secondWaveCurve_.setSelected(false );
        firstWaveCurve_.setSelected(false);
        UIController.instance().getSelectedGeometriesList().remove(firstWaveCurve_);
        UIController.instance().getSelectedGeometriesList().remove(secondWaveCurve_);

        Point point = (Point) point_;
        List<RealVector> wcVertices = point.getWCVertices();

        RealVector pointOnWaveCurve = wcVertices.get(0);

        RiemannProfileCalc rc = new RiemannProfileCalc((WaveCurve) firstWaveCurve_.geomFactory().geomSource(),
                (WaveCurve) secondWaveCurve_.geomFactory().geomSource(), pointOnWaveCurve);

        RpDiagramFactory factory = new RpDiagramFactory(rc);
        DiagramGeom geom = (DiagramGeom) factory.geom();

        return geom;
    }
}
