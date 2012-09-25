/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import wave.multid.model.*;
import wave.multid.view.*;
import wave.multid.DimMismatchEx;
import java.util.Iterator;

public class BoundaryExtensionCurveView
        extends GeomObjView {
    //
    // Members
    //


    private ViewingTransform viewingTransf_;
    private AbstractGeomObj abstractGeom_;
    private ViewingAttr viewAttr_;

    //
    // Constructor
    //
    public BoundaryExtensionCurveView(BoundaryExtensionCurveGeom abstractGeom,
            ViewingTransform transf,
            ViewingAttr viewAttr) throws DimMismatchEx {
        
        super(abstractGeom, transf, viewAttr);
        
    }

  

    //Original update method
    public void update() {
        viewList_.clear();

        BoundaryExtensionCurveGeom extensionCurveGeom = (BoundaryExtensionCurveGeom) abstractGeom_;
       
        Iterator geomListIterator = extensionCurveGeom.getBifurcationSegmentsIterator();

        while (geomListIterator.hasNext()) {
            RealSegGeom geomObj = (RealSegGeom) geomListIterator.next();
            geomObj.viewingAttr().setColor(BoundaryExtensionCurveGeom.COLOR);
            try {
                viewList_.add(geomObj.createView(getViewingTransform()));
            } catch (DimMismatchEx dex) {
                dex.printStackTrace();
            }
        }
    }
}
