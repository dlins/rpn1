package rpn.component;

import wave.multid.*;
import wave.multid.view.*;

public class RarefactionGeom extends OrbitGeom {

    public RarefactionGeom(CoordsArray[] vertices, OrbitGeomFactory factory) {
        super(vertices, factory);

    }

    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {


        return new OrbitGeomView(this,transf,viewingAttr());


//        return new RarefactionOrbitView(this, transf, viewingAttr());

    }


}
