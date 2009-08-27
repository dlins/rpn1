/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import rpnumerics.*;

public class ShockCurveGeomFactory extends RpCalcBasedGeomFactory {
    //
    // Constants
    //
    //
    // Members
    //
    //
    // Constructors/Initializers
    //
    public ShockCurveGeomFactory(ShockCurveCalc calc) {
        super(calc);
    }

    //
    // Accessors/Mutators
    //
    //
    // Methods
    //
    protected RpGeometry createGeomFromSource() {
        ShockCurve shockCurve = (ShockCurve) geomSource();

        return new ShockCurveGeom(MultidAdapter.converseOrbitPointsToCoordsArray(shockCurve.getPoints()), this);
    }

    public String toXML() {//TODO Implement
        StringBuffer str = new StringBuffer();
        String timedir = "ShockCurve XML";
        str.append(timedir);
        return str.toString();
    }
}
