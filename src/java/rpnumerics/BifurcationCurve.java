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
import rpn.parser.RPnDataModule;
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
    private int jDC;

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
        
        if (RPnPhaseSpaceAbstraction.namePhaseSpace.equals(RPnDataModule.RIGHTPHASESPACE.getName()))
            segments = (ArrayList) leftSegments();

        if (RPnPhaseSpaceAbstraction.namePhaseSpace.equals(RPnDataModule.LEFTPHASESPACE.getName()))
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

        if (RPnPhaseSpaceAbstraction.namePhaseSpace.equals(RPnDataModule.RIGHTPHASESPACE.getName()))
            segments = (ArrayList) leftSegments();

        if (RPnPhaseSpaceAbstraction.namePhaseSpace.equals(RPnDataModule.LEFTPHASESPACE.getName()))
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

            //int jDC = 0;
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


    // --- Acrescentei este método em 20FEV
    private double minDist(List<RealVector> list, RealVector point) {
        double dist = 1E6;
        double dist2 = 0.;

        for (RealVector realVector : list) {
            dist2 = realVector.distance(point);
            if (dist2 < dist)
                dist = dist2;
        }

        return dist;
    }
    // ---


    // --- Alterando este método em 20FEV2013 : segunda versão
    public List<RealVector> correspondentPoints(RealVector pMarca) {

        List<RealVector> correspondent = new ArrayList();
        ArrayList segments = (ArrayList)segments();         // segments() retorna os segmentos da união (Left U Right)
        ArrayList toRestore = new ArrayList();
        ArrayList index = new ArrayList();

        // --- busca e trata a primeira correspondência
        int j = findClosestSegment(pMarca);
        RealVector p0 = secondPointDCOtherVersion(j);
        correspondent.add(p0);
        index.add(j);
        index.add(jDC);
        toRestore.add((RealSegment) segments().get(j));
        toRestore.add((RealSegment) segments().get(jDC));
        segments.removeAll(toRestore);
        // ---

        // --- mandar procurar quantos?
        int n = 5;

        // --- qual a distância adequada?
        double eps = 0.1;

        // --- busca e trata outras correspondências
        for (int k=0; k<n; k++) {

            int i = findClosestSegment(pMarca);
            RealVector p = secondPointDCOtherVersion(i);

            if (minDist(correspondent, p)>eps) {
                correspondent.add(p);
                index.add(i);
                index.add(jDC);
                toRestore.add((RealSegment) segments().get(i));
                toRestore.add((RealSegment) segments().get(jDC));
                segments.removeAll(toRestore);
            }

        }
        // ---

        // --- restaura a curva
        for (int k = 0; k < toRestore.size() / 2; k++) {
            int size = segments.size();

            if ((Integer)index.get(2*k)>=size/2) {
                segments.add(size -1, toRestore.get(2*k));
                segments.add(size/2 -1, toRestore.get(2*k + 1));
            }
            else {
                segments.add(size/2 -1, toRestore.get(2*k));
                segments.add(size -1, toRestore.get(2*k + 1));
            }

        }
        // ---

        return correspondent;
    }
    // ---

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
