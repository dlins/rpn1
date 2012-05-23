/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import java.awt.Color;
import wave.multid.view.*;
import wave.multid.*;

public class PhysicalBoundaryGeom extends SegmentedCurveGeom {

    private ViewingAttr viewAtt_;

    public PhysicalBoundaryGeom(RealSegGeom[] segArray, PhysicalBoundaryFactory factory) {

        super(segArray, factory);
        viewAtt_=new ViewingAttr(Color.gray);


    }

    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {
        return new PhysicalBoundaryView(this, transf, viewingAttr());
    }

    @Override
    public ViewingAttr viewingAttr() {
        return viewAtt_;
    }
}
