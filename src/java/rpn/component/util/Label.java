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
import java.awt.geom.Path2D.Double;
import java.util.List;
import rpn.RPnPhaseSpacePanel;
import wave.multid.Coords2D;
import wave.multid.CoordsArray;
import wave.multid.view.ViewingAttr;
import wave.multid.view.ViewingTransform;
import wave.util.RealVector;

public class Label extends GraphicsUtil {
    
    public RPnPhaseSpacePanel panel_;
    
    public Label(List<Object> wcObjects, ViewingTransform viewingTransform, ViewingAttr viewAttr) {
        super(wcObjects, viewingTransform, viewAttr);
        
    }
    
    @Override
    public Shape createShape() {
        return null;
    }
    
    @Override
    protected void drawSelected(Graphics2D g) {
        g.setStroke(new BasicStroke(10f));
        
    }
    
    @Override
    public void draw(Graphics2D g) {
        
//        Font f = new Font("Arial", Font.BOLD, 15);
//        
//        Coords2D dcPoint2 = new Coords2D();
//        CoordsArray wcPoint2 = new CoordsArray((RealVector) wcObjects_.get(0));
//        getViewingTransform().viewPlaneTransform(wcPoint2, dcPoint2);
//        
//        GlyphVector v = f.createGlyphVector(g.getFontRenderContext(), (String)wcObjects_.get(1));
//        
//        g.drawGlyphVector(v, (float) dcPoint2.getX(), (float) dcPoint2.getY());
    }
    
    @Override
    public Double getWCObject() {
        return null;
    }
    
}
