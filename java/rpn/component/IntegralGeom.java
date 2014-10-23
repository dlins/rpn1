package rpn.component;

import wave.multid.*;
import wave.multid.view.*;

public class IntegralGeom extends OrbitGeom implements RpGeometry {

    

    public IntegralGeom(CoordsArray[] vertices, IntegralOrbitGeomFactory factory) {
        super(vertices, factory);
        
    }

    @Override
    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {
        return new IntegralOrbitView(this, transf,   viewingAttr());

    }

   
   
}
