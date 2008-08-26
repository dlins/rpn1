package rpn.component;

import java.awt.Color;
import rpnumerics.BifurcationCurve;
import wave.multid.*;
import wave.multid.model.MultiPolyLine;
import wave.multid.view.*;

public class BifurcationCurveGeom extends MultiPolyLine implements RpGeometry {

    private RpGeomFactory factory_;
    public static ViewingAttr VIEWING_ATTR = new ViewingAttr(Color.white);

    public BifurcationCurveGeom(BifurcationCurve curve, BifurcationCurveGeomFactory factory) {

        super(MultidAdapter.converseRPnCurveToCoordsArray(curve), VIEWING_ATTR);
        factory_ = factory;

    }

   
    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {
        return new RarefactionOrbitView(this, transf, viewingAttr());



    }

    public RpGeomFactory geomFactory() {

        return factory_;

    }
}
