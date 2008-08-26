package rpnumerics;

import wave.util.*;

public class HugoniotParams {

    private PhasePoint xZero_;
    private RealVector fMinus_;
    private RealVector uMinus_;
    private RealMatrix2 dFMinus_;
    private FluxFunction fluxFunction_;
    public HugoniotParams(PhasePoint xZero) {

        xZero_ = xZero;

    }

    public HugoniotParams(PhasePoint xZero,FluxFunction fluxFunction) {

        xZero_ = xZero;
        fluxFunction_=fluxFunction;

    }

    
    
     public void uMinusChangeNotify(PhasePoint uMinus) {

        setUMinus(uMinus);

    }

    public void setUMinus(PhasePoint pPoint) {
        uMinus_ = pPoint.getCoords();
        fMinus_ = getFluxFunction().F(uMinus_);
        dFMinus_ = getFluxFunction().DF(uMinus_);
    }

    public PhasePoint getXZero() {

        return xZero_;
    }

    public void setXZero(PhasePoint p) {


        xZero_ = p;

    }

    public RealVector getFMinus() {
        return fMinus_;
    }

    public void setFMinus(RealVector fMinus_) {
        this.fMinus_ = fMinus_;
    }

    public RealVector getUMinus() {
        return uMinus_;
    }

    public void setUMinus(RealVector uMinus_) {
        this.uMinus_ = uMinus_;
    }

    public RealMatrix2 getDFMinus() {
        return dFMinus_;
    }

    public void setDFMinus(RealMatrix2 dFMinus_) {
        this.dFMinus_ = dFMinus_;
    }

    public FluxFunction getFluxFunction() {
        return fluxFunction_;
    }
}
