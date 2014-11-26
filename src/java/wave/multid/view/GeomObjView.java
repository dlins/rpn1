/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package wave.multid.view;

import wave.multid.model.AbstractGeomObj;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;

/** The main class of the view package. This class declares the basic methods to create
 * a visual form of a multidimensional object. A GeomObjView object contains the attributes
 * and the transformation associated with a visual form of a multidimensional object.
 */
public abstract class GeomObjView {

    protected List viewList_;
    private ViewingTransform viewingTransf_;
    private AbstractGeomObj abstractGeom_;
    private ViewingAttr viewAttr_;

    public GeomObjView(AbstractGeomObj abstractGeom, ViewingTransform transf, ViewingAttr viewAttr) {

        setAbstractGeom(abstractGeom);
        setViewingTransform(transf);
        setViewingAttr(viewAttr);
        viewList_ = new ArrayList();
        viewAttr_=viewAttr;
        update();

    }

    /** Update the visual form of a multidimensional object */
    public abstract void update();

    /** Draws a  multidimensional object */
    public void draw(Graphics2D g) {

        for (int i = 0; i < viewList_.size(); i++) {
            ((GeomObjView) viewList_.get(i)).draw(g);
        }
    }

    /**Tests if this view objects intersects the polygon bounds*/
    public boolean intersect(Polygon polygon) {

        for (Object object : viewList_) {

            PolyLine segment = (PolyLine) object;

            if (segment.getShape().intersects(polygon.getBounds())) {
                return true;
            }

        }

        return false;

    }

    /**Returns the segments indices that are inside the polygon*/
    public List<Integer> contains(Polygon polygon) {

        ArrayList<Integer> segmentIndex = new ArrayList<Integer>();
        int segIndex = 0;

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
    
    

    /** Returns the geometrics proprieties of a multidimensional object. */
    public AbstractGeomObj getAbstractGeom() {
        return abstractGeom_;
    }

    /** Set geometrics proprieties to a multidimensional object. */
    public void setAbstractGeom(AbstractGeomObj abstractGeom) {
        abstractGeom_ = abstractGeom;
    }

    /** Returns the view transform that is necessary to obtain a visual form of a multidimensional object. */
    public ViewingTransform getViewingTransform() {
        return viewingTransf_;
    }

    /** Set a view transform to a multidimensional object to get it visual form.  */
    public void setViewingTransform(ViewingTransform transf) {
        viewingTransf_ = transf;
    }

    /** Returns the visual attributes of a multidimensional object. */
    public ViewingAttr getViewingAttr() {
        return viewAttr_;
    }

    /** Set a view a attribute to multidimensional object. */
    public void setViewingAttr(ViewingAttr viewAttr) {
        viewAttr_ = viewAttr;
    }

    
}
