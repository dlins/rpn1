package rpn.component;

import java.awt.Graphics2D;
import java.util.Iterator;
import rpn.component.util.GraphicsUtil;

import wave.multid.DimMismatchEx;
import wave.multid.view.GeomObjView;
import wave.multid.view.ViewingAttr;
import wave.multid.view.ViewingTransform;

public class BifurcationCurveView extends GeomObjView {

    public BifurcationCurveView(BifurcationCurveGeom abstractGeom,
            ViewingTransform transf,
            ViewingAttr viewAttr) throws DimMismatchEx {

        super(abstractGeom, transf, viewAttr);

    }
   

    //Original update method
    public void update() {
        viewList_.clear();
        Iterator geomListIterator = ((BifurcationCurveGeom) getAbstractGeom()).getBifurcationSegmentsIterator();
        while (geomListIterator.hasNext()) {
            RealSegGeom geomObj = (RealSegGeom) geomListIterator.next();
            try {
                viewList_.add(geomObj.createView(getViewingTransform()));
            } catch (DimMismatchEx dex) {
                dex.printStackTrace();
            }
        }
    }
    
     @Override
    public void draw(Graphics2D g){
        
        super.draw(g);
        
        BifurcationCurveGeom doubleContactGeom = (BifurcationCurveGeom)getAbstractGeom();
        
         Iterator<GraphicsUtil> annotationIterator = doubleContactGeom.getAnnotationIterator();

        while (annotationIterator.hasNext()) {
            GraphicsUtil graphicsUtil = annotationIterator.next();
            graphicsUtil.update(getViewingTransform());
            graphicsUtil.getViewingAttr().setVisible(doubleContactGeom.viewingAttr().isVisible());
            g.setColor(graphicsUtil.getViewingAttr().getColor());
            graphicsUtil.draw(g);

        }
        
    }
    
    
    
    
}
