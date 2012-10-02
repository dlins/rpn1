/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rpn.controller.ui;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import rpn.RPnPhaseSpacePanel;
import rpn.component.RpGeometry;
import rpn.component.util.GeometryGraphND;
import rpn.parser.RPnDataModule;
import rpnumerics.HugoniotCurve;
import rpnumerics.Orbit;
import rpnumerics.OrbitPoint;
import rpnumerics.RPnCurve;
import rpnumerics.WaveCurve;
import rpnumerics.WaveCurveOrbit;
import wave.multid.Coords2D;
import wave.multid.CoordsArray;
import wave.multid.view.ViewingTransform;
import wave.util.RealVector;

/**
 *
 * @author moreira
 */
public class RPnVelocityPlotter extends RPn2DMouseController {
    
    private Point cursorPos_;
    private Line2D.Double line_;
    private String velStr = "";
    private boolean addLine_ = false;
    public static List<RealVector> listaEquil = new ArrayList();
    static private RPnVelocityPlotter instance_ = null;


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

            int size = panel.getCastedUI().getVelocityArrows().size();
            panel.getCastedUI().getVelocityArrows().set(size - 1, line_);
            panel.getCastedUI().getVelocityString().set(size - 1, velStr);

        }
    }



    public void mousePressed(MouseEvent me) {
        listaEquil.clear();

        RPnPhaseSpacePanel panel = (RPnPhaseSpacePanel) me.getSource();

        if(addLine_ == false) {

            UserInputTable userInputList = UIController.instance().globalInputTable();
            RealVector newValue = userInputList.values();
            RpGeometry geom = RPnDataModule.PHASESPACE.findClosestGeometry(newValue);
            RPnCurve curve = (RPnCurve) (geom.geomFactory().geomSource());
            RealVector closestPoint = curve.findClosestPoint(newValue);
            GeometryGraphND.pMarca = closestPoint;

            if (curve instanceof Orbit) {
                OrbitPoint point = (OrbitPoint) ((Orbit) curve).getPoints()[curve.findClosestSegment(closestPoint)];
                velStr = String.format("%.4e", point.getLambda());
            }
            else if (curve instanceof HugoniotCurve) {
                velStr = String.format("%.4e", ((HugoniotCurve)curve).velocity(closestPoint));
            }
            else if (curve instanceof WaveCurve) {
                int tam = ((WaveCurve) curve).getBranchsList().size();
                ArrayList<OrbitPoint> result = new ArrayList<OrbitPoint>();

                for (int i = 0; i < tam; i++) {
                    WaveCurveOrbit orbit = (WaveCurveOrbit) ((WaveCurve) curve).getBranchsList().get(i);
                    OrbitPoint[] parcial = orbit.getPoints();
                    for (int j = 0; j < parcial.length; j++) {
                        result.add(parcial[j]);
                    }
                }

                int seg = curve.findClosestSegment(closestPoint);
                velStr = String.format("%.4e", result.get(seg).getLambda());

            } else {
                velStr = String.valueOf(0.0);
            }


            ViewingTransform transf = panel.scene().getViewingTransform();
            CoordsArray wcCoords = new CoordsArray(closestPoint);
            Coords2D dcCoords = new Coords2D();
            transf.viewPlaneTransform(wcCoords, dcCoords);
            cursorPos_ = new Point(dcCoords.getIntCoords()[0], dcCoords.getIntCoords()[1]);

            panel.getCastedUI().getVelocityArrows().add(new Line2D.Double());
            panel.getCastedUI().getVelocityString().add("");

            addLine_ = true;

        }
        else {
            addLine_ = false;
        }
    }


    public void mouseDragged(MouseEvent e) {
        addLine_ = false;
        UserInputTable userInputList = UIController.instance().globalInputTable();
        RealVector newValue = userInputList.values();
        RpGeometry geom = RPnDataModule.PHASESPACE.findClosestGeometry(newValue);
        RPnCurve curve = (RPnCurve) (geom.geomFactory().geomSource());
        RealVector closestPoint = curve.findClosestPoint(newValue);
        GeometryGraphND.pMarca = closestPoint;

        if (curve instanceof HugoniotCurve)
            listaEquil = ((HugoniotCurve)(curve)).equilPoints(GeometryGraphND.pMarca);
    }


    public static RPnVelocityPlotter instance() {
        if (instance_ == null) {
            instance_ = new RPnVelocityPlotter();
        }
        return instance_;
    }


    public void clearLastString() {
        GeometryGraphND.clearpMarca();
        System.out.println("Entrei no novo clearLastString() .............. ");

        Iterator<RPnPhaseSpacePanel> iterator = UIController.instance().getInstalledPanelsIterator();
        while (iterator.hasNext()) {
            RPnPhaseSpacePanel panel = iterator.next();

            int size = panel.getCastedUI().getVelocityString().size();
            if (size > 0) {
                panel.getCastedUI().getVelocityArrows().remove(size - 1);
                panel.getCastedUI().getVelocityString().remove(size - 1);
            }
            
        }

    }


    public void clearVelocities() {
        GeometryGraphND.clearpMarca();
        System.out.println("Entrei no novo clearVelocities() .............. ");

        Iterator<RPnPhaseSpacePanel> iterator = UIController.instance().getInstalledPanelsIterator();
        while (iterator.hasNext()) {
            RPnPhaseSpacePanel panel = iterator.next();
            int size = panel.getCastedUI().getVelocityString().size();
            if (size > 0) {
                panel.getCastedUI().getVelocityString().clear();
                panel.getCastedUI().getVelocityArrows().clear();
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
