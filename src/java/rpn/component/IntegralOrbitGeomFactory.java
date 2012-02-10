/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import rpnumerics.*;

public class IntegralOrbitGeomFactory extends OrbitGeomFactory {
    //
    // Constants
    //
    //
    // Members
    //
    //
    // Constructors/Initializers
    //
    public IntegralOrbitGeomFactory(IntegralCurveCalc calc) {
        super(calc);
    }

    //
    // Accessors/Mutators
    //
    //
    // Methods
    //
   

    @Override
    protected RpGeometry createGeomFromSource() {
        Orbit orbit = (Orbit) geomSource();

        return new IntegralGeom(MultidAdapter.converseOrbitToCoordsArray(orbit), this);
    }


    public String toMatlab(int curveIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
