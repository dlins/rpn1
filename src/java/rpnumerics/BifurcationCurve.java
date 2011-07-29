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

public class BifurcationCurve extends SegmentedCurve {
    //
    // Members
    //

    private int familyIndex_;
    private List leftSegments_;// TODO It is necessary ?
    private List rightSegments_;//TODO It is necessary ?
//    private List segments;

    //
    // Constructor
    public BifurcationCurve(List<HugoniotSegment> leftList, List<HugoniotSegment> rightList) {

        super(createSingleSegmentList(leftList, rightList));
       
        leftSegments_ = leftList;
        rightSegments_ = rightList;
    }

    public BifurcationCurve(List<HugoniotSegment> singleList) {

        super(singleList);
        
        leftSegments_ = singleList;
        rightSegments_ = singleList;
    }

    public BifurcationCurve(int familyIndex, ContourCurve curve, ViewingAttr viewingAttr) {

        super(new ArrayList());

        familyIndex_ = familyIndex;
        // segments = MultidAdapter.converseCoordsArrayToRealSegments(MultidAdapter.converseRPnCurveToCoordsArray(this));
//        segments = MultidAdapter.converseRPnCurveToRealSegments(this);
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
        int i = 0;
        for (HugoniotSegment hugoniotSegment : leftSeg) {
            hugoniotSegment.setIntType(19);
            i++;
        }
        for (HugoniotSegment hugoniotSegment : rightSeg) {
            hugoniotSegment.setIntType(18);
        }

        if (leftSeg.addAll(rightSeg)) {

            return leftSeg;
        } else {
            return null;
        }

//        List<HugoniotSegment> mergedList = new ArrayList<HugoniotSegment>();
//
//        for (int i = 0; i < rightSeg.size(); i++) {
//            HugoniotSegment hSegmentRight = rightSeg.get(i);
//            HugoniotSegment hSegmentLeft = leftSeg.get(i);
//
//            RealVector leftPoint = new RealVector(hSegmentLeft.p1().getSize()*2);
//
//            for (int j = 0; j < hSegmentLeft.p1().getSize(); j++) {
//                leftPoint.setElement(j, hSegmentLeft.p1().getElement(j));
//
//            }
//
//            for (int j = 0; j < hSegmentRight.p1().getSize();j++){
//                leftPoint.setElement(j+hSegmentRight.p1().getSize(), hSegmentRight.p1().getElement(j));
//            }
//
//
//            RealVector rightPoint = new RealVector(hSegmentRight.p2().getSize() * 2);
//
//             for (int j = 0; j < hSegmentRight.p2().getSize(); j++) {
//                rightPoint.setElement(j, hSegmentLeft.p2().getElement(j));
//
//            }
//
//            for (int j = 0; j < hSegmentRight.p2().getSize();j++){
//                rightPoint.setElement(j+hSegmentRight.p2().getSize(), hSegmentRight.p2().getElement(j));
//            }


//
//
//
//
//
//
//            RealVector leftPoint = new RealVector(hSegmentLeft.p1().toString()+hSegmentRight.p1().toString());
//            System.out.println(leftPoint);
//            RealVector rightPoint = new RealVector(hSegmentLeft.p2().toString() + hSegmentRight.p2().toString());
//            System.out.println(hSegmentLeft.p2().toString() + hSegmentRight.p2().toString());
//            System.out.println(rightPoint);
//            HugoniotSegment mergedSegment = new HugoniotSegment(leftPoint, 0.0, rightPoint, 0.0, 16); // TODO TESTE !!!
//
//            mergedList.add(mergedSegment);
//        }
//
//
//        return mergedList;


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
