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
import rpn.RPnPhaseSpaceAbstraction;
import rpn.RPnPhaseSpacePanel;
import rpn.component.RpGeometry;
import rpn.controller.ui.AREASELECTION_CONFIG;
import rpn.controller.ui.UIController;
import rpnumerics.RPNUMERICS;
import rpnumerics.RPnCurve;
import wave.multid.Coords2D;
import wave.multid.view.Scene;
import wave.util.RealVector;
import rpn.controller.ui.CLASSIFIERAGENT_CONFIG;
import rpn.controller.ui.UserInputTable;
import rpn.controller.ui.VELOCITYAGENT_CONFIG;
import rpn.parser.RPnDataModule;
import rpn.usecase.VelocityAgent;
import rpnumerics.BifurcationCurve;
import rpnumerics.WaveCurve;
import wave.multid.CoordsArray;
import wave.util.Boundary;
import wave.util.IsoTriang2DBoundary;

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


    // membros que - possivelmente - serao levados para GGND
    public static int mostraSing = 0;
    public static int count = 0;    //substituto do ControlClick.ind


    private Polygon defBordo() {

        double xMin = RPNUMERICS.boundary().getMinimums().getElement(0);
        double xMax = RPNUMERICS.boundary().getMaximums().getElement(0);
        double yMin = RPNUMERICS.boundary().getMinimums().getElement(1);
        double yMax = RPNUMERICS.boundary().getMaximums().getElement(1);

        RealVector V1 = new RealVector(2);
        V1.setElement(0, xMin);        V1.setElement(1, yMin);
        RealVector V2 = new RealVector(2);
        V2.setElement(0, xMin);        V2.setElement(1, yMax);
        RealVector V3 = new RealVector(2);
        V3.setElement(0, xMax);        V3.setElement(1, yMin);

        double xV1 = V1.getElement(0);      double yV1 = V1.getElement(1);
        double xV2 = V2.getElement(0);      double yV2 = V2.getElement(1);
        double xV3 = V3.getElement(0);      double yV3 = V3.getElement(1);
        V2x = xV2;  V2y = yV2;
        V3x = xV3;  V3y = yV3;

        triangle = new Polygon();
        triangle.addPoint((int)xV1, (int)yV1);
        triangle.addPoint((int)xV2, (int)yV2);
        triangle.addPoint((int)xV3, (int)yV3);

        return triangle;
    }


    public void infoWaveCurve(RealVector newValue, WaveCurve curve, RPnPhaseSpacePanel panel) {
//        count = 0;
//
//        int index = curve.findClosestSegment(newValue);
//        int indBegin = curve.getBeginSubCurve(index);
//
//        String locSubCurve = String.valueOf(index - indBegin);
//        String name = curve.getName()[index];
//        String family = " Family : " +String.valueOf(curve.getFamily());
//
//        if (name.equals("Rarefaction"))
//            panel.setToolTipText(String.valueOf(index) + " " +name + " " +curve.getPoints()[index].getLambda());
//
//        panel.setToolTipText(locSubCurve + " " + name + " " + family);
//        ToolTipManager ttm = ToolTipManager.sharedInstance();
//        ttm.setInitialDelay(0);
//        ttm.setDismissDelay(500);

    }



    public void drawFirstPanel(Graphics g, Scene scene_, RPnPhaseSpacePanel panel) {

        //----------------------------------------------Aqui, o input é um click
        UserInputTable userInputList = UIController.instance().globalInputTable();
        RealVector newValue = userInputList.values();
        //----------------------------------------------------------------------


        //------------------------------- Manter assim ou voltar à forma antiga?
//        CoordsArray wc = toWorldCoords(new Coords2D(panel.getCursorPos().getX(), panel.getCursorPos().getY()), scene_);
//        RealVector rv = new RealVector(2);
//        rv.setElement(0, wc.getElement(0));
//        rv.setElement(1, wc.getElement(1));
//        RealVector newValue = new RealVector(2);
//        newValue = rv;
        //----------------------------------------------------------------------


        //if (mostraGrid != 0  &&  panel.getName().equals(RPnPhaseSpaceAbstraction.namePhaseSpace)){
        if(mostraGrid != 0) {
            drawGrid(g, scene_);
        }

        Graphics2D graph = (Graphics2D) g;

        if (UIController.instance().getState() instanceof AREASELECTION_CONFIG  &&  panel.getName().equals(RPnPhaseSpaceAbstraction.namePhaseSpace)) {
            g.setColor(cor12);
            graph.draw(line1);
            graph.draw(line2);

        }

        g.setColor(cor34);


        if (panel.getName().equals(RPnPhaseSpaceAbstraction.namePhaseSpace)) {
            graph.draw(line3);
            graph.draw(line4);

            //********* esboço de marcação de pontos de equilibrio
            for (int i=0; i<VelocityAgent.listaEquil.size(); i++) {
                RealVector p = VelocityAgent.listaEquil.get(i);

                Coords2D dcCoordsEQ = toDeviceCoords(scene_, p);
                double xEQ = dcCoordsEQ.getElement(0);
                double yEQ = dcCoordsEQ.getElement(1);

                Line2D lineEQ1 = new Line2D.Double(xEQ - 5, yEQ, xEQ + 5, yEQ);
                Line2D lineEQ2 = new Line2D.Double(xEQ, yEQ - 5, xEQ, yEQ + 5);

                graph.draw(lineEQ1);
                graph.draw(lineEQ2);

            }
            //*********

        }


        try {
            RPnPhaseSpaceAbstraction phaseSpace = RPnDataModule.PHASESPACE;
            RpGeometry geom = phaseSpace.findClosestGeometry(newValue);

            //RpGeometry geom = RPnPhaseSpaceAbstraction.findClosestGeometry(newValue);
            RPnCurve curve = (RPnCurve)(geom.geomFactory().geomSource());
            if (curve instanceof BifurcationCurve) {
                if (!panel.getName().equals(RPnPhaseSpaceAbstraction.namePhaseSpace) && !panel.getName().equals("Phase Space")) {
                    graph.draw(line3DC);
                    graph.draw(line4DC);
                }
            }


            //------------------------------------------------------------------
//            else if (curve instanceof WaveCurve) {
//
//                //---------------------------------
//                Coords2D dcCoordsMP = toDeviceCoords(scene_, curve.findClosestPoint(newValue));
//                double xMP = dcCoordsMP.getElement(1);
//                double yMP = dcCoordsMP.getElement(0);
//
//                line3 = new Line2D.Double(yMP - 5, xMP, yMP + 5, xMP);
//                line4 = new Line2D.Double(yMP, xMP - 5, yMP, xMP + 5);
//                if (panel.getName().equals(RPnPhaseSpaceAbstraction.namePhaseSpace)) {
//                    graph.draw(line3);
//                    graph.draw(line4);
//
//                    infoWaveCurve(newValue, (WaveCurve) curve, panel);
//                }
//                //---------------------------------
//
//                //infoWaveCurve(newValue, (WaveCurve) curve, panel);
//
//            }
//            else panel.setToolTipText(null);
            //------------------------------------------------------------------


        } catch (Exception e) {
        }


        if ((count % 2) == 0) {

            if (UIController.instance().getState() instanceof AREASELECTION_CONFIG  &&  panel.getName().equals(RPnPhaseSpaceAbstraction.namePhaseSpace)) {
                g.setColor(cor56);
                graph.draw(line5);
                graph.draw(line6);
                g.setColor(corSquare);
                
                graph.draw(square1);
            }
            if (UIController.instance().getState() instanceof AREASELECTION_CONFIG  &&  !panel.getName().equals(RPnPhaseSpaceAbstraction.namePhaseSpace)) {

                RpGeometry geom = RPnDataModule.PHASESPACE.findClosestGeometry(newValue);
                RPnCurve curve = (RPnCurve)(geom.geomFactory().geomSource());
                if (curve instanceof BifurcationCurve) {
                    g.setColor(corSquare);
                    graph.draw(squareDC);
                }
                
            }
            
        }


            //*** Para os botoes Classify e Velocity
            if ((UIController.instance().getState() instanceof CLASSIFIERAGENT_CONFIG)
                    || (UIController.instance().getState() instanceof VELOCITYAGENT_CONFIG)) {

                defineClassifiers(g, scene_, panel);
                defineVelocities(g, scene_, panel);

            }
            //*** Fim dos botoes Classify e Velocity





    }


    public void paintComponent(Graphics g, Scene scene_, RPnPhaseSpacePanel panel) {

        changeColor();
        drawFirstPanel(g, scene_, panel);

    }


    public void markArea(Scene scene) {

        double vMin = RPNUMERICS.boundary().getMinimums().getElement(0);
        double vMax = RPNUMERICS.boundary().getMaximums().getElement(0);
        double uMin = RPNUMERICS.boundary().getMinimums().getElement(1);
        double uMax = RPNUMERICS.boundary().getMaximums().getElement(1);

        RealVector P1, P2, P3, P4;

        int[] resolution = {1, 1};

        if (RPnPhaseSpaceAbstraction.listResolution.size()==1) RPnPhaseSpaceAbstraction.closestCurve=0;
        if (RPnPhaseSpaceAbstraction.listResolution.size()>0) resolution = (int[]) RPnPhaseSpaceAbstraction.listResolution.get(RPnPhaseSpaceAbstraction.closestCurve);

        int nv = resolution[0];
        int nu = resolution[1];

        double zmin = Math.min(targetPoint.getElement(0), cornerRet.getElement(0));
        double zmax = Math.max(targetPoint.getElement(0), cornerRet.getElement(0));
        double wmin = Math.min(targetPoint.getElement(1), cornerRet.getElement(1));
        double wmax = Math.max(targetPoint.getElement(1), cornerRet.getElement(1));

        if (nv==0  &&  nu==0) {
            P1 = new RealVector(new double[]{zmin, wmin});
            P2 = new RealVector(new double[]{zmax, wmin});
            P3 = new RealVector(new double[]{zmax, wmax});
            P4 = new RealVector(new double[]{zmin, wmax});
        }
        else {
            double dv = (vMax - vMin)/(1.*nv);
            double du = (uMax - uMin)/(1.*nu);

            P1 = new RealVector(new double[]{vMin + (int) ((zmin - vMin) / dv) * dv, uMin + (int) ((wmin - uMin) / du) * du});
            P2 = new RealVector(new double[]{vMin + (int) ((zmax - vMin) / dv + 1) * dv, uMin + (int) ((wmin - uMin) / du) * du});
            P3 = new RealVector(new double[]{vMin + (int) ((zmax - vMin) / dv + 1) * dv, uMin + (int) ((wmax - uMin) / du + 1) * du});
            P4 = new RealVector(new double[]{vMin + (int) ((zmin - vMin) / dv) * dv, uMin + (int) ((wmax - uMin) / du + 1) * du});

            int ResV = (int) Math.round((P2.getElement(0) - P1.getElement(0))/dv);
            int ResU = (int) Math.round((P4.getElement(1) - P1.getElement(1))/du);

            //System.out.println("Resolucao local : " +ResV  +" por " +ResU);
        }

        Coords2D dcP1 = toDeviceCoords(scene, P1);
        Coords2D dcP2 = toDeviceCoords(scene, P2);
        Coords2D dcP3 = toDeviceCoords(scene, P3);
        Coords2D dcP4 = toDeviceCoords(scene, P4);

        Polygon pol = new Polygon();
        pol.addPoint((int)dcP1.getX(), (int)dcP1.getY());
        pol.addPoint((int)dcP2.getX(), (int)dcP2.getY());
        pol.addPoint((int)dcP3.getX(), (int)dcP3.getY());
        pol.addPoint((int)dcP4.getX() , (int)dcP4.getY());

        square1 = defShapeWC(pol, scene);

        indContido.clear();
        testAreaContains(scene);

    }



    private Shape defShapeWC(Shape shape, Scene scene) {

        Boundary boundary = RPNUMERICS.boundary();
        if (boundary instanceof IsoTriang2DBoundary) {
            defBordo();
        }

        Polygon poly = new Polygon();

        double v_s = shape.getBounds2D().getMinX();
        double u_s = shape.getBounds2D().getMinY();
        double v_i = shape.getBounds2D().getMaxX();
        double u_i = shape.getBounds2D().getMaxY();

        double dx = 0.;
        if (mapToEqui == 1) dx = (Math.abs(u_s - u_i)) / 1.73205;

        Coords2D dcP1 = new Coords2D(v_s, u_i);
        Coords2D dcP2 = new Coords2D(v_i, u_i);
        Coords2D dcP3 = new Coords2D(v_i, u_s);
        Coords2D dcP4 = new Coords2D(v_s, u_s);

        CoordsArray wcP1 = toWorldCoords(dcP1, scene);
        CoordsArray wcP2 = toWorldCoords(dcP2, scene);
        CoordsArray wcP3 = toWorldCoords(dcP3, scene);
        CoordsArray wcP4 = toWorldCoords(dcP4, scene);

        double x = 0.;
        double y = 0.;

        //--- caso B: o lados superior e direito do paralelogramo intersectam o bordo direito do triangulo
        if (Line2D.linesIntersect(wcP4.getElement(0), wcP4.getElement(1), wcP3.getElement(0), wcP3.getElement(1), V2x, V2y, V3x, V3y)) {
            x = ((V2y-wcP3.getElement(1))*V3x + (wcP3.getElement(1)-V3y)*V2x)/(V2y - V3y);
            y = ((V3x-wcP3.getElement(0))*V2y + (wcP3.getElement(0)-V2x)*V3y)/(V3x - V2x);

            RealVector PA = new RealVector(new double[]{x, wcP3.getElement(1)});
            RealVector PB = new RealVector(new double[]{wcP3.getElement(0), y});

            Coords2D dcPA = toDeviceCoords(scene, PA);
            Coords2D dcPB = toDeviceCoords(scene, PB);

            poly.addPoint((int)dcP1.getX(), (int)dcP1.getY());
            poly.addPoint((int)(dcP2.getX()-dx), (int)dcP2.getY());
            poly.addPoint((int)dcPB.getX() , (int)dcPB.getY());
            poly.addPoint((int)dcPA.getX() , (int)dcPA.getY());
            poly.addPoint((int)(dcP4.getX()+dx) , (int)dcP4.getY());

        }
        //--- caso A: o paralelogramo nao intersecta o bordo direito do triangulo
        else {
            poly = (Polygon) shape;
        }

        topRight.setElement(0, wcP3.getElement(0));
        topRight.setElement(1, wcP3.getElement(1));
        downLeft.setElement(0, wcP1.getElement(0));
        downLeft.setElement(1, wcP1.getElement(1));

        //return new Rectangle2D.Double(v_s, u_s, Math.abs(v_i - v_s), Math.abs(u_i - u_s));    //*** REMOVER: ficar apenas enquanto faço teste de zoom

        return poly;

    }


    public void markPoints(Scene scene) {        //*** era do GeometryGraph3D, vou usar para testar mapeamento do square

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

        if ((UIController.instance().getState() instanceof AREASELECTION_CONFIG)) {
            markArea(scene);
        }


    }


}