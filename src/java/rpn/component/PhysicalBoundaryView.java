/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Stroke;
import wave.multid.view.*;
import wave.multid.DimMismatchEx;
import java.util.Iterator;
import rpn.component.util.GraphicsUtil;

public class PhysicalBoundaryView extends  GeomObjView {
    //
    // Members
    //

  

    //
    // Constructor
    //
    public PhysicalBoundaryView(PhysicalBoundaryGeom abstractGeom,
            ViewingTransform transf,
            ViewingAttr viewAttr) throws DimMismatchEx {
        super(abstractGeom, transf, viewAttr);
    }

   
    //Original update method
    public void update() {

        viewList_.clear();
        Iterator geomListIterator = ((PhysicalBoundaryGeom) getAbstractGeom()).getRealSegIterator();
        while (geomListIterator.hasNext()) {
           RealSegGeom geomObj = (RealSegGeom) geomListIterator.next();
            try {
                viewList_.add(geomObj.createView(getViewingTransform()));
            } catch (DimMismatchEx dex) {
                dex.printStackTrace();
            }
        }
    }


 /** Draws a  multidimensional object */
    public void draw(Graphics2D g) {
        Stroke previousStroke = g.getStroke();
        
        g.setStroke(new BasicStroke(3));
        
        for (int i = 0; i < viewList_.size(); i++) {
            ((GeomObjView) viewList_.get(i)).draw(g);
        }
        
         g.setStroke(previousStroke);
        
        PhysicalBoundaryGeom shockGeom = (PhysicalBoundaryGeom) getAbstractGeom();
        Iterator<GraphicsUtil> annotationIterator = shockGeom.getAnnotationIterator();

        while (annotationIterator.hasNext()) {
            GraphicsUtil graphicsUtil = annotationIterator.next();
            graphicsUtil.update(getViewingTransform());
            graphicsUtil.getViewingAttr().setVisible(shockGeom.viewingAttr().isVisible());
            g.setColor(graphicsUtil.getViewingAttr().getColor());
            graphicsUtil.draw(g);

        }

        
        
        
        
        
        
        
        
        
        
//        g.setStroke(previousStroke);
    }





}
