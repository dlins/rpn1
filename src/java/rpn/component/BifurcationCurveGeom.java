/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import java.awt.Color;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import rpn.component.util.GraphicsUtil;
import rpn.component.util.Label;
import rpnumerics.BifurcationCurve;
import wave.multid.*;
import wave.multid.map.Map;
import wave.multid.model.AbstractPath;
import wave.multid.model.AbstractPathIterator;
import wave.multid.model.BoundingBox;
import wave.multid.model.MultiGeometry;
import wave.multid.model.MultiPolyLine;
import wave.multid.view.*;
import wave.util.RealSegment;
import wave.util.RealVector;

public class BifurcationCurveGeom implements MultiGeometry, RpGeometry {

    private RpGeomFactory factory_;
    private ViewingAttr viewingAttr_;
    private List<MultiPolyLine> segList_;
    private Space space_;
    private BoundingBox boundary_;
    private RpGeometry otherSide_;
    private List<GraphicsUtil> annotationsList_;

    public BifurcationCurveGeom(RealSegGeom[] segArray, BifurcationCurveGeomFactory factory) {

        viewingAttr_ = segArray[0].viewingAttr();
        annotationsList_ = new ArrayList<GraphicsUtil>();

//        System.out.println("Tamanho do array no construtor: "+segArray.length);
        segList_ = new ArrayList();
        for (int i = 0; i < segArray.length; i++) {
//            System.out.println("Segmento no array: "+segArray[i]);
            segList_.add(segArray[i]);
        }
        factory_ = factory;
        space_ = new Space("Auxiliar Space", rpnumerics.RPNUMERICS.domainDim());
        try {
            boundary_ = new BoundingBox(new CoordsArray(space_), new CoordsArray(space_));
        } catch (DimMismatchEx dex) {
            dex.printStackTrace();
        }

    }

    // ************************************************
    public Iterator getRealSegIterator() {
        return segList_.iterator();
    }
    // ************************************************

    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {
        return new BifurcationCurveView(this, transf, viewingAttr());
    }

    public void lowLight() {

        for (MultiPolyLine object : segList_) {
            object.lowLight();
        }

    }

    public List<MultiPolyLine> getSegList() {
        return segList_;
    }

    public void highLight() {
        for (MultiPolyLine object : segList_) {
            object.highLight();
        }

    }

    public RpGeomFactory geomFactory() {
        return factory_;
    }

    public AbstractPathIterator getPathIterator() {
        AbstractPath nullPath = new AbstractPath(getSpace());
        return nullPath.getPathIterator();
    }

    public AbstractPathIterator getPathIterator(Map map) throws DimMismatchEx {
        AbstractPath nullPath = new AbstractPath(getSpace());
        return nullPath.getPathIterator(map);
    }

    public ViewingAttr viewingAttr() {
        return viewingAttr_;
    }

    public RpGeometry getOtherSide() {
        return otherSide_;
    }

    public void setOtherSide(RpGeometry otherSide) {
        otherSide_ = otherSide;
    }

    public Iterator getBifurcationSegmentsIterator() {
        return segList_.iterator();
    }

    public BoundingBox getBoundary() {
        return boundary_;
    }

    public Space getSpace() {
        return space_;
    }

    //
    // Methods
    //
    public void applyMap(Map map) {
    }

    public void print(FileWriter cout) {
    }

    public void load(FileReader cin) {
    }

    @Override
    public void setVisible(boolean visible) {
        for (MultiPolyLine object : segList_) {
            object.setVisible(visible);
        }
    }

    @Override
    public void setSelected(boolean selected) {
        viewingAttr_.setSelected(selected);
    }

    @Override
    public boolean isVisible() {
        return viewingAttr_.isVisible();
    }

    @Override
    public boolean isSelected() {
        return viewingAttr_.isSelected();
    }

    @Override
    public void addAnnotation(GraphicsUtil annotation) {

        annotationsList_.add(annotation);

    }

    @Override
    public void clearAnnotations() {
        annotationsList_.clear();
    }

