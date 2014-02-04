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

public class HugoniotCurveView extends GeomObjView {

    //
    // Constructor
    //
    public HugoniotCurveView(HugoniotCurveGeom abstractGeom,
            ViewingTransform transf,
            ViewingAttr viewAttr) throws DimMismatchEx {
        super(abstractGeom, transf, viewAttr);

    }

    //
    // Accessors/Mutators
    //
    /**
     * Set a view a attribute to multidimensional object.
     */
    @Override
    public void setViewingAttr(ViewingAttr viewAttr) {
        Iterator geomListIterator = ((HugoniotCurveGeom) getAbstractGeom()).getRealSegIterator();
        while (geomListIterator.hasNext()) {
            HugoniotSegGeom geomObj = (HugoniotSegGeom) geomListIterator.next();
            geomObj.viewingAttr().setVisible(viewAttr.isVisible());
            geomObj.viewingAttr().setSelected(viewAttr.isSelected());
        }
    }

    @Override
    public void draw(Graphics2D g) {
        super.draw(g);

        HugoniotCurveGeom shockGeom = (HugoniotCurveGeom) getAbstractGeom();
        Iterator<GraphicsUtil> annotationIterator = shockGeom.getAnnotationIterator();

        while (annotationIterator.hasNext()) {
            GraphicsUtil graphicsUtil = annotationIterator.next();
            graphicsUtil.getViewingAttr().setVisible(shockGeom.viewingAttr().isVisible());
            g.setColor(graphicsUtil.getViewingAttr().getColor());
            graphicsUtil.draw(g);

        }

    }

    //Original update method
    public void update() {
        viewList_.clear();
        Iterator geomListIterator = ((HugoniotCurveGeom) getAbstractGeom()).getRealSegIterator();
        while (geomListIterator.hasNext()) {
            HugoniotSegGeom geomObj = (HugoniotSegGeom) geomListIterator.next();
            try {
                viewList_.add(geomObj.createView(getViewingTransform()));
            } catch (DimMismatchEx dex) {
                dex.printStackTrace();
            }
        }
    }
}
