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
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;

import rpn.RPnPhaseSpacePanel;
import wave.multid.Coords2D;
import wave.multid.CoordsArray;
import wave.multid.Space;
import wave.multid.graphs.Iso2EquiTransform;
import wave.multid.map.Map;
import wave.multid.view.Scene;
import wave.multid.view.ViewingTransform;
import wave.util.RealMatrix2;

/**
 *
 * @author edsonlan
 */
public class RPnPhasePanelBoxPlotter implements MouseMotionListener, MouseListener {

    private Point cursorPos_;
    private Rectangle2D.Double tempRectangle;
    private boolean addRectangle_ = false;

    @Override
    public void mouseMoved(MouseEvent me) {

        if (addRectangle_) {
            RPnPhaseSpacePanel panel = (RPnPhaseSpacePanel) me.getSource();
            ViewingTransform viewingTransform = panel.scene().getViewingTransform();

            double x = cursorPos_.getX();
            double y = cursorPos_.getY();

            //P1

            double newx = me.getX();
            double newy = me.getY();

            double w = Math.abs(newx - x);
            double h = Math.abs(newy - y);


            x = Math.min(x, newx);
            y = Math.min(y, newy);

            //P2

            int listSize = panel.getCastedUI().getSelectionAreas().size();


            Map testeMap = viewingTransform.projectionMap();

            RealMatrix2 matrixTeste = testeMap.getTransfMatrix();
            double XScaling = matrixTeste.getElement(0, 0);
            double YScaling = matrixTeste.getElement(1, 1);

            double XTranslate = matrixTeste.getElement(2, 0);
            double YTranslate = matrixTeste.getElement(2, 1);

            double XShear = 0;
            double YShear = 0;

            tempRectangle = new Rectangle2D.Double(x, y, w, h);



            ///////////////////////////////////////////////




            if (viewingTransform instanceof Iso2EquiTransform) {

                XShear = 0.5;
                YShear = 0.0;

                XScaling = 1.0;
                YScaling = Math.sqrt(3) / 2.;
              


            }

            double[] affineMatrix = {XScaling, YShear, XShear, YScaling, XTranslate, YTranslate};

            Polygon testePolygon = new Polygon();


            PathIterator iterator = tempRectangle.getPathIterator(new AffineTransform(affineMatrix));


            while (!iterator.isDone()) {

                double[] segmentArray = new double[2];

                int segment = iterator.currentSegment(segmentArray);
                if (segment != PathIterator.SEG_CLOSE) {
                    testePolygon.addPoint((int) segmentArray[0], (int) segmentArray[1]);
                }

                iterator.next();


            }


            //////////////////////////////////////////////////////





            int polySize = panel.getCastedUI().testeSelectedAreas().size();

            if (polySize > 0) {
                panel.getCastedUI().testeSelectedAreas().set(polySize - 1, testePolygon);
            } else {
                panel.getCastedUI().testeSelectedAreas().add(testePolygon);
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

//    public void mouseMoved(MouseEvent me) {
//
//        if (addRectangle_) {
//            RPnPhaseSpacePanel panel = (RPnPhaseSpacePanel) me.getSource();
//            ViewingTransform viewingTransform = panel.scene().getViewingTransform();
//
//            double x = cursorPos_.getX();
//            double y = cursorPos_.getY();
//
//            //P1
//
//            double newx = me.getX();
//            double newy = me.getY();
//
//            double w = Math.abs(newx - x);
//            double h = Math.abs(newy - y);
//
//
//            x = Math.min(x, newx);
//            y = Math.min(y, newy);
//
//            //P2
//
//
//
//
//            int listSize = panel.getCastedUI().getSelectionAreas().size();
//
//
//            Map testeMap = viewingTransform.projectionMap();
//
//
//
//
//            RealMatrix2 matrixTeste = testeMap.getTransfMatrix();
//
//
//
//            double XScaling = matrixTeste.getElement(0, 0);
//            double YScaling = matrixTeste.getElement(1, 1);
//            
//            
//            System.out.println("Scale X: "+XScaling);
//            System.out.println("Scale Y: "+YScaling);
//
//
//            double XTranslate = matrixTeste.getElement(2, 0);
//            double YTranslate = matrixTeste.getElement(2, 1);
//
//
//            double XShear = 0;
//            double YShear = 0;
//
//            tempRectangle = new Rectangle2D.Double(x, y, w, h);
//
//
//            Area transfArea = new Area(tempRectangle);
//
//
//            ///////////////////////////////////////////////
//
//
//
//
//            if (viewingTransform instanceof Iso2EquiTransform) {
//
//                XShear = 0.5;
//                YShear = 0.0;
//                
//                XScaling=1.0;
//                YScaling=Math.sqrt(3) / 2.;
//
//
//            }
//
//            double[] affineMatrix = {XScaling, YShear, XShear, YScaling, XTranslate, YTranslate};
//            transfArea = transfArea.createTransformedArea(new AffineTransform(affineMatrix));
//            Polygon testePolygon = new Polygon();
//            
//
//            PathIterator iterator = transfArea.getPathIterator(null);
//
//            
//            testePolygon.addPoint(me.getX(),me.getY());
//            
//
//
//            while (!iterator.isDone()) {
//
//                double[] segmentArray = new double[2];
//
//                int segment = iterator.currentSegment(segmentArray);
//
//                for (int i = 0; i < segmentArray.length; i++) {
//                    double d = segmentArray[i];
//
//                    System.out.println(d);
//
//                    testePolygon.addPoint((int) segmentArray[0], (int) segmentArray[1]);
//
//                }
//
//
//                iterator.next();
//
//
//            }
//
//
//            //////////////////////////////////////////////////////
//
//
//            panel.getGraphics().drawPolygon(testePolygon);
//
//            System.out.println("--------------------------");
//
//
//
//            int polySize = panel.getCastedUI().testeSelectedAreas().size();
//
//            if (polySize > 0) {
//                panel.getCastedUI().testeSelectedAreas().set(polySize - 1, testePolygon);
//            } else {
//                panel.getCastedUI().testeSelectedAreas().add(testePolygon);
//            }
//
//
////
////            if (listSize > 0) {
////                panel.getCastedUI().getSelectionAreas().set(listSize - 1, tempRectangle);
////            } else {
////                panel.getCastedUI().getSelectionAreas().add(tempRectangle);
////            }
//
//
//            panel.repaint();
//
//        }
//    }
    private Coords2D testeTransformVertices(Scene scene, double[] oldCoords) {

        ViewingTransform viewingTransform = scene.getViewingTransform();

        Coords2D inDC = new Coords2D(oldCoords);
//
        CoordsArray outWC = new CoordsArray(new Space("", 2));
//
        viewingTransform.dcInverseTransform(inDC, outWC);



//
        Coords2D finalDC = new Coords2D();
//
        viewingTransform.viewPlaneTransform(outWC, finalDC);

        return finalDC;




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

    public Rectangle2D.Double getSelectedRectangle() {
        return tempRectangle;
    }
}
