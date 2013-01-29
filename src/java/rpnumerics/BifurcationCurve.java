/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import java.util.ArrayList;
import java.util.List;
import rpn.RPnPhaseSpaceAbstraction;
import rpn.controller.ui.UIController;
import rpnumerics.methods.contour.ContourCurve;

import wave.multid.view.ViewingAttr;
import wave.util.RealSegment;
import wave.util.RealVector;

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

    //*** Segunda versao: considera as curvas de bifurcacao como sendo formadas de dois conjuntos de geometrias (left e right)
    //*** Aqui, a correspondencia acontece de forma direta: left(i) <--> right(i)
    public RealVector secondPointDC(int i) {

        ArrayList segments = new ArrayList();
        
        if (RPnPhaseSpaceAbstraction.namePhaseSpace.equals("RightPhase Space"))
            segments = (ArrayList) leftSegments();

        if (RPnPhaseSpaceAbstraction.namePhaseSpace.equals("LeftPhase Space"))
            segments = (ArrayList) rightSegments();

        RealVector p1 = new RealVector(((RealSegment) (segments).get(i)).p1());
        RealVector p2 = new RealVector(((RealSegment) (segments).get(i)).p2());

        RealVector pDC = new RealVector(p1.getSize());
        pDC.sub(p1, p2);
        pDC.scale(getALFA());
        pDC.add(p2);

        return pDC;
    }


    // ----- Protótipo de versao única, para ficar em BifurcationCurve (16JAN2013)
    public RealVector secondPointDCOtherVersion(int i) {

        RealVector p1, p2, pDC = null;

        ArrayList segments = (ArrayList)segments();

        if (RPnPhaseSpaceAbstraction.namePhaseSpace.equals("RightPhase Space"))
            segments = (ArrayList) leftSegments();

        if (RPnPhaseSpaceAbstraction.namePhaseSpace.equals("LeftPhase Space"))
            segments = (ArrayList) rightSegments();


        if (UIController.instance().isAuxPanelsEnabled()) {
            p1 = new RealVector(((RealSegment) (segments).get(i)).p1());
            p2 = new RealVector(((RealSegment) (segments).get(i)).p2());

            pDC = new RealVector(p1.getSize());
            pDC.sub(p1, p2);
            pDC.scale(getALFA());
            pDC.add(p2);
        }
        else {
            int jDC = 0;
            if (i >= segments.size() / 2) {
                jDC = i - segments.size() / 2;
            } else {
                jDC = i + segments.size() / 2;
            }

            p1 = new RealVector(((RealSegment) (segments).get(jDC)).p1());
            p2 = new RealVector(((RealSegment) (segments).get(jDC)).p2());

            pDC = new RealVector(p1.getSize());
            pDC.sub(p1, p2);
            pDC.scale(getALFA());
            pDC.add(p2);
        }

        return pDC;
    }
    // -----


    // --- Acresecenti este método em 28JAN2013
    public List<RealVector> correspondentPoints(RealVector pMarca) {

        List<RealVector> correspondent = new ArrayList();
        ArrayList segments = (ArrayList)segments();         // segments() retorna os segmentos da união (Left U Right)
        ArrayList toRestore = new ArrayList();
        // --- Testar com 3; estabelecer critério
        int n = 3;
        int[] index = new int[n];

        for (int k=0; k<n; k++) {
            int i = findClosestSegment(pMarca);
            RealSegment realSeg = (RealSegment) segments().get(i);
            RealVector p = secondPointDCOtherVersion(i);
            correspondent.add(p);
            index[k] = i;
            if (k<(n-1)) {
                toRestore.add(realSeg);
                segments.remove(i);
            }
            
        }

        for (int k=0; k<(n-1); k++) {
            segments.add(index[k], toRestore.get(k));
        }

        return correspondent;
    }
    // ---


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
