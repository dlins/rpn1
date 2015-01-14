/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import rpn.component.util.CorrespondenceMark;
import rpnumerics.BifurcationCurve;
import wave.multid.*;
import wave.multid.model.MultiGeometry;
import wave.multid.view.*;
import wave.util.RealSegment;
import wave.util.RealVector;

public class BifurcationCurveGeom extends BifurcationCurveBranchGeom implements MultiGeometry{

    private final List<BifurcationCurveBranchGeom> bifurcationGeomBranches_;

    public BifurcationCurveGeom(List<BifurcationCurveBranchGeom> branches, BifurcationCurveGeomFactory factory) {
        super(factory);
        bifurcationGeomBranches_ = branches;
    }

   

    @Override
    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {
        return new BifurcationCurveView(this, transf, viewingAttr());
    }

    public void addBranch(BifurcationCurveGeom branch) {
        bifurcationGeomBranches_.add(branch);
    }
   
   
    @Override
    public void showSpeed(CoordsArray curvePoint, CoordsArray wcPoint, ViewingTransform transform) {

    }

    @Override
    public void showClassification(CoordsArray curvePoint, CoordsArray wcPoint, ViewingTransform transform) {

    }

    @Override
    public void showCorrespondentPoint(CoordsArray curvePoint,  ViewingTransform transform) {

        if (getCorrespondenceDirection() != CorrespondenceDirection.NONE.ordinal()) {
            RealVector pointOnCurve = new RealVector(curvePoint.getCoords());

            BifurcationCurve bifurcationCurve = (BifurcationCurve) geomFactory().geomSource();

            List<RealSegment> thisSegments = null;
            List<RealSegment> otherSegments = null;

            if (getCorrespondenceDirection() == CorrespondenceDirection.LEFTRIGHT.ordinal()) {
                thisSegments = bifurcationCurve.leftSegments();
                otherSegments = bifurcationCurve.rightSegments();
            }

            if (getCorrespondenceDirection()== CorrespondenceDirection.RIGHTLEFT.ordinal()) {
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
        else {
            clearCorrespondenceMarks();
        }

    }

    @Override
    public List<BifurcationCurveBranchGeom> getBifurcationListGeom() {
        return bifurcationGeomBranches_;
    }

   

}
