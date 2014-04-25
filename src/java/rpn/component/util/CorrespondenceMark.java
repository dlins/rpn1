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
import java.util.logging.Level;
import java.util.logging.Logger;
import rpn.RPnPhaseSpacePanel;
import wave.multid.Coords2D;
import wave.multid.CoordsArray;
import wave.multid.DimMismatchEx;
import wave.multid.model.MultiPoint;
import wave.multid.view.PointMark;
import wave.multid.view.ViewingAttr;
import wave.multid.view.ViewingTransform;
import wave.util.RealVector;

public class CorrespondenceMark extends GraphicsUtil {
    
    private RPnPhaseSpacePanel panel_;
    
    private final MultiPoint point_;
   
    
    public CorrespondenceMark(List<Object> wcObjects, ViewingTransform viewingTransform, ViewingAttr viewAttr) {
        super(wcObjects, viewingTransform, viewAttr);
        
        Coords2D dcPoint2 = new Coords2D();
        CoordsArray wcPoint2 = new CoordsArray((RealVector) wcObjects_.get(0));
        getViewingTransform().viewPlaneTransform(wcPoint2, dcPoint2);
        
        point_ = new MultiPoint(wcPoint2, getViewingAttr());
        
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
        
        try {
            PointMark pointMark = new PointMark(point_,getViewingTransform(),point_.viewingAttr());
            pointMark.draw(g);
        } catch (DimMismatchEx ex) {
            Logger.getLogger(CorrespondenceMark.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    @Override
    public Double getWCObject() {
        return null;
    }
    
}
