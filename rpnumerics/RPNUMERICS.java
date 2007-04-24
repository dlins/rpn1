/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpnumerics;

import wave.util.*;
import wave.ode.*;
import rpnumerics.physics.*;
import wave.multid.Space;
import rpnumerics.ShockFlowParams;
import rpnumerics.HugoniotParams;

public class RPNUMERICS {
    //
    // Constants
    //

    static public int INCREASING_LAMBDA = 0;
    //
    // Members
    //
    static private Physics physics_ = null;
    static private RpErrorControl errorControl_ = null;
    static private ODESolver odeSolver_ = null;
    static private RPNumericsProfile profile_;
    static private HugoniotCurveCalc hugoniotCurveCalc_ = null;
    static private WaveFlow flow_ = null;

    //
    // Constructors/Initializers
    //
    static public void init(RPNumericsProfile profile) throws RpException {

        profile_ = profile;
        physics_ = rpnumerics.factories.FluxFunctionFactory.physicsCreation(
                profile);

        if (profile_.hasBoundary()) {

            RPNUMERICS.getPhysics().setBoundary(profile_.getBoundary());
        }

        errorControl_ = new RpErrorControl(boundary());
        initODESolver();

    }

    static public void resetShockFlow(PhasePoint xZero, double sigma) {

        HugoniotParams hparams = new HugoniotParams(xZero);

        hugoniotCurveCalc_ = rpnumerics.factories.HugoniotFactory.
                             hugoniotCurveCalcCreate(profile_, hparams);

        flow_ = rpnumerics.factories.WaveFlowFactory.shockFlowCreate(new
                ShockFlowParams(xZero, sigma), profile_);
        initODESolver();

    }

    static public void resetRarefactionFlow(PhasePoint xZero) {

        if (profile_.getFlowType().equals("rarefactionflow")) {

            flow_ = rpnumerics.factories.WaveFlowFactory.rarefactionFlowCreate(
                    xZero, profile_);
        }

        if (profile_.getFlowType().equals("blowuprarefactionflow")) {

            BlowUpLineFieldVector blowUpUserInput = new BlowUpLineFieldVector(
                    xZero.
                    getCoords(), profile_.getFamilyIndex());
//            RealVector scales = new RealVector(blowUpUserInput);
//
//            for (int i = 0; i < scales.getSize(); i++) {
//                scales.setElement(i, 1);
//            }

//            errorControl_ = new RpErrorControl(scales);
            flow_ = rpnumerics.factories.WaveFlowFactory.blowUpFlowCreate(
                    blowUpUserInput, profile_);

        }

        System.out.println("RarefactionFlow: " + profile_.getFlowType());

        System.out.println("FamilyIndex: " +
                           profile_.getFamilyIndex());

        RealVector scales = new RealVector(xZero.getSize());

        for (int i = 0; i < scales.getSize(); i++) {
            scales.setElement(i, 1);
        }

        errorControl_ = new RpErrorControl(scales);
        initODESolver();

    }


    static public void initODESolver() {

        odeSolver_ = new Rk4BPMethod(
                new Rk4BPProfile(new FlowVectorField(flow()),
                                 errorControl().eps(),
                                 errorControl().ode().maxStateStepLength(),
                                 boundary(), errorControl().ode().getScale(),
                                 errorControl().ode().maxNumberOfSteps()));

    }


    //
    // Accessors
    //

    static public final String physicsID() {
        return physics_.ID();
    }

    static public final FluxFunction fluxFunction() {
        return physics_.fluxFunction();
    }

    static public final Boundary boundary() {
        return physics_.boundary();
    }

    static public final Space domain() {
        return physics_.domain();
    }

    static public final Physics getPhysics() {
        return physics_;
    }

    static public final HugoniotCurveCalc hugoniotCurveCalc() {
        return hugoniotCurveCalc_;
    }

    static public final WaveFlow flow() {
        return flow_;
    }

    static public final AccumulationFunction accumulationFunction() {
        return physics_.accumulationFunction();
    }

    static public final RpErrorControl errorControl() {
        return errorControl_;
    }

    static public final SimplexPoincareSection pSection() {
        return ((Rk4BPProfile) odeSolver_.getProfile()).getPoincareSection();
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
