/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package wave.multid.view;

import wave.multid.model.*;
import wave.multid.DimMismatchEx;
import java.awt.Graphics2D;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

public class Scene implements GeomObjView {
    //
    // Members
    //

    private List viewList_;
    private ViewingTransform viewingTransf_;
    private AbstractGeomObj abstractGeom_;
    private ViewingAttr viewAttr_;

    //
    // Constructor
    //
    
    public Scene(AbstractScene abstractGeom, ViewingTransform transf, ViewingAttr viewAttr) throws DimMismatchEx {
        setAbstractGeom(abstractGeom);
        setViewingTransform(transf);
        setViewingAttr(viewAttr);
        viewList_ = new ArrayList();
        update();
    }

    //
    // Accessors/Mutators
    //
    public AbstractGeomObj getAbstractGeom() {
        return abstractGeom_;
    }

    public void setAbstractGeom(AbstractGeomObj abstractGeom) {
        abstractGeom_ = abstractGeom;
    }

    public ViewingTransform getViewingTransform() {
        return viewingTransf_;
    }

    public void setViewingTransform(ViewingTransform transf) {
        viewingTransf_ = transf;
    }

    public ViewingAttr getViewingAttr() {
        return viewAttr_;
    }

    public void setViewingAttr(ViewingAttr viewAttr) {
        viewAttr_ = viewAttr;
    }

    public Iterator geometries() {
        return viewList_.iterator();
    }

    //
    // Methods
    //
    public void draw(Graphics2D g) {
        for (int i = 0; i < viewList_.size(); i++) {
            if (((GeomObjView) viewList_.get(i)).getViewingAttr().isVisible()) {
                ((GeomObjView) viewList_.get(i)).draw(g);
            }
        }
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
        Iterator geomListIterator = ((AbstractScene) abstractGeom_).getGeomObjIterator();
        while (geomListIterator.hasNext()) {
            MultiGeometry geomObj = (MultiGeometry) geomListIterator.next();
            addViewFor(geomObj);
        }

    }
}
