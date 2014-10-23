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

public class ProfileGeom extends MultiPolyLine implements RpGeometry {
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
    public ProfileGeom(CoordsArray[] source, ProfileGeomFactory factory) {
        super(source, VIEWING_ATTR);
        factory_ = factory;
    }

    //
    // Accessors/Mutators
    //
    public RpGeomFactory geomFactory() { return factory_; }
}
