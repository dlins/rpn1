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

public class OrbitGeom extends MultiPolyLine implements RpGeometry {
    //
    // Constants
    //
    public static final int FORWARD_DIR = 1;
    public static final int BACKWARD_DIR = -1;
    public static ViewingAttr VIEWING_ATTR = new ViewingAttr(Color.white);
    //
    // Members
    //
    private RpGeomFactory factory_;

    //
    // Constructors
    //
    public OrbitGeom(CoordsArray[] source, OrbitGeomFactory factory) {
        super(source, VIEWING_ATTR);
        factory_ = factory;
    }

    //
    // Accessors/Mutators
    //

    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {

        return new OrbitGeomView(this, transf, viewingAttr());

    }

    public RpGeomFactory geomFactory() {
        return factory_;
    }
}
