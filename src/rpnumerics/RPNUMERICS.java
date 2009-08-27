/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import java.util.ArrayList;
import java.util.HashMap;
import rpn.RPnConfig;
import rpn.parser.MethodProfile;
import rpn.plugininterface.PluginProfile;
import rpn.plugininterface.PluginTableModel;
import rpnumerics.methods.HugoniotContinuationMethod;
import rpnumerics.methods.HugoniotContourMethod;
import wave.util.*;
import wave.ode.*;
import wave.multid.Space;

public class RPNUMERICS {
    //
    // Constants
    //
    static public int INCREASING_LAMBDA = 0;
    //
    // Members
    //
    private static HashMap<String, MethodConfiguration> methodsConfigMap_ = new HashMap<String, MethodConfiguration>();
    static private RpErrorControl errorControl_ = null;
    static private ODESolver odeSolver_ = null;
    static private ShockProfile shockProfile_ = ShockProfile.instance();
    static private RarefactionProfile rarefactionProfile_ = RarefactionProfile.instance();
    static private BifurcationProfile bifurcationProfile_ = BifurcationProfile.instance();
    static private ShockRarefactionProfile shockRarefactionProfile_ = null;
    static private ContourConfiguration contourConfiguration_ = null;
    static private Integer direction_;

    //
    // Constructors/Initializers
    //
    static public void init(RPNumericsProfile profile) {
        try {
            System.loadLibrary("wave"); //TODO libwave is always loaded ?
            System.loadLibrary("rpnumerics");
            initNative(profile.getPhysicsID());
            if (profile.hasBoundary()) {
                setBoundary(profile.getBoundary());
            }

            if (profile.hasFluxParams()) {
                setFluxParams(profile.getFluxParams());
            }


            errorControl_ = new RpErrorControl(boundary());
        } catch (Exception ex) {
            ex.printStackTrace();

        }
    }

    static public void init(String physicsID) {
        System.loadLibrary("wave");//TODO libwave is always loaded ?
        System.loadLibrary("rpnumerics");
        initNative(physicsID);
        errorControl_ = new RpErrorControl(boundary());
     
    }

    public static void resetMethodsParams() {

        ArrayList<MethodProfile> methodsProfiles = RPnConfig.getAllMethodsProfiles();

        for (int i = 0; i < methodsProfiles.size(); i++) {
            MethodProfile profile = methodsProfiles.get(i);
            String methodName = profile.getName();

            if (methodName.equals("Contour") || methodName.equals("contour")) {
                contourConfiguration_ = new ContourConfiguration(profile.getParams());
            } else {

                HashMap<String, String> profileParams = profile.getParams();
                MethodConfiguration methodConfiguration = new MethodConfiguration(profileParams);
                RPNUMERICS.setMethodParam(methodName, methodConfiguration);

            }

        }

    }

    public static void setMethodParam(String methodName, MethodConfiguration methodConfiguration) {

        methodsConfigMap_.put(methodName, methodConfiguration);

    }

    public static void setMethodParam(String methodName, String paramName, String paramValue) {

        if (methodName.equals("Contour") || methodName.equals("contour")) {
            contourConfiguration_.setParamValue(paramName, paramValue);
        } else {

            MethodConfiguration methodConfig = methodsConfigMap_.get(methodName);
            methodConfig.setParamValue(paramName, paramValue);
            methodsConfigMap_.put(methodName, methodConfig);
        }

    }

    public static int methodsConfigurationSize() {
        return methodsConfigMap_.size();
    }
    
    

    /**
     * 
     * @deprecated
     */
    public static native void initNative(String physicsName);

    public static HugoniotCurveCalc createHugoniotCalc() {

        HugoniotCurveCalc hugoniotCurveCalc = null;
        HugoniotParams hparams = new HugoniotParams(shockProfile_.getXZero(), new FluxFunction(getFluxParams()));

        ShockFlow shockFlow = (ShockFlow) createShockFlow();
        //Not specific

        if (shockProfile_.getHugoniotMethodName().equals("Continuation")) {

            GenericHugoniotFunction hugoniotFunction = new GenericHugoniotFunction(hparams);

            HugoniotContinuationMethod method = new HugoniotContinuationMethod(hugoniotFunction, hparams, createODESolver(shockFlow));

            hugoniotCurveCalc = new HugoniotCurveCalcND((HugoniotContinuationMethod) method);

        }

        if (shockProfile_.getHugoniotMethodName().equals("Contour")) {

            HugoniotContourMethod contourMethod = new HugoniotContourMethod(hparams);

            hugoniotCurveCalc = new HugoniotCurveCalcND(contourMethod);

        }
        hugoniotCurveCalc.uMinusChangeNotify(shockProfile_.getUminus());

        return hugoniotCurveCalc;
    }

    public static ShockCurveCalc createRarefactionCalc(OrbitPoint orbitPoint) {

        return new ShockCurveCalc("methodName", "flowName", orbitPoint, rarefactionProfile_.getFamily(), direction_);//TODO Is method and flowName needed ? 

    }

    public static StationaryPointCalc createStationaryPointCalc(PhasePoint initial) {

        ShockFlow shockFlow = createShockFlow();
        createODESolver((WaveFlow) shockFlow);

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

    public static OrbitCalc createOrbitCalc(OrbitPoint orbitPoint) {

        ShockFlow flow = (ShockFlow) createShockFlow();
        return new OrbitCalc(orbitPoint, direction_, createODESolver(flow));

    }

    public static BifurcationCurveCalc createBifurcationCalc() {
        return new BifurcationCurveCalc();
    }

    public static ShockFlow createShockFlow() {

        RPNUMERICS.getShockProfile().setFlowName((String) PluginTableModel.instance().getValueAt(0, 2));

        RPNUMERICS.setCurrentProfile(RPNUMERICS.getShockProfile());

        FluxFunction flux = new FluxFunction(getFluxParams());

        PluginProfile profile = PluginTableModel.getPluginConfig(ShockProfile.SHOCKFLOW_NAME);

        Double sigmaValue = new Double(profile.getParamValue("sigma"));

        ShockFlowParams shockParams = new ShockFlowParams(shockProfile_.getXZero(), sigmaValue.doubleValue());

        ShockFlow flow = new ShockFlow(shockParams, flux);
        return flow;

    }

    public static ShockFlow createShockFlow(ShockFlowParams shockFlowParams) {
        ShockFlow flow = new ShockFlow(shockFlowParams, new FluxFunction(getFluxParams()));
        return flow;
    }

    public static void setDirection(Integer integer) {
            direction_=integer;
    }

    private static ODESolver createODESolver(WaveFlow flow) {

        odeSolver_ = new Rk4BPMethod(
                new Rk4BPProfile(new FlowVectorField(flow),
                errorControl().eps(),
                errorControl().ode().maxStateStepLength(),
                boundary(), errorControl().ode().getScale(),
                errorControl().ode().maxNumberOfSteps()));
        return odeSolver_;
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

    public static ContourConfiguration getContourConfiguration() {
        return contourConfiguration_;
    }

    private native void setFamilyIndex(int familyIndex);

    private native void setTimeDirection(int timeDirection);

    /**
     * Clean up the native layer
     */
    public static native void clean();

    public static native void setFluxParams(FluxParams fluxParams);

    public static native FluxParams getFluxParams();

    public static native String physicsID();

    public static native Boundary boundary();

    public static native void setBoundary(Boundary newBoundary);

    public static native int domainDim();

    public static native Space domain();
}
