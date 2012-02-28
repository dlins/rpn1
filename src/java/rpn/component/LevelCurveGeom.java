/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import wave.multid.DimMismatchEx;
import wave.multid.view.GeomObjView;
import wave.multid.view.ViewingTransform;

public class LevelCurveGeom extends SegmentedCurveGeom {

    //
    // Constructors
    public LevelCurveGeom(RealSegGeom[] segArray, LevelCurveGeomFactory factory) {

        super(segArray, factory);

    }

    //
    // Methods
    //
    @Override
    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {
        return new LevelCurveView(this, transf, viewingAttr());
    }

   
}
