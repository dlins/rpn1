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
            HugoniotSegment hugoniotSegment = (HugoniotSegment) segments().get(i);
            RealSegment realSegment = new RealSegment(hugoniotSegment.p1(), hugoniotSegment.p2());
            buffer.append(realSegment.toXML());
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

    }
}
