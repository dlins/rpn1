/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpnumerics;

import rpnumerics.methods.HugoniotMethod;
import rpnumerics.methods.HugoniotContinuationMethod;
import rpnumerics.methods.HugoniotContourMethod;
import rpnumerics.methods.HugoniotContinuationParams;
import rpnumerics.methods.HugoniotContourParams;
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
  private RealVector Fminus_;
  private RealMatrix2 DFminus_;



  //
  // Constructors
  //

  public HugoniotCurveCalcND(HugoniotContinuationMethod hugoniotMethod) {

    hugoniotMethod_ = hugoniotMethod;

    HugoniotContinuationParams params = hugoniotMethod.getParams();
    f_ = params.getFunction();



  }

  public HugoniotCurveCalcND(HugoniotContourMethod hugoniotMethod) {

    hugoniotMethod_ = hugoniotMethod;

    /**
     * @todo So para rodar sem erro
     */

//    f_ = new HugoniotCubeFunction(rpnumerics.RPNUMERICS.fluxFunction(),
//                                  new
//                                  HugoniotParams(new PhasePoint(new
//        RealVector(2))));

    HugoniotContourParams params = hugoniotMethod.getParams();



  }

  //
  // Accessors/Mutators
  //


  public void uMinusChangeNotify(PhasePoint uMinus) {

    setUMinus(uMinus);

  }

  public void setUMinus(PhasePoint pPoint) {
    Uminus_ = pPoint.getCoords();
    Fminus_ = rpnumerics.RPNUMERICS.fluxFunction().F(Uminus_);
    DFminus_ = rpnumerics.RPNUMERICS.fluxFunction().DF(Uminus_);
  }

  public PhasePoint getUMinus() { return new PhasePoint(Uminus_); }
  public RealVector getFMinus() { return Fminus_; }
  public RealMatrix2 getDFMinus() { return DFminus_; }
  public double[] getPrimitiveUMinus(){return Uminus_.toDouble();}

  public RpSolution calc() {
    //		System.out.println("DEBUG - will do the approximation");
    // the XZero shift will be the reference point for now...

    wave.util.RealVector initialPoint = rpnumerics.RPNUMERICS.hugoniotCurveCalc().getUMinus().getCoords();
    for (int i = 0; i < initialPoint.getSize(); i++) {
      initialPoint.setElement(i, initialPoint.getElement(i) + UMINUS_SHIFT);

    }
    return hugoniotMethod_.curve(initialPoint);

  }

  public RpSolution recalc() {
    return calc();
  }
}
