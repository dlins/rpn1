/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import java.util.logging.Level;
import java.util.logging.Logger;
import wave.util.*;
import wave.ode.ODESolution;
import org.netlib.lapack.DGEES;
import org.netlib.lapack.DTRSYL;
import org.netlib.util.intW;
import org.netlib.util.doubleW;
import wave.util.RealVector;
import wave.util.RealMatrix2;

/*
Computations:
a)evaluates the second order approximation of the manifold, see Section 4.2
b)finds value of h = UuT(firstPoint - stationaryPoint)
c)corrects value of h using formula (4.27), where T  is chosen such that the inequalities ||x(T) - xapp(T)||  eps and ||x(T) - x(-)||  prefDeltaNorm are satisfied (the bisection method is used to find the value of T  such that one of the inequalities is satisfied as an equality).
d)numerical integrations from the point x(T) = x0 + Uuh,  where we set T = 0.
 */
public class ManifoldOrbitCalc implements RpCalculation {
    //
    // Constants
    //
    //
    // Members
    //
    private StationaryPoint stationaryPoint_;
    private PhasePoint firstPoint_;
    private int timeDirection_;
    private String methodName_;
    private SimplexPoincareSection poincare_;
    
    //
    // Constructors
    //


    public ManifoldOrbitCalc(StationaryPoint stationaryPoint, PhasePoint firstPoint, SimplexPoincareSection poincareSection,int timeDirection) {
        stationaryPoint_ = stationaryPoint;
        timeDirection_ = timeDirection;
        poincare_=poincareSection;
        firstPoint_ = firstPoint;
    }


    public ManifoldOrbitCalc(StationaryPoint stationaryPoint, SimplexPoincareSection poincareSection,int timeDirection)throws RpException {      //RETOMAR AQUI !!!
        stationaryPoint_ = stationaryPoint;
        timeDirection_ = timeDirection;
        poincare_=poincareSection;

          if(stationaryPoint.isSaddle()) {
            firstPoint_ = orbitInitialPoint(stationaryPoint, poincareSection);
        }

        else throw  new RpException("Stationary point is not saddle in Manifold Constructor");

    }




    public ManifoldOrbitCalc(StationaryPoint stationaryPoint, PhasePoint firstPoint, int timeDirection) {
        timeDirection_ = timeDirection;
        stationaryPoint_ = stationaryPoint;
        firstPoint_ = firstPoint;

        RealVector [] poincarePoints = new RealVector[2];// Poincare sendo apenas 1 segmento (usando um ponto como default)

        poincarePoints[0]= new RealVector(2);

        poincarePoints[0].setElement(0, 0.0);
        poincarePoints[0].setElement(1, 0.0);

    
        poincarePoints[1]= new RealVector(2);

        poincarePoints[1].setElement(0, 0.0);
        poincarePoints[1].setElement(1, 0.0);

        SimplexPoincareSection poincare = new SimplexPoincareSection(poincarePoints);

        poincare_=poincare;


    }
    // -------------------------------------------------------------------------
    private PhasePoint orbitInitialPoint(StationaryPoint stationaryPoint, SimplexPoincareSection poincareSection) {

        double P10 = poincareSection.getPoints()[0].getElement(0);
        double P11 = poincareSection.getPoints()[0].getElement(1);
        double P20 = poincareSection.getPoints()[1].getElement(0);
        double P21 = poincareSection.getPoints()[1].getElement(1);
        double a = P11 - P21;
        double b = P20 - P10;
        double c = P10*P21 - P20*P11;


        double h = 1E-2;
        RealVector center = new RealVector(stationaryPoint.getCoords());
        RealVector point1 = new RealVector(2);
        RealVector point2 = new RealVector(2);
        double dist1 = 0.;
        double dist2 = 0.;

        PhasePoint initialPoint = null;

        for (int i = 0; i < 2; i++) {
            if (stationaryPoint.getEigenValR()[i] > 0.) {
                RealVector dir = new RealVector(stationaryPoint.getEigenVec()[i]);

                point1.setElement(0, h * dir.getElement(0) + center.getElement(0));
                point1.setElement(1, h * dir.getElement(1) + center.getElement(1));

                point2.setElement(0, -h * dir.getElement(0) + center.getElement(0));
                point2.setElement(1, -h * dir.getElement(1) + center.getElement(1));

                dist1 = (Math.abs(a * point1.getElement(0) + b * point1.getElement(1) + c)) / Math.sqrt(a * a + b * b);
                dist2 = (Math.abs(a * point2.getElement(0) + b * point2.getElement(1) + c)) / Math.sqrt(a * a + b * b);
            }
        }


        if(dist1 < dist2) {
            initialPoint = new PhasePoint(point1);
        }
        else {
            initialPoint = new PhasePoint(point2);
        }

        return initialPoint;

    }


