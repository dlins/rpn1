// VERSAO DE 03/08 - Funcionando melhor!!!

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rpn.component.util;

import java.awt.event.MouseEvent;
import rpn.RPnPhaseSpacePanel;
import rpn.controller.ui.AREASELECTION_CONFIG;
import rpn.controller.ui.UIController;
import rpnumerics.DoubleContactCurve;
import rpnumerics.HugoniotCurve;
import rpnumerics.HugoniotSegment;
import rpnumerics.Orbit;
import rpnumerics.OrbitPoint;
import rpnumerics.RPNUMERICS;
import rpnumerics.RPnCurve;
import rpnumerics.SegmentedCurve;
import wave.multid.Coords2D;
import wave.multid.CoordsArray;
import wave.multid.view.Scene;
import wave.multid.view.ViewingTransform;
import wave.util.RealSegment;
import wave.util.RealVector;

/**
 *
 * @author moreira
 */
public class ControlClick {

    public static int ind = 0;

    //*************************************
    public static void clearpMarca() {
        for (int i=0; i<GeometryGraphND.pMarca.getSize(); i++) {
            GeometryGraphND.pMarca.setElement(i, 100.);
        }
    }

    public static void clearLastString() {
        clearpMarca();

        if (UIController.instance().getState() instanceof CLASSIFIERAGENT_CONFIG) {
            int lastIndex = ClassifierAgent.tipo.size() - 1;
            ClassifierAgent.indCurvaCla.remove(lastIndex);
            ClassifierAgent.tipo.remove(lastIndex);
            ClassifierAgent.xDevSeta.remove(lastIndex);
            ClassifierAgent.xDevStr.remove(lastIndex);
            ClassifierAgent.xSeta.remove(lastIndex);
            ClassifierAgent.xStr.remove(lastIndex);
            ClassifierAgent.yDevSeta.remove(lastIndex);
            ClassifierAgent.yDevStr.remove(lastIndex);
            ClassifierAgent.ySeta.remove(lastIndex);
            ClassifierAgent.yStr.remove(lastIndex);
        }

        if (UIController.instance().getState() instanceof VELOCITYAGENT_CONFIG) {
            int lastIndex = VelocityAgent.vel.size() - 1;
            VelocityAgent.indCurvaVel.remove(lastIndex);
            VelocityAgent.vel.remove(lastIndex);
            VelocityAgent.xDevSetaVel.remove(lastIndex);
            VelocityAgent.xDevVel.remove(lastIndex);
            VelocityAgent.xSetaVel.remove(lastIndex);
            VelocityAgent.xVel.remove(lastIndex);
            VelocityAgent.yDevSetaVel.remove(lastIndex);
            VelocityAgent.yDevVel.remove(lastIndex);
            VelocityAgent.ySetaVel.remove(lastIndex);
            VelocityAgent.yVel.remove(lastIndex);
        }
    }

    public static void clearAllStrings() {
        VelocityAgent.clearVelocities();
        ClassifierAgent.clearClassifiers();
    }

    public static void clearAll() {
        ClassifierAgent.indCurvaCla.clear();
        VelocityAgent.indCurvaVel.clear();
        clearAllStrings();
        GeometryUtil.listResolution.clear();
    }
    //***************************************

    //---------------------------------------

    public static RealVector secondPointDC(RPnCurve curve_) {

        int jDC = 0;
        SegmentedCurve curve = (SegmentedCurve)curve_;

        if (GeometryUtil.closestSeg > curve.segments().size() / 2) {
            jDC = GeometryUtil.closestSeg - curve.segments().size() / 2;
        } else {
            jDC = GeometryUtil.closestSeg + curve.segments().size() / 2;
        }

        RealVector pDC = new RealVector(((RealSegment) ((curve).segments()).get(jDC)).p1());

        return pDC;
    }

    //---------------------------------------

