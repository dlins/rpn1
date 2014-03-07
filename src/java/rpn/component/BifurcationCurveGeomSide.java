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

public class BifurcationCurveGeomSide extends BifurcationCurveBranchGeom implements MultiGeometry {

    private List<MultiPolyLine> segList_;

    private final List<BifurcationCurveBranchGeom> bifurcationGeomBranches_;

    private BifurcationCurveGeomSide otherSide_;

    public BifurcationCurveGeomSide(RealSegGeom[] segArray, BifurcationCurveGeomFactory factory) {
        super(factory);

        bifurcationGeomBranches_ = new ArrayList<BifurcationCurveBranchGeom>();
        segList_ = new ArrayList();
        for (int i = 0; i < segArray.length; i++) {
            segList_.add(segArray[i]);
        }

    }

    public BifurcationCurveGeomSide getOtherSide() {
        return otherSide_;
    }

    public void setOtherSide(BifurcationCurveGeomSide otherSide) {
        otherSide_ = otherSide;
    }

    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {
        return new BifurcationCurveSideView(this, transf, viewingAttr());
    }

    public List<MultiPolyLine> getSegList() {
        return segList_;
    }

    @Override
    public void showSpeed(CoordsArray curvePoint, CoordsArray wcPoint, ViewingTransform transform) {

    }

    @Override
    public void showClassification(CoordsArray curvePoint, CoordsArray wcPoint, ViewingTransform transform) {

    }

    public void showCorrespondentPoint(CoordsArray curvePoint, ViewingTransform transform) {

        RealVector pointOnDomain = new RealVector(curvePoint.getCoords());

        BifurcationCurveGeomFactory factory = (BifurcationCurveGeomFactory) geomFactory();

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
    public void setVisible(boolean visible) {
        viewingAttr().setVisible(visible);
        for (MultiPolyLine multiPolyLine : segList_) {
            multiPolyLine.setVisible(visible);
        }

    }
    
    
    @Override
    public void setSelected(boolean selected) {
        viewingAttr().setSelected(selected);
        for (MultiPolyLine multiPolyLine : segList_) {
            multiPolyLine.setSelected(selected);
        }
    }

    @Override
    public List<BifurcationCurveBranchGeom> getBifurcationListGeom() {

        if (!bifurcationGeomBranches_.contains(this)) {
            bifurcationGeomBranches_.add(this);
        }

        return bifurcationGeomBranches_;
    }

}
