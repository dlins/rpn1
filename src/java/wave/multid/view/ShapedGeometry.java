/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package wave.multid.view;

import wave.multid.DimMismatchEx;
import wave.multid.model.AbstractGeomObj;
import java.awt.Shape;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import rpnumerics.Area;
import wave.multid.Coords2D;
import wave.multid.CoordsArray;
import wave.multid.model.MultiGeometryImpl;
import wave.util.RealVector;

public abstract class ShapedGeometry implements GeomObjView {
    //
    // Members
    //

    private ViewingTransform viewingTransf_;
    private Shape shape_;
    private Box boundaryBox_;
    private AbstractGeomObj abstractGeom_;
    private ViewingAttr viewAttr_;

    //
    // Constructors
    //
    // TODO make ViewTransform member of ViewingAttr ?
    protected ShapedGeometry(MultiGeometryImpl abstractGeom, ViewingTransform transf,
            ViewingAttr viewAttr) throws DimMismatchEx {
        setAbstractGeom(abstractGeom);
        setViewingTransform(transf);
        setViewingAttr(viewAttr);
        setShape(createShape());
        setBoundaryBox(createBoundaryBox());

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

    public Shape getShape() {
        return shape_;
    }

    public void setShape(Shape shape) {
        shape_ = shape;
    }

    public ViewingTransform getViewingTransform() {
        return viewingTransf_;
    }

    public void setViewingTransform(ViewingTransform transf) {
        viewingTransf_ = transf;
    }

    public Box getBoundaryBox() {
        return boundaryBox_;
    }

    public void setBoundaryBox(Box box) {
        boundaryBox_ = box;
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
    protected Box createBoundaryBox() {
        return new Box(getShape(), getViewingTransform());
    }

    protected abstract Shape createShape() throws DimMismatchEx;

    public boolean intersects(Area area) {

        ViewingTransform viewTransform = getViewingTransform();

        RealVector areaTopRight = area.getTopRight();
        RealVector areaDownLeft = area.getDownLeft();

        CoordsArray topRightWC = new CoordsArray(areaTopRight);

        CoordsArray downLeftWC = new CoordsArray(areaDownLeft);

        Coords2D topRightDC = new Coords2D();

        Coords2D downLeftDC = new Coords2D();

        viewTransform.viewPlaneTransform(topRightWC, topRightDC);
        viewTransform.viewPlaneTransform(downLeftWC, downLeftDC);

        double x = downLeftDC.getX();
        double y = topRightDC.getY();

        double w = topRightDC.getX() - downLeftDC.getX();
        double h = topRightDC.getY() + downLeftDC.getY();

        Rectangle2D.Double areaRectangle = new Rectangle2D.Double(x, y, w, h);

        return getShape().intersects(areaRectangle);

    }

    public void draw(Graphics2D g) {
        Color previous = g.getColor();
        g.setColor(getViewingAttr().getColor());
        g.draw(getShape());
        if (getViewingAttr().isSelected()) {
            getBoundaryBox().draw(g);
        }
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
