/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import wave.multid.view.*;
import wave.multid.*;

public class SubInflectionExtensionCurveGeom extends BifurcationCurveGeom {//implements MultiGeometry, RpGeometry {
   

    //
    // Constructors
    //


    public SubInflectionExtensionCurveGeom(BifurcationSegGeom[] segArray, SubInflectionExtensionCurveGeomFactory factory) {

        super(segArray, factory);

    }


    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {
        return new SubInflectionExtensionCurveView(this, transf, viewingAttr());
    }


}