    public static void mousePressed(MouseEvent event, Scene scene) {

        RPnPhaseSpacePanel panel_ = (RPnPhaseSpacePanel) event.getComponent();
        ViewingTransform transf = panel_.scene().getViewingTransform();

        //** Para usar transfs originais.   FUNCIONANDO!!!
        Coords2D dcCoords = new Coords2D(event.getX(), event.getY());
        CoordsArray wcCoords = new CoordsArray(transf.projectionMap().getDomain());
        transf.dcInverseTransform(dcCoords, wcCoords);
        //***

        //****** Para primeiro click
        if (((UIController.instance().getState() instanceof AREASELECTION_CONFIG)
                || (UIController.instance().getState() instanceof AREASELECTION_CONFIG2)
                || (UIController.instance().getState() instanceof CLASSIFIERAGENT_CONFIG)
                || (UIController.instance().getState() instanceof VELOCITYAGENT_CONFIG)) && (ind % 2) == 0) {

            for (int i = 0; i < GeometryGraphND.targetPoint.getSize(); i++) {
                GeometryGraphND.targetPoint.setElement(i, wcCoords.getElement(i));
            }
            //System.out.println("Target point: " + GeometryUtil.targetPoint);

            RPnCurve curve = GeometryUtil.findClosestCurve(GeometryGraphND.targetPoint);
            
            if (curve instanceof SegmentedCurve)     GeometryGraphND.pMarca = ((RealSegment) (((SegmentedCurve) curve).segments()).get(GeometryUtil.closestSeg)).p1();
            if (curve instanceof Orbit)              GeometryGraphND.pMarca = ((Orbit) curve).getPoints()[GeometryUtil.closestSeg];

//            if (curve instanceof SegmentedCurve) {
//                RealVector V1 = ((RealSegment) (((SegmentedCurve) curve).segments()).get(GeometryUtil.closestSeg)).p1();
//                RealVector V2 = ((RealSegment) (((SegmentedCurve) curve).segments()).get(GeometryUtil.closestSeg)).p2();
//                RealVector V1MinusV2 = new RealVector(V1.getSize());
//
//                for (int i = 0; i < V1.getSize(); i++) {
//                    V1MinusV2.setElement(i, V2.getElement(i) - V1.getElement(i));
//                }
//
//                GeometryGraphND.pMarca = curve.findClosestPoint(GeometryGraphND.targetPoint);
//
//            }
//            if (curve instanceof Orbit) {
//                GeometryGraphND.pMarca = curve.findClosestPoint(GeometryGraphND.targetPoint);
//            }
            
            if (curve instanceof DoubleContactCurve) {
                GeometryGraphND.pMarcaDC = secondPointDC(curve);
            }
            else GeometryGraphND.pMarcaDC = GeometryGraphND.pMarca;
            
            GeometryGraphND.zContido.clear();
            GeometryGraphND.wContido.clear();

            ind += 1;

            return;

        }
        //******


        //****** Para segundo click, com  AREASELECTION_CONFIG  ativo
        if ((UIController.instance().getState() instanceof AREASELECTION_CONFIG  ||
                (UIController.instance().getState() instanceof AREASELECTION_CONFIG2)) && (ind % 2) == 1) {

            for (int i = 0; i < GeometryGraphND.targetPoint.getSize(); i++) {
                GeometryGraphND.cornerRet.setElement(i, wcCoords.getElement(i));
            }
            //System.out.println("Corner do retangulo: " + GeometryGraphND.cornerRet);

            GeometryGraphND.zContido.clear();
            GeometryGraphND.wContido.clear();

            ind += 1;

            return;

        }
        //******

        
        //****** Para segundo click, com  CLASSIFIERAGENT_CONFIG  ou  VELOCITYAGENT_CONFIG  ativo
        if (((UIController.instance().getState() instanceof CLASSIFIERAGENT_CONFIG)
                || (UIController.instance().getState() instanceof VELOCITYAGENT_CONFIG)) && (ind % 2) == 1) {

            for (int i = 0; i < GeometryGraphND.targetPoint.getSize(); i++) {
                GeometryGraphND.cornerStr.setElement(i, wcCoords.getElement(i));
                GeometryGraphND.cornerRet.setElement(i, 0);
                GeometryGraphND.targetPoint.setElement(i, 0.);
            }
            //System.out.println("Corner da string: " + GeometryUtil.cornerStr);


            //*** Botao CLASSIFY para HUGONIOT CURVE
            if (UIController.instance().getState() instanceof CLASSIFIERAGENT_CONFIG && GeometryUtil.closestCurve_ instanceof HugoniotCurve) {
            
                HugoniotSegment segment = (HugoniotSegment)(((SegmentedCurve)GeometryUtil.closestCurve_).segments()).get(GeometryUtil.closestSeg);
                ClassifierAgent.tipo.add(segment.getType());
                
                ClassifierAgent.xStr.add(GeometryGraphND.cornerStr.getElement(1));
                ClassifierAgent.yStr.add(GeometryGraphND.cornerStr.getElement(0));

                ClassifierAgent.strView.add(1);

                CoordsArray wcCoordsCR = new CoordsArray(GeometryGraphND.cornerStr);
                Coords2D dcCoordsCR = new Coords2D();
                transf.viewPlaneTransform(wcCoordsCR, dcCoordsCR);
                double xCR = dcCoordsCR.getElement(1);
                double yCR = dcCoordsCR.getElement(0);
                ClassifierAgent.xDevStr.add(xCR);
                ClassifierAgent.yDevStr.add(yCR);

                //***
                ClassifierAgent.xSeta.add(GeometryGraphND.pMarca.getElement(1));
                ClassifierAgent.ySeta.add(GeometryGraphND.pMarca.getElement(0));
                CoordsArray wcCoordsSeta = new CoordsArray(GeometryGraphND.pMarca);
                Coords2D dcCoordsSeta = new Coords2D();
                transf.viewPlaneTransform(wcCoordsSeta, dcCoordsSeta);
                double xSeta_ = dcCoordsSeta.getElement(1);
                double ySeta_ = dcCoordsSeta.getElement(0);
                ClassifierAgent.xDevSeta.add(xSeta_);
                ClassifierAgent.yDevSeta.add(ySeta_);
                //***

                ClassifierAgent.indCurvaCla.add(GeometryUtil.closestCurve);
                
                ind += 1;
                return;

            } //***

            //*** Botao VELOCITY
            else if (UIController.instance().getState() instanceof VELOCITYAGENT_CONFIG) {

                if (GeometryUtil.closestCurve_ instanceof Orbit) {
                    OrbitPoint point = (OrbitPoint) ((Orbit) GeometryUtil.closestCurve_).getPoints()[GeometryUtil.closestSeg];
                    VelocityAgent.vel.add(point.getLambda());
                }
                else if (GeometryUtil.closestCurve_ instanceof HugoniotCurve) {
                    HugoniotSegment segment = (HugoniotSegment)(((SegmentedCurve)GeometryUtil.closestCurve_).segments()).get(GeometryUtil.closestSeg);
                    VelocityAgent.vel.add(segment.leftSigma());
                }

                VelocityAgent.xVel.add(GeometryGraphND.cornerStr.getElement(1));
                VelocityAgent.yVel.add(GeometryGraphND.cornerStr.getElement(0));

                VelocityAgent.velView.add(1);

                CoordsArray wcCoordsCR = new CoordsArray(GeometryGraphND.cornerStr);
                Coords2D dcCoordsCR = new Coords2D();
                transf.viewPlaneTransform(wcCoordsCR, dcCoordsCR);
                double xCR = dcCoordsCR.getElement(1);
                double yCR = dcCoordsCR.getElement(0);
                VelocityAgent.xDevVel.add(xCR);
                VelocityAgent.yDevVel.add(yCR);

                //***
                VelocityAgent.xSetaVel.add(GeometryGraphND.pMarca.getElement(1));
                VelocityAgent.ySetaVel.add(GeometryGraphND.pMarca.getElement(0));
                CoordsArray wcCoordsSeta = new CoordsArray(GeometryGraphND.pMarca);
                Coords2D dcCoordsSeta = new Coords2D();
                transf.viewPlaneTransform(wcCoordsSeta, dcCoordsSeta);
                double xSeta_ = dcCoordsSeta.getElement(1);
                double ySeta_ = dcCoordsSeta.getElement(0);
                VelocityAgent.xDevSetaVel.add(xSeta_);
                VelocityAgent.yDevSetaVel.add(ySeta_);
                //***

                VelocityAgent.indCurvaVel.add(GeometryUtil.closestCurve);

                ind += 1;
                return;

            } //***


            else {
                GeometryGraphND.zContido.clear();
                GeometryGraphND.wContido.clear();

                ind += 1;
                return;
            }


        }

    }
}



