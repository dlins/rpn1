/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import java.util.logging.Level;
import java.util.logging.Logger;
import rpn.configuration.Configuration;
import rpnumerics.viscousprofile.ViscousProfileData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import rpn.configuration.BoundaryConfiguration;
import rpn.configuration.RPnConfig;
import rpn.configuration.ConfigurationProfile;
import rpn.configuration.PhysicsConfiguration;
import rpn.parser.RPnDataModule;
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
        try {
            System.loadLibrary("rpn");
            setRPnHome(System.getProperty("rpnhome"));

            initNative(physicsID);

            //Processing configuration data

            ConfigurationProfile physicsProfile = RPnConfig.getActivePhysicsProfile();

            HashMap<String, Configuration> configurationList = RPnConfig.createConfigurationList(physicsProfile);

            Configuration physicsConfiguration = new PhysicsConfiguration(physicsProfile, configurationList);


//            //Accumulation function
//
//            Configuration accumulationFunctionConfig = physicsConfiguration.getConfiguration("accumulationfunction");
//
//            if (accumulationFunctionConfig != null) {
//                //            System.out.println("Printando configuration para acumulacao: " + accumulationFunctionConfig.getParamsSize());
//                //            System.out.println(accumulationFunctionConfig);
//
//                RealVector newAccParams = new RealVector(accumulationFunctionConfig.getParamsSize());
//                for (int i = 0; i < newAccParams.getSize(); i++) {
//                    //            System.out.println(accumulationFunctionConfig);
//                    newAccParams.setElement(i, new Double(accumulationFunctionConfig.getParam(i)));
//
//                }
//                setAccumulationParams(newAccParams);
//            } else {
//
//                RealVector paramsVector = getAccumulationParams();
//
//                Configuration accumlationFunctionConfiguration = new PhysicsConfiguration("accumulationfunction");
//
//                for (int i = 0; i < paramsVector.getSize(); i++) {
//                    accumlationFunctionConfiguration.setParamValue("param " + i, String.valueOf(paramsVector.getElement(i)));
//                    accumlationFunctionConfiguration.setParamOrder("param " + i, i);
//                }
//
//                physicsConfiguration.addConfiguration("accumulationfunction", accumlationFunctionConfiguration);
//            }

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

                //Catching boundary from input file

                Configuration boundaryConfiguration = new BoundaryConfiguration(boundaryProfile);

                if (boundaryConfiguration.getName().equals("rect")) {
                    setBoundary(new RectBoundary(boundaryConfiguration.getParam("limits")));
                }


                if (boundaryConfiguration.getName().equals("Three_Phase_Boundary")) {
                    setBoundary(new IsoTriang2DBoundary(boundaryConfiguration.getParam("limits")));
                }

                //            System.out.println("Limits : " + boundaryConfiguration.getParam("limits"));

            } else {//            System.out.println("Limits : " + boundaryConfiguration.getParam("limits"));

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

                Configuration boundaryConfiguration = new BoundaryConfiguration(defaultBoundaryProfile);
                physicsConfiguration.addConfiguration(boundaryConfiguration.getName(), boundaryConfiguration);


            }


            configMap_.put(physicsID, physicsConfiguration);
            errorControl_ = new RpErrorControl(boundary());
        } catch (Exception ex) {
            Logger.getLogger(RPNUMERICS.class.getName()).log(Level.SEVERE, null, ex);
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

    public static HugoniotCurveCalc createHugoniotCalc(RealVector input) {

        int[] resolution = RPnDataModule.processResolution(getParamValue("hugoniotcurve", "resolution"));

        Integer direction = new Integer(getParamValue("fundamentalcurve", "direction"));

        HugoniotParams params = new HugoniotParams(new PhasePoint(input), direction, resolution);

        return new HugoniotCurveCalcND(params);
    }

    public static EnvelopeCurveCalc createEnvelopeCurveCalc(Configuration configuration) {

        int[] resolution = RPnDataModule.processResolution(configuration.getParam("resolution"));
        int whereIsConstant = new Integer(configuration.getParam("edge"));
        int numberOfSteps = new Integer(configuration.getParam("numberofsteps"));

        return new EnvelopeCurveCalc(new ContourParams(resolution), whereIsConstant, numberOfSteps);

    }

    public static EllipticBoundaryCalc createEllipticBoundaryCalc(Configuration configuration) {


        int[] resolution = RPnDataModule.processResolution(configuration.getParam("resolution"));

        return new EllipticBoundaryCalc(new ContourParams(resolution));



    }
    
    public static WaveCurveCalc createBoundaryWaveCurve (OrbitPoint orbitPoint,int edge){
        Integer direction = new Integer(getParamValue("fundamentalcurve", "direction"));
        return new WaveCurveCalc(orbitPoint, Integer.parseInt(getParamValue("fundamentalcurve", "family")), direction,edge);
    }

    public static RarefactionCurveCalc createRarefactionCalc(OrbitPoint orbitPoint) {
        Integer direction = new Integer(getParamValue("fundamentalcurve", "direction"));
        return new RarefactionCurveCalc(orbitPoint, Integer.parseInt(getParamValue("fundamentalcurve", "family")), direction);

    }
    
     public static RarefactionCurveCalc createRarefactionCalc(OrbitPoint orbitPoint,int edge) {
        Integer direction = new Integer(getParamValue("fundamentalcurve", "direction"));
        return new RarefactionCurveCalc(orbitPoint, Integer.parseInt(getParamValue("fundamentalcurve", "family")), direction,edge);

    }
    

    public static DoubleContactCurveCalc createDoubleContactCurveCalc(Configuration configuration) {

        int[] resolution = RPnDataModule.processResolution(configuration.getParam("resolution"));

        int curveFamily = new Integer(configuration.getParam("curvefamily"));
        int domainFamily = new Integer(configuration.getParam("domainfamily"));

        ContourParams params = new ContourParams(resolution);

        return new DoubleContactCurveCalc(params, curveFamily, domainFamily);

    }

    public static EllipticBoundaryExtensionCalc createEllipticExtensionCalc(Configuration configuration) {


        int[] resolution = RPnDataModule.processResolution(configuration.getParam("resolution"));

        int family = new Integer(configuration.getParam("family"));

        int characteristicWhere = new Integer(configuration.getParam("characteristicwhere"));

        return new EllipticBoundaryExtensionCalc(new ContourParams(resolution), characteristicWhere, family);



    }

    public static SecondaryBifurcationCurveCalc createSecondaryBifurcationCurveCalc(Configuration configuration) {

        int[] resolution = RPnDataModule.processResolution(configuration.getParam("resolution"));

        ContourParams params = new ContourParams(resolution);

        return new SecondaryBifurcationCurveCalc(params);

    }

    public static WaveCurveCalc createWaveCurveCalc(OrbitPoint orbitPoint) {
        Integer direction = new Integer(getParamValue("fundamentalcurve", "direction"));
        return new WaveCurveCalc(orbitPoint, Integer.parseInt(getParamValue("fundamentalcurve", "family")), direction);

    }

    public static LevelCurveCalc createPointLevelCurveCalc(RealVector orbitPoint) {



        int[] resolution = RPnDataModule.processResolution(getParamValue("hugoniotcurve", "resolution"));

        int family = new Integer(getParamValue("levelcurve", "family"));

        return new PointLevelCalc(orbitPoint, family, new ContourParams(resolution));

    }

    public static CoincidenceExtensionCurveCalc createCoincidenceExtensionCurveCalc(Configuration configuration) {

        int[] resolution = RPnDataModule.processResolution(configuration.getParam("resolution"));

        int leftFamily = new Integer(configuration.getParam("curvefamily"));
        int rightFamily = new Integer(configuration.getParam("domainfamily"));
        int characteristicDomain = new Integer(configuration.getParam("characteristicwhere"));

        ContourParams params = new ContourParams(resolution);

        return new CoincidenceExtensionCurveCalc(params, leftFamily, rightFamily, characteristicDomain);
    }

    public static ExtensionCurveCalc createExtensionCurveCalc(List<RealSegment> segments, CoordsArray[] areaSelected) {

        int[] resolution = RPnDataModule.processResolution(getParamValue("extensioncurve", "resolution"));
        int family = new Integer(getParamValue("extensioncurve", "family"));
        int characteristic = new Integer(getParamValue("extensioncurve", "characteristic"));
        boolean singular = new Boolean(getParamValue("extensioncurve", "singular"));
        

        int insideArea;

        ArrayList<RealVector> realVectorList = new ArrayList<RealVector>();

        if (areaSelected == null) {
            insideArea=1;
        }
        else {
            insideArea = new Integer(getParamValue("extensioncurve", "domain"));
            for (int i = 0; i < areaSelected.length; i++) {
                realVectorList.add(new RealVector(areaSelected[i].getCoords()));

            }

        }


        return new ExtensionCurveCalc(new ContourParams(resolution), segments, realVectorList, family, characteristic, singular, insideArea);

    }

    public static BoundaryExtensionCurveCalc createBoundaryExtensionCurveCalc(Configuration configuration) {


        int[] resolution = RPnDataModule.processResolution(configuration.getParam("resolution"));

        int edge = new Integer(configuration.getParam("edge"));

        int edgeResolution = new Integer(configuration.getParam("edgeresolution"));

        int characteristic = new Integer(configuration.getParam("characteristicwhere"));

        int family = new Integer(configuration.getParam("family"));

        ContourParams params = new ContourParams(resolution);

        return new BoundaryExtensionCurveCalc(params, edgeResolution, family, edge, characteristic);
    }

    //public ExtensionCurveCalc(ContourParams contourParams, List<RealSegment> list, int extensionFamily, int characteristicDomain, boolean singular) {
    public static EnvelopeCurveCalc createEnvelopeCurveCalc() {

        int[] resolution = RPnDataModule.processResolution(getParamValue("envelopecurve", "resolution"));
        int whereIsConstant = new Integer(getParamValue("envelopecurve", "edge"));
        int numberOfSteps = new Integer(getParamValue("envelopecurve", "numberofsteps"));

        return new EnvelopeCurveCalc(new ContourParams(resolution), whereIsConstant, numberOfSteps);

    }

    public static EllipticBoundaryExtensionCalc createEllipticBoundaryExtensionCalc(Configuration configuration) {

        int[] resolution = RPnDataModule.processResolution(configuration.getParam("resolution"));

        int curveFamily = new Integer(configuration.getParam("curvefamily"));
        int domainFamily = new Integer(configuration.getParam("domainfamily"));

        ContourParams params = new ContourParams(resolution);

        return new EllipticBoundaryExtensionCalc(params, curveFamily, domainFamily);

    }

    public static LevelCurveCalc createLevelCurveCalc(double level) {

        int[] resolution = RPnDataModule.processResolution(getParamValue("hugoniotcurve", "resolution"));

        int family = new Integer(getParamValue("levelcurve", "family"));

        return new LevelCurveCalc(family, level, new ContourParams(resolution));

    }

    public static InflectionCurveCalc createInflectionCurveCalc(Configuration configuration) {

        int[] resolution = RPnDataModule.processResolution(configuration.getParam("resolution"));
        int family = Integer.parseInt(configuration.getParam("family"));
        ContourParams params = new ContourParams(resolution);

        return new InflectionCurveCalc(params, family);
    }

    public static HysteresisCurveCalc createHysteresisCurveCalc(Configuration configuration) {

        int[] resolution = RPnDataModule.processResolution(configuration.getParam("resolution"));

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
    
    
    
    
     public static SecondaryBifurcationCurveCalc createStoneSecondaryBifurcationCurveCalc() {

        int[] resolution = RPnDataModule.processResolution(getParamValue("bifurcationcurve", "resolution"));

        int edgeresolution = new Integer (getParamValue("explicitsecondarybifurcation", "numberofsteps"));
        int edge =  new Integer (getParamValue("explicitsecondarybifurcation", "edge"));

        ContourParams params = new ContourParams(resolution);

        return new StoneExplicitSecondaryBifurcationCurveCalc(params, edge, edgeresolution);

    }
    
    

    public static SecondaryBifurcationCurveCalc createSecondaryBifurcationCurveCalc() {

        //int[] resolution = RPnDataModule.processResolution(getParamValue("inflectioncurve", "resolution"));

        int[] resolution = RPnDataModule.processResolution(getParamValue("doublecontactcurve", "resolution"));


        ContourParams params = new ContourParams(resolution);

        return new SecondaryBifurcationCurveCalc(params);

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

        int[] resolution = RPnDataModule.processResolution(getParamValue("hysteresiscurve", "resolution"));

        ContourParams params = new ContourParams(resolution);

        int family = new Integer(getParamValue("hysteresiscurve", "family"));

        return new HysteresisCurveCalc(params, family);

    }

    public static CompositeCalc createCompositeCalc(OrbitPoint initialPoint) {
        Integer direction = new Integer(getParamValue("fundamentalcurve", "direction"));
        return new CompositeCalc(initialPoint, Integer.parseInt(getParamValue("fundamentalcurve", "family")), direction);


    }
    
    
    public static CompositeCalc createCompositeCalc(OrbitPoint initialPoint,Configuration configuration) {
        Integer direction = new Integer(configuration.getParam("direction"));
        return new CompositeCalc(initialPoint, new Integer(configuration.getParam("family")), direction);


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
    
    
   public static ShockCurveCalc createShockCurveCalc(OrbitPoint orbitPoint,int edge) {

        Integer family = new Integer(getParamValue("fundamentalcurve", "family"));
        Double tolerance = new Double(getParamValue("Newton", "tolerance"));

        Integer direction = new Integer(getParamValue("fundamentalcurve", "direction"));
        return new ShockCurveCalc(orbitPoint, family, direction, tolerance);


    }
    
    
     public static ShockCurveCalc createShockCurveCalc(OrbitPoint orbitPoint,Configuration shockConfiguration) {

        Integer family = new Integer(shockConfiguration.getParam("family"));
        Double tolerance = 0.001;//TODO : Esperar a nova inteface do choque com tolerancia ???

        Integer direction = new Integer(shockConfiguration.getParam("direction"));
        return new ShockCurveCalc(orbitPoint, family, direction, tolerance);


    }


    public static ShockCurveCalc createShockCurveCalc(OrbitPoint orbitPoint) {

        Integer family = new Integer(getParamValue("fundamentalcurve", "family"));
        Double tolerance = new Double(getParamValue("Newton", "tolerance"));

        Integer direction = new Integer(getParamValue("fundamentalcurve", "direction"));
        return new ShockCurveCalc(orbitPoint, family, direction, tolerance);


    }

    public static ContourCurveCalc createBifurcationCalc() {//TODO Remove
        ContourCurveCalc bifurcationCurveCalc = null;

        return bifurcationCurveCalc;


    }

    public static InflectionCurveCalc createInflectionCurveCalc() {

        int[] resolution = RPnDataModule.processResolution(RPNUMERICS.getParamValue("bifurcationcurve", "resolution"));

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
            previousParamArray[i] = String.valueOf(previousParams.getElement(i));

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

    public static native void setMethod(String methodType, String methodName);

    public static native int domainDim();

    public static native Space domain();

    private static native void setFluxParams(FluxParams fluxParams);

    private static native void setAccumulationParams(RealVector accumulationParams);
}
