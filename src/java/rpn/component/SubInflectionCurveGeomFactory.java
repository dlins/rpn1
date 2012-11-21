/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import rpnumerics.*;

public class SubInflectionCurveGeomFactory extends BifurcationCurveGeomFactory {

    public SubInflectionCurveGeomFactory(SubInflectionCurveCalc calc) {
        super(calc);

    }

    //
    // Methods
    //
    public RpGeometry createGeomFromSource() {

        SubInflectionCurve curve = (SubInflectionCurve) geomSource();

        // assuming a container with HugoniotSegment elements
        int resultSize = curve.segments().size();
        RealSegGeom[] hugoniotArray = new RealSegGeom[resultSize];
        for (int i = 0; i < resultSize; i++) {
            hugoniotArray[i] = new RealSegGeom((HugoniotSegment) curve.segments().get(i));
        }
        return new SubInflectionCurveGeom(hugoniotArray, this);

    }
}
