/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rpn.component.util;

import java.awt.event.MouseEvent;
//import java.util.logging.Level;
//import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.List;
import rpn.RPnPhaseSpacePanel;
//import rpn.RPnUIFrame;
import rpn.controller.ui.AREASELECTION_CONFIG;
import rpn.controller.ui.UIController;
//import rpnumerics.RPNUMERICS;
import rpnumerics.HugoniotCurve;
import rpnumerics.Orbit;
import rpnumerics.RPnCurve;
import wave.multid.Coords2D;
import wave.multid.CoordsArray;
import wave.multid.view.Scene;
import wave.multid.view.ViewingTransform;

/**
 *
 * @author moreira
 */
public class ControlClick {

    public static int ind = 0;

    //** Para a posição da string em coordenadas físicas
    static public List xStr = new ArrayList();
    static public List yStr = new ArrayList();

    //** Para a posição da string em coordenadas do dispositivo
    static public List xDevStr = new ArrayList();
    static public List yDevStr = new ArrayList();

    //** Para ponta da seta da string na curva em coordenadas físicas
    static public List xSeta = new ArrayList();
    static public List ySeta = new ArrayList();

    //** Para ponta da seta da string em coordenadas do dispositivo
    static public List xDevSeta = new ArrayList();
    static public List yDevSeta = new ArrayList();

    //** Para a posição da velocidade em coordenadas físicas
    static public List xVel = new ArrayList();
    static public List yVel = new ArrayList();

    //** Para a posição da velocidade em coordenadas do dispositivo
    static public List xDevVel = new ArrayList();
    static public List yDevVel = new ArrayList();

    //** Para ponta da seta da velocidade na curva em coordenadas físicas
    static public List xSetaVel = new ArrayList();
    static public List ySetaVel = new ArrayList();

    //** Para ponta da seta da velocidade na curva em coordenadas físicas
    static public List xDevSetaVel = new ArrayList();
    static public List yDevSetaVel = new ArrayList();

    //** Para indicar as curvas
    //static public List indCurva = new ArrayList();


    public static void clearMarks() {
        //*************************************// (Leandro)
        RPnCurve.lista.clear();

        GeometryUtil.vel.clear();
        GeometryUtil.tipo.clear();

        xDevSeta.clear();           xDevStr.clear();
        xSeta.clear();              xStr.clear();
        yDevSeta.clear();           yDevStr.clear();
        ySeta.clear();              yStr.clear();

        xDevSetaVel.clear();        xDevVel.clear();
        xSetaVel.clear();           xVel.clear();
        yDevSetaVel.clear();        yDevVel.clear();
        ySetaVel.clear();           yVel.clear();

        //***************************************
    }


