/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import rpnumerics.*;
import rpnumerics.EllipticBoundary;
import wave.util.RealSegment;

public class PhysicalBoundaryFactory extends RpCalcBasedGeomFactory {

    public PhysicalBoundaryFactory(PhysicalBoundaryCalc calc) {
        super(calc);


    }
    //
    // Methods
    //

    public RpGeometry createGeomFromSource() {

        PhysicalBoundary curve = (PhysicalBoundary) geomSource();

        int resultSize = curve.segments().size();

        RealSegGeom[] segmentsArray = new RealSegGeom[resultSize];
        for (int i = 0; i < resultSize; i++) {
            segmentsArray[i] = new RealSegGeom((RealSegment) curve.segments().get(i));
        }
        return new PhysicalBoundaryGeom(segmentsArray, this);

    }
}
