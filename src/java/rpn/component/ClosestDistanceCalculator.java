/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import wave.util.RealVector;

import java.util.List;
import wave.util.RealSegment;

public class ClosestDistanceCalculator {

    private final List<RealSegment> segments_;
    private double alpha_;

    private final int segmentIndex_;

    private final RealVector closestPoint_;
    private double distance_;

    public ClosestDistanceCalculator(List<RealSegment> segments, RealVector point) {
        segments_ = segments;
        segmentIndex_ = findClosestSegment(point);
        closestPoint_ = findClosestPoint(point);

    }

    public double getAlpha() {
        return alpha_;
    }

    public int getSegmentIndex() {
        return segmentIndex_;
    }

    public RealVector getClosestPoint() {
        return closestPoint_;
    }

    public double distance() {
        return distance_;
    }

    private int findClosestSegment(RealVector targetPoint) {

        RealVector target = new RealVector(targetPoint);
        RealVector closest = null;
        RealVector segmentVector = null;
        double alpha = 0.;
        int closestSegment = 0;
        double closestDistance = -1.;

        for (int i = 0; i < segments_.size(); i++) {

            RealSegment segment = (RealSegment) segments_.get(i);
            segmentVector = new RealVector(segment.p1());
            segmentVector.sub(segment.p2());

            closest = new RealVector(target);

            closest.sub(segment.p2());

            if (segmentVector.norm() != 0.) {
                alpha = closest.dot(segmentVector)
                        / segmentVector.dot(segmentVector);
            } else {
                alpha = 0.;
            }

            if (alpha <= 0) {
                alpha = 0.;
            }
            if (alpha >= 1) {
                alpha = 1.;
            }
            segmentVector.scale(alpha);
            closest.sub(segmentVector);

            //------------------------------------------
            //** para calcular na projecao
            for (int k = 0; k < target.getSize(); k++) {
                if (target.getElement(k) == 0.) {
                    closest.setElement(k, 0.);
                }
            }
            //------------------------------------------

            if ((closestDistance < 0.) || (closestDistance > closest.norm())) {
                closestSegment = i;
                closestDistance = closest.norm();
                alpha_ = alpha;
            }
        }

        distance_ = closestDistance;

        return closestSegment;
    }

    private RealVector findClosestPoint(RealVector targetPoint) {

        RealSegment closestSegment = (RealSegment) segments_.get(findClosestSegment(targetPoint));

        if (alpha_ <= 0) {
            return closestSegment.p2();
        }
        if (alpha_ >= 1) {
            return closestSegment.p1();
        }

        RealVector projVec = calcVecProj(closestSegment.p2(), targetPoint,
                closestSegment.p1());

        return projVec;

    }

    public static RealVector convexCombination(RealVector a, RealVector b, double alpha) {

        final RealVector Va = new RealVector(a);
        final RealVector Vb = new RealVector(b);
        
        RealVector result = new RealVector(Va.getSize());
        
        Va.scale(alpha);
        Vb.scale(1-alpha);
        
        result.add(Va,Vb);
        
        return result;


    }

    private RealVector calcVecProj(RealVector a, RealVector b, RealVector o) {

        // Va = a - o
        // Vb = b - o
        final RealVector VoNeg = new RealVector(o);
        VoNeg.negate();
        final RealVector Va = new RealVector(a);
        final RealVector Vb = new RealVector(b);

        Va.scaleAdd(1, a, VoNeg);
        Vb.scaleAdd(1, b, VoNeg);

        double dotVaVb = Va.dot(Vb);
        double normVa = Va.norm();

        Va.scale(dotVaVb / Math.pow(normVa, 2));

        RealVector Vproj = new RealVector(Va.getSize());

        Vproj.add(Va, o);

        return Vproj;

    }

}
