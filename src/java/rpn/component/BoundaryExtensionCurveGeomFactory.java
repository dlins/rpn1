/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import java.awt.Color;
import rpnumerics.*;
import wave.multid.view.ViewingAttr;
import wave.util.RealSegment;

public class BoundaryExtensionCurveGeomFactory extends BifurcationCurveGeomFactory {

    private static ViewingAttr viewAtt_ = new ViewingAttr(Color.white);

    public BoundaryExtensionCurveGeomFactory(BoundaryExtensionCurveCalc calc) {
        super(calc);
    }

    public BoundaryExtensionCurveGeomFactory(BoundaryExtensionCurveCalc calc, BoundaryExtensionCurve curve) {
        super(calc, curve);
    }

    //
    // Methods
    //
    @Override
    public RpGeometry createGeomFromSource() {

        BoundaryExtensionCurve curve = (BoundaryExtensionCurve) geomSource();

        int resultSize = curve.segments().size();

        RealSegGeom[] leftBifurcationSegArray = new RealSegGeom[resultSize];
        for (int i = 0; i < resultSize; i++) {
            leftBifurcationSegArray[i] = new RealSegGeom((RealSegment) curve.segments().get(i), viewAtt_);
        }

        return new BoundaryExtensionCurveGeom(leftBifurcationSegArray, this);

    }


}
