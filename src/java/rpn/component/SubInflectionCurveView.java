/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import java.awt.Color;
import wave.multid.model.*;
import wave.multid.view.*;
import wave.multid.DimMismatchEx;
import java.awt.Graphics2D;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

public class SubInflectionCurveView
        extends BifurcationCurveView {
    //
    // Members
    //

    //
    // Constructor
    //
    public SubInflectionCurveView(SubInflectionCurveGeom abstractGeom,
            ViewingTransform transf,
            ViewingAttr viewAttr) throws DimMismatchEx {
        super(abstractGeom, transf, viewAttr);

    }

//    //Original update method
//    public void update() {
//
//
//        viewList_.clear();
//        Iterator geomListIterator = ((SubInflectionCurveGeom) getAbstractGeom()).getBifurcationSegmentsIterator();
//        while (geomListIterator.hasNext()) {
//            RealSegGeom geomObj = (RealSegGeom) geomListIterator.next();
//            geomObj.viewingAttr().setColor(SubInflectionCurveGeom.COLOR);
//            try {
//                viewList_.add(geomObj.createView(getViewingTransform()));
//            } catch (DimMismatchEx dex) {
//                dex.printStackTrace();
//            }
//        }
//    }
}
