/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rpn.component;

import java.awt.Shape;
import java.awt.geom.GeneralPath;
import wave.multid.CoordsArray;
import wave.multid.DimMismatchEx;
import wave.multid.model.AbstractPathIterator;
import wave.multid.model.AbstractSegment;
import wave.multid.model.AbstractSegmentAtt;
import wave.multid.model.MultiGeometry;
import wave.multid.model.MultiGeometryImpl;
import wave.multid.view.ShapedGeometry;
import wave.multid.view.ViewingAttr;
import wave.multid.view.ViewingTransform;

class CharacteristicCurveGeomView extends ShapedGeometry {

    
    public CharacteristicCurveGeomView(MultiGeometryImpl abstractGeom, ViewingTransform transf, ViewingAttr viewAttr) throws DimMismatchEx {
        super(abstractGeom, transf, viewAttr);
        
       
        
    }
    
     protected Shape createShape() throws DimMismatchEx {
         return null;//criar varias polylines
         
     }
    
//     protected Shape createShape() throws DimMismatchEx {
//        GeneralPath path = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
//        AbstractPathIterator modelIterator = ((MultiGeometry)getAbstractGeom()).
//            getPathIterator(getViewingTransform().viewingMap());
//        while (!modelIterator.isDone()) {
//            modelIterator.next();
//            CoordsArray[] currSegCoords = new CoordsArray[AbstractSegment.MAX_DEF_POINTS];
//            AbstractSegmentAtt currSegAtt = modelIterator.currentSegment(currSegCoords);
//            switch (currSegAtt.getType()) {
//                case AbstractSegment.SEG_MOVETO:
//                    path.moveTo(new Float(currSegCoords[0].getElement(0)).floatValue(),
//                        new Float(currSegCoords[0].getElement(1)).floatValue());
//                    break;
//                case AbstractSegment.SEG_CLOSE:
//                    path.closePath();
//                    break;
//                case AbstractSegment.SEG_LINETO:
//                    path.lineTo(new Float(currSegCoords[1].getElement(0)).floatValue(),
//                        new Float(currSegCoords[1].getElement(1)).floatValue());
//                    break;
//                case AbstractSegment.SEG_QUADTO:
//                    path.quadTo(new Float(currSegCoords[1].getElement(0)).floatValue(), new Float(currSegCoords[1].getElement(1)).floatValue(),
//                        new Float(currSegCoords[2].getElement(0)).floatValue(),
//                        new Float(currSegCoords[2].getElement(1)).floatValue());
//                    break;
//                case AbstractSegment.SEG_CUBICTO:
//                    path.curveTo(new Float(currSegCoords[2].getElement(0)).floatValue(), new Float(currSegCoords[2].getElement(1)).floatValue(),
//                        new Float(currSegCoords[1].getElement(0)).floatValue(), new Float(currSegCoords[1].getElement(1)).floatValue(),
//                        new Float(currSegCoords[0].getElement(0)).floatValue(),
//                        new Float(currSegCoords[0].getElement(1)).floatValue());
//                    break;
//            }
//        }
//        return path;
//    }
    

   
    
   

   
}
