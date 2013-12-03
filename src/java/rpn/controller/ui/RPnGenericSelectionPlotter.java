/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.controller.ui;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import rpn.RPnPhaseSpacePanel;
import wave.multid.Coords2D;
import wave.multid.CoordsArray;
import wave.multid.Space;
import wave.multid.view.ViewingAttr;
import rpnumerics.RPNUMERICS;
import wave.multid.model.MultiPolyLine;
import wave.multid.model.MultiPolygon;
import wave.util.RealVector;

public class RPnGenericSelectionPlotter extends RPn2DMouseController {

    private Point cursorPos_;
    private boolean newPolygon_ = false;
    private List<CoordsArray> lineCoordsList_;

    public RPnGenericSelectionPlotter() {
        lineCoordsList_ = new ArrayList<CoordsArray>();
    }
    
    
    

    @Override
    public void mouseMoved(MouseEvent me) {

        if (!lineCoordsList_.isEmpty()) {
            RPnPhaseSpacePanel panel = (RPnPhaseSpacePanel) me.getSource();

            List<CoordsArray> tempCoordsList_ = new ArrayList<CoordsArray>();
            cursorPos_ = new Point(me.getX(), me.getY());

            double[] mePosArray = {me.getX(), me.getY()};

            Coords2D mePosDC = new Coords2D(mePosArray);
            CoordsArray mePosWC = new CoordsArray(new Space(" ", RPNUMERICS.domainDim()));

            panel.scene().getViewingTransform().dcInverseTransform(mePosDC, mePosWC);

            tempCoordsList_.clear();

            tempCoordsList_.addAll(lineCoordsList_);
            tempCoordsList_.add(mePosWC);


            ViewingAttr viewingAttr = new ViewingAttr(Color.red);


            CoordsArray[] testeArray = new CoordsArray[tempCoordsList_.size()];


            for (int i = 0; i < testeArray.length; i++) {
                testeArray[i] = tempCoordsList_.get(i);
            }

            panel.setLastGenericSelection(new MultiPolyLine(testeArray, viewingAttr));
            panel.repaint();

        }


    }

    @Override
    public void mousePressed(MouseEvent me) {

        RPnPhaseSpacePanel panel = (RPnPhaseSpacePanel) me.getSource();

        cursorPos_ = new Point(me.getX(), me.getY());

        double[] mePosArray = {me.getX(), me.getY()};

        Coords2D mePosDC = new Coords2D(mePosArray);
        CoordsArray mePosWC = new CoordsArray(new Space(" ", RPNUMERICS.domainDim()));

        panel.scene().getViewingTransform().dcInverseTransform(mePosDC, mePosWC);

        if (me.getButton() == MouseEvent.BUTTON1) {

            lineCoordsList_.add(mePosWC);
        }


        if (me.getButton() == MouseEvent.BUTTON3) {
            lineCoordsList_.add(mePosWC);

            CoordsArray[] testeArray = new CoordsArray[lineCoordsList_.size()];

            ViewingAttr viewingAttr = new ViewingAttr(Color.red);
            for (int i = 0; i < testeArray.length; i++) {
                testeArray[i] = lineCoordsList_.get(i);
            }

            MultiPolygon multiPolygon = new MultiPolygon(testeArray, viewingAttr);

            Vector<RealVector> vertices = new Vector<RealVector>();
            for (int i = 0; i < lineCoordsList_.size(); i++) {
                CoordsArray coordsArray = lineCoordsList_.get(i);
                RealVector testeRealVector = new RealVector(coordsArray.getCoords());
                vertices.add(testeRealVector);
            }

            MultiPolygon convexPolygon = multiPolygon.convexPolygon();
            
            setChanged();
            notifyObservers(convexPolygon);

            convexPolygon.viewingAttr().setColor(Color.green);
            panel.setLastGenericSelection(convexPolygon);

            panel.updateGraphicsUtil();
            panel.repaint();

            lineCoordsList_.clear();


        }

    }

    @Override
    public void mouseClicked(MouseEvent me) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mouseReleased(MouseEvent me) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mouseEntered(MouseEvent me) {
//        throw new UnsupportedOperationException("Not supported yet.");
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
