/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.component;

import wave.multid.model.MultiPolyLine;
import wave.multid.view.ViewingAttr;
import wave.multid.CoordsArray;
import java.awt.Color;
import wave.multid.view.GeomObjView;
import wave.multid.view.ViewingTransform;
import wave.multid.DimMismatchEx;

public class ShockCurveGeom extends MultiPolyLine implements RpGeometry {
    //
    // Constants
    //
    public static final int FORWARD_DIR = 1;
    public static final int BACKWARD_DIR = -1;
    public static final int BOTH_DIR =0;
    public static ViewingAttr VIEWING_ATTR = new ViewingAttr(Color.red);
    //
    // Members
    //
    private RpGeomFactory factory_;

    //
    // Constructors
    //
    public ShockCurveGeom(CoordsArray[] source, ShockCurveGeomFactory factory) {
        super(source, factory.selectViewingAttr());
        factory_ = factory;
    }

    //
    // Accessors/Mutators
    //

    @Override
    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {

        return new ShockCurveGeomView(this, transf, viewingAttr());

    }

    public RpGeomFactory geomFactory() {
        return factory_;
    }
}
