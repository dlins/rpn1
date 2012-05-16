package rpn.component;

import wave.multid.*;
import wave.multid.view.*;

public class WaveCurveGeom extends OrbitGeom  {


    public WaveCurveGeom(CoordsArray[] vertices, RarefactionOrbitGeomFactory factory) {
        super(vertices, factory);

    }

    @Override
    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {
        return new WaveCurveView(this, transf,viewingAttr());

    }


   
}
