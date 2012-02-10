/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import java.awt.Color;
import wave.multid.view.*;
import wave.multid.*;

public class DoubleContactCurveGeom extends BifurcationCurveGeom {//implements MultiGeometry, RpGeometry {
   

    //
    // Constructors
    //


    public DoubleContactCurveGeom(BifurcationSegGeom[] segArray, BifurcationCurveGeomFactory factory) {

        super(segArray, factory);
        System.out.println("Tamanho de segArray em DoubleContactCurveGeom : " +segArray.length);
        
    }


    


    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {

        return new DoubleContactCurveView (this, transf, viewingAttr());
    }


}
