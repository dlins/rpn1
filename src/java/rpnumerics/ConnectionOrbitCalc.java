/*
*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package rpnumerics;

import wave.ode.ODESolution;
import wave.util.RealMatrix2;
import wave.util.RealVector;

/*
Creates a connecting orbit using Newton s (or gradient) method for manifoldObitA and manifoldObitB.
Spep in the method is based on equations (5.3), (5.4).
New reference points are obtained using equation (5.1) and corrected by
deleting a component proportional to f(x(t)).
A gradient method is used is ||xPa - xPb|| > maxPSectionStepLength or |Ds| > maxSigmaStepLength.
 */

public class ConnectionOrbitCalc implements RpCalculation {
    // bound for changing approximated part
    static double APPROXIMATION_CHANGE_RELATIVE_MAXIMUM = 0.5;
    //
    // Members
    //
    private ManifoldOrbit manifoldOrbitA_;
    private ManifoldOrbit manifoldOrbitB_;
    private int flag_;
    private int iterationNumber_;
    private double sigmaAccuracy_;
    private String methodCalcName_;
    private ShockFlow flow_;

    //
    // Constructor
    //
    public ConnectionOrbitCalc(ManifoldOrbit manifoldOrbitA, ManifoldOrbit manifoldOrbitB, ShockFlow flow) {
        methodCalcName_="default";
        flow_=flow;

        manifoldOrbitA_ = manifoldOrbitA;
        manifoldOrbitB_ = manifoldOrbitB;
        if ((manifoldOrbitA_.getFinishType() != ODESolution.STOP_ON_POINCARE_SECTION) ||
            (manifoldOrbitB_.getFinishType() != ODESolution.STOP_ON_POINCARE_SECTION))
                flag_ = RpSolution.NO_POINCARE_SECTION;
        else {
            RealVector lastPointA = new RealVector(manifoldOrbitA_.getOrbit().getPoints()
                [manifoldOrbitA_.getOrbit().getPoints().length - 1].getCoords());
            RealVector lastPointB = new RealVector(manifoldOrbitB_.getOrbit().getPoints()
                [manifoldOrbitB_.getOrbit().getPoints().length - 1].getCoords());
            lastPointB.sub(lastPointA);
            if (RPNUMERICS.errorControl().ode().stateVectorNorm(lastPointB) < RPNUMERICS.errorControl().eps())
                flag_ = RpSolution.CONNECTED;
            else
                flag_ = RpSolution.CONNECTION_NOT_FINISHED;
        }
        iterationNumber_ = 0;
        sigmaAccuracy_ = 0;
    }

    //
    // Accessors/Mutators
    //
    public ManifoldOrbit getManifoldOrbitA() { return manifoldOrbitA_; }

    public ManifoldOrbit getManifoldOrbitB() { return manifoldOrbitB_; }

    public int getIterationNumber() { return iterationNumber_; }

    public double getSigmaAccuracy() { return sigmaAccuracy_; }

    //
    // Methods
    //
    public RpSolution recalc() throws RpException {
//        // recalculation for each Manifold
//        // Manifold A
//        RealVector firstPointDeltaA = new RealVector(manifoldOrbitA_.getFirstPoint().getCoords());
//        firstPointDeltaA.sub(manifoldOrbitA_.getStationaryPoint().getPoint().getCoords());
//        StationaryPointCalc statPointCalcA = new StationaryPointCalc(manifoldOrbitA_.getStationaryPoint().getPoint(),flow_);
//        StationaryPoint statPointA = (StationaryPoint)statPointCalcA.calc();
//        PhasePoint firstPointA = new PhasePoint(statPointA.getPoint());
//        firstPointA.getCoords().add(firstPointDeltaA);
////        ManifoldOrbitCalc manifoldCalcA = RPNUMERICS.createManifoldCalc(statPointA, firstPointA, manifoldOrbitA_.getTimeDirection());//()flag_)new ManifoldOrbitCalc(statPointA, firstPointA, manifoldOrbitA_.getTimeDirection());
//        manifoldOrbitA_ = (ManifoldOrbit)manifoldCalcA.calc();
//        // Manifold B
//        RealVector firstPointDeltaB = new RealVector(manifoldOrbitB_.getFirstPoint().getCoords());
//        firstPointDeltaB.sub(manifoldOrbitB_.getStationaryPoint().getPoint().getCoords());
//        StationaryPointCalc statPointCalcB = new StationaryPointCalc(manifoldOrbitB_.getStationaryPoint().getPoint(),flow_);
//        StationaryPoint statPointB = (StationaryPoint)statPointCalcB.calc();
//        PhasePoint firstPointB = new PhasePoint(statPointB.getPoint());
//        firstPointB.getCoords().add(firstPointDeltaB);
////        ManifoldOrbitCalc manifoldCalcB = RPNUMERICS.createManifoldCalc(statPointB, firstPointB, manifoldOrbitB_.getTimeDirection());//flag_)new ManifoldOrbitCalc(statPointB, firstPointB, manifoldOrbitB_.getTimeDirection());
//        manifoldOrbitB_ = (ManifoldOrbit)manifoldCalcB.calc();
////        ConnectionOrbitCalc newCalc = RPNUMERICS.createConnectionOrbitCalc(manifoldOrbitA_, manifoldOrbitB_);//new ConnectionOrbitCalc(manifoldOrbitA_, manifoldOrbitB_);
//        return newCalc.calc();
        return null;
    }

