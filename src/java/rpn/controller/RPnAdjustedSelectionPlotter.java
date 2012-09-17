/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rpn.controller;

import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import rpn.RPnPhaseSpacePanel;
import rpn.component.HugoniotCurveView;
import rpn.component.RpCalcBasedGeomFactory;
import rpn.component.RpGeometry;
import rpn.parser.RPnDataModule;
import rpnumerics.Configuration;
import rpnumerics.HugoniotCurveCalcND;
import rpnumerics.RPnCurve;
import wave.multid.Coords2D;
import wave.multid.CoordsArray;
import wave.multid.Space;
import wave.multid.graphs.wcWindow;
import wave.multid.view.GeomObjView;
import wave.multid.view.ViewingTransform;
import wave.util.RealSegment;

/**
 *
 * @author edsonlan
 */
public class RPnAdjustedSelectionPlotter implements MouseMotionListener, MouseListener {

    private Point cursorPos_;
    private Polygon selectedPolygon_;
    private boolean addRectangle_ = false;

    public void mouseMoved(MouseEvent me) {

        if (addRectangle_) {
            RPnPhaseSpacePanel panel = (RPnPhaseSpacePanel) me.getSource();
            ViewingTransform viewingTransform = panel.scene().getViewingTransform();

            double[] cursorPosArray = {cursorPos_.x, cursorPos_.y};
            double[] mePosArray = {me.getX(), me.getY()};

            CoordsArray cursorPosWC = new CoordsArray(new Space(" ", 2));
            CoordsArray mePosWC = new CoordsArray(new Space(" ", 2));

            Coords2D cursorPosDC = new Coords2D(cursorPosArray);
            Coords2D mePosDC = new Coords2D(mePosArray);

            viewingTransform.dcInverseTransform(cursorPosDC, cursorPosWC);
            viewingTransform.dcInverseTransform(mePosDC, mePosWC);

            Path2D.Double selectionPath = new Path2D.Double();

            selectionPath.moveTo(cursorPosWC.getElement(0), cursorPosWC.getElement(1));

            selectionPath.lineTo(mePosWC.getElement(0), cursorPosWC.getElement(1));

            selectionPath.lineTo(mePosWC.getElement(0), mePosWC.getElement(1));

            selectionPath.lineTo(cursorPosWC.getElement(0), mePosWC.getElement(1));

            selectionPath.closePath();

            Configuration config = rpnumerics.RPNUMERICS.getConfiguration("hugoniotcurve");


            String resolution = config.getParam("resolution");


            int[] res = RPnDataModule.processResolution(resolution);

            List<GeomObjView> insideGeometries = panel.intersectAreas();


            for (GeomObjView geomObjView : insideGeometries) {

                if (geomObjView instanceof HugoniotCurveView) {


                    RpGeometry rpGeometry = (RpGeometry) geomObjView.getAbstractGeom();

                    RpCalcBasedGeomFactory factory = (RpCalcBasedGeomFactory) rpGeometry.geomFactory();

                    HugoniotCurveCalcND calc = (HugoniotCurveCalcND) factory.rpCalc();
                    res = calc.getParams().getResolution();


                }

            }


            int xResolution = res[0];
            int yResolution = res[1];

            Path2D.Double adjustedPath = adjustPath(selectionPath, viewingTransform.viewPlane().getWindow(), xResolution, yResolution);


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



            if (!panel.getCastedUI().getSelectionAreas().isEmpty()) {
                panel.getCastedUI().getSelectionAreas().set(panel.getCastedUI().getSelectionAreas().size() - 1, selectedPolygon_);
            } else {
                panel.getCastedUI().getSelectionAreas().add(selectedPolygon_);
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
            source.getCastedUI().getSelectionAreas().add(selectedPolygon_);
            Iterator geomIterator = source.scene().geometries();

            RPnCurve curve = null;
            RpCalcBasedGeomFactory factory = null;

            List<RealSegment> segRem = new ArrayList<RealSegment>();

            while (geomIterator.hasNext()) {
                GeomObjView geomObjView = (GeomObjView) geomIterator.next();


                List<Integer> segmentIndex = geomObjView.contains(selectedPolygon_);

                System.out.println(geomObjView + " " + segmentIndex.size());

                RpGeometry rpGeometry = (RpGeometry) geomObjView.getAbstractGeom();

                curve = (RPnCurve) rpGeometry.geomFactory().geomSource();
                factory = (RpCalcBasedGeomFactory) rpGeometry.geomFactory();

                for (Integer i : segmentIndex) {
                    segRem.add(curve.segments().get(i));
                }
            }


            curve.segments().removeAll(segRem);

            factory.updateGeom();

            RPnDataModule.PHASESPACE.update();


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

    public Polygon getSelectedPolygon() {
        return selectedPolygon_;
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
    //TODO Reimplementar usando os paineis 
//     public void drawGrid(Graphics g, Scene scene) {
//
//        Coords2D maxDevCoords = toDeviceCoords(scene,  RPNUMERICS.boundary().getMaximums());
//        Coords2D minDevCoords = toDeviceCoords(scene,  RPNUMERICS.boundary().getMinimums());
//        double deltaX = Math.abs(maxDevCoords.getX() - minDevCoords.getX());
//        double deltaY = Math.abs(maxDevCoords.getY() - minDevCoords.getY());
//        Boundary boundary = RPNUMERICS.boundary();
//
//        if (mapToEqui == 1) {
//            deltaX = RPnPhaseSpacePanel.myW_;
//            deltaY = RPnPhaseSpacePanel.myH_;
//        }
//
//        int index = 0;
//        if (RPNUMERICS.domainDim() == 3) index = 1;
//
//        g.setColor(Color.gray);
//        
//        Graphics2D graph = (Graphics2D) g;
//
//        int[] resolution = {1, 1};
//
//        if (RPnPhaseSpaceAbstraction.listResolution.size()==1) RPnPhaseSpaceAbstraction.closestCurve=0;
//        if (RPnPhaseSpaceAbstraction.listResolution.size()>0) resolution = (int[]) RPnPhaseSpaceAbstraction.listResolution.get(RPnPhaseSpaceAbstraction.closestCurve);
//
//        int xResolution = resolution[0];
//        int yResolution = resolution[1];
//        
//        int nu = (int) xResolution;
//        double dx = deltaX/(1.0*nu);
//
//        int nv = (int) yResolution;
//        double dy = deltaY/(1.0*nv);
//
//        //*** desenha as linhas verticais
//        for (int i = 0; i < nu; i++) {
//            //linex = new Line2D.Double(i * dx, 0, i * dx, RPnPhaseSpacePanel.myH_);
//            linex = new Line2D.Double(i * dx, 0, i * dx, deltaY);
//            if (index == 0  && mapToEqui == 1) linex = mapLine(linex, deltaX, deltaY);
//            graph.draw(linex);
//        }
//        //*******************************
//
//        //*** desenha as linhas horizontais
//        for (int i = 0; i < nv; i++) {
//            //liney = new Line2D.Double(0, i * dy, RPnPhaseSpacePanel.myW_, i * dy);                // preencher com coordenadas do dispositivo
//            liney = new Line2D.Double(0, i * dy, deltaX, i * dy);                                   // preencher com coordenadas do dispositivo
//            if (index == 0  &&  mapToEqui == 1) liney = mapLine(liney, deltaX, deltaY);
//            graph.draw(liney);
//        }
//        //*********************************
//
//        //*** desenha as linhas obliquas        
//        if (boundary instanceof IsoTriang2DBoundary) {
//            for (int i = 0; i < nu; i++) {
//                lineObl = new Line2D.Double(0, RPnPhaseSpacePanel.myH_ - i * dy, i * dx, RPnPhaseSpacePanel.myH_);
//                //lineObl = new Line2D.Double(0, deltaY - i * dy, i * dx, deltaY);
//                if (mapToEqui == 1) lineObl = mapLine(lineObl, deltaX, deltaY);
//                graph.draw(lineObl);
//            }
//        }
//        //*****************************************
//        
//
//    }
//
//    
}
