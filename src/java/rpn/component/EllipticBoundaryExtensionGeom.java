/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import java.util.List;
import wave.multid.view.*;
import wave.multid.*;

public class EllipticBoundaryExtensionGeom extends BifurcationCurveGeom {//implements MultiGeometry, RpGeometry {
   

    //
    // Constructors
    //
    public EllipticBoundaryExtensionGeom(List<BifurcationCurveBranchGeom> segArray, EllipticBoundaryExtensionFactory factory) {

        super(segArray, factory);

    }


    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {
        return new EllipticBoundaryExtensionView(this, transf, viewingAttr());
    }

   


}
