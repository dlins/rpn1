/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import wave.util.RealVector;
import wave.util.RealMatrix2;

public class HugoniotCurveCalcND extends ContourCurveCalc implements HugoniotCurveCalc {
    //
    // Constants
    //

    static public final double UMINUS_SHIFT = .01;
  
    //
    // Constructors
    //
    public HugoniotCurveCalcND(HugoniotParams params ){
        super(params);

    }

    //
    // Accessors/Mutators
    //
    public void uMinusChangeNotify(PhasePoint uMinus) {

        setUMinus(uMinus);

    }

    public void setUMinus(PhasePoint pPoint) {
//        Uminus_ = pPoint.getCoords();
//        Fminus_ = rpnumerics.RPNUMERICS.fluxFunction().F(Uminus_);

//        hugoniotParams_.uMinusChangeNotify(pPoint);

//        hugoniotParams_.setFMinus(rpnumerics.RPNUMERICS.fluxFunction().F(Uminus_));

//        DFminus_ = rpnumerics.RPNUMERICS.fluxFunction().DF(Uminus_);
//        hugoniotParams_.setDFMinus(rpnumerics.RPNUMERICS.fluxFunction().DF(Uminus_));
//        hugoniotParams_.setUMinus(Uminus_);
//         DFminus_ = rpnumerics.RPNUMERICS.fluxFunction().DF(Uminus_);
//        f_ = new GenericHugoniotFunction(hugoniotParams_);
    }

   
/**
 *
 * @deprecated
 */
    public RealVector getFMinus() {
//        return new PhasePoint(hugoniotParams_.getFMinus());
        return null;


    }


    /**
     *
     * @deprecated
     */
    public RealMatrix2 getDFMinus() {
//        return hugoniotParams_.getDFMinus();

        return null;

    }


    public RpSolution calc() throws RpException {
             
        HugoniotCurve result;
       
        result= (HugoniotCurve) calc(((HugoniotParams)getParams()).getXZero(), getParams().getResolution());
        return result;

    }

    public RpSolution recalc() throws RpException {

        return calc();
    }


    @Override
    public RpSolution recalc(Area area) throws RpException {

        HugoniotCurve result;
        result = (HugoniotCurve) calc(((HugoniotParams)getParams()).getXZero(), (int)area.getResolution().getElement(0), (int)area.getResolution().getElement(1), area.getTopRight(), area.getDownLeft());

        return result;
    }

  
    public PhasePoint getUMinus() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public double[] getPrimitiveUMinus() {
        throw new UnsupportedOperationException("Not supported yet.");
    }



    private native RpSolution calc(PhasePoint initialpoint, int resolution[]) throws RpException;

    private native RpSolution calc(PhasePoint initialpoint, int xRes_, int yRes_, RealVector topR, RealVector dwnL) throws RpException;


}
