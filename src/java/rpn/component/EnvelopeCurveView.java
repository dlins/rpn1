/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import wave.multid.model.*;
import wave.multid.view.*;
import wave.multid.DimMismatchEx;
import java.awt.Graphics2D;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

public class EnvelopeCurveView extends BifurcationCurveView {
    //
    // Members
    //


    //
    // Constructor
    //
    public EnvelopeCurveView(EnvelopeCurveGeom abstractGeom,
            ViewingTransform transf,
            ViewingAttr viewAttr) throws DimMismatchEx {
        super(abstractGeom, transf, viewAttr);
        
    }

  
    
//    //Original update method
//    public void update() {
//        viewList_.clear();
//
//        EnvelopeCurveGeom doubleContactGeom = (EnvelopeCurveGeom) getAbstractGeom();
//        Iterator geomListIterator = doubleContactGeom.getBifurcationSegmentsIterator();
//
//
//        while (geomListIterator.hasNext()) {
//            RealSegGeom geomObj = (RealSegGeom) geomListIterator.next();
//            try {
//                if (geomObj == null) {
//                    System.out.println("Eh nulo segmento");
//                }
//
//                viewList_.add(geomObj.createView(getViewingTransform()));
//            } catch (DimMismatchEx dex) {
//                dex.printStackTrace();
//            }
//        }
//    }
}
