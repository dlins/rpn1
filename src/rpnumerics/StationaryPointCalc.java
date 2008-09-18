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
    private ShockFlow flow_;
    private String methodCalcName_ = "default";
    //
    // Constructors
    //
//    public StationaryPointCalc(PhasePoint initial) {
//
//        initial_ = initial;
//        flowName_ = RPNUMERICS.getShockProfile().getFlowName();//TODO Pass flow name as parameter ??
//    }
    public StationaryPointCalc(PhasePoint initial, ShockFlow flow) {
        initial_ = initial;
        flow_ = flow;

    }

    public StationaryPointCalc(PhasePoint initial, ShockFlow flow, String methodCalcName) {
        initial_ = initial;
        flow_ = flow;
        methodCalcName_ = methodCalcName;


    }

    //
    // Accessors/Mutators
    //
    public PhasePoint getInitPoint() {
        return initial_;
    }

    public void setInitPoint(PhasePoint initial) {
        initial_ = initial;
    }

    //
    // Methods
    //
    public RpSolution recalc() throws RpException {
        return calc();
    }

    public RpSolution calc() throws RpException {

        int stateSpaceDim = initial_.getCoords().getSize();

        int i, j;

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
//        RealVector x0 = new RealVector(initial_.getCoords());
        // evaluation of f and df
//        RealVector f0 = RPNUMERICS.flow().flux(x0);

        WaveState x0 = new WaveState(new PhasePoint(initial_));
        JetMatrix output = new JetMatrix(stateSpaceDim);

        flow_.jet(x0, output, 1);

        RealVector f0 = output.f();


//        RealVector f0 = flow_.flux(x0);//RPNUMERICS.flow().flux(x0);
//        RealMatrix2 df = new RealMatrix2(RPNUMERICS.flow().fluxDeriv(x0));
//        RealMatrix2 df = flow_.fluxDeriv(x0);//new RealMatrix2(RPNUMERICS.flow().fluxDeriv(x0));
        RealMatrix2 df = new RealMatrix2(stateSpaceDim, stateSpaceDim);

        JacobianMatrix jacobian = output.jacobian();

        for (i = 0; i < stateSpaceDim; i++) {
            for (j = 0; j < stateSpaceDim; j++) {

                df.setElement(i, j, output.getElement(i, j));
            }
        }

        // making the first Newton iteration
        i = 1;
        df.invert();
//        RealVector dx = new RealVector(x0.getSize());

        RealVector dx = new RealVector(stateSpaceDim);



        dx.mul(df, f0);
//        x0.sub(x0, dx);

        x0.initialState().sub(x0.initialState().getCoords(), dx);

        // secuence of Newton iterations
        while ((RPNUMERICS.errorControl().ode().stateVectorNorm(dx) >= RpErrorControl.MAX_PRECISION) &&
                (i < RpErrorControl.MAX_ITERATIONS_NUMBER_FOR_STATIONARY_POINT_COMPUTATION)) {
//                f0 = RPNUMERICS.flow().flux(x0);
//            f0 = flow_.flux(x0);//RPNUMERICS.flow().flux(x0);
            flow_.jet(x0, output, 1);
            f0 = output.f();

//            df = new RealMatrix2(RPNUMERICS.flow().fluxDeriv(x0));

//            df = new RealMatrix2(flow_.fluxDeriv(x0));

            for (i = 0; i < stateSpaceDim; i++) {
                for (j = 0; j < stateSpaceDim; j++) {

                    df.setElement(i, j, output.getElement(i, j));
                }
            }


            i = i + 1;
            df.invert();
            dx.mul(df, f0);
//            x0.sub(x0, dx);


            x0.initialState().sub(x0.initialState().getCoords(), dx);



        }
        if (i == RpErrorControl.MAX_ITERATIONS_NUMBER_FOR_STATIONARY_POINT_COMPUTATION) {
            integrationFlag = RpSolution.NO_CONVERGENCE_IN_STATIONARY_POINT_COMPUTATION;
            throw new RpException("Error in StationaryPointCalc : " + integrationFlag);
        } else if (!RPNUMERICS.boundary().inside(x0.initialState().getCoords())) {  //if (!RPNUMERICS.boundary().inside(x0))
            integrationFlag = RpSolution.FOUND_OUT_OF_BOUNDARY;
            throw new RpException("Error in StationaryPointCalc : " + integrationFlag);
        } else {
            integrationFlag = RpSolution.CONVERGENCE_IN_STATIONARY_POINT_COMPUTATION;
//            df = new RealMatrix2(RPNUMERICS.flow().fluxDeriv(x0));

//            df = new RealMatrix2(flow_.fluxDeriv(x0));//RPNUMERICS.flow().fluxDeriv(x0));

            flow_.jet(x0, output, 1);
            for (i = 0; i < stateSpaceDim; i++) {
                for (j = 0; j < stateSpaceDim; j++) {

                    df.setElement(i, j, output.getElement(i, j));
                }
            }


            // schur decomposition with negative real part eigenvalues first
            schurFormP = new RealMatrix2(df);
            DimP = schurFormP.fillSchurData(stateSpaceDim, new LapackSelectPos(), schurFormP, schurVecP);
            // schur decomposition with positive real part eigenvalues first
            schurFormN = new RealMatrix2(df);
            DimN = schurFormN.fillSchurData(stateSpaceDim, new LapackSelectNeg(), schurFormN, schurVecN);
            // eigenvalues and eigenvectors computation
            schurFormN.fillEigenData(stateSpaceDim, df, eigenValR, eigenValI, eigenVec);
            // getting eigenvalues and eigenvectors sorted
            // with increasing absolute value of real part
            RealVector.sortEigenData(stateSpaceDim, eigenValR, eigenValI, eigenVec);
        }
        point = new PhasePoint(x0.initialState());
        return new StationaryPoint(point, eigenValR, eigenValI, eigenVec, DimP, schurFormP, schurVecP, DimN, schurFormN,
                schurVecN, integrationFlag);
    }

    public String getCalcMethodName() {
        return methodCalcName_;
    }

    public WaveFlow getFlow() {
        return flow_;

    }
    // function sorting eigenvalues and eigenvalues with
    // increase of real part of eigenvalues
}
