/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import java.awt.Shape;
import rpnumerics.methods.contour.ContourCurve;
import wave.multid.*;
import wave.multid.model.AbstractSegment;
import wave.multid.model.AbstractSegmentAtt;
import wave.multid.model.MultiPolyLine;
import wave.multid.model.RelaxedChainedPolylineSet;
import wave.multid.model.SegmentAlreadyInList;
import wave.multid.model.SegmentCiclesPolyline;
import wave.multid.model.SegmentDegradesPolyline;
import wave.multid.model.WrongNumberOfDefPointsEx;
import wave.multid.view.*;
import wave.util.*;
import java.util.ArrayList;
import java.util.List;
import rpn.RPnPhaseSpaceAbstraction;
import rpn.component.RpGeometry;
import rpn.parser.RPnDataModule;

//public abstract class RpCurve extends MultiPolyLine {
public abstract class RPnCurve implements RpSolution {

    private RelaxedChainedPolylineSet polyLinesSetList_ = null;
    private ViewingAttr viewAttr = null;
    private double ALFA;
    //** declarei isso (Leandro)
    public double distancia = 0;

    public List claToRemove = new ArrayList();
    public List velToRemove = new ArrayList();
    public List claStringToRemove = new ArrayList();
    public List velStringToRemove = new ArrayList();
    private int id_;


    public RPnCurve() {//TODO REMOVE !!
        //super(new CoordsArray[3], new ViewingAttr(Color.WHITE));
    }

    public RPnCurve(PointNDimension[][] polyline, ViewingAttr viewAttr) {
        //super(fromPointNDimensionCurveToSegment(polyline), viewAttr);

        try {
            polyLinesSetList_ = new RelaxedChainedPolylineSet(polyline);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.viewAttr = viewAttr;
    }

    public RPnCurve(CoordsArray[] vertices, ViewingAttr viewAttr) {

        PointNDimension[][] polyline = new PointNDimension[1][vertices.length];

        for (int pont_point = 0; pont_point < vertices.length; pont_point++) {
            polyline[0][pont_point] = new PointNDimension(vertices[pont_point]);
        }

        try {
            polyLinesSetList_ = new RelaxedChainedPolylineSet(polyline);
        } catch (Exception e) {
        }

        this.viewAttr = viewAttr;


    }

    public RPnCurve(AbstractSegment[] segments, ViewingAttr viewAttr) {
        //super(segments, viewAttr);
        // converter para chained
    }

//    @Override
//    public String toString() {
//        //return getPath().toString();
//    }
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
        RPnDataModule.PHASESPACE.update();

        System.out.println("Tamanho da curva depois da remocao : " + curve.segments().size());


//        for (int i = 0; i < GeometryUtil.targetPoint.getSize(); i++) {        // Pode ser útil na hora de fazer inclusao dos novos segmentos (para nao serem eliminados)
//            GeometryUtil.cornerRet.setElement(i, 0);
//            GeometryUtil.targetPoint.setElement(i, 0.);
//        }

    }
    //********

//    //-------------------------------------------- Parece que não é mais usado
//    public RealVector projectionCurve(RpCurve curve, RealVector targetPoint) {
//
//        int segmentIndex = curve.findClosestSegment(targetPoint);
//
//        RealVector closestPoint = curve.findClosestPoint(targetPoint);
//
//
//        if (curve instanceof BifurcationCurve) {
//
//            BifurcationCurve bifurcationCurve = (BifurcationCurve) curve;
//
//            //2D Processing
//            RealSegment closest2DSegment = (RealSegment) bifurcationCurve.segments().get(segmentIndex);
//
//
//            RealVector PP1 = new RealVector(2);
//            RealVector PP2 = new RealVector(2);
//
//            PP1.sub(closestPoint, closest2DSegment.p1());
//            PP2.sub(closest2DSegment.p2(), closestPoint);
//
//            double K = PP1.norm() / PP2.norm();
//
//            //this curve processing
//            BifurcationCurve bifurcationNDCurve = (BifurcationCurve) this;
//
//            RealSegment closestSegment = (RealSegment) bifurcationNDCurve.segments().get(segmentIndex);
//
//            RealVector x1 = new RealVector(closestSegment.p1());
//            RealVector x2 = new RealVector(closestSegment.p2());
//
//            for (int i = 0; i < bifurcationNDCurve.getSpace().getDim(); i++) {
//
//                x2.scale(K);
//                x1.sub(x2);
//
//                x1.scale(1 - K);
//
//            }
//
//            return x1;
//
//        }
//
//
//        return null;
//    }
//    --------------------------------------------------------------------------

