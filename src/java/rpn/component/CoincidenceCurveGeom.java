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

public class CoincidenceCurveGeom extends SegmentedCurveGeom {//implements MultiGeometry, RpGeometry {

    //
    // Constructors
    public static Color COLOR = new Color(20, 43, 140);

    public CoincidenceCurveGeom(HugoniotSegGeom[] segArray, CoincidenceCurveGeomFactory factory) {

        super(segArray, factory);

    }

    //
    // Methods
    //
    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {

        viewingAttr().setColor(COLOR);
        return new CoincidenceCurveView(this, transf, viewingAttr());
    }
}
