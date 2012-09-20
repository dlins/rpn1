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
import java.awt.geom.Point2D;

import java.util.ArrayList;
import rpn.RPnPhaseSpacePanel;
import wave.multid.Coords2D;
import wave.multid.CoordsArray;
import wave.multid.graphs.wcWindow;
import wave.multid.view.ViewingTransform;


public class RPnAdjustedSelectionPlotter extends SelectionPlotter  {

    private Point cursorPos_;
    private Polygon selectedPolygon_;
    private boolean addRectangle_ = false;
    private int xResolution_;
    private int yResolution_;

    public RPnAdjustedSelectionPlotter(int xResolution, int yResolution) {
        xResolution_ = xResolution;
        yResolution_ = yResolution;
    }

    public void mouseMoved(MouseEvent me) {

        if (addRectangle_) {
            RPnPhaseSpacePanel panel = (RPnPhaseSpacePanel) me.getSource();
            ViewingTransform viewingTransform = panel.scene().getViewingTransform();

            Path2D.Double selectionPath = plotWCPath(cursorPos_, me, panel);

            Path2D.Double adjustedPath = adjustPath(selectionPath, viewingTransform.viewPlane().getWindow(), xResolution_, yResolution_);

            selectedPolygon_ = new Polygon();

            PathIterator iterator = adjustedPath.getPathIterator(null);

            while (!iterator.isDone()) {

                double[] segmentArray = new double[2];

                int segment = iterator.currentSegment(segmentArray);
                if (segment != PathIterator.SEG_CLOSE) {

                    Coords2D dcSelectionPoint = new Coords2D(0, 0);

                    CoordsArray wcSelectionPoint = new CoordsArray(segmentArray);

                    viewingTransform.viewPlaneTransform(wcSelectionPoint, dcSelectionPoint);

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

    private Path2D.Double adjustPath(Path2D.Double input, wcWindow wc, int xResolution, int yResolution) {

        Path2D.Double output = new Path2D.Double();

        Point2D.Double lowerLeftCorner = wc.getOriginPosition();

        double vMin = lowerLeftCorner.x;
        double uMin = lowerLeftCorner.y;

        double dv = wc.getWidth() / xResolution;
        double du = wc.getHeight() / yResolution;

        PathIterator inputIterator = input.getPathIterator(null);

        ArrayList<double[]> selectionVertex = new ArrayList<double[]>();

        while (!inputIterator.isDone()) {

            double[] segmentArray = new double[2];

            int segment = inputIterator.currentSegment(segmentArray);


            if (segment != PathIterator.SEG_CLOSE) {
                selectionVertex.add(segmentArray);

            }
            inputIterator.next();
        }


        double[] vertex = selectionVertex.get(0);

        double topRightXAdjusted = vMin + (int) ((vertex[0] - vMin) / dv) * dv;
        double topRigthYAdjusted = uMin + (int) ((vertex[1] - uMin) / du + 1) * du;

        vertex = selectionVertex.get(2);


        double downLeftXAdjusted = vMin + (int) ((vertex[0] - vMin) / dv + 1) * dv;
        double downLeftYAdjusted = uMin + (int) ((vertex[1] - uMin) / du) * du;


        output.moveTo(topRightXAdjusted, topRigthYAdjusted);


        output.lineTo(downLeftXAdjusted, topRigthYAdjusted);
        output.lineTo(downLeftXAdjusted, downLeftYAdjusted);

        output.lineTo(topRightXAdjusted, downLeftYAdjusted);

        output.closePath();

        return output;

    }
  
}
