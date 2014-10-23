/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import wave.multid.view.*;
import wave.multid.DimMismatchEx;
import java.util.Iterator;

public class BuckleyLeverettinCurveView extends GeomObjView {
    //
    // Members
    //



    //
    // Constructor
    //
    public BuckleyLeverettinCurveView(BuckleyLeverettinInflectionGeom abstractGeom,
            ViewingTransform transf,
            ViewingAttr viewAttr) throws DimMismatchEx {
        super (abstractGeom, transf, viewAttr);

    }

  

    //Original update method
    public void update() {


        viewList_.clear();
        Iterator geomListIterator = ((BuckleyLeverettinInflectionGeom) getAbstractGeom()).getBifurcationSegmentsIterator();
        while (geomListIterator.hasNext()) {
            RealSegGeom geomObj = (RealSegGeom) geomListIterator.next();
            geomObj.viewingAttr().setColor(BuckleyLeverettinInflectionGeom.COLOR);
            try {
                viewList_.add(geomObj.createView(getViewingTransform()));
            } catch (DimMismatchEx dex) {
                dex.printStackTrace();
            }
        }
    }
}
