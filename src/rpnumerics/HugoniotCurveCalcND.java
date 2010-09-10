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
//    private RealVector Uminus_;
    private HugoniotParams hugoniotParams_;


    //
    // Constructors
    //
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

        return new PhasePoint(hugoniotParams_.getUMinus());
//        return new PhasePoint(Uminus_);
    }

    public RealVector getFMinus() {
        return new PhasePoint(hugoniotParams_.getFMinus());
//        return Fminus_;
    }

    public RealMatrix2 getDFMinus() {
        return hugoniotParams_.getDFMinus();
//        return DFminus_;
    }

    public double[] getPrimitiveUMinus() {
        return hugoniotParams_.getUMinus().toDouble();
//        return Uminus_.toDouble();
    }

    public RpSolution calc() {
        //		System.out.println("DEBUG - will do the approximation");
        // the XZero shift will be the reference point for now...

        wave.util.RealVector initialPoint = getUMinus();//rpnumerics.RPNUMERICS.hugoniotCurveCalc().getUMinus().getCoords();
        for (int i = 0; i < initialPoint.getSize(); i++) {
            initialPoint.setElement(i, initialPoint.getElement(i) + UMINUS_SHIFT);

        }

        return hugoniotMethod_.curve(initialPoint);

    }

    public RpSolution recalc() {
//        System.out.println("Chamando recalc");
        return calc();
    }

    
}
