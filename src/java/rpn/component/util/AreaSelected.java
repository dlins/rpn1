/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.font.GlyphVector;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.List;
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

    public AreaSelected(RealVector[] vertices, ViewingTransform viewingTransform, ViewingAttr viewingAttr) {

        super(createPath2D(vertices), viewingTransform, viewingAttr);
        

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

        if (!id_.isEmpty()) {
        
            RealVector upperLeftVertice = getWCVertices().get(3);
            Coords2D dcSelectionPoint = new Coords2D(0, 0);
            CoordsArray wcSelectionPoint = new CoordsArray(upperLeftVertice);

            getViewingTransform().viewPlaneTransform(wcSelectionPoint, dcSelectionPoint);
                        
            dcSelectionPoint.setElement(0, dcSelectionPoint.getX()+20);
            dcSelectionPoint.setElement(1, dcSelectionPoint.getY()+20);
            
            Font f = new Font("Arial", Font.BOLD, 15);

            GlyphVector v = f.createGlyphVector(g.getFontRenderContext(), id_);

            g.drawGlyphVector(v,(float) dcSelectionPoint.getX(), (float) dcSelectionPoint.getY());
        }

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

   

    @Override
    public String toXML() {

        StringBuilder buffer = new StringBuilder();

        buffer.append("<CURVESELECTION>\n");

        List<RealVector> vertices = getWCVertices();

        for (int i = 0; i < vertices.size(); i++) {
            buffer.append(vertices.get(i).toXML());
        }

        buffer.append("\n</CURVESELECTION>\n");

        return buffer.toString();

    }

    private static List<Object> createPath2D(RealVector[] vertices) {

        Path2D.Double selectionPath = new Path2D.Double();

        ArrayList<Object> wcList = new ArrayList<Object>();

        selectionPath.moveTo(vertices[0].getElement(0), vertices[0].getElement(1));

        selectionPath.lineTo(vertices[1].getElement(0), vertices[1].getElement(1));

        selectionPath.lineTo(vertices[2].getElement(0), vertices[2].getElement(1));

        selectionPath.lineTo(vertices[3].getElement(0), vertices[3].getElement(1));

        selectionPath.closePath();

        wcList.add(selectionPath);

        return wcList;

    }

    public ArrayList<RealVector> getDCVertices() {
        
        
         PathIterator iterator = ((Path2D.Double) wcObjects_.get(0)).getPathIterator(null);
         ArrayList<RealVector> dcCoordsArray = new ArrayList<RealVector>();

           double[] segmentArray = new double[RPNUMERICS.domainDim()];   
        while (!iterator.isDone()) {

            int segment = iterator.currentSegment(segmentArray);
            if (segment != PathIterator.SEG_CLOSE) {

                Coords2D dcSelectionPoint = new Coords2D();
                CoordsArray wcSelectionPoint = new CoordsArray(segmentArray);
                getViewingTransform().viewPlaneTransform(wcSelectionPoint, dcSelectionPoint);
                RealVector dcPoint = new RealVector(dcSelectionPoint.getCoords());
                dcCoordsArray.add(dcPoint);
                                
            }

            iterator.next();
        }

        return dcCoordsArray;

    }
    
    
    

}
