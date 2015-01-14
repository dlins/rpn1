/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component.util;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.GlyphVector;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Path2D.Double;
import java.text.NumberFormat;
import java.util.List;
import rpn.RPnPhaseSpacePanel;
import wave.multid.Coords2D;
import wave.multid.CoordsArray;
import wave.multid.view.ViewingAttr;
import wave.multid.view.ViewingTransform;
import wave.util.RealVector;

public class LinePlotted extends GraphicsUtil {

    public RPnPhaseSpacePanel panel_;

    public LinePlotted(List<Object> wcObjects, ViewingTransform viewingTransform, ViewingAttr viewAttr) {
        super(wcObjects, viewingTransform, viewAttr);
    }

    @Override
    public Shape createShape() {

        RealVector curvePoint = (RealVector) wcObjects_.get(0);
        RealVector stringPoint = (RealVector) wcObjects_.get(1);

        RealVector newCurvePoint1 = new RealVector(curvePoint);

        RealVector newStringPoint1 = new RealVector(stringPoint);

//        Adding a gap between the curve and the line and between the line and the string
        newStringPoint1.scale(0.9);

        newCurvePoint1.scale(0.1);

        newCurvePoint1.add(newStringPoint1);

        RealVector newCurvePoint2 = new RealVector(curvePoint);

        RealVector newStringPoint2 = new RealVector(stringPoint);

        newStringPoint2.scale(0.1);

        newCurvePoint2.scale(0.9);

        newStringPoint2.add(newCurvePoint2);

        ///
        Coords2D dcPoint1 = new Coords2D();
        Coords2D dcPoint2 = new Coords2D();

        CoordsArray wcPoint1 = new CoordsArray(newCurvePoint1);
        CoordsArray wcPoint2 = new CoordsArray(newStringPoint2);
        
        
        getViewingTransform().viewPlaneTransform(wcPoint1, dcPoint1);
        getViewingTransform().viewPlaneTransform(wcPoint2, dcPoint2);

        Line2D.Double result = new Line2D.Double(dcPoint1.getX(), dcPoint1.getY(), dcPoint2.getX(), dcPoint2.getY());

        return result;
    }

    @Override
    protected void drawSelected(Graphics2D g) {
        g.setStroke(new BasicStroke(10f));

    }

    @Override
    public void draw(Graphics2D g) {

        Font f = new Font("Arial", Font.BOLD, 15);

        GlyphVector v = f.createGlyphVector(g.getFontRenderContext(), formatter((String) wcObjects_.get(2)));
        RealVector stringPoint = (RealVector) wcObjects_.get(1);

        Coords2D dcPoint2 = new Coords2D();
        CoordsArray wcPoint2 = new CoordsArray(stringPoint);
        getViewingTransform().viewPlaneTransform(wcPoint2, dcPoint2);

        g.draw(getShape());

        g.drawGlyphVector(v, (float) dcPoint2.getX(), (float) dcPoint2.getY());

    }

    @Override
    public Path2D.Double getWCObject() {

       return (Double) wcObjects_;
        
    }
    
     public String toXML() {

        StringBuilder buffer = new StringBuilder();

        for (int i = 0; i < wcObjects_.size()-1; i++) {
            buffer.append(((RealVector)wcObjects_.get(i)).toXML());
        }

        return buffer.toString();

    }
    
    

    private String formatter(String toFormat) {

        NumberFormat formatter = NumberFormat.getInstance();
        formatter.setMaximumFractionDigits(4);
        
        try {
           java.lang.Double number = new java.lang.Double(toFormat);
           String result = formatter.format(number);
           
           return result;
                   
        }
        
        catch (NumberFormatException ex){
            return toFormat;
        }
        

    }

 

}
