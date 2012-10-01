/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import wave.multid.view.*;
import wave.multid.DimMismatchEx;
import java.util.Iterator;

public class EllipticBoundaryView extends GeomObjView {
    //
    // Members
    //


    //
    // Constructor
    //
    public EllipticBoundaryView(EllipticBoundaryGeom abstractGeom,
            ViewingTransform transf,
            ViewingAttr viewAttr) throws DimMismatchEx {
        super(abstractGeom, transf, viewAttr);

    }

   
    //Original update method
    public void update() {


        viewList_.clear();
        Iterator geomListIterator = ((EllipticBoundaryGeom) getAbstractGeom()).getRealSegIterator();
        while (geomListIterator.hasNext()) {
           RealSegGeom geomObj = (RealSegGeom) geomListIterator.next();
            try {
                viewList_.add(geomObj.createView(getViewingTransform()));
            } catch (DimMismatchEx dex) {
                dex.printStackTrace();
            }
        }
    }
}
