/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpnumerics.factories;

import rpnumerics.WaveCurve;
import rpnumerics.HugoniotCurveCalc;

public class ShockCurveEngine {
    //
    // Constants
    //
    //
    // Members/Inners
    //
    static class HugoniotClipper {
        static WaveCurve clip(HugoniotCurveCalc curve) {
            return null;
        }
    }


    //
    // Constructors
    //
    private ShockCurveEngine() { }

    //
    // Accessors/Mutators
    //
    //
    // Methods
    //
    public static WaveCurve create(HugoniotCurveCalc curve) {
        return HugoniotClipper.clip(curve);
    }

    public static WaveCurve create() {
        return null;
    }
}
