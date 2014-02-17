/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import wave.multid.view.*;
import wave.multid.*;
import wave.multid.model.MultiPoint;

public class SecondaryBifurcationCurveGeom extends BifurcationCurveGeom {
   

    //
    // Constructors
    //

    public MultiPoint umbilicPoint_;
    public SecondaryBifurcationCurveGeom(RealSegGeom[] segArray, MultiPoint umbilicPoint,BifurcationCurveGeomFactory factory) {

        super(segArray, factory);
        System.out.println("Tamanho de segArray em SecondaryBifurcationGeom : " +segArray.length);
        umbilicPoint_=umbilicPoint;
        
    }

    public MultiPoint getUmbilicPoint() {
        return umbilicPoint_;
    }


    


    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {

        return new SecondaryBifurcationCurveView(this, transf, viewingAttr());
    }


}
