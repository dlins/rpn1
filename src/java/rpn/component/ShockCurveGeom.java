/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.component;

import wave.multid.CoordsArray;
import wave.multid.view.GeomObjView;
import wave.multid.view.ViewingTransform;
import wave.multid.DimMismatchEx;

public class ShockCurveGeom extends WaveCurveOrbitGeom implements RpGeometry {
   
  

    //
    // Constructors
    //
    public ShockCurveGeom(CoordsArray[] source, WaveCurveOrbitGeomFactory factory) {
        super(source, factory);
    }

    //
    // Accessors/Mutators
    //

    @Override
    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {

        return new ShockCurveGeomView(this, transf,viewingAttr());

    }

    
}
