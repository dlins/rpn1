package rpn.component;

import wave.multid.*;
import wave.multid.model.MultiPolyLine;
import wave.multid.view.*;

public class IntegralGeom extends MultiPolyLine implements RpGeometry {

    private RpGeomFactory factory_;

    public IntegralGeom(CoordsArray[] vertices, IntegralOrbitGeomFactory factory) {
        super(vertices, factory.selectViewingAttr());
        factory_ = factory;
    }

    @Override
    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {
        return new IntegralOrbitView(this, transf,  viewingAttr());

    }

    public RpGeomFactory geomFactory() {
        return factory_;
    }
   
}
