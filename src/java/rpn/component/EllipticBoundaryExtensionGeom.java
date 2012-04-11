/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import wave.multid.view.*;
import wave.multid.*;

public class EllipticBoundaryExtensionGeom extends SegmentedCurveGeom {//implements MultiGeometry, RpGeometry {
   

    //
    // Constructors
    //
    public EllipticBoundaryExtensionGeom(RealSegGeom[] segArray, EllipticBoundaryExtensionFactory factory) {

        super(segArray, factory);

    }


    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {
        return new EllipticBoundaryExtensionView(this, transf, viewingAttr());
    }

   


}
