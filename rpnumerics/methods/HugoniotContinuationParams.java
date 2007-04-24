/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */


package rpnumerics.methods;

import wave.util.VectorFunction;


public class HugoniotContinuationParams {

  VectorFunction function_;


  public HugoniotContinuationParams(VectorFunction function) {
    function_=function;
  }

 public VectorFunction getFunction(){return function_;}


}
