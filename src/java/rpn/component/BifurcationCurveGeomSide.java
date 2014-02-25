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
import rpn.component.util.CorrespondenceMark;
import rpn.component.util.GraphicsUtil;
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

public class BifurcationCurveGeomSide extends  BifurcationCurveBranchGeom implements MultiGeometry{

    private final RpGeomFactory factory_;
    private ViewingAttr viewingAttr_;
    private List<MultiPolyLine> segList_;
    private Space space_;
    private BoundingBox boundary_;
    private final List<GraphicsUtil> annotationsList_;
    private final List<BifurcationCurveBranchGeom> bifurcationGeomBranches_;
    
    private BifurcationCurveGeomSide otherSide_;

    public BifurcationCurveGeomSide(RealSegGeom[] segArray, BifurcationCurveGeomFactory factory) {

        viewingAttr_ = segArray[0].viewingAttr();
        annotationsList_ = new ArrayList<GraphicsUtil>();
        bifurcationGeomBranches_ = new ArrayList<BifurcationCurveBranchGeom>();
        segList_ = new ArrayList();
        for (int i = 0; i < segArray.length; i++) {
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
    public BifurcationCurveGeomSide getOtherSide() {
        return otherSide_;
    }

    public void setOtherSide (BifurcationCurveGeomSide otherSide) {
        otherSide_ = otherSide;
    }

    
    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {
        return new BifurcationCurveSideView(this, transf, viewingAttr());
    }

    public List<MultiPolyLine> getSegList() {
        return segList_;
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

    public void showCorrespondentPoint(CoordsArray curvePoint, ViewingTransform transform) {

        RealVector pointOnDomain = new RealVector(curvePoint.getCoords());
        
        BifurcationCurveGeomFactory factory = (BifurcationCurveGeomFactory)factory_;

        List<RealSegment> thisSegments = factory.segmentsList(this);

        ClosestDistanceCalculator closestCalculator = new ClosestDistanceCalculator(thisSegments, pointOnDomain);

        int segmentIndex = closestCalculator.getSegmentIndex();
        
        RealSegment thisSegment = thisSegments.get(segmentIndex);

        RealVector thisMark = ClosestDistanceCalculator.convexCombination(thisSegment.p1(), thisSegment.p2(), closestCalculator.getAlpha());
        
        RealSegment otherSideSegment = factory.segmentsList(getOtherSide()).get(segmentIndex);
        RealVector otherSideMark = ClosestDistanceCalculator.convexCombination(otherSideSegment.p1(), otherSideSegment.p2(), closestCalculator.getAlpha());

        List<Object> wcObjectsLeft = new ArrayList();

        wcObjectsLeft.add(thisMark);

        CorrespondenceMark testeLabelLeft = new CorrespondenceMark(wcObjectsLeft, transform, new ViewingAttr(Color.white));

        addAnnotation(testeLabelLeft);

        List<Object> wcObjectsRight = new ArrayList();

        wcObjectsRight.add(otherSideMark);

        CorrespondenceMark testeLabelRight = new CorrespondenceMark(wcObjectsRight, transform, new ViewingAttr(Color.white));
        getOtherSide().removeLastAnnotation();
        getOtherSide().addAnnotation(testeLabelRight);



    }

    @Override
    public List<BifurcationCurveBranchGeom> getBifurcationListGeom() {

        if (!bifurcationGeomBranches_.contains(this)) {
            bifurcationGeomBranches_.add(this);
        }

        return bifurcationGeomBranches_;
    }

   

}
