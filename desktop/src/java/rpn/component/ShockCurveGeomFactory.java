/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import java.awt.Color;
import rpnumerics.*;
import wave.multid.view.ViewingAttr;

public class ShockCurveGeomFactory extends WaveCurveOrbitGeomFactory {
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
    public RpGeometry createGeomFromSource() {
        ShockCurve shockCurve = (ShockCurve) geomSource();

        return new ShockCurveGeom(MultidAdapter.converseOrbitPointsToCoordsArray(shockCurve.getPoints()), this);
    }


     @Override
      protected ViewingAttr selectViewingAttr() {
        int family = (((ShockCurve) this.geomSource()).getFamilyIndex());


        if (family == 1) {
            return new ViewingAttr(Color.red);
        }
        if (family == 0) {
            return new ViewingAttr(Color.blue);
        }


        return null;

      }
}
