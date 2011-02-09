/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.component;


import wave.multid.DimMismatchEx;
import wave.multid.view.GeomObjView;
import wave.multid.view.ViewingTransform;

class CoincidenceCurveGeom extends SegmentedCurveGeom{//implements MultiGeometry, RpGeometry {
  
    //
    // Constructors

    //
    public CoincidenceCurveGeom(HugoniotSegGeom[] segArray, CoincidenceCurveGeomFactory factory) {

        super(segArray,factory);
    }

    //
    // Methods
    //

    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {
        return new CoincidenceCurveView(this, transf, VIEWING_ATTR);
    }
  
}
