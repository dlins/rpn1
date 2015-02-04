/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import rpn.configuration.Configuration;
import rpnumerics.viscousprofile.ViscousProfileData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;
import rpn.configuration.ConfigurationProfile;
import rpn.configuration.Parameter;
import rpn.configuration.PhysicsConfiguration;
import rpn.configuration.PhysicsConfigurationParams;
import wave.multid.CoordsArray;
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
//        try {
        System.loadLibrary("rpn");
        setRPnHome(System.getProperty("rpnhome"));

        initNative(physicsID);

        PhysicsConfiguration physicsConfiguration = (PhysicsConfiguration) getConfiguration(physicsID);

        PhysicsConfigurationParams fluxFunctionConfiguration = (PhysicsConfigurationParams) physicsConfiguration.getConfiguration("fluxfunction");

        if (fluxFunctionConfiguration != null) {//Reading from file

            for (int i = 0; i < fluxFunctionConfiguration.getParameterList().size(); i++) {

                Parameter param = fluxFunctionConfiguration.getParamList().get(i);

                setPhysicsParams(param.getName(), param.getValue());

            }
        }

        HashMap<String, Configuration> configurationMap = physicsConfiguration.getConfigurationMap();

        Set<Map.Entry<String, Configuration>> entrySet = configurationMap.entrySet();
        Iterator<Map.Entry<String, Configuration>> iterator = entrySet.iterator();

        while (iterator.hasNext()) {

            Map.Entry<String, Configuration> entry = iterator.next();

            Configuration config = entry.getValue();

            if (!config.getName().equals("fluxfunction")) {
                for (Parameter param : config.getParameterList()) {

                    setAuxFuntionParam(config.getName(), param.getName(), param.getValue());

                }

            }

        }

    }

    public static Set<String> getConfigurationNames() {
        return configMap_.keySet();

    }

    public static HashMap<String, Configuration> getConfigurations() {
        return configMap_;
    }

    public static void setFamily(int family) {
        setParamValue("fundamentalcurve", "family", String.valueOf(family));
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

            if (configurationEntry.getValue().getType().equalsIgnoreCase(ConfigurationProfile.CURVECONFIGURATION)) {
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

        StringBuilder buffer = new StringBuilder();

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

    public static EnvelopeCurveCalc createEnvelopeCurveCalc(Configuration configuration) {

        int[] resolution = processResolution(configuration.getParam("resolution"));
        int whereIsConstant = new Integer(configuration.getParam("edge"));
        int numberOfSteps = new Integer(configuration.getParam("numberofsteps"));

        return new EnvelopeCurveCalc(new ContourParams(resolution), whereIsConstant, numberOfSteps);

    }

    public static EllipticBoundaryCalc createEllipticBoundaryCalc(Configuration configuration) {

        int[] resolution = processResolution(configuration.getParam("resolution"));

        return new EllipticBoundaryCalc(new ContourParams(resolution));

    }

    public static RarefactionCurveCalc createRarefactionCalc(OrbitPoint orbitPoint) {
        Integer direction = new Integer(getParamValue("fundamentalcurve", "direction"));
        return new RarefactionCurveCalc(orbitPoint, Integer.parseInt(getParamValue("fundamentalcurve", "family")), direction);

    }

    public static RarefactionCurveCalc createRarefactionCalc(OrbitPoint orbitPoint, int edge) {
        Integer direction = new Integer(getParamValue("fundamentalcurve", "direction"));
        return new RarefactionCurveCalc(orbitPoint, Integer.parseInt(getParamValue("fundamentalcurve", "family")), direction, edge);

    }

    public static DoubleContactCurveCalc createDoubleContactCurveCalc(Configuration configuration) {

        int[] resolution = processResolution(configuration.getParam("resolution"));

        int curveFamily = new Integer(configuration.getParam("curvefamily"));
        int domainFamily = new Integer(configuration.getParam("domainfamily"));

        ContourParams params = new ContourParams(resolution);

        return new DoubleContactCurveCalc(params, curveFamily, domainFamily);

    }

    public static EllipticBoundaryExtensionCalc createEllipticExtensionCalc(Configuration configuration) {

        int[] resolution = processResolution(configuration.getParam("resolution"));

        int family = new Integer(configuration.getParam("family"));

        int characteristicWhere = new Integer(configuration.getParam("characteristicwhere"));

        return new EllipticBoundaryExtensionCalc(new ContourParams(resolution), characteristicWhere, family);

    }

    public static SecondaryBifurcationCurveCalc createSecondaryBifurcationCurveCalc(Configuration configuration) {

        int[] resolution = processResolution(configuration.getParam("resolution"));

        String methodName = HugoniotCurveCalcND.HugoniotMethods.valueOf(configuration.getParam("method")).name();

        ContourParams params = new ContourParams(resolution);

        int edge = Integer.parseInt(configuration.getParam("edge"));

        return new SecondaryBifurcationCurveCalc(params, methodName, edge);

    }

    public static CharacteristicPolynomialLevelCalc createPointLevelCurveCalc(RealVector orbitPoint) {

        int[] resolution = processResolution(RPNUMERICS.getParamValue("hugoniotcurve", "resolution"));

        int family = new Integer(getParamValue("levelcurve", "family"));

        return new EigenValuePointLevelCalc(orbitPoint, family, new ContourParams(resolution));

    }

    public static CoincidenceExtensionCurveCalc createCoincidenceExtensionCurveCalc(Configuration configuration) {

        int[] resolution = processResolution(configuration.getParam("resolution"));

        int leftFamily = new Integer(configuration.getParam("curvefamily"));
        int rightFamily = new Integer(configuration.getParam("domainfamily"));
        int characteristicDomain = new Integer(configuration.getParam("characteristicwhere"));

        ContourParams params = new ContourParams(resolution);

        return new CoincidenceExtensionCurveCalc(params, leftFamily, rightFamily, characteristicDomain);
    }

    public static ExtensionCurveCalc createExtensionCurveCalc(List<RealSegment> segments, CoordsArray[] areaSelected) {

        int[] resolution = processResolution(RPNUMERICS.getParamValue("grid", "resolution"));
        int family = new Integer(getParamValue("extensioncurve", "family"));
        int characteristic = new Integer(getParamValue("extensioncurve", "characteristic"));
        boolean singular = new Boolean(getParamValue("extensioncurve", "singular"));

        int insideArea;

        ArrayList<RealVector> realVectorList = new ArrayList<RealVector>();

        if (areaSelected == null) {
            insideArea = 1;
        } else {
            insideArea = new Integer(getParamValue("extensioncurve", "domain"));
            for (int i = 0; i < areaSelected.length; i++) {
                realVectorList.add(new RealVector(areaSelected[i].getCoords()));

            }

        }

        return new ExtensionCurveCalc(new ContourParams(resolution), segments, realVectorList, family, characteristic, singular, insideArea);

    }

    public static BoundaryExtensionCurveCalc createBoundaryExtensionCurveCalc(Configuration configuration) {

        int[] resolution = processResolution(configuration.getParam("resolution"));

        int edge = new Integer(configuration.getParam("edge"));

        int edgeResolution = new Integer(configuration.getParam("edgeresolution"));

        int characteristic = new Integer(configuration.getParam("characteristicwhere"));

        int family = new Integer(configuration.getParam("family"));

        ContourParams params = new ContourParams(resolution);

        return new BoundaryExtensionCurveCalc(params, edgeResolution, family, edge, characteristic);
    }

    public static EnvelopeCurveCalc createEnvelopeCurveCalc() {

        int[] resolution = processResolution(RPNUMERICS.getParamValue("hugoniotcurve", "resolution"));
        int whereIsConstant = new Integer(getParamValue("envelopecurve", "edge"));
        int numberOfSteps = new Integer(getParamValue("envelopecurve", "numberofsteps"));

        return new EnvelopeCurveCalc(new ContourParams(resolution), whereIsConstant, numberOfSteps);

    }

    public static EllipticBoundaryExtensionCalc createEllipticBoundaryExtensionCalc(Configuration configuration) {

        int[] resolution = processResolution(configuration.getParam("resolution"));

        int curveFamily = new Integer(configuration.getParam("curvefamily"));
        int domainFamily = new Integer(configuration.getParam("domainfamily"));

        ContourParams params = new ContourParams(resolution);

        return new EllipticBoundaryExtensionCalc(params, curveFamily, domainFamily);

    }

    public static CharacteristicPolynomialLevelCalc createLevelCurveCalc(double level) {

        int[] resolution = processResolution(RPNUMERICS.getParamValue("grid", "resolution"));

        int family = new Integer(getParamValue("levelcurve", "family"));

        return new EigenValueLevelCalc(family, level, new ContourParams(resolution));

    }

    public static CharacteristicPolynomialLevelCalc createDiscriminantLevelCurveCalc(Double level) {

        int[] resolution = processResolution(RPNUMERICS.getParamValue("grid", "resolution"));

        return new DiscriminantLevelCurveCalc(level, new ContourParams(resolution));

    }

    public static CharacteristicPolynomialLevelCalc createDiscriminantPointLevelCurveCalc(RealVector realVector) {

        int[] resolution = processResolution(RPNUMERICS.getParamValue("grid", "resolution"));

        return new DiscriminantPointLevelCalc(realVector, new ContourParams(resolution));

    }

    public static CharacteristicPolynomialLevelCalc createDerivativeDiscriminantLevelCurveCalc() {
        int[] resolution = processResolution(RPNUMERICS.getParamValue("grid", "resolution"));
        int u = Integer.parseInt(getParamValue("derivativediscriminant", "u"));
        return new DerivativeDiscriminantLevelCurveCalc(u, new ContourParams(resolution));

    }

    public static InflectionCurveCalc createInflectionCurveCalc(Configuration configuration) {

        int[] resolution = processResolution(configuration.getParam("resolution"));
        int family = Integer.parseInt(configuration.getParam("family"));
        ContourParams params = new ContourParams(resolution);

        return new InflectionCurveCalc(params, family);
    }

    public static HysteresisCurveCalc createHysteresisCurveCalc(Configuration configuration) {

        int[] resolution = processResolution(configuration.getParam("resolution"));

        ContourParams params = new ContourParams(resolution);

        int family = new Integer(configuration.getParam("family"));

        return new HysteresisCurveCalc(params, family);

    }

    public static RarefactionCurveCalc createRarefactionCalc(Configuration configuration, OrbitPoint orbitPoint) {

        Integer direction = new Integer(configuration.getParam("direction"));
        return new RarefactionCurveCalc(orbitPoint, Integer.parseInt(configuration.getParam("family")), direction);

    }

    public static IntegralCurveCalc createIntegralCurveCalc(OrbitPoint orbitPoint) {

        return new IntegralCurveCalc(orbitPoint, Integer.parseInt(getParamValue("fundamentalcurve", "family")));

    }

    public static EllipticBoundaryCalc createEllipticBoundaryCalc() {

        int[] resolution = processResolution(RPNUMERICS.getParamValue("grid", "resolution"));

        return new EllipticBoundaryCalc(new ContourParams(resolution));

    }

    public static EllipticBoundaryExtensionCalc createEllipticExtensionCalc() {

        int[] resolution = processResolution(RPNUMERICS.getParamValue("grid", "resolution"));

        int family = new Integer(getParamValue("ellipticboundaryextension", "family"));

        int characteristicWhere = new Integer(getParamValue("ellipticboundaryextension", "characteristicwhere"));

        return new EllipticBoundaryExtensionCalc(new ContourParams(resolution), characteristicWhere, family);

    }

    public static DoubleContactCurveCalc createDoubleContactCurveCalc() {

        int[] resolution = processResolution(RPNUMERICS.getParamValue("doublecontactcurve", "resolution"));

        int curveFamily = new Integer(getParamValue("doublecontactcurve", "curvefamily"));
        int domainFamily = new Integer(getParamValue("doublecontactcurve", "domainfamily"));

        System.out.println("Curve family: " + curveFamily);
        System.out.println("Domain family: " + domainFamily);

        ContourParams params = new ContourParams(resolution);

        return new DoubleContactCurveCalc(params, curveFamily, domainFamily);

    }

    public static SecondaryBifurcationCurveCalc createSecondaryBifurcationCurveCalc() {

        int[] resolution = processResolution(RPNUMERICS.getParamValue("doublecontactcurve", "resolution"));

        ContourParams params = new ContourParams(resolution);

        String methodName = HugoniotCurveCalcND.HugoniotMethods.valueOf(getParamValue("secondarybifurcationcurve", "method")).name();

        int edge = Integer.parseInt(getParamValue("secondarybifurcationcurve", "edge"));

        return new SecondaryBifurcationCurveCalc(params, methodName, edge);

    }

    public static OrbitCalc createOrbitCalc(OrbitPoint oPoint) {
        Integer direction = new Integer(getParamValue("fundamentalcurve", "direction"));

        return new OrbitCalc(oPoint, direction);

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

        int[] resolution = processResolution(RPNUMERICS.getParamValue("grid", "resolution"));

        ContourParams params = new ContourParams(resolution);

        int family = new Integer(getParamValue("hysteresiscurve", "family"));

        return new HysteresisCurveCalc(params, family);

    }

    public static CompositeCalc createCompositeCalc(OrbitPoint initialPoint) {
        Integer direction = new Integer(getParamValue("fundamentalcurve", "direction"));
        return new CompositeCalc(initialPoint, Integer.parseInt(getParamValue("fundamentalcurve", "family")), direction);

    }

    public static CompositeCalc createCompositeCalc(OrbitPoint initialPoint, Configuration configuration) {
        Integer direction = new Integer(configuration.getParam("direction"));
        return new CompositeCalc(initialPoint, new Integer(configuration.getParam("family")), direction);

    }

    public static BoundaryExtensionCurveCalc createBoundaryExtensionCurveCalc() {

        int[] resolution = processResolution(RPNUMERICS.getParamValue("grid", "resolution"));

        ContourParams params = new ContourParams(resolution);

        int edge = new Integer(getParamValue("boundaryextensioncurve", "edge"));

        int edgeResolution = new Integer(getParamValue("boundaryextensioncurve", "edgeres"));

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

    public static ShockCurveCalc createShockCurveCalc(OrbitPoint orbitPoint, int edge) {

        Integer family = new Integer(getParamValue("fundamentalcurve", "family"));
        Double tolerance = new Double(getParamValue("Newton", "tolerance"));

        Integer direction = new Integer(getParamValue("fundamentalcurve", "direction"));
        return new ShockCurveCalc(orbitPoint, family, direction, tolerance);

    }

    public static ShockCurveCalc createShockCurveCalc(OrbitPoint orbitPoint, Configuration shockConfiguration) {

        Integer family = new Integer(shockConfiguration.getParam("family"));
        Double tolerance = 0.001;//TODO : Esperar a nova inteface do choque com tolerancia ???

        Integer direction = new Integer(shockConfiguration.getParam("direction"));
        return new ShockCurveCalc(orbitPoint, family, direction, tolerance);

    }

    public static ShockCurveCalc createShockCurveCalc(OrbitPoint orbitPoint) {

        Integer family = new Integer(getParamValue("fundamentalcurve", "family"));
//        Double tolerance = new Double(getParamValue("Newton", "tolerance"));//TODO REMOVE ??

        Integer direction = new Integer(getParamValue("fundamentalcurve", "direction"));
        return new ShockCurveCalc(orbitPoint, family, direction, 0.01);

    }

    public static ContourCurveCalc createBifurcationCalc() {//TODO Remove
        ContourCurveCalc bifurcationCurveCalc = null;

        return bifurcationCurveCalc;

    }

    public static InflectionCurveCalc createInflectionCurveCalc() {

        int[] resolution = processResolution(RPNUMERICS.getParamValue("grid", "resolution"));

        ContourParams params = new ContourParams(resolution);

        int family = new Integer(RPNUMERICS.getParamValue("inflectioncurve", "family"));

        return new InflectionCurveCalc(params, family);
    }

    public static CoincidenceCurveCalc createCoincidenceCurveCalc() {

        return null;

    }

    public static RarefactionExtensionCalc createRarefactionExtensionCalc(OrbitPoint orbitPoint) {

        int[] resolution = processResolution(RPNUMERICS.getParamValue("grid", "resolution"));

        int curveFamily = new Integer(getParamValue("rarefactionextension", "curvefamily"));

        int characteristicDomain = new Integer(getParamValue("rarefactionextension", "characteristic"));

        int extensionFamily = new Integer(getParamValue("rarefactionextension", "extensionfamily"));

        Integer direction = new Integer(getParamValue("rarefactionextension", "direction"));

        return new RarefactionExtensionCalc(new ContourParams(resolution), orbitPoint, direction, curveFamily, extensionFamily, characteristicDomain);

    }

    public static ShockFlow createShockFlow(ShockFlowParams shockFlowParams) {
        ShockFlow flow = new ShockFlow(shockFlowParams, new FluxFunction(getFluxParams()));

        return flow;

    }

    public static void setDirection(Integer integer) {
        direction_ = integer;

    }

    public static int[] processResolution(String resolution) {
        String[] splitedResolution = resolution.split(" ");
        int[] result = new int[splitedResolution.length];
        try {
            for (int i = 0; i < splitedResolution.length; i++) {
                String string = splitedResolution[i];
                result[i] = new Integer(string.replaceAll(",", ""));
            }
        } catch (NumberFormatException ex) {
            System.out.println("Error in resolution format !");
        }
        return result;
    }

    public static void updateUplus(List<RealVector> eqPoints) {
        RealVector newUPlus = null;
        PhasePoint uPlus = RPNUMERICS.getViscousProfileData().getUplus();
        if (uPlus != null) {//TODO  Inicializar uPlus
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
        if (uPlus != null) {//TODO  Inicializar uPlus
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
     * @deprecated ONLY TO JAVA CALCS USE
     */
    static public final RpErrorControl errorControl() {
        return errorControl_;

    }

    /**
     *
     *
     * @deprecated ONLY TO JAVA CALCS USE !!
     */
    static public final SimplexPoincareSection pSection() {
        return ((Rk4BPProfile) odeSolver_.getProfile()).getPoincareSection();

    }

    /**
     *
     *
     * @deprecated ONLY TO JAVA CALCS USE !!
     */
    static public final ODESolver odeSolver() {
        return odeSolver_;

    }

    public static void applyFluxParams() {

//        Configuration physicsConfiguration = configMap_.get(physicsID());
//
//        HashMap<String, String> params = physicsConfiguration.getParams();
//
//        Set<String> keySet = params.keySet();
//
//        for (String paramName : keySet) {
//
//            setPhysicsParams(physicsConfiguration.getParamOrder(paramName), physicsConfiguration.getParam(paramName));
//
//        }
//        
////        FluxParams previousParams = getFluxParams();
////
////        String[] previousParamArray = new String[previousParams.getParams().getSize()];//fluxFunctionConfig.getParamsSize()];
////
////        for (int i = 0; i < previousParamArray.length; i++) {
////            previousParamArray[i] = String.valueOf(previousParams.getElement(i));
////
////        }
//
//        getViscousProfileData().setPreviousSigma(getViscousProfileData().getSigma());
//        getViscousProfileData().setPreviousXZero(getViscousProfileData().getXZero());
//        getViscousProfileData().setPreviousPhysicsParams(previousParamArray);
//        setParams(paramsArray);
    }

    /**
     * Clean up the native layer
     */
    public static native void clean();

    public static native void setPhysicsParams(String paramName, String value);

    public static native String getPhysicsParam(int paramIndex);

    public static native String[] getPhysicsParamsNames();

    public static native FluxParams getFluxParams();

    public static native void setResolution(RealVector min, RealVector max, String gridName, int[] newResolution);

    public static native String physicsID();

    public static native void setRPnHome(String rpnHome);

    public static native Boundary boundary();

    public static native void setBoundary(Boundary newBoundary);

    public static native int domainDim();

    public static native Space domain();

    public static native void clearCurvesCache();

    public static native void removeCurve(int curveID);

    public static native List<String> getEigenSortFunctionNames();

    public static native void setEigenSortFunction(String functionName);

    public static native Vector<String> getLabelVector();

    public static native ArrayList<String> getTransisionalLinesNames();

    public static native void readNativePhysicsConfig();

    public static native List<String> getAuxFunctionNames();

    public static native void setAuxFuntionParam(String auxFuncName, String auxFuncParam, String value);

    public static native String[] getHugoniotCaseNames(String hugoniotMethodName);

}
