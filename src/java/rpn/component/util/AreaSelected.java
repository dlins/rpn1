/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.util.List;
import rpn.RPnPhaseSpacePanel;
import wave.multid.Coords2D;
import wave.multid.CoordsArray;
import wave.multid.view.ViewingAttr;
import wave.multid.view.ViewingTransform;
import rpnumerics.RPNUMERICS;
import wave.util.RealVector;

public class AreaSelected extends GraphicsUtil {

    public AreaSelected(List<Object> wcObjects, ViewingTransform viewingTransform, ViewingAttr viewAttr) {
        super(wcObjects, viewingTransform, viewAttr);
    }

    @Override
    public Shape createShape() {

        PathIterator iterator = ((Path2D.Double) getWCObject()).getPathIterator(null);
        Polygon selectedPolygon = new Polygon();

        while (!iterator.isDone()) {

            double[] segmentArray = new double[RPNUMERICS.domainDim()];      // *** Estava hard

            int segment = iterator.currentSegment(segmentArray);
            if (segment != PathIterator.SEG_CLOSE) {

                Coords2D dcSelectionPoint = new Coords2D(0, 0);
                CoordsArray wcSelectionPoint = new CoordsArray(segmentArray);

                getViewingTransform().viewPlaneTransform(wcSelectionPoint, dcSelectionPoint);

                selectedPolygon.addPoint((int) dcSelectionPoint.getX(), (int) dcSelectionPoint.getY());   // --- original
            }

            iterator.next();
        }

        return selectedPolygon;

    }

    @Override
    public Path2D.Double getWCObject() {
        Path2D.Double wcPath = (Path2D.Double) wcObjects_.get(0);
        return wcPath;

    }
    
    
     public void draw(Graphics2D g) {

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
    protected void drawSelected(Graphics2D g) {

        Color previous = getViewingAttr().getColor();

        Color fillColor = new Color(previous.getRed(), previous.getGreen(), previous.getBlue(), 60);
        g.setColor(fillColor);
        g.fill(getShape());

    }
    
    
    public String toXML(){
          
        StringBuffer buffer = new StringBuffer();

        buffer.append("<CURVESELECTION>\n");
        

        List<RealVector> vertices = getWCVertices();
        
        for (int i = 0; i < vertices.size(); i++) {
            buffer.append(vertices.get(i).toXML());
        }
       
        buffer.append("<\\CURVESELECTION>");

        return buffer.toString();
        
        
    }
    
}