    private static AbstractSegment[] fromPointNDimensionCurveToSegment(
            PointNDimension[][] polyline) {
        int numberOfPolylines = polyline.length;

        int numberOfSegments = 0;
        for (int pont_polyline = 0; pont_polyline < numberOfPolylines;
                pont_polyline++) {
            numberOfSegments++;
            numberOfSegments += (polyline[pont_polyline].length - 1);
        }

        AbstractSegment[] segments = new AbstractSegment[numberOfSegments];

        int segmentCount = 0;

        try {

            for (int pont_polyline = 0; pont_polyline < numberOfPolylines;
                    pont_polyline++) {

                CoordsArray[] segmentPoints = new CoordsArray[2];
                segmentPoints[0] = polyline[pont_polyline][0].toCoordsArray();
                segmentPoints[1] = new CoordsArray(segmentPoints[0].getSpace());
                segments[segmentCount] = new AbstractSegment(segmentPoints,
                        new AbstractSegmentAtt(AbstractSegment.SEG_MOVETO));
                segmentCount++;

                int size = polyline[pont_polyline].length;
                for (int pont_point = 1; pont_point < size; pont_point++) {
                    segmentPoints[1] = polyline[pont_polyline][pont_point].toCoordsArray();
                    segments[segmentCount] = new AbstractSegment(segmentPoints,
                            new AbstractSegmentAtt(AbstractSegment.SEG_LINETO));
                    segmentCount++;
                    segmentPoints[0] = segmentPoints[1];
                }

            }

        } catch (WrongNumberOfDefPointsEx e) {
            e.printStackTrace();
        }

        return segments;
    }

