/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rpn.component.util;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
//import java.util.logging.Level;
//import java.util.logging.Logger;
import rpn.RPnPhaseSpacePanel;
import rpn.controller.ui.AREASELECTION_CONFIG;
import rpn.controller.ui.UIController;
import rpnumerics.RPNUMERICS;
import wave.multid.Coords2D;
import wave.multid.CoordsArray;
import wave.multid.view.Scene;
import wave.multid.view.ViewingTransform;
import wave.util.RealVector;
import rpn.controller.ui.CLASSIFIERAGENT_CONFIG;
import rpn.controller.ui.VELOCITYAGENT_CONFIG;

/**
 *
 * @author moreira
 */

public class GeometryGraph3D extends GeometryGraphND {


//    public void drawFirstPanel(Graphics g, Scene scene_) {
//
//        if (mostraGrid != 0){
//            drawGrid(g, scene_);
//        }
//
//        Graphics2D graph = (Graphics2D) g;
//
//        if (zerado == 2) {
//
//            if (UIController.instance().getState() instanceof AREASELECTION_CONFIG) {
//                g.setColor(cor12);
//                graph.draw(line1);
//                    graph.draw(line2);
//                }
//
//                g.setColor(cor34);
//                graph.draw(line3);
//                graph.draw(line4);
//                graph.draw(line3DC);
//                graph.draw(line4DC);
//
//
//                if ((ControlClick.ind % 2) == 0) {
//
//                    if (UIController.instance().getState() instanceof AREASELECTION_CONFIG) {
//                        g.setColor(cor56);
//                        graph.draw(line5);
//                        graph.draw(line6);
//                        g.setColor(corSquare);
//                        graph.draw(square1);
//                    }
//
//
//                    //*** Para os botoes Classify e Velocity
//                    if ((UIController.instance().getState() instanceof CLASSIFIERAGENT_CONFIG)  ||
//                        (UIController.instance().getState() instanceof VELOCITYAGENT_CONFIG)) {
//
//                        //defineClassifiers(g, scene_);
//                        //defineVelocities(g, scene_);
//
//                    }
//                    //*** Fim dos botoes Classify e Velocity
//
//
//                }
//
//            } else {
//                g.setColor(cor78);
//                graph.draw(line7);
//                graph.draw(line8);
//
//                if (((ControlClick.ind % 2) == 0)  &&  (UIController.instance().getState() instanceof AREASELECTION_CONFIG)) {
//                    g.setColor(corSquare);
//                    graph.draw(square2);
//                }
//
//            }
//    }
//
//
//    public void drawSecondPanel(Graphics g, Scene scene_) {
//
//        Graphics2D graph = (Graphics2D) g;
//
//        if (zerado == 2) {
//
//            g.setColor(cor78);
//            graph.draw(line7);
//            graph.draw(line8);
//
//            if (zContido.size() != 0) {
//                if (UIController.instance().getState() instanceof AREASELECTION_CONFIG) {
//                    g.setColor(corSquare);
//                    graph.draw(square2);
//                }
//            }
//
//        } else {
//
//            if (UIController.instance().getState() instanceof AREASELECTION_CONFIG) {
//                g.setColor(cor12);
//                graph.draw(line1);
//                graph.draw(line2);
//            }
//
//            g.setColor(cor34);
//            graph.draw(line3);
//            graph.draw(line4);
//
//
//            if (((ControlClick.ind % 2) == 0) && (UIController.instance().getState() instanceof CLASSIFIERAGENT_CONFIG)) {
//                g.setColor(corString);
//
//                //*** Analisar o que fazer caso o click seja no painel 2.
//                //*** Precisa imprimir as strings lá?
//                //*** Se sim, adaptar as linhas abaixo.
//
//                //String s1 = Integer.toString(GeometryUtil.tipo);
//                //g.drawString((s1 + HugoniotSegGeom.s[GeometryUtil.tipo]), (int) (yCR), (int) (xCR));
//                //Line2D line_ = new Line2D.Double(yMP, xMP, yCR, xCR);
//                //graph.draw(line_);
//            }
//            //****************************
//
//
//            if (((ControlClick.ind % 2) == 0) && (UIController.instance().getState() instanceof AREASELECTION_CONFIG)) {
//                g.setColor(cor56);
//                graph.draw(line5);
//                graph.draw(line6);
//                g.setColor(corSquare);
//                graph.draw(square1);
//            }
//
//        }
//
//    }
//
//
//    public void paintComponent(Graphics g, Scene scene_) {               // era static
//
//        /*
//         * line1 e line2 = marcador da posição do primeiro click; plot apenas se acionada a seleção de área.
//         * line3 e line4 = marcador sobre o ponto/segmento mais próximo de uma curva (encontrado no painel onde foi dado o click).
//         * line5 e line6 = marcador da posição do segundo click; plot apenas se acionada a seleção de área.
//         * line7 e line8 = marcador sobre o ponto/segmento mais próximo de uma curva (encontrado no painel onde não foi dado o click).
//         * square1 e square2 = retângulos usados para área de seleção.
//         */
//
//        changeColor();
//
//        //** Desenha no primeiro painel
//        if (scene_.getViewingTransform().projectionMap().getCompIndexes()[0] == 1) {
//            drawFirstPanel(g, scene_);
//
//        }
//        //***
//
//        //** Desenha no segundo painel
//        if (scene_.getViewingTransform().projectionMap().getCompIndexes()[0] == 2 ) {
//            drawSecondPanel(g, scene_);
//
//        }
//        //***
//
//
//    }
//
//
//    public void markPoints(Scene scene) {        // era static
//
//        //System.out.println("Entrou em markPoints");
//
//        RealVector bdryMin = RPNUMERICS.boundary().getMinimums();
//        RealVector bdryMax = RPNUMERICS.boundary().getMaximums();
//
//        double xResolution = new Double(RPNUMERICS.getConfiguration("Contour").getParam("x-resolution"));
//        double yResolution = new Double(RPNUMERICS.getConfiguration("Contour").getParam("y-resolution"));
//
//        int nu = (int) xResolution;
//        int nv = (int) yResolution;
//        int nw = nv;
//
//        double zMP = 0.;
//        double dw = 0.;
//
//        Coords2D dcCoordsTP = toDeviceCoords(scene, targetPoint);
//        double xTP = dcCoordsTP.getElement(1);
//        double yTP = dcCoordsTP.getElement(0);
//
//        for (int k = 0; k < targetPoint.getSize(); k++) {
//            if (targetPoint.getElement(k) == 0.) {
//                zerado = k;
//            }
//        }
//
//        Coords2D dcCoordsMP = toDeviceCoords(scene, pMarca);
//        double xMP = dcCoordsMP.getElement(1);
//        double yMP = dcCoordsMP.getElement(0);
//
//        Coords2D dcCoordsMPDCnt = toDeviceCoords(scene, pMarcaDC);
//        double xMPDCnt = dcCoordsMPDCnt.getElement(1);
//        double yMPDCnt = dcCoordsMPDCnt.getElement(0);
//
//        Coords2D dcCoordsCR = toDeviceCoords(scene, cornerRet);
//        double xCR = dcCoordsCR.getElement(1);
//        double yCR = dcCoordsCR.getElement(0);
//
//        if (zerado == 2) {
//            zMP = (RPnPhaseSpacePanel.myW_ / (bdryMax.getElement(2) - bdryMin.getElement(2))) * (pMarca.getElement(2) - bdryMin.getElement(2));
//        } else {
//            zMP = (RPnPhaseSpacePanel.myW_ / (bdryMax.getElement(1) - bdryMin.getElement(1))) * (pMarca.getElement(1) - bdryMin.getElement(1));
//        }
//
//        double z0 = 0., z1 = 0., zmin = 0., zmax = 0.;
//
//        double du = (1. * RPnPhaseSpacePanel.myH_) / nu;
//        double dv = (1. * RPnPhaseSpacePanel.myW_) / nv;
//
//        if (zerado == 2) {
//            dw = (bdryMax.getElement(2) - bdryMin.getElement(2)) / nw;
//        } else {
//            dw = (bdryMax.getElement(1) - bdryMin.getElement(1)) / nv;
//        }
//
//        double us = 0, vs = 0, zs = 0, ui = 0, vi = 0, zi = 0, u_s = 0, v_s = 0, z_s = 0, u_i = 0, v_i = 0, z_i = 0;
//
//        if ((ControlClick.ind % 2) == 0) {
//
//            vs = Math.min(yTP, yCR);               // Ja estao em coordenadas do dispositivo, mas sem ajuste no grid.
//            us = Math.min(xTP, xCR);
//            vi = Math.max(yTP, yCR);
//            ui = Math.max(xTP, xCR);
//
//            v_s = (int) (vs / dv) * dv;               //  Ajuste feito diretamente em pixel.
//            u_s = (int) (us / du) * du;
//            v_i = (int) (vi / dv + 1) * dv;
//            u_i = (int) (ui / du + 1) * du;
//
//            //----------------------------------------------------------------------------
//            if (scene.getViewingTransform().projectionMap().getCompIndexes()[0] == 1) {
//
//                CoordsArray wcCoordsTopRight = toWorldCoords(new Coords2D(v_i, u_s), scene);
//                CoordsArray wcCoordsDownLeft = toWorldCoords(new Coords2D(v_s, u_i), scene);
//
//                topRight.setElement(0, wcCoordsTopRight.getElement(0));
//                topRight.setElement(1, wcCoordsTopRight.getElement(1));
//                downLeft.setElement(0, wcCoordsDownLeft.getElement(0));
//                downLeft.setElement(1, wcCoordsDownLeft.getElement(1));
//
//            }
//            //------------------------------------------------------------------------------
//
//        }
//
//        //** Define as geometrias de resposta para interface.
//        int h = 5;
//        line1 = new Line2D.Double(yTP - h, xTP, yTP + h, xTP);
//        line2 = new Line2D.Double(yTP, xTP - h, yTP, xTP + h);
//        line3 = new Line2D.Double(yMP - h, xMP, yMP + h, xMP);
//        line4 = new Line2D.Double(yMP, xMP - h, yMP, xMP + h);
//        line3DC = new Line2D.Double(yMPDCnt - h, xMPDCnt, yMPDCnt + h, xMPDCnt);
//        line4DC = new Line2D.Double(yMPDCnt, xMPDCnt - h, yMPDCnt, xMPDCnt + h);
//        line5 = new Line2D.Double(yCR - h, xCR, yCR + h, xCR);
//        line6 = new Line2D.Double(yCR, xCR - h, yCR, xCR + h);
//        line7 = new Line2D.Double(zMP - h, xMP, zMP + h, xMP);
//        line8 = new Line2D.Double(zMP, xMP - h, zMP, xMP + h);
//        //***
//
//        //** Testa inclusão na área de selecão.
//        if ((ControlClick.ind % 2) == 0  &&  (UIController.instance().getState() instanceof AREASELECTION_CONFIG)) {
//
//            square1 = new Rectangle2D.Double(v_s, u_s, Math.abs(v_i - v_s), Math.abs(u_i - u_s));
//            testAreaContains(scene);
//
//        }
//        //***
//
//        if (zContido.size() != 0) {
//
//            Object obj0 = zContido.get(0);
//            String str0 = obj0.toString();
//            z0 = Double.valueOf(str0).doubleValue();
//            zmin = z0;
//
//            Object obj1 = zContido.get(0);
//            String str1 = obj1.toString();
//            z1 = Double.valueOf(str1).doubleValue();
//            zmax = z1;
//
//            for (int i = 0; i < zContido.size(); i++) {
//                obj0 = zContido.get(i);
//                str0 = obj0.toString();
//                z0 = Double.valueOf(str0).doubleValue();
//
//                if (z0 <= zmin) {
//                    zmin = z0;
//                }
//
//                obj1 = zContido.get(i);
//                str1 = obj1.toString();
//                z1 = Double.valueOf(str1).doubleValue();
//
//                if (z1 >= zmax) {
//                    zmax = z1;
//                }
//            }
//
//            zs = zmin;
//            zi = zmax;
//
//            z_s = (int) (zs / dw) * dw;
//            z_i = (int) (zi / dw + 1) * dw;
//
//            if (zerado == 2) {
//                z_s = (RPnPhaseSpacePanel.myW_ / (bdryMax.getElement(2) - bdryMin.getElement(2))) * (z_s - bdryMin.getElement(2));
//                z_i = (RPnPhaseSpacePanel.myW_ / (bdryMax.getElement(2) - bdryMin.getElement(2))) * (z_i - bdryMin.getElement(2));
//            } else {
//                z_s = (RPnPhaseSpacePanel.myW_ / (bdryMax.getElement(1) - bdryMin.getElement(1))) * (z_s - bdryMin.getElement(1));
//                z_i = (RPnPhaseSpacePanel.myW_ / (bdryMax.getElement(1) - bdryMin.getElement(1))) * (z_i - bdryMin.getElement(1));
//            }
//
//            if ((ControlClick.ind % 2) == 0  &&  (UIController.instance().getState() instanceof AREASELECTION_CONFIG)) {
//                square2 = new Rectangle2D.Double(z_s, u_s, Math.abs(z_i - z_s), Math.abs(u_i - u_s));
//
//            }
//
//
//        }
//
//
//    }
}