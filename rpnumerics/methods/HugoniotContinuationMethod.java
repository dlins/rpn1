/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpnumerics.methods;

import rpnumerics.methods.continuation.HugoniotContinuation;
import wave.util.RealVector;
import rpnumerics.HugoniotCurve;


public class HugoniotContinuationMethod extends HugoniotMethod {


  HugoniotContinuationParams params_;

  HugoniotContinuation continuation_;


public  HugoniotContinuationMethod (HugoniotContinuationParams params){

  params_=params;

  continuation_= new HugoniotContinuation(params_.getFunction());

}


public HugoniotCurve curve(RealVector initialPoint){

  return continuation_.curve(initialPoint);


}


public HugoniotContinuationParams getParams(){return params_;}

}