    protected Orbit createConnectingOrbit() {
        return null;
//        int i;
//        int nA = manifoldOrbitA_.getOrbit().getPoints().length;
//        int nB = manifoldOrbitB_.getOrbit().getPoints().length;
//        double tA = manifoldOrbitA_.getOrbit().getPoints() [nA - 1].getLambda();
//        double tB = manifoldOrbitB_.getOrbit().getPoints() [nB - 1].getLambda();
//        OrbitPoint[] result = new OrbitPoint[nA + nB - 1];
//        for (i = 0; i < nA - 1; i++) {
//            result[i] = new OrbitPoint(manifoldOrbitA_.getOrbit().getPoints() [i]);
//            result[i].setLambda(result[i].getLambda() - tA);
//        }
//
//      /* calculates the middle point and replace both endpoints
//        TODO this should become a Multid Map*/
//
//        RealVector tmp = new RealVector(manifoldOrbitA_.getOrbit().getPoints() [nA - 1].getCoords());
//        tmp.add(new RealVector(manifoldOrbitB_.getOrbit().getPoints() [nB - 1].getCoords()));
//        tmp.scale(0.5);
//        result[nA - 1] = new OrbitPoint(tmp, 0.0);
//        for (i = nB - 2; i >= 0; i--) {
//            result[nA + nB - 2 - i] = new OrbitPoint(manifoldOrbitB_.getOrbit().getPoints() [i]);
//            result[nA + nB - 2 - i].setLambda(result[nA + nB - 2 - i].getLambda() - tB);
//        }
//        return new Orbit(result, flag_);
    }

