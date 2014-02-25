/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rpn.controller.ui;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import rpn.RPnPhaseSpaceAbstraction;
import rpn.RPnPhaseSpacePanel;
import rpn.component.BifurcationCurveBranchGeom;
import rpn.component.RpGeometry;
import wave.util.RealVector;
import wave.multid.Coords2D;
import wave.multid.CoordsArray;
import wave.multid.Space;
import rpnumerics.RPNUMERICS;

/**
 *
 * @author moreira
 */
public class RPnCorrespondencePlotter extends RPn2DMouseController {

    private RealVector cursorPos_;
    private boolean addLine_ = false;
    public static List<RealVector> correspondentList = new ArrayList();
    private static RPnCorrespondencePlotter instance_;

    private static int counter = 0;

    @Override
    public void mouseMoved(MouseEvent me) {

//
        RPnPhaseSpacePanel panel = (RPnPhaseSpacePanel) me.getSource();

        int dim = RPNUMERICS.domainDim();
        Coords2D coordsDC = new Coords2D(me.getX(), me.getY());
        CoordsArray coordsWC = new CoordsArray(new Space("", dim));
        panel.scene().getViewingTransform().dcInverseTransform(coordsDC, coordsWC);

        RealVector targetPoint = new RealVector(dim);
        for (int i = 0; i < dim; i++) {
            targetPoint.setElement(i, coordsWC.getElement(i));
        }

        RPnPhaseSpaceAbstraction phaseSpace = (RPnPhaseSpaceAbstraction) panel.scene().getAbstractGeom();
        Iterator<RpGeometry> geometry = phaseSpace.getGeomObjIterator();
        while (geometry.hasNext()) {

            RpGeometry rpGeometry = geometry.next();

            if (rpGeometry instanceof BifurcationCurveBranchGeom) {

                BifurcationCurveBranchGeom bifurcationCurveGeom = (BifurcationCurveBranchGeom) rpGeometry;
                bifurcationCurveGeom.removeLastAnnotation();
                bifurcationCurveGeom.removeLastAnnotation();
                bifurcationCurveGeom.showCorrespondentPoint(coordsWC, panel.scene().getViewingTransform());
         

                UIController.instance().panelsUpdate();
            }

        }

    }

    public void mousePressed(MouseEvent me) {

//        RPnVelocityPlotter.listaEquil.clear();
//
//        RPnPhaseSpacePanel panel = (RPnPhaseSpacePanel) me.getSource();
//
//        if (addLine_ == false) {
//
//                        addLine_ = true;
//
////                    }
//
////                }
//
////            if (curve instanceof BifurcationCurve) {
////                int i = curve.findClosestSegment(newValue);
////                GeometryGraphND.pMarcaDC = ((BifurcationCurve) curve).secondPointDCOtherVersion(i);
////                correspondentList = ((BifurcationCurve) (curve)).correspondentPoints(GeometryGraphND.pMarca);
////                
////                for (RealVector realVector : correspondentList) {
////                    System.out.println(realVector);
////                    
////                }
////                
////            } else {
////                GeometryGraphND.pMarcaDC = GeometryGraphND.pMarca;
////            }
//        } else {
//                addLine_ = false;
//        }
//
//                UIController.instance()
//                        .globalInputTable().reset();
//
////            }
////       }
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

    public static RPnCorrespondencePlotter instance() {
        if (instance_ == null) {
            instance_ = new RPnCorrespondencePlotter();
        }
        return instance_;
    }

}
