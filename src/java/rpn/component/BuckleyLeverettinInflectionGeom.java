/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import java.awt.Color;
import wave.multid.view.*;
import wave.multid.*;

public class BuckleyLeverettinInflectionGeom extends BifurcationCurveGeom {//implements MultiGeometry, RpGeometry {

    //
    // Constructors
    //
    public static Color COLOR = new Color(0, 255, 0);

    public BuckleyLeverettinInflectionGeom(RealSegGeom[] segArray, BuckleyLeverettinCurveGeomFactory factory) {
        super(segArray, factory);

    }
    //
    // Accessors/Mutators
    //

    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {
        viewingAttr().setColor(COLOR);
        return new BuckleyLeverettinCurveView(this, transf, viewingAttr());
    }
}
