/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package wave.ode;

import wave.util.*;
import rpnumerics.WavePoint;

public class Rk4BPMethod implements ODESolver {
    //
    // Constants
    //
    static double PGROW = -0.2;
    static double PSHRINK = -0.25;
    static double FCOR = 0.06666666;
    static double SAFETY = 0.9;
    static double ERRCON = 6.0e-4;
    static int MADE_MAXIMUM_STEP_NUMBER = 0;
    //
    // Members
    //
    private RealVector[] coords_;
    private double[] times_;
    private int flag_;
    private Rk4BPProfile opt_;

    //
    // Constructors
    //
    public Rk4BPMethod(Rk4BPProfile opt) {
        opt_ = opt;
    }

    public ODESolution solve(RealVector firstPoint, int timeDirection) {
        int nok = 0;
        double kount = 0;
        double hNew;
        double h2 = 0;
        double hNew2 = 0;
        double time = 0;
        int resultFlag = MADE_MAXIMUM_STEP_NUMBER;
        RealVector yNew = new RealVector(firstPoint.getSize());
        RealVector yNew2 = new RealVector(firstPoint.getSize());
        RealVector tmp = new RealVector(firstPoint.getSize());
        RealVector dy = new RealVector(firstPoint.getSize());
        RealVector y = new RealVector(firstPoint);

        tmp.set(opt_.getFunction().f(y));
        double h = opt_.getDYmax() / norm(tmp);
        if (timeDirection < 0) {
            h = -h;
        }
        RealVector[] coords = new RealVector[opt_.getMaximumStepNumber()];
        double[] times = new double[opt_.getMaximumStepNumber()];
        coords[0] = new RealVector(y);
        times[0] = 0;
        int stepN = 1;
        while ((stepN < opt_.getMaximumStepNumber()) &&
               (resultFlag == MADE_MAXIMUM_STEP_NUMBER)) {
            hNew = rkqc(y, h, yNew);
            // boundary
            if (opt_.hasBoundary() && (!opt_.getBoundary().inside(yNew))) {
                resultFlag = ODESolution.STOP_OUT_OF_BOUNDARY;
                coords[stepN] = new RealVector(yNew);
                times[stepN] = time + hNew;
            } else {
                // Poincare section
                // finding by bisection algorithm
                try {
                    if (opt_.hasPoincareSection() &&
                        (opt_.getPoincareSection().intersect(y, yNew))) {
                        resultFlag = ODESolution.STOP_ON_POINCARE_SECTION;
                        h = hNew;
                        h2 = 0;
                        yNew2.set(y);
                        dy.sub(yNew, yNew2);
                        while (norm(dy) > opt_.getEpsilon()) {
                            hNew = 0.5 * (h + h2);
                            tmp.set(rk4(y, hNew));
                            if (opt_.getPoincareSection().intersectPlane(y, tmp)) { // was changed 12.11.2003
                                yNew.set(tmp);
                                h = hNew;
                            } else {
                                yNew2.set(tmp);
                                h2 = hNew;
                            }
                            dy.sub(yNew, yNew2);
                        }
                        y.set(opt_.getPoincareSection().intersectionPoint(yNew,
                                yNew2));
                        yNew.sub(y);
                        yNew2.sub(y);
                        coords[stepN] = new RealVector(y);
                        times[stepN] = time +
                                       (h * yNew2.norm() + h2 * yNew.norm()) /
                                       (yNew.norm() + yNew2.norm());
                    } else {
                        // normal step
                        if (hNew == h) {
                            nok++;
                        }
                        if (nok > 2) {
                            nok = 0;
                            h = h * 2;
                        } else {
                            h = hNew;
                        }
                        coords[stepN] = new RealVector(yNew);
                        times[stepN] = time + hNew;
                        y.set(yNew);
                        time = time + hNew;
                    }
                } catch (Exception ex) {
                    System.out.println(ex);
                }
            }
            stepN++;
        }

        //Setting sigma information for rarefaction curve calculation

        WavePoint[] resultCoords = new WavePoint[stepN];
        double[] resultTimes = new double[stepN];
        for (int i = 0; i < stepN; i++) {
            resultCoords[i] = new WavePoint(coords[i],
                                            opt_.getFunction().f(coords[i]).
                                            getSigma());
            resultTimes[i] = times[i];
        }

        return new ODESolution(resultCoords, resultTimes, resultFlag);


    }

    //
    // Accessors
    //
    public ODESolverProfile getProfile() {
        return opt_;
    }

    //
    // Methods
    //
    protected RealVector rk4(RealVector y0, double h) {
        RealVector y = new RealVector(y0);
        double halfH = 0.5 * h;
        RealVector halfK1 = new RealVector(opt_.getFunction().f(y));
        halfK1.scale(halfH);
        y.add(y0, halfK1);
        RealVector halfK2 = new RealVector(opt_.getFunction().f(y));
        halfK2.scale(halfH);
        y.add(y0, halfK2);
        RealVector k3 = new RealVector(opt_.getFunction().f(y));
        k3.scale(h);
        y.add(y0, k3);
        RealVector k4 = new RealVector(opt_.getFunction().f(y));
        k4.scale(h);
        RealVector result = new RealVector(y0);
        halfK1.scale(1.0 / 3.0);
        result.add(halfK1);
        halfK2.scale(2.0 / 3.0);
        result.add(halfK2);
        k3.scale(1.0 / 3.0);
        result.add(k3);
        k4.scale(1.0 / 6.0);
        result.add(k4);

        return result;
    }

    protected double rkqc(RealVector y0, double hTry, RealVector result) {
        // returns a step size h really made
        RealVector yStart = new RealVector(y0);
        double h = hTry;
        double halfH;
        boolean accuracyNotReached = true;
        RealVector yTemp = new RealVector(y0.getSize());
        RealVector yTemp2 = new RealVector(y0.getSize());
        double accuracy;
        double stepSize;
        int i;
        double temp;
        while (accuracyNotReached) {
            // one full steps
            yTemp.set(rk4(yStart, h));
            // checking the length (can be changes to checking the difference)
            yTemp2.sub(yTemp, yStart);
            stepSize = norm(yTemp2);
            if (stepSize > opt_.getDYmax()) {
                h *= SAFETY * opt_.getDYmax() / stepSize;
            } else {
                // two half-steps
                halfH = 0.5 * h;
                yTemp2.set(rk4(yStart, halfH));
                result.set(rk4(yTemp2, halfH));
                yTemp2.sub(result, yTemp);
                accuracy = norm(yTemp2) / opt_.getEpsilon();
                if (accuracy <= 1.0) {
                    accuracyNotReached = false;
                } else {
                    h *= SAFETY * Math.exp(PSHRINK * Math.log(accuracy));
                }
                // adaption of the next step is not as in the book
                // just do nothing
            }
        }
        yTemp2.scale(FCOR);
        result.add(yTemp2);
        return h;
    }

    protected double norm(RealVector y) {
        // returns scaled norm
        double normValue = 0;
        for (int i = 0; i < y.getSize(); i++) {
            normValue += y.getElement(i) * y.getElement(i) /
                    (opt_.getYScales().getElement(i) *
                     opt_.getYScales().getElement(i));
        }
        normValue = Math.sqrt(normValue);
        return normValue;
    }
}
