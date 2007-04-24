/*
*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package rpnumerics;

//
// finds a stationary point of the vector field f
// using Newton method with the starting point x0
//
// fills the local information of eigenvalues, eigenvectors, schur decompositions
// the point initial_ is taken as an initial guess.
//
import wave.util.*;
import wave.util.RealVector;
import wave.util.RealMatrix2;

public class StationaryPointCalc implements RpCalculation {
    //
    // Constants
    //

    //
    // Members
    //
    private PhasePoint initial_;

    //
    // Constructors
    //
    public StationaryPointCalc(PhasePoint initial) {
        initial_ = initial;
    }

    //
    // Accessors/Mutators
    //
    public PhasePoint getInitPoint() { return initial_; }

    public void setInitPoint(PhasePoint initial) { initial_ = initial; }

    //
    // Methods
    //
    public RpSolution recalc() throws RpException {
        return calc();
    }

    public RpSolution calc() throws RpException {

	int stateSpaceDim = initial_.getCoords().getSize();

        double[] eigenValR = new double[stateSpaceDim];
        double[] eigenValI = new double[stateSpaceDim];
        RealVector[] eigenVec = new RealVector[stateSpaceDim];
        int DimP = 0;
        RealMatrix2 schurFormP = new RealMatrix2(stateSpaceDim, stateSpaceDim);
        RealMatrix2 schurVecP = new RealMatrix2(stateSpaceDim, stateSpaceDim);
        int DimN = 0;
        RealMatrix2 schurFormN = new RealMatrix2(stateSpaceDim, stateSpaceDim);
        RealMatrix2 schurVecN = new RealMatrix2(stateSpaceDim, stateSpaceDim);
        PhasePoint point = null;
        int integrationFlag;
        RealVector x0 = new RealVector(initial_.getCoords());
        // evaluation of f and df
        RealVector f0 = RPNUMERICS.flow().flux(x0);
        RealMatrix2 df = new RealMatrix2(RPNUMERICS.flow().fluxDeriv(x0));
        // making the first Newton iteration
        int i = 1;
        df.invert();
        RealVector dx = new RealVector(x0.getSize());
        dx.mul(df, f0);
        x0.sub(x0, dx);
        // secuence of Newton iterations
        while ((RPNUMERICS.errorControl().ode().stateVectorNorm(dx) >= RpErrorControl.MAX_PRECISION) &&
            (i < RpErrorControl.MAX_ITERATIONS_NUMBER_FOR_STATIONARY_POINT_COMPUTATION)) {
                f0 = RPNUMERICS.flow().flux(x0);
                df = new RealMatrix2(RPNUMERICS.flow().fluxDeriv(x0));
                i = i + 1;
                df.invert();
                dx.mul(df, f0);
                x0.sub(x0, dx);
        }
        if (i == RpErrorControl.MAX_ITERATIONS_NUMBER_FOR_STATIONARY_POINT_COMPUTATION) {
            integrationFlag = RpSolution.NO_CONVERGENCE_IN_STATIONARY_POINT_COMPUTATION;
            throw new RpException("Error in StationaryPointCalc : " + integrationFlag);
        }
        else if (!RPNUMERICS.boundary().inside(x0)) {
            integrationFlag = RpSolution.FOUND_OUT_OF_BOUNDARY;
            throw new RpException("Error in StationaryPointCalc : " + integrationFlag);
        }
        else {
            integrationFlag = RpSolution.CONVERGENCE_IN_STATIONARY_POINT_COMPUTATION;
            df = new RealMatrix2(RPNUMERICS.flow().fluxDeriv(x0));
            // schur decomposition with negative real part eigenvalues first
            schurFormP = new RealMatrix2(df);
            DimP = schurFormP.fillSchurData(stateSpaceDim, new LapackSelectPos(), schurFormP, schurVecP);
            // schur decomposition with positive real part eigenvalues first
            schurFormN = new RealMatrix2(df);
            DimN =  schurFormN.fillSchurData(stateSpaceDim, new LapackSelectNeg(), schurFormN, schurVecN);
            // eigenvalues and eigenvectors computation
            schurFormN.fillEigenData(stateSpaceDim, df, eigenValR, eigenValI, eigenVec);
            // getting eigenvalues and eigenvectors sorted
            // with increasing absolute value of real part
            RealVector.sortEigenData(stateSpaceDim, eigenValR, eigenValI, eigenVec);
        }
        point = new PhasePoint(x0);
        return new StationaryPoint(point, eigenValR, eigenValI, eigenVec, DimP, schurFormP, schurVecP, DimN, schurFormN,
            schurVecN, integrationFlag);
    }


    // function sorting eigenvalues and eigenvalues with
    // increase of real part of eigenvalues

}
