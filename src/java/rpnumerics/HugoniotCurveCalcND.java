/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import rpnumerics.methods.HugoniotContourMethod;
import wave.util.RealVector;
import wave.util.RealMatrix2;

public class HugoniotCurveCalcND implements HugoniotCurveCalc {
    //
    // Constants
    //

    static public final double UMINUS_SHIFT = .01;
    //
    // Members
    //
 

 
    private HugoniotParams hugoniotParams_;
   

    private  int resolution_ [];

    //
    // Constructors
    //
    public HugoniotCurveCalcND(HugoniotParams params ){
        hugoniotParams_ = params;
    }





    public HugoniotCurveCalcND(HugoniotContourMethod hugoniotMethod) {          // ****** ISTO É USADO? ******
//        hugoniotMethod_ = hugoniotMethod;
        hugoniotParams_ = hugoniotMethod.getParams();
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

        hugoniotParams_.uMinusChangeNotify(pPoint);

//        hugoniotParams_.setFMinus(rpnumerics.RPNUMERICS.fluxFunction().F(Uminus_));

//        DFminus_ = rpnumerics.RPNUMERICS.fluxFunction().DF(Uminus_);
//        hugoniotParams_.setDFMinus(rpnumerics.RPNUMERICS.fluxFunction().DF(Uminus_));
//        hugoniotParams_.setUMinus(Uminus_);
//         DFminus_ = rpnumerics.RPNUMERICS.fluxFunction().DF(Uminus_);
//        f_ = new GenericHugoniotFunction(hugoniotParams_);
    }

   

    public RealVector getFMinus() {
        return new PhasePoint(hugoniotParams_.getFMinus());

    }

    public RealMatrix2 getDFMinus() {
        return hugoniotParams_.getDFMinus();

    }


    public RpSolution calc() throws RpException {
        //		System.out.println("DEBUG - will do the approximation");
        // the XZero shift will be the reference point for now...

//        wave.util.RealVector initialPoint = getUMinus();//rpnumerics.RPNUMERICS.hugoniotCurveCalc().getUMinus().getCoords();
//        for (int i = 0; i < initialPoint.getSize(); i++) {
//            initialPoint.setElement(i, initialPoint.getElement(i) + UMINUS_SHIFT);
//
//        }
        
        HugoniotCurve result;
       
        result= (HugoniotCurve) calc(hugoniotParams_.getXZero(), hugoniotParams_.getResolution());

        System.out.println("Tamanho de result: " + result.segments().size());
        return result;

    }

    public RpSolution recalc() throws RpException {

        return calc();
    }

    public HugoniotParams getParams() {
        return hugoniotParams_;
    }


    //private native RpSolution calc(PhasePoint initialpoint, int xRes_, int yRes_) throws RpException;


    


    public RpSolution recalc(Area area) throws RpException {

        HugoniotCurve result;
        result = (HugoniotCurve) calc(hugoniotParams_.getXZero(), (int)area.getResolution().getElement(0), (int)area.getResolution().getElement(1), area.getTopRight(), area.getDownLeft());

        return result;
    }

    private native RpSolution calc(PhasePoint initialpoint,int resolution[]) throws RpException;

    private native RpSolution calc(PhasePoint initialpoint, int xRes_, int yRes_, RealVector topR, RealVector dwnL) throws RpException;

    public PhasePoint getUMinus() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public double[] getPrimitiveUMinus() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
