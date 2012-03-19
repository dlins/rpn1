/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import wave.multid.view.*;
import wave.multid.*;

public class EllipticBoundaryGeom extends SegmentedCurveGeom {//implements MultiGeometry, RpGeometry {
   

    //
    // Constructors
    //
    public EllipticBoundaryGeom(RealSegGeom[] segArray, EllipticBoundaryFactory factory) {

        super(segArray, factory);

    }


    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {
        return new EllipticBoundaryView(this, transf, viewingAttr());
    }

   


}
