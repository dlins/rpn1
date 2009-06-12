/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
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
    
    private static HashMap<String,MethodProfile> methodsMap_=new HashMap<String, MethodProfile>();
//    static private Physics physics_ = null;
    static private RpErrorControl errorControl_ = null;
    static private ODESolver odeSolver_ = null;
    static private ShockProfile shockProfile_ = ShockProfile.instance();
    static private RarefactionProfile rarefactionProfile_ = RarefactionProfile.instance();
    static private BifurcationProfile bifurcationProfile_ = BifurcationProfile.instance();
    static private ShockRarefactionProfile shockRarefactionProfile_ = null;
    static private int[] CONTOUR_RESOLUTION = {100, 100};
    static private ContourConfiguration contourConfiguration_ = new ContourConfiguration();

    static private FluxParams fluxParams_;
    //
    // Constructors/Initializers
    //
    static public void init(RPNumericsProfile profile) {

        System.loadLibrary("wave");//TODO libwave is always loaded ?
        System.loadLibrary("rpnumerics");
        initNative(profile.getPhysicsID());
        errorControl_ = new RpErrorControl(boundary());
        fluxParams_ = getFluxParams();

        System.out.println("Inicializando");
    }

    static public void init(String physicsID) {
        System.loadLibrary("wave");//TODO libwave is always loaded ?
        System.loadLibrary("rpnumerics");
        initNative(physicsID);
        errorControl_ = new RpErrorControl(boundary());
        fluxParams_ = getFluxParams();

                System.out.println("Inicializando com o nome da fisica");
    }
    
    public static void addMethod(String methodName,MethodProfile profile){

        methodsMap_.put(methodName, profile);
        
    }
    
    public static void addParam(String methodName,String paramName,String paramValue){
        
        if ( !methodsMap_.containsKey(methodName)){
            
            MethodProfile newProfile =new MethodProfile(methodName);
            
            newProfile.addParam(paramName, paramValue);
            
            methodsMap_.put(methodName,newProfile);
        }
        
        else{
            
            MethodProfile methodProfile = methodsMap_.get(methodName);
            
            methodProfile.addParam(paramName, paramValue);
            
            methodsMap_.put(methodName, methodProfile);
            
        }        
        
    }
    
    public static ArrayList<MethodProfile> getAllMethodsProfiles(){
        
        ArrayList <MethodProfile> returnedArrayList = new ArrayList<MethodProfile>();
        
        Set<Entry<String, MethodProfile>> methodSet = methodsMap_.entrySet();
        
        Iterator<Entry<String, MethodProfile>> methodIterator = methodSet.iterator();
        
        while (methodIterator.hasNext()) {
            Entry<String, MethodProfile> entry = methodIterator.next();
            
            returnedArrayList.add(entry.getValue());
            
        }
        
        
        System.out.println("Tamanho dos parametros em rpnumerics:" + returnedArrayList.size());
        
        return returnedArrayList;
        
    }
    
    
    public static MethodProfile getMethodProfile(String methodName){
        
        return methodsMap_.get(methodName);
        
    }
    
    public static void removeMethod(String methodName){
        
        methodsMap_.remove(methodName);
        
    }

    /**
     * 
     * @deprecated
     */
    public static native void initNative(String physicsName);
    
    public static HugoniotCurveCalc createHugoniotCalc() {

        HugoniotCurveCalc hugoniotCurveCalc = null;
        HugoniotParams hparams = new HugoniotParams(shockProfile_.getXZero(), new FluxFunction(fluxParams_));

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

    public static OrbitCalc createOrbitCalc(OrbitPoint orbitPoint, int timeDirection) {

        ShockFlow flow = (ShockFlow) createShockFlow();
        return new OrbitCalc(orbitPoint, timeDirection, createODESolver(flow));

    }

    public static BifurcationCurveCalc createBifurcationCalc() {
        return new BifurcationCurveCalc();
    }

    public static ShockFlow createShockFlow() {

        FluxFunction flux = new FluxFunction(fluxParams_);

        PluginProfile profile = PluginTableModel.getPluginConfig(ShockProfile.SHOCKFLOW_NAME);

        Double sigmaValue = new Double(profile.getParamValue("sigma"));

        ShockFlowParams shockParams = new ShockFlowParams(shockProfile_.getXZero(), sigmaValue.doubleValue());

        ShockFlow flow = new ShockFlow(shockParams, flux);
        return flow;

    }

    public static ShockFlow createShockFlow(ShockFlowParams shockFlowParams) {
        ShockFlow flow = new ShockFlow(shockFlowParams, new FluxFunction(fluxParams_));
        return flow;
    }

    public static RarefactionFlow createRarefactionFlow() {

        return new RarefactionFlow(rarefactionProfile_.getXZero(), rarefactionProfile_.getFamily(), new FluxFunction(fluxParams_));

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

    //TODO 
    /**
     * 
     * @deprecated CHANGE TO RPFUNCTION REFERENCES !!
     */
//    static public final FluxFunction fluxFunction() {
////        return physics_.fluxFunction();
//        return fluxFunction_;
//    }

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

    public static void setFluxParams(FluxParams fluxParams) {
        fluxParams_ = fluxParams;
    }

    public static native FluxParams getFluxParams();

    public static native String physicsID();

    public static native Boundary boundary();

    public static native void setBoundary(Boundary newBoundary);

    public static native int domainDim();

    public static native Space domain();
}
