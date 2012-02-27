/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.component;


import java.awt.Color;
import wave.multid.DimMismatchEx;
import wave.multid.view.GeomObjView;
import wave.multid.view.ViewingTransform;


public class CoincidenceExtensionCurveGeom extends BifurcationCurveGeom{//implements MultiGeometry, RpGeometry {

  
    //
    // Constructors


    public static Color COLOR = new Color(0, 153, 153);


    public CoincidenceExtensionCurveGeom(RealSegGeom[] segArray, CoincidenceExtensionCurveGeomFactory factory) {

        super(segArray,factory);
    }

    //
    // Methods
    //

    @Override
    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {
        viewingAttr().setColor(COLOR);
        return new CoincidenceExtensionCurveGeomView(this, transf, viewingAttr());
    }
  
}
