package rpn.component;

import java.util.ArrayList;
import java.util.List;
import wave.multid.*;
import wave.multid.view.*;

public class WaveCurveOrbitGeom extends OrbitGeom implements WaveCurveBranchGeom {




    public WaveCurveOrbitGeom(CoordsArray[] vertices,WaveCurveOrbitGeomFactory factory) {
        super(vertices, factory);



    }

    @Override
    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {
        return new WaveCurveOrbitGeomView(this, transf,viewingAttr());

    }

    public List<WaveCurveBranchGeom> getOrbitGeom() {
        ArrayList<WaveCurveBranchGeom> list = new ArrayList<WaveCurveBranchGeom>();
        list.add(this);
        return list;
    }


   
}