    //
    // Accessors/Mutators
    //
    // TODO Alexei, is this really the first point ?
    public PhasePoint orbitRefPoint() {
        return firstPoint_;
    }

    public int tDirection() {
        return timeDirection_;
    }

    //
    // Methods
    //


   

    public RpSolution recalc(Area area) throws RpException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public RpSolution calc() throws RpException {
        OrbitCalc oCalc = new OrbitCalc(new OrbitPoint(firstPoint_), timeDirection_);

        oCalc.setPoincareSection(poincare_.getPoints());
        Orbit orbit = (Orbit) oCalc.calc();


//        System.out.println("A orbita cortou "+ orbit.isInterPoincare()+" em :"+ orbit.getPoints()[orbit.getPoints().length-1]);



        return new ManifoldOrbit(stationaryPoint_, firstPoint_, orbit, timeDirection_);
    }

    public RpSolution recalc() throws RpException {
        return calc();
    }






//    public RpSolution recalc() throws RpException {
        // stores the delta from the previous stationary point
//        PhasePoint firstDelta = new PhasePoint(firstPoint_);
//        firstDelta.getCoords().sub(firstPoint_.getCoords(), stationaryPoint_.getPoint().getCoords());
//        // recalculation of stationary point
////        StationaryPointCalc pointRecalc = new StationaryPointCalc(stationaryPoint_.getPoint());
//        StationaryPointCalc pointRecalc = RPNUMERICS.createStationaryPointCalc(stationaryPoint_.getPoint());
//        stationaryPoint_ = (StationaryPoint) pointRecalc.calc();
//        // adds the delta to the new stationary point
//        firstPoint_.getCoords().add(stationaryPoint_.getPoint().getCoords(), firstDelta.getCoords());
//        return calc();
//        return null;
//    }

//    public RpSolution calc() throws RpException {
//        int m = RPNUMERICS.domainDim();
//        // checking input
//        boolean correctInput = true;
//        // check timeDirection
//        if (timeDirection_ == 0) {
//            correctInput = false;
//        } else {
//            timeDirection_ = timeDirection_ / Math.abs(timeDirection_);
//        }
//        // check stationary point input
//        if (correctInput) {
//            if ((stationaryPoint_.getIntegrationFlag() != RpSolution.CONVERGENCE_IN_STATIONARY_POINT_COMPUTATION) ||
//                    (stationaryPoint_.getDimP() == 0) || (stationaryPoint_.getDimN() == 0)) {
//                correctInput = false;
//            }
//        }
//        //check first point
//        if (correctInput) {
//            int kk;
//            if (timeDirection_ > 0) {
//                kk = stationaryPoint_.getDimP();
//            } else {
//                kk = stationaryPoint_.getDimN();
//            }
//            RealMatrix2 UuTmp = new RealMatrix2(m, kk);
//            if (timeDirection_ > 0) {
//                stationaryPoint_.getSchurVecP().copySubMatrix(0, 0, m, kk, 0, 0, UuTmp);
//            } else {
//                stationaryPoint_.getSchurVecN().copySubMatrix(0, 0, m, kk, 0, 0, UuTmp);
//            }
//            RealVector xStat = new RealVector(stationaryPoint_.getPoint().getCoords());
//            RealVector dx = new RealVector(firstPoint_.getCoords());
//            dx.sub(xStat);
//            RealVector q = new RealVector(kk);
//            RealMatrix2 UuTmpT = new RealMatrix2(kk, m);
//            UuTmpT.transpose(UuTmp);
//            q.mul(UuTmpT, dx);
//            if (q.norm() == 0.0) {
//                correctInput = false;
//            }
//        }
//        if (!correctInput) {
//            throw new RpException("wrong input");
//        } // incorrect input
//        // end of checking input
//        int i = 0;
//        int j = 0;
//        double prefDeltaNorm = RPNUMERICS.errorControl().ode().prefStationaryPointDistance();
//        double eps = RPNUMERICS.errorControl().eps();
//        RealVector point1 = new RealVector(firstPoint_.getCoords());
//        Orbit orbit = null;
//        int finishType = 0;
//        // filling up the matrices Ss, Su, and Us, Uu
//        RealMatrix2 schurForm = new RealMatrix2(m, m);
//        RealMatrix2 schurVec = new RealMatrix2(m, m);
//        int k = 0;
//        if (timeDirection_ < 0) {
//            schurForm.set(stationaryPoint_.getSchurFormN());
//            schurForm.negate();
//            schurVec.set(stationaryPoint_.getSchurVecN());
//            k = stationaryPoint_.getDimN();
//        } else {
//            schurForm.set(stationaryPoint_.getSchurFormP());
//            schurVec.set(stationaryPoint_.getSchurVecP());
//            k = stationaryPoint_.getDimP();
//        }
//        RealMatrix2 Su = new RealMatrix2(k, k);
//        RealMatrix2 Uu = new RealMatrix2(m, k);
//        RealMatrix2 UuT = new RealMatrix2(k, m);
//        RealMatrix2 Ss = new RealMatrix2(m - k, m - k);
//        RealMatrix2 Us = new RealMatrix2(m, m - k);
//        RealMatrix2 UsT = new RealMatrix2(m - k, m);
//        schurForm.copySubMatrix(0, 0, k, k, 0, 0, Su);
//        schurVec.copySubMatrix(0, 0, m, k, 0, 0, Uu);
//        UuT.transpose(Uu);
//        schurForm.copySubMatrix(k, k, m - k, m - k, 0, 0, Ss);
//        schurVec.copySubMatrix(0, k, m, m - k, 0, 0, Us);
//        UsT.transpose(Us);
//        // intialization of local data for an orbit
//        RealVector point0 = new RealVector(stationaryPoint_.getPoint().getCoords());
//        RealVector delta = new RealVector(m);
//        delta.sub(point1, point0);
//        // projection of delta on the corresponding unvariant subspace
//        RealVector q = new RealVector(k);
//        q.mul(UuT, delta); // q is the coordinate vector in the base given by schur vectors
//        delta.mul(Uu, q);
//        // error analysis of the approximation
//        // creation of the matrix R, see paper
//        RealMatrix2 R = new RealMatrix2(k * k, k * k);
//        RealMatrix2 tmpR1 = new RealMatrix2(k, 1);
//        RealMatrix2 tmpR2 = new RealMatrix2(k, 1);
//        for (i = 0; i < k; i++) {
//            Su.copySubMatrix(0, 0, k, k, i * k, i * k, R);
//        }
//        for (i = 0; i < k; i++) {
//            Su.copySubMatrix(0, i, k, 1, 0, 0, tmpR2);
//            for (j = 0; j < k; j++) {
//                R.copySubMatrix(j * k, i * k + j, k, 1, 0, 0, tmpR1);
//                tmpR1.add(tmpR2);
//                tmpR1.copySubMatrix(0, 0, k, 1, j * k, i * k + j, R);
//            }
//        }
//        // creation of the matrix F, see paper
//        // UU is a matrix with (v*m+w,i*k+j)th element = Uu(v,i)*Uu(w,j)
//        RealMatrix2 UU = new RealMatrix2(m * m, k * k);
//        for (int v = 0; v < m; v++) {
//            for (int w = 0; w < m; w++) {
//                for (i = 0; i < k; i++) {
//                    for (j = 0; j < k; j++) {
//                        UU.setElement(v * m + w, i * k + j, Uu.getElement(v, i) * Uu.getElement(w, j));
//                    }
//                }
//            }
//        }
//        // ddf is a matrix with the (v*k+w)th column = d^2 f/ dx_v dx_w
//        RealMatrix2 ddf = new RealMatrix2(m, m * m);
//        RealVector ddf_v_w = new RealVector(m);
//
////        WaveState input = new WaveState (new PhasePoint(point0));
//
////        JetMatrix output = new JetMatrix(m);
//
////        getFlow().jet(input, output, 2);
//
//        //HessianMatrix D2X =  flow_.fluxDeriv2(point0);
//
//
////        HessianMatrix D2X = getFlow().fluxDeriv2(point0);
//        for (int v = 0; v < m; v++) {
//            for (int w = 0; w < m; w++) {
//                for (int u = 0; u < m; u++) {
//                    ddf.setElement(u, v * m + w, D2X.getElement(u, v, w));
//                }
//            }
//        }
//        // final construction F = UsT*ddf*UU
//        RealMatrix2 F = new RealMatrix2(m - k, k * k);
//        RealMatrix2 ddfUU = new RealMatrix2(m, k * k);
//        ddfUU.mul(ddf, UU);
//        F.mul(UsT, ddfUU);
//        // solving the equation Us*H-H*R = -F
//        F.negate();
//        RealMatrix2 H = new RealMatrix2(sylvester(m - k, k * k, Ss, R, F));
//        // calculation of the second order term for the manifold
//        RealMatrix2 secondOderTerm = new RealMatrix2(m, k * k);
//        secondOderTerm.mul(Us, H);
//        secondOderTerm.scale(0.5);
//        //        RealMatrix2 id = new RealMatrix2(m,m); // delete
//        //        id.scale(0.5); // delete
//        //        secondOderTerm.mul(id,secondOderTerm); // delete
//        // scaling up the solution up to STATIONARY_POINT_PREF_DISTANCE
//        // finding the minimum eigenvalue of Su
//        double scale0 = 0;
//        double scale2 = Su.getElement(0, 0);
//        for (i = 1; i < k; i++) {
//            if (scale2 > Su.getElement(i, i)) {
//                scale2 = Su.getElement(i, i);
//            }
//        }
//        //setting the initial scale parameter to decend/asend along the orbit
//        scale2 = -Math.log(0.2) / scale2;
//        double scaleDirection = 1d; // scale up
//        boolean approximationQuality = (RPNUMERICS.errorControl().ode().stateVectorNorm(delta) < prefDeltaNorm) &&
//                (errorValue(secondOderTerm, q) < eps);
//        if (!approximationQuality) {
//            scale2 = -scale2; // scale down
//        }
//        // rough scaling until geting the right interval
//        RealVector delta0 = new RealVector(delta);
//        RealVector q0 = new RealVector(q);
//        RealMatrix2 scaledSu = new RealMatrix2(Su);
//        scaledSu.scale(scale2);
//        RealMatrix2 expSu = new RealMatrix2(RealMatrix2.matrixExponent(scaledSu));
//        RealVector delta2 = new RealVector(m);
//        RealVector q2 = new RealVector(k);
//        q2.mul(expSu, q);
//        delta2.mul(Uu, q2);
//        boolean approximationQuality2 = (RPNUMERICS.errorControl().ode().stateVectorNorm(delta2) < prefDeltaNorm) &&
//                (errorValue(secondOderTerm, q2) < eps);
//        while (approximationQuality == approximationQuality2) {
//            scale2 = scale2 * 2d;
//            scaledSu = new RealMatrix2(Su);
//            scaledSu.scale(scale2);
//            expSu = new RealMatrix2(RealMatrix2.matrixExponent(scaledSu));
//            q2.mul(expSu, q);
//            delta2.mul(Uu, q2);
//            approximationQuality2 = (RPNUMERICS.errorControl().ode().stateVectorNorm(delta2) < prefDeltaNorm) &&
//                    (errorValue(secondOderTerm, q2) < eps);
//        }
//        // fine scaling -> scales exponents !!!
//        RealVector delta1 = new RealVector(delta0);
//        RealVector q1 = new RealVector(q0);
//        double scale1 = scale0;
//        RealVector deltaDifference = new RealVector(m);
//        deltaDifference.sub(delta2, delta0);
//        while (RPNUMERICS.errorControl().ode().stateVectorNorm(deltaDifference) > RpErrorControl.MAX_PRECISION) {
//            scale1 = 0.5 * (scale2 + scale0);
//            scaledSu = new RealMatrix2(Su);
//            scaledSu.scale(scale1);
//            expSu = new RealMatrix2(RealMatrix2.matrixExponent(scaledSu));
//            q1.mul(expSu, q);
//            delta1.mul(Uu, q1);
//            approximationQuality = (RPNUMERICS.errorControl().ode().stateVectorNorm(delta1) < prefDeltaNorm) &&
//                    (errorValue(secondOderTerm, q1) < eps);
//            if (approximationQuality == approximationQuality2) {
//                scale2 = scale1;
//                delta2 = new RealVector(delta1);
//                q2 = new RealVector(q1);
//            } else {
//                scale0 = scale1;
//                delta0 = new RealVector(delta1);
//                q0 = new RealVector(q1);
//            }
//            deltaDifference.sub(delta2, delta0);
//        }
//        double chk = errorValue(secondOderTerm, q1);
//        double chk2 = RPNUMERICS.errorControl().ode().stateVectorNorm(delta1);
//        point1 = new RealVector(point0);
//        point1.add(delta1);
//        // adjustment of the maximal time step at the start
//        // condition: step < DY_MAX, turn < pi/6
//        double x = Math.log(1 + RPNUMERICS.errorControl().ode().maxStateStepLength() / delta1.norm());
//        i = 0;
//        double dT1 = x / Su.getElement(i, i);
//        double im;
//        double dT2 = 0;
//        double dT;
//        if ((i + 1 < k) && (Su.getElement(i + 1, i) != 0)) {
//            im = Math.sqrt(-(Su.getElement(i, i) - Su.getElement(i + 1, i + 1)) *
//                    (Su.getElement(i, i) - Su.getElement(i + 1, i + 1)) / 4 + Su.getElement(i, i + 1) * Su.getElement(i + 1, i));
//            i = i + 2;
//            dT2 = Math.PI / (6.0 * im);
//            dT1 = Math.min(dT1, dT2);
//        } else {
//            i = i + 1;
//        }
//        dT = dT1;
//        while (i < k) {
//            dT1 = x / Su.getElement(i, i);
//            if ((i + 1 < k) && (Su.getElement(i + 1, i) != 0)) {
//                im = Math.sqrt(-(Su.getElement(i, i) - Su.getElement(i + 1, i + 1)) *
//                        (Su.getElement(i, i) - Su.getElement(i + 1, i + 1)) / 4 + Su.getElement(i, i + 1) *
//                        Su.getElement(i + 1, i));
//                i = i + 2;
//                dT2 = Math.PI / (6.0 * im);
//                dT1 = Math.min(dT1, dT2);
//            } else {
//                i = i + 1;
//            }
//            dT = Math.min(dT, dT1);
//        }
//        // checking intersection the approximated segment with Poincare section and boundary
//
//        try {
//            if (RPNUMERICS.pSection() != null && RPNUMERICS.pSection().intersect(point0, point1)) {
//                finishType = RpSolution.STATIONARY_POINT_IS_TOO_CLOSE_TO_POINCARE_SECTION;
//            } else if (!RPNUMERICS.boundary().inside(point1)) {
//                finishType = RpSolution.STATIONARY_POINT_IS_TOO_CLOSE_TO_BOUNDARY;
//            } else {
//                // evaluation of the orbit
//                // evaluation of the orbit
//                ODESolution solution = AdvanceCurve.calc(new RealVector(point1), timeDirection_);
//                RealVector[] coords = solution.getWavePoints();
//                double[] times = solution.getTimes();
//                finishType = solution.getFlag();
//                orbit = new Orbit(coords, times, finishType);
//            }
//
//        } catch (Exception ex) {
//            System.out.println(ex);
//        }
//
//        if (orbit == null) {
//            orbit = rpn.parser.RPnDataModule.ORBIT;
//        }
//
//        return new ManifoldOrbit(stationaryPoint_, firstPoint_, orbit, timeDirection_);


//    }

