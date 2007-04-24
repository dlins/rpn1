/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.component;

import wave.multid.model.MultiPolygon;
import wave.multid.view.ViewingAttr;
import wave.multid.CoordsArray;
import java.awt.Color;

public class PoincareSectionGeom extends MultiPolygon implements RpGeometry {
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
    public PoincareSectionGeom(CoordsArray[] source, PoincareSectionGeomFactory factory) {
        super(source, VIEWING_ATTR);
        factory_ = factory;
    }

    //
    // Accessors/Mutators
    //
    public RpGeomFactory geomFactory() { return factory_; }
}
