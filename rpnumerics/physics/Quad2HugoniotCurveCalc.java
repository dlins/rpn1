/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpnumerics.physics;

import wave.util.MathUtil;
import java.util.ArrayList;
import wave.util.RealVector;
import wave.util.RealMatrix2;
import rpnumerics.PhasePoint;
import rpnumerics.StationaryPoint;
import rpnumerics.WaveState;
import rpnumerics.HugoniotCurve;
import rpnumerics.RpSolution;
import rpnumerics.HugoniotCurveCalc;


/*
	This is a Rankine-Hugoniot curve hardcoded to the Quad physics. It
loops the PHI angle from 0 to PI/2 and calls the calcxy routine which
calculates a point HUGXY of the Rankine-Hugoniot curve ( shock curve )
given the base point HORIGN ( Hugoniot-Origin) ( i.e. the specified
stationary point). It uses polar coordinates, so it computes the point
at the angle PHI.

* FORTRAN implementation translation

TODO : calcxy sigma explicit formula...
*/

public class Quad2HugoniotCurveCalc implements HugoniotCurveCalc {
    //
    // Constants
    //
    static public final double DELTA_PHI = 100d;
    static public final double DEFAULT_SIGMA = 0d;
    static public final int DEFAULT_FLAG = 0;
    //
    // Members
    //
    //    private StationaryPoint origin_;

    private PhasePoint origin_;
	//  private Quad2Params params_;
    private FluxParams params_;

    private double alpha1_;
    private double beta1_;
    private double gamma1_;
    private double alpha2_;
    private double beta2_;
    private double gamma2_;
    private double alpha0_;
    private double beta0_;
    private double gamma0_;
    private double alpht1_;
    private double betat1_;
    private double gammt1_;
    private double alpht2_;
    private double betat2_;
    private double gammt2_;
    private double alpht0_;
    private double betat0_;
    private double gammt0_;
    private int homog_;

    //
    // Constructors
    //
    // public Quad2HugoniotCurveCalc(Quad2Params params, StationaryPoint origin) {
//         params_ = new Quad2Params(params);
//         origin_ = origin;
//         inicff();
//     }


    public Quad2HugoniotCurveCalc(FluxParams params, PhasePoint origin) {
        params_ = params;
        origin_ = origin;
        inicff();
    }





    //
    // Accessors/Mutators
    //
    public double qcoef_alpha(double c2phi, double s2phi) {
        return alpha1_ * c2phi + alpha2_ * s2phi + alpha0_;
    }

    public double qcoef_beta(double c2phi, double s2phi) {
        return beta1_ * c2phi + beta2_ * s2phi + beta0_;
    }

    public double qcoef_gamma(double c2phi, double s2phi) {
        return gamma1_ * c2phi + gamma2_ * s2phi + gamma0_;
    }

    //
    // Methods
    //
    public RpSolution recalc() { return calc(); }

    public RpSolution calc() {
        ArrayList statesVector = new ArrayList();
        double phi = 0d;
        do {
            statesVector.add(calcState(phi));
            phi += Math.PI / DELTA_PHI;
        } while (phi < (Math.PI));
        WaveState[] states = new WaveState[statesVector.size()];
        for (int i = 0; i < statesVector.size(); i++)
            states[i] = (WaveState)statesVector.get(i);

	return new HugoniotCurve (origin_,states);
	//        return new WaveCurve(states);
    }

