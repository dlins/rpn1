/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import wave.multid.view.*;
import wave.multid.*;

public class SubInflectionCurveGeom extends SegmentedCurveGeom{
   
  
    //
    // Constructors
    //
    public SubInflectionCurveGeom(HugoniotSegGeom[] segArray, SubInflectionCurveGeomFactory factory) {

        super(segArray,factory);
    }

    //
    // Methods
    //
 

    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {
        return new SubInflectionCurveView(this, transf, viewingAttr());
    }
    
}
