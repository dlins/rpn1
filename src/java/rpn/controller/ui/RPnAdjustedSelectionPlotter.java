/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */



package rpn.controller.ui;

import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import rpn.RPnPhaseSpacePanel;
import rpn.component.RpGeometry;
import rpn.component.util.AreaSelected;
import rpn.component.util.GeometryGraph;
import rpn.component.util.GeometryGraphND;
import rpn.component.util.GraphicsUtil;
import rpn.parser.RPnDataModule;
import rpnumerics.BifurcationCurve;
import rpnumerics.RPnCurve;
import wave.multid.Coords2D;
import wave.multid.CoordsArray;
import wave.multid.Space;
import wave.multid.graphs.wcWindow;
import wave.multid.view.GeomObjView;
import wave.multid.view.ViewingAttr;
import wave.multid.view.ViewingTransform;
import wave.util.RealVector;


public class RPnAdjustedSelectionPlotter extends RPn2DMouseController  {

    private Point cursorPos_;
    private boolean addRectangle_ = false;
    private int xResolution_;
    private int yResolution_;

    public RPnAdjustedSelectionPlotter(int xResolution, int yResolution) {
        xResolution_ = xResolution;
        yResolution_ = yResolution;
    }

    public void mouseMoved(MouseEvent me) {

        if (addRectangle_) {

            GeometryGraph.count++;

            RPnPhaseSpacePanel panel = (RPnPhaseSpacePanel) me.getSource();
            ViewingTransform viewingTransform = panel.scene().getViewingTransform();

            Path2D.Double selectionPath = plotWCArea(cursorPos_, me, panel);
            Path2D.Double adjustedPath = adjustPath(selectionPath, viewingTransform.viewPlane().getWindow(), xResolution_, yResolution_);
            List<Object> wcObject = new ArrayList();
            wcObject.add(adjustedPath);
            ViewingAttr viewingAttr =new ViewingAttr(Color.red);
            GraphicsUtil selectedArea= new AreaSelected(wcObject, viewingTransform, viewingAttr);
            panel.setLastGraphicsUtil(selectedArea);
            panel.repaint();
            
            indexToRemove(panel);
            secondArea(panel, viewingTransform);
            
        }

    }

    public void mousePressed(MouseEvent me) {

        RPnPhaseSpacePanel panel = (RPnPhaseSpacePanel) me.getSource();
        if (addRectangle_ == false) {

            GeometryGraph.count = 0;

            cursorPos_ = new Point(me.getX(), me.getY());

            double[] mePosArray = {me.getX(), me.getY()};
            CoordsArray cursorPosWC = new CoordsArray(new Space(" ", 2));
            Coords2D mePosDC = new Coords2D(mePosArray);
            CoordsArray mePosWC = new CoordsArray(new Space(" ",3));
            panel.scene().getViewingTransform().dcInverseTransform(mePosDC, mePosWC);
            Path2D.Double selectionPath = new Path2D.Double();
            selectionPath.moveTo(cursorPosWC.getElement(0), cursorPosWC.getElement(1));
            List<Object> wcObjectsList = new ArrayList();
            wcObjectsList.add(selectionPath);
            ViewingAttr viewingAttr = new ViewingAttr(Color.red);
            GraphicsUtil emptyGraphicsUtil = new AreaSelected(wcObjectsList, panel.scene().getViewingTransform(), viewingAttr);

            panel.addGraphicUtil(emptyGraphicsUtil);

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


    private void secondArea(RPnPhaseSpacePanel panel, ViewingTransform viewingTransform) {

        String panelName = panel.getName();
        String secondPanelName = "";
        if(panelName.equals("RightPhase Space"))
            secondPanelName = "LeftPhase Space";
        if(panelName.equals("LeftPhase Space"))
            secondPanelName = "RightPhase Space";

        UserInputTable userInputList = UIController.instance().globalInputTable();
        RealVector newValue = userInputList.values();
        RpGeometry geom = RPnDataModule.PHASESPACE.findClosestGeometry(newValue);
        RPnCurve curve = (RPnCurve) (geom.geomFactory().geomSource());
        if (curve instanceof BifurcationCurve) {
            GeometryGraphND geomND = new GeometryGraphND();
            Polygon pol = geomND.secondArea((BifurcationCurve) curve, panel.scene());
            Point point1 = new Point((int) pol.getBounds2D().getMinX(), (int) pol.getBounds2D().getMinY());
            Point point2 = new Point((int) pol.getBounds2D().getMaxX(), (int) pol.getBounds2D().getMaxY());

            Path2D.Double selectionPath = plotWCArea(point1, point2, panel);
            Path2D.Double adjustedPath = adjustPath(selectionPath, viewingTransform.viewPlane().getWindow(), xResolution_, yResolution_);
            List<Object> wcObject = new ArrayList();
            wcObject.add(adjustedPath);
            ViewingAttr viewingAttr = new ViewingAttr(Color.red);
            GraphicsUtil selectedArea = new AreaSelected(wcObject, viewingTransform, viewingAttr);

            Iterator<RPnPhaseSpacePanel> phaseSpacePanelIterator = UIController.instance().getInstalledPanelsIterator();
            while (phaseSpacePanelIterator.hasNext()) {
                RPnPhaseSpacePanel secondPanel = phaseSpacePanelIterator.next();
                if (secondPanel.getName().equals(secondPanelName)) {
                    secondPanel.setLastGraphicsUtil(selectedArea);
                    secondPanel.repaint();
                }
            }

        }
    }


    private void indexToRemove(RPnPhaseSpacePanel panel) {
        List<Integer> indexToRemove = new ArrayList<Integer>();
        List<AreaSelected> selectedAreas = panel.getSelectedAreas();

        for (AreaSelected area : selectedAreas) {
            Iterator geomIterator = panel.scene().geometries();
            while (geomIterator.hasNext()) {
                GeomObjView geomObjView = (GeomObjView) geomIterator.next();
                List<Integer> segmentIndex = geomObjView.contains((Polygon)area.getShape());
                if (!segmentIndex.isEmpty()) {
                    indexToRemove.addAll(segmentIndex);
                }
            }
        }
        GeometryGraphND.indContido = indexToRemove;
    }


    private Path2D.Double adjustPath(Path2D.Double input, wcWindow wc, int xResolution, int yResolution) {

        Path2D.Double output = new Path2D.Double();

        Point2D.Double lowerLeftCorner = wc.getOriginPosition();

//        double vMin = lowerLeftCorner.x;
//        double uMin = lowerLeftCorner.y;
//
//        double dv = wc.getWidth() / xResolution;
//        double du = wc.getHeight() / yResolution;

        // --- Teste rápido para a TPCW, aguardar confirmacao do Edson
        double vMin = lowerLeftCorner.y;
        double uMin = lowerLeftCorner.x;

        double dv = wc.getHeight() / yResolution;
        double du = wc.getWidth() / xResolution;
        // ---

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
