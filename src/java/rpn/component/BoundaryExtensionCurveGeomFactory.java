/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import rpnumerics.*;

public class BoundaryExtensionCurveGeomFactory extends BifurcationCurveGeomFactory {



    public BoundaryExtensionCurveGeomFactory(BoundaryExtensionCurveCalc calc) {
        super(calc);
    }

    public BoundaryExtensionCurveGeomFactory(BoundaryExtensionCurveCalc calc, BoundaryExtensionCurve curve) {
        super(calc, curve);
    }

  

}
