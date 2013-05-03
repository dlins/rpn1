package rpnumerics;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import wave.util.RealVector;
import wave.util.RealSegment;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import rpn.RPnPhaseSpaceAbstraction;
import rpn.RPnUIFrame;
import rpn.component.RpCalcBasedGeomFactory;
import rpn.component.RpGeomFactory;
import rpn.component.RpGeometry;

public class HugoniotCurve extends SegmentedCurve {
    //
    // Members
    //

    private PhasePoint xZero_;
    private int direction_;
    
    private List<RealVector> transitionList_;

  
    
    
    public HugoniotCurve(PhasePoint xZero, List<HugoniotSegment> hSegments,List<RealVector> transitionList) {
        super(hSegments);

        xZero_ = new PhasePoint(xZero);
        direction_=Orbit.FORWARD_DIR;
        transitionList_=transitionList;
        
//        
//        for (RealVector realVector : transitionList) {
//            
//            System.out.println("Ponto de transicao em Java:"+realVector);
//            
//        }

    }
    public void setDirection(int direction){
        direction_=direction;
    }
    public int getDirection(){return direction_;}

    public static List interpolate(HugoniotPoint v1,
            HugoniotPoint v2) {
        List segments = new ArrayList();
        // dimension
        int m = v1.getSize();
        int negativeRealPartNoRight1 = v1.type().negativeRealPartNoRight();
        int zeroRealPartNoRight1 = v1.type().zeroRealPartNoRight();
        int positiveRealPartNoRight1 = v1.type().positiveRealPartNoRight();
        int negativeRealPartNoLeft1 = v1.type().negativeRealPartNoLeft();
        int zeroRealPartNoLeft1 = v1.type().zeroRealPartNoLeft();
        int positiveRealPartNoLeft1 = v1.type().positiveRealPartNoLeft();
        int negativeRealPartNoRight2 = v2.type().negativeRealPartNoRight();
        int zeroRealPartNoRight2 = v2.type().zeroRealPartNoRight();
        int positiveRealPartNoRight2 = v2.type().positiveRealPartNoRight();
        int negativeRealPartNoLeft2 = v2.type().negativeRealPartNoLeft();
        int zeroRealPartNoLeft2 = v2.type().zeroRealPartNoLeft();
        int positiveRealPartNoLeft2 = v2.type().positiveRealPartNoLeft();
        // number and direction of changes in the left state
        int leftStateChangesNo = 0;
        if ((positiveRealPartNoLeft2 - positiveRealPartNoLeft1 < 0)
                && ((positiveRealPartNoLeft2 - positiveRealPartNoLeft1)
                * (positiveRealPartNoLeft2 - positiveRealPartNoLeft1
                + zeroRealPartNoLeft2) > 0)
                || (positiveRealPartNoLeft2 - positiveRealPartNoLeft1 > 0)
                && ((positiveRealPartNoLeft2 - positiveRealPartNoLeft1)
                * (positiveRealPartNoLeft2 - positiveRealPartNoLeft1
                - zeroRealPartNoLeft1) > 0)) {
            // there are changes of type
            // determine how many
            if (positiveRealPartNoLeft2 - positiveRealPartNoLeft1 > 0) {
                leftStateChangesNo = positiveRealPartNoLeft2
                        - positiveRealPartNoLeft1
                        - zeroRealPartNoLeft1;
                positiveRealPartNoLeft1 += zeroRealPartNoLeft1;
                zeroRealPartNoLeft1 = 0;
            } else {
                leftStateChangesNo = positiveRealPartNoLeft2
                        + zeroRealPartNoLeft2
                        - positiveRealPartNoLeft1;
                negativeRealPartNoLeft1 += zeroRealPartNoLeft1;
                zeroRealPartNoLeft1 = 0;
            }
        } else {
            // no change of type
            // determine left type inside segment
            positiveRealPartNoLeft1 = Math.max(positiveRealPartNoLeft1,
                    positiveRealPartNoLeft2);
            negativeRealPartNoLeft1 = Math.max(negativeRealPartNoLeft1,
                    negativeRealPartNoLeft2);
            zeroRealPartNoLeft1 = m - positiveRealPartNoLeft1
                    - negativeRealPartNoLeft1;
        }
        // number and direction of changes in the right state
        int rightStateChangesNo = 0;
        if ((positiveRealPartNoRight2 - positiveRealPartNoRight1 < 0)
                && ((positiveRealPartNoRight2 - positiveRealPartNoRight1)
                * (positiveRealPartNoRight2 - positiveRealPartNoRight1
                + zeroRealPartNoRight2) > 0)
                || (positiveRealPartNoRight2 - positiveRealPartNoRight1 > 0)
                && ((positiveRealPartNoRight2 - positiveRealPartNoRight1)
                * (positiveRealPartNoRight2 - positiveRealPartNoRight1
                - zeroRealPartNoRight1) > 0)) {
            // there are changes of type
            // determine how many
            if (positiveRealPartNoRight2 - positiveRealPartNoRight1 > 0) {
                rightStateChangesNo = positiveRealPartNoRight2
                        - positiveRealPartNoRight1
                        - zeroRealPartNoRight1;
                positiveRealPartNoRight1 += zeroRealPartNoRight1;
                zeroRealPartNoRight1 = 0;
            } else {
                rightStateChangesNo = positiveRealPartNoRight2
                        + zeroRealPartNoRight2
                        - positiveRealPartNoRight1;
                negativeRealPartNoRight1 += zeroRealPartNoRight1;
                zeroRealPartNoRight1 = 0;
            }
        } else {
            // no change of type
            // determine right type inside segment
            positiveRealPartNoRight1 = Math.max(positiveRealPartNoRight1,
                    positiveRealPartNoRight2);
            negativeRealPartNoRight1 = Math.max(negativeRealPartNoRight1,
                    negativeRealPartNoRight2);
            zeroRealPartNoRight1 = m - positiveRealPartNoRight1
                    - negativeRealPartNoRight1;
        }
        // cutting and creating segments
        HugoniotPointType type;
        double alphaLeft = 0, alphaRight = 0;
        int dLeft = 0, dRight = 0;
        double sigma1 = v1.sigma();
        RealVector x1 = new RealVector(v1);
        RealVector x2 = new RealVector(v2);
        double sigma2Left = v2.sigma(), sigma2Right = v2.sigma();
        type = new HugoniotPointType(negativeRealPartNoLeft1,
                zeroRealPartNoLeft1,
                positiveRealPartNoLeft1,
                negativeRealPartNoRight1,
                zeroRealPartNoRight1,
                positiveRealPartNoRight1);
        int count = 0;
        while ((leftStateChangesNo != 0) || (rightStateChangesNo != 0)) {
            // find nearest change of left type
            if (leftStateChangesNo > 0) {
                dLeft = 1;
                alphaLeft = v1.eigenValRLeft().getElement(
                        negativeRealPartNoLeft1 - 1)
                        / (v1.eigenValRLeft().getElement(
                        negativeRealPartNoLeft1 - 1)
                        - v2.eigenValRLeft().getElement(
                        negativeRealPartNoLeft1 - 1));
                sigma2Left = (1 - alphaLeft) * v1.sigma()
                        + alphaLeft * v2.sigma();
            }
            if (leftStateChangesNo < 0) {
                dLeft = -1;
                alphaLeft = v1.eigenValRLeft().getElement(m
                        - positiveRealPartNoLeft1)
                        / (v1.eigenValRLeft().getElement(m
                        - positiveRealPartNoLeft1)
                        - v2.eigenValRLeft().getElement(m
                        - positiveRealPartNoLeft1));
                sigma2Left = (1 - alphaLeft) * v1.sigma()
                        + alphaLeft * v2.sigma();
            }
            if (leftStateChangesNo == 0) {
                dLeft = 0;
                alphaLeft = 1;
                sigma2Left = v2.sigma();
            }
            // find nearest change of right type
            if (rightStateChangesNo > 0) {
                dRight = 1;
                alphaRight = v1.eigenValRRight().getElement(
                        negativeRealPartNoRight1 - 1)
                        / (v1.eigenValRRight().getElement(
                        negativeRealPartNoRight1 - 1)
                        - v2.eigenValRRight().getElement(
                        negativeRealPartNoRight1 - 1));
                sigma2Right = (1 - alphaRight) * v1.sigma()
                        + alphaRight * v2.sigma();
            }
            if (rightStateChangesNo < 0) {
                dRight = -1;
                alphaRight = v1.eigenValRRight().getElement(m
                        - positiveRealPartNoRight1)
                        / (v1.eigenValRRight().getElement(m
                        - positiveRealPartNoRight1)
                        - v2.eigenValRRight().getElement(m
                        - positiveRealPartNoRight1));
                sigma2Right = (1 - alphaRight) * v1.sigma()
                        + alphaRight * v2.sigma();
            }
            if (rightStateChangesNo == 0) {
                dRight = 0;
                alphaRight = 1;
                sigma2Right = v2.sigma();
            }
            // create segment to the nearest change point
            if (alphaLeft < alphaRight) {
                x2.set(v1);
                x2.interpolate(v2, alphaLeft);
                if (!(x1.equals(x2)) || (sigma1 != sigma2Left)) {
                    segments.add(new HugoniotSegment(x1, sigma1, x2, sigma2Left,
                            type));
                }
                x1.set(x2);
                sigma1 = sigma2Left;
                leftStateChangesNo -= dLeft;
                negativeRealPartNoLeft1 -= dLeft;
                positiveRealPartNoLeft1 += dLeft;
                type = new HugoniotPointType(negativeRealPartNoLeft1,
                        zeroRealPartNoLeft1,
                        positiveRealPartNoLeft1,
                        negativeRealPartNoRight1,
                        zeroRealPartNoRight1,
                        positiveRealPartNoRight1);
            } else {
                x2.set(v1);
                x2.interpolate(v2, alphaRight);
                if (!(x1.equals(x2)) || (sigma1 != sigma2Right)) {
                    segments.add(new HugoniotSegment(x1, sigma1, x2,
                            sigma2Right, type));
                }
                x1.set(x2);
                sigma1 = sigma2Right;
                rightStateChangesNo -= dRight;
                negativeRealPartNoRight1 -= dRight;
                positiveRealPartNoRight1 += dRight;
                type = new HugoniotPointType(negativeRealPartNoLeft1,
                        zeroRealPartNoLeft1,
                        positiveRealPartNoLeft1,
                        negativeRealPartNoRight1,
                        zeroRealPartNoRight1,
                        positiveRealPartNoRight1);
            }
        }
        if (!(x1.equals(v2)) || (sigma1 != sigma2Left)) {
            segments.add(new HugoniotSegment(x1, sigma1, v2, v2.sigma(), type));
        }
        return segments;
    }
    
    
      public List<RealVector> getTransitionList() {
        return transitionList_;
    }
    