    protected WaveState calcState(double phi) {
        double ul, vl;
        double alphai, betai, gammai;
        double c2phi, s2phi, cosphi, sinphi, cbif, sbif, r;
        double aa1, aa2, aa3, bb1, bb2, bb3, cc1, cc2, cc3;
        double k1, k2, k3, k4, kt1, kt2, kt3;
        double numer, denom;
	//      RealVector horign = new RealVector(origin_.getPoint().getCoords());

        RealVector horign = new RealVector(origin_.getCoords());
        ul = horign.getElement(0);
        vl = horign.getElement(1);
        cosphi = Math.cos(phi);
        sinphi = Math.sin(phi);
        // generic point
        c2phi = cosphi * cosphi - sinphi * sinphi;
        s2phi = 2. * cosphi * sinphi;
        alphai = qcoef_alpha(c2phi, s2phi);
        betai = qcoef_beta(c2phi, s2phi);
        gammai = qcoef_gamma(c2phi, s2phi);
        denom = alphai * cosphi + betai * sinphi;
        numer = -2. * (alphai * ul + betai * vl + gammai);
        //c to do:  fix this
        if (Math.abs(denom) <= 1.0e-3 * Math.abs(numer))
            r = 1.0e3 * MathUtil.sign(1.0, denom) * MathUtil.sign(1.0, numer);
        else
            r = numer / denom;
        RealVector hugxy = new RealVector(2);
        hugxy.setElement(0, horign.getElement(0) + r * cosphi);
        hugxy.setElement(1, horign.getElement(1) + r * sinphi);
        // creates a dummy stat point eigen/schur
        double[] eigenValR = new double[2];
        double[] eigenValI = new double[2];
        RealVector[] eigenVec = new RealVector[2];
        int dimP = 2;
        RealMatrix2 schurFormP = new RealMatrix2(2, 2);
        RealMatrix2 schurVecP = new RealMatrix2(2, 2);
        int dimN = 2;
        RealMatrix2 schurFormN = new RealMatrix2(2, 2);
        RealMatrix2 schurVecN = new RealMatrix2(2, 2);
        PhasePoint pPoint = new PhasePoint(hugxy);
        StationaryPoint x0 = new StationaryPoint(pPoint, eigenValR, eigenValI, eigenVec, dimP, schurFormP, schurVecP, dimN,
            schurFormN, schurVecN, RpSolution.CONVERGENCE_IN_STATIONARY_POINT_COMPUTATION);
        return new WaveState(origin_, x0, DEFAULT_SIGMA);
    }

    public void inicff() {
        double a1 = params_.getElement(0);
        double b1 = params_.getElement(0);
        double c1 = params_.getElement(0);
        double d1 = params_.getElement(0);
        double e1 = params_.getElement(0);
        double a2 = params_.getElement(0);
        double b2 = params_.getElement(0);
        double c2 = params_.getElement(0);
        double d2 = params_.getElement(0);
        double e2 = params_.getElement(0);
        alpha1_ = .5 * (a2 + b1);
        beta1_ = .5 * (b2 + c1);
        gamma1_ = .5 * (d2 + e1);
        alpha2_ = .5 * (b2 - a1);
        beta2_ = .5 * (c2 - b1);
        gamma2_ = .5 * (e2 - d1);
        alpha0_ = .5 * (a2 - b1);
        beta0_ = .5 * (b2 - c1);
        gamma0_ = .5 * (d2 - e1);
        alpht1_ = -alpha2_;
        betat1_ = -beta2_;
        gammt1_ = -gamma2_;
        alpht2_ = alpha1_;
        betat2_ = beta1_;
        gammt2_ = gamma1_;
        alpht0_ = .5 * (a1 + b2);
        betat0_ = .5 * (b1 + c2);
        gammt0_ = .5 * (d1 + e2);



/* c  TO DO:  initialize the following

c     eta, homog:
c     nascoi, ascoi(2):  roots of det -- asymptotic angles for coincidence locus
c     nasshk, asshk(3):  roots of mu -- bifurcation angles
c                        (cf. setbif; the bifang common should be replaced)
c     nasinf, asinf(3):  roots of mut*det + .5*mu*detp -- inflection asymptotes
c     nashys, ashys(3):  roots of ??? -- hysteresis asymptotes
c     nxinfl, xinfl(2):  roots of dett -- exceptional inflection angles
*/

        if (Math.abs(gamma0_) < .001)
            homog_ = 1;
        else
            homog_ = 0;
        //c  TO DO:  various coefficients associated with visrpn.ty
    }

    public void uMinusChangeNotify(PhasePoint phasePoint){


	origin_=phasePoint;

    }

    public PhasePoint getUMinus(){return origin_;}
    public double[] getPrimitiveUMinus(){return origin_.getCoords().toDouble();}

        public RealVector getFMinus(){return null;}
        public RealMatrix2 getDFMinus() {return null;}






}

