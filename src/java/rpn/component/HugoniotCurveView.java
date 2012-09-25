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
import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

public class HugoniotCurveView extends GeomObjView {
   
    //
    // Constructor
    //
    public HugoniotCurveView(HugoniotCurveGeom abstractGeom,
            ViewingTransform transf,
            ViewingAttr viewAttr) throws DimMismatchEx {
        super(abstractGeom, transf, viewAttr);

    }

    //
    // Accessors/Mutators
    //
   
    

    //Original update method
    public void update() {


        viewList_.clear();
        Iterator geomListIterator = ((HugoniotCurveGeom) getAbstractGeom()).getRealSegIterator();
        while (geomListIterator.hasNext()) {
            HugoniotSegGeom geomObj = (HugoniotSegGeom) geomListIterator.next();
            try {
                viewList_.add(geomObj.createView(getViewingTransform()));
            } catch (DimMismatchEx dex) {
                dex.printStackTrace();
            }
        }
    }
}
