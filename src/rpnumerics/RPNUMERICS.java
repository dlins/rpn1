/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import rpnumerics.methods.HugoniotContinuationMethod;
import rpnumerics.physics.CapilParams;
import rpnumerics.physics.CombFluxParams;
import rpnumerics.physics.CombHugoniotCurveCalc;
import rpnumerics.physics.PGas;
import rpnumerics.physics.PGasFluxParams;
import rpnumerics.physics.PermParams;
import wave.util.*;
import wave.ode.*;
import rpnumerics.physics.Physics;
import rpnumerics.physics.Quad2;
import rpnumerics.physics.Quad2FluxParams;
import rpnumerics.physics.Quad2HugoniotCurveCalc;
import rpnumerics.physics.Quad4;
import rpnumerics.physics.Quad4FluxParams;
import rpnumerics.physics.Steam;
import rpnumerics.physics.SteamFluxParams;
import rpnumerics.physics.TriPhase;
import rpnumerics.physics.TriPhaseFluxParams;
import rpnumerics.physics.ViscosityParams;
import wave.multid.Space;

public class RPNUMERICS {


    //
    // Constants
    //
    static public int INCREASING_LAMBDA = 0;
    //
    // Members
    //
    static private Physics physics_ = null;
    static private String libName_ = null;
    static private RpErrorControl errorControl_ = null;
    static private ODESolver odeSolver_ = null;
    static private ShockProfile shockProfile_ = ShockProfile.instance();
    static private RarefactionProfile rarefactionProfile_ = RarefactionProfile.instance();
    static private BifurcationProfile bifurcationProfile_ = BifurcationProfile.instance();
    static private ShockRarefactionProfile shockRarefactionProfile_ = null;

