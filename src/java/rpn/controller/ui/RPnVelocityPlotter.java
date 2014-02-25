/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.controller.ui;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import rpn.RPnPhaseSpacePanel;
import rpn.component.RpGeometry;
import rpn.parser.RPnDataModule;
import rpnumerics.RPnCurve;
import rpnumerics.RPNUMERICS;
import wave.multid.Coords2D;
import wave.multid.CoordsArray;
import wave.multid.Space;
import wave.util.RealVector;


public class RPnVelocityPlotter extends RPn2DMouseController {
    
    private RealVector cursorPos_;

    private boolean addLine_ = false;
    public static List<RealVector> listaEquil = new ArrayList();
    private static RPnVelocityPlotter instance_;
    private RpGeometry geometry_;

    

    public void mouseMoved(MouseEvent me) {

        if(addLine_) {

            RPnPhaseSpacePanel panel = (RPnPhaseSpacePanel) me.getSource();

            int dim = RPNUMERICS.domainDim();
            Coords2D coordsDC = new Coords2D(me.getX(), me.getY());
            CoordsArray coordsWC = new CoordsArray(new Space("", dim));
            panel.scene().getViewingTransform().dcInverseTransform(coordsDC, coordsWC);
            geometry_.removeLastAnnotation();
            geometry_.showSpeed(new CoordsArray(cursorPos_), coordsWC, panel.scene().getViewingTransform());

            panel.repaint();

        }

        UIController.instance().globalInputTable().reset();
    }


    public void mousePressed(MouseEvent me) {
        listaEquil.clear();

        RPnPhaseSpacePanel panel = (RPnPhaseSpacePanel) me.getSource();

        if(addLine_ == false) {


            int dim = RPNUMERICS.domainDim();
            Coords2D coordsDC = new Coords2D(me.getX(), me.getY());
            CoordsArray coordsWC = new CoordsArray(new Space("", dim));
            panel.scene().getViewingTransform().dcInverseTransform(coordsDC, coordsWC);

            RealVector newValue = new RealVector(dim);
            for (int i=0; i < dim; i++) {
                newValue.setElement(i, coordsWC.getElement(i));
            }

            geometry_=RPnDataModule.PHASESPACE.findClosestGeometry(newValue);
            RPnCurve curve = (RPnCurve) (geometry_.geomFactory().geomSource());
            RealVector closestPoint = curve.findClosestPoint(newValue);

            cursorPos_=closestPoint;
            geometry_.showSpeed(new CoordsArray(cursorPos_), new CoordsArray(cursorPos_), panel.scene().getViewingTransform());
            
            addLine_ = true;

            
        }
        else {
            addLine_ = false;
        }

        UIController.instance().globalInputTable().reset();
    }


    public void mouseDragged(MouseEvent me) {
//        addLine_ = false;
//        //UserInputTable userInputList = UIController.instance().globalInputTable();
//        //RealVector newValue = userInputList.values();
//
//        RPnPhaseSpacePanel panel = (RPnPhaseSpacePanel) me.getSource();
//
//        int dim = RPNUMERICS.domainDim();
//        Coords2D coordsDC = new Coords2D(me.getX(), me.getY());
//        CoordsArray coordsWC = new CoordsArray(new Space("", dim));
//        panel.scene().getViewingTransform().dcInverseTransform(coordsDC, coordsWC);
//
//        RealVector newValue = new RealVector(dim);
//        for (int i = 0; i < dim; i++) {
//            newValue.setElement(i, coordsWC.getElement(i));
//        }
//
//        RpGeometry geom = RPnDataModule.PHASESPACE.findClosestGeometry(newValue);
//        RPnCurve curve = (RPnCurve) (geom.geomFactory().geomSource());
//        RealVector closestPoint = curve.findClosestPoint(newValue);
//        GeometryGraphND.pMarca = closestPoint;
//
//        if (curve instanceof HugoniotCurve)
//            listaEquil = ((HugoniotCurve)(curve)).equilPoints(GeometryGraphND.pMarca);
//
//        UIController.instance().globalInputTable().reset();
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


    public static RPnVelocityPlotter instance() {
        if (instance_ == null) {
            instance_ = new RPnVelocityPlotter();
        }
        return instance_;
    }



}
