/*
*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package rpnumerics;

import wave.util.Boundary;
import wave.util.RealVector;

public class RpErrorControl {
    //
    // Constants
    //
    static final public double DEFAULT_EPS = 1E-6;
    static final double MAX_PRECISION = 1.0e-14;
    static final int MAX_ITERATIONS_NUMBER_FOR_STATIONARY_POINT_COMPUTATION = 15;
    static final double NUMERICAL_ZERO_FOR_EIGEN_COMPUTATION = 1.0e-6;
    //
    // Members
    //
    private ODEErrorModule odeModule_;
    private ConnectionsErrorModule connModule_;
    private double eps_;

    //
    // Inners/Initializers
    //
    public static final class ODEErrorModule {
        //
        // Constants
        //
        static final double DEFAULT_MAX_STATE_STEP_LENGTH = 1E-2;
        static final double DEFAULT_STATIONARY_POINT_MIN_DISTANCE = 1E-5;
        static final double DEFAULT_STATIONARY_POINT_PREF_DISTANCE = Math.sqrt(RpErrorControl.DEFAULT_EPS);
        static final int DEFAULT_STEP_MAX = 500;
        //
        // Members
        //
        double maxStateStepLength_ = DEFAULT_MAX_STATE_STEP_LENGTH;

        double[] statPointDistance_ = new double[] {
            DEFAULT_STATIONARY_POINT_MIN_DISTANCE, DEFAULT_STATIONARY_POINT_PREF_DISTANCE
        };

        // we should have n factors where n = problem dimension
        double[] stateScalingFactors_ = new double[RPNUMERICS.domainDim()];

        //
        // Initializers
        //
        public ODEErrorModule(double eps, Boundary boundary) {
            reset(eps, boundary);
        }

        public ODEErrorModule(double eps, RealVector scales) {
            reset(eps, scales);
        }

        protected void reset(double eps, RealVector scales) {

            statPointDistance_[1] = Math.sqrt(eps);
	    stateScalingFactors_  = new double[scales.getSize()];

            for (int j = 0; j < scales.getSize(); j++) 
	    	stateScalingFactors_[j] = scales.getElement(j);

        }

        protected void reset(double eps, Boundary boundary) {

            statPointDistance_[1] = Math.sqrt(eps);
            // by default we initiate with boundary values
            for (int j = 0; j < stateScalingFactors_.length; j++) 
                // Coordinate Span
                stateScalingFactors_[j] = boundary.getMaximums().getElement(j) - boundary.getMinimums().getElement(j);
            
        }

        //
        // Accessors
        //
        public double maxStateStepLength() { return maxStateStepLength_; }

        public double minStationaryPointDistance() { return statPointDistance_[0]; }

        public double prefStationaryPointDistance() { return statPointDistance_[1]; }

        public int maxNumberOfSteps() { return DEFAULT_STEP_MAX; }

        public double stateVectorNorm(RealVector y) {
            RealVector norm = new RealVector(y.getSize());
            for (int i = 0; i < y.getSize(); i++)
                norm.setElement(i, y.getElement(i) / stateScalingFactors_[i]);
            return y.norm();
        }

        // should access the norm function directly...
        public RealVector getScale() { return new RealVector(stateScalingFactors_); }
    }


    static final class ConnectionsErrorModule {
        //
        // Constants
        //
        static final double DEFAULT_MAX_SIGMA_STEP_LENGTH = 5E-2;
        static final double DEFAULT_MAX_STATE_STEP_LENGTH = 5E-2;
        static final double DEFAULT_SIGMA_SCALING_FACTOR = 5E-2;
        //
        // Members
        //
        double maxSigmaStepLength_ = DEFAULT_MAX_SIGMA_STEP_LENGTH;
        double maxPSectionStepLength_ = DEFAULT_MAX_STATE_STEP_LENGTH;
        double sigmaScalingFactor_ = DEFAULT_SIGMA_SCALING_FACTOR;

        //
        // Initializers
        //
        public ConnectionsErrorModule(double eps) {
        }

        public void reset(double eps) { }

        //
        // Accessors
        //
        public double maxSigmaStepLength() { return maxSigmaStepLength_; }

        public double maxPSectionStepLength() { return maxPSectionStepLength_; }

        double sigmaNorm(double sigma) {
            return sigma / sigmaScalingFactor_;
        }
    }


    public RpErrorControl(Boundary boundary) {
        eps_ = DEFAULT_EPS;
        odeModule_ = new ODEErrorModule(eps_, boundary);
        connModule_ = new ConnectionsErrorModule(eps_);
    }

    public RpErrorControl(RealVector scales) {
        eps_ = DEFAULT_EPS;
        odeModule_ = new ODEErrorModule(eps_, scales);
        connModule_ = new ConnectionsErrorModule(eps_);
    }

    public void reset(double eps, Boundary boundary) {
        eps_ = eps;
        odeModule_.reset(eps, boundary);
        connModule_.reset(eps);
    }

    //
    // Accessors
    //
    public final ODEErrorModule ode() { return odeModule_; }

    public final ConnectionsErrorModule conn() { return connModule_; }

    public final double eps() { return eps_; }
}
