/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import rpnumerics.CompositeCalc;
import rpnumerics.CompositeCurve;

public class CompositeGeomFactory extends WaveCurveOrbitGeomFactory {
    //
    // Constants
    //
    //
    // Members
    //
    //
    // Constructors/Initializers
    //

    public CompositeGeomFactory(CompositeCalc calc) {
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

        CompositeCurve compositeCurve = (CompositeCurve) geomSource();

        return new CompositeGeom(MultidAdapter.converseOrbitPointsToCoordsArray(compositeCurve.getPoints()), this);


    }
}
