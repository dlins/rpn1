/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Path2D.Double;
import java.awt.geom.Rectangle2D;
import java.util.List;
import rpn.RPnPhaseSpacePanel;
import wave.multid.Coords2D;
import wave.multid.CoordsArray;
import wave.multid.view.ViewingAttr;
import wave.multid.view.ViewingTransform;
import wave.util.RealVector;

public class Point extends GraphicsUtil {

    public RPnPhaseSpacePanel panel_;

    public Point(List<Object> wcObjects, ViewingTransform viewingTransform, ViewingAttr viewAttr) {
        super(wcObjects, viewingTransform, viewAttr);

    }

    @Override
    public Shape createShape() {
        RealVector center = getWCVertices().get(0);

        CoordsArray centerCoords = new CoordsArray(center);

        Coords2D dcCoords = new Coords2D();

        getViewingTransform().viewPlaneTransform(centerCoords, dcCoords);

        Rectangle2D.Double rectangle = new Rectangle2D.Double (dcCoords.getX(), dcCoords.getY(), 3, 3);
        
        return rectangle;

    }
    
    
    
    public void draw(Graphics2D g) {

        Color previousColor = g.getColor();
        g.setColor(getViewingAttr().getColor());
        g.draw(getShape());
        Stroke previousStroke = g.getStroke();

        if (getViewingAttr().isSelected()) {

            drawSelected(g);

        }
        
        g.fill(getShape());

        g.setColor(previousColor);
        g.setStroke(previousStroke);
    }
    
    

    @Override
    protected void drawSelected(Graphics2D g) {
        
         Color previousColor = g.getColor();
        g.setColor(getViewingAttr().getColor());
        g.draw(getShape());
        Stroke previousStroke = g.getStroke();

        if (getViewingAttr().isSelected()) {

            drawSelected(g);

        }
        
     

        g.setColor(previousColor);
        g.setStroke(previousStroke);
        
    

    }

    @Override
    public Double getWCObject() {

        Path2D.Double path = new Double();
        CoordsArray center = (CoordsArray) wcObjects_.get(0);

        path.moveTo(center.getElement(0), center.getElement(1));

        return path;
    }

}
