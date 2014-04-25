/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rpn.component.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.List;
import rpn.component.RpGeometry;
import wave.multid.view.ViewingAttr;
import wave.multid.view.ViewingTransform;
import wave.util.RealVector;

/**
 *
 * @author edsonlan
 */
public abstract class GraphicsUtil {

    protected List<Object> wcObjects_;
    private ViewingTransform viewingTransform_;
    private ViewingAttr viewAttr_;
    private Shape shape_;
    protected RpGeometry geometry_;
    protected String id_;

    public GraphicsUtil() {
    }

    public GraphicsUtil(List<Object> wcObjects, ViewingTransform viewingTransform, ViewingAttr viewAttr) {
        this.wcObjects_ = wcObjects;
        this.viewingTransform_ = viewingTransform;
        viewAttr_ = viewAttr;
        setShape(createShape());
        id_ = "";
    }

    public void draw(Graphics2D g) {

        Color previousColor = g.getColor();
        g.setColor(viewAttr_.getColor());
        g.draw(getShape());
        Stroke previousStroke = g.getStroke();

        if (viewAttr_.isSelected()) {

            drawSelected(g);

        }

        g.setColor(previousColor);
        g.setStroke(previousStroke);
    }

    public final void setShape(Shape createShape) {
        shape_ = createShape;

    }

    public void setViewingAttribute(ViewingAttr viewingAttr) {
        viewAttr_ = viewingAttr;
    }

    public Shape getShape() {
        return shape_;
    }

    public ViewingAttr getViewingAttr() {
        return viewAttr_;
    }

    protected ViewingTransform getViewingTransform() {
        return viewingTransform_;
    }

    public void update(ViewingTransform viewingTransform) {
        viewingTransform_ = viewingTransform;
        setShape(createShape());
    }

    public void setGeometry(RpGeometry geometry) {
        geometry_ = geometry;
    }

    public RpGeometry getGeometry() {
        return geometry_;
    }

    // ---------------
    public void update(ViewingAttr viewAttr) {
        viewAttr_ = viewAttr;
        setShape(createShape());
    }
    // ---------------

    public List<RealVector> getWCVertices() {

        List<RealVector> areaPointsList = new ArrayList<RealVector>();
        Path2D.Double wcObject = getWCObject();
        PathIterator pathIterator = wcObject.getPathIterator(null);

        double[] segmentArray = new double[2];

        while (!pathIterator.isDone()) {

            int segment = pathIterator.currentSegment(segmentArray);
            if (segment != PathIterator.SEG_CLOSE) {
                RealVector testeSegment = new RealVector(segmentArray);
                areaPointsList.add(testeSegment);
            }

            pathIterator.next();

        }

        return areaPointsList;

    }

    public void setID(String id) {
        id_ = id;
    }

    public String getID() {
        return id_;
    }

    public String toXML() {

        StringBuffer buffer = new StringBuffer();

        buffer.append("<CURVESELECTION>\n");

        List<RealVector> vertices = getWCVertices();

        for (int i = 0; i < vertices.size(); i++) {
            buffer.append(vertices.get(i).toXML());
        }

        buffer.append("<\\CURVESELECTION>");

        return buffer.toString();

    }

    public abstract Shape createShape();

    public abstract Path2D.Double getWCObject();

    protected abstract void drawSelected(Graphics2D g);
}
