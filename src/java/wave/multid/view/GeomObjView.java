/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package wave.multid.view;

import wave.multid.model.AbstractGeomObj;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.List;

/** The main class of the view package. This class declares the basic methods to create a visual form of a multidimensional object. A GeomObjView object contains the attributes and the transformation associated with a visual form of a multidimensional object. */
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
        update();

    }

    /** Updates the visual form of a multidimensional object */
    public abstract void update();

    /** Draw a  multidimensional object */
    public void draw(Graphics2D g) {

        for (int i = 0; i < viewList_.size(); i++) {
            ((GeomObjView) viewList_.get(i)).draw(g);
        }
    }

    public boolean intersect(Polygon polygon) {
        ArrayList<double[]> polygonVertices = new ArrayList<double[]>();

        PathIterator polygonIterator = polygon.getPathIterator(null);

        while (!polygonIterator.isDone()) {

            double[] vertexArray = new double[2];
            int segment = polygonIterator.currentSegment(vertexArray);
            if (segment != PathIterator.SEG_CLOSE) {

                polygonVertices.add(vertexArray);

            }
            polygonIterator.next();
        }

        for (Object object : viewList_) {

            PolyLine segment = (PolyLine) object;
            PathIterator segmentPath = segment.getShape().getPathIterator(null);

            ArrayList<double[]> segmentPoints = new ArrayList<double[]>();

            double[] segmentCoords = new double[2];

            while (!segmentPath.isDone()) {
                segmentPath.currentSegment(segmentCoords);
                segmentPoints.add(segmentCoords);
                segmentPath.next();
            }


            if ( segmentIntersectEdge(polygonVertices, segmentPoints)) {
                return true;
            }

        }
        return false;


    }

        
    
    
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
        System.out.println("Segmentos dentro: " + segmentIndex.size());
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

    private boolean segmentIntersectEdge(List<double[]> polygonVertices, List<double[]> lineSegment) {

        for (int i = 0; i < polygonVertices.size(); i++) {
            double[] polygonVertex1 = polygonVertices.get(i);
            double[] polygonVertex2 = null;
            if (i == polygonVertices.size() - 1) {
                polygonVertex2 = polygonVertices.get(0);
            } else {
                polygonVertex2 = polygonVertices.get(i + 1);
            }

            boolean intersect = Line2D.linesIntersect(polygonVertex1[0], polygonVertex1[1], polygonVertex2[0], polygonVertex2[1], lineSegment.get(0)[0],
                    lineSegment.get(0)[1], lineSegment.get(1)[0], lineSegment.get(1)[1]);

            if (intersect) {
                return intersect;
            }


        }
        return false;
    }
}
