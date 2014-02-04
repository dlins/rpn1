package rpn.component.util;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.font.GlyphVector;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D.Double;
import java.util.List;
import rpn.RPnPhaseSpacePanel;
import wave.multid.Coords2D;
import wave.multid.CoordsArray;
import wave.multid.view.ViewingAttr;
import wave.multid.view.ViewingTransform;
import rpnumerics.RPNUMERICS;
import wave.util.RealVector;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author moreira
 */
public class LinePlotted extends GraphicsUtil {

    public static RPnPhaseSpacePanel panel_;

    public LinePlotted(List<Object> wcObjects, ViewingTransform viewingTransform, ViewingAttr viewAttr) {
        super(wcObjects, viewingTransform, viewAttr);
    }

    @Override
    public Shape createShape() {

        int[] compIndex = panel_.scene().getViewingTransform().projectionMap().getCompIndexes();
        int biggestIndex = Math.max(compIndex[0], compIndex[1]);
        int smallestIndex = Math.min(compIndex[0], compIndex[1]);

        int dim = RPNUMERICS.domainDim();
        Line2D line = (Line2D) wcObjects_.get(0);

        Coords2D dcPoint1 = new Coords2D();
        Coords2D dcPoint2 = new Coords2D();

        RealVector point1 = new RealVector(dim);
        RealVector point2 = new RealVector(dim);

        point1.setElement(smallestIndex, line.getX1());
        point1.setElement(biggestIndex, line.getY1());

        point2.setElement(smallestIndex, line.getX2());
        point2.setElement(biggestIndex, line.getY2());

        CoordsArray wcPoint1 = new CoordsArray(point1);
        CoordsArray wcPoint2 = new CoordsArray(point2);
        getViewingTransform().viewPlaneTransform(wcPoint1, dcPoint1);
        getViewingTransform().viewPlaneTransform(wcPoint2, dcPoint2);

        Line2D.Double result = new Line2D.Double(dcPoint1.getX(), dcPoint1.getY(), dcPoint2.getX(), dcPoint2.getY());

        return result;
    }

    @Override
    public Double getWCObject() {
        return null;
    }

    @Override
    protected void drawSelected(Graphics2D g) {
        g.setStroke(new BasicStroke(10f));

    }

    @Override
    public void draw(Graphics2D g) {

        Font f = new Font("Arial", Font.BOLD, 15);

        GlyphVector v = f.createGlyphVector(g.getFontRenderContext(), (String)wcObjects_.get(1));
        
        Line2D line  = (Line2D.Double)getShape();
        

        g.draw(getShape());
        
        g.drawGlyphVector(v,(float) line.getX2(),(float) line.getY2());
    }

}
