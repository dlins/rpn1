/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import java.awt.Shape;
import wave.multid.model.RelaxedChainedPolylineSet;
import wave.multid.view.*;
import wave.util.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import rpn.RPnPhaseSpaceAbstraction;
import rpn.parser.RPnDataModule;


//public abstract class RpCurve extends MultiPolyLine {
public abstract class RPnCurve implements RpSolution {

    public static String XML_TAG = "CURVE";
    private RelaxedChainedPolylineSet polyLinesSetList_ = null;
    private List<? extends RealSegment> segments_;
    private ViewingAttr viewAttr = null;
    private double ALFA;
    //** declarei isso (Leandro)
    public double distancia = 0.;
    public List claToRemove = new ArrayList();
    public List velToRemove = new ArrayList();
    public List claStringToRemove = new ArrayList();
    public List velStringToRemove = new ArrayList();
    private int id_;
    private HyperOctree<RealSegment> octree_;
    
    private OrbitPoint referencePoint_;

    public RPnCurve() {//TODO REMOVE !!
    }

    public RPnCurve(List<? extends RealSegment> segments) {

        segments_ = segments;

        Boundary boundary = rpnumerics.RPNUMERICS.boundary();

        BoxND box = new BoxND(boundary.getMinimums(), boundary.getMaximums());

        octree_ = new HyperOctree(box, 3, 100);

        for (int i = 0; i < segments.size(); i++) {
            RealSegment realSegment = segments.get(i);

            octree_.add(realSegment);

        }

    }
    
    
    public List<RealSegment> segmentsInside(BoxND box){
        Vector<RealSegment> realSegmentsInside = new Vector();
            octree_.within_box(box, realSegmentsInside);
            
            return realSegmentsInside;
    }

    public boolean intersect(BoxND box) {
        Vector<RealSegment> realSegmentsInside = new Vector();
        octree_.within_box(box, realSegmentsInside);
        if (realSegmentsInside.isEmpty()) {
            return false;
        }
        return true;

    }

    //******** Era usado no refinamento local
    public static void remove(SegmentedCurve curve, List indexList, Shape square, double zmin, double zmax, Scene scene) {    // tentar colocar isso na classe SegmentedCurve.java

        System.out.println("Tamanho de indexList : " + indexList.size());
        System.out.println("Tamanho da curva antes da remocao : " + curve.segments().size());

        List segRem = new ArrayList();

        for (int i = 0; i < indexList.size(); i++) {
            int ind = Integer.parseInt((indexList.get(i)).toString());
            segRem.add(curve.segments().get(ind));
        }

        curve.segments().removeAll(segRem);

        scene.update();
        RPnDataModule.updatePhaseSpaces();

        System.out.println("Tamanho da curva depois da remocao : " + curve.segments().size());


//        for (int i = 0; i < GeometryUtil.targetPoint.getSize(); i++) {        // Pode ser útil na hora de fazer inclusao dos novos segmentos (para nao serem eliminados)
//            GeometryUtil.cornerRet.setElement(i, 0);
//            GeometryUtil.targetPoint.setElement(i, 0.);
//        }

    }


    public double getALFA() {
        return ALFA;
    }

