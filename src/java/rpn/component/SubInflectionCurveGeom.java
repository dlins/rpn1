/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import java.awt.Color;
import wave.multid.view.*;
import wave.multid.*;

public class SubInflectionCurveGeom extends SegmentedCurveGeom{
   
    public static Color COLOR  = new Color(243, 123, 46);
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
        viewingAttr().setColor(COLOR);
        return new SubInflectionCurveView(this, transf, viewingAttr());
    }
    
}
