package rpn.component;

import java.awt.Color;
import rpnumerics.IntegralCurve;
import rpnumerics.RarefactionOrbit;
import wave.multid.*;
import wave.multid.model.MultiPolyLine;
import wave.multid.view.*;

public class IntegralGeom extends MultiPolyLine implements RpGeometry {

    private RpGeomFactory factory_;

    public IntegralGeom(CoordsArray[] vertices, IntegralOrbitGeomFactory factory) {
        super(vertices, selectViewingAttr(factory));
        factory_ = factory;
    }

    @Override
    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {
        return new IntegralOrbitView(this, transf,  viewingAttr());

    }

    public RpGeomFactory geomFactory() {
        return factory_;
    }

    private static ViewingAttr selectViewingAttr(IntegralOrbitGeomFactory factory) {

        int family = (((IntegralCurve) factory.geomSource()).getFamilyIndex());
        if (family == 1) {
            return new ViewingAttr(Color.red);
        }
        if (family == 0) {
            return new ViewingAttr(Color.blue);
        }


        return new ViewingAttr(Color.white);

    }
}
