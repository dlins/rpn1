/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import java.awt.Color;
import rpnumerics.FundamentalCurve;
import rpnumerics.WaveCurveOrbitCalc;
import wave.multid.view.ViewingAttr;

public class WaveCurveOrbitGeomFactory extends OrbitGeomFactory {
    //
    // Constants
    //
    //
    // Members
    //
    //
    // Constructors/Initializers
    //

    public WaveCurveOrbitGeomFactory(WaveCurveOrbitCalc calc) {
        super(calc);
    }

    //
    // Accessors/Mutators
    //
    //
    // Methods
    //
    




    @Override
      protected ViewingAttr selectViewingAttr() {
        int family = (((FundamentalCurve) this.geomSource()).getFamilyIndex());//TODO REMOVE

        if (family == 1) {
            return new ViewingAttr(Color.red);
        }
        if (family == 0) {
            return new ViewingAttr(Color.blue);
        }


        return null;

      }
}
