/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package wave.multid.view;

import java.awt.Color;
import java.awt.Graphics2D;
import wave.multid.model.*;
import wave.multid.DimMismatchEx;
import wave.multid.CoordsArray;
import java.awt.Shape;
import java.awt.geom.GeneralPath;

public class PolyLine extends ShapedGeometry {
    //
    // Constants
    //
    //
    // Members
    //
    //
    // Constructors
    //

    public PolyLine(MultiGeometryImpl abstractGeom, ViewingTransform transf, ViewingAttr viewAttr) throws DimMismatchEx {
        super(abstractGeom, transf, viewAttr);
    }

    //
    // Accessors/Mutators
    //
    //
    // Methods
    //
    @Override
    protected Shape createShape() throws DimMismatchEx {
        
        GeneralPath path = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
        AbstractPathIterator modelIterator = ((MultiGeometry) getAbstractGeom()).
                getPathIterator(getViewingTransform().viewingMap());
        
        
        while (!modelIterator.isDone()) {
            modelIterator.next();
            CoordsArray[] currSegCoords = new CoordsArray[AbstractSegment.MAX_DEF_POINTS];
            AbstractSegmentAtt currSegAtt = modelIterator.currentSegment(currSegCoords);
            switch (currSegAtt.getType()) {
                case AbstractSegment.SEG_MOVETO:
                    path.moveTo(new Float(currSegCoords[0].getElement(0)).floatValue(),
                            new Float(currSegCoords[0].getElement(1)).floatValue());
                    break;
                case AbstractSegment.SEG_CLOSE:
                    path.closePath();
                    break;
                case AbstractSegment.SEG_LINETO:
                    path.lineTo(new Float(currSegCoords[1].getElement(0)).floatValue(),
                            new Float(currSegCoords[1].getElement(1)).floatValue());
                    break;
                case AbstractSegment.SEG_QUADTO:
                    path.quadTo(new Float(currSegCoords[1].getElement(0)).floatValue(), new Float(currSegCoords[1].getElement(1)).floatValue(),
                            new Float(currSegCoords[2].getElement(0)).floatValue(),
                            new Float(currSegCoords[2].getElement(1)).floatValue());
                    break;
                case AbstractSegment.SEG_CUBICTO:
                    path.curveTo(new Float(currSegCoords[2].getElement(0)).floatValue(), new Float(currSegCoords[2].getElement(1)).floatValue(),
                            new Float(currSegCoords[1].getElement(0)).floatValue(), new Float(currSegCoords[1].getElement(1)).floatValue(),
                            new Float(currSegCoords[0].getElement(0)).floatValue(),
                            new Float(currSegCoords[0].getElement(1)).floatValue());
                    break;
            }
        }
        return path;
    }
    
    @Override
    public void draw(Graphics2D g) {
        
        
        Color previous = g.getColor();
        g.setColor(getViewingAttr().getColor());
        g.draw(getShape());
        if (getViewingAttr().isSelected()) {
            
            Color selectionColor = new Color(previous.getRed(), previous.getGreen(), previous.getBlue(), 60);
            g.setColor(selectionColor);
            g.fill(getShape());
        }
        g.setColor(previous);
    }
}
