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
import java.util.ArrayList;
import javax.swing.ToolTipManager;
import rpn.RPnPhaseSpaceAbstraction;
import rpn.RPnPhaseSpacePanel;
import rpn.component.RpGeometry;
import rpn.controller.ui.UIController;
import rpnumerics.RPNUMERICS;
import rpnumerics.RPnCurve;
import wave.multid.Coords2D;
import wave.multid.view.Scene;
import wave.util.RealVector;
import rpn.controller.ui.RPnVelocityPlotter;
import rpn.controller.ui.UserInputTable;
import rpn.parser.RPnDataModule;
import rpnumerics.BifurcationCurve;
import rpnumerics.OrbitPoint;
import rpnumerics.WaveCurve;
import rpnumerics.FundamentalCurve;

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


    //*** COMECEI A ADAPTAR E RESTAURAR USO EM 05NOV
    public void infoWaveCurve(RealVector newValue, WaveCurve curve, RPnPhaseSpacePanel panel) {

        int tam = ((WaveCurve) curve).getBranchsList().size();
        ArrayList<Integer> result = new ArrayList<Integer>();
        ArrayList<OrbitPoint> resultPoint = new ArrayList<OrbitPoint>();
        ArrayList<String> name = new ArrayList<String>();

        for (int i = 0; i < tam; i++) {
            FundamentalCurve orbit = (FundamentalCurve) ((WaveCurve) curve).getBranchsList().get(i);
            OrbitPoint[] parcial = orbit.getPoints();
            for (int j = 0; j < parcial.length; j++) {
                result.add(j);
                resultPoint.add(parcial[j]);
                name.add(orbit.getClass().getSimpleName());
            }
        }

        int segTotal = curve.findClosestSegment(newValue);
        int segBranch = result.get(segTotal);

        //panel.setToolTipText(String.valueOf(segBranch) +" , " +String.valueOf(curve.segments().size()));
        //ToolTipManager ttm = ToolTipManager.sharedInstance();
        //ttm.setInitialDelay(0);
        //ttm.setDismissDelay(500);

        int other = resultPoint.get(segTotal).getCorrespondingPointIndex();
        pMarcaWC = resultPoint.get(other).getCoords();

        if (name.get(segTotal).equals("RarefactionOrbit"))
            pMarcaWC = resultPoint.get(segTotal).getCoords();

    }



    public void drawFirstPanel(Graphics g, Scene scene_, RPnPhaseSpacePanel panel) {

        UserInputTable userInputList = UIController.instance().globalInputTable();
        RealVector newValue = userInputList.values();        

        if(mostraGrid != 0)
            drawGrid(g, scene_);
        
        Graphics2D graph = (Graphics2D) g;
        g.setColor(cor34);

        if (panel.getName().equals(RPnPhaseSpaceAbstraction.namePhaseSpace)) {
            graph.draw(line3);
            graph.draw(line4);

            //********* esboço de marcação de pontos de equilibrio
            for (int i=0; i<RPnVelocityPlotter.listaEquil.size(); i++) {
                RealVector p = RPnVelocityPlotter.listaEquil.get(i);

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

            RPnCurve curve = (RPnCurve)(geom.geomFactory().geomSource());
            if (curve instanceof BifurcationCurve) {

                if (UIController.instance().isAuxPanelsEnabled()) {
                    if (!panel.getName().equals(RPnPhaseSpaceAbstraction.namePhaseSpace) && !panel.getName().equals("Phase Space")) {
                        graph.draw(line3DC);
                        graph.draw(line4DC);
                    }
                }
                else {
                    graph.draw(line3DC);
                    graph.draw(line4DC);
                }

            }
            if (curve instanceof WaveCurve) {
                infoWaveCurve(newValue, (WaveCurve)curve, panel);
                graph.draw(line3WC);
                graph.draw(line4WC);
            }

        } catch (Exception e) {
        }


    }


    public void paintComponent(Graphics g, Scene scene_, RPnPhaseSpacePanel panel) {

        changeColor();
        drawFirstPanel(g, scene_, panel);

    }



    //*** NAO SERA MAIS USADO   --- MAS, POR ENQUANTO, NÃO DELETAR!!!
    private Shape defShapeWC(Shape shape, Scene scene) {

//        Boundary boundary = RPNUMERICS.boundary();
//        if (boundary instanceof IsoTriang2DBoundary) {
//            defBordo();
//        }
//
//        Polygon poly = new Polygon();
//
//        double v_s = shape.getBounds2D().getMinX();
//        double u_s = shape.getBounds2D().getMinY();
//        double v_i = shape.getBounds2D().getMaxX();
//        double u_i = shape.getBounds2D().getMaxY();
//
//        double dx = 0.;
//        if (mapToEqui == 1) dx = (Math.abs(u_s - u_i)) / 1.73205;
//
//        Coords2D dcP1 = new Coords2D(v_s, u_i);
//        Coords2D dcP2 = new Coords2D(v_i, u_i);
//        Coords2D dcP3 = new Coords2D(v_i, u_s);
//        Coords2D dcP4 = new Coords2D(v_s, u_s);
//
//        CoordsArray wcP1 = toWorldCoords(dcP1, scene);
//        CoordsArray wcP2 = toWorldCoords(dcP2, scene);
//        CoordsArray wcP3 = toWorldCoords(dcP3, scene);
//        CoordsArray wcP4 = toWorldCoords(dcP4, scene);
//
//        double x = 0.;
//        double y = 0.;
//
//        //--- caso B: o lados superior e direito do paralelogramo intersectam o bordo direito do triangulo
//        if (Line2D.linesIntersect(wcP4.getElement(0), wcP4.getElement(1), wcP3.getElement(0), wcP3.getElement(1), V2x, V2y, V3x, V3y)) {
//            x = ((V2y-wcP3.getElement(1))*V3x + (wcP3.getElement(1)-V3y)*V2x)/(V2y - V3y);
//            y = ((V3x-wcP3.getElement(0))*V2y + (wcP3.getElement(0)-V2x)*V3y)/(V3x - V2x);
//
//            RealVector PA = new RealVector(new double[]{x, wcP3.getElement(1)});
//            RealVector PB = new RealVector(new double[]{wcP3.getElement(0), y});
//
//            Coords2D dcPA = toDeviceCoords(scene, PA);
//            Coords2D dcPB = toDeviceCoords(scene, PB);
//
//            poly.addPoint((int)dcP1.getX(), (int)dcP1.getY());
//            poly.addPoint((int)(dcP2.getX()-dx), (int)dcP2.getY());
//            poly.addPoint((int)dcPB.getX() , (int)dcPB.getY());
//            poly.addPoint((int)dcPA.getX() , (int)dcPA.getY());
//            poly.addPoint((int)(dcP4.getX()+dx) , (int)dcP4.getY());
//
//        }
//        //--- caso A: o paralelogramo nao intersecta o bordo direito do triangulo
//        else {
//            poly = (Polygon) shape;
//        }
//
//        topRight.setElement(0, wcP3.getElement(0));
//        topRight.setElement(1, wcP3.getElement(1));
//        downLeft.setElement(0, wcP1.getElement(0));
//        downLeft.setElement(1, wcP1.getElement(1));
//
//        //return new Rectangle2D.Double(v_s, u_s, Math.abs(v_i - v_s), Math.abs(u_i - u_s));    //*** REMOVER: ficar apenas enquanto faço teste de zoom
//
//        return poly;


        return null;

    }


    public void markPoints(Scene scene) {
        Coords2D dcCoordsMP = toDeviceCoords(scene, pMarca);
        double xMP = dcCoordsMP.getElement(1);
        double yMP = dcCoordsMP.getElement(0);

        Coords2D dcCoordsMPDCnt = toDeviceCoords(scene, pMarcaDC);
        double xMPDCnt = dcCoordsMPDCnt.getElement(1);
        double yMPDCnt = dcCoordsMPDCnt.getElement(0);

        // ---------
        Coords2D dcCoordsMPWCurve = toDeviceCoords(scene, pMarcaWC);
        double xMPWCurve = dcCoordsMPWCurve.getElement(1);
        double yMPWCurve = dcCoordsMPWCurve.getElement(0);
        // ---------

        //** Define as geometrias de resposta para interface.
        int h = 5;
        line3 = new Line2D.Double(yMP - h, xMP, yMP + h, xMP);
        line4 = new Line2D.Double(yMP, xMP - h, yMP, xMP + h);
        line3DC = new Line2D.Double(yMPDCnt - h, xMPDCnt, yMPDCnt + h, xMPDCnt);
        line4DC = new Line2D.Double(yMPDCnt, xMPDCnt - h, yMPDCnt, xMPDCnt + h);

        // --------
        line3WC = new Line2D.Double(yMPWCurve - h, xMPWCurve, yMPWCurve + h, xMPWCurve);
        line4WC = new Line2D.Double(yMPWCurve, xMPWCurve - h, yMPWCurve, xMPWCurve + h);
        // --------

        //***
    }


}