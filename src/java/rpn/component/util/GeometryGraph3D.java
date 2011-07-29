/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rpn.component.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
//import java.util.logging.Level;
//import java.util.logging.Logger;
import rpn.RPnPhaseSpacePanel;
import rpn.component.HugoniotSegGeom;
import rpn.controller.ui.AREASELECTION_CONFIG;
import rpn.controller.ui.UIController;
import rpnumerics.HugoniotCurve;
import rpnumerics.Orbit;
import rpnumerics.RPNUMERICS;
import rpnumerics.RPnCurve;
import rpnumerics.SegmentedCurve;
import wave.multid.Coords2D;
import wave.multid.CoordsArray;
import wave.multid.view.Scene;
import wave.multid.view.ViewingTransform;
import wave.util.RealVector;
import wave.util.RealSegment;

/**
 *
 * @author moreira
 */
public class GeometryGraph3D {

    static public Line2D line1;
    static public Line2D line2;
    static public Line2D line3;
    static public Line2D line4;
    static public Line2D line5;
    static public Line2D line6;
    static public Line2D line7;
    static public Line2D line8;
    static public Shape square1;
    static public Shape square2;
    static public int zerado = 0;
    static public List<Rectangle2D.Double> listaRet = new ArrayList();  ////////
    

    public void grava() {
        try {
            //FileWriter gravador = new FileWriter("/impa/home/g/moreira/ListaDePontos/ListaDePontos.txt");
            FileWriter gravador = new FileWriter("/home/moreira/ListaDePontos/ListaDePontos.txt");
            BufferedWriter saida = new BufferedWriter(gravador);
            saida.write("Curva mais proxima: ");
            saida.write(" ");
            Object obj = GeometryUtil.closestCurve;
            String str = obj.toString();
            saida.write(str);
            saida.write(" ");
            saida.flush();
            saida.write("\n\n");
            saida.write("Pontos da curva:\n");
            for (int i = 0; i < GeometryUtil.indContido.size(); i++) {
                saida.write(GeometryUtil.indContido.get(i).toString());
                saida.write(" ");
                saida.flush();
                saida.write("\n");
            }
            saida.close();

        } catch (IOException e) {
            System.out.println("Não deu para escrever o arquivo");
        }
    }

