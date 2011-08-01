/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.component;

import wave.multid.view.*;
import wave.multid.*;

public class BuckleyLeverettinInflectionGeom extends BifurcationCurveGeom{//implements MultiGeometry, RpGeometry {
    
    //
    // Constructors
    //
    public BuckleyLeverettinInflectionGeom(BifurcationSegGeom[] segArray, BuckleyLeverettinCurveGeomFactory factory) {
        super (segArray,factory);

   }
    //
    // Accessors/Mutators
    //
  
    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {
        return new BuckleyLeverettinCurveView(this, transf, viewingAttr());
    }
}
