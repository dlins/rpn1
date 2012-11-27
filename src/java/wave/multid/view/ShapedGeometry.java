/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package wave.multid.view;

import wave.multid.DimMismatchEx;
import java.awt.Shape;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.PathIterator;
import wave.multid.model.MultiGeometryImpl;

public abstract class ShapedGeometry extends GeomObjView {
    //
    // Members
    //

    private Shape shape_;
    private Box boundaryBox_;

    //
    // Constructors
    //
    // TODO make ViewTransform member of ViewingAttr ?
    protected ShapedGeometry(MultiGeometryImpl abstractGeom, ViewingTransform transf,
            ViewingAttr viewAttr) throws DimMismatchEx {
        super(abstractGeom, transf, viewAttr);

        setShape(createShape());
        setBoundaryBox(createBoundaryBox());

    }

    public Shape getShape() {
        return shape_;
    }

    public void setShape(Shape shape) {
        shape_ = shape;
    }

    public Box getBoundaryBox() {
        return boundaryBox_;
    }

    public void setBoundaryBox(Box box) {
        boundaryBox_ = box;
    }

    //
    // Methods
    //
    protected Box createBoundaryBox() {
        return new Box(getShape(), getViewingTransform());
    }

    protected abstract Shape createShape() throws DimMismatchEx;

    
    /** Tests if the geometry intersects the polygon*/
    public boolean intersect(Polygon area) {

        PathIterator pathIterator = getShape().getPathIterator(null);

        while (!pathIterator.isDone()) {

            double[] segmentArray = new double[2];

            int segment = pathIterator.currentSegment(segmentArray);
            
            if (segment != PathIterator.SEG_MOVETO) {
                if (area.contains(segmentArray[0], segmentArray[1]))
                return true;
            }

            pathIterator.next();
        }

        return false;
    }

    public void draw(Graphics2D g) {
        

        Color previous = g.getColor();
        g.setColor(getViewingAttr().getColor());
        g.draw(getShape());
//        if (getViewingAttr().isSelected()) {
//            getBoundaryBox().draw(g);
//        }
        g.setColor(previous);
    }

    public void update() {
        try {
            setShape(createShape());
        } catch (DimMismatchEx dex) {
            dex.printStackTrace();
        }
    }
}
