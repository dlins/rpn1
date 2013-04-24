/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.component;

import wave.multid.model.MultiGeometry;

/** This interface is the main class of a factory method pattern. With this pattern all abstract objects used by the application are created .*/

public interface RpGeometry extends MultiGeometry {

    /** Returns the object that constructs the geometric model. */

    RpGeomFactory geomFactory();
  

}
