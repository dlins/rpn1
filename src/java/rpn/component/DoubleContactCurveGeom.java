/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import java.util.List;
import wave.multid.view.*;
import wave.multid.*;

public class DoubleContactCurveGeom extends BifurcationCurveGeom {
   

    //
    // Constructors
    //


    public DoubleContactCurveGeom(List<BifurcationCurveBranchGeom> segArray, BifurcationCurveGeomFactory factory) {

        super(segArray, factory);

        
    }


    


    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {

        return new DoubleContactCurveView (this, transf, viewingAttr());
    }


}
