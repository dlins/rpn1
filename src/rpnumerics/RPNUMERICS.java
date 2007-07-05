/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpnumerics;

import rpnumerics.AccumulationFunction;
import rpnumerics.FluxFunction;
import wave.util.*;
import wave.multid.Space;
import rpnumerics.Physics;
import rpnumerics.AccumulationFunction;
import rpnumerics.FluxFunction;
import rpnumerics.Physics;
import rpnumerics.ode.ODESolver;
import rpnumerics.ode.ODESolverProfile;

public class RPNUMERICS {
    //
    // Constants
    //

    static public int INCREASING_LAMBDA = 0;
    //
    // Members
    //
    static private ODESolver odeSolver_ = null;
    static private RPNumericsProfile profile_;
    static private HugoniotCurveCalc hugoniotCurveCalc_ = null;

    //
    // Constructors/Initializers
    //
    static public void init(RPNumericsProfile profile) throws RpException {

        profile_ = profile;


        if (profile_.hasBoundary()) {

            RPNUMERICS.getPhysics().setBoundary(profile_.getBoundary());
        }

        initODESolver();

    }

    static public void resetShockFlow(PhasePoint xZero, double sigma) {

        initODESolver();

    }

    static public void resetRarefactionFlow(PhasePoint xZero) {

        if (profile_.getFlowType().equals("rarefactionflow")) {

        }

        if (profile_.getFlowType().equals("blowuprarefactionflow")) {


        }

        System.out.println("RarefactionFlow: " + profile_.getFlowType());

        System.out.println("FamilyIndex: " +
                           profile_.getFamilyIndex());

        RealVector scales = new RealVector(xZero.getSize());

        for (int i = 0; i < scales.getSize(); i++) {
            scales.setElement(i, 1);
        }

//        errorControl_ = new RpErrorControl(scales);
        initODESolver();

    }


    static public void initODESolver() {

//        odeSolver_ = new Rk4BPMethod(
//                new Rk4BPProfile(new FlowVectorField(flow()),
//                                 errorControl().eps(),
//                                 errorControl().ode().maxStateStepLength(),
//                                 boundary(), errorControl().ode().getScale(),
//                                 errorControl().ode().maxNumberOfSteps()));

    }


    //
    // Accessors
    //

    static public final String physicsID() {
        
        return null;
//        return physics_.ID();
    }

    static public final FluxFunction fluxFunction() {
//        return physics_.fluxFunction();
        
        return null;
    }

    static public final Boundary boundary() {
//        return physics_.boundary();
        return null;
    }

    static public final Space domain() {
//        return physics_.domain();
        return null;
    }

    static public final Physics getPhysics() {
//        return physics_;
        return null;
        
    }

    static public final HugoniotCurveCalc hugoniotCurveCalc() {
        return hugoniotCurveCalc_;
    }

    static public final WaveFlow flow() {
        return null;
    }

    static public final AccumulationFunction accumulationFunction() {
       return null;
    }

   
    static public final SimplexPoincareSection pSection() {
        return ((ODESolverProfile) odeSolver_.getProfile()).getPoincareSection();
    }

    static public final RPNumericsProfile getProfile() {
        return profile_;
    }

    static public final boolean isNativePhysics() {
        return profile_.isNativePhysics();
    }

    static public final ODESolver odeSolver() {
        return odeSolver_;
    }

    static public final int domainDim() {
        return domain().getDim();
    }


}
