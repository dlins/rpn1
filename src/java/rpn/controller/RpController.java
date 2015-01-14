/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.controller;

import rpn.component.RpGeomFactory;
import java.beans.PropertyChangeListener;

/** This interface declares basic methods to add and remove controllers of  visual elements . When a orbit ,point , etc is recalculated the controller of this element sends a signal to change its visual properties . */ 

public interface RpController extends PropertyChangeListener {

    /** Installs a controller in a geometric object */

    void install(RpGeomFactory geom);

    /** Uninstalls the  controller of the  geometric object */

    void uninstall(RpGeomFactory geom);
}
