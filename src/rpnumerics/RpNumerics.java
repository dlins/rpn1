/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpnumerics;

import wave.util.*;
import wave.multid.Space;

public class RPNUMERICS {
    //
    // Constants
    //
    
    // Members
    //
    static private RPNumericsProfile profile_;
    static private HugoniotCurveCalc hugoniotCurveCalc_ = new HugoniotCurveCalcND();
    
//    private static WaveFlow flow_;
    
    
    //
    // Constructors/Initializers
    //
    
    public native static void init(RPNumericsProfile profile) throws RpException;
    
//    private native static Physics initPhysics();
    
    private native static void initShockFlow(PhasePoint xZero,double sigma);
    
    private native static void initRarefactionFlow(PhasePoint xZero,String flowType);
    
    public native static PoincareSection changePoincareSection(ConnectionOrbit section);
    
    public native static void setPoincareSectionFlag(boolean flag);
    
    public native static  void changeFluxParams(double [] params);
    
    public native static void changeErrorControl(double eps);
    
    public native static void changeSigma(double sigma);
    
    public native static void changeSigma(RealVector sigma);
    
    public static native double getSigma();
    
    public static native PhasePoint getXZero();
    
    public static native double setXZero(PhasePoint xzero);
    
    public static native double [] getFluxParams();
    
    public static native  void setShockFlow(PhasePoint xZero, double sigma);
    
    public static native void setRarefactionFlow(PhasePoint xZero);
    
    public static native Boundary boundary();
 
    //
    // Accessors
    //
    
    static public final Space domain() { return profile_.getDomain(); }
    
    static public final HugoniotCurveCalc hugoniotCurveCalc() { return hugoniotCurveCalc_; }
    
    static public final RPNumericsProfile getProfile() { return profile_;}
    
    static public final int domainDim() { return domain().getDim();    }
    
    public static String physicsID() {
        return profile_.getPhysicsID();
    }
    
    static void setProfile(RPNumericsProfile rPNumericsProfile) {
        profile_=rPNumericsProfile;
    }
    
    
    
    
    
}
