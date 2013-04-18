/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import rpnumerics.*;
import rpnumerics.EllipticBoundary;
import wave.util.RealSegment;

public class EllipticBoundaryExtensionFactory extends BifurcationCurveGeomFactory {

    public EllipticBoundaryExtensionFactory(EllipticBoundaryExtensionCalc calc) {
        super(calc);


    }

    public EllipticBoundaryExtensionFactory(EllipticBoundaryExtensionCalc calc,EllipticBoundaryExtension curve) {
        super(calc,curve);


    }
    //
    // Methods
    //

    public RpGeometry createGeomFromSource() {

        EllipticBoundaryExtension curve = (EllipticBoundaryExtension) geomSource();


        // assuming a container with HugoniotSegment elements
        int resultSize = curve.segments().size();

        RealSegGeom[] hugoniotArray = new RealSegGeom[resultSize];
        for (int i = 0; i < resultSize; i++) {
            hugoniotArray[i] = new RealSegGeom((RealSegment) curve.segments().get(i));
        }
        return new EllipticBoundaryExtensionGeom(hugoniotArray, this);

    }

    public String toMatlab(int curveIndex) {
        return null;
    }
}
