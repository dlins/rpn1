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
import java.util.List;
import rpn.RPnPhaseSpacePanel;
import wave.multid.view.ViewingAttr;
import wave.multid.view.ViewingTransform;

/**
 *
 * @author edsonlan
 */
public abstract class GraphicsUtil {

    protected List<Object> wcObjects_;
    private ViewingTransform viewingTransform_;
    private ViewingAttr viewAttr_;
    private Shape shape_;


    public GraphicsUtil(List<Object> wcObjects, ViewingTransform viewingTransform, ViewingAttr viewAttr) {
        this.wcObjects_ = wcObjects;
        this.viewingTransform_ = viewingTransform;
        viewAttr_ = viewAttr;
        setShape(createShape());
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

    public void setShape(Shape createShape) {
        shape_ = createShape;

    }

    public void setViewingAttribute(ViewingAttr viewingAttr) {
        viewAttr_ = viewingAttr;
    }

    public Shape getShape() {
        return shape_;
    }
    
    public ViewingAttr getViewingAttr(){
        return viewAttr_;
    }

    protected ViewingTransform getViewingTransform() {
        return viewingTransform_;
    }

    public void update(ViewingTransform viewingTransform) {
        viewingTransform_ = viewingTransform;
        setShape(createShape());
    }

    // ---------------
    public void update(ViewingAttr viewAttr) {
        viewAttr_ = viewAttr;
        setShape(createShape());
    }
    // ---------------

    

    public abstract Shape createShape();

    public abstract Path2D.Double getWCObject();

    protected abstract void drawSelected(Graphics2D g);
}