    @Override
        public RealVector findClosestPoint(RealVector targetPoint) {

        ArrayList segments = (ArrayList) segments();

        RealSegment closestSegment = (RealSegment) segments.get(findClosestSegment(targetPoint));

        //*** Acrescentei esse trecho em 07SET2012
        RealVector segmentVector = new RealVector(closestSegment.p1());
	segmentVector.sub(closestSegment.p2());
	RealVector temp = new RealVector(targetPoint);
	temp.sub(closestSegment.p2());
	double alpha = temp.dot(segmentVector)
                    / segmentVector.dot(segmentVector);

	if (alpha <= 0) {
            return closestSegment.p2();
        }
        if (alpha >= 1) {
            return closestSegment.p1();
        }
        //*****



        RealVector projVec = calcVecProj(closestSegment.p2(), targetPoint,
                closestSegment.p1());

        return projVec;

    }



    private static List hugoniotSegsFromWaveState(PhasePoint xZero, WaveState[] wStates) {

        ArrayList result = new ArrayList();

        int inputSize = wStates.length;
        for (int i = 0; i < inputSize - 1; i++) {
            // type is set...
            HugoniotPoint v1 = new HugoniotPoint(xZero,
                    wStates[i].finalState().
                    getCoords(),
                    wStates[i].speed());
            HugoniotPoint v2 = new HugoniotPoint(xZero,
                    wStates[i
                    + 1].finalState().getCoords(),
                    wStates[i + 1].speed());
            if ((v1.type().equals(v2.type()))) {
                result.add(new HugoniotSegment(v1, v1.sigma(), v2, v2.sigma(),
                        v1.type()));
            } else {
                List partCurve = interpolate(v1, v2);
                result.addAll(partCurve);
            }
        }

        return result;

    }