    public static void mousePressed(MouseEvent event, Scene scene) {

        //**************************************************************   (Leandro)
        RPnPhaseSpacePanel panel_ = (RPnPhaseSpacePanel) event.getComponent();      // para usar transfs originais
        ViewingTransform transf = panel_.scene().getViewingTransform();             // para usar transfs originais

        //** Para usar transfs originais.   FUNCIONANDO!!!
        Coords2D dcCoords = new Coords2D(event.getX(), event.getY());
        CoordsArray wcCoords = new CoordsArray(transf.projectionMap().getDomain());
        transf.dcInverseTransform(dcCoords, wcCoords);
        //***

        //**************************************************************    Edson
        if (((UIController.instance_.getState() instanceof AREASELECTION_CONFIG)  ||
             (UIController.instance_.getState() instanceof CLASSIFIERAGENT_CONFIG) ||
             (UIController.instance_.getState() instanceof VELOCITYAGENT_CONFIG)) && (ind % 2) == 0) {

            for (int i = 0; i < GeometryUtil.targetPoint.getSize(); i++) {
                GeometryUtil.targetPoint.setElement(i, wcCoords.getElement(i));
            }

            GeometryUtil.findClosestCurve(GeometryUtil.targetPoint, RPnCurve.lista, 0);

            System.out.println("Target point: " + GeometryUtil.targetPoint);
            GeometryUtil.zContido.clear();
            GeometryUtil.wContido.clear();

            ind += 1;

            return;

        } //** Teste para segundo click. Segunda-feira, 09/05  (Leandro)
        else if (((UIController.instance_.getState() instanceof AREASELECTION_CONFIG)  ||
                  (UIController.instance_.getState() instanceof CLASSIFIERAGENT_CONFIG)  ||
                  (UIController.instance_.getState() instanceof VELOCITYAGENT_CONFIG)) && (ind % 2) == 1) {

            for (int i = 0; i < GeometryUtil.targetPoint.getSize(); i++) {
                GeometryUtil.cornerRet.setElement(i, wcCoords.getElement(i));
            }

            System.out.println("Corner do retangulo: " + GeometryUtil.cornerRet);

            if (UIController.instance_.getState() instanceof CLASSIFIERAGENT_CONFIG  &&  RPnCurve.lista.get(GeometryUtil.closestCurve) instanceof HugoniotCurve) {

                xStr.add(GeometryUtil.cornerRet.getElement(1));
                yStr.add(GeometryUtil.cornerRet.getElement(0));

                CoordsArray wcCoordsCR = new CoordsArray(GeometryUtil.cornerRet);
                Coords2D dcCoordsCR = new Coords2D();
                transf.viewPlaneTransform(wcCoordsCR, dcCoordsCR);
                double xCR = dcCoordsCR.getElement(1);
                double yCR = dcCoordsCR.getElement(0);
                xDevStr.add(xCR);
                yDevStr.add(yCR);

                //**
                xSeta.add(GeometryUtil.pMarca.getElement(1));
                ySeta.add(GeometryUtil.pMarca.getElement(0));
                CoordsArray wcCoordsSeta = new CoordsArray(GeometryUtil.pMarca);
                Coords2D dcCoordsSeta = new Coords2D();
                transf.viewPlaneTransform(wcCoordsSeta, dcCoordsSeta);
                double xSeta_ = dcCoordsSeta.getElement(1);
                double ySeta_ = dcCoordsSeta.getElement(0);
                xDevSeta.add(xSeta_);
                yDevSeta.add(ySeta_);
                //***

                ind += 1;
                return;

            }

            else if (UIController.instance_.getState() instanceof VELOCITYAGENT_CONFIG  &&  RPnCurve.lista.get(GeometryUtil.closestCurve) instanceof Orbit) {

                xVel.add(GeometryUtil.cornerRet.getElement(1));
                yVel.add(GeometryUtil.cornerRet.getElement(0));

                CoordsArray wcCoordsCR = new CoordsArray(GeometryUtil.cornerRet);
                Coords2D dcCoordsCR = new Coords2D();
                transf.viewPlaneTransform(wcCoordsCR, dcCoordsCR);
                double xCR = dcCoordsCR.getElement(1);
                double yCR = dcCoordsCR.getElement(0);
                xDevVel.add(xCR);
                yDevVel.add(yCR);

                //**
                xSetaVel.add(GeometryUtil.pMarca.getElement(1));
                ySetaVel.add(GeometryUtil.pMarca.getElement(0));
                CoordsArray wcCoordsSeta = new CoordsArray(GeometryUtil.pMarca);
                Coords2D dcCoordsSeta = new Coords2D();
                transf.viewPlaneTransform(wcCoordsSeta, dcCoordsSeta);
                double xSeta_ = dcCoordsSeta.getElement(1);
                double ySeta_ = dcCoordsSeta.getElement(0);
                xDevSetaVel.add(xSeta_);
                yDevSetaVel.add(ySeta_);
                //***

                ind += 1;
                return;

            }

            else if (UIController.instance_.getState() instanceof VELOCITYAGENT_CONFIG  &&  RPnCurve.lista.get(GeometryUtil.closestCurve) instanceof HugoniotCurve) {

                xVel.add(GeometryUtil.cornerRet.getElement(1));
                yVel.add(GeometryUtil.cornerRet.getElement(0));

                CoordsArray wcCoordsCR = new CoordsArray(GeometryUtil.cornerRet);
                Coords2D dcCoordsCR = new Coords2D();
                transf.viewPlaneTransform(wcCoordsCR, dcCoordsCR);
                double xCR = dcCoordsCR.getElement(1);
                double yCR = dcCoordsCR.getElement(0);
                xDevVel.add(xCR);
                yDevVel.add(yCR);

                //**
                xSetaVel.add(GeometryUtil.pMarca.getElement(1));
                ySetaVel.add(GeometryUtil.pMarca.getElement(0));
                CoordsArray wcCoordsSeta = new CoordsArray(GeometryUtil.pMarca);
                Coords2D dcCoordsSeta = new Coords2D();
                transf.viewPlaneTransform(wcCoordsSeta, dcCoordsSeta);
                double xSeta_ = dcCoordsSeta.getElement(1);
                double ySeta_ = dcCoordsSeta.getElement(0);
                xDevSetaVel.add(xSeta_);
                yDevSetaVel.add(ySeta_);
                //***

                ind += 1;
                return;

            }

            else {
                GeometryUtil.zContido.clear();
                GeometryUtil.wContido.clear();

                ind += 1;
                return;
            }


        }
        //**************************************************************   (Leandro)
        //**************************************************************    Edson

    }

}
