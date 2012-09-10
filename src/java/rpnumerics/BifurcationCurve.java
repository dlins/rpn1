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


    private List leftSegments_;
    private List rightSegments_;

    //
    // Constructor



     public BifurcationCurve(List<RealSegment> leftList, List<RealSegment> rightList) {

        super(createSingleSegmentList(leftList, rightList));

        leftSegments_ = leftList;
        rightSegments_ = rightList;

        System.out.println("CTOR de BifurcationCurve");

    }



    public BifurcationCurve(List<? extends RealSegment> singleList) {
        
        super(singleList);
        leftSegments_ = singleList;
        rightSegments_ = singleList;
    }


    

    /** @deprecated
     *
     */

    public BifurcationCurve(int familyIndex, ContourCurve curve, ViewingAttr viewingAttr) {

        super(new ArrayList());

     
    }

    //
    // Accessors/Mutators
    //
    public String toXML() {

        StringBuilder buffer = new StringBuilder();

        for (int i = 0; i < segments().size(); i++) {

            RealSegment realSegment =(RealSegment) segments().get(i);
            buffer.append(realSegment.toXML());
        }
        return buffer.toString();

    }

    public List<RealSegment> leftSegments() {
        return leftSegments_;
    }

    public List<RealSegment> rightSegments() {
        return rightSegments_;
    }

    private static List createSingleSegmentList(List<RealSegment> leftSeg, List<RealSegment> rightSeg) {
    
        ArrayList<RealSegment> returned = new ArrayList<RealSegment>();

        returned.addAll(leftSeg);
        returned.addAll(rightSeg);

        return returned;




//        int i = 0;

//        for (HugoniotSegment hugoniotSegment : leftSeg) {
//            hugoniotSegment.setIntType(19);
////            i++;
//        }
//        for (HugoniotSegment hugoniotSegment : rightSeg) {
//            hugoniotSegment.setIntType(18);
//        }


//
////        List<HugoniotSegment> mergedList = new ArrayList<HugoniotSegment>();
//
//        for (int i = 0; i < rightSeg.size(); i++) {
//            HugoniotSegment hSegmentRight = rightSeg.get(i);
//            HugoniotSegment hSegmentLeft = leftSeg.get(i);
//
//            RealVector leftPoint = new RealVector(hSegmentLeft.p1().getSize() * 2);
//
//        for (HugoniotSegment hugoniotSegment : leftSeg) {
//            hugoniotSegment.setIntType(16);
//            i++;
//
//            for (int j = 0; j < hSegmentLeft.p1().getSize(); j++) {
//                leftPoint.setElement(j, hSegmentLeft.p1().getElement(j));
//
//            }
//
//            for (int j = 0; j < hSegmentRight.p1().getSize(); j++) {
//                leftPoint.setElement(j + hSegmentRight.p1().getSize(), hSegmentRight.p1().getElement(j));
//            }
//
//
//            RealVector rightPoint = new RealVector(hSegmentRight.p2().getSize() * 2);
//
//            for (int j = 0; j < hSegmentRight.p2().getSize(); j++) {
//                rightPoint.setElement(j, hSegmentLeft.p2().getElement(j));
//
//            }
//
//            for (int j = 0; j < hSegmentRight.p2().getSize(); j++) {
//                rightPoint.setElement(j + hSegmentRight.p2().getSize(), hSegmentRight.p2().getElement(j));
//            }
//            System.out.println(leftPoint + "  " + rightPoint);
////            RealVector rightPoint = new RealVector(hSegmentLeft.p2().toString() + hSegmentRight.p2().toString());
////            System.out.println(hSegmentLeft.p2().toString() + hSegmentRight.p2().toString());
////            System.out.println(rightPoint);
//
//        }
//
////            RealVector leftPoint = new RealVector(hSegmentLeft.p1().toString() + hSegmentRight.p1().toString());


        
    }
}
