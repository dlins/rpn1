/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import rpnumerics.viscousprofile.ViscousProfileData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import rpn.RPnConfig;
import rpn.parser.ConfigurationProfile;
import rpn.parser.RPnDataModule;
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
    static private ViscousProfileData shockProfile_ = ViscousProfileData.instance();
    static private BifurcationProfile bifurcationProfile_ = BifurcationProfile.instance();
    static private Integer direction_ = Orbit.FORWARD_DIR;
    //
    // Constructors/Initializers
    //

    static public void init(String physicsID) {

        System.loadLibrary("rpn");
        setRPnHome(System.getProperty("rpnhome"));

        initNative(physicsID);

        //Processing configuration data

        ConfigurationProfile physicsProfile = RPnConfig.getActivePhysicsProfile();

        Configuration physicsConfiguration = new Configuration(physicsProfile);


        //Accumulation function

        Configuration accumulationFunctionConfig = physicsConfiguration.getConfiguration("accumulationfunction");

        if (accumulationFunctionConfig != null) {
//            System.out.println("Printando configuration para acumulacao: " + accumulationFunctionConfig.getParamsSize());
//            System.out.println(accumulationFunctionConfig);

            RealVector newAccParams = new RealVector(accumulationFunctionConfig.getParamsSize());
            for (int i = 0; i < newAccParams.getSize(); i++) {
                //SET ACCUM PARAMS !!!
                newAccParams.setElement(i, new Double(accumulationFunctionConfig.getParam(i)));

            }
            setAccumulationParams(newAccParams);
        } else {

            RealVector paramsVector = getAccumulationParams();

            Configuration accumlationFunctionConfiguration = new Configuration("accumulationfunction", ConfigurationProfile.PHYSICS_CONFIG);

            for (int i = 0; i < paramsVector.getSize(); i++) {
                accumlationFunctionConfiguration.setParamValue("param " + i, String.valueOf(paramsVector.getElement(i)));
                accumlationFunctionConfiguration.setParamOrder("param " + i, i);
            }

            physicsConfiguration.addConfiguration("accumulationfunction", accumlationFunctionConfiguration);
        }

        //Flux function

        Configuration fluxFunctionConfig = physicsConfiguration.getConfiguration("fluxfunction");

        String[] paramsArray = new String[fluxFunctionConfig.getParamsSize()];


        for (int i = 0; i < fluxFunctionConfig.getParamsSize(); i++) {

            paramsArray[i] = fluxFunctionConfig.getParam(i);

        }

        setParams(paramsArray);


        RPnConfig.addProfile(physicsID, physicsProfile);



        ConfigurationProfile boundaryProfile = physicsProfile.getConfigurationProfile(ConfigurationProfile.BOUNDARY);

        if (boundaryProfile != null) { //Catching boundary from input file

//            System.out.println("Pegando do arquivo de entrada");

            Configuration boundaryConfiguration = new Configuration(boundaryProfile);

            if (boundaryConfiguration.getName().equals("rect")) {
                setBoundary(new RectBoundary(boundaryConfiguration.getParam("limits")));
            }


            if (boundaryConfiguration.getName().equals("Three_Phase_Boundary")) {
                setBoundary(new IsoTriang2DBoundary(boundaryConfiguration.getParam("limits")));
            }

//            System.out.println("Limits : " + boundaryConfiguration.getParam("limits"));

        } else {//Catching boundary from numerics layer

            Boundary boundary = boundary();

            RealVector min = boundary.getMinimums();

            ConfigurationProfile defaultBoundaryProfile = null;

            if (boundary instanceof RectBoundary) {
                defaultBoundaryProfile = new ConfigurationProfile("rect", ConfigurationProfile.BOUNDARY);
            }

            if (boundary instanceof IsoTriang2DBoundary) {
                defaultBoundaryProfile = new ConfigurationProfile("Three_Phase_Boundary", ConfigurationProfile.BOUNDARY);

            }

            defaultBoundaryProfile.addParam("limits", boundary.limits());
            defaultBoundaryProfile.addParam("dimension", String.valueOf(min.getSize()));
            physicsProfile.addConfigurationProfile(ConfigurationProfile.BOUNDARY, boundaryProfile);

            Configuration boundaryConfiguration = new Configuration(defaultBoundaryProfile);
            physicsConfiguration.addConfiguration(boundaryConfiguration.getName(), boundaryConfiguration);


        }




        configMap_.put(physicsID, physicsConfiguration);
        errorControl_ = new RpErrorControl(boundary());
    }

    public static Set<String> getConfigurationNames() {
        return configMap_.keySet();


    }

    public static HashMap<String, Configuration> getConfigurations() {
        return configMap_;
    }

    public static void setFamily(int family) {
        setParamValue("orbit", "family", String.valueOf(family));
    }

    public static void setConfiguration(String configurationName, Configuration configuration) {
        configMap_.put(configurationName, configuration);
    }

    public static String getPhysicsParamValue(String configurationName, String paramName) {

        Configuration physicsConfig = configMap_.get(physicsID());
        return physicsConfig.getConfiguration(configurationName).getParam(paramName);
    }

    public static String getParamValue(String methodName, String paramName) {

        Configuration configuration = configMap_.get(methodName);
        return configuration.getParam(paramName);
    }

    public static Configuration getConfiguration(String configurationName) {
        return configMap_.get(configurationName);


    }

    public static void setParamValue(String methodName, String paramName, String paramValue) {

        Configuration methodConfig = configMap_.get(methodName);
        methodConfig.setParamValue(paramName, paramValue);
        configMap_.put(methodName, methodConfig);
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



            if (configurationEntry.getValue().getType().equalsIgnoreCase(ConfigurationProfile.METHOD)) {
                methodConfiguration.add(configurationEntry.getValue());


            }

            if (configurationEntry.getValue().getType().equalsIgnoreCase(ConfigurationProfile.CURVE)) {
                curveConfiguration.add(configurationEntry.getValue());


            }
            if (configurationEntry.getValue().getType().equalsIgnoreCase(ConfigurationProfile.PHYSICS_PROFILE)) {
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

            if (configurationEntry.getType().equalsIgnoreCase(ConfigurationProfile.PHYSICS_PROFILE) && configurationEntry.getName().equalsIgnoreCase(physicsID())) {
                buffer.append(configurationEntry.toXML());
            }
            if (!configurationEntry.getType().equalsIgnoreCase(ConfigurationProfile.PHYSICS_PROFILE)) {
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

    public static HugoniotCurveCalc createHugoniotCalc(RealVector input) {

        int[] resolution = RPnDataModule.processResolution(getParamValue("hugoniotcurve", "resolution"));

        HugoniotParams params = new HugoniotParams(new PhasePoint(input), direction_, resolution);

        return new HugoniotCurveCalcND(params);
    }

    public static RarefactionOrbitCalc createRarefactionCalc(OrbitPoint orbitPoint) {

        return new RarefactionOrbitCalc(orbitPoint, Integer.parseInt(getParamValue("orbit", "family")), direction_);

    }

    public static WaveCurveCalc createWaveCurveCalc(OrbitPoint orbitPoint) {

        return new WaveCurveCalc(orbitPoint, Integer.parseInt(getParamValue("orbit", "family")), direction_);

    }

    public static LevelCurveCalc createPointLevelCurveCalc(RealVector orbitPoint) {

        int[] resolution = RPnDataModule.processResolution(getParamValue("levelcurve", "resolution"));

        int family = new Integer(getParamValue("levelcurve", "family"));

        return new PointLevelCalc(orbitPoint, family, new ContourParams(resolution));

    }

    public static EnvelopeCurveCalc createEnvelopeCurveCalc() {

        int[] resolution = RPnDataModule.processResolution(getParamValue("levelcurve", "resolution"));
        int whereIsConstant = new Integer(getParamValue("envelopecurve", "edge"));
        int numberOfSteps = new Integer(getParamValue("envelopecurve", "numberofsteps"));

        return new EnvelopeCurveCalc(new ContourParams(resolution), whereIsConstant, numberOfSteps);

    }

    public static LevelCurveCalc createLevelCurveCalc(double level) {

        int[] resolution = RPnDataModule.processResolution(getParamValue("levelcurve", "resolution"));

        int family = new Integer(getParamValue("levelcurve", "family"));

        return new LevelCurveCalc(family, level, new ContourParams(resolution));

    }

    public static IntegralCurveCalc createIntegralCurveCalc(OrbitPoint orbitPoint) {

        return new IntegralCurveCalc(orbitPoint, Integer.parseInt(getParamValue("orbit", "family")));

    }

    public static EllipticBoundaryCalc createEllipticBoundaryCalc() {


        int[] resolution = RPnDataModule.processResolution(getParamValue("ellipticboundary", "resolution"));

        return new EllipticBoundaryCalc(new ContourParams(resolution));



    }

    public static EllipticBoundaryExtensionCalc createEllipticExtensionCalc() {


        int[] resolution = RPnDataModule.processResolution(getParamValue("ellipticextension", "resolution"));

        int family = new Integer(getParamValue("ellipticextension", "family"));

        int characteristicWhere = new Integer(getParamValue("ellipticextension", "characteristicwhere"));

        return new EllipticBoundaryExtensionCalc(new ContourParams(resolution), characteristicWhere, family);



    }

//    public static StationaryPointCalc createStationaryPointCalc(PhasePoint initial) {
//
//        ShockFlow shockFlow = createShockFlow();
//        createODESolver((WaveFlow) shockFlow);
//
//        return new StationaryPointCalc(initial, shockFlow);
//
//    }
//    public static ManifoldOrbitCalc createManifoldCalc(StationaryPoint statPoint, PhasePoint initialPoint, int timeDirection) {
//        ShockFlow shockFlow = (ShockFlow) createShockFlow();
//        return new ManifoldOrbitCalc(statPoint, initialPoint, shockFlow, timeDirection);
//
//    }
//    public static ConnectionOrbitCalc createConnectionOrbitCalc(ManifoldOrbit manifoldA, ManifoldOrbit manifoldB) {
//
//        ShockFlow shockFlow = (ShockFlow) createShockFlow();
//
//        return new ConnectionOrbitCalc(manifoldA, manifoldB, shockFlow);
//
//
//    }
//    public static OrbitCalc createOrbitCalc(OrbitPoint orbitPoint) {
//
//        ShockFlow flow = (ShockFlow) createShockFlow();
//        return new OrbitCalc(orbitPoint, direction_, createODESolver(flow));
//
//    }
    public static DoubleContactCurveCalc createDoubleContactCurveCalc() {

        int[] resolution = RPnDataModule.processResolution(getParamValue("doublecontactcurve", "resolution"));

        int curveFamily = new Integer(getParamValue("doublecontactcurve", "curvefamily"));
        int domainFamily = new Integer(getParamValue("doublecontactcurve", "domainfamily"));

        System.out.println("Curve family: " + curveFamily);
        System.out.println("Domain family: " + domainFamily);

        ContourParams params = new ContourParams(resolution);

        return new DoubleContactCurveCalc(params, curveFamily, domainFamily);

    }

    public static SecondaryBifurcationCurveCalc createSecondaryBifurcationCurveCalc() {

        int[] resolution = RPnDataModule.processResolution(getParamValue("inflectioncurve", "resolution"));


        ContourParams params = new ContourParams(resolution);

        return new SecondaryBifurcationCurveCalc(params);

    }

    public static OrbitCalc createOrbitCalc(OrbitPoint oPoint) {

        return new OrbitCalc(oPoint, direction_);

    }

    public static CoincidenceExtensionCurveCalc createCoincidenceExtensionCurveCalc() {


        int xResolution = new Integer(getConfiguration("Contour").getParam("x-resolution"));
        int yResolution = new Integer(getConfiguration("Contour").getParam("y-resolution"));

        int[] resolution = new int[2];

        resolution[0] = xResolution;
        resolution[1] = yResolution;

        ContourParams params = new ContourParams(resolution);

        int characteristicWhere = new Integer(getParamValue("extensioncurve", "characteristicwhere"));


        System.out.println("Coincidence Extension Resolution Java:" + xResolution + " " + yResolution);

        return new CoincidenceExtensionCurveCalc(params, new Integer(getParamValue("extensioncurve", "curvefamily")), new Integer(getParamValue("extensioncurve", "domainfamily")), characteristicWhere);


    }

    public static HysteresisCurveCalc createHysteresisCurveCalc() {

        int[] resolution = RPnDataModule.processResolution(getParamValue("hysteresiscurve", "resolution"));

        ContourParams params = new ContourParams(resolution);

        int family = new Integer(getParamValue("hysteresiscurve", "family"));

        return new HysteresisCurveCalc(params, family);

    }

    public static CompositeCalc createCompositeCalc(OrbitPoint initialPoint) {

        return new CompositeCalc(initialPoint, Integer.parseInt(getParamValue("orbit", "family")), direction_);


    }

    public static BoundaryExtensionCurveCalc createBoundaryExtensionCurveCalc() {

        int[] resolution = RPnDataModule.processResolution(getParamValue("boundaryextensioncurve", "resolution"));

        ContourParams params = new ContourParams(resolution);

        int edge = new Integer(getParamValue("boundaryextensioncurve", "edge"));

        int edgeResolution = new Integer(getParamValue("boundaryextensioncurve", "edgeresolution"));

        int characteristic = new Integer(getParamValue("boundaryextensioncurve", "characteristicwhere"));

        return new BoundaryExtensionCurveCalc(params, edgeResolution, new Integer(getParamValue("boundaryextensioncurve", "family")), edge, characteristic);


    }

    public static SubInflectionExtensionCurveCalc createSubInflectionExtensionCurveCalc() {


        int xResolution = new Integer(getConfiguration("Contour").getParam("x-resolution"));
        int yResolution = new Integer(getConfiguration("Contour").getParam("y-resolution"));


        int[] resolution = new int[2];

        resolution[0] = xResolution;
        resolution[1] = yResolution;

        ContourParams params = new ContourParams(resolution);

        int characteristicWhere = new Integer(getParamValue("extensioncurve", "characteristicwhere"));

        System.out.println("Resolucao em Java:" + xResolution + " " + yResolution);


        return new SubInflectionExtensionCurveCalc(params, new Integer(getParamValue("extensioncurve", "curvefamily")), new Integer(getParamValue("extensioncurve", "domainfamily")), characteristicWhere);



    }

    public static ShockCurveCalc createShockCurveCalc(OrbitPoint orbitPoint) {

        Integer family = new Integer(getParamValue("orbit", "family"));
        Double tolerance = new Double(getParamValue("Newton", "tolerance"));

        System.out.println(direction_);

        return new ShockCurveCalc(orbitPoint, family, direction_);

    }

    public static ContourCurveCalc createBifurcationCalc() {//TODO Remove
        ContourCurveCalc bifurcationCurveCalc = null;

        return bifurcationCurveCalc;


    }

    public static InflectionCurveCalc createInflectionCurveCalc() {

        int[] resolution = RPnDataModule.processResolution(RPNUMERICS.getParamValue("inflectioncurve", "resolution"));

        ContourParams params = new ContourParams(resolution);

        int family = new Integer(RPNUMERICS.getParamValue("inflectioncurve", "family"));

        return new InflectionCurveCalc(params, family);
    }

    public static CoincidenceCurveCalc createCoincidenceCurveCalc() {

        return null;


    }

    public static RarefactionExtensionCalc createRarefactionExtensionCalc(OrbitPoint orbitPoint) {

        int[] resolution = RPnDataModule.processResolution(RPNUMERICS.getParamValue("rarefactionextension", "resolution"));

        int curveFamily = new Integer(getParamValue("rarefactionextension", "curvefamily"));

        int characteristicDomain = new Integer(getParamValue("rarefactionextension", "characteristic"));

        return new RarefactionExtensionCalc(new ContourParams(resolution), orbitPoint, direction_, curveFamily, characteristicDomain);

    }

    public static ShockFlow createShockFlow(ShockFlowParams shockFlowParams) {
        ShockFlow flow = new ShockFlow(shockFlowParams, new FluxFunction(getFluxParams()));


        return flow;


    }

    public static void setDirection(Integer integer) {
        direction_ = integer;



    }

    public static void updateUplus(List<RealVector> eqPoints) {
        RealVector newUPlus = null;
            PhasePoint uPlus = RPNUMERICS.getViscousProfileData().getUplus();
            if(uPlus!=null){//TODO  Inicializar uPlus
                //System.out.println("Uqem eh uPlus dentro do update : " + uPlus);

            double dist = 1E10;    //***Melhorar criterio
            double dist2 = 0.;


            for (RealVector realVector : eqPoints) {
                dist2 = realVector.distance(uPlus);
                if (dist2 < dist) {
                    dist = dist2;
                    newUPlus = realVector;
                }
            }

            RPNUMERICS.getViscousProfileData().setUplus(new PhasePoint(newUPlus));

            }
            
    }



    public static void updateUplusM(List<RealVector> eqPoints) {
        RealVector newUPlus = null;
            PhasePoint uPlus = RPNUMERICS.getViscousProfileData().getUplus();
            if(uPlus!=null){//TODO  Inicializar uPlus
                //System.out.println("Uqem eh uPlus dentro do update : " + uPlus);

            double dist = 1E10;    //***Melhorar criterio
            double dist2 = 0.;


            for (RealVector realVector : eqPoints) {
                dist2 = realVector.distance(uPlus);
                if (dist2 < dist) {
                    dist = dist2;
                    newUPlus = realVector;
                }
            }

            RPNUMERICS.getViscousProfileData().setUplusM(new PhasePoint(newUPlus));

            }

    }




    private static void setFluxDefaultConfiguration() {

        System.out.println("Chamando setFluxDefaultConfiguration");

        FluxParams fluxParams = getFluxParams();

        Configuration physicsConfiguration = configMap_.get(physicsID());



        for (int i = 0; i
                < fluxParams.getParams().getSize(); i++) {

            physicsConfiguration.setParamOrder("param" + "i", i);
            physicsConfiguration.setParamValue("param" + i, fluxParams.getElement(i) + "");




        }


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

    public static ViscousProfileData getViscousProfileData() {
        return shockProfile_;


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

    private native void setFamilyIndex(int familyIndex);

    private native void setTimeDirection(int timeDirection);

    public static void applyFluxParams() {

        Configuration physicsConfiguration = configMap_.get(physicsID());

        Configuration fluxFunctionConfig = physicsConfiguration.getConfiguration("fluxfunction");

        String[] paramsArray = new String[fluxFunctionConfig.getParamsSize()];

        for (int i = 0; i < fluxFunctionConfig.getParamsSize(); i++) {


            paramsArray[i] = fluxFunctionConfig.getParam(i);

        }

        FluxParams previousParams = getFluxParams();

        String[] previousParamArray = new String[previousParams.getParams().getSize()];//fluxFunctionConfig.getParamsSize()];

        for (int i = 0; i < previousParamArray.length; i++) {
            previousParamArray[i]= String.valueOf(previousParams.getElement(i));

        }


        getViscousProfileData().setPreviousSigma(getViscousProfileData().getSigma());
        getViscousProfileData().setPreviousXZero(getViscousProfileData().getXZero());
        getViscousProfileData().setPreviousPhysicsParams(previousParamArray);

        setParams(paramsArray);
    }

    /**
     * Clean up the native layer
     */
    public static native void clean();

    public static native void setParams(String[] params);

    public static native FluxParams getFluxParams();

    public static native void setResolution(RealVector min, RealVector max, String gridName, int[] newResolution);

    public static native RealVector getAccumulationParams();

    public static native String physicsID();

    public static native void setRPnHome(String rpnHome);

    public static native Boundary boundary();

    public static native void setBoundary(Boundary newBoundary);

    public static native int domainDim();

    public static native Space domain();

    private static native void setFluxParams(FluxParams fluxParams);

    private static native void setAccumulationParams(RealVector accumulationParams);
}
