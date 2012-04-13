// VERSAO DE 03/08 - Funcionando melhor!!!

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rpn.component.util;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rpn.RPnPhaseSpaceAbstraction;
import rpn.RPnPhaseSpacePanel;
import rpn.controller.ui.AREASELECTION_CONFIG;
import rpn.controller.ui.UIController;
import rpn.parser.RPnDataModule;
import rpn.usecase.DragPlotAgent;
import rpnumerics.DoubleContactCurve;
import rpnumerics.HugoniotCurve;
import rpnumerics.HugoniotParams;
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
    public static int onCurve = 0;
    public static List<RealVector> listaEquil = new ArrayList();  ////////
    public static List<RealVector> listaVec = new ArrayList();  ////////
    public static List listaLambda = new ArrayList();  ////////
    public static double vel = 0;

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
            ClassifierAgent.strView.remove(lastIndex);
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
            VelocityAgent.velView.remove(lastIndex);
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
        listaEquil.clear();
        listaLambda.clear();
        listaVec.clear();
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

    private static void testRandAutoPar() {

        double fator = 0.03;
        
        RealVector autovet1 = new RealVector(2);
        double x = 2 * (Math.random() - 0.5);
        double y = 2 * (Math.random() - 0.5);
        autovet1.setElement(0, fator * x / Math.sqrt(x * x + y * y));
        autovet1.setElement(1, fator * y / Math.sqrt(x * x + y * y));

        listaVec.add(autovet1);

        double lambda = 2 * (Math.random() - 0.5);
        listaLambda.add(lambda);
        
    }

