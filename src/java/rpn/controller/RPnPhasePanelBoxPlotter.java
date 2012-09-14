/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rpn.controller;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;

import rpn.RPnPhaseSpacePanel;
import wave.multid.Coords2D;
import wave.multid.CoordsArray;
import wave.multid.Space;
import wave.multid.view.ViewingTransform;

/**
 *
 * @author edsonlan
 */
public class RPnPhasePanelBoxPlotter implements MouseMotionListener, MouseListener {

    private Point cursorPos_;
    private Polygon tempRectangle;
    private boolean addRectangle_ = false;

    public void mouseMoved(MouseEvent me) {

        if (addRectangle_) {
            RPnPhaseSpacePanel panel = (RPnPhaseSpacePanel) me.getSource();
            ViewingTransform viewingTransform = panel.scene().getViewingTransform();


            double[] cursorPosArray = {cursorPos_.x, cursorPos_.y};
            double [] mePosArray = {me.getX(),me.getY()};

            CoordsArray cursorPosWC = new CoordsArray(new Space(" ", 2));
            CoordsArray mePosWC = new CoordsArray(new Space(" ", 2));

            Coords2D cursorPosDC = new Coords2D(cursorPosArray);
            Coords2D mePosDC = new Coords2D(mePosArray);

            viewingTransform.dcInverseTransform(cursorPosDC, cursorPosWC);
            viewingTransform.dcInverseTransform(mePosDC, mePosWC);
            
            Path2D.Double selectionPath = new Path2D.Double();

            selectionPath.moveTo(cursorPosWC.getElement(0), cursorPosWC.getElement(1));
            
            selectionPath.lineTo(mePosWC.getElement(0),cursorPosWC.getElement(1));

            
            selectionPath.lineTo(mePosWC.getElement(0), mePosWC.getElement(1));
            
            selectionPath.lineTo(cursorPosWC.getElement(0), mePosWC.getElement(1));
            
            selectionPath.closePath();
            
            

            Polygon testePolygon = new Polygon();

            PathIterator iterator = selectionPath.getPathIterator(null);

            while (!iterator.isDone()) {

                double[] segmentArray = new double[2];

                int segment = iterator.currentSegment(segmentArray);
                if (segment != PathIterator.SEG_CLOSE) {
                    
                    Coords2D dcSelectionPoint = new Coords2D(0,0);
                    
                    CoordsArray wcSelectionPoint = new CoordsArray(segmentArray);
                    
                    viewingTransform.viewPlaneTransform(wcSelectionPoint,dcSelectionPoint);

                    testePolygon.addPoint((int) dcSelectionPoint.getX(), (int) dcSelectionPoint.getY());

                }

                iterator.next();


            }
            
            tempRectangle=testePolygon;

            int polySize = panel.getCastedUI().getSelectionAreas().size();

            if (polySize > 0) {
                panel.getCastedUI().getSelectionAreas().set(polySize - 1, testePolygon);
            } else {
                panel.getCastedUI().getSelectionAreas().add(testePolygon);
            }


//
//            if (listSize > 0) {
//                panel.getCastedUI().getSelectionAreas().set(listSize - 1, tempRectangle);
//            } else {
//                panel.getCastedUI().getSelectionAreas().add(tempRectangle);
//            }


            panel.repaint();

        }
    }


    public void mousePressed(MouseEvent me) {

        RPnPhaseSpacePanel source = (RPnPhaseSpacePanel) me.getSource();
        if (addRectangle_ == false) {

            cursorPos_ = new Point(me.getX(), me.getY());
            source.repaint();
            addRectangle_ = true;
        } else {
            addRectangle_ = false;
            source.getCastedUI().getSelectionAreas().add(tempRectangle);

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

    public Polygon getSelectedRectangle() {
        return tempRectangle;
    }
}
