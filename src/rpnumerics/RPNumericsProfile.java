/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpnumerics;


import rpn.controller.ui.*;
import rpnumerics.HugoniotCurveCalc;
import rpnumerics.Physics;
import rpnumerics.WaveFlow;
import wave.util.Boundary;

public class RPNumericsProfile {

    private boolean isNativePhysics_;
    public Physics physics_;

    private Boundary boundary_;
    private boolean hasBoundary_ = false;

    private HugoniotCurveCalc hugoniotCurveCalc_;
    private static WaveFlow flow_ = null;
    private PhasePoint phasePoint_;
    private double sigma_;
    private Integer familyIndex_, minus_, plus_;
    private int[] variations_;
    private boolean isSpecificHugoniot_;
    private boolean isSpecificShockFlow_;
    private boolean contourMethod_;
    private static boolean flowInitialized_ = false;
    private static UserInputHandler initialState_ = null;
    private String nativeLibName_;
    private String flowType_;
    private String physicsId_;
    private String curveName_;
    private String contourType_;

    // Constructors/Initializers

    public void initPhysics(String physicsId, Boolean isNative,
                            String nativeLibName, Integer minus, Integer plus) {

        System.out.println("isNative=" + isNative);
        isNativePhysics_ = isNative.booleanValue();
        physicsId_ = physicsId;
        nativeLibName_ = nativeLibName;
        minus_ = minus;
        plus_ = plus;

    }

    public void initSpecificHugoniotAndFlow(boolean specificHugoniot,
                                            boolean specificShockFlow) throws
            RPWrongInitParamsException {

        isSpecificHugoniot_ = specificHugoniot;
        isSpecificShockFlow_ = specificShockFlow;
    }

    public void initFlowType(String flowType) {

        flowType_ = flowType;

        if (flowType.equals("shockflow")) {
            setInitialState(new SHOCK_CONFIG());
        }

        if (flowType.equals("rarefactionflow")|| flowType.equals("blowuprarefactionflow")) {
            setInitialState(new RAREFACTION_CONFIG());
        }


        if (initialState_ == null) {
            System.out.println("Wrong flow type!");
        }

    }

    //Acessors

    public void setContourMethod(boolean isContour) {
        contourMethod_ = isContour;
    }

    public void setContourType(String contourType) {
        contourType_ = contourType;
    }

    public void setFamilyIndexFlow(int familyIndex) {
        familyIndex_ = new Integer(familyIndex);
    }

    public void setBoundary(Boundary boundary) {
        boundary_ = boundary;
        hasBoundary_ = true;
    }

    public void setVariations(int[] variations) {
        variations_ = variations;
    }

    public void setCurveName(String name) {
        curveName_ = name;
    }


    public boolean hasBoundary() {
        return hasBoundary_;
    }

    public Physics getPhysics() {
        return physics_;
    }

    public boolean isNativePhysics() {
        return isNativePhysics_;
    }

    public Boundary getBoundary() {

        if (hasBoundary()) {

            return boundary_;
        } else {

            return RPNUMERICS.boundary();
        }
    }

    public int[] getVariations() {
        return variations_;
    }

    public int getMinus() {
        return minus_.intValue();
    }

    public int getPlus() {
        return plus_.intValue();
    }

    public String getPhysicsID() {
        return physicsId_;
    }

    public String getCurveName() {
        return curveName_;
    }

//    public WaveFlow getFlow() {
//        return flow_;
//    }

    public HugoniotCurveCalc getHugoniotCurveCalc() {
        return hugoniotCurveCalc_;
    }

    public String libName() {
        return nativeLibName_;
    }

    public boolean isFlowInitialized() {
        return flowInitialized_;
    }

    public boolean isContourMethod() {
        return contourMethod_;
    }

    public boolean isSpecificHugoniot() {
        return isSpecificHugoniot_;
    }

    public boolean isSpecificShockFlow() {
        return isSpecificShockFlow_;
    }

    public int getFamilyIndex() {
        return familyIndex_.intValue();
    }

    public String getFlowType() {
        return flowType_;
    }

    public static UserInputHandler getInitialState() {
        return initialState_;
    }


    //Mutators

    //public void setAccumulationParams (double [] doubleArray){ physics_.accumulationFunction().accumulationParams().setParams(doubleArray);}

    public static void setInitialState(UserInputHandler userInput) {
        initialState_ = userInput;
    }


}