    public int findClosestSegment(RealVector targetPoint) {

        //*** A conversão abaixo não retorna número certo de segmentos se a curva é SegmentedCurve.
        //ArrayList segments = MultidAdapter.converseCoordsArrayToRealSegments(MultidAdapter.converseRPnCurveToCoordsArray(this));

        //*** Aqui, o problema continua
        //ArrayList segments = MultidAdapter.converseRPnCurveToRealSegments(this);


        ArrayList segments = (ArrayList) segments();

        if (this instanceof BifurcationCurve) {

            if (RPnPhaseSpaceAbstraction.namePhaseSpace.equals(RPnDataModule.RIGHTPHASESPACE.getName())) {
                segments = (ArrayList) ((BifurcationCurve) this).rightSegments();
            }

            if (RPnPhaseSpaceAbstraction.namePhaseSpace.equals(RPnDataModule.LEFTPHASESPACE.getName())) {
                segments = (ArrayList) ((BifurcationCurve) this).leftSegments();
            }
        }

        RealVector target = new RealVector(targetPoint);
        RealVector closest = null;
        RealVector segmentVector = null;
        double alpha = 0.;
        int closestSegment = 0;
        double closestDistance = -1.;

        for (int i = 0; i < segments.size(); i++) {

            RealSegment segment = (RealSegment) segments.get(i);
            segmentVector = new RealVector(segment.p1());
            segmentVector.sub(segment.p2());

            //------------------------------------------
            //** para calcular na projecao
//            for (int k = 0; k < target.getSize(); k++) {
//                if (target.getElement(k) == 0.) {
//                    segmentVector.setElement(k, 0.);
//                }
//            }
            //------------------------------------------

            closest = new RealVector(target);

            closest.sub(segment.p2());

            if (segmentVector.norm() != 0.) {
                alpha = closest.dot(segmentVector)
                        / segmentVector.dot(segmentVector);
            } else {
                alpha = 0.;
            }

            if (alpha <= 0) {
                alpha = 0.;
            }
            if (alpha >= 1) {
                alpha = 1.;
            }
            segmentVector.scale(alpha);
            closest.sub(segmentVector);

            //------------------------------------------
            //** para calcular na projecao
            for (int k = 0; k < target.getSize(); k++) {
                if (target.getElement(k) == 0.) {
                    closest.setElement(k, 0.);
                }
            }
            //------------------------------------------

            if ((closestDistance < 0.) || (closestDistance > closest.norm())) {
                closestSegment = i;
                closestDistance = closest.norm();
                ALFA = alpha;
            }
        }

        distancia = closestDistance;

        return closestSegment;
    }

    public int getId() {
        return id_;
    }

    public void setId(int id) {
        id_ = id;
    }

    public RealVector findClosestPoint(RealVector targetPoint) {

//        RPnPhaseSpaceAbstraction phaseSpace = RPnDataModule.PHASESPACE;
//
//        RpGeometry geom = phaseSpace.findClosestGeometry(targetPoint);
//        RPnCurve curve = (RPnCurve) (geom.geomFactory().geomSource());
        ArrayList segments = (ArrayList) segments();

        if (this instanceof BifurcationCurve) {

            if (RPnPhaseSpaceAbstraction.namePhaseSpace.equals(RPnDataModule.RIGHTPHASESPACE.getName())) {
                segments = (ArrayList) ((BifurcationCurve) this).rightSegments();
            }

            if (RPnPhaseSpaceAbstraction.namePhaseSpace.equals(RPnDataModule.LEFTPHASESPACE.getName())) {
                segments = (ArrayList) ((BifurcationCurve) this).leftSegments();
            }
        }

        RealSegment closestSegment = (RealSegment) segments.get(findClosestSegment(targetPoint));

        if (ALFA <= 0) {
            return closestSegment.p2();
        }
        if (ALFA >= 1) {
            return closestSegment.p1();
        }

        RealVector projVec = calcVecProj(closestSegment.p2(), targetPoint,
                closestSegment.p1());

        return projVec;

    }

    public RealVector calcVecProj(RealVector a, RealVector b, RealVector o) {

        // Va = a - o
        // Vb = b - o
        final RealVector VoNeg = new RealVector(o);
        VoNeg.negate();
        final RealVector Va = new RealVector(a);
        final RealVector Vb = new RealVector(b);

        Va.scaleAdd(1, a, VoNeg);
        Vb.scaleAdd(1, b, VoNeg);

        double dotVaVb = Va.dot(Vb);
        double normVa = Va.norm();

        Va.scale(dotVaVb / Math.pow(normVa, 2));

        RealVector Vproj = new RealVector(Va.getSize());

        Vproj.add(Va, o);

        return Vproj;

    }

    public PointNDimension[][] getPolylines() {
        return polyLinesSetList_.getPolylines();
    }

    public List<RealSegment> segments() {
        return (List<RealSegment>) segments_;
    }

    public OrbitPoint getReferencePoint() throws RpException{
        
        if (referencePoint_==null)
            throw  new RpException("No reference point available");
        else
        
        return referencePoint_;
    }

    public void setReferencePoint(OrbitPoint referencePoint_) {
        this.referencePoint_ = referencePoint_;
    }
    
    
    

    
    public String toXML() {
        return "";
    }

    public String toMatlab(int curveIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
