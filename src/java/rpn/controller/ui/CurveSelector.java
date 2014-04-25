/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.controller.ui;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.List;
import rpn.RPnCurvesList;
import rpn.RPnPhaseSpacePanel;
import rpn.component.RpGeomFactory;
import rpn.component.RpGeometry;
import rpnumerics.RPnCurve;
import wave.multid.Coords2D;
import wave.multid.CoordsArray;
import wave.multid.Space;
import wave.multid.view.ViewingTransform;
import wave.util.BoxND;
import wave.util.RealVector;

public class CurveSelector extends RPn2DMouseController {

    private RpGeometry selectedGeometry_;

    public CurveSelector(RpGeometry selectedGeometry) {
        selectedGeometry_ = selectedGeometry;
    }

    @Override
    public void mouseMoved(MouseEvent me) {
        if (selectedGeometry_ != null) {

            RPnPhaseSpacePanel panel = (RPnPhaseSpacePanel) me.getSource();

            int x = me.getX();
            int y = me.getY();

            Coords2D dcCoords = new Coords2D(x, y);


            CoordsArray coordsWC = new CoordsArray(new Space("", rpnumerics.RPNUMERICS.domainDim()));

            panel.scene().getViewingTransform().dcInverseTransform(dcCoords, coordsWC);


            RealVector wcCoordsVector = new RealVector(coordsWC.getCoords());

            RealVector min = new RealVector(2);
            min.setElement(0, wcCoordsVector.getElement(0) + 0.01);
            min.setElement(1, wcCoordsVector.getElement(1) + 0.01);
            BoxND box = new BoxND(wcCoordsVector, min);



            Iterator geomObjIterator = UIController.instance().getActivePhaseSpace().getGeomObjIterator();

            while (geomObjIterator.hasNext()) {
                Object object = geomObjIterator.next();

                RpGeometry geometry = (RpGeometry) object;

                RpGeomFactory geomFactory = geometry.geomFactory();

                RPnCurve curve = (RPnCurve) geomFactory.geomSource();

                if (curve.intersect(box)) {
                    selectedGeometry_ = geometry;
                }

            }


            ViewingTransform viewingTransform = panel.scene().getViewingTransform();

            Coords2D wcCoords = new Coords2D(wcCoordsVector.getElement(0), wcCoordsVector.getElement(1));

            Coords2D painelCoords = new Coords2D();

            viewingTransform.viewPlaneTransform(wcCoords, painelCoords);

            Point point = new Point((int) painelCoords.getX(), (int) painelCoords.getY());

            List pointMarkBuffer = panel.getCastedUI().pointMarkBuffer();

            if (pointMarkBuffer.isEmpty()) {
                pointMarkBuffer.add(point);
            } else {
                pointMarkBuffer.set(pointMarkBuffer.size() - 1, point);
            }

            UIController.instance().panelsUpdate();


        }




    }

    @Override
    public void mousePressed(MouseEvent me) {


        if (!UIController.instance().getSelectedGeometriesList().contains(selectedGeometry_)) {
            UIController.instance().getSelectedGeometriesList().add(selectedGeometry_);
            selectedGeometry_.setSelected(true);
        } else {
            UIController.instance().getSelectedGeometriesList().remove(selectedGeometry_);
            selectedGeometry_.setSelected(false);
        }

        UIController.instance().getActivePhaseSpace().updateCurveSelection();
        
        Iterator curvesListIterator = UIController.instance().getActivePhaseSpace().curvesListIterator();
        while (curvesListIterator.hasNext()) {
            Object object = curvesListIterator.next();
            RPnCurvesList curvesList = (RPnCurvesList)object;
            curvesList.update();
            
            
        }
        



    }

    public void mouseClicked(MouseEvent me) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mouseReleased(MouseEvent me) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mouseEntered(MouseEvent me) {
    }

    @Override
    public void mouseExited(MouseEvent me) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mouseDragged(MouseEvent me) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }
}
