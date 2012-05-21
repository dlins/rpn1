package rpn.component;

import wave.multid.*;
import wave.multid.view.*;

public class RarefactionGeom extends WaveCurveOrbitGeom implements RpGeometry {


    public RarefactionGeom(CoordsArray[] vertices, WaveCurveOrbitGeomFactory factory) {
        super(vertices, factory);

    }

    @Override
    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {
        return new RarefactionOrbitView(this, transf,viewingAttr());

    }


   
}