    @Override
    public Iterator<GraphicsUtil> getAnnotationIterator() {
        return annotationsList_.iterator();
    }

    @Override
    public void removeLastAnnotation() {
        if (!annotationsList_.isEmpty()) {
            annotationsList_.remove(annotationsList_.size() - 1);
        }
    }

    @Override
    public void removeAnnotation(GraphicsUtil selectedAnnotation) {
        annotationsList_.remove(selectedAnnotation);
    }

    @Override
    public void showSpeed(CoordsArray curvePoint, CoordsArray wcPoint, ViewingTransform transform) {

    }

    @Override
    public void showClassification(CoordsArray curvePoint, CoordsArray wcPoint, ViewingTransform transform) {

    }

    public void showCorrespondentPoint(CoordsArray curvePoint, CoordsArray wcPoint, ViewingTransform transform) {

        RealVector pointOnDomain = new RealVector(wcPoint.getCoords());

        BifurcationCurve bifurcationCurve = (BifurcationCurve) factory_.geomSource();

        List<RealSegment> segments = bifurcationCurve.segments();

        ClosestDistanceCalculator closestCalculator = new ClosestDistanceCalculator(segments, pointOnDomain);

        int segmentIndex = closestCalculator.getSegmentIndex();

        int midPoint = segments.size() - bifurcationCurve.leftSegments().size() - 1;

        System.out.println("Tamanho total " + segments.size());

        System.out.println("Indice do segmento mais proximo do ponto: " + segmentIndex);
        RealSegment leftSegment = null;
        RealSegment rightSegment = null;
        if (segmentIndex < bifurcationCurve.leftSegments().size() - 1) {
            System.out.println("O segmento esta na parte esquerda");
            int rightIndex = midPoint + segmentIndex;
            System.out.println("Correspondente direito: " + rightIndex);
            leftSegment = segments.get(segmentIndex);
            rightSegment = segments.get(rightIndex);
        } else {
            System.out.println("O segmento na parte direita");

            int leftIndex = segmentIndex - midPoint;
            System.out.println("Correspondente esquerdo: " + leftIndex);
            rightSegment = segments.get(segmentIndex);
            leftSegment = segments.get(leftIndex);
        }

        RealVector mark1 = ClosestDistanceCalculator.convexCombination(leftSegment.p1(), leftSegment.p2(), closestCalculator.getAlpha());
        RealVector mark2 = ClosestDistanceCalculator.convexCombination(rightSegment.p1(), rightSegment.p2(), closestCalculator.getAlpha());

        List<Object> wcObjectsLeft = new ArrayList();

        wcObjectsLeft.add(mark1);
        wcObjectsLeft.add("A");

        Label testeLabelLeft = new Label(wcObjectsLeft, transform, new ViewingAttr(Color.white));
        
        addAnnotation(testeLabelLeft);
        

         List<Object> wcObjectsRight = new ArrayList();

        wcObjectsRight.add(mark2);
        wcObjectsRight.add("B");
        
        
        Label testeLabelRight = new Label(wcObjectsRight, transform, new ViewingAttr(Color.white));

        addAnnotation(testeLabelRight);
//        int segmentIndex = 0;
//
//        for (int i = 0; i < segments.size(); i++) {
//
//            if (segments.get(i).p1().equals(pointOnCurve)) {
//
//                segmentIndex = i;
//            }
//
//        }
//
//        MultiPolyLine thisSegment = getSegList().get(segmentIndex);
//
//        thisSegment.viewingAttr().setColor(Color.CYAN);
//
//        RpGeometry otherGeometry = getOtherSide();
//
//        BifurcationCurveGeom otherBifurcationCurveGeom = (BifurcationCurveGeom) otherGeometry;
//        MultiPolyLine otherSegmentGeom = null;
//        for (int i = 0; i < otherBifurcationCurveGeom.getSegList().size(); i++) {
//
//            otherSegmentGeom = otherBifurcationCurveGeom.getSegList().get(segmentIndex);
//
//        }
//
//        otherSegmentGeom.viewingAttr().setColor(Color.green);
    }

}
