/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.component;


import wave.multid.DimMismatchEx;
import wave.multid.view.GeomObjView;
import wave.multid.view.ViewingTransform;

class CoincidenceExtensionCurveGeom extends SegmentedCurveGeom{//implements MultiGeometry, RpGeometry {
  
    //
    // Constructors

    //
    public CoincidenceExtensionCurveGeom(HugoniotSegGeom[] segArray, CoincidenceExtensionCurveGeomFactory factory) {

        super(segArray,factory);
    }

    //
    // Methods
    //

    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {
        return new CoincidenceExtensionCurveGeomView(this, transf, VIEWING_ATTR);
    }
  
}
