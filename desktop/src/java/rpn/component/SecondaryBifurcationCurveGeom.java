/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import wave.multid.view.*;
import wave.multid.*;

public class SecondaryBifurcationCurveGeom extends BifurcationCurveGeom {
   

    //
    // Constructors
    //


    public SecondaryBifurcationCurveGeom(RealSegGeom[] segArray, BifurcationCurveGeomFactory factory) {

        super(segArray, factory);
        System.out.println("Tamanho de segArray em SecondaryBifurcationGeom : " +segArray.length);
        
    }


    


    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {

        return new SecondaryBifurcationCurveView(this, transf, viewingAttr());
    }


}
