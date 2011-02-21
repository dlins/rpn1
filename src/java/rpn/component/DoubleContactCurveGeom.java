/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import wave.multid.view.*;
import wave.multid.*;
import wave.multid.model.BoundingBox;

public class DoubleContactCurveGeom extends BifurcationCurveGeom {//implements MultiGeometry, RpGeometry {
   

    //
    // Constructors
    //


    public DoubleContactCurveGeom(BifurcationSegGeom[] segArray, BifurcationCurveGeomFactory factory) {

        super(segArray, factory);

    }


    


    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {
        return new DoubleContactCurveView (this, transf, viewingAttr());
    }


}
