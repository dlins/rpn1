/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import wave.multid.view.*;
import wave.multid.DimMismatchEx;
import java.util.Iterator;

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

        System.out.println("Atualizando Hugoniot");
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
