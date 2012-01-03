/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import rpn.RPnConfig;
import rpn.component.OrbitGeom;
import rpn.parser.ConfigurationProfile;
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
//    static private RarefactionProfile rarefactionProfile_ = RarefactionProfile.instance();
    static private BifurcationProfile bifurcationProfile_ = BifurcationProfile.instance();
//    static private ShockRarefactionProfile shockRarefactionProfile_ = null;
//    static private ContourConfiguration contourConfiguration_ = null;
    static private Integer direction_ = OrbitGeom.FORWARD_DIR;
    //
    // Constructors/Initializers
    //

    static public void init(String physicsID) {
        //System.loadLibrary("wave");//TODO libwave is always loaded ?
        //System.loadLibrary("rpnumerics");
        System.loadLibrary("rpn");
        setRPnHome(System.getProperty("rpnhome"));

//        System.out.println("Inicializando a fisica: " + physicsID);
        initNative(physicsID);

        //Processing configuration data

        ConfigurationProfile physicsProfile = RPnConfig.getActivePhysicsProfile();

//        System.out.println("Profile ativo: " + physicsProfile.getName());

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

            Configuration accumlationFunctionConfiguration = new Configuration("accumulationfunction", ConfigurationProfile.PHYSICS_CONFIG_PROFILE);

            for (int i = 0; i < paramsVector.getSize(); i++) {
                accumlationFunctionConfiguration.setParamValue("param " + i, String.valueOf(paramsVector.getElement(i)));
                accumlationFunctionConfiguration.setParamOrder("param " + i, i);


            }

            physicsConfiguration.addConfiguration("accumulationfunction", accumlationFunctionConfiguration);
        }

        //Flux function

        Configuration fluxFunctionConfig = physicsConfiguration.getConfiguration("fluxfunction");
        FluxParams fluxParams = getFluxParams();
        if (fluxFunctionConfig != null) {


            for (int i = 0; i < fluxFunctionConfig.getParamsSize(); i++) {
                //SET FLUX PARAMS !!!
                fluxParams.setParam(i, new Double(fluxFunctionConfig.getParam(i)));
//                System.out.println("Do arquivo Param : " + " order:" + i + " " + fluxFunctionConfig.getParam(i));
            }
            setFluxParams(fluxParams);


        } else {

            RealVector paramsVector = fluxParams.getParams();

            Configuration fluxFunctionConfiguration = new Configuration("fluxfunction", ConfigurationProfile.PHYSICS_CONFIG_PROFILE);

            for (int i = 0; i < paramsVector.getSize(); i++) {
                fluxFunctionConfiguration.setParamValue("param " + i, String.valueOf(paramsVector.getElement(i)));
                fluxFunctionConfiguration.setParamOrder("param " + i, i);

            }

            physicsConfiguration.addConfiguration("fluxfunction", fluxFunctionConfiguration);


//            System.out.println("Usando fluxo default");

            RPnConfig.addProfile(physicsID, physicsProfile);

        }

        ConfigurationProfile boundaryProfile = physicsProfile.getConfigurationProfile(ConfigurationProfile.BOUNDARY_PROFILE);

//        System.out.println("Printando boundaryProfile: " + boundaryProfile);

