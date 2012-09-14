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
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import rpn.component.HugoniotSegGeom;

public class HugoniotCurveView
        implements GeomObjView {
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
    public HugoniotCurveView(HugoniotCurveGeom abstractGeom,
            ViewingTransform transf,
            ViewingAttr viewAttr) throws DimMismatchEx {
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

    //
    // Methods
    //
    public void draw(Graphics2D g) {

        for (int i = 0; i < viewList_.size(); i++) {
            ((GeomObjView) viewList_.get(i)).draw(g);
        }
    }

    public boolean intersect(Polygon polygon) {

        boolean inter = false;
        for (Object object : viewList_) {


            PolyLine segment = (PolyLine) object;

            inter = segment.getShape().intersects(polygon.getBounds());

            if (inter) {
                return inter;
            }

        }
        return inter;


    }

    public List<Integer> contains(Polygon polygon) {

        ArrayList<Integer> segmentIndex = new ArrayList<Integer>();
        int segIndex = 0;
        
        System.out.println("Tamanho da hugoniot: "+viewList_.size());
        for (Object object : viewList_) {

            PolyLine segment = (PolyLine) object;
            double xCenterSegment = segment.getShape().getBounds().getCenterX();
            double yCenterSegment = segment.getShape().getBounds().getCenterY();

            if (polygon.contains(xCenterSegment, yCenterSegment)) {
                segmentIndex.add(segIndex);

            }

            segIndex++;
        }


        return segmentIndex;

    }

    //Original update method
    public void update() {


        viewList_.clear();
        Iterator geomListIterator = ((HugoniotCurveGeom) abstractGeom_).getRealSegIterator();
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
