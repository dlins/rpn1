/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import rpnumerics.*;

public class IntegralOrbitGeomFactory extends WaveCurveOrbitGeomFactory {
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
    public RpGeometry createGeomFromSource() {

       IntegralCurve integralCurve = (IntegralCurve) geomSource();

        return new IntegralGeom(MultidAdapter.converseOrbitPointsToCoordsArray(integralCurve.getPoints()), this);

    }



    public String toMatlab(int curveIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
