/*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package wave.multid.view;

import wave.multid.model.*;
import wave.multid.map.Map;
import wave.multid.DimMismatchEx;
import wave.multid.CoordsArray;
import java.awt.Shape;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.AffineTransform;

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
    protected Shape createShape() throws DimMismatchEx {
        GeneralPath path = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
        AbstractPathIterator modelIterator = ((MultiGeometry)getAbstractGeom()).
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
}
