/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rpn.component.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import rpn.RPnPhaseSpaceAbstraction;
import rpn.RPnPhaseSpacePanel;
import rpn.component.BifurcationCurveGeom;
import rpn.component.RealSegGeom;
import rpn.component.RpGeometry;
import rpnumerics.RPNUMERICS;
import wave.multid.Coords2D;
import wave.multid.CoordsArray;
import wave.multid.view.Scene;
import wave.multid.view.ViewingTransform;
import wave.util.RealSegment;
import wave.util.RealVector;
import rpn.parser.RPnDataModule;
import rpnumerics.BifurcationCurve;
import wave.multid.model.MultiGeometry;

/**
 *
 * @author moreira
 */
public class GeometryGraphND {

    protected Line2D line3;
    protected Line2D line4;
    protected Line2D line3DC;
    protected Line2D line4DC;
    protected Line2D line3WC;
    protected Line2D line4WC;
    static public RealVector topRight = new RealVector(RPNUMERICS.domainDim());
    static public RealVector downLeft = new RealVector(RPNUMERICS.domainDim());
    static public RealVector pMarca = new RealVector(RPNUMERICS.domainDim());
    static public RealVector pMarcaDC = new RealVector(RPNUMERICS.domainDim());
    static public RealVector pMarcaWC = new RealVector(RPNUMERICS.domainDim());
    static public RealVector targetPoint = new RealVector(RPNUMERICS.domainDim());
    static public RealVector cornerRet = new RealVector(RPNUMERICS.domainDim());
    //*** isso é uma lista de indices (Leandro)
    static public List indContido = new ArrayList();
    
    protected Color cor34 = null, corString = null;
    protected Line2D.Double linex, liney, lineObl;
    public static int mostraGrid = 0;
    public static int mapToEqui = 0;
    public static int onCurve = 0;



    public static void clearpMarca() {
        for (int i=0; i<GeometryGraphND.pMarca.getSize(); i++) {
            GeometryGraphND.pMarca.setElement(i, 100.);
        }
    }

    public void changeColor() {
        if (RPnPhaseSpacePanel.DEFAULT_BACKGROUND_COLOR == Color.white) {
            cor34 = Color.red;
            corString = Color.black;
        }
        if (RPnPhaseSpacePanel.DEFAULT_BACKGROUND_COLOR == Color.black) {
            cor34 = Color.yellow;
            corString = Color.white;
        }
    }


    public Coords2D toDeviceCoords(Scene scene, RealVector point) {
        int dim = Math.max((scene.getAbstractGeom()).getSpace().getDim(), RPNUMERICS.domainDim());
        point.setSize(dim);
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


    public List<RealVector> secondArea(BifurcationCurve curve, Scene scene) {
        
        ArrayList segments = new ArrayList();

        if (RPnPhaseSpaceAbstraction.namePhaseSpace.equals("RightPhase Space"))
            segments = (ArrayList) ((BifurcationCurve)curve).leftSegments();

        if (RPnPhaseSpaceAbstraction.namePhaseSpace.equals("LeftPhase Space"))
            segments = (ArrayList) ((BifurcationCurve)curve).rightSegments();

        double zmin = 1.E10;
        double zmax = -1.E10;
        double wmin = 1.E10;
        double wmax = -1.E10;

        System.out.println("GeometryGraphND.indContido.size() ::: " +GeometryGraphND.indContido.size());

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

        RealVector pMin = new RealVector(new double[]{zmin, wmin});
        RealVector pMax = new RealVector(new double[]{zmax, wmax});

        Coords2D dc_pMin = toDeviceCoords(scene, pMin);
        Coords2D dc_pMax = toDeviceCoords(scene, pMax);

        List<RealVector> list = new ArrayList<RealVector>();

        list.add(new RealVector(new double[]{dc_pMin.getX(), dc_pMin.getY()}));
        list.add(new RealVector(new double[]{dc_pMax.getX(), dc_pMax.getY()}));

        return list;
    }


    public void lightTest(int ind) {       //*** ALterei BifurcationCurveGeom

        Iterator<RpGeometry> geomList = RPnDataModule.LEFTPHASESPACE.getGeomObjIterator();

        Color color = null;
        Color otherColor = null;

        if (RPnPhaseSpaceAbstraction.namePhaseSpace.equals("RightPhase Space")) {
            geomList = RPnDataModule.LEFTPHASESPACE.getGeomObjIterator();
            color = Color.yellow;
            otherColor = Color.magenta;
        }
        if (RPnPhaseSpaceAbstraction.namePhaseSpace.equals("LeftPhase Space"))  {
            geomList = RPnDataModule.RIGHTPHASESPACE.getGeomObjIterator();
            color = Color.magenta;
            otherColor = Color.yellow;
        }


        int i = 0;
        while (geomList.hasNext()) {
            MultiGeometry geometry = geomList.next();
            
            if (geometry instanceof BifurcationCurveGeom) {
                BifurcationCurveGeom bifurcGeom = (BifurcationCurveGeom) geometry;
                Iterator it = bifurcGeom.getRealSegIterator();
                while(it.hasNext()) {
                    RealSegGeom geom = (RealSegGeom) it.next();
                    if ((GeometryGraph.count%2) == 0) geom.viewingAttr().setColor(color);
                    if (i == ind  &&  (GeometryGraph.count%2) == 1) geom.viewingAttr().setColor(otherColor);
                    i++;
                }

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
            P1.setSize(RPNUMERICS.domainDim());
            P2.setSize(RPNUMERICS.domainDim());
            
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
            P1.setSize(RPNUMERICS.domainDim());
            P2.setSize(RPNUMERICS.domainDim());

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
            P1.setSize(RPNUMERICS.domainDim());
            P2.setSize(RPNUMERICS.domainDim());

            Coords2D dcP1 = toDeviceCoords(scene, P1);
            Coords2D dcP2 = toDeviceCoords(scene, P2);

            lineObl = new Line2D.Double(dcP1.getX(), dcP1.getY(), dcP2.getX(), dcP2.getY());

            graph.draw(lineObl);
        }
        //*******************************
        
    }



}