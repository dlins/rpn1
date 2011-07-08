/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import java.util.ArrayList;
import java.util.List;
import rpnumerics.methods.contour.ContourCurve;

import wave.multid.CoordsArray;
import wave.multid.view.ViewingAttr;
import wave.util.RealSegment;
import wave.util.RealVector;

public class BifurcationCurve extends SegmentedCurve {
    //
    // Members
    //

    private int familyIndex_;
    private List leftSegments_;
    private List rightSegments_;
//    private List segments;

    //
    // Constructor
    //
//    public BifurcationCurve(int familyIndex, ArrayList states) {
//        super(coordsArrayFromRealSegments(states), new ViewingAttr(Color.white));
//
//        familyIndex_ = familyIndex;
//        segments = states;
//    }
    public BifurcationCurve(List<HugoniotSegment> leftList, List<HugoniotSegment> rightList) {

        super(createSingleSegmentList(leftList, rightList));
        leftSegments_ = leftList;
        rightSegments_ = rightList;
    }

    public BifurcationCurve(int familyIndex, ContourCurve curve, ViewingAttr viewingAttr) {

        super(new ArrayList());

        familyIndex_ = familyIndex;
        // segments = MultidAdapter.converseCoordsArrayToRealSegments(MultidAdapter.converseRPnCurveToCoordsArray(this));
//        segments = MultidAdapter.converseRPnCurveToRealSegments(this);
    }

    @Override
    public int findClosestSegment(RealVector targetPoint, double alpha) {
//        RealVector target = new RealVector(targetPoint);
//        RealVector closest = null;
//        RealVector segmentVector = null;
//        alpha = 0;
//        int closestSegment = 0;
//        double closestDistance = -1;
//
//        List bifurcationSegment = segments();
//        for (int i = 0; i < segments.size(); i++) {
//
//            RealSegment segment = (RealSegment) bifurcationSegment.get(i);
//            segmentVector = new RealVector(segment.p1());
//            segmentVector.sub(segment.p2());
//
//            closest = new RealVector(target);
//            closest.sub(segment.p2());
//
//
//
//            alpha = closest.dot(segmentVector) / segmentVector.dot(segmentVector);

//
//            System.out.println("Numerador: " + closest.dot(segmentVector));
//            System.out.println("Denominador: " + closest.dot(segmentVector));
//
//
//            System.out.println("Dentro de findClosestSegment:" + alpha);

//            if (alpha < 0) {
//                alpha = 0;
//            }
//            if (alpha > 1) {
//                alpha = 1;
//            }
//            segmentVector.scale(alpha);
//            closest.sub(segmentVector);
//            if ((closestDistance < 0) || (closestDistance > closest.norm())) {
//                closestSegment = i;
//                closestDistance = closest.norm();
//            }
//        }
//
//
//        return closestSegment;
        return 0;
    }

    //
    // Accessors/Mutators
    //
    public int getFamilyIndex() {
        return familyIndex_;
    }

    public String toXML() {
        StringBuffer buffer = new StringBuffer();

        for (int i = 0; i < segments().size(); i++) {
                RealSegment rSegment = (RealSegment) segments().get(i);
                buffer.append(rSegment.toXML());
            }

        return buffer.toString();

    }

    public List<HugoniotSegment> leftSegments() {
        return leftSegments_;
    }

    public List<HugoniotSegment> rightSegments() {
        return rightSegments_;
    }

    private static List createSingleSegmentList(List<HugoniotSegment> leftSeg, List<HugoniotSegment> rightSeg) {
//        int i = 0;


//
//        for (HugoniotSegment hugoniotSegment : leftSeg) {
//            hugoniotSegment.setIntType(16);
////            System.out.println("Segmento : " + i + " " + hugoniotSegment);
//
////            i++;
//
//
//        }
//        for (HugoniotSegment hugoniotSegment : rightSeg) {
//            hugoniotSegment.setIntType(15);
//        }
//
//        if (leftSeg.addAll(rightSeg)) {
//
//            return leftSeg;
//        } else {
//            return null;
//        }

        List<HugoniotSegment> mergedList = new ArrayList<HugoniotSegment>();

        for (int i = 0; i < rightSeg.size(); i++) {
            HugoniotSegment hSegmentRight = rightSeg.get(i);
            HugoniotSegment hSegmentLeft = leftSeg.get(i);

            RealVector leftPoint = new RealVector(hSegmentLeft.p1().toString()+hSegmentRight.p1().toString());
            System.out.println(leftPoint);
            RealVector rightPoint = new RealVector(hSegmentLeft.p2().toString() + hSegmentRight.p2().toString());
//            System.out.println(hSegmentLeft.p2().toString() + hSegmentRight.p2().toString());
            System.out.println(rightPoint);
            HugoniotSegment mergedSegment = new HugoniotSegment(leftPoint, 0.0, rightPoint, 0.0, 16); // TODO TESTE !!!

            mergedList.add(mergedSegment);
        }


        return mergedList;


    }

    private static CoordsArray[] coordsArrayFromRealSegments(List segments) {

        ArrayList tempCoords = new ArrayList(segments.size());
        for (int i = 0; i < segments.size(); i++) {
            RealSegment segment = (RealSegment) segments.get(i);
            tempCoords.add(new CoordsArray(segment.p1()));
            tempCoords.add(new CoordsArray(segment.p2()));

        }

        CoordsArray[] coords = new CoordsArray[tempCoords.size()];
        for (int i = 0; i < tempCoords.size(); i++) {
            coords[i] = (CoordsArray) tempCoords.get(i);
        }
        tempCoords = null;
        return coords;

    }
}
