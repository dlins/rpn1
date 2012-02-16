/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rpn.component.util;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import org.apache.batik.ext.awt.geom.Polygon2D;
import rpn.RPnPhaseSpacePanel;
import rpn.controller.ui.AREASELECTION_CONFIG;
import rpn.controller.ui.BIFURCATIONREFINE_CONFIG;
import rpn.controller.ui.UIController;
import rpn.parser.RPnDataModule;
import rpnumerics.Orbit;
import rpnumerics.RPNUMERICS;
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
public class GeometryGraph extends GeometryGraphND {   //*** Versão para 2-D

    private Polygon triangle;

    private double V2x =0.;
    private double V2y =0.;
    private double V3x =0.;
    private double V3y =0.;

    private final double TAN60 = Math.tan(Math.PI/3.);


    public Polygon defBordo(Scene scene_) {

        RealVector V1 = new RealVector(2);
        V1.setElement(0, 0.);        V1.setElement(1, 0.);
        RealVector V2 = new RealVector(2);
        V2.setElement(0, 0.);        V2.setElement(1, 1.);
        RealVector V3 = new RealVector(2);
        V3.setElement(0, 1.);        V3.setElement(1, 0.);

        Coords2D dcCoordsV1 = new Coords2D();
        dcCoordsV1 = toDeviceCoords(scene_, V1);
        double xV1 = dcCoordsV1.getElement(0);
        double yV1 = dcCoordsV1.getElement(1);

        Coords2D dcCoordsV2 = new Coords2D();
        dcCoordsV2 = toDeviceCoords(scene_, V2);
        double xV2 = dcCoordsV2.getElement(0);
        double yV2 = dcCoordsV2.getElement(1);
        V2x = xV2;      V2y = yV2;

        Coords2D dcCoordsV3 = new Coords2D();
        dcCoordsV3 = toDeviceCoords(scene_, V3);
        double xV3 = dcCoordsV3.getElement(0);
        double yV3 = dcCoordsV3.getElement(1);
        V3x = xV3;      V3y = yV3;

        triangle = new Polygon();
        triangle.addPoint((int)xV1, (int)yV1);
        triangle.addPoint((int)xV2, (int)yV2);
        triangle.addPoint((int)xV3, (int)yV3);

        return triangle;
    }


    public void drawFirstPanel(Graphics g, Scene scene_) {

        if (mostraGrid != 0){
            drawGrid(g, scene_);
        }

        Graphics2D graph = (Graphics2D) g;
        
        if (UIController.instance().getState() instanceof AREASELECTION_CONFIG) {
            g.setColor(cor12);
            graph.draw(line1);
            graph.draw(line2);
        }

        g.setColor(cor34);
        graph.draw(line3);
        graph.draw(line4);
        graph.draw(line3DC);
        graph.draw(line4DC);


        if ((ControlClick.ind % 2) == 0) {

            if (UIController.instance().getState() instanceof AREASELECTION_CONFIG) {
                g.setColor(cor56);
                graph.draw(line5);
                graph.draw(line6);
                g.setColor(corSquare);
                //graph.draw(poly);   //*** funcionamento original

                graph.draw(square1);
            }


            //*** Para os botoes Classify e Velocity
            if ((UIController.instance().getState() instanceof CLASSIFIERAGENT_CONFIG)
                    || (UIController.instance().getState() instanceof VELOCITYAGENT_CONFIG)) {

                defineClassifiers(g, scene_);
                defineVelocities(g, scene_);

            }
            //*** Fim dos botoes Classify e Velocity


        }


    }


    public void paintComponent(Graphics g, Scene scene_) {

        changeColor();
        drawFirstPanel(g, scene_);

    }


