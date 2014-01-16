/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.controller.ui;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.List;
import rpn.RPnPhaseSpacePanel;
import rpn.component.RpGeomFactory;
import rpn.component.RpGeometry;
import rpn.parser.RPnDataModule;
import rpnumerics.RPnCurve;
import wave.multid.Coords2D;
import wave.multid.CoordsArray;
import wave.multid.Space;
import wave.multid.view.ViewingTransform;
import wave.util.RealVector;

public class ClosestPointPlotter extends RPn2DMouseController {

    private RpGeometry selectedGeometry_;

    public ClosestPointPlotter(RpGeometry selectedGeometry) {
        selectedGeometry_ = selectedGeometry;
    }

    public void mouseMoved(MouseEvent me) {

        RPnPhaseSpacePanel panel = (RPnPhaseSpacePanel) me.getSource();

        int x = me.getX();
        int y = me.getY();

        Coords2D dcCoords = new Coords2D(x, y);


        CoordsArray coordsWC = new CoordsArray(new Space("", rpnumerics.RPNUMERICS.domainDim()));

        panel.scene().getViewingTransform().dcInverseTransform(dcCoords, coordsWC);


        RealVector wcCoordsVector = new RealVector(coordsWC.getCoords());


        RpGeomFactory geomFactory = selectedGeometry_.geomFactory();


        RPnCurve curve = (RPnCurve) geomFactory.geomSource();

//        RPnCurve testePoint = (RPnCurve) selectedGeometry_.geomFactory().geomSource();

        RealVector closestPoint = curve.findClosestPoint(wcCoordsVector);
//        System.out.println("Ponto mais proximo teste: "+ testePoint.getClass().getName()+" "+curve.findClosestPoint(wcCoordsVector));

        ViewingTransform viewingTransform = panel.scene().getViewingTransform();

        Coords2D wcCoords = new Coords2D(closestPoint.getElement(0), closestPoint.getElement(1));

        Coords2D painelCoords = new Coords2D();

        viewingTransform.viewPlaneTransform(wcCoords, painelCoords);

        Point point = new Point((int) painelCoords.getX(), (int) painelCoords.getY());

        List pointMarkBuffer = panel.getCastedUI().pointMarkBuffer();
        
        if (pointMarkBuffer.isEmpty()) {
            pointMarkBuffer.add(point);
        } else {
            pointMarkBuffer.set(pointMarkBuffer.size() - 1, point);
        }


        panel.repaint();


    }

    public void mousePressed(MouseEvent me) {
//        RPnPhaseSpacePanel panel = (RPnPhaseSpacePanel) me.getSource();
//
//        int x = me.getX();
//        int y = me.getY();
//
//        Coords2D dcCoords = new Coords2D(x, y);
//
//
//        CoordsArray coordsWC = new CoordsArray(new Space("", rpnumerics.RPNUMERICS.domainDim()));
//
//        panel.scene().getViewingTransform().dcInverseTransform(dcCoords, coordsWC);
//
//
//        RealVector wcCoordsVector = new RealVector(rpnumerics.RPNUMERICS.domainDim());
//
//
//        for (int i = 0; i < rpnumerics.RPNUMERICS.domainDim(); i++) {
//            wcCoordsVector.setElement(i, coordsWC.getElement(i));
//        }
//
//
//
//
//        RpGeometry geometry = RPnDataModule.PHASESPACE.findClosestGeometry(wcCoordsVector);
//
//        RpGeomFactory geomFactory = geometry.geomFactory();
//
//
//        RPnCurve curve = (RPnCurve) geomFactory.geomSource();
//
//        RPnCurve testePoint = (RPnCurve) selectedGeometry_.geomFactory().geomSource();
//
//        
//        System.out.println("Ponto mais proximo teste: "+ testePoint.getClass().getName()+" "+testePoint.findClosestPoint(wcCoordsVector));
//
//
//        RealVector closestPoint = curve.findClosestPoint(wcCoordsVector);
//        UIController.instance().userInputComplete(closestPoint);
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