    //
    // Constructors/Initializers
    //
    static public void init(RPNumericsProfile profile) throws RpException {


        System.loadLibrary("wave");//TODO libwave is always loaded ?
        System.loadLibrary(profile.getLibName());
        libName_ = profile.getLibName();

        initNative(profile.getPhysicsID());

        physics_ = physicsCreation(
                profile);

        if (profile.hasBoundary()) {
            try {

                physics_.setBoundary(profile.getBoundary());
            } catch (Exception ex) {
                System.out.println(ex);
            //Logger.getLogger(RPNUMERICS.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

   

    /**
     * 
     * @deprecated
     */
    public static native void initNative(String physicsName);

    public static HugoniotCurveCalc createHugoniotCalc() {

        HugoniotParams hparams = new HugoniotParams(shockProfile_.getXZero(),fluxFunction());

        ShockFlow shockFlow = (ShockFlow) createShockFlow();


        if (shockProfile_.isHugoniotSpecific()) {

            if (physicsID().equals("Quad2")) {

                return new Quad2HugoniotCurveCalc(RPNUMERICS.fluxFunction().fluxParams(), hparams.getXZero());
            }


            if (physicsID().equals("Comb")) {

                return new CombHugoniotCurveCalc((CombFluxParams) fluxFunction().fluxParams(), hparams.getXZero(), 1d);

            }

        }
        //Not specific

        if (shockProfile_.getHugoniotMethodName().equals("Continuation")) {

            GenericHugoniotFunction hugoniotFunction = new GenericHugoniotFunction(hparams);

            HugoniotContinuationMethod method = new HugoniotContinuationMethod(hugoniotFunction, hparams, createODESolver(shockFlow));

            HugoniotCurveCalc hugoniotCurveCalc = new HugoniotCurveCalcND((HugoniotContinuationMethod) method);

            hugoniotCurveCalc.uMinusChangeNotify(shockProfile_.getUminus());

            return hugoniotCurveCalc;

        }

        return null;
    }

    public static RarefactionOrbitCalc createRarefactionCalc(OrbitPoint orbitPoint, int timeDirection) {

        RarefactionFlow flow = createRarefactionFlow();
        RealVector scales = new RealVector(rarefactionProfile_.getXZero().getSize());
        for (int i = 0; i < scales.getSize(); i++) {
            scales.setElement(i, 1);
        }

        errorControl_ = new RpErrorControl(scales);
        return new RarefactionOrbitCalc(orbitPoint, timeDirection, createODESolver(flow), rarefactionProfile_.geRarefactiontMethodName());
    }

    public static StationaryPointCalc createStationaryPointCalc(PhasePoint initial) {

        ShockFlow shockFlow = (ShockFlow) createShockFlow();
        createODESolver((WaveFlow)shockFlow);

        return new StationaryPointCalc(initial, shockFlow);

    }

    public static ManifoldOrbitCalc createManifoldCalc(StationaryPoint statPoint, PhasePoint initialPoint, int timeDirection) {
        ShockFlow shockFlow = (ShockFlow) createShockFlow();
        return new ManifoldOrbitCalc(statPoint, initialPoint, shockFlow, timeDirection);

    }

    public static ConnectionOrbitCalc createConnectionOrbitCalc(ManifoldOrbit manifoldA, ManifoldOrbit manifoldB) {

        ShockFlow shockFlow = (ShockFlow) createShockFlow();

        return new ConnectionOrbitCalc(manifoldA, manifoldB, shockFlow);


    }

    public static OrbitCalc createOrbitCalc(OrbitPoint orbitPoint, int timeDirection) {

        ShockFlow flow = (ShockFlow) createShockFlow();

        return new OrbitCalc(orbitPoint, timeDirection, createODESolver(flow));


    }

    public static BifurcationCurveCalc createBifurcationCalc() {
        return new BifurcationCurveCalc();
    }

    public static WaveFlow createShockFlow() {

        if (shockProfile_.getFlowName().equals("Conservation Shock Flow")) {

            ShockFlowParams shockParams = new ShockFlowParams(shockProfile_.getXZero(), shockProfile_.getSigma());
            ConservationShockFlow flow = new ConservationShockFlow(shockParams, fluxFunction());
            return flow;
        }

        return null;
    }

    public static WaveFlow createShockFlow(ShockFlowParams shockFlowParams) {

        if (shockProfile_.getFlowName().equals("Conservation Shock Flow")) {


            ConservationShockFlow flow = new ConservationShockFlow(shockFlowParams, fluxFunction());

            
            return flow;
        }

        return null;
    }

    private static RarefactionFlow createRarefactionFlow() {


        if (rarefactionProfile_.getFlowName().equals("Native Rarefaction Flow")) {
            System.out.println("Flow Nativo");
            return null;

        }


        if (rarefactionProfile_.getFlowName().equals("Blow Up")) {
            BlowUpLineFieldVector blowUpUserInput = new BlowUpLineFieldVector(rarefactionProfile_.getXZero(), rarefactionProfile_.getFamily(), fluxFunction());

            return new BlowUpFlow(blowUpUserInput, fluxFunction());
        }

        if (rarefactionProfile_.getFlowName().equals("Rarefaction Flow")) {

            return new RarefactionFlow(rarefactionProfile_.getXZero(), rarefactionProfile_.getFamily(), fluxFunction());
        }

        return null;

    }

    private static ODESolver createODESolver(WaveFlow flow) {


        errorControl_ = new RpErrorControl(boundary());
        odeSolver_ = new Rk4BPMethod(
                new Rk4BPProfile(new FlowVectorField(flow),
                errorControl().eps(),
                errorControl().ode().maxStateStepLength(),
                boundary(), errorControl().ode().getScale(),
                errorControl().ode().maxNumberOfSteps()));
        return odeSolver_;
    }
    
    
    /**
     * @deprecated Physics will be created in the native layer 
     *
     */
     private static Physics physicsCreation(RPNumericsProfile profile) {

        // Physics initialization

        if (profile.getPhysicsID().equals("QuadraticR2")) {

            return new Quad2(new Quad2FluxParams());

        }

        if (profile.getPhysicsID().equals("QuadraticR4")) {
            return new Quad4(new Quad4FluxParams());
        }

        if (profile.getPhysicsID().equals("Triphase")) {

            return new TriPhase(new TriPhaseFluxParams(), new PermParams(),
                    new CapilParams(0.4d, 3d, 44d, 8d),
                    new ViscosityParams(0.5d));

        }

        if (profile.getPhysicsID().equals("P Gas")) {

            return new PGas(new PGasFluxParams());

        }

        if (profile.getPhysicsID().equals("Steam")) {
            return new Steam(new SteamFluxParams());

        }

        return null;
    }
    
    

    //
    // Accessors
    //
     
     
     
    public static void setCurrentProfile(ShockRarefactionProfile aShockRarefactionProfile_) {
        shockRarefactionProfile_ = aShockRarefactionProfile_;
    }

    public static ShockRarefactionProfile getCurrentProfile() {


        return shockRarefactionProfile_;
    }

    public static ShockProfile getShockProfile() {
        return shockProfile_;
    }

    public static RarefactionProfile getRarefactionProfile() {
        return rarefactionProfile_;
    }

    public static BifurcationProfile getBifurcationProfile() {
        return bifurcationProfile_;
    }

    //TODO 
    /**
     * 
     * @deprecated CHANGE TO RPFUNCTION REFERENCES !!
     */
    static public final FluxFunction fluxFunction() {
        return physics_.fluxFunction();
    }

    //TODO KEEP TO JAVA CALCS USE  !!
    /**
     * 
     * 
     * @deprecated   ONLY TO JAVA CALCS USE  
     */
    static public final RpErrorControl errorControl() {
        return errorControl_;
    }

    /**
     * 
     * 
     * @deprecated   ONLY TO JAVA CALCS USE  !!
     */
    static public final SimplexPoincareSection pSection() {
        return ((Rk4BPProfile) odeSolver_.getProfile()).getPoincareSection();
    }

    /**
     * 
     * 
     * @deprecated  ONLY TO JAVA CALCS USE  !!
     */
    static public final ODESolver odeSolver() {
        return odeSolver_;
    }
    
    private native void setFamilyIndex(int familyIndex);
    private native void setTimeDirection(int timeDirection);
    
     /**
     * Clean up the native layer
     */

    public static native void clean();
    
    public static native String physicsID();

    public static native Boundary boundary();

    public static native int domainDim();

    public static native Space domain();

    public static String getLibName() {
        return libName_;
    }
}