    private static List hugoniotSegsFromWaveState(PhasePoint xZero, List wStates) {

        ArrayList result = new ArrayList();

        int inputSize = wStates.size();
        for (int i = 0; i < inputSize - 1; i++) {
            // type is set...
            HugoniotPoint v1 = new HugoniotPoint(xZero,
                    ((WaveState) wStates.get(i)).finalState().
                    getCoords(),
                    ((WaveState) wStates.get(i)).speed());
            HugoniotPoint v2 = new HugoniotPoint(xZero,
                    ((WaveState) wStates.get(i + 1)).finalState().getCoords(), ((WaveState) wStates.get(i + 1)).speed());

            if ((v1.type().equals(v2.type()))) {
                result.add(new HugoniotSegment(v1, v1.sigma(), v2, v2.sigma(),
                        v1.type()));
            } else {
                List partCurve = interpolate(v1, v2);
                result.addAll(partCurve);
            }
        }

        return result;

    }

    private static List hugoniotSegsFromRealSegs(PhasePoint xZero_,
            List realSegs) {

        ArrayList result = new ArrayList();

        int inputSize = realSegs.size();
        for (int i = 0; i < inputSize; i++) {
            // type is set...

            HugoniotPoint v1 = new HugoniotPoint(xZero_,
                    ((RealSegment) realSegs.get(i)).p1());
            HugoniotPoint v2 = new HugoniotPoint(xZero_,
                    ((RealSegment) realSegs.get(i)).p2());
            if ((v1.type().equals(v2.type()))) {
                result.add(new HugoniotSegment(v1, v1.sigma(), v2, v2.sigma(),
                        v1.type()));
            } else {
                List partCurve = interpolate(v1, v2);
                result.addAll(partCurve);
            }
        }
        return result;
    }

