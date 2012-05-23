/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.component;

import wave.multid.model.MultiPoint;
import wave.multid.view.*;
import wave.multid.CoordsArray;
import wave.multid.DimMismatchEx;
import java.awt.Color;

public class StationaryPointGeom extends MultiPoint implements RpGeometry {
    //
    // Constants
    //
    public static ViewingAttr VIEWING_ATTR = new ViewingAttr(Color.white);
    //
    // Members
    //
    private RpGeomFactory factory_;

    //
    // Constructors
    //
    public StationaryPointGeom(CoordsArray source, StationaryPointGeomFactory factory) {
        super(source, VIEWING_ATTR);
        System.out.println("//*** Construtor de StationaryPointGeom");
        factory_ = factory;
    }

    //
    // Accessors/Mutators
    //
    public RpGeomFactory geomFactory() { return factory_; }

    //
    // Methods
    //
    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {
        return new StationaryPointView(this, transf, viewingAttr());
    }
}
