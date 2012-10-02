/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rpn.controller.ui;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.util.Iterator;
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
import wave.multid.view.ViewingTransform;

/**
 *
 * @author moreira
 */
public class RPnStringPlotter extends RPn2DMouseController {

    private Point cursorPos_;
    private Line2D.Double line_;
    private String typeStr = "";
    private boolean addLine_ = false;
    static private RPnStringPlotter instance_ = null;


    public void mouseMoved(MouseEvent me) {

        RPnPhaseSpacePanel panel = (RPnPhaseSpacePanel) me.getSource();
        
        if(addLine_) {
            double raio = 7.;
            double Dx = Math.abs(me.getPoint().getX() - cursorPos_.getX());
            double Dy = Math.abs(me.getPoint().getY() - cursorPos_.getY());
            double dist = Math.sqrt(Math.pow(Dy, 2) + Math.pow(Dx, 2));

            double dx = (raio * Dx) / dist;
            double dy = (raio * Dy) / dist;

            Line2D.Double line = new Line2D.Double();

            if (cursorPos_.getX() < me.getPoint().getX() && cursorPos_.getY() < me.getPoint().getY()) {
                line = new Line2D.Double(cursorPos_.getX()+dx, cursorPos_.getY()+dy, me.getPoint().getX(), me.getPoint().getY());
                line_ = line;
            } else if (cursorPos_.getX() > me.getPoint().getX() && cursorPos_.getY() < me.getPoint().getY()) {
                line = new Line2D.Double(cursorPos_.getX()-dx, cursorPos_.getY()+dy, me.getPoint().getX(), me.getPoint().getY());
                line_ = line;
            } else if (cursorPos_.getX() > me.getPoint().getX() && cursorPos_.getY() > me.getPoint().getY()) {
                line = new Line2D.Double(cursorPos_.getX()-dx, cursorPos_.getY()-dy, me.getPoint().getX(), me.getPoint().getY());
                line_ = line;
            } else if (cursorPos_.getX() < me.getPoint().getX() && cursorPos_.getY() > me.getPoint().getY()) {
                line = new Line2D.Double(cursorPos_.getX()+dx, cursorPos_.getY()-dy, me.getPoint().getX(), me.getPoint().getY());
                line_ = line;
            }

            int size = panel.getCastedUI().getStringArrows().size();
            panel.getCastedUI().getStringArrows().set(size - 1, line_);
            panel.getCastedUI().getTypeString().set(size - 1, typeStr);

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

                ViewingTransform transf = panel.scene().getViewingTransform();
                CoordsArray wcCoords = new CoordsArray(closestPoint);
                Coords2D dcCoords = new Coords2D();
                transf.viewPlaneTransform(wcCoords, dcCoords);
                cursorPos_ = new Point(dcCoords.getIntCoords()[0], dcCoords.getIntCoords()[1]);

                panel.getCastedUI().getStringArrows().add(new Line2D.Double());
                panel.getCastedUI().getTypeString().add("");

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
    
    
    public static RPnStringPlotter instance() {
        if (instance_ == null) {
            instance_ = new RPnStringPlotter();
        }
        return instance_;
    }

    

    public void clearLastString() {
        GeometryGraphND.clearpMarca();
        System.out.println("Entrei no novo clearLastString() .............. ");

        Iterator<RPnPhaseSpacePanel> iterator = UIController.instance().getInstalledPanelsIterator();
        while (iterator.hasNext()) {
            RPnPhaseSpacePanel panel = iterator.next();

            int size = panel.getCastedUI().getTypeString().size();
            if (size > 0) {
                panel.getCastedUI().getTypeString().remove(size - 1);
                panel.getCastedUI().getStringArrows().remove(size - 1);
            }
            
        }

    }


    public void clearClassifiers() {
        GeometryGraphND.clearpMarca();
        System.out.println("Entrei no novo clearClassifiers() .............. ");

        Iterator<RPnPhaseSpacePanel> iterator = UIController.instance().getInstalledPanelsIterator();
        while (iterator.hasNext()) {
            RPnPhaseSpacePanel panel = iterator.next();
            int size = panel.getCastedUI().getTypeString().size();
            if (size > 0) {
                panel.getCastedUI().getTypeString().clear();
                panel.getCastedUI().getStringArrows().clear();
            }
            
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


}