//    private static void equilPoints() {
//
//        if (GeometryUtil.closestCurve_ instanceof HugoniotCurve) {
//            HugoniotSegment segment = (HugoniotSegment) (((SegmentedCurve) GeometryUtil.closestCurve_).segments()).get(GeometryUtil.closestSeg);
//            //VelocityAgent.vel.add(segment.leftSigma());
//
//            double lSigma = segment.leftSigma();
//            double rSigma = segment.rightSigma();
//            double lX = segment.leftPoint().getElement(0);
//            double rX = segment.rightPoint().getElement(0);
//            double X = GeometryGraphND.pMarca.getElement(0);
//
//            vel = (rSigma - lSigma) * (X - lX) / (rX - lX) + lSigma;
//            System.out.println("Valor de vel : " +vel);
//
//            //----------------------------------------------------------
//
//            listaEquil.clear();
//            listaVec.clear();
//            listaLambda.clear();
//
//            //----------------------------------------------------------
//            // inclui o Uref na lista de pontos de equilibrio
//            RealVector pZero = ((HugoniotCurve) GeometryUtil.closestCurve_).getXZero();
//            listaEquil.add(pZero);
//            //*** em cada ponto de equilibro, há 2 autopares
//            for (int j = 0; j < 2; j++) {
//                testRandAutoPar();
//            }
//            //----------------------------------------------------------
//
//            int sz = (((SegmentedCurve) GeometryUtil.closestCurve_).segments().size());
//            for (int i = 0; i < sz; i++) {
//                HugoniotSegment segment_ = (HugoniotSegment) (((SegmentedCurve) GeometryUtil.closestCurve_).segments()).get(i);
//
//                if ((segment_.leftSigma() <= vel && segment_.rightSigma() >= vel)
//                        || (segment_.leftSigma() >= vel && segment_.rightSigma() <= vel)) {
//
//                    //----------------------------------------------------------
//                    double lSigma_ = segment_.leftSigma();
//                    double rSigma_ = segment_.rightSigma();
//                    double lX_ = segment_.leftPoint().getElement(0);
//                    double rX_ = segment_.rightPoint().getElement(0);
//                    double lY_ = segment_.leftPoint().getElement(1);
//                    double rY_ = segment_.rightPoint().getElement(1);
//
//                    double X_ = (rX_ - lX_) * (vel - lSigma_) / (rSigma_ - lSigma_) + lX_;
//                    double Y_ = (rY_ - lY_) * (vel - lSigma_) / (rSigma_ - lSigma_) + lY_;
//                    RealVector p = new RealVector(2);
//                    p.setElement(0, X_);
//                    p.setElement(1, Y_);
//
//                    if (p != pZero) {
//                        listaEquil.add(p);
//                    }
//
//                    //*** em cada ponto de equilibro, há 2 autopares
//                    for (int j = 0; j < 2; j++) {
//                        testRandAutoPar();
//                    }
//                    //----------------------------------------------------------
//
//                }
//            }
//
//            System.out.println("listaEquil.size() : " +listaEquil.size());
//            System.out.println("listaLambda.size() : " +listaLambda.size());
//            System.out.println("Lista de pontos de equilibrio : " +listaEquil.toString());
//
//            //----------------------------------------------------------
//
//        }
//
//    }

    public static void mousePressed(MouseEvent event) {

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
            //System.out.println("Target point: " + GeometryGraphND.targetPoint);

            RPnCurve curve = GeometryUtil.findClosestCurve(GeometryGraphND.targetPoint);
            
            //if (curve instanceof SegmentedCurve)     GeometryGraphND.pMarca = ((RealSegment) (((SegmentedCurve) curve).segments()).get(GeometryUtil.closestSeg)).p1();
            //if (curve instanceof Orbit)              GeometryGraphND.pMarca = ((Orbit) curve).getPoints()[GeometryUtil.closestSeg];

            
            GeometryGraphND.pMarca = curve.findClosestPoint(GeometryGraphND.targetPoint);


            if (curve instanceof DoubleContactCurve) {
                GeometryGraphND.pMarcaDC = secondPointDC(curve);
            }
            else GeometryGraphND.pMarcaDC = GeometryGraphND.pMarca;
            
            
            GeometryGraphND.zContido.clear();
            GeometryGraphND.wContido.clear();


            //*** esboço para encontrar pontos de equilibrio
//            if (UIController.instance().getState() instanceof VELOCITYAGENT_CONFIG) {
//                equilPoints();
//            }
            //***


            //*** esboço para encontrar pontos de equilibrio
            if (UIController.instance().getState() instanceof VELOCITYAGENT_CONFIG) {
                if (GeometryUtil.closestCurve_ instanceof HugoniotCurve)
                   ((HugoniotCurve)(GeometryUtil.closestCurve_)).equilPoints();
            }
            //***


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

                //ClassifierAgent.strView.add(1);
                //--------------------------------------------------------------
                
                if (panel_.getName().equals("Phase Space")) {
                    ClassifierAgent.strView.add(1);
                } //else ClassifierAgent.strView.add(-1);
                if (panel_.getName().equals("RightPhase Space")) {
                    ClassifierAgent.strView.add(2);
                }
                if (panel_.getName().equals("LeftPhase Space")) {
                    ClassifierAgent.strView.add(3);
                }

                //--------------------------------------------------------------

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
                    //HugoniotSegment segment = (HugoniotSegment)(((SegmentedCurve)GeometryUtil.closestCurve_).segments()).get(GeometryUtil.closestSeg);
                    //VelocityAgent.vel.add(segment.leftSigma());

                    VelocityAgent.vel.add(vel);

                }

                VelocityAgent.xVel.add(GeometryGraphND.cornerStr.getElement(1));
                VelocityAgent.yVel.add(GeometryGraphND.cornerStr.getElement(0));

                //VelocityAgent.velView.add(1);

                //--------------------------------------------------------------

                if (panel_.getName().equals("Phase Space")) {
                    VelocityAgent.velView.add(1);
                }
                if (panel_.getName().equals("RightPhase Space")) {
                    VelocityAgent.velView.add(2);
                }
                if (panel_.getName().equals("LeftPhase Space")) {
                    VelocityAgent.velView.add(3);
                }

                //--------------------------------------------------------------


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



