/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import rpn.RPnConfig;
import rpn.parser.ConfigurationProfile;
import rpn.plugininterface.PluginProfile;
import rpn.plugininterface.PluginTableModel;
import rpnumerics.methods.HugoniotContinuationMethod;
import rpnumerics.methods.HugoniotContourMethod;
import wave.util.*;
import wave.ode.*;
import wave.multid.Space;
import wave.util.Boundary;

public class RPNUMERICS {
    //
    // Constants
    //

    static public int INCREASING_LAMBDA = 0;
    //
    // Members
    //
    private static HashMap<String, Configuration> configMap_ = new HashMap<String, Configuration>();
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

    public static void resetParams() {

        ArrayList<ConfigurationProfile> configurationProfiles = RPnConfig.getAllConfigurationProfiles();

        for (int i = 0; i < configurationProfiles.size(); i++) {
            ConfigurationProfile profile = configurationProfiles.get(i);
            String configName = profile.getName();


            if (configName.equals("Contour") || configName.equals("contour")) {
                contourConfiguration_ = new ContourConfiguration(profile.getParams());
                RPNUMERICS.setConfiguration(configName, contourConfiguration_);
            } else {

                HashMap<String, String> profileParams = profile.getParams();

                Configuration configuration = null;

                if (profile.getType().equalsIgnoreCase("physics")) {
                    configuration = processPhysicsProfile(profile);
                }

                if (profile.getType().equalsIgnoreCase("method")) {

                    configuration = new Configuration(profile.getName(), "method", profileParams);
                }

                if (profile.getType().equalsIgnoreCase("curve")) {
                    configuration = new Configuration(profile.getName(), "curve", profileParams);
                }

                RPNUMERICS.setConfiguration(configName, configuration);

            }

        }



    }

    public static void setConfiguration(String methodName, Configuration methodConfiguration) {

        configMap_.put(methodName, methodConfiguration);

    }

    public static String getParamValue(String methodName, String paramValue) {

        Configuration configuration = configMap_.get(methodName);

        return configuration.getParamValue(paramValue);


    }

    public static Configuration getConfiguration(String configurationName) {
        return configMap_.get(configurationName);
    }

    public static void setParamValue(String methodName, String paramName, String paramValue) {

        if (methodName.equals("Contour") || methodName.equals("contour")) {
            contourConfiguration_.setParamValue(paramName, paramValue);
        } else {

            Configuration methodConfig = configMap_.get(methodName);
            methodConfig.setParamValue(paramName, paramValue);
            configMap_.put(methodName, methodConfig);
        }

    }

    public static int getConfigurationSize() {
        return configMap_.size();
    }

    private static ArrayList<Configuration> sortConfigurations() {
        ArrayList<Configuration> methodConfiguration = new ArrayList<Configuration>();
        ArrayList<Configuration> curveConfiguration = new ArrayList<Configuration>();
        ArrayList<Configuration> returnedConfigurationArray = new ArrayList<Configuration>();

        Set<Entry<String, Configuration>> configurationSet = configMap_.entrySet();

        for (Entry<String, Configuration> configurationEntry : configurationSet) {

            if (configurationEntry.getValue().getType().equalsIgnoreCase("method")) {

                methodConfiguration.add(configurationEntry.getValue());

            }
            if (configurationEntry.getValue().getType().equalsIgnoreCase("curve")) {
                curveConfiguration.add(configurationEntry.getValue());
            }
            if (configurationEntry.getValue().getType().equalsIgnoreCase("physics")) {
                returnedConfigurationArray.add(configurationEntry.getValue());
            }


        }
        returnedConfigurationArray.addAll(methodConfiguration);
        returnedConfigurationArray.addAll(curveConfiguration);
        return returnedConfigurationArray;

    }

    public static String toXML() {

        StringBuffer buffer = new StringBuffer();

        ArrayList<Configuration> configurationArray = sortConfigurations();

        for (Configuration configurationEntry : configurationArray) {

            if (configurationEntry.getType().equalsIgnoreCase("physics") && configurationEntry.getName().equalsIgnoreCase(physicsID())) {

                buffer.append(configurationEntry.toXML());

            }
            if (!configurationEntry.getType().equalsIgnoreCase("physics")) {
                buffer.append(configurationEntry.toXML());
            }

        }

        return buffer.toString();

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

    public static RarefactionOrbitCalc createRarefactionCalc(OrbitPoint orbitPoint) {

        return new RarefactionOrbitCalc("methodName", "flowName", orbitPoint, rarefactionProfile_.getFamily(), direction_);//TODO Is method and flowName needed ? 

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

    public static ShockCurveCalc createShockCurveCalc(OrbitPoint orbitPoint) {
//TODO Family hardcoded to zero
        return new ShockCurveCalc("methodname", "flowName", orbitPoint, 0, direction_);

    }

    public static BifurcationCurveCalc createBifurcationCalc() {
        BifurcationCurveCalc bifurcationCurveCalc = null;
        BifurcationParams params = null;

        params = new BifurcationParams(boundary());

        bifurcationCurveCalc = new BifurcationCurveCalc(params);

        return bifurcationCurveCalc;
    }

    public static CompositeCalc createCompositeCalc(OrbitPoint orbitPoint) {
        return new CompositeCalc(orbitPoint, direction_);
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
        direction_ = integer;
    }

    private static Configuration processPhysicsProfile(ConfigurationProfile physicsProfile) {
        Configuration physicsConfiguration = new Configuration(physicsProfile.getName(), "physics");

        for (int i = 0; i < physicsProfile.getIndicesSize(); i++) {
            HashMap<String, String> fluxParam = physicsProfile.getParam(i);
            Set<Entry<String, String>> fluxParamSet = fluxParam.entrySet();
            for (Entry<String, String> fluxEntry : fluxParamSet) {
                physicsConfiguration.setParamValue(fluxEntry.getKey(), fluxEntry.getValue());
                physicsConfiguration.setParamOrder(fluxEntry.getKey(), i);
            }
        }
        ConfigurationProfile boundaryProfile = physicsProfile.getConfigurationProfile(0);
        Configuration boundaryConfiguration = new Configuration(boundaryProfile.getName(), "boundary", boundaryProfile.getParams());

        physicsConfiguration.addConfiguration(boundaryConfiguration);

        return physicsConfiguration;
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
