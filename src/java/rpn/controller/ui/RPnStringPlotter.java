/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rpn.controller.ui;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import rpn.RPnPhaseSpacePanel;
import rpn.command.GenericExtensionCurveCommand;
import rpn.command.RpCommand;
import rpn.component.RpGeometry;
import rpn.component.util.GeometryGraphND;
import rpn.component.util.GraphicsUtil;
import rpn.message.RPnNetworkStatus;
import rpnumerics.RPnCurve;
import wave.util.RealVector;
import rpn.parser.RPnDataModule;
import rpnumerics.BifurcationCurve;
import wave.multid.Coords2D;
import wave.multid.CoordsArray;
import wave.multid.Space;
import rpnumerics.RPNUMERICS;

/**
 *
 * @author moreira
 */
public class RPnStringPlotter extends RPn2DMouseController {

    private RealVector cursorPos_;
    private boolean addLine_ = false;
    public static List<RealVector> correspondentList = new ArrayList();
    private static RPnStringPlotter instance_;

    private RpGeometry geometry_;

   

    public void mouseMoved(MouseEvent me) {

        if (addLine_) {

            RPnPhaseSpacePanel panel = (RPnPhaseSpacePanel) me.getSource();

            Coords2D meDC = new Coords2D(me.getPoint().getX(), me.getPoint().getY());
            CoordsArray meWC = new CoordsArray(new Space(" ", RPNUMERICS.domainDim()));
            panel.scene().getViewingTransform().dcInverseTransform(meDC, meWC);
            geometry_.removeLastAnnotation();
            geometry_.showClassification(new CoordsArray(cursorPos_), meWC, panel.scene().getViewingTransform());
            panel.repaint();

        }

        UIController.instance().globalInputTable().reset();

    }

    public void mousePressed(MouseEvent me) {

        RPnVelocityPlotter.listaEquil.clear();

        RPnPhaseSpacePanel panel = (RPnPhaseSpacePanel) me.getSource();

        if (addLine_ == false) {

            int dim = RPNUMERICS.domainDim();
            Coords2D coordsDC = new Coords2D(me.getX(), me.getY());
            CoordsArray coordsWC = new CoordsArray(new Space("", dim));
            panel.scene().getViewingTransform().dcInverseTransform(coordsDC, coordsWC);

            RealVector newValue = new RealVector(dim);
            for (int i = 0; i < dim; i++) {
                newValue.setElement(i, coordsWC.getElement(i));
            }

            geometry_ = RPnDataModule.PHASESPACE.findClosestGeometry(newValue);
            RPnCurve curve = (RPnCurve) (geometry_.geomFactory().geomSource());
            RealVector closestPoint = curve.findClosestPoint(newValue);
            
            
            cursorPos_=closestPoint;
            geometry_.showClassification(new CoordsArray(cursorPos_), new CoordsArray(cursorPos_), panel.scene().getViewingTransform());
            
            addLine_ = true;
            
  
            if (curve instanceof BifurcationCurve) {
                int i = curve.findClosestSegment(newValue);
                GeometryGraphND.pMarcaDC = ((BifurcationCurve) curve).secondPointDCOtherVersion(i);
                correspondentList = ((BifurcationCurve) (curve)).correspondentPoints(GeometryGraphND.pMarca);

            } else {
                GeometryGraphND.pMarcaDC = GeometryGraphND.pMarca;
            }

        } else {
            
            Iterator<GraphicsUtil> annotationIterator = geometry_.getAnnotationIterator();
            
            GraphicsUtil lastAnnotation=null;
            while (annotationIterator.hasNext()) {
               lastAnnotation = annotationIterator.next();
                
            }
            
            RpCommand command = new RpCommand(lastAnnotation.toXML(),"classify");

            GenericExtensionCurveCommand.instance().logCommand(command);


            if (RPnNetworkStatus.instance().isOnline() && RPnNetworkStatus.instance().isMaster()) {
                RPnNetworkStatus.instance().sendCommand(rpn.controller.ui.UndoActionController.instance().getLastCommand().toXML());
            }
            
            
            
            addLine_ = false;
        }

        UIController.instance().globalInputTable().reset();

    }

    public void mouseDragged(MouseEvent me) {
//        RPnVelocityPlotter.listaEquil.clear();
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
//        if (curve instanceof BifurcationCurve) {
//            int i = curve.findClosestSegment(newValue);
//            GeometryGraphND.pMarcaDC = ((BifurcationCurve) curve).secondPointDCOtherVersion(i);
//            correspondentList = ((BifurcationCurve) (curve)).correspondentPoints(GeometryGraphND.pMarca);
//
//        } else {
//            GeometryGraphND.pMarcaDC = GeometryGraphND.pMarca;
//        }
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

    public static RPnStringPlotter instance() {
        if (instance_ == null) {
            instance_ = new RPnStringPlotter();
        }
        return instance_;
    }

}
