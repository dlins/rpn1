/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import wave.util.RealVector;
import wave.util.RealSegment;
import java.util.List;
import wave.multid.CoordsArray;
import java.util.ArrayList;
import wave.multid.view.ViewingAttr;
import java.awt.Color;
import rpn.component.MultidAdapter;

public class HugoniotCurve extends RPnCurve implements RpSolution {
    //
    // Members
    //
    private PhasePoint xZero_;
    private List hugoniotSegments_;

    // the list is filled with RealSegments...
    public HugoniotCurve(PhasePoint xZero, List realSegs) {
        super(coordsArrayFromRealSegments(realSegs), new ViewingAttr(Color.red));
        xZero_ = new PhasePoint(xZero);
        hugoniotSegments_ = hugoniotSegsFromRealSegs(xZero_, realSegs);

    }

    public HugoniotCurve(PhasePoint xZero, WaveState[] states) {
        super(coordsArrayFromRealSegments(HugoniotCurve.hugoniotSegsFromWaveState(xZero,
                states)), new ViewingAttr(Color.red));

        xZero_ = new PhasePoint(xZero);
        hugoniotSegments_ = HugoniotCurve.hugoniotSegsFromWaveState(xZero,
                states);

    }

    public HugoniotCurve(PhasePoint xZero, ArrayList states) {
        super(coordsArrayFromRealSegments(HugoniotCurve.hugoniotSegsFromWaveState(xZero,
                states)), new ViewingAttr(Color.red));

        xZero_ = new PhasePoint(xZero);
        hugoniotSegments_ = HugoniotCurve.hugoniotSegsFromWaveState(xZero,
                states);

    }
    
