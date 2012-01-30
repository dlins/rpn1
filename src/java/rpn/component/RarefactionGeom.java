package rpn.component;

import wave.multid.*;
import wave.multid.view.*;

public class RarefactionGeom extends OrbitGeom implements RpGeometry {


    public RarefactionGeom(CoordsArray[] vertices, RarefactionOrbitGeomFactory factory) {
        super(vertices, factory);

    }

    @Override
    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {
        return new RarefactionOrbitView(this, transf,viewingAttr());

    }


   
}