    public RPnCurve(ContourCurve curve, ViewingAttr viewingAttr) {

        //super(RpCurve.fromPointNDimensionCurveToSegment(curve.getCurve()), viewingAttr);

        viewAttr = viewingAttr;
        try {
            polyLinesSetList_ = new RelaxedChainedPolylineSet(curve.getCurve());
            //TODO Construtor para usar com o contour
        } catch (Exception ex) {

            ex.printStackTrace();

        }

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
        
//        System.out.println("tamanho em findClose: "+segments.size());

        
        if (this instanceof BifurcationCurve) {

            if (RPnPhaseSpaceAbstraction.namePhaseSpace.equals(RPnDataModule.RIGHTPHASESPACE.getName()))
                segments = (ArrayList) ((BifurcationCurve)this).rightSegments();

            if (RPnPhaseSpaceAbstraction.namePhaseSpace.equals(RPnDataModule.LEFTPHASESPACE.getName()))
                segments = (ArrayList) ((BifurcationCurve)this).leftSegments();
        }


        RealVector target = new RealVector(targetPoint);
        RealVector closest = null;
        RealVector segmentVector = null;
        double alpha = 0;
        int closestSegment = 0;
        double closestDistance = -1;

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
            alpha = closest.dot(segmentVector)
                    / segmentVector.dot(segmentVector);

            if (alpha < 0) {
                alpha = 0;
            }
            if (alpha > 1) {
                alpha = 1;
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

            if ((closestDistance < 0) || (closestDistance > closest.norm())) {
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

        RPnPhaseSpaceAbstraction phaseSpace = RPnDataModule.PHASESPACE;

        RpGeometry geom = phaseSpace.findClosestGeometry(targetPoint);
        RPnCurve curve = (RPnCurve) (geom.geomFactory().geomSource());
        ArrayList segments = (ArrayList) curve.segments();

        if (this instanceof BifurcationCurve) {

            if (RPnPhaseSpaceAbstraction.namePhaseSpace.equals("Right Phase Space"))
                segments = (ArrayList) ((BifurcationCurve)this).rightSegments();

            if (RPnPhaseSpaceAbstraction.namePhaseSpace.equals("Left Phase Space"))
                segments = (ArrayList) ((BifurcationCurve)this).leftSegments();
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

//    private void updateMultiPolyline() {
//        (this.getPath()).reset();
//
//        PointNDimension[][] polyline = polyLinesSetList_.getPolylines();
//        int numberOfPolylines = polyline.length;
//
//        try {
//            for (int pont_polyline = 0; pont_polyline < numberOfPolylines;
//                    pont_polyline++) {
//
//                CoordsArray[] segmentPoints = new CoordsArray[2];
//                segmentPoints[0] = polyline[pont_polyline][0].toCoordsArray();
//                segmentPoints[1] = new CoordsArray(segmentPoints[0].getSpace());
//                super.append(new AbstractSegment(segmentPoints,
//                        new AbstractSegmentAtt(AbstractSegment.SEG_MOVETO)), false);
//
//                int size = polyline[pont_polyline].length;
//                for (int pont_point = 1; pont_point < size; pont_point++) {
//                    segmentPoints[1] = polyline[pont_polyline][pont_point].toCoordsArray();
//                    super.append(new AbstractSegment(segmentPoints,
//                            new AbstractSegmentAtt(AbstractSegment.SEG_LINETO)), false);
//                    segmentPoints[0] = segmentPoints[1];
//                }
//
//            }
//        } catch (WrongNumberOfDefPointsEx e) {
//            e.printStackTrace();
//        } catch (DimMismatchEx dex) {
//            dex.printStackTrace();
//        }
//
//    }
    public PointNDimension[] getPolyLineAt(int index) {
        PointNDimension[][] polyline = polyLinesSetList_.getPolylines();
        return polyline[index];
    }

    public PointNDimension[][] getPolylines() {
        return polyLinesSetList_.getPolylines();
    }

    public int getNumberOfPolylines() {
        return polyLinesSetList_.size();
    }

    public MultiPolyLine getMultidPolylineAt(int index, ViewingAttr attributes) {

        PointNDimension[][] polyline = polyLinesSetList_.getPolylines();

        int size = polyline[index].length;
        
        CoordsArray[] coordsPolyline = new CoordsArray[size];

        for (int pont_point = 0; pont_point < size; pont_point++) {
            coordsPolyline[pont_point] = polyline[index][pont_point].toCoordsArray();
        }

        return new MultiPolyLine(coordsPolyline, attributes);
    }

//    @Override
//    public void append(AbstractPathIterator toAppend, boolean connect) throws
//            DimMismatchEx {
//        // nao sei como implementar
//    }
//    @Override
//    public void append(AbstractSegment toAppend, boolean connect) throws
//            DimMismatchEx {
//        CoordsArray[] definitionPoints = toAppend.getDefinitionPoints();
//        PointNDimension point1 = new PointNDimension(definitionPoints[0]);
//        PointNDimension point2 = new PointNDimension(definitionPoints[1]);
//
//        try {
//            polyLinesSetList_.addSegment(point1, point2);
//        } catch (Exception e) {
//            return;
//        }
//
//        updateMultiPolyline();
//    }
    public void append(PointNDimension point1, PointNDimension point2) {
        try {
            polyLinesSetList_.addSegment(point1, point2);
        } catch (Exception e) {
            return;
        }

        //updateMultiPolyline();
    }

    public void append(PointNDimension[] polyline) throws SegmentAlreadyInList,
            SegmentDegradesPolyline,
            SegmentCiclesPolyline {
        try {
            polyLinesSetList_.addPolyline(polyline);
        } catch (SegmentAlreadyInList e) {
            throw e;
        } catch (SegmentDegradesPolyline e) {
            throw e;
        } catch (SegmentCiclesPolyline e) {
            throw e;
        }

        //updateMultiPolyline();
    }

    // remover segmento e polylines
    public ViewingAttr getViewAttr() {
        return this.viewAttr;
    }

    abstract public List<RealSegment> segments();

    public String toXML() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String toMatlab(int curveIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
