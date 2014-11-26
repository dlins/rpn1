/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import rpnumerics.*;
import wave.multid.view.*;

public class ManifoldGeomFactory extends RpCalcBasedGeomFactory {
    //
    // Constants
    //
    //
    // Constructors/Initializers
    //
    public ManifoldGeomFactory(ManifoldOrbitCalc calc) {
        super(calc);
    }

    //
    // Methods
    //
    public RpGeometry createGeomFromSource() {
        ManifoldOrbit orbit = (ManifoldOrbit) geomSource();
        return new ManifoldGeom(MultidAdapter.converseOrbitToCoordsArray(orbit.getOrbit()), this);
    }
}
