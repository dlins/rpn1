package rpn.component;

import java.util.ArrayList;
import java.util.List;
import wave.multid.*;
import wave.multid.view.*;

public class WaveCurveGeom extends WaveCurveOrbitGeom implements WaveCurveBranchGeom {

    private List<WaveCurveBranchGeom> childWCBGeom;

    public WaveCurveGeom(CoordsArray[] vertices, WaveCurveGeomFactory factory) {
        super(vertices, factory);

        childWCBGeom = new ArrayList<WaveCurveBranchGeom>();

    }

    public List<WaveCurveBranchGeom> getOrbitGeom() {
        return childWCBGeom;
    }

    public void add(WaveCurveBranchGeom bGeom) {
        childWCBGeom.add(bGeom);
    }

    public void remove(WaveCurveBranchGeom bGeom) {
        childWCBGeom.remove(bGeom);
    }

    @Override
    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {

        return new WaveCurveView(this, transf, viewingAttr());

    }


}