//        if (boundaryProfile.getParam("limits") != null) { //Catching boundary from input file

        if (boundaryProfile != null) { //Catching boundary from input file

//            System.out.println("Pegando do arquivo de entrada");

            Configuration boundaryConfiguration = new Configuration(boundaryProfile);

            if (boundaryConfiguration.getName().equalsIgnoreCase("rect")) {

                RealVector min = new RealVector(new Integer(boundaryConfiguration.getParam("dimension")));
                RealVector max = new RealVector(new Integer(boundaryConfiguration.getParam("dimension")));

                System.out.println("Printando limites: " + boundaryConfiguration.getParam("limits"));


                String[] limitsNumbers = boundaryConfiguration.getParam("limits").split(" ");


                int vectorIndex = 0;
                for (int i = 0; i < limitsNumbers.length / 2; i++) {

                    min.setElement(vectorIndex, new Double(limitsNumbers[i]));
                    vectorIndex++;
                }

                vectorIndex = 0;

                for (int i = limitsNumbers.length / 2; i < limitsNumbers.length; i++) {

                    max.setElement(vectorIndex, new Double(limitsNumbers[i]));

                    vectorIndex++;

                }

                System.out.println("Valor de min: " + min);
                System.out.println("Valor de max: " + max);
                RectBoundary boundary = new RectBoundary(min, max);
                setBoundary(boundary);
            }

        } else {//Catching boundary from numerics layer

            Boundary boundary = boundary();

            if (boundary instanceof RectBoundary) {

                RealVector min = boundary.getMinimums();
                RealVector max = boundary.getMaximums();

                ConfigurationProfile defaultBoundaryProfile = new ConfigurationProfile("rect", ConfigurationProfile.BOUNDARY_PROFILE);

                String limits = "";

                for (int i = 0; i < min.getSize(); i++) {
                    limits += min.getElement(i);
                    limits += " ";
                }

                for (int i = 0; i < max.getSize(); i++) {
//                    limits = limits.replaceFirst(" ", max.getElement(i) + "");

                    limits += max.getElement(i);
                    limits += " ";

                }

                defaultBoundaryProfile.addParam("limits", limits);
                defaultBoundaryProfile.addParam("dimension", String.valueOf(min.getSize()));

                defaultBoundaryProfile.addParam("x-min", min.getElement(0) + "");
                defaultBoundaryProfile.addParam("y-min", min.getElement(1) + "");
                defaultBoundaryProfile.addParam("x-max", max.getElement(0) + "");
                defaultBoundaryProfile.addParam("y-max", max.getElement(1) + "");

                physicsProfile.addConfigurationProfile(ConfigurationProfile.BOUNDARY_PROFILE, boundaryProfile);

                Configuration boundaryConfiguration = new Configuration(defaultBoundaryProfile);

                physicsConfiguration.addConfiguration(boundaryConfiguration.getName(), boundaryConfiguration);


            }
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
        setParamValue("shock", "family", String.valueOf(family));

    }

    public static void setConfiguration(String methodName, Configuration methodConfiguration) {
//        if (methodName.equalsIgnoreCase("Contour")) {
//            contourConfiguration_ = new ContourConfiguration(methodConfiguration.getParams());
//        }

        configMap_.put(methodName, methodConfiguration);

    }

    public static String getPhysicsParamValue(String configurationName, String paramName) {

//        System.out.println(configurationName + " " + paramName);

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
//                System.out.println(configurationEntry.toString());

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

        RealVector teste = new RealVector(3);

        teste.setElement(0, 0.0);
        teste.setElement(1, 0.0);
        teste.setElement(2, 0.0);


        return new HugoniotCurveCalcND(teste);


//        HugoniotParams hparams = new HugoniotParams(new PhasePoint(teste), new FluxFunction(getFluxParams()));



//        ShockFlow shockFlow = (ShockFlow) createShockFlow();
        //Not specific

//        if (shockProfile_.getHugoniotMethodName().equals("Continuation")) {
//
//            GenericHugoniotFunction hugoniotFunction = new GenericHugoniotFunction(hparams);
//
//            HugoniotContinuationMethod method = new HugoniotContinuationMethod(hugoniotFunction, hparams, createODESolver(shockFlow));
//
//            hugoniotCurveCalc = new HugoniotCurveCalcND((HugoniotContinuationMethod) method);
//
//        }
//
//        if (shockProfile_.getHugoniotMethodName().equals("Contour")) {
//
//            HugoniotContourMethod contourMethod = new HugoniotContourMethod(hparams);
//
//            hugoniotCurveCalc = new HugoniotCurveCalcND(contourMethod);
//
//        }
//        hugoniotCurveCalc.uMinusChangeNotify(shockProfile_.getUminus());

//        return hugoniotCurveCalc;
    }

    public static RarefactionOrbitCalc createRarefactionCalc(OrbitPoint orbitPoint) {

        /*
         * TODO O Valor de family deve ser o mesmo para choque e rarefacao ????
         */

        return new RarefactionOrbitCalc("methodName", "flowName", orbitPoint, Integer.parseInt(getParamValue("shock", "family")), direction_);//TODO Is method and flowName needed ?

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


        Double xResolution = new Double (getConfiguration("Contour").getParam("x-resolution"));
        Double yResolution = new Double(getConfiguration("Contour").getParam("y-resolution"));

        System.out.println("Resolucao em Java:" + xResolution + " " + yResolution);


        return new DoubleContactCurveCalc(xResolution.intValue(), yResolution.intValue(), new Integer(getParamValue("bifurcationcurve", "curvefamily")), new Integer(getParamValue("bifurcationcurve", "domainfamily")));

    }

    public static CoincidenceExtensionCurveCalc createCoincidenceExtensionCurveCalc() {


        int xResolution = new Integer(getConfiguration("Contour").getParam("x-resolution"));
        int yResolution = new Integer(getConfiguration("Contour").getParam("y-resolution"));


       

        int characteristicWhere = new Integer(getParamValue("extensioncurve", "characteristicwhere"));


        System.out.println("Coincidence Extension Resolution Java:" + xResolution + " " + yResolution);


        return new CoincidenceExtensionCurveCalc(xResolution, yResolution, new Integer(getParamValue("extensioncurve", "curvefamily")), new Integer(getParamValue("extensioncurve", "domainfamily")), characteristicWhere);
    }

    public static BoundaryExtensionCurveCalc createExtensionCurveCalc() {


        int xResolution = new Integer(getConfiguration("Contour").getParam("x-resolution"));
        int yResolution = new Integer(getConfiguration("Contour").getParam("y-resolution"));


        int characteristicWhere = new Integer(getParamValue("extensioncurve", "characteristicwhere"));
        int edge = new Integer(getParamValue("extensioncurve", "edge"));
        int edgeResolution = new Integer(getParamValue("extensioncurve", "edgeresolution"));

        System.out.println("Resolucao em Java:" + xResolution + " " + yResolution);


        return new BoundaryExtensionCurveCalc(xResolution, yResolution, edgeResolution, new Integer(getParamValue("extensioncurve", "curvefamily")), new Integer(getParamValue("extensioncurve", "domainfamily")), edge, characteristicWhere);

    }

    public static SubInflectionExtensionCurveCalc createSubInflectionExtensionCurveCalc() {


        int xResolution = new Integer(getConfiguration("Contour").getParam("x-resolution"));
        int yResolution = new Integer(getConfiguration("Contour").getParam("y-resolution"));

        int characteristicWhere = new Integer(getParamValue("extensioncurve", "characteristicwhere"));


        System.out.println("Resolucao em Java:" + xResolution + " " + yResolution);


        return new SubInflectionExtensionCurveCalc(xResolution, yResolution, new Integer(getParamValue("extensioncurve", "curvefamily")), new Integer(getParamValue("extensioncurve", "domainfamily")), characteristicWhere);

    }

    public static ShockCurveCalc createShockCurveCalc(OrbitPoint orbitPoint) {

        Integer family = new Integer(getParamValue("shock", "family"));
        Double tolerance = new Double(getParamValue("Newton", "tolerance"));

        System.out.println(direction_);

        return new ShockCurveCalc("methodname", tolerance, orbitPoint, family, direction_);
    }

    public static BifurcationCurveCalc createBifurcationCalc() {
        BifurcationCurveCalc bifurcationCurveCalc = null;
        BifurcationParams params = null;

        params = new BifurcationParams(boundary());

        bifurcationCurveCalc = new BifurcationCurveCalc(params);

        return bifurcationCurveCalc;
    }

    public static CompositeCalc createCompositeCalc(OrbitPoint orbitPoint) {
        System.out.println("aqui no create " + getConfiguration("Contour").getParam("x-resolution") + " " + getConfiguration("Contour").getParam("y-resolution"));



        int xResolution = new Integer(getConfiguration("Contour").getParam("x-resolution"));
        int yResolution = new Integer(getConfiguration("Contour").getParam("y-resolution"));


        int curveFamily = new Integer(getParamValue("wavecurve", "curvefamily"));
        int domainFamily = new Integer(getParamValue("wavecurve", "domainfamily"));


        int characteristicDomain = new Integer(getParamValue("wavecurve", "characteristicwhere"));


        return new CompositeCalc(xResolution, yResolution, orbitPoint, direction_, curveFamily, domainFamily, characteristicDomain);
    }