    public void markPoints(Scene scene) {        //*** era do GeometryGraph3D, vou usar para testar mapeamento do square

        //double xResolution = new Double(RPNUMERICS.getConfiguration("Contour").getParam("x-resolution"));
        //double yResolution = new Double(RPNUMERICS.getConfiguration("Contour").getParam("y-resolution"));

        //int[] resolution = RPnDataModule.processResolution(RPNUMERICS.getParamValue("hugoniotcurve", "resolution"));
        int[] resolution = {1, 1};

        if (RPNUMERICS.listResolution.size()==1) GeometryUtil.closestCurve=0;
        if (RPNUMERICS.listResolution.size()>0) resolution = (int[]) RPNUMERICS.listResolution.get(GeometryUtil.closestCurve);
        //if (scene.geometries().hasNext()) resolution = (int[]) RPNUMERICS.listResolution.get(GeometryUtil.closestCurve);

        int xResolution = resolution[0];
        int yResolution = resolution[1];

        int nu = (int) xResolution;
        int nv = (int) yResolution;

        Coords2D dcCoordsTP = toDeviceCoords(scene, targetPoint);
        double xTP = dcCoordsTP.getElement(1);
        double yTP = dcCoordsTP.getElement(0);
        
        Coords2D dcCoordsMP = toDeviceCoords(scene, pMarca);
        double xMP = dcCoordsMP.getElement(1);
        double yMP = dcCoordsMP.getElement(0);

        Coords2D dcCoordsMPDCnt = toDeviceCoords(scene, pMarcaDC);
        double xMPDCnt = dcCoordsMPDCnt.getElement(1);
        double yMPDCnt = dcCoordsMPDCnt.getElement(0);

        Coords2D dcCoordsCR = toDeviceCoords(scene, cornerRet);
        double xCR = dcCoordsCR.getElement(1);
        double yCR = dcCoordsCR.getElement(0);

        double du = (1. * RPnPhaseSpacePanel.myH_) / nu;
        double dv = (1. * RPnPhaseSpacePanel.myW_) / nv;

        double us = 0, vs = 0, ui = 0, vi = 0, u_s = 0, v_s = 0, u_i = 0, v_i = 0;


        //** Define as geometrias de resposta para interface.
        int h = 5;
        line1 = new Line2D.Double(yTP - h, xTP, yTP + h, xTP);
        line2 = new Line2D.Double(yTP, xTP - h, yTP, xTP + h);
        line3 = new Line2D.Double(yMP - h, xMP, yMP + h, xMP);
        line4 = new Line2D.Double(yMP, xMP - h, yMP, xMP + h);
        line3DC = new Line2D.Double(yMPDCnt - h, xMPDCnt, yMPDCnt + h, xMPDCnt);
        line4DC = new Line2D.Double(yMPDCnt, xMPDCnt - h, yMPDCnt, xMPDCnt + h);
        line5 = new Line2D.Double(yCR - h, xCR, yCR + h, xCR);
        line6 = new Line2D.Double(yCR, xCR - h, yCR, xCR + h);
        //***

        //** Testa inclusão na área de selecão.
        if ((ControlClick.ind % 2) == 0 && (UIController.instance().getState() instanceof AREASELECTION_CONFIG)) {

            vs = Math.min(yTP, yCR);                      // Ja estao em coordenadas do dispositivo, mas sem ajuste no grid.
            us = Math.min(xTP, xCR);
            vi = Math.max(yTP, yCR);
            ui = Math.max(xTP, xCR);
            
            if (mapToEqui == 0) {
                v_s = (int) (vs / dv) * dv;               //  Ajuste feito diretamente em pixel.
                u_s = (int) (us / du) * du;
                v_i = (int) (vi / dv + 1) * dv;
                u_i = (int) (ui / du + 1) * du;
            }

            if (mapToEqui == 1) {
                //*** !!!! Nao tem na versao original !!!!
                vs = vs + 0.57735 * us - RPnPhaseSpacePanel.myW_ / 2 - 0.57735 * 0.13397 * RPnPhaseSpacePanel.myH_;
                vi = vi + 0.57735 * us - RPnPhaseSpacePanel.myW_ / 2 - 0.57735 * 0.13397 * RPnPhaseSpacePanel.myH_;
                us = 1.1547 * us - 1.1547 * 0.13397 * RPnPhaseSpacePanel.myH_;
                ui = 1.1547 * ui - 1.1547 * 0.13397 * RPnPhaseSpacePanel.myH_;
                //***

                v_s = (int) (vs / dv) * dv;               //  Ajuste feito diretamente em pixel.
                u_s = (int) (us / du) * du;
                v_i = (int) (vi / dv + 1) * dv;
                u_i = (int) (ui / du + 1) * du;
            }

            square1 = mapShape(new Rectangle2D.Double(v_s, u_s, Math.abs(v_i - v_s), Math.abs(u_i - u_s)), scene);
            indContido.clear();
            testAreaContains(scene);

        }
        //***


    }

