/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import rpnumerics.methods.HugoniotMethod;
import rpnumerics.methods.HugoniotContinuationMethod;
import rpnumerics.methods.HugoniotContourMethod;
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
    private HugoniotMethod hugoniotMethod_;
    private RealVector Uminus_;
    private HugoniotParams hugoniotParams_;

    //
    // Constructors
    //
    public HugoniotCurveCalcND(RealVector uMinus) {
        Uminus_=new PhasePoint(uMinus);
    }

    public HugoniotCurveCalcND(HugoniotContinuationMethod hugoniotMethod) {

        hugoniotMethod_ = hugoniotMethod;

        hugoniotParams_ = hugoniotMethod.getParams();
        f_ = hugoniotMethod.getFunction();//(HugoniotContinuationParams) hugoniotParams_).getFunction();


    }

    public HugoniotCurveCalcND(HugoniotContourMethod hugoniotMethod) {
        hugoniotMethod_ = hugoniotMethod;
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
        f_ = new GenericHugoniotFunction(hugoniotParams_);
    }

    public PhasePoint getUMinus() {
        return new PhasePoint(Uminus_);
    }

    public RealVector getFMinus() {
        return new PhasePoint(hugoniotParams_.getFMinus());

    }

    public RealMatrix2 getDFMinus() {
        return hugoniotParams_.getDFMinus();

    }

    public double[] getPrimitiveUMinus() {
        return hugoniotParams_.getUMinus().toDouble();

    }

    public RpSolution calc() throws RpException {
        //		System.out.println("DEBUG - will do the approximation");
        // the XZero shift will be the reference point for now...

//        wave.util.RealVector initialPoint = getUMinus();//rpnumerics.RPNUMERICS.hugoniotCurveCalc().getUMinus().getCoords();
//        for (int i = 0; i < initialPoint.getSize(); i++) {
//            initialPoint.setElement(i, initialPoint.getElement(i) + UMINUS_SHIFT);
//
//        }

        HugoniotCurve result= (HugoniotCurve) calc(getUMinus());

        //** acrescentei isso (Leandro)

        RPnCurve.lista.add(result);
        System.out.println("Tamanho da lista: " + RPnCurve.lista.size());
        //***

        System.out.println("Tamanho de result: " + result.segments().size());
        return result;

    }

    public RpSolution recalc() throws RpException {

        return calc();
    }

    private native RpSolution calc(PhasePoint initialpoint) throws RpException;
}