//    public static ShockFlow createShockFlow() {
//
//        RPNUMERICS.getShockProfile().setFlowName((String) PluginTableModel.instance().getValueAt(0, 2));
//
//        RPNUMERICS.setCurrentProfile(RPNUMERICS.getShockProfile());
//
//        FluxFunction flux = new FluxFunction(getFluxParams());
//
//        PluginProfile profile = PluginTableModel.getPluginConfig(ShockProfile.SHOCKFLOW_NAME);
//
//        Double sigmaValue = new Double(profile.getParamValue("sigma"));
//
//        ShockFlowParams shockParams = new ShockFlowParams(shockProfile_.getXZero(), sigmaValue.doubleValue());
//
//        ShockFlow flow = new ShockFlow(shockParams, flux);
//        return flow;
//
//    }
    public static ShockFlow createShockFlow(ShockFlowParams shockFlowParams) {
        ShockFlow flow = new ShockFlow(shockFlowParams, new FluxFunction(getFluxParams()));
        return flow;
    }

    public static void setDirection(Integer integer) {
        direction_ = integer;

    }

    private static void setFluxDefaultConfiguration() {

        System.out.println("Chamando setFluxDefaultConfiguration");

        FluxParams fluxParams = getFluxParams();

        Configuration physicsConfiguration = configMap_.get(physicsID());

        for (int i = 0; i < fluxParams.getParams().getSize(); i++) {

            physicsConfiguration.setParamOrder("param" + i, i);
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

//    public static void setCurrentProfile(ShockRarefactionProfile aShockRarefactionProfile_) {
//        shockRarefactionProfile_ = aShockRarefactionProfile_;
//    }
//    public static ShockRarefactionProfile getCurrentProfile() {
//
//
//        return shockRarefactionProfile_;
//    }
    public static ShockProfile getShockProfile() {
        return shockProfile_;
    }

//    public static RarefactionProfile getRarefactionProfile() {
//        return rarefactionProfile_;
//    }
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

//    public static ContourConfiguration getContourConfiguration() {
//        return (ContourConfiguration) configMap_.get("Contour");
//    }
    private native void setFamilyIndex(int familyIndex);

    private native void setTimeDirection(int timeDirection);

    public static void applyFluxParams() {

        Configuration physicsConfiguration = configMap_.get(physicsID());

        Configuration fluxConfiguration = physicsConfiguration.getConfiguration("fluxfunction");
        Configuration accumulationConfiguration = physicsConfiguration.getConfiguration("accumulationfunction");

        
        RealVector fluxParamsVector = new RealVector(fluxConfiguration.getParamsSize());
        for (int i = 0; i < fluxParamsVector.getSize(); i++) {
            fluxParamsVector.setElement(i, new Double(fluxConfiguration.getParam(i)));

        }

        FluxParams newFluxParams = new FluxParams(fluxParamsVector);
        setFluxParams(newFluxParams);

        RealVector accumulationParamsVector = new RealVector(accumulationConfiguration.getParamsSize());

        for (int i = 0; i < accumulationParamsVector.getSize(); i++) {
            accumulationParamsVector.setElement(i, new Double(accumulationConfiguration.getParam(i)));

        }
        setAccumulationParams(accumulationParamsVector);

    }

    /**
     * Clean up the native layer
     */
    public static native void clean();

    private static native void setFluxParams(FluxParams fluxParams);

    public static native FluxParams getFluxParams();

    private static native void setAccumulationParams(RealVector accumulationParams);

    public static native RealVector getAccumulationParams();

    public static native String physicsID();

    public static native void setRPnHome(String rpnHome);

    public static native Boundary boundary();

    public static native void setBoundary(Boundary newBoundary);

    public static native int domainDim();

    public static native Space domain();
}
