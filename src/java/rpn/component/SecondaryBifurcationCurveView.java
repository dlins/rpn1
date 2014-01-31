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
import java.util.logging.Level;
import java.util.logging.Logger;
import wave.multid.model.MultiPoint;

public class SecondaryBifurcationCurveView extends GeomObjView {
    //
    // Members
    //

    //
    // Constructor
    //
    public SecondaryBifurcationCurveView(SecondaryBifurcationCurveGeom abstractGeom,
            ViewingTransform transf,
            ViewingAttr viewAttr) throws DimMismatchEx {
        super(abstractGeom, transf, viewAttr);

    }

    @Override
    public void draw(Graphics2D g) {

        g.setColor(getViewingAttr().getColor());

        super.draw(g);
        SecondaryBifurcationCurveGeom doubleContactGeom = (SecondaryBifurcationCurveGeom) getAbstractGeom();
        MultiPoint umbilicPoint = doubleContactGeom.getUmbilicPoint();
        if (umbilicPoint != null) {
            PointMark pointMark;
            try {
                pointMark = new PointMark(umbilicPoint, getViewingTransform(), umbilicPoint.viewingAttr());
                pointMark.getViewingAttr().setVisible(getViewingAttr().isVisible());
                pointMark.draw(g);

            } catch (DimMismatchEx ex) {
                Logger.getLogger(SecondaryBifurcationCurveView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    //Original update method
    public void update() {
        viewList_.clear();
        SecondaryBifurcationCurveGeom secondaryBifurcationGeom = (SecondaryBifurcationCurveGeom) getAbstractGeom();
        Iterator geomListIterator = secondaryBifurcationGeom.getBifurcationSegmentsIterator();
        while (geomListIterator.hasNext()) {
            RealSegGeom geomObj = (RealSegGeom) geomListIterator.next();
            try {
                viewList_.add(geomObj.createView(getViewingTransform()));
            } catch (DimMismatchEx dex) {
                dex.printStackTrace();
            }
        }

    }
}
