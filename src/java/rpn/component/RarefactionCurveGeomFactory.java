/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import rpnumerics.RarefactionCurve;
import rpnumerics.RarefactionCurveCalc;

public class RarefactionCurveGeomFactory extends WaveCurveOrbitGeomFactory {
    //
    // Constants
    //
    //
    // Members
    //
    //
    // Constructors/Initializers
    //

    public RarefactionCurveGeomFactory(RarefactionCurveCalc calc) {
        super(calc);
    }

    //
    // Accessors/Mutators
    //
    //
    // Methods
    //
    @Override
    public RpGeometry createGeomFromSource() {

        RarefactionCurve orbit = (RarefactionCurve) geomSource();


        return new RarefactionCurveGeom(MultidAdapter.converseOrbitPointsToCoordsArray(orbit.getPoints()), this);

    }
}
