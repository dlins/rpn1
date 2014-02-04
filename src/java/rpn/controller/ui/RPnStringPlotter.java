/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rpn.controller.ui;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;
import rpn.component.util.GraphicsUtil;
import rpn.RPnPhaseSpacePanel;
import rpn.component.HugoniotSegGeom;
import rpn.component.RpGeometry;
import rpn.component.util.GeometryGraphND;
import rpnumerics.RPnCurve;
import wave.util.RealVector;
import rpn.parser.RPnDataModule;
import rpnumerics.BifurcationCurve;
import rpnumerics.HugoniotCurve;
import rpnumerics.HugoniotSegment;
import rpnumerics.SegmentedCurve;
import wave.multid.Coords2D;
import wave.multid.CoordsArray;
import wave.multid.Space;
import wave.multid.view.ViewingAttr;
import wave.multid.view.ViewingTransform;
import rpn.component.util.LinePlotted;
import rpnumerics.Orbit;
import rpnumerics.RPNUMERICS;

/**
 *
 * @author moreira
 */
public class RPnStringPlotter extends RPn2DMouseController {

    private Point cursorPos_;
    private String typeStr = "";
    private boolean addLine_ = false;
    public static List<RealVector> correspondentList = new ArrayList();
    private static RPnStringPlotter instance_;
    
    private    RpGeometry geometryTeste_;


    private String permuteString(String string, int flag) {
        String str = string;
        if(flag == Orbit.BACKWARD_DIR) {
            String str1 = str.substring(0, 2);
            String str2 = str.substring(2, 4);
            str = str2.concat(str1);
        }
        return str;
    }


    public void mouseMoved(MouseEvent me) {

        if(addLine_) {

            RPnPhaseSpacePanel panel = (RPnPhaseSpacePanel) me.getSource();
            ViewingTransform viewingTransform = panel.scene().getViewingTransform();
            LinePlotted.panel_ = panel;

            // --- Add 1
            int[] compIndex = panel.scene().getViewingTransform().projectionMap().getCompIndexes();
            int biggestIndex = Math.max(compIndex[0], compIndex[1]);
            int smallestIndex = Math.min(compIndex[0], compIndex[1]);
            // ---
            
            double raio = 7.;
            double Dx = Math.abs(me.getPoint().getX() - cursorPos_.getX());
            double Dy = Math.abs(me.getPoint().getY() - cursorPos_.getY());
            double dist = Math.sqrt(Math.pow(Dy, 2) + Math.pow(Dx, 2));

            double dx = (raio * Dx) / dist;
            double dy = (raio * Dy) / dist;

            Coords2D meDC = new Coords2D(me.getPoint().getX(), me.getPoint().getY());
            CoordsArray meWC = new CoordsArray(new Space(" ", RPNUMERICS.domainDim()));
            panel.scene().getViewingTransform().dcInverseTransform(meDC, meWC);

            // --- Add 2
            CoordsArray tempCoords = new CoordsArray(meWC);
            meWC.setElement(0, tempCoords.getElement(smallestIndex));
            meWC.setElement(1, tempCoords.getElement(biggestIndex));
            // ---
            
            CoordsArray cursorWC = new CoordsArray(new Space(" ", RPNUMERICS.domainDim()));
            Coords2D cursorDC = new Coords2D();

            if (cursorPos_.getX() <= me.getPoint().getX() && cursorPos_.getY() <= me.getPoint().getY()) {
                cursorDC.setElement(0, cursorPos_.getX()+dx);
                cursorDC.setElement(1, cursorPos_.getY()+dy);
            } else if (cursorPos_.getX() >= me.getPoint().getX() && cursorPos_.getY() <= me.getPoint().getY()) {
                cursorDC.setElement(0, cursorPos_.getX()-dx);
                cursorDC.setElement(1, cursorPos_.getY()+dy);
            } else if (cursorPos_.getX() >= me.getPoint().getX() && cursorPos_.getY() >= me.getPoint().getY()) {
                cursorDC.setElement(0, cursorPos_.getX()-dx);
                cursorDC.setElement(1, cursorPos_.getY()-dy);
            } else if (cursorPos_.getX() <= me.getPoint().getX() && cursorPos_.getY() >= me.getPoint().getY()) {
                cursorDC.setElement(0, cursorPos_.getX()+dx);
                cursorDC.setElement(1, cursorPos_.getY()-dy);
            }


            // ---
            int size = panel.getCastedUI().getTypeString().size();
            panel.getCastedUI().getTypeString().set(size - 1, typeStr);
            panel.getCastedUI().getVelocityString().set(size - 1, "");
            // ---

            panel.scene().getViewingTransform().dcInverseTransform(cursorDC, cursorWC);

            // --- Add 3
            CoordsArray tempCoords2 = new CoordsArray(cursorWC);
            cursorWC.setElement(0, tempCoords2.getElement(smallestIndex));
            cursorWC.setElement(1, tempCoords2.getElement(biggestIndex));
            // ---


            List<Object> wcObject = new ArrayList();
            wcObject.add(new Line2D.Double(cursorWC.getElement(0), cursorWC.getElement(1), meWC.getElement(0), meWC.getElement(1)));
            ViewingAttr attr = new ViewingAttr(Color.white);
            wcObject.add(typeStr);
            GraphicsUtil plotted = new LinePlotted(wcObject, viewingTransform, attr);
            
            geometryTeste_.setLastAnnotation(plotted);
//            panel.setLastGraphicsUtil(plotted);
            panel.repaint();


        }

        UIController.instance().globalInputTable().reset();

    }