    protected double errorValue(RealMatrix2 A, RealVector q) {
        // evaluates the error  =  ||0.5 q^T A q||
        int m = RPNUMERICS.domainDim();
        int k = q.getSize();
        RealVector Err = new RealVector(m);
        RealVector column = new RealVector(m);
        int i, j;
        for (i = 0; i < k; i++) {
            for (j = 0; j < k; j++) {
                A.getColumn(i * k + j, column);
                Err.scaleAdd(q.getElement(i) * q.getElement(j), column, Err);
            }
        }
        double error = RPNUMERICS.errorControl().ode().stateVectorNorm(Err);
        return error;
    }

    protected RealMatrix2 sylvester(int n1, int n2, RealMatrix2 A, RealMatrix2 B, RealMatrix2 C) { // was changed 12.11.2003
        // solution of the Sylvester equation
        // AD-DB = C
        // for given A,B,C of dimensions n1xn1, n2xn2, n1xn2
        double[][] Ac = RealMatrix2.convert(A);
        double[][] Bc = RealMatrix2.convert(B);
        RealMatrix2 D = new RealMatrix2(n1, n2);
        intW INFO1 = new intW(0);
        int LWORK1 = n1 * 5;
        double[] WORK1 = new double[LWORK1];
        boolean[] BWORK1 = new boolean[n1];
        double[] eigenValR1 = new double[n1];
        double[] eigenValI1 = new double[n1];
        double[][] schurVecArrayA = new double[n1][n1];
        intW DimW1 = new intW(0);
        intW INFO2 = new intW(0);
        int LWORK2 = n2 * 5;
        double[] WORK2 = new double[LWORK2];
        boolean[] BWORK2 = new boolean[n2];
        double[] eigenValR2 = new double[n2];
        double[] eigenValI2 = new double[n2];
        double[][] schurVecArrayB = new double[n2][n2];
        intW DimW2 = new intW(0);
        DGEES.DGEES("V", "N",
                new LapackSelectPos(), n1, Ac, DimW1, eigenValR1, eigenValI1, schurVecArrayA, WORK1, LWORK1, BWORK1, INFO1);
        DGEES.DGEES("V", "N",
                new LapackSelectPos(), n2, Bc, DimW2, eigenValR2, eigenValI2, schurVecArrayB, WORK2, LWORK2, BWORK2, INFO2);
        RealMatrix2 UA = new RealMatrix2(n1, n1);
        RealMatrix2 UB = new RealMatrix2(n2, n2);
        RealMatrix2.convert(n1, n1, schurVecArrayA);
        RealMatrix2.convert(n2, n2, schurVecArrayB);
        RealMatrix2 UAT = new RealMatrix2(UA);
        RealMatrix2 UBT = new RealMatrix2(UB);
        UAT.transpose();
        UBT.transpose();
        C.mul(UBT);
        C.transpose();
        C.mul(UAT);
        C.transpose();
        double[][] Cc = RealMatrix2.convert(C);
        intW iW = new intW(0);
        doubleW dW = new doubleW(0d);
        DTRSYL.DTRSYL("N", "N", -1, n1, n2, Ac, Bc, Cc, dW, iW);
        D = new RealMatrix2(n1, n2);
        D = new RealMatrix2(D.convert(n1, n2, Cc));
        D.mul(UB);
        RealMatrix2 DT = new RealMatrix2(n2, n1);
        DT.transpose(D);
        DT.mul(UA);
        D.transpose(DT);
        return D;
    }



    
}