    public void paintComponent(Graphics g, Scene scene_) {               // era static

        /*
         * line1 e line2 = marcador da posição do primeiro click; plot apenas se acionada a seleção de área.
         * line3 e line4 = marcador sobre o ponto/segmento mais próximo de uma curva (encontrado no painel onde foi dado o click).
         * line5 e line6 = marcador da posição do segundo click; plot apenas se acionada a seleção de área.
         * line7 e line8 = marcador sobre o ponto/segmento mais próximo de uma curva (encontrado no painel onde não foi dado o click).
         * square1 e square2 = retângulos usados para área de seleção.
         */

        Color cor12 = null, cor34 = null, cor56 = null, cor78 = null, corSquare = null, corString = null;

        if (RPnPhaseSpacePanel.DEFAULT_BACKGROUND_COLOR == Color.white) {
            cor12 = Color.red;
            cor34 = Color.red;         //*** red se background = white ; yellow se background = black
            cor56 = Color.red;
            cor78 = Color.red;         //*** red se background = white ; yellow se background = black
            corSquare = Color.red;
            corString = Color.black;   //*** black se background = white ; white se background = black
        }

        if (RPnPhaseSpacePanel.DEFAULT_BACKGROUND_COLOR == Color.black) {
            cor12 = Color.red;
            cor34 = Color.yellow;         //*** red se background = white ; yellow se background = black
            cor56 = Color.red;
            cor78 = Color.yellow;         //*** red se background = white ; yellow se background = black
            corSquare = Color.red;
            corString = Color.white;      //*** black se background = white ; white se background = black
        }

        
        Graphics2D graph = (Graphics2D) g;

        //** Desenha no primeiro painel
        if (scene_.getViewingTransform().projectionMap().getCompIndexes()[0] == 1) {

            if (zerado == 2) {

                if (UIController.instance_.getState() instanceof AREASELECTION_CONFIG) {
                    g.setColor(cor12);
                    graph.draw(line1);
                    graph.draw(line2);
                }
                
                g.setColor(cor34);
                graph.draw(line3);
                graph.draw(line4);

                
                if ((ControlClick.ind % 2) == 0) {

                    if (UIController.instance_.getState() instanceof AREASELECTION_CONFIG) {
                        g.setColor(cor56);
                        graph.draw(line5);
                        graph.draw(line6);
                        g.setColor(corSquare);
                        graph.draw(square1);
                    }
                    
                    ViewingTransform transf = scene_.getViewingTransform();
                    CoordsArray wcCoordsCR = new CoordsArray(GeometryUtil.cornerRet);
                    Coords2D dcCoordsCR = new Coords2D();
                    transf.viewPlaneTransform(wcCoordsCR, dcCoordsCR);
                    double xCR = dcCoordsCR.getElement(1);
                    double yCR = dcCoordsCR.getElement(0);

                    CoordsArray wcCoordsMP = new CoordsArray(GeometryUtil.pMarca);
                    Coords2D dcCoordsMP = new Coords2D();
                    transf.viewPlaneTransform(wcCoordsMP, dcCoordsMP);
                    double xMP = dcCoordsMP.getElement(1);
                    double yMP = dcCoordsMP.getElement(0);



                    //*** Para o botao Classify
                    if ((UIController.instance_.getState() instanceof VELOCITYAGENT_CONFIG)  ||
                        (UIController.instance_.getState() instanceof CLASSIFIERAGENT_CONFIG  &&
                        RPnCurve.lista.get(GeometryUtil.closestCurve) instanceof HugoniotCurve)) {

                        int cont = ControlClick.xDevStr.size();

                        g.setColor(corString);
                        
                        for (int i = 0; i < cont; i++) {

                            double YCR = Double.valueOf(ControlClick.yStr.get(i).toString()).doubleValue();
                            double XCR = Double.valueOf(ControlClick.xStr.get(i).toString()).doubleValue();

                            RealVector newRV = new RealVector(3);
                            newRV.setElement(0, YCR);
                            newRV.setElement(1, XCR);
                            newRV.setElement(2, 0.);

                            CoordsArray newCA = new CoordsArray(newRV);
                            Coords2D newDC = new Coords2D();
                            transf.viewPlaneTransform(newCA, newDC);

                            // ***
                            double YSeta = Double.valueOf(ControlClick.ySeta.get(i).toString()).doubleValue();
                            double XSeta = Double.valueOf(ControlClick.xSeta.get(i).toString()).doubleValue();

                            RealVector setaRV = new RealVector(3);
                            setaRV.setElement(0, YSeta);
                            setaRV.setElement(1, XSeta);
                            setaRV.setElement(2, 0.);

                            CoordsArray setaCA = new CoordsArray(setaRV);
                            Coords2D setaDC = new Coords2D();
                            transf.viewPlaneTransform(setaCA, setaDC);
                            // ***

                            int s1 = (Integer) (GeometryUtil.tipo.get(i));
                            Font font = new Font("Verdana",Font.PLAIN,16);
                            g.setFont(font);
                            FontMetrics metrics = new FontMetrics(font) {
                              };
                            Rectangle2D bounds = metrics.getStringBounds(HugoniotSegGeom.s[s1].toString(), null);
                            int tamPix = (int) bounds.getWidth();

                            //***** Ficou melhor !!!!!!!!!!!!
                            double raio = 7.;
                            double Dx = Math.abs(newDC.getX()-setaDC.getX());
                            double Dy = Math.abs(newDC.getY()-setaDC.getY());
                            double dist = Math.sqrt(Math.pow(Dy,2) + Math.pow(Dx,2));

                            double dx = (raio*Dx)/dist;
                            double dy = (raio*Dy)/dist;

                            if (setaDC.getX()<newDC.getX() && setaDC.getY()<newDC.getY()) {
                                g.drawString(HugoniotSegGeom.s[s1], (int) (newDC.getX()+5), (int) (newDC.getY()+5));
                                Line2D line_ = new Line2D.Double(setaDC.getX()+dx, setaDC.getY()+dy, newDC.getX(), newDC.getY());
                                graph.draw(line_);
                            }
                            else if (setaDC.getX()>newDC.getX() && setaDC.getY()<newDC.getY()){
                                g.drawString(HugoniotSegGeom.s[s1], (int) (newDC.getX()-(tamPix+2)), (int) (newDC.getY()+5));
                                Line2D line_ = new Line2D.Double(setaDC.getX()-dx, setaDC.getY()+dy, newDC.getX(), newDC.getY());
                                graph.draw(line_);
                            }
                            else if (setaDC.getX()>newDC.getX() && setaDC.getY()>newDC.getY()){
                                g.drawString(HugoniotSegGeom.s[s1], (int) (newDC.getX()-(tamPix+2)), (int) (newDC.getY()+5));
                                Line2D line_ = new Line2D.Double(setaDC.getX()-dx, setaDC.getY()-dy, newDC.getX(), newDC.getY());
                                graph.draw(line_);
                            }
                            else if (setaDC.getX()<newDC.getX() && setaDC.getY()>newDC.getY()){
                                g.drawString(HugoniotSegGeom.s[s1], (int) (newDC.getX()+5), (int) (newDC.getY()+5));
                                Line2D line_ = new Line2D.Double(setaDC.getX()+dx, setaDC.getY()-dy, newDC.getX(), newDC.getY());
                                graph.draw(line_);
                            }
                            //**************************************************
                        }
                    }
                    //*** Fim botao Classify



                    //*** Para o botao Velocity
                    if ((UIController.instance_.getState() instanceof CLASSIFIERAGENT_CONFIG)  ||
                        (UIController.instance_.getState() instanceof VELOCITYAGENT_CONFIG  &&
                        ((RPnCurve.lista.get(GeometryUtil.closestCurve) instanceof Orbit)  ||
                        (RPnCurve.lista.get(GeometryUtil.closestCurve) instanceof HugoniotCurve)))) {

                        int cont = ControlClick.xDevVel.size();

                        g.setColor(corString);

                        for (int i = 0; i < cont; i++) {

                            double YCR = Double.valueOf(ControlClick.yVel.get(i).toString()).doubleValue();
                            double XCR = Double.valueOf(ControlClick.xVel.get(i).toString()).doubleValue();

                            RealVector newRV = new RealVector(3);
                            newRV.setElement(0, YCR);
                            newRV.setElement(1, XCR);
                            newRV.setElement(2, 0.);

                            CoordsArray newCA = new CoordsArray(newRV);
                            Coords2D newDC = new Coords2D();
                            transf.viewPlaneTransform(newCA, newDC);

                            // ***
                            double YSeta = Double.valueOf(ControlClick.ySetaVel.get(i).toString()).doubleValue();
                            double XSeta = Double.valueOf(ControlClick.xSetaVel.get(i).toString()).doubleValue();

                            RealVector setaRV = new RealVector(3);
                            setaRV.setElement(0, YSeta);
                            setaRV.setElement(1, XSeta);
                            setaRV.setElement(2, 0.);

                            CoordsArray setaCA = new CoordsArray(setaRV);
                            Coords2D setaDC = new Coords2D();
                            transf.viewPlaneTransform(setaCA, setaDC);
                            // ***

                            double v1 = (Double) (GeometryUtil.vel.get(i));
                            Font font = new Font("Verdana", Font.PLAIN, 10);
                            g.setFont(font);
                            FontMetrics metrics = new FontMetrics(font) {
                            };
                            String exp = String.format("%.4e", v1);
                            Rectangle2D bounds = metrics.getStringBounds(exp, null);
                            int tamPix = (int) bounds.getWidth();

                            //***** Ficou melhor !!!!!!!!!!!!
                            double raio = 7.;
                            double Dx = Math.abs(newDC.getX() - setaDC.getX());
                            double Dy = Math.abs(newDC.getY() - setaDC.getY());
                            double dist = Math.sqrt(Math.pow(Dy, 2) + Math.pow(Dx, 2));

                            double dx = (raio * Dx) / dist;
                            double dy = (raio * Dy) / dist;

                            if (setaDC.getX() < newDC.getX() && setaDC.getY() < newDC.getY()) {
                                g.drawString(exp, (int) (newDC.getX() + 5), (int) (newDC.getY() + 5));
                                Line2D line_ = new Line2D.Double(setaDC.getX() + dx, setaDC.getY() + dy, newDC.getX(), newDC.getY());
                                graph.draw(line_);
                            } else if (setaDC.getX() > newDC.getX() && setaDC.getY() < newDC.getY()) {
                                g.drawString(exp, (int) (newDC.getX() - (tamPix + 2)), (int) (newDC.getY() + 5));
                                Line2D line_ = new Line2D.Double(setaDC.getX() - dx, setaDC.getY() + dy, newDC.getX(), newDC.getY());
                                graph.draw(line_);
                            } else if (setaDC.getX() > newDC.getX() && setaDC.getY() > newDC.getY()) {
                                g.drawString(exp, (int) (newDC.getX() - (tamPix + 2)), (int) (newDC.getY() + 5));
                                Line2D line_ = new Line2D.Double(setaDC.getX() - dx, setaDC.getY() - dy, newDC.getX(), newDC.getY());
                                graph.draw(line_);
                            } else if (setaDC.getX() < newDC.getX() && setaDC.getY() > newDC.getY()) {
                                g.drawString(exp, (int) (newDC.getX() + 5), (int) (newDC.getY() + 5));
                                Line2D line_ = new Line2D.Double(setaDC.getX() + dx, setaDC.getY() - dy, newDC.getX(), newDC.getY());
                                graph.draw(line_);
                            }
                            //**************************************************

                        }
                    }
                    //*** Fim botao Velocity

                }

            } else {
                g.setColor(cor78);
                graph.draw(line7);
                graph.draw(line8);

                if (((ControlClick.ind % 2) == 0)  &&  (UIController.instance_.getState() instanceof AREASELECTION_CONFIG)) {
                    g.setColor(corSquare);
                    graph.draw(square2);
                }

            }

        }
        //***

        //** Desenha no segundo painel
        if (scene_.getViewingTransform().projectionMap().getCompIndexes()[0] == 2 && RPnCurve.lista.size() > 0) {

            if (zerado == 2) {

                g.setColor(cor78);
                graph.draw(line7);
                graph.draw(line8);
                
                if (GeometryUtil.zContido.size() != 0) {
                    if (UIController.instance_.getState() instanceof AREASELECTION_CONFIG) {
                        g.setColor(corSquare);
                        graph.draw(square2);
                    }
                }

            } else {

                if (UIController.instance_.getState() instanceof AREASELECTION_CONFIG) {
                    g.setColor(cor12);
                    graph.draw(line1);
                    graph.draw(line2);
                }

                g.setColor(cor34);
                graph.draw(line3);
                graph.draw(line4);

                //** Para string no painel 2
                    ViewingTransform transf = scene_.getViewingTransform();
                    CoordsArray wcCoordsCR = new CoordsArray(GeometryUtil.cornerRet);
                    Coords2D dcCoordsCR = new Coords2D();
                    transf.viewPlaneTransform(wcCoordsCR, dcCoordsCR);
                    double xCR = dcCoordsCR.getElement(1);
                    double yCR = dcCoordsCR.getElement(0);

                    CoordsArray wcCoordsMP = new CoordsArray(GeometryUtil.pMarca);
                    Coords2D dcCoordsMP = new Coords2D();
                    transf.viewPlaneTransform(wcCoordsMP, dcCoordsMP);
                    double xMP = dcCoordsMP.getElement(1);
                    double yMP = dcCoordsMP.getElement(0);

                    if (((ControlClick.ind % 2) == 0)  &&  (UIController.instance_.getState() instanceof CLASSIFIERAGENT_CONFIG)) {
                        //g.setColor(corString);

                        //*** Analisar o que fazer caso o click seja no painel 2.
                        //*** Precisa imprimir as strings lá?
                        //*** Se sim, adaptar as linhas abaixo.

                        //String s1 = Integer.toString(GeometryUtil.tipo);
                        //g.drawString((s1 + HugoniotSegGeom.s[GeometryUtil.tipo]), (int) (yCR), (int) (xCR));
                        //Line2D line_ = new Line2D.Double(yMP, xMP, yCR, xCR);
                        //graph.draw(line_);
                    }
                //****************************


                if (((ControlClick.ind % 2) == 0)  &&  (UIController.instance_.getState() instanceof AREASELECTION_CONFIG)) {
                    g.setColor(cor56);
                    graph.draw(line5);
                    graph.draw(line6);
                    g.setColor(corSquare);
                    graph.draw(square1);
                }

            }

        }
        //***


    }

    
    public void markPoints(RealVector targetPoint, RealVector pMarca, Scene scene) {        // era static

        //System.out.println("Entrou em markPoints");

        RealVector bdryMin = RPNUMERICS.boundary().getMinimums();
        RealVector bdryMax = RPNUMERICS.boundary().getMaximums();

        double zMP = 0.;
        double dw = 0.;

        RPnPhaseSpacePanel panel = new RPnPhaseSpacePanel(scene);
        ViewingTransform transf = panel.scene().getViewingTransform();

        CoordsArray wcCoordsTP = new CoordsArray(GeometryUtil.targetPoint);
        Coords2D dcCoordsTP = new Coords2D();
        transf.viewPlaneTransform(wcCoordsTP, dcCoordsTP);
        double xTP = dcCoordsTP.getElement(1);
        double yTP = dcCoordsTP.getElement(0);

        for (int k = 0; k < GeometryUtil.targetPoint.getSize(); k++) {
            if (GeometryUtil.targetPoint.getElement(k) == 0.) {
                zerado = k;
            }
        }

        CoordsArray wcCoordsMP = new CoordsArray(GeometryUtil.pMarca);
        Coords2D dcCoordsMP = new Coords2D();
        transf.viewPlaneTransform(wcCoordsMP, dcCoordsMP);
        double xMP = dcCoordsMP.getElement(1);
        double yMP = dcCoordsMP.getElement(0);

        CoordsArray wcCoordsCR = new CoordsArray(GeometryUtil.cornerRet);
        Coords2D dcCoordsCR = new Coords2D();
        transf.viewPlaneTransform(wcCoordsCR, dcCoordsCR);
        double xCR = dcCoordsCR.getElement(1);
        double yCR = dcCoordsCR.getElement(0);
        
        if (zerado == 2) {
            zMP = (RPnPhaseSpacePanel.myW_ / (bdryMax.getElement(2) - bdryMin.getElement(2))) * (GeometryUtil.pMarca.getElement(2) - bdryMin.getElement(2));
        } else {
            zMP = (RPnPhaseSpacePanel.myW_ / (bdryMax.getElement(1) - bdryMin.getElement(1))) * (GeometryUtil.pMarca.getElement(1) - bdryMin.getElement(1));
        }

        double z0 = 0., z1 = 0., zmin = 0., zmax = 0.;

        int nu = 151;
        int nv = 151;
        int nw = 151;

        double du = 1. * RPnPhaseSpacePanel.myH_ / nu;
        double dv = 1. * RPnPhaseSpacePanel.myW_ / nv;

        if (zerado == 2) {
            dw = (bdryMax.getElement(2) - bdryMin.getElement(2)) / nw;
        } else {
            dw = (bdryMax.getElement(1) - bdryMin.getElement(1)) / nv;
        }

        double us = 0, vs = 0, zs = 0, ui = 0, vi = 0, zi = 0, u_s = 0, v_s = 0, z_s = 0, u_i = 0, v_i = 0, z_i = 0;

        if ((ControlClick.ind % 2) == 0) {

            vs = Math.min(yTP, yCR);               // Ja estao em coordenadas do dispositivo, mas sem ajuste no grid.
            us = Math.min(xTP, xCR);
            vi = Math.max(yTP, yCR);
            ui = Math.max(xTP, xCR);

            v_s = (int) (vs / dv) * dv;               //  Ajuste feito diretamente em pixel.
            u_s = (int) (us / du) * du;
            v_i = (int) (vi / dv + 1) * dv;
            u_i = (int) (ui / du + 1) * du;

        }

        //** Define as geometrias de resposta para interface.
        line1 = new Line2D.Double(yTP - 3, xTP, yTP + 3, xTP);
        line2 = new Line2D.Double(yTP, xTP - 3, yTP, xTP + 3);
        line3 = new Line2D.Double(yMP - 3, xMP, yMP + 3, xMP);
        line4 = new Line2D.Double(yMP, xMP - 3, yMP, xMP + 3);
        line5 = new Line2D.Double(yCR - 3, xCR, yCR + 3, xCR);
        line6 = new Line2D.Double(yCR, xCR - 3, yCR, xCR + 3);
        line7 = new Line2D.Double(zMP - 3, xMP, zMP + 3, xMP);
        line8 = new Line2D.Double(zMP, xMP - 3, zMP, xMP + 3);
        //***

        //** Testa inclusão na área de selecão.
        if ((ControlClick.ind % 2) == 0  &&  (UIController.instance_.getState() instanceof AREASELECTION_CONFIG)) {

            square1 = new Rectangle2D.Double(v_s, u_s, Math.abs(v_i - v_s), Math.abs(u_i - u_s));

            if (square1.contains(yTP, xTP)) {

                if (RPnCurve.lista.size() > 0  &&  RPnCurve.lista.get(GeometryUtil.closestCurve) instanceof Orbit) {

                    Orbit curve = (Orbit) RPnCurve.lista.get(GeometryUtil.closestCurve);


                    for (int i = 0; i < curve.getPoints().length; i++) {

                        CoordsArray wcCoordsCurve = new CoordsArray(curve.getPoints()[i]);
                        Coords2D dcCoordsCurve = new Coords2D();
                        transf.viewPlaneTransform(wcCoordsCurve, dcCoordsCurve);

                        double xCurve = dcCoordsCurve.getElement(0);
                        double yCurve = dcCoordsCurve.getElement(1);

                        if (square1.contains(xCurve, yCurve)) {
                            GeometryUtil.indContido.add(i);
                            if (zerado == 2) {
                                GeometryUtil.zContido.add(curve.getPoints()[i].getElement(2));
                            } else {
                                GeometryUtil.zContido.add(curve.getPoints()[i].getElement(1));
                            }

                        }

                    }

                }


                if (RPnCurve.lista.size() > 0  &&  RPnCurve.lista.get(GeometryUtil.closestCurve) instanceof SegmentedCurve) {

                    SegmentedCurve curve = (SegmentedCurve) RPnCurve.lista.get(GeometryUtil.closestCurve);

                    for (int i = 0; i < curve.segments().size(); i++) {

                        CoordsArray wcCoordsCurve = new CoordsArray(((RealSegment) (curve.segments()).get(i)).p1());
                        Coords2D dcCoordsCurve = new Coords2D();
                        transf.viewPlaneTransform(wcCoordsCurve, dcCoordsCurve);

                        double xCurve = dcCoordsCurve.getElement(0);
                        double yCurve = dcCoordsCurve.getElement(1);

                        if (square1.contains(xCurve, yCurve)) {
                            GeometryUtil.indContido.add(i);
                            if (zerado == 2) {
                                GeometryUtil.zContido.add(((RealSegment) (curve.segments()).get(i)).p1().getElement(2));
                            } else {
                                GeometryUtil.zContido.add(((RealSegment) (curve.segments()).get(i)).p1().getElement(1));
                            }

                        }

                    }

                }

            }

        }


        //***

        if (GeometryUtil.zContido.size() != 0) {

            if (GeometryUtil.indContido.size() != 0) {

                //grava();

                //System.out.println("Tamanho do indContido indo pro Remove : " +GeometryUtil.indContido.size());

                //RPnCurve.lista.get(GeometryUtil.closestCurve).remove((SegmentedCurve) RPnCurve.lista.get(GeometryUtil.closestCurve), GeometryUtil.indContido);
                
            }

            Object obj0 = GeometryUtil.zContido.get(0);
            String str0 = obj0.toString();
            z0 = Double.valueOf(str0).doubleValue();
            zmin = z0;

            Object obj1 = GeometryUtil.zContido.get(0);
            String str1 = obj1.toString();
            z1 = Double.valueOf(str1).doubleValue();
            zmax = z1;

            for (int i = 0; i < GeometryUtil.zContido.size(); i++) {
                obj0 = GeometryUtil.zContido.get(i);
                str0 = obj0.toString();
                z0 = Double.valueOf(str0).doubleValue();

                if (z0 <= zmin) {
                    zmin = z0;
                }

                obj1 = GeometryUtil.zContido.get(i);
                str1 = obj1.toString();
                z1 = -Double.valueOf(str1).doubleValue();

                if (z1 <= zmax) {
                    zmax = z1;
                }
            }

            zs = zmin;
            zi = -zmax;

            z_s = (int) (zs / dw) * dw;
            z_i = (int) (zi / dw + 1) * dw;

            if (zerado == 2) {
                z_s = (RPnPhaseSpacePanel.myW_ / (bdryMax.getElement(2) - bdryMin.getElement(2))) * (z_s - bdryMin.getElement(2));
                z_i = (RPnPhaseSpacePanel.myW_ / (bdryMax.getElement(2) - bdryMin.getElement(2))) * (z_i - bdryMin.getElement(2));
            } else {
                z_s = (RPnPhaseSpacePanel.myW_ / (bdryMax.getElement(1) - bdryMin.getElement(1))) * (z_s - bdryMin.getElement(1));
                z_i = (RPnPhaseSpacePanel.myW_ / (bdryMax.getElement(1) - bdryMin.getElement(1))) * (z_i - bdryMin.getElement(1));
            }

            if ((ControlClick.ind % 2) == 0  &&  (UIController.instance_.getState() instanceof AREASELECTION_CONFIG)) {
                square2 = new Rectangle2D.Double(z_s, u_s, Math.abs(z_i - z_s), Math.abs(u_i - u_s));
            }

        }

        GeometryUtil.indContido.clear();   //////

    }
}