    public double findSigma(PhasePoint targetPoint) {

        int alpha = 0;
        int hugoniotSegmentIndx = findClosestSegment(targetPoint);

        HugoniotSegment segment = (HugoniotSegment) segments().get(
                hugoniotSegmentIndx);

        return (segment.leftSigma() * (1 - alpha)
                + segment.rightSigma() * alpha);

    }

     public List findPoints(double sigma) {
        ArrayList points = new ArrayList();
        double alpha = 0;
        RealVector point = null;

        for (int i = 0; i < segments().size(); i++) {

            HugoniotSegment segment = (HugoniotSegment) segments().get(i);

            if ((sigma - segment.leftSigma()) * (sigma - segment.rightSigma())
                    <= 0) {
                alpha = (segment.leftSigma() - sigma)
                        / (segment.leftSigma() - segment.rightSigma());
                point = new RealVector(segment.leftPoint());
                point.interpolate(segment.rightPoint(), alpha);
                points.add(point);
            }
        }
        return points;
    }


    public double velocity(RealVector pMarca) {
        HugoniotSegment segment = (HugoniotSegment) (segments()).get(findClosestSegment(pMarca));
        double lSigma = segment.leftSigma();
        double rSigma = segment.rightSigma();

        RealVector u = new RealVector(RPNUMERICS.domainDim());
        u.sub(segment.rightPoint(), segment.leftPoint());

        RealVector v = new RealVector(RPNUMERICS.domainDim());
        v.sub(pMarca, segment.leftPoint());

        double normV = v.norm();
        double normU = u.norm();

        return (rSigma - lSigma) * normV / normU + lSigma;
    }
     

    public List<RealVector> equilPoints(RealVector pMarca) {

        List<RealVector> equil = new ArrayList();

        double velocity = velocity(pMarca);
        
        // inclui o Uref na lista de pontos de equilibrio
        RealVector pZero = getXZero();
        //equil.add(pZero);

        int sz = segments().size();
        for (int i = 0; i < sz; i++) {
            HugoniotSegment segment_ = (HugoniotSegment) segments().get(i);

            if ((segment_.leftSigma() <= velocity && segment_.rightSigma() >= velocity)
                    || (segment_.leftSigma() >= velocity && segment_.rightSigma() <= velocity)) {

                double lSigma_ = segment_.leftSigma();
                double rSigma_ = segment_.rightSigma();
                double lX_ = segment_.leftPoint().getElement(0);
                double rX_ = segment_.rightPoint().getElement(0);
                double lY_ = segment_.leftPoint().getElement(1);
                double rY_ = segment_.rightPoint().getElement(1);

                double X_ = (rX_ - lX_) * (velocity - lSigma_) / (rSigma_ - lSigma_) + lX_;
                double Y_ = (rY_ - lY_) * (velocity - lSigma_) / (rSigma_ - lSigma_) + lY_;
                RealVector p = new RealVector(RPNUMERICS.domainDim());
                p.setElement(0, X_);
                p.setElement(1, Y_);

                RealVector temp = new RealVector(RPNUMERICS.domainDim());
                temp.sub(pZero, p);

                if(temp.norm() > 0.01) equil.add(p);

                //if (p != pZero) {
                    //equil.add(p);
                //}

            }
        }

        System.out.println("Tamanho da lista equilPoints antes do return : " +equil.size());

        return equil;

    }


