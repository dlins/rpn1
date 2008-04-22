/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpnumerics;

import wave.util.RealVector;
import wave.util.RealMatrix2;
import wave.util.VectorFunction;

public class HugoniotCurveCalcND
        implements HugoniotCurveCalc {
    //
    // Constants
    //
    static public final double UMINUS_SHIFT = .01;
    //
    // Members
    //
    
    private VectorFunction f_;
    
    private RealVector Uminus_;
    private RealVector Fminus_;
    private RealMatrix2 DFminus_;
    
    public void uMinusChangeNotify(PhasePoint uMinus) {
        
        Uminus_=uMinus;
        setUMinus(uMinus);
    }
    
    
    
    public PhasePoint getUMinus() { return new PhasePoint(Uminus_); }
    public RealVector getFMinus() { return Fminus_; }
    public RealMatrix2 getDFMinus() { return DFminus_; }
    public double[] getPrimitiveUMinus(){return Uminus_.toDouble();}
    
    
    public native void setUMinus(PhasePoint uMinus);
    
    public  RpSolution calc(){
        
        return calc(Uminus_);
        
    }
    
    public   RpSolution recalc() {
        
        return calc();
        
    }
    
    private native RpSolution calc(RealVector uMinus);
    
    
}
