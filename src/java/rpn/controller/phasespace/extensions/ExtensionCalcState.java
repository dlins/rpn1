package rpn.controller.phasespace.extensions;

import java.util.List;
import rpn.component.RpGeometry;
import rpn.component.util.GraphicsUtil;
import rpnumerics.RPnCurve;
import wave.util.RealSegment;

public abstract class ExtensionCalcState {

    private List<RealSegment> segmentsToExtend_;

    public ExtensionCalcState(RpGeometry geom) {

        segmentsToExtend_ = ((RPnCurve) geom.geomFactory().geomSource()).segments();
    }

    public ExtensionCalcState(List<RealSegment> segmentsToExtend) {
        segmentsToExtend_ = segmentsToExtend;
    }

    public void setCurve(RpGeometry geom) {

        segmentsToExtend_ = ((RPnCurve) geom.geomFactory().geomSource()).segments();
    }

    public List<RealSegment> getSegmentsToExtend() {
        return segmentsToExtend_;
    }

    public abstract void select(GraphicsUtil selection);

    protected void setSegmentsToExtend(List<RealSegment> segments) {
        segmentsToExtend_ = segments;
    }

}
