package rpn.component;

import java.awt.Color;
import rpnumerics.CompositeCurve;
import wave.multid.*;
import wave.multid.model.MultiPolyLine;
import wave.multid.view.*;

public class CompositeGeom extends MultiPolyLine implements RpGeometry {

    public static ViewingAttr VIEWING_ATTR = new ViewingAttr(Color.green);
    private RpGeomFactory factory_;

    public CompositeGeom(CoordsArray[] coordsArray, CompositeGeomFactory factory) {
        super(coordsArray, factory.selectViewingAttr());
        factory_ = factory;
    }

    public RpGeomFactory geomFactory() {
        return factory_;
    }

    @Override
    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {
        return new CompositeOrbitView(this, transf, viewingAttr());

    }
}