    public RpSolution calc() throws RpException {
        return null;
//        if (manifoldOrbitA_.getStationaryPoint().getDimP() + manifoldOrbitB_.getStationaryPoint().getDimN() !=
//            RPNUMERICS.domainDim()) {
//                throw new RpException("wrong input");
//        } // incorrect input
//        while (flag_ == RpSolution.CONNECTION_NOT_FINISHED)
//            connectionIterationStep();
//
//        /* WE SHOULD RETURN THE CONNECTION ANYWAY
//      if (flag_ != RpSolution.CONNECTED)
//        throw new RpException("Error in Connection Calc - " + flag_);*/
//
//        return new ConnectionOrbit(manifoldOrbitA_.getStationaryPoint(), manifoldOrbitB_.getStationaryPoint(),
//            createConnectingOrbit());
//    }
//
//    /*
//      This will enable step by step analysis using getManifoldA/B()
//      */
//
//    public void connectionIterationStep() {
//        iterationNumber_ = iterationNumber_ + 1;
//        ManifoldSensitivity manifoldSensitivityA = null;
//        ManifoldSensitivity manifoldSensitivityB = null;
//        manifoldSensitivityA = manifoldOrbitA_.sensitivity();
//        manifoldSensitivityB = manifoldOrbitB_.sensitivity();
//        // extracting information on first orbit
//        RealVector x_StationaryA = new RealVector(manifoldSensitivityA.getx_Stationary());
//        RealVector Xp_StationaryA = new RealVector(manifoldSensitivityA.getXp_Stationary());
//        RealVector x_t0A = new RealVector(manifoldSensitivityA.getx_t0());
//        RealMatrix2 X_t0A = new RealMatrix2(manifoldSensitivityA.getX_t0());
//        RealVector x_t1A = new RealVector(manifoldSensitivityA.getx_t1());
//        RealMatrix2 X_t1A = new RealMatrix2(manifoldSensitivityA.getX_t1());
//        RealVector Xp_t1A = new RealVector(manifoldSensitivityA.getXp_t1());
//        int kA = X_t0A.getNumCol();
//        // extracting information on second orbit
//        RealVector x_StationaryB = new RealVector(manifoldSensitivityB.getx_Stationary());
//        RealVector Xp_StationaryB = new RealVector(manifoldSensitivityB.getXp_Stationary());
//        RealVector x_t0B = new RealVector(manifoldSensitivityB.getx_t0());
//        RealMatrix2 X_t0B = new RealMatrix2(manifoldSensitivityB.getX_t0());
//        RealVector x_t1B = new RealVector(manifoldSensitivityB.getx_t1());
//        RealMatrix2 X_t1B = new RealMatrix2(manifoldSensitivityB.getX_t1());
//        RealVector Xp_t1B = new RealVector(manifoldSensitivityB.getXp_t1());
//        int kB = X_t0B.getNumCol();
//        int m = x_t0B.getSize();
//        // filling matrix A and vectod b
//        RealMatrix2 A = new RealMatrix2(m + 1, kA + kB + 1);
//        X_t1A.copySubMatrix(0, 0, m, kA, 0, 0, A);
//        RealMatrix2 minusX_t1B = new RealMatrix2(X_t1B);
//        minusX_t1B.negate();
//        minusX_t1B.copySubMatrix(0, 0, m, kB, 0, kA, A);
//        int i;
//        for (i = 0; i < m; i++)
//            A.setElement(i, m, Xp_t1A.getElement(i) - Xp_t1B.getElement(i));
//        RealVector n = new RealVector(RPNUMERICS.pSection().getNormal());
//        RealVector tmp = new RealVector(m);
//        for (i = 0; i < kA; i++) {
//            X_t1A.getColumn(i, tmp);
//            A.setElement(m, i, n.dot(tmp));
//        }
//        A.setElement(m, m, n.dot(Xp_t1A));
//        // checking the length of the step
//        RealVector dx = new RealVector(m);
//        dx.sub(x_t1B, x_t1A);
//        RealVector dLastPoint = new RealVector(dx);
//        double dx_norm = RPNUMERICS.errorControl().ode().stateVectorNorm(dx);
//        if (dx_norm > RPNUMERICS.errorControl().conn().maxPSectionStepLength())
//            dx.scale(RPNUMERICS.errorControl().conn().maxPSectionStepLength() / dx_norm);
//        RealVector b = new RealVector(m + 1);
//        for (i = 0; i < m; i++)
//            b.setElement(i, dx.getElement(i));
//        // solve equation A g = b
//        RealVector g = new RealVector(wave.util.MathUtil.linearSolver(A, b));
//        double dsigma = g.getElement(m);
//        if (RPNUMERICS.errorControl().conn().sigmaNorm(dsigma) > RPNUMERICS.errorControl().conn().maxSigmaStepLength()) {
//            double reduce = RPNUMERICS.errorControl().conn().maxSigmaStepLength() /
//                RPNUMERICS.errorControl().conn().sigmaNorm(dsigma);
//            g.scale(reduce);
//            dsigma = g.getElement(m);
//        }
//        RealVector xiA = new RealVector(kA);
//        for (i = 0; i < kA; i++)
//            xiA.setElement(i, g.getElement(i));
//        RealVector xiB = new RealVector(kB);
//        for (i = 0; i < kB; i++)
//            xiB.setElement(i, g.getElement(kA + i));
//        RealVector dFirstPointA = new RealVector(newPointShift(x_StationaryA, x_t0A, X_t0A, xiA));
//        RealVector dFirstPointB = new RealVector(newPointShift(x_StationaryB, x_t0B, X_t0B, xiB));
//
//
//
//        double sigma =RPNUMERICS.getShockProfile().getSigma()+dsigma;//(ConservationShockFlow)RPNUMERICS.flow()).getSigma() + dsigma;
//        RPNUMERICS.getShockProfile().setSigma(sigma);
////        ((ConservationShockFlow)RPNUMERICS.flow()).setSigma(sigma);
//
//        // calculation of new stationary points
//        StationaryPoint stationaryPointA = null;
//        StationaryPoint stationaryPointB = null;
//        try {
//            tmp.set(x_StationaryA);
//            Xp_StationaryA.scale(dsigma);
//            tmp.add(Xp_StationaryA);
//            StationaryPointCalc calcA = new StationaryPointCalc(new PhasePoint(tmp),flow_);
//            stationaryPointA = (StationaryPoint)calcA.calc();
//            tmp.set(x_StationaryB);
//            Xp_StationaryB.scale(dsigma);
//            tmp.add(Xp_StationaryB);
//            StationaryPointCalc calcB = new StationaryPointCalc(new PhasePoint(tmp),flow_);
//            stationaryPointB = (StationaryPoint)calcB.calc();
//        } catch (RpException ex) { ex.printStackTrace(); }
//        RealVector firstPointA = new RealVector(m);
//        RealVector firstPointB = new RealVector(m);
//        firstPointA.add(new RealVector(stationaryPointA.getPoint().getCoords()), dFirstPointA);
//        firstPointB.add(new RealVector(stationaryPointB.getPoint().getCoords()), dFirstPointB);
        // calculation of new manifold orbits
//        try {
//            ManifoldOrbitCalc calcManA = RPNUMERICS.createManifoldCalc(stationaryPointA, stationaryPointA, manifoldOrbitA_.getTimeDirection());//( i)new ManifoldOrbitCalc(stationaryPointA,
//               // new PhasePoint(firstPointA), manifoldOrbitA_.getTimeDirection());
//            manifoldOrbitA_ = (ManifoldOrbit)calcManA.calc();
//            ManifoldOrbitCalc calcManB = RPNUMERICS.createManifoldCalc(stationaryPointB, stationaryPointB, manifoldOrbitB_.getTimeDirection());// i)new ManifoldOrbitCalc(stationaryPointB,
////                new PhasePoint(firstPointB), manifoldOrbitB_.getTimeDirection());
////
////            ManifoldOrbitCalc calcManB = new ManifoldOrbitCalc(stationaryPointB,
////                new PhasePoint(firstPointB), manifoldOrbitB_.getTimeDirection());
//            manifoldOrbitB_ = (ManifoldOrbit)calcManB.calc();
//        } catch (RpException ex) { ex.printStackTrace(); }
        // final check
//        if ((manifoldOrbitA_.getFinishType() != ODESolution.STOP_ON_POINCARE_SECTION) ||
//            (manifoldOrbitB_.getFinishType() != ODESolution.STOP_ON_POINCARE_SECTION))
//                flag_ = RpSolution.NO_POINCARE_SECTION;
//        else {
//            RealVector lastPointA = new RealVector(manifoldOrbitA_.getOrbit().getPoints()
//                [manifoldOrbitA_.getOrbit().getPoints().length - 1].getCoords());
//            RealVector lastPointB = new RealVector(manifoldOrbitB_.getOrbit().getPoints()
//                [manifoldOrbitB_.getOrbit().getPoints().length - 1].getCoords());
//            RealVector dLastPointNew = new RealVector(m);
//            dLastPointNew.sub(lastPointB, lastPointA);
//            if (RPNUMERICS.errorControl().ode().stateVectorNorm(dLastPointNew) >
//                RPNUMERICS.errorControl().ode().stateVectorNorm(dLastPoint))
//                    flag_ = RpSolution.DEVIATION_INCREASED;
//            else
//                flag_ = RpSolution.CONNECTION_NOT_FINISHED;
//            if (RPNUMERICS.errorControl().ode().stateVectorNorm(dLastPointNew) < RPNUMERICS.errorControl().eps()) {
//                flag_ = RpSolution.CONNECTED;
//                // SET ACCURACY !!!
//                sigmaAccuracy_ = 0;
//            }
//        }
    }

    // updating initial point for the manifold orbit
    protected RealVector newPointShift(RealVector x_Stat, RealVector x_t0, RealMatrix2 X_t0, RealVector xi) {
        int m = x_t0.getSize();
        RealVector ddx0 = new RealVector(m);
        ddx0.mul(X_t0, xi);
//        RealVector f0 = new RealVector(RPNUMERICS.flow().flux(x_t0));

//        WaveState input = new WaveState(new PhasePoint(x_t0));
//        JetMatrix output = new JetMatrix (m);
//        
//        flow_.jet(input, output, 0);
        
        RealVector f0= flow_.flux(x_t0);
        
        
        
//        RealVector f0 = flow_.flux((x_t0));
        
        RealVector scaleF = new RealVector(m);
        scaleF.scale(f0.dot(ddx0) / f0.dot(f0), f0);
        ddx0.sub(scaleF);
        ddx0.add(x_t0);
        ddx0.sub(x_Stat);
        return ddx0;
    }

    public String getCalcMethodName() {
        return methodCalcName_;

    }

    public RpSolution recalc(Area area) throws RpException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
