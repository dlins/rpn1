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
import rpnumerics.BifurcationCurve;
import wave.multid.*;
import wave.multid.map.Map;
import wave.multid.model.AbstractPath;
import wave.multid.model.AbstractPathIterator;
import wave.multid.model.BoundingBox;
import wave.multid.model.MultiGeometry;
import wave.multid.view.*;
import wave.util.RealSegment;
import wave.util.RealVector;

public class BifurcationCurveGeom extends BifurcationCurveBranchGeom implements MultiGeometry{

    private RpGeomFactory factory_;
    private ViewingAttr viewingAttr_;
    private Space space_;
    private BoundingBox boundary_;
//    private RpGeometry otherSide_;

    private final List<BifurcationCurveBranchGeom> bifurcationGeomBranches_;

    public BifurcationCurveGeom(List<BifurcationCurveBranchGeom> branches, BifurcationCurveGeomFactory factory) {

        viewingAttr_ = new ViewingAttr(Color.white);

        bifurcationGeomBranches_ = branches;
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
        return null;
    }
    // ************************************************

    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {
        return new BifurcationCurveView(this, transf, viewingAttr());
    }

    public void addBranch(BifurcationCurveGeom branch) {
        bifurcationGeomBranches_.add(branch);
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

//    public RpGeometry getOtherSide() {
//        return otherSide_;
//    }
//
//    public void setOtherSide(RpGeometry otherSide) {
//        otherSide_ = otherSide;
//    }
//    public Iterator getBifurcationSegmentsIterator() {
//        return segList_.iterator();
//    }
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
        for (BifurcationCurveBranchGeom object : getBifurcationListGeom()) {
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
    public void showSpeed(CoordsArray curvePoint, CoordsArray wcPoint, ViewingTransform transform) {

    }

    @Override
    public void showClassification(CoordsArray curvePoint, CoordsArray wcPoint, ViewingTransform transform) {

    }

    @Override
    public void showCorrespondentPoint(CoordsArray curvePoint,  ViewingTransform transform) {

        if (getCorrespondenceDirection() != BifurcationCurveBranchGeom.NONE) {
            RealVector pointOnCurve = new RealVector(curvePoint.getCoords());

            BifurcationCurve bifurcationCurve = (BifurcationCurve) factory_.geomSource();

            List<RealSegment> thisSegments = null;
            List<RealSegment> otherSegments = null;

            if (getCorrespondenceDirection() == BifurcationCurveBranchGeom.LEFTRIGHT) {
                thisSegments = bifurcationCurve.leftSegments();
                otherSegments = bifurcationCurve.rightSegments();
            }

            if (getCorrespondenceDirection()== BifurcationCurveBranchGeom.RIGHTLEFT) {
                thisSegments = bifurcationCurve.rightSegments();
                otherSegments = bifurcationCurve.leftSegments();
            }

            ClosestDistanceCalculator closestCalculator = new ClosestDistanceCalculator(thisSegments, pointOnCurve);

            int segmentIndex = closestCalculator.getSegmentIndex();

            RealSegment leftSegment = thisSegments.get(segmentIndex);
            RealSegment rightSegment = otherSegments.get(segmentIndex);

            RealVector mark1 = ClosestDistanceCalculator.convexCombination(leftSegment.p1(), leftSegment.p2(), closestCalculator.getAlpha());
            RealVector mark2 = ClosestDistanceCalculator.convexCombination(rightSegment.p1(), rightSegment.p2(), closestCalculator.getAlpha());

            List<Object> wcObjectsLeft = new ArrayList();

            wcObjectsLeft.add(mark1);

            CorrespondenceMark testeLabelLeft = new CorrespondenceMark(wcObjectsLeft, transform, new ViewingAttr(Color.white));

            addAnnotation(testeLabelLeft);

            List<Object> wcObjectsRight = new ArrayList();

            wcObjectsRight.add(mark2);

            CorrespondenceMark testeLabelRight = new CorrespondenceMark(wcObjectsRight, transform, new ViewingAttr(Color.white));

            addAnnotation(testeLabelRight);
        }

    }

    @Override
    public List<BifurcationCurveBranchGeom> getBifurcationListGeom() {
        return bifurcationGeomBranches_;
    }

}
