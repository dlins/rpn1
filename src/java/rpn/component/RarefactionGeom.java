package rpn.component;

import wave.multid.*;
import wave.multid.model.MultiPolyLine;
import wave.multid.view.*;

public class RarefactionGeom extends MultiPolyLine implements RpGeometry {

    private RpGeomFactory factory_;

    public RarefactionGeom(CoordsArray[] vertices, RarefactionOrbitGeomFactory factory) {
        super(vertices, factory.selectViewingAttr());
        factory_ = factory;
    }

    @Override
    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {
        return new RarefactionOrbitView(this, transf, viewingAttr());

    }

    public RpGeomFactory geomFactory() {
        return factory_;
    }

   
}