    public Shape mapShape(Shape shape, Scene scene) {         //*** ESTA CORRETO, MAS AINDA NAO TAO PERFEITO QUANTO O ANTIGO METODO DE DESENHAR A AREA

        defBordo(scene);

        Polygon poly = new Polygon();

        double v_s = shape.getBounds2D().getMinX();
        double u_s = shape.getBounds2D().getMinY();
        double v_i = shape.getBounds2D().getMaxX();
        double u_i = shape.getBounds2D().getMaxY();

        double v1 = v_i;
        double u1 = u_s;
        double v2 = v_s;
        double u2 = u_i;

        double dx = 0.;
        
        if (mapToEqui == 1) {
            v_s = v_s + 0.5 * (RPnPhaseSpacePanel.myH_ - u_s);
            v_i = v_i + 0.5 * (RPnPhaseSpacePanel.myH_ - u_i);

            u_s = RPnPhaseSpacePanel.myH_ - 0.8660254 * (RPnPhaseSpacePanel.myH_ - u_s);
            u_i = RPnPhaseSpacePanel.myH_ - 0.8660254 * (RPnPhaseSpacePanel.myH_ - u_i);

            v1 = v1 + 0.5 * (RPnPhaseSpacePanel.myH_ - u1);
            u1 = RPnPhaseSpacePanel.myH_ - 0.8660254 * (RPnPhaseSpacePanel.myH_ - u1);

            v2 = v2 + 0.5 * (RPnPhaseSpacePanel.myH_ - u2);
            u2 = RPnPhaseSpacePanel.myH_ - 0.8660254 * (RPnPhaseSpacePanel.myH_ - u2);

            dx = (Math.abs(u_s - u_i)) / 1.73205;
        }


            //--- caso B: o lados superior e direito do paralelogramo intersectam o bordo direito do triangulo
            if (Line2D.linesIntersect(v_s, u_s, v_i + dx, u_s, V2x, V2y, V3x, V3y)) {
                Coords2D p1Int = new Coords2D();
                Coords2D p2Int = new Coords2D();
                double yp2 = 0.;

                if (mapToEqui == 0) {
                    yp2 = V3y + v_i - V3x;

                    p1Int.setElement(0, V3x - (V3y - u_s));
                    p1Int.setElement(1, u_s);
                    p2Int.setElement(0, v_i);
                    p2Int.setElement(1, yp2);
                }

                if (mapToEqui == 1) {
                    yp2 = (V3y + u_i - (V3x - v_i) * TAN60) / 2.;

                    p1Int.setElement(0, V3x - (V3y - u_s) / TAN60);
                    p1Int.setElement(1, u_s);
                    p2Int.setElement(0, V3x - (V3y - yp2) / TAN60);
                    p2Int.setElement(1, yp2);

                }
                
                poly.addPoint((int) v_s, (int) u_s);
                poly.addPoint((int) (v_s - dx), (int) u_i);
                poly.addPoint((int) v_i, (int) u_i);
                poly.addPoint((int) (p2Int.getElement(0)), (int) (p2Int.getElement(1)));
                poly.addPoint((int) (p1Int.getElement(0)), (int) u_s);

            } //----------------------------------------------------------------------------------------------
            //--- caso A: o paralelogramo nao intersecta o bordo direito do triangulo
            else {
                poly.addPoint((int) (v_s), (int) (u_s));
                poly.addPoint((int) (v1), (int) (u1));
                poly.addPoint((int) (v_i), (int) (u_i));
                poly.addPoint((int) (v2), (int) (u2));

            }


        CoordsArray wcCoordsTopRight = toWorldCoords(new Coords2D(v1, u1), scene);
        CoordsArray wcCoordsDownLeft = toWorldCoords(new Coords2D(v2, u2), scene);

        topRight.setElement(0, wcCoordsTopRight.getElement(0));
        topRight.setElement(1, wcCoordsTopRight.getElement(1));
        downLeft.setElement(0, wcCoordsDownLeft.getElement(0));
        downLeft.setElement(1, wcCoordsDownLeft.getElement(1));

        return poly;

    }


}
