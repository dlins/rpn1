/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import rpnumerics.*;

public class SubInflectionExtensionCurveGeomFactory extends BifurcationCurveGeomFactory {

    public SubInflectionExtensionCurveGeomFactory(SubInflectionExtensionCurveCalc calc) {
        super(calc);
    }

    //
    // Methods
    //
    @Override
    public RpGeometry createGeomFromSource() {

        SubInflectionExtensionCurve curve = (SubInflectionExtensionCurve) geomSource();

        RealSegGeom[] bifurcationSegArray = null;

        int resultSize = curve.segments().size();

        bifurcationSegArray = new RealSegGeom[resultSize];
        for (int i = 0; i < resultSize; i++) {
            bifurcationSegArray[i] = new RealSegGeom((HugoniotSegment) curve.segments().get(i));

        }

        return new SubInflectionExtensionCurveGeom(bifurcationSegArray, this);

    }

}
