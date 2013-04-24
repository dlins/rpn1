/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import wave.multid.view.*;
import wave.multid.*;

public class HugoniotCurveGeom extends SegmentedCurveGeom {//implements MultiGeometry, RpGeometry {
   

    //
    // Constructors
    //
    public HugoniotCurveGeom(HugoniotSegGeom[] segArray, HugoniotCurveGeomFactory factory) {

        super(segArray, factory);
    }

    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {
        return new HugoniotCurveView(this, transf, viewingAttr());
    }

   


}
