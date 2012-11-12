package rpnumerics;

import wave.util.*;

public class HugoniotParams extends ContourParams {

    private RealVector xZero_;
    private RealVector fMinus_;
    private RealVector uMinus_;
    private RealMatrix2 dFMinus_;
    private FluxFunction fluxFunction_;
    private int direction_;

    public HugoniotParams(PhasePoint xZero, int direction, int[] resolution) {

        super(resolution);
        xZero_ = xZero;

        direction_ = direction;


    }

    public void uMinusChangeNotify(PhasePoint uMinus) {

        setUMinus(uMinus);

    }

    public void setUMinus(PhasePoint pPoint) {

        WaveState input = new WaveState(pPoint);
        JetMatrix output = new JetMatrix(pPoint.getSize());
        getFluxFunction().jet(input, output, 1);

        dFMinus_ = new RealMatrix2(output.n_comps(), output.n_comps()); //TODO Replace for JacobianMatrix

        for (int i = 0; i < output.n_comps(); i++) {
            for (int j = 0; j < output.n_comps(); j++) {
                dFMinus_.setElement(i, j, output.getElement(i, j));
            }
        }

        uMinus_ = pPoint.getCoords();
        fMinus_ = output.f();
//        fMinus_ = getFluxFunction().F(uMinus_);
//        dFMinus_ = getFluxFunction().DF(uMinus_);
    }

    public PhasePoint getXZero() {

        return new PhasePoint(xZero_);
    }

    public void setXZero(RealVector p) {


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

    public int getDirection() {
        return direction_;
    }

    public void setDirection(int direction_) {
        this.direction_ = direction_;
    }
}
