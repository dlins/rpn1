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
import java.util.Iterator;
import java.util.List;
import rpn.RPnPhaseSpaceAbstraction;
import rpn.RPnPhaseSpacePanel;
import rpn.component.BifurcationCurveGeom;
import rpn.component.HugoniotSegGeom;
import rpn.component.RealSegGeom;
import rpn.component.RpGeometry;
import rpn.component.SegmentedCurveGeom;
import rpn.controller.ui.UIController;
import rpn.controller.ui.UserInputTable;
import rpnumerics.Orbit;
import rpnumerics.RPNUMERICS;
import rpnumerics.RPnCurve;
import rpnumerics.SegmentedCurve;
import wave.multid.Coords2D;
import wave.multid.CoordsArray;
import wave.multid.view.Scene;
import wave.multid.view.ViewingTransform;
import wave.util.RealSegment;
import wave.util.RealVector;
import rpn.controller.ui.CLASSIFIERAGENT_CONFIG;
import rpn.controller.ui.VELOCITYAGENT_CONFIG;
import rpn.parser.RPnDataModule;
import rpn.usecase.ClassifierAgent;
import rpn.usecase.VelocityAgent;
import rpnumerics.BifurcationCurve;
import wave.multid.model.MultiGeometry;

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
    static public Shape squareDC;
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
    public static int onCurve = 0;


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
            ClassifierAgent.xSeta.remove(lastIndex);
            ClassifierAgent.xStr.remove(lastIndex);
            ClassifierAgent.ySeta.remove(lastIndex);
            ClassifierAgent.yStr.remove(lastIndex);
        }

        if (UIController.instance().getState() instanceof VELOCITYAGENT_CONFIG) {
            int lastIndex = VelocityAgent.vel.size() - 1;
            VelocityAgent.velView.remove(lastIndex);
            VelocityAgent.indCurvaVel.remove(lastIndex);
            VelocityAgent.vel.remove(lastIndex);
            VelocityAgent.xSetaVel.remove(lastIndex);
            VelocityAgent.xVel.remove(lastIndex);
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
        RPnPhaseSpaceAbstraction.listResolution.clear();
        VelocityAgent.listaEquil.clear();
        
    }

    
    public void grava() {
        try {
            //FileWriter gravador = new FileWriter("/impa/home/g/moreira/ListaDePontos/ListaDePontos.txt");
            FileWriter gravador = new FileWriter("/home/moreira/ListaDePontos/ListaDePontos.txt");
            BufferedWriter saida = new BufferedWriter(gravador);
            saida.write("Curva mais proxima: ");
            saida.write(" ");
            Object obj = RPnPhaseSpaceAbstraction.closestCurve;
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


    public void defineClassifiers(Graphics g, Scene scene_, RPnPhaseSpacePanel panel) {

        //int cont = ClassifierAgent.xDevStr.size();
        int cont = ClassifierAgent.xStr.size();

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

            //drawObjects(g, obj, newDC, setaDC);     // algo equivalente ao panel.getName() deve ser usado
            if ((Integer)ClassifierAgent.strView.get(i) == 1  &&  (panel.getName().equals("Phase Space"))) drawObjects(g, obj, newDC, setaDC);
            else if ((Integer)ClassifierAgent.strView.get(i) == 2  &&  (panel.getName().equals("RightPhase Space"))) drawObjects(g, obj, newDC, setaDC);
            else if ((Integer)ClassifierAgent.strView.get(i) == 3  &&  (panel.getName().equals("LeftPhase Space"))) drawObjects(g, obj, newDC, setaDC);

        }

    }


    public void defineVelocities(Graphics g, Scene scene_, RPnPhaseSpacePanel panel) {

        //int cont = VelocityAgent.xDevVel.size();
        int cont = VelocityAgent.xVel.size();

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

            //drawObjects(g, obj, newDC, setaDC);
            if ((Integer)VelocityAgent.velView.get(i) == 1  &&  (panel.getName().equals("Phase Space"))) drawObjects(g, obj, newDC, setaDC);
            else if ((Integer)VelocityAgent.velView.get(i) == 2  &&  (panel.getName().equals("RightPhase Space"))) drawObjects(g, obj, newDC, setaDC);
            else if ((Integer)VelocityAgent.velView.get(i) == 3  &&  (panel.getName().equals("LeftPhase Space"))) drawObjects(g, obj, newDC, setaDC);

        }

    }


    public void secondArea(BifurcationCurve curve, Scene scene) {
        ArrayList segments = new ArrayList();

        if (RPnPhaseSpaceAbstraction.namePhaseSpace.equals("RightPhase Space"))
            segments = (ArrayList) ((BifurcationCurve)curve).leftSegments();

        if (RPnPhaseSpaceAbstraction.namePhaseSpace.equals("LeftPhase Space"))
            segments = (ArrayList) ((BifurcationCurve)curve).rightSegments();


        double zmin = 1.E10;
        double zmax = -1.E10;
        double wmin = 1.E10;
        double wmax = -1.E10;


        for (int i = 0; i < GeometryGraphND.indContido.size(); i++) {
            int ind = Integer.parseInt((GeometryGraphND.indContido.get(i)).toString());
            RealVector p1 = new RealVector(((RealSegment) (segments).get(ind)).p1());
            RealVector p2 = new RealVector(((RealSegment) (segments).get(ind)).p2());

            double z1 = Math.min(p1.getElement(0), p2.getElement(0));
            double z2 = Math.max(p1.getElement(0), p2.getElement(0));
            if(zmin > z1) zmin = z1;
            if(zmax < z2) zmax = z2;

            double w1 = Math.min(p1.getElement(1), p2.getElement(1));
            double w2 = Math.max(p1.getElement(1), p2.getElement(1));
            if(wmin > w1) wmin = w1;
            if(wmax < w2) wmax = w2;

            lightTest(ind);

        }

        
        double vMin = RPNUMERICS.boundary().getMinimums().getElement(0);
        double vMax = RPNUMERICS.boundary().getMaximums().getElement(0);
        double uMin = RPNUMERICS.boundary().getMinimums().getElement(1);
        double uMax = RPNUMERICS.boundary().getMaximums().getElement(1);

        int[] resolution = {1, 1};

        if (RPnPhaseSpaceAbstraction.listResolution.size()==1) RPnPhaseSpaceAbstraction.closestCurve=0;
        if (RPnPhaseSpaceAbstraction.listResolution.size()>0) resolution = (int[]) RPnPhaseSpaceAbstraction.listResolution.get(RPnPhaseSpaceAbstraction.closestCurve);

        int nv = resolution[0];
        int nu = resolution[1];

        double dv = (vMax - vMin)/(1.*nv);
        double du = (uMax - uMin)/(1.*nu);

        RealVector P1 = new RealVector(new double[]{vMin+(int)((zmin-vMin)/dv)*dv, uMin+(int)((wmin-uMin)/du)*du});
        RealVector P2 = new RealVector(new double[]{vMin+(int)((zmax-vMin)/dv+1)*dv, uMin+(int)((wmin-uMin)/du)*du});
        RealVector P3 = new RealVector(new double[]{vMin+(int)((zmax-vMin)/dv+1)*dv, uMin+(int)((wmax-uMin)/du+1)*du});
        RealVector P4 = new RealVector(new double[]{vMin+(int)((zmin-vMin)/dv)*dv, uMin+(int)((wmax-uMin)/du+1)*du});

        int ResV = (int) Math.round((P2.getElement(0) - P1.getElement(0))/dv);
        int ResU = (int) Math.round((P4.getElement(1) - P1.getElement(1))/du);
        //System.out.println("Resolucao local : " +ResV  +" por " +ResU);

        Coords2D dcP1 = toDeviceCoords(scene, P1);
        Coords2D dcP2 = toDeviceCoords(scene, P2);
        Coords2D dcP3 = toDeviceCoords(scene, P3);
        Coords2D dcP4 = toDeviceCoords(scene, P4);

        Polygon pol = new Polygon();
        pol.addPoint((int)dcP1.getX(), (int)dcP1.getY());
        pol.addPoint((int)dcP2.getX(), (int)dcP2.getY());
        pol.addPoint((int)dcP3.getX(), (int)dcP3.getY());
        pol.addPoint((int)dcP4.getX() , (int)dcP4.getY());

        squareDC = pol;
        
    }


    public void lightTest(int ind) {       //*** ALterei BifurcationCurveGeom

        Iterator<RpGeometry> geomList = RPnDataModule.LEFTPHASESPACE.getGeomObjIterator();

        Color color = null;

        if (RPnPhaseSpaceAbstraction.namePhaseSpace.equals("RightPhase Space")) {
            geomList = RPnDataModule.LEFTPHASESPACE.getGeomObjIterator();
            color = Color.yellow;
        }
        if (RPnPhaseSpaceAbstraction.namePhaseSpace.equals("LeftPhase Space"))  {
            geomList = RPnDataModule.RIGHTPHASESPACE.getGeomObjIterator();
            color = Color.magenta;
        }

        int i = 0;

        while (geomList.hasNext()) {
            MultiGeometry geometry = geomList.next();
            //color = geometry.viewingAttr().getColor();

            if (geometry instanceof BifurcationCurveGeom) {
                BifurcationCurveGeom bifurcGeom = (BifurcationCurveGeom) geometry;

                Iterator it = bifurcGeom.getRealSegIterator();
                while(it.hasNext()) {
                    RealSegGeom geom = (RealSegGeom) it.next();

                    if (i == ind  &&  (GeometryGraph.count%2) == 0) geom.viewingAttr().setColor(Color.red);
                    if ((GeometryGraph.count%2) == 1) geom.viewingAttr().setColor(color);

                    i++;
                }

            }

        }

    }


    public void testAreaContains(Scene scene) {

        RPnPhaseSpacePanel panel = new RPnPhaseSpacePanel(scene);
        ViewingTransform transf = panel.scene().getViewingTransform();

        UserInputTable userInputList = UIController.instance().globalInputTable();
        RealVector newValue = userInputList.values();

        RPnPhaseSpaceAbstraction phaseSpace = RPnDataModule.PHASESPACE;
        RpGeometry geom = phaseSpace.findClosestGeometry(newValue);

        //RpGeometry geom = RPnPhaseSpaceAbstraction.findClosestGeometry(newValue);
        RPnCurve curve = (RPnCurve)(geom.geomFactory().geomSource());


        if (curve instanceof Orbit) {

            for (int i = 0; i < ((Orbit)curve).getPoints().length; i++) {

                CoordsArray wcCoordsCurve = new CoordsArray(((Orbit)curve).getPoints()[i]);
                Coords2D dcCoordsCurve = new Coords2D();
                transf.viewPlaneTransform(wcCoordsCurve, dcCoordsCurve);

                double xCurve = dcCoordsCurve.getElement(0);
                double yCurve = dcCoordsCurve.getElement(1);

                if (square1.contains(xCurve, yCurve)) {
                    indContido.add(i);
                    if (zerado == 2) {
                        zContido.add(((Orbit)curve).getPoints()[i].getElement(2));
                    } else {
                        zContido.add(((Orbit)curve).getPoints()[i].getElement(1));
                    }

                }

            }

        }


        if (curve instanceof SegmentedCurve) {

            ArrayList segments = (ArrayList) curve.segments();

            if (curve instanceof BifurcationCurve) {
                if (RPnPhaseSpaceAbstraction.namePhaseSpace.equals("RightPhase Space"))
                    segments = (ArrayList) ((BifurcationCurve)curve).rightSegments();

                if (RPnPhaseSpaceAbstraction.namePhaseSpace.equals("LeftPhase Space"))
                    segments = (ArrayList) ((BifurcationCurve)curve).leftSegments();
            }


            
            for (int i = 0; i < segments.size(); i++) {

                Coords2D dcCoordsCurve = new Coords2D();
                dcCoordsCurve = toDeviceCoords(scene, ((RealSegment) segments.get(i)).p1());
                double xCurve = dcCoordsCurve.getElement(0);
                double yCurve = dcCoordsCurve.getElement(1);

                Coords2D dcCoordsCurve2 = new Coords2D();
                dcCoordsCurve2 = toDeviceCoords(scene, ((RealSegment) segments.get(i)).p2());
                double xCurve2 = dcCoordsCurve2.getElement(0);
                double yCurve2 = dcCoordsCurve2.getElement(1);

                double xMed = (xCurve+xCurve2)*0.5;
                double yMed = (yCurve+yCurve2)*0.5;

                if (square1.contains(xMed, yMed)) {
                    indContido.add(i);
                }

            }

            if (curve instanceof BifurcationCurve) {
                secondArea((BifurcationCurve) curve, scene);
            }

        }

    }

    
    public void drawGrid(Graphics g, Scene scene) {
        RealVector maxCoords = RPNUMERICS.boundary().getMaximums();
        RealVector minCoords = RPNUMERICS.boundary().getMinimums();
        
        g.setColor(Color.gray);
        Graphics2D graph = (Graphics2D)g;
        
        int[] resolution = {1, 1};

        if (RPnPhaseSpaceAbstraction.listResolution.size()==1) RPnPhaseSpaceAbstraction.closestCurve=0;
        if (RPnPhaseSpaceAbstraction.listResolution.size()>0) resolution = (int[]) RPnPhaseSpaceAbstraction.listResolution.get(RPnPhaseSpaceAbstraction.closestCurve);

        int nv = resolution[0];
        int nu = resolution[1];
        
        double dv = (maxCoords.getElement(0)-minCoords.getElement(0))/nv;
        double du = (maxCoords.getElement(1)-minCoords.getElement(1))/nu;
        
        //*** desenha as linhas verticais
        for (int i = 0; i < nv; i++) {
            RealVector P1 = new RealVector(new double[]{i*dv + minCoords.getElement(0), maxCoords.getElement(1)});
            RealVector P2 = new RealVector(new double[]{i*dv + minCoords.getElement(0), minCoords.getElement(1)});
            
            Coords2D dcP1 = toDeviceCoords(scene, P1);
            Coords2D dcP2 = toDeviceCoords(scene, P2);
            
            linex = new Line2D.Double(dcP1.getX(), dcP1.getY(), dcP2.getX(), dcP2.getY());
            
            graph.draw(linex);
        }
        //*******************************

        //*** desenha as linhas horizontais
        for (int i = 0; i < nu; i++) {
            RealVector P1 = new RealVector(new double[]{minCoords.getElement(0), i*du + minCoords.getElement(1)});
            RealVector P2 = new RealVector(new double[]{maxCoords.getElement(0), i*du + minCoords.getElement(1)});

            Coords2D dcP1 = toDeviceCoords(scene, P1);
            Coords2D dcP2 = toDeviceCoords(scene, P2);

            liney = new Line2D.Double(dcP1.getX(), dcP1.getY(), dcP2.getX(), dcP2.getY());

            graph.draw(liney);
        }
        //*******************************

        //*** desenha as linhas obliquas
        for (int i = 0; i < (nu+nv); i++) {
            RealVector P1 = new RealVector(new double[]{minCoords.getElement(0), i * du + minCoords.getElement(1)});
            RealVector P2 = new RealVector(new double[]{i * dv + minCoords.getElement(0), minCoords.getElement(1)});

            Coords2D dcP1 = toDeviceCoords(scene, P1);
            Coords2D dcP2 = toDeviceCoords(scene, P2);

            lineObl = new Line2D.Double(dcP1.getX(), dcP1.getY(), dcP2.getX(), dcP2.getY());

            graph.draw(lineObl);
        }
        //*******************************
        
    }



}