/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import java.awt.Color;
import java.util.List;
import wave.multid.view.*;
import wave.multid.*;

public class SubInflectionExtensionCurveGeom extends BifurcationCurveGeom {//implements MultiGeometry, RpGeometry {

    //
    // Constructors
    //
    public static Color COLOR =new Color(0, 153, 153);

    public SubInflectionExtensionCurveGeom(List<BifurcationCurveBranchGeom> segArray, SubInflectionExtensionCurveGeomFactory factory) {

        super(segArray, factory);

    }

    @Override
    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {
        viewingAttr().setColor(COLOR);
        return new SubInflectionExtensionCurveView(this, transf, viewingAttr());
    }
}
