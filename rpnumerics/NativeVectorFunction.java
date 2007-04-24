/* Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpnumerics;

import wave.util.RealVector;
import wave.util.RealMatrix2;
import wave.util.VectorFunction;




public class NativeVectorFunction implements VectorFunction {


  private NativeVectorFunctionFacade facade_;


  public NativeVectorFunction (NativeVectorFunctionFacade fac){

    facade_=fac;

  }

  public RealVector value(RealVector d){

      return	facade_.value(d,rpnumerics.RPNUMERICS.hugoniotCurveCalc().getUMinus());

  }

  public RealMatrix2 deriv(RealVector d){

    try {

      return facade_.deriv(d,rpnumerics.RPNUMERICS.hugoniotCurveCalc().getUMinus());

    }

    catch (RpException ex){

      System.out.println(ex);

      return null;

    }

  }
}





