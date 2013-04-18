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

public class PhysicalBoundaryView extends  GeomObjView {
    //
    // Members
    //

  

    //
    // Constructor
    //
    public PhysicalBoundaryView(PhysicalBoundaryGeom abstractGeom,
            ViewingTransform transf,
            ViewingAttr viewAttr) throws DimMismatchEx {
        super(abstractGeom, transf, viewAttr);
    }

   
    //Original update method
    public void update() {

        viewList_.clear();
        Iterator geomListIterator = ((PhysicalBoundaryGeom) getAbstractGeom()).getRealSegIterator();
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
