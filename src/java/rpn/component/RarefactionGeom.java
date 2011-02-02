package rpn.component;

import java.awt.Color;
import rpnumerics.RarefactionOrbit;
import wave.multid.*;
import wave.multid.model.MultiPolyLine;
import wave.multid.view.*;

public class RarefactionGeom extends MultiPolyLine implements RpGeometry {

    private RpGeomFactory factory_;

    public RarefactionGeom(CoordsArray[] vertices, RarefactionOrbitGeomFactory factory) {
        super(vertices, selectViewingAttr(factory));
        factory_ = factory;
    }

    @Override
    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {
        return new RarefactionOrbitView(this, transf, viewingAttr());

    }

    public RpGeomFactory geomFactory() {
        return factory_;
    }

    private static ViewingAttr selectViewingAttr(RarefactionOrbitGeomFactory factory) {

        int family = (((RarefactionOrbit) factory.geomSource()).getFamilyIndex());
        if (family == 1) {
            return new ViewingAttr(Color.red);
        }
        if (family == 0) {
            return new ViewingAttr(Color.blue);
        }


        return new ViewingAttr(Color.white);

    }
}
