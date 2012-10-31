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

/**
 *
 * @author moreira
 */
public class RPnStringPlotter extends RPn2DMouseController {

    private Point cursorPos_;
    private String typeStr = "";
    private boolean addLine_ = false;
    private static RPnStringPlotter instance_;

    public void mouseMoved(MouseEvent me) {

        if(addLine_) {

            RPnPhaseSpacePanel panel = (RPnPhaseSpacePanel) me.getSource();
            ViewingTransform viewingTransform = panel.scene().getViewingTransform();
            
            double raio = 7.;
            double Dx = Math.abs(me.getPoint().getX() - cursorPos_.getX());
            double Dy = Math.abs(me.getPoint().getY() - cursorPos_.getY());
            double dist = Math.sqrt(Math.pow(Dy, 2) + Math.pow(Dx, 2));

            double dx = (raio * Dx) / dist;
            double dy = (raio * Dy) / dist;

            Coords2D meDC = new Coords2D(me.getPoint().getX(), me.getPoint().getY());
            CoordsArray meWC = new CoordsArray(new Space(" ", 2));
            panel.scene().getViewingTransform().dcInverseTransform(meDC, meWC);
            
            CoordsArray cursorWC = new CoordsArray(new Space(" ", 2));
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
            List<Object> wcObject = new ArrayList();
            wcObject.add(new Line2D.Double(cursorWC.getElement(0), cursorWC.getElement(1), meWC.getElement(0), meWC.getElement(1)));
            ViewingAttr attr = new ViewingAttr(Color.white);
            
            GraphicsUtil plotted = new LinePlotted(wcObject, viewingTransform, attr);
            panel.setLastGraphicsUtil(plotted);
            panel.repaint();


        }
        

    }


    public void mousePressed(MouseEvent me) {

        RPnVelocityPlotter.listaEquil.clear();

        RPnPhaseSpacePanel panel = (RPnPhaseSpacePanel) me.getSource();

        if(addLine_ == false) {

            UserInputTable userInputList = UIController.instance().globalInputTable();
            RealVector newValue = userInputList.values();
            RpGeometry geom = RPnDataModule.PHASESPACE.findClosestGeometry(newValue);
            RPnCurve curve = (RPnCurve) (geom.geomFactory().geomSource());
            RealVector closestPoint = curve.findClosestPoint(newValue);
            GeometryGraphND.pMarca = closestPoint;
            
            if (curve instanceof HugoniotCurve) {
                HugoniotSegment segment = (HugoniotSegment) (((SegmentedCurve) curve).segments()).get(curve.findClosestSegment(closestPoint));
                typeStr = HugoniotSegGeom.s[segment.getType()];
                System.out.println("String encontrada : " +typeStr);

                ViewingTransform transf = panel.scene().getViewingTransform();
                CoordsArray wcCoords = new CoordsArray(closestPoint);
                Coords2D dcCoords = new Coords2D();
                transf.viewPlaneTransform(wcCoords, dcCoords);
                cursorPos_ = new Point(dcCoords.getIntCoords()[0], dcCoords.getIntCoords()[1]);

                panel.getCastedUI().getTypeString().add("");
                panel.getCastedUI().getVelocityString().add("");

                List<Object> wcObjectsList = new ArrayList();
                ViewingAttr viewingAttr = new ViewingAttr(Color.white);
                wcObjectsList.add(new Line2D.Double());
                GraphicsUtil empty = new LinePlotted(wcObjectsList, panel.scene().getViewingTransform(), viewingAttr);

                panel.addGraphicUtil(empty);
                
                addLine_ = true;
            }
            if (curve instanceof BifurcationCurve) {
                int i = curve.findClosestSegment(newValue);
                GeometryGraphND.pMarcaDC = ((BifurcationCurve) curve).secondPointDC(i);
            } else {
                GeometryGraphND.pMarcaDC = GeometryGraphND.pMarca;
            }


        }
        else {
            addLine_ = false;
        }
            
    }


    public void mouseDragged(MouseEvent e) {
        RPnVelocityPlotter.listaEquil.clear();
        addLine_ = false;
        UserInputTable userInputList = UIController.instance().globalInputTable();
        RealVector newValue = userInputList.values();
        RpGeometry geom = RPnDataModule.PHASESPACE.findClosestGeometry(newValue);
        RPnCurve curve = (RPnCurve) (geom.geomFactory().geomSource());
        RealVector closestPoint = curve.findClosestPoint(newValue);
        GeometryGraphND.pMarca = closestPoint;
        if (curve instanceof BifurcationCurve) {
            int i = curve.findClosestSegment(newValue);
            GeometryGraphND.pMarcaDC = ((BifurcationCurve) curve).secondPointDC(i);
        } else {
            GeometryGraphND.pMarcaDC = GeometryGraphND.pMarca;
        }
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
