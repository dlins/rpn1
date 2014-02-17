/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rpn.controller.ui;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import rpn.RPnLeftPhaseSpaceAbstraction;
import rpn.RPnPhaseSpaceAbstraction;
import rpn.RPnPhaseSpacePanel;
import rpn.RPnRightPhaseSpaceAbstraction;
import rpn.component.BifurcationCurveGeom;
import rpn.component.ClosestDistanceCalculator;
import rpn.component.RpGeometry;
import rpn.component.util.Label;
import wave.util.RealVector;
import rpnumerics.BifurcationCurve;
import wave.multid.Coords2D;
import wave.multid.CoordsArray;
import wave.multid.Space;
import rpnumerics.RPNUMERICS;
import wave.multid.view.ViewingAttr;
import wave.util.RealSegment;

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

            if (rpGeometry instanceof BifurcationCurveGeom) {

                BifurcationCurveGeom bifurcationCurveGeom = (BifurcationCurveGeom) rpGeometry;
                RpGeometry otherSide = bifurcationCurveGeom.getOtherSide();

                BifurcationCurve curve = (BifurcationCurve) (bifurcationCurveGeom.geomFactory().geomSource());

                List<RealSegment> curveSegments = null;
                List<RealSegment> otherSideSegments = null;

                if (phaseSpace instanceof RPnLeftPhaseSpaceAbstraction) {
                    curveSegments = curve.leftSegments();
                    otherSideSegments = curve.rightSegments();

                } else if (phaseSpace instanceof RPnRightPhaseSpaceAbstraction) {

                    curveSegments = curve.rightSegments();
                    otherSideSegments = curve.leftSegments();

                } else {
                    bifurcationCurveGeom.removeLastAnnotation();
                    bifurcationCurveGeom.removeLastAnnotation();
                    bifurcationCurveGeom.showCorrespondentPoint(coordsWC, coordsWC, panel.scene().getViewingTransform());
                    return;
                }

//                else {
//                    List<RealSegment> segments = curve.segments();
//                    int leftSize = curve.leftSegments().size();
//                    
//                    ArrayList<RealSegment> tempLeft = new ArrayList<RealSegment>();
//                    
//                    for (int i = 0; i < leftSize; i++) {
//                         tempLeft.setElement(i,segments.get(i));
//                        
//                    }
//                     ArrayList<RealSegment> tempRight = new ArrayList<RealSegment>();
//                     
//                    for (int i = leftSize; i < segments.size(); i++) {
//                         tempRight.setElement(i-leftSize,segments.get(i));
//                        
//                    }
//                    
//                    
//                }
                // Mover para a classe BifurcationCurveGeom . Colocar a correspondecia na geometria principal e nas geometrias laterais.
                //Nas geometrias laterais , usar o codigo acima . Na geometria principal , descobrir em que parte da curva esta o ponto
                //mais proximo pelo indice do segmento. A partir desse indice achar o seu "correspondente" na mesma curva e colocar a marca da 
                // coorespondencia em ambos os pontos.
                ClosestDistanceCalculator closestCalculator = new ClosestDistanceCalculator(curveSegments, targetPoint);

                int segmentIndex = closestCalculator.getSegmentIndex();

                RealSegment otherSegment = otherSideSegments.get(segmentIndex);

                RealVector curveMark = closestCalculator.getClosestPoint();

                List<Object> wcObjectsCurveSide = new ArrayList();

                wcObjectsCurveSide.add(curveMark);
                wcObjectsCurveSide.add(counter + "");

                Label testeLabelOtherSide = new Label(wcObjectsCurveSide, panel.scene().getViewingTransform(), new ViewingAttr(Color.white));

                bifurcationCurveGeom.removeLastAnnotation();
                bifurcationCurveGeom.addAnnotation(testeLabelOtherSide);

                List<Object> wcObjects = new ArrayList();

                RealVector otherCurvePoint = ClosestDistanceCalculator.convexCombination(otherSegment.p1(), otherSegment.p2(), closestCalculator.getAlpha());

                wcObjects.add(otherCurvePoint);
                wcObjects.add(counter + "");

                counter++;

                Label testeLabel = new Label(wcObjects, panel.scene().getViewingTransform(), new ViewingAttr(Color.white));
                otherSide.removeLastAnnotation();
                otherSide.addAnnotation(testeLabel);

                panel.repaint();
            }

        }

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

            RPnPhaseSpaceAbstraction phaseSpace = (RPnPhaseSpaceAbstraction) panel.scene().getAbstractGeom();
            Iterator<RpGeometry> geometry = phaseSpace.getGeomObjIterator();
            while (geometry.hasNext()) {

                RpGeometry rpGeometry = geometry.next();

                if (rpGeometry instanceof BifurcationCurveGeom) {

                    BifurcationCurveGeom bifurcationCurveGeom = (BifurcationCurveGeom) rpGeometry;

                    if (phaseSpace instanceof RPnLeftPhaseSpaceAbstraction) {

                        BifurcationCurve curve = (BifurcationCurve) (bifurcationCurveGeom.geomFactory().geomSource());

                        int segmentIndex = curve.findClosestSegment(newValue);

                        RpGeometry otherSide = bifurcationCurveGeom.getOtherSide();

                        RealSegment curveSegment = curve.leftSegments().get(segmentIndex);
                        RealSegment otherSegment = curve.rightSegments().get(segmentIndex);

                        RealVector curveMark = curveSegment.p1();

                        List<Object> wcObjectsCurveSide = new ArrayList();

                        wcObjectsCurveSide.add(curveMark);
                        wcObjectsCurveSide.add(counter + "");

                        Label testeLabelOtherSide = new Label(wcObjectsCurveSide, panel.scene().getViewingTransform(), new ViewingAttr(Color.white));

                        bifurcationCurveGeom.addAnnotation(testeLabelOtherSide);

                        List<Object> wcObjects = new ArrayList();

                        wcObjects.add(otherSegment.p1());
                        wcObjects.add(counter + "");

                        counter++;

                        Label testeLabel = new Label(wcObjects, panel.scene().getViewingTransform(), new ViewingAttr(Color.white));

                        otherSide.addAnnotation(testeLabel);

                    } else if (phaseSpace instanceof RPnRightPhaseSpaceAbstraction) {

                        BifurcationCurve curve = (BifurcationCurve) (bifurcationCurveGeom.geomFactory().geomSource());

                        int segmentIndex = curve.findClosestSegment(newValue);

                        RpGeometry otherSide = bifurcationCurveGeom.getOtherSide();

                        RealSegment curveSegment = curve.rightSegments().get(segmentIndex);
                        RealSegment otherSegment = curve.leftSegments().get(segmentIndex);

                        RealVector curveMark = curveSegment.p1();

                        List<Object> wcObjectsCurveSide = new ArrayList();

                        wcObjectsCurveSide.add(curveMark);
                        wcObjectsCurveSide.add(counter + "");

                        Label testeLabelOtherSide = new Label(wcObjectsCurveSide, panel.scene().getViewingTransform(), new ViewingAttr(Color.white));

                        bifurcationCurveGeom.addAnnotation(testeLabelOtherSide);

                        List<Object> wcObjects = new ArrayList();

                        wcObjects.add(otherSegment.p1());
                        wcObjects.add(counter + "");

                        counter++;

                        Label testeLabel = new Label(wcObjects, panel.scene().getViewingTransform(), new ViewingAttr(Color.white));

                        otherSide.addAnnotation(testeLabel);

                    } else {

                        BifurcationCurve curve = (BifurcationCurve) (bifurcationCurveGeom.geomFactory().geomSource());

                        int segmentIndex = curve.findClosestSegment(newValue);

                        if (segmentIndex < curve.leftSegments().size()) {

                            RealSegment curveSegment = (RealSegment) curve.segments().get(segmentIndex);
                            RealSegment otherSegment = (RealSegment) curve.segments().get(curve.leftSegments().size() - 1 + segmentIndex);

                            RealVector curveMark = curveSegment.p1();

                            List<Object> wcObjectsCurveSide = new ArrayList();

                            wcObjectsCurveSide.add(curveMark);
                            wcObjectsCurveSide.add(counter + "");

                            Label testeLabelOtherSide = new Label(wcObjectsCurveSide, panel.scene().getViewingTransform(), new ViewingAttr(Color.white));

                            bifurcationCurveGeom.addAnnotation(testeLabelOtherSide);

                            List<Object> wcObjects = new ArrayList();

                            wcObjects.add(otherSegment.p1());
                            wcObjects.add(counter + "");

                            counter++;

                            Label testeLabel = new Label(wcObjects, panel.scene().getViewingTransform(), new ViewingAttr(Color.white));

                            bifurcationCurveGeom.addAnnotation(testeLabel);

                        } else {

                            RealSegment curveSegment = (RealSegment) curve.segments().get(curve.leftSegments().size() - 1 + segmentIndex);

                            RealSegment otherSegment = (RealSegment) curve.segments().get(segmentIndex);

                            RealVector curveMark = curveSegment.p1();

                            List<Object> wcObjectsCurveSide = new ArrayList();

                            wcObjectsCurveSide.add(curveMark);
                            wcObjectsCurveSide.add(counter + "");

                            Label testeLabelOtherSide = new Label(wcObjectsCurveSide, panel.scene().getViewingTransform(), new ViewingAttr(Color.white));

                            bifurcationCurveGeom.addAnnotation(testeLabelOtherSide);

                            List<Object> wcObjects = new ArrayList();

                            wcObjects.add(otherSegment.p1());
                            wcObjects.add(counter + "");

                            counter++;

                            Label testeLabel = new Label(wcObjects, panel.scene().getViewingTransform(), new ViewingAttr(Color.white));

                            bifurcationCurveGeom.addAnnotation(testeLabel);

                        }

//                  
//            }
//            
//            geometry_ =geometry;
                        addLine_ = true;

                    }

                }

//            if (curve instanceof BifurcationCurve) {
//                int i = curve.findClosestSegment(newValue);
//                GeometryGraphND.pMarcaDC = ((BifurcationCurve) curve).secondPointDCOtherVersion(i);
//                correspondentList = ((BifurcationCurve) (curve)).correspondentPoints(GeometryGraphND.pMarca);
//                
//                for (RealVector realVector : correspondentList) {
//                    System.out.println(realVector);
//                    
//                }
//                
//            } else {
//                GeometryGraphND.pMarcaDC = GeometryGraphND.pMarca;
//            }
//        } else {
                addLine_ = false;
//        }

                UIController.instance()
                        .globalInputTable().reset();

            }
        }
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
