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
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import rpn.RPnPhaseSpacePanel;
import rpn.component.HugoniotSegGeom;
import rpnumerics.Orbit;
import rpnumerics.RPNUMERICS;
import rpnumerics.SegmentedCurve;
import wave.multid.Coords2D;
import wave.multid.CoordsArray;
import wave.multid.view.Scene;
import wave.multid.view.ViewingTransform;
import wave.util.Boundary;
import wave.util.IsoTriang2DBoundary;
import wave.util.RealSegment;
import wave.util.RealVector;

/**
 *
 * @author moreira
 */
public class GeometryGraphND {

    protected Line2D line1;
    protected Line2D line2;
    protected Line2D line3;
    protected Line2D line4;
    protected Line2D line3DC;
    protected Line2D line4DC;
    protected Line2D line5;
    protected Line2D line6;
    protected Line2D line7;
    protected Line2D line8;
    protected Shape square1;
    protected Shape square2;
    protected int zerado = 0;
    protected List<Rectangle2D.Double> listaRet = new ArrayList();  ////////
    static public RealVector topRight = new RealVector(RPNUMERICS.domainDim());
    static public RealVector downLeft = new RealVector(RPNUMERICS.domainDim());
    static public RealVector pMarca = new RealVector(RPNUMERICS.domainDim());
    static public RealVector pMarcaDC = new RealVector(RPNUMERICS.domainDim());
    static public RealVector targetPoint = new RealVector(RPNUMERICS.domainDim());
    static public RealVector cornerRet = new RealVector(RPNUMERICS.domainDim());
    static public RealVector cornerStr = new RealVector(RPNUMERICS.domainDim());
    static public List zContido = new ArrayList();
    static public List wContido = new ArrayList();
    //*** isso é uma lista de indices (Leandro)
    static public List indContido = new ArrayList();
    
