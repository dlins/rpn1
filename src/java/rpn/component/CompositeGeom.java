package rpn.component;

import wave.multid.*;
import wave.multid.view.*;

public class CompositeGeom extends WaveCurveOrbitGeom implements RpGeometry {

    public CompositeGeom(CoordsArray[] coordsArray, WaveCurveOrbitGeomFactory factory) {
        super(coordsArray, factory);
       
    }

    @Override
    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {
        return new CompositeOrbitView(this, transf,  viewingAttr());

    }
}