    public void mousePressed(MouseEvent me) {

        RPnVelocityPlotter.listaEquil.clear();

        RPnPhaseSpacePanel panel = (RPnPhaseSpacePanel) me.getSource();
        LinePlotted.panel_ = panel;

        if(addLine_ == false) {

            //UserInputTable userInputList = UIController.instance().globalInputTable();
            //RealVector newValue = userInputList.values();

            int dim = RPNUMERICS.domainDim();
            Coords2D coordsDC = new Coords2D(me.getX(), me.getY());
            CoordsArray coordsWC = new CoordsArray(new Space("", dim));
            panel.scene().getViewingTransform().dcInverseTransform(coordsDC, coordsWC);

            RealVector newValue = new RealVector(dim);
            for (int i=0; i < dim; i++) {
                newValue.setElement(i, coordsWC.getElement(i));
            }

            RpGeometry geom = RPnDataModule.PHASESPACE.findClosestGeometry(newValue);
            geometryTeste_=geom;
            RPnCurve curve = (RPnCurve) (geom.geomFactory().geomSource());
            RealVector closestPoint = curve.findClosestPoint(newValue);
            GeometryGraphND.pMarca = closestPoint;
            
            if (curve instanceof HugoniotCurve) {
                int dir = ((HugoniotCurve)curve).getDirection();
                HugoniotSegment segment = (HugoniotSegment) (((SegmentedCurve) curve).segments()).get(curve.findClosestSegment(closestPoint));
                typeStr = permuteString(HugoniotSegGeom.s[segment.getType()], dir);

                CoordsArray wcCoords = new CoordsArray(closestPoint);
                Coords2D dcCoords = new Coords2D();
                panel.scene().getViewingTransform().viewPlaneTransform(wcCoords, dcCoords);
                cursorPos_ = new Point(dcCoords.getIntCoords()[0], dcCoords.getIntCoords()[1]);

                panel.getCastedUI().getTypeString().add("");
                panel.getCastedUI().getVelocityString().add("");

                List<Object> wcObjectsList = new ArrayList();
                ViewingAttr viewingAttr = new ViewingAttr(Color.white);
                wcObjectsList.add(new Line2D.Double());
                wcObjectsList.add(typeStr);
                GraphicsUtil empty = new LinePlotted(wcObjectsList, panel.scene().getViewingTransform(), viewingAttr);

                geometryTeste_.addAnnotation(empty);
                
                
//                panel.addGraphicUtil(empty);
                
                addLine_ = true;
            }
            if (curve instanceof BifurcationCurve) {
                int i = curve.findClosestSegment(newValue);
                GeometryGraphND.pMarcaDC = ((BifurcationCurve) curve).secondPointDCOtherVersion(i);
                correspondentList = ((BifurcationCurve)(curve)).correspondentPoints(GeometryGraphND.pMarca);
                
            } else {
                GeometryGraphND.pMarcaDC = GeometryGraphND.pMarca;
            }


        }
        else {
            addLine_ = false;
        }

        UIController.instance().globalInputTable().reset();

    }


    public void mouseDragged(MouseEvent me) {
        RPnVelocityPlotter.listaEquil.clear();
        addLine_ = false;
        //UserInputTable userInputList = UIController.instance().globalInputTable();
        //RealVector newValue = userInputList.values();

        RPnPhaseSpacePanel panel = (RPnPhaseSpacePanel) me.getSource();
        
        int dim = RPNUMERICS.domainDim();
        Coords2D coordsDC = new Coords2D(me.getX(), me.getY());
        CoordsArray coordsWC = new CoordsArray(new Space("", dim));
        panel.scene().getViewingTransform().dcInverseTransform(coordsDC, coordsWC);

        RealVector newValue = new RealVector(dim);
        for (int i = 0; i < dim; i++) {
            newValue.setElement(i, coordsWC.getElement(i));
        }
        
        RpGeometry geom = RPnDataModule.PHASESPACE.findClosestGeometry(newValue);
        RPnCurve curve = (RPnCurve) (geom.geomFactory().geomSource());
        RealVector closestPoint = curve.findClosestPoint(newValue);
        GeometryGraphND.pMarca = closestPoint;
        if (curve instanceof BifurcationCurve) {
            int i = curve.findClosestSegment(newValue);
            GeometryGraphND.pMarcaDC = ((BifurcationCurve) curve).secondPointDCOtherVersion(i);
            correspondentList = ((BifurcationCurve)(curve)).correspondentPoints(GeometryGraphND.pMarca);

        } else {
            GeometryGraphND.pMarcaDC = GeometryGraphND.pMarca;
        }

        UIController.instance().globalInputTable().reset();
    }


    public void mouseClicked(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void mouseReleased(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void mouseEntered(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void mouseExited(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }


    public static RPnStringPlotter instance() {
        if (instance_ == null) {
            instance_ = new RPnStringPlotter();
        }
        return instance_;
    }



}