    protected Color cor12 = null, cor34 = null, cor56 = null, cor78 = null, corSquare = null, corString = null;
    protected Line2D.Double linex, liney, lineObl;
    public static int mostraGrid = 0;
    public static int mapToEqui = 0;
    public static int refina = 0;
    
    

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
            for (int i = 0; i < indContido.size(); i++) {
                saida.write(indContido.get(i).toString());
                saida.write(" ");
                saida.flush();
                saida.write("\n");
            }
            saida.close();

        } catch (IOException e) {
            System.out.println("Não deu para escrever o arquivo");
        }
    }


    public void changeColor() {
        if (RPnPhaseSpacePanel.DEFAULT_BACKGROUND_COLOR == Color.white) {
            cor12 = Color.red;
            cor34 = Color.red;
            cor56 = Color.red;
            cor78 = Color.red;
            corSquare = Color.red;
            corString = Color.black;
        }
        if (RPnPhaseSpacePanel.DEFAULT_BACKGROUND_COLOR == Color.black) {
            cor12 = Color.red;
            cor34 = Color.yellow;
            cor56 = Color.red;
            cor78 = Color.yellow;
            corSquare = Color.red;
            corString = Color.white;
        }
    }


    public Coords2D toDeviceCoords(Scene scene, RealVector point) {
        ViewingTransform transf = scene.getViewingTransform();
        CoordsArray wcCoords = new CoordsArray(point);
        Coords2D dcCoords = new Coords2D();
        transf.viewPlaneTransform(wcCoords, dcCoords);

        return dcCoords;
    }


    public CoordsArray toWorldCoords(Coords2D ponto, Scene scene) {
        ViewingTransform transf = scene.getViewingTransform();
        Coords2D dcCoords = new Coords2D(ponto);
        RealVector wc = new RealVector(2);
        CoordsArray wcCoords = new CoordsArray(wc);
        transf.dcInverseTransform(dcCoords, wcCoords);

        return wcCoords;
    }


    public void drawObjects(Graphics g, Object obj, Coords2D newDC, Coords2D setaDC) {

        Graphics2D graph = (Graphics2D) g;

        Font font = new Font("Verdana", Font.PLAIN, 13);
        g.setFont(font);
        FontMetrics metrics = new FontMetrics(font) {
        };
        Rectangle2D bounds = metrics.getStringBounds(obj.toString(), null);
        int tamPix = (int) bounds.getWidth();

        //***** Ficou melhor !!!!!!!!!!!!
        double raio = 7.;
        double Dx = Math.abs(newDC.getX() - setaDC.getX());
        double Dy = Math.abs(newDC.getY() - setaDC.getY());
        double dist = Math.sqrt(Math.pow(Dy, 2) + Math.pow(Dx, 2));

        double dx = (raio * Dx) / dist;
        double dy = (raio * Dy) / dist;

        if (setaDC.getX() < newDC.getX() && setaDC.getY() < newDC.getY()) {
            g.drawString((String) obj, (int) (newDC.getX() + 5), (int) (newDC.getY() + 5));
            Line2D line_ = new Line2D.Double(setaDC.getX() + dx, setaDC.getY() + dy, newDC.getX(), newDC.getY());
            graph.draw(line_);
        } else if (setaDC.getX() > newDC.getX() && setaDC.getY() < newDC.getY()) {
            g.drawString((String) obj, (int) (newDC.getX() - (tamPix + 2)), (int) (newDC.getY() + 5));
            Line2D line_ = new Line2D.Double(setaDC.getX() - dx, setaDC.getY() + dy, newDC.getX(), newDC.getY());
            graph.draw(line_);
        } else if (setaDC.getX() > newDC.getX() && setaDC.getY() > newDC.getY()) {
            g.drawString((String) obj, (int) (newDC.getX() - (tamPix + 2)), (int) (newDC.getY() + 5));
            Line2D line_ = new Line2D.Double(setaDC.getX() - dx, setaDC.getY() - dy, newDC.getX(), newDC.getY());
            graph.draw(line_);
        } else if (setaDC.getX() < newDC.getX() && setaDC.getY() > newDC.getY()) {
            g.drawString((String) obj, (int) (newDC.getX() + 5), (int) (newDC.getY() + 5));
            Line2D line_ = new Line2D.Double(setaDC.getX() + dx, setaDC.getY() - dy, newDC.getX(), newDC.getY());
            graph.draw(line_);
        }
        //**************************************************

    }


    public void defineClassifiers(Graphics g, Scene scene_) {

        int cont = ClassifierAgent.xDevStr.size();

        for (int i = 0; i < cont; i++) {

            double YCR = Double.valueOf(ClassifierAgent.yStr.get(i).toString()).doubleValue();
            double XCR = Double.valueOf(ClassifierAgent.xStr.get(i).toString()).doubleValue();

//            int strView = Integer.valueOf(ClassifierAgent.strView.get(i).toString()).intValue();
//
//            if (strView == -1) {
//                g.setColor(RPnPhaseSpacePanel.DEFAULT_BACKGROUND_COLOR);
//            } else {
//                g.setColor(corString);
//            }

            g.setColor(corString);

            RealVector newRV = new RealVector(RPNUMERICS.domainDim());
            newRV.setElement(0, YCR);
            newRV.setElement(1, XCR);

            Coords2D newDC = toDeviceCoords(scene_, newRV);

            // ***
            double YSeta = Double.valueOf(ClassifierAgent.ySeta.get(i).toString()).doubleValue();
            double XSeta = Double.valueOf(ClassifierAgent.xSeta.get(i).toString()).doubleValue();

            RealVector setaRV = new RealVector(RPNUMERICS.domainDim());
            setaRV.setElement(0, YSeta);
            setaRV.setElement(1, XSeta);

            Coords2D setaDC = toDeviceCoords(scene_, setaRV);
            // ***

            int s1 = (Integer) (ClassifierAgent.tipo.get(i));
            Object obj = HugoniotSegGeom.s[s1];

            drawObjects(g, obj, newDC, setaDC);

        }

    }


    public void defineVelocities(Graphics g, Scene scene_) {

        int cont = VelocityAgent.xDevVel.size();

        for (int i = 0; i < cont; i++) {

            double YCR = Double.valueOf(VelocityAgent.yVel.get(i).toString()).doubleValue();
            double XCR = Double.valueOf(VelocityAgent.xVel.get(i).toString()).doubleValue();

//            int velView = Integer.valueOf(VelocityAgent.velView.get(i).toString()).intValue();
//
//            if (velView == -1) {
//                g.setColor(RPnPhaseSpacePanel.DEFAULT_BACKGROUND_COLOR);
//            } else {
//                g.setColor(corString);
//            }

            g.setColor(corString);

            RealVector newRV = new RealVector(RPNUMERICS.domainDim());
            newRV.setElement(0, YCR);
            newRV.setElement(1, XCR);

            Coords2D newDC = toDeviceCoords(scene_, newRV);

            // ***
            double YSeta = Double.valueOf(VelocityAgent.ySetaVel.get(i).toString()).doubleValue();
            double XSeta = Double.valueOf(VelocityAgent.xSetaVel.get(i).toString()).doubleValue();

            RealVector setaRV = new RealVector(RPNUMERICS.domainDim());
            setaRV.setElement(0, YSeta);
            setaRV.setElement(1, XSeta);

            Coords2D setaDC = toDeviceCoords(scene_, setaRV);
            // ***

            double v1 = (Double) (VelocityAgent.vel.get(i));
            String exp = String.format("%.4e", v1);
            Object obj = exp;

            drawObjects(g, obj, newDC, setaDC);

        }

    }


    public void testAreaContains(Scene scene) {
    
        RPnPhaseSpacePanel panel = new RPnPhaseSpacePanel(scene);
        ViewingTransform transf = panel.scene().getViewingTransform();

        if (GeometryUtil.closestCurve_ instanceof Orbit) {

            Orbit curve = (Orbit) GeometryUtil.closestCurve_;


            for (int i = 0; i < curve.getPoints().length; i++) {

                CoordsArray wcCoordsCurve = new CoordsArray(curve.getPoints()[i]);
                Coords2D dcCoordsCurve = new Coords2D();
                transf.viewPlaneTransform(wcCoordsCurve, dcCoordsCurve);

                double xCurve = dcCoordsCurve.getElement(0);
                double yCurve = dcCoordsCurve.getElement(1);

                if (square1.contains(xCurve, yCurve)  &&  refina == 1) {
                    indContido.add(i);
                    if (zerado == 2) {
                        zContido.add(curve.getPoints()[i].getElement(2));
                    } else {
                        zContido.add(curve.getPoints()[i].getElement(1));
                    }

                }

            }

        }


        if (GeometryUtil.closestCurve_ instanceof SegmentedCurve) {

            SegmentedCurve curve = (SegmentedCurve) GeometryUtil.closestCurve_;

            for (int i = 0; i < curve.segments().size(); i++) {

                Coords2D dcCoordsCurve = new Coords2D();
                dcCoordsCurve = toDeviceCoords(scene, ((RealSegment) (curve.segments()).get(i)).p1());
                double xCurve = dcCoordsCurve.getElement(0);
                double yCurve = dcCoordsCurve.getElement(1);

                Coords2D dcCoordsCurve2 = new Coords2D();
                dcCoordsCurve2 = toDeviceCoords(scene, ((RealSegment) (curve.segments()).get(i)).p2());
                double xCurve2 = dcCoordsCurve2.getElement(0);
                double yCurve2 = dcCoordsCurve2.getElement(1);

                if ((square1.contains(xCurve, yCurve) && square1.contains(xCurve2, yCurve2))  &&  refina == 1) {
                    indContido.add(i);
                    if (zerado == 2) {
                        zContido.add(((RealSegment) (curve.segments()).get(i)).p1().getElement(2));
                    } else {
                        zContido.add(((RealSegment) (curve.segments()).get(i)).p1().getElement(1));
                    }

                }

            }
            
        }

    }



    public Line2D.Double mapLine(Line2D.Double line) {

        line.x1 = line.x1 + 0.5 * (RPnPhaseSpacePanel.myH_ - line.y1);
        line.x2 = line.x2 + 0.5 * (RPnPhaseSpacePanel.myH_ - line.y2);

        line.y1 = RPnPhaseSpacePanel.myH_ - 0.8660254 * (RPnPhaseSpacePanel.myH_ - line.y1);
        line.y2 = RPnPhaseSpacePanel.myH_ - 0.8660254 * (RPnPhaseSpacePanel.myH_ - line.y2);

        return line;
    }



    public void drawGrid(Graphics g, Scene scene) {

        int index = 0;
        if (RPNUMERICS.domainDim() == 3) index = 1;

        g.setColor(Color.gray);
        Graphics2D graph = (Graphics2D) g;

        //double xResolution = new Double(RPNUMERICS.getConfiguration("Contour").getParam("x-resolution"));
        //int[] resolution = RPnDataModule.processResolution(RPNUMERICS.getParamValue("hugoniotcurve", "resolution"));
        int[] resolution = {1, 1};

        if (RPNUMERICS.listResolution.size()==1) GeometryUtil.closestCurve=0;
        if (RPNUMERICS.listResolution.size()>0) resolution = (int[]) RPNUMERICS.listResolution.get(GeometryUtil.closestCurve);
        //if (scene.geometries().hasNext()) resolution = (int[]) RPNUMERICS.listResolution.get(GeometryUtil.closestCurve);
        int xResolution = resolution[0];
        int yResolution = resolution[1];
        
        int nu = (int) xResolution;
        double dx = RPnPhaseSpacePanel.myW_/(1.0*nu);
        
        //*** desenha as linhas verticais
        for (int i = 0; i < nu; i++) {
            linex = new Line2D.Double(i * dx, 0, i * dx, RPnPhaseSpacePanel.myH_);
            if (index == 0  && mapToEqui == 1) linex = mapLine(linex);
            graph.draw(linex);
        }
        //*******************************

        //double yResolution = new Double(RPNUMERICS.getConfiguration("Contour").getParam("y-resolution"));
        int nv = (int) yResolution;
        double dy = RPnPhaseSpacePanel.myH_/(1.0*nu);
        
        //*** desenha as linhas horizontais
        for (int i = 0; i < nv; i++) {
            liney = new Line2D.Double(0, i * dy, RPnPhaseSpacePanel.myW_, i * dy);              // preencher com coordenadas do dispositivo
            if (index == 0  &&  mapToEqui == 1) liney = mapLine(liney);
            graph.draw(liney);
        }
        //*********************************

        //*** desenha as linhas obliquas
        Boundary boundary = RPNUMERICS.boundary();
        if (boundary instanceof IsoTriang2DBoundary) {
            for (int i = 0; i < nu; i++) {
                lineObl = new Line2D.Double(0, RPnPhaseSpacePanel.myH_ - i * dy, i * dx, RPnPhaseSpacePanel.myH_);
                if (mapToEqui == 1) lineObl = mapLine(lineObl);
                graph.draw(lineObl);
            }
        }
        //*****************************************
        

    }



}