/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.controller;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;

import rpn.RPnPhaseSpacePanel;
import wave.multid.Coords2D;
import wave.multid.CoordsArray;

public class RPnPhasePanelBoxPlotter extends SelectionPlotter  {

    private Point cursorPos_;
    private Polygon selectedPolygon_;
    private boolean addRectangle_ = false;

    public void mouseMoved(MouseEvent me) {

        if (addRectangle_) {
            RPnPhaseSpacePanel panel = (RPnPhaseSpacePanel) me.getSource();
            
            Path2D.Double selectionPath = plotWCPath(cursorPos_, me, panel);

            selectedPolygon_ = new Polygon();

            PathIterator iterator = selectionPath.getPathIterator(null);

            while (!iterator.isDone()) {

                double[] segmentArray = new double[2];

                int segment = iterator.currentSegment(segmentArray);
                if (segment != PathIterator.SEG_CLOSE) {

                    Coords2D dcSelectionPoint = new Coords2D(0, 0);

                    CoordsArray wcSelectionPoint = new CoordsArray(segmentArray);

                    panel.scene().getViewingTransform().viewPlaneTransform(wcSelectionPoint, dcSelectionPoint);

                    selectedPolygon_.addPoint((int) dcSelectionPoint.getX(), (int) dcSelectionPoint.getY());

                }

                iterator.next();


            }

            int size = panel.getCastedUI().getSelectionAreas().size();

            panel.getCastedUI().getSelectionAreas().set(size - 1, selectedPolygon_);

            panel.repaint();

        }
    }

    public void mousePressed(MouseEvent me) {

        RPnPhaseSpacePanel panel = (RPnPhaseSpacePanel) me.getSource();
        if (addRectangle_ == false) {
            cursorPos_ = new Point(me.getX(), me.getY());
            Polygon emptyPolygon = new Polygon();
            emptyPolygon.addPoint(cursorPos_.x, cursorPos_.y);
            panel.getCastedUI().getSelectionAreas().add(emptyPolygon);

            addRectangle_ = true;
        } else {
            addRectangle_ = false;
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
}
