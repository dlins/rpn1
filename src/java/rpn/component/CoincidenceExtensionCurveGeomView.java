/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import java.awt.Graphics2D;
import java.util.Iterator;
import java.util.List;
import wave.multid.DimMismatchEx;
import wave.multid.model.AbstractGeomObj;
import wave.multid.view.GeomObjView;
import wave.multid.view.ViewingAttr;
import wave.multid.view.ViewingTransform;

class CoincidenceExtensionCurveGeomView extends BifurcationCurveView{
    //
    // Members
    //

    private List viewList_;
    private ViewingTransform viewingTransf_;
    private AbstractGeomObj abstractGeom_;
    private ViewingAttr viewAttr_;

    public CoincidenceExtensionCurveGeomView(CoincidenceExtensionCurveGeom abstractGeom,
            ViewingTransform transf,
            ViewingAttr viewAttr) throws DimMismatchEx {

        super(abstractGeom, transf, viewAttr);

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

    //
    // Methods
    //
    public void draw(Graphics2D g) {

        for (int i = 0; i < viewList_.size(); i++) {
            ((GeomObjView) viewList_.get(i)).draw(g);
        }
    }

//    //Original update method
//    public void update() {
//        viewList_.clear();
//        Iterator geomListIterator = ((CoincidenceExtensionCurveGeom) abstractGeom_).getBifurcationSegmentsIterator();
//        while (geomListIterator.hasNext()) {
//            RealSegGeom geomObj = (RealSegGeom) geomListIterator.next();
//
//            geomObj.viewingAttr().setColor(CoincidenceExtensionCurveGeom.COLOR);
//
//            try {
//                viewList_.add(geomObj.createView(getViewingTransform()));
//            } catch (DimMismatchEx dex) {
//                dex.printStackTrace();
//            }
//        }
//    }
}
