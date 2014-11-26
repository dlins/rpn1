/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import java.awt.Graphics2D;
import wave.multid.view.*;
import wave.multid.DimMismatchEx;
import java.util.Iterator;
import rpn.component.util.GraphicsUtil;

public class LevelCurveView extends  GeomObjView {
    //
    // Members
    //


    //
    // Constructor
    //
    public LevelCurveView(LevelCurveGeom abstractGeom,
            ViewingTransform transf,
            ViewingAttr viewAttr) throws DimMismatchEx {
        super(abstractGeom, transf, viewAttr);

    }

  

    public void update() {
        viewList_.clear();

        LevelCurveGeom abstractGeom = (LevelCurveGeom) getAbstractGeom();
        Iterator geomListIterator = abstractGeom.getRealSegIterator();
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
    public void draw(Graphics2D g) {
        super.draw(g);

        LevelCurveGeom shockGeom = (LevelCurveGeom) getAbstractGeom();
        Iterator<GraphicsUtil> annotationIterator = shockGeom.getAnnotationIterator();

        while (annotationIterator.hasNext()) {
            GraphicsUtil graphicsUtil = annotationIterator.next();
            graphicsUtil.update(getViewingTransform());
            graphicsUtil.getViewingAttr().setVisible(shockGeom.viewingAttr().isVisible());
            g.setColor(graphicsUtil.getViewingAttr().getColor());
            graphicsUtil.draw(g);

        }

    }
    
    
    
    
    
    
}
