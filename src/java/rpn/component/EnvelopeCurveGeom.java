/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import java.util.List;
import wave.multid.view.*;
import wave.multid.*;

public class EnvelopeCurveGeom extends BifurcationCurveGeom {
   

    //
    // Constructors
    //


    public EnvelopeCurveGeom(List<BifurcationCurveBranchGeom> segArray, BifurcationCurveGeomFactory factory) {
        super(segArray, factory);
    }


    


    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {
        return new EnvelopeCurveView(this, transf, viewingAttr());
    }


}
