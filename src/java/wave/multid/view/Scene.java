/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package wave.multid.view;

import wave.multid.model.*;
import wave.multid.DimMismatchEx;
import java.util.Iterator;

public class Scene extends GeomObjView {
    //
    // Members
    //

    //
    // Constructor
    //
    
    public Scene(AbstractScene abstractGeom, ViewingTransform transf, ViewingAttr viewAttr) throws DimMismatchEx {
        super(abstractGeom, transf, viewAttr);

    }

 
    public Iterator geometries() {
        return viewList_.iterator();
    }

  
        
    public void addViewFor(MultiGeometry geomObj) {
        try {
            GeomObjView updatedGeomView = (GeomObjView) geomObj.createView(getViewingTransform());
            viewList_.add(updatedGeomView);
        } catch (DimMismatchEx dex) {
            dex.printStackTrace();
        }
    }

    public void removeViewOf(MultiGeometry geomObj) {
        Iterator viewListIterator = viewList_.iterator();
        while (viewListIterator.hasNext()) {
            GeomObjView oldGeomView = (GeomObjView) viewListIterator.next();
            if (oldGeomView.getAbstractGeom().equals(geomObj)) {
                viewListIterator.remove();
                break;
            }
        }
    }



    public void update() {
        viewList_.clear();
        Iterator geomListIterator = ((AbstractScene) getAbstractGeom()).getGeomObjIterator();
        while (geomListIterator.hasNext()) {
            MultiGeometry geomObj = (MultiGeometry) geomListIterator.next();
            addViewFor(geomObj);
        }

    }
}