    public HugoniotCurve(PhasePoint xZero,CoordsArray [] coords) {
        
        super(coords, new ViewingAttr(Color.RED));
        
        List realSegments = MultidAdapter.converseCoordsArrayToRealSegments(coords);
        hugoniotSegments_ = hugoniotSegsFromRealSegs(xZero, realSegments);
        
        xZero_=new PhasePoint(xZero);
        
        
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
        if ((positiveRealPartNoLeft2 - positiveRealPartNoLeft1 < 0) &&
                ((positiveRealPartNoLeft2 - positiveRealPartNoLeft1) *
                (positiveRealPartNoLeft2 - positiveRealPartNoLeft1 +
                zeroRealPartNoLeft2) > 0) ||
                (positiveRealPartNoLeft2 - positiveRealPartNoLeft1 > 0) &&
                ((positiveRealPartNoLeft2 - positiveRealPartNoLeft1) *
                (positiveRealPartNoLeft2 - positiveRealPartNoLeft1 -
                zeroRealPartNoLeft1) > 0)) {
            // there are changes of type
            // determine how many
            if (positiveRealPartNoLeft2 - positiveRealPartNoLeft1 > 0) {
                leftStateChangesNo = positiveRealPartNoLeft2 -
                        positiveRealPartNoLeft1 -
                        zeroRealPartNoLeft1;
                positiveRealPartNoLeft1 += zeroRealPartNoLeft1;
                zeroRealPartNoLeft1 = 0;
            } else {
                leftStateChangesNo = positiveRealPartNoLeft2 +
                        zeroRealPartNoLeft2 -
                        positiveRealPartNoLeft1;
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
            zeroRealPartNoLeft1 = m - positiveRealPartNoLeft1 -
                    negativeRealPartNoLeft1;
        }
        // number and direction of changes in the right state
        int rightStateChangesNo = 0;
        if ((positiveRealPartNoRight2 - positiveRealPartNoRight1 < 0) &&
                ((positiveRealPartNoRight2 - positiveRealPartNoRight1) *
                (positiveRealPartNoRight2 - positiveRealPartNoRight1 +
                zeroRealPartNoRight2) > 0) ||
                (positiveRealPartNoRight2 - positiveRealPartNoRight1 > 0) &&
                ((positiveRealPartNoRight2 - positiveRealPartNoRight1) *
                (positiveRealPartNoRight2 - positiveRealPartNoRight1 -
                zeroRealPartNoRight1) > 0)) {
            // there are changes of type
            // determine how many
            if (positiveRealPartNoRight2 - positiveRealPartNoRight1 > 0) {
                rightStateChangesNo = positiveRealPartNoRight2 -
                        positiveRealPartNoRight1 -
                        zeroRealPartNoRight1;
                positiveRealPartNoRight1 += zeroRealPartNoRight1;
                zeroRealPartNoRight1 = 0;
            } else {
                rightStateChangesNo = positiveRealPartNoRight2 +
                        zeroRealPartNoRight2 -
                        positiveRealPartNoRight1;
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
            zeroRealPartNoRight1 = m - positiveRealPartNoRight1 -
                    negativeRealPartNoRight1;
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
                        negativeRealPartNoLeft1 - 1) /
                        (v1.eigenValRLeft().getElement(
                        negativeRealPartNoLeft1 - 1) -
                        v2.eigenValRLeft().getElement(
                        negativeRealPartNoLeft1 - 1));
                sigma2Left = (1 - alphaLeft) * v1.sigma() +
                        alphaLeft * v2.sigma();
            }
            if (leftStateChangesNo < 0) {
                dLeft = -1;
                alphaLeft = v1.eigenValRLeft().getElement(m -
                        positiveRealPartNoLeft1) /
                        (v1.eigenValRLeft().getElement(m -
                        positiveRealPartNoLeft1) -
                        v2.eigenValRLeft().getElement(m -
                        positiveRealPartNoLeft1));
                sigma2Left = (1 - alphaLeft) * v1.sigma() +
                        alphaLeft * v2.sigma();
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
                        negativeRealPartNoRight1 - 1) /
                        (v1.eigenValRRight().getElement(
                        negativeRealPartNoRight1 - 1) -
                        v2.eigenValRRight().getElement(
                        negativeRealPartNoRight1 - 1));
                sigma2Right = (1 - alphaRight) * v1.sigma() +
                        alphaRight * v2.sigma();
            }
            if (rightStateChangesNo < 0) {
                dRight = -1;
                alphaRight = v1.eigenValRRight().getElement(m -
                        positiveRealPartNoRight1) /
                        (v1.eigenValRRight().getElement(m -
                        positiveRealPartNoRight1) -
                        v2.eigenValRRight().getElement(m -
                        positiveRealPartNoRight1));
                sigma2Right = (1 - alphaRight) * v1.sigma() +
                        alphaRight * v2.sigma();
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

    @Override
    public int findClosestSegment(RealVector targetPoint, double alpha) {

        RealVector target = new RealVector(targetPoint);
        RealVector closest = null;
        RealVector segmentVector = null;
        alpha = 0;
        int closestSegment = 0;
        double closestDistance = -1;

        List hugoniotSegList = segments();
        for (int i = 0; i < hugoniotSegments_.size(); i++) {

            HugoniotSegment segment = (HugoniotSegment) hugoniotSegList.get(i);
            segmentVector = new RealVector(segment.rightPoint());
            segmentVector.sub(segment.leftPoint());
            closest = new RealVector(target);
            closest.sub(segment.rightPoint());
            alpha = closest.dot(segmentVector) /
                    segmentVector.dot(segmentVector);
            if (alpha < 0) {
                alpha = 0;
            }
            if (alpha > 1) {
                alpha = 1;
            }
            segmentVector.scale(alpha);
            closest.sub(segmentVector);
            if ((closestDistance < 0) || (closestDistance > closest.norm())) {
                closestSegment = i;
                closestDistance = closest.norm();
            }
        }
        

        return closestSegment;
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
                    wStates[i +
                    1].finalState().getCoords(),
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


    //
    // Accessors/Mutators
    //
    public List segments() {
        return hugoniotSegments_;
    }

    public double findSigma(PhasePoint targetPoint) {

        int alpha = 0;
        int hugoniotSegmentIndx = findClosestSegment(targetPoint, alpha);

        HugoniotSegment segment = (HugoniotSegment) segments().get(
                hugoniotSegmentIndx);
        
        return (segment.leftSigma() * (1 - alpha) +
                segment.rightSigma() * alpha);

    }

    public List findPoints(double sigma) {
        ArrayList points = new ArrayList();
        double alpha = 0;
        RealVector point = null;

        for (int i = 0; i < hugoniotSegments_.size(); i++) {

            HugoniotSegment segment = (HugoniotSegment) hugoniotSegments_.get(i);

            if ((sigma - segment.leftSigma()) * (sigma - segment.rightSigma()) <=
                    0) {
                alpha = (segment.leftSigma() - sigma) /
                        (segment.leftSigma() - segment.rightSigma());
                point = new RealVector(segment.leftPoint());
                point.interpolate(segment.rightPoint(), alpha);
                points.add(point);
            }
        }
        return points;
    }

    public String toXML(boolean calcReady) {
        StringBuffer buffer = new StringBuffer();
        if (calcReady) {

            buffer.append("<HUGONIOTCURVE>\n");

            for (int i = 0; i < hugoniotSegments_.size(); i++) {

                HugoniotSegment hSegment = ((HugoniotSegment) hugoniotSegments_.get(
                        i));
                RealSegment rSegment = new RealSegment(hSegment.leftPoint(),
                        hSegment.rightPoint());
                buffer.append(rSegment.toXML());

            }
            buffer.append("</HUGONIOTCURVE>\n");


        }

        return buffer.toString();

    }

    public PhasePoint getXZero() {
        return xZero_;
    }
}