    public List<RealVector> equilPoints(double sigma) {

        List<RealVector> equil = new ArrayList();

        // inclui o Uref na lista de pontos de equilibrio
        RealVector pZero = getXZero();
        //equil.add(pZero);

        int sz = segments().size();
        for (int i = 0; i < sz; i++) {
            HugoniotSegment segment_ = (HugoniotSegment) segments().get(i);

            if ((segment_.leftSigma() <= sigma && segment_.rightSigma() >= sigma)
                    || (segment_.leftSigma() >= sigma && segment_.rightSigma() <= sigma)) {

                double lSigma_ = segment_.leftSigma();
                double rSigma_ = segment_.rightSigma();
                double lX_ = segment_.leftPoint().getElement(0);
                double rX_ = segment_.rightPoint().getElement(0);
                double lY_ = segment_.leftPoint().getElement(1);
                double rY_ = segment_.rightPoint().getElement(1);

                double X_ = (rX_ - lX_) * (sigma - lSigma_) / (rSigma_ - lSigma_) + lX_;
                double Y_ = (rY_ - lY_) * (sigma - lSigma_) / (rSigma_ - lSigma_) + lY_;
                RealVector p = new RealVector(2);
                p.setElement(0, X_);
                p.setElement(1, Y_);

                RealVector temp = new RealVector(2);
                temp.sub(pZero, p);

                if(temp.norm() > 0.01) equil.add(p);

                //if (p != pZero) {
                    //equil.add(p);
                //}

            }
        }

        return equil;

    }


    @Override
    public String toMatlabData2D(int identifier, RPnPhaseSpaceAbstraction phaseSpace) {

        StringBuffer buffer = new StringBuffer();

        int[] calcRes = {0, 0};
        Iterator<RpGeometry> geomList = phaseSpace.getGeomObjIterator();

        while (geomList.hasNext()) {
            RpGeometry geom = (RpGeometry) geomList.next();
            RpGeomFactory factory = geom.geomFactory();
            RPnCurve curve = (RPnCurve) factory.geomSource();
            if (curve==this) {
                RpCalcBasedGeomFactory geomFactory = (RpCalcBasedGeomFactory) factory;
                RpCalculation calc = geomFactory.rpCalc();
                ContourCurveCalc curveCalc = (ContourCurveCalc) calc;
                calcRes = curveCalc.getParams().getResolution();
            }
        }

        try {
            FileWriter gravador = new FileWriter(RPnUIFrame.dir + "/data" +identifier +".txt");
            BufferedWriter saida = new BufferedWriter(gravador);

            String direction = "Forward";
            if (getDirection() == Orbit.BACKWARD_DIR) {
                direction = "Backward";
            }
            saida.write("%% " + getClass().getSimpleName() + " InputPoint: " + getXZero() + " Resolution: " + calcRes[0] + " " + calcRes[1] + " Direction:" + direction + "\n");
            saida.write("%% xcoord1 ycoord1 xcoord2 ycoord2 firstPointShockSpeed secondPointShockSpeed leftEigenValue0 leftEigenValue1 rightEigenValue0 rightEigenValue1\n");

            for (int i = 0; i < segments().size(); i++) {
                HugoniotSegment hSegment = ((HugoniotSegment) segments().get(i));
                RealSegment rSegment = new RealSegment(hSegment.leftPoint(), hSegment.rightPoint());
                double leftSigma = hSegment.leftSigma();
                double rightSigma = hSegment.rightSigma();
                saida.write(rSegment.toString() + "   " + leftSigma + " " + rightSigma + " "
                              + hSegment.getLeftLambdaArray()[0] + " " + hSegment.getLeftLambdaArray()[1] + " "
                              + hSegment.getRightLambdaArray()[0] + " " + hSegment.getRightLambdaArray()[1] + "\n");
            }

            saida.close();

        } catch (IOException e) {
            System.out.println("Arquivos .txt de HugoniotCurve não foram escritos.");
        }

        return buffer.toString();
        
    }


    public String toXML() {
        StringBuilder buffer = new StringBuilder();

        for (int i = 0; i < segments().size(); i++) {
            HugoniotSegment hSegment = ((HugoniotSegment) segments().get(
                    i));
            buffer.append(hSegment.toXML());

        }

        return buffer.toString();

    }

    public PhasePoint getXZero() {
        return xZero_;
    }


    public String toMatlab(int curveIndex) {
        return null;
    }

}
