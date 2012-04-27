package rpnumerics;

import wave.util.RealVector;
import wave.util.RealSegment;
import java.util.List;
import java.util.ArrayList;

public class HugoniotCurve extends SegmentedCurve {
    //
    // Members
    //

    private PhasePoint xZero_;
    
    public HugoniotCurve(PhasePoint xZero, List<HugoniotSegment> hSegments) {
        super(hSegments);

        xZero_ = new PhasePoint(xZero);


    }

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


    //****************************
    public double velocity(HugoniotSegment segment, RealVector pMarca) {
        double lSigma = segment.leftSigma();
        double rSigma = segment.rightSigma();
        double lX = segment.leftPoint().getElement(0);
        double rX = segment.rightPoint().getElement(0);
        double X = pMarca.getElement(0);
        
        return ((rSigma - lSigma) * (X - lX) / (rX - lX) + lSigma);
    }

    //****************************
    public List<RealVector> equilPoints(RealVector pMarca) {

        List<RealVector> equil = new ArrayList();

        HugoniotSegment segment = (HugoniotSegment) (segments()).get(findClosestSegment(pMarca));    //tirei as referencias externas
        double velocity = velocity(segment, pMarca);

        // inclui o Uref na lista de pontos de equilibrio
        //RealVector pZero = getXZero();
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
                RealVector p = new RealVector(2);
                p.setElement(0, X_);
                p.setElement(1, Y_);

                //if (p != pZero) {
                    equil.add(p);
                //}

            }
        }

        return equil;

    }
    //****************************


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
}
