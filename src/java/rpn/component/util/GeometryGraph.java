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
public class GeometryGraph {   //*** Versão para 2-D

    static public Line2D line1;
    static public Line2D line2;
    static public Line2D line3;
    static public Line2D line4;
    static public Line2D line5;
    static public Line2D line6;
    static public Line2D line7;
    static public Line2D line8;
    static public Shape square1;
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


    public void paintComponent(Graphics g, Scene scene_) {

        Graphics2D graph = (Graphics2D) g;

        if (UIController.instance().getState() instanceof AREASELECTION_CONFIG) {
            g.setColor(Color.red);
            graph.draw(line1);
            graph.draw(line2);
        }
        
        g.setColor(Color.yellow);
        graph.draw(line3);
        graph.draw(line4);

        if ((ControlClick.ind % 2) == 0) {

            if (UIController.instance().getState() instanceof AREASELECTION_CONFIG) {
                g.setColor(Color.red);
                graph.draw(line5);
                graph.draw(line6);
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

            if (UIController.instance().getState() instanceof CLASSIFIERAGENT_CONFIG    &&
                RPnCurve.lista.get(GeometryUtil.closestCurve) instanceof HugoniotCurve) {

                int cont = ControlClick.xDevStr.size();  /////

                g.setColor(Color.white);

                for (int i = 0; i < cont; i++) {
                    double YCR = (Double) ControlClick.yDevStr.get(i);      //** Nao estao acompanhando o resize. Melhorar!!!
                    double XCR = (Double) ControlClick.xDevStr.get(i);
                    double YSeta = (Double) ControlClick.yDevSeta.get(i);
                    double XSeta = (Double) ControlClick.xDevSeta.get(i);

                    int s1 = (Integer) (GeometryUtil.tipo.get(i));

                    Font font = new Font("Verdana",Font.PLAIN,14);
                    g.setFont(font);
                    FontMetrics metrics = new FontMetrics(font) {
                              };
                    Rectangle2D bounds = metrics.getStringBounds(HugoniotSegGeom.s[s1].toString(), null);
                    int tamPix = (int) bounds.getWidth();
                    
                    //***** Talvez possa ficar melhor !!!!!!!!!!!!
		    int dx = 3;
		    double dy = dx*Math.abs((YCR-YSeta)/(XCR-XSeta));

                    if (XSeta < XCR && YSeta < YCR) {
                        g.drawString(HugoniotSegGeom.s[s1], (int) (YCR + 5), (int) (XCR + 5));
                        Line2D line_ = new Line2D.Double(YSeta + dy, XSeta + dx, YCR, XCR);
                        graph.draw(line_);
                    }
                    else if (YSeta > YCR && XSeta < XCR) {
                        g.drawString(HugoniotSegGeom.s[s1], (int) (YCR - 1.2 * tamPix), (int) (XCR + 5));
                        Line2D line_ = new Line2D.Double(YSeta - dy, XSeta + dx, YCR, XCR);
                        graph.draw(line_);
                    }
                    else if (YSeta > YCR && XSeta > XCR) {
                        g.drawString(HugoniotSegGeom.s[s1], (int) (YCR - 1.2 * tamPix), (int) (XCR + 5));
                        Line2D line_ = new Line2D.Double(YSeta - dy, XSeta - dx, YCR, XCR);
                        graph.draw(line_);
                    }
                    else if (YSeta < YCR && XSeta > XCR) {
                        g.drawString(HugoniotSegGeom.s[s1], (int) (YCR + 5), (int) (XCR + 5));
                        Line2D line_ = new Line2D.Double(YSeta + dy, XSeta - dx, YCR, XCR);
                        graph.draw(line_);
                    }
                    //**************************************************

                }

            }
            
        }
        
    }


    public void markPoints(RealVector targetPoint, RealVector pMarca, Scene scene) {

        RealVector bdryMin = RPNUMERICS.boundary().getMinimums();
        RealVector bdryMax = RPNUMERICS.boundary().getMaximums();

        RPnPhaseSpacePanel panel = new RPnPhaseSpacePanel(scene);
        ViewingTransform transf = panel.scene().getViewingTransform();

        CoordsArray wcCoordsTP = new CoordsArray(GeometryUtil.targetPoint);
        Coords2D dcCoordsTP = new Coords2D();
        transf.viewPlaneTransform(wcCoordsTP, dcCoordsTP);
        double xTP = dcCoordsTP.getElement(1);
        double yTP = dcCoordsTP.getElement(0);

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

        int nu = 151;
        int nv = 151;

        double du = 1. * RPnPhaseSpacePanel.myH_ / nu;
        double dv = 1. * RPnPhaseSpacePanel.myW_ / nv;

        double us = 0, vs = 0, zs = 0, ui = 0, vi = 0, zi = 0, u_s = 0, v_s = 0, z_s = 0, u_i = 0, v_i = 0, z_i = 0;

        if ((ControlClick.ind % 2) == 0) {

            vs = Math.min(yTP, yCR);
            us = Math.min(xTP, xCR);
            vi = Math.max(yTP, yCR);
            ui = Math.max(xTP, xCR);

            v_s = (int) (vs / dv) * dv;
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
        //***

        //** Testa inclusão na área de selecão.
        if ((ControlClick.ind % 2) == 0) {

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
                        }

                    }

                }

            }

        }


        //***

            if (GeometryUtil.indContido.size() != 0) {

                //grava();

                //System.out.println("Tamanho do indContido indo pro Remove : " + GeometryUtil.indContido.size());

                //RPnCurve.lista.get(GeometryUtil.closestCurve).remove((SegmentedCurve) RPnCurve.lista.get(GeometryUtil.closestCurve), GeometryUtil.indContido);

            }

        GeometryUtil.indContido.clear();   //////
        
    }

}
