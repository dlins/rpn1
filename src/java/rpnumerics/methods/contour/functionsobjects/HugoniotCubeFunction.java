/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpnumerics.methods.contour.functionsobjects;

import rpnumerics.AccumulationFunction;
import wave.util.RealVector;
import wave.util.RealMatrix2;
import rpnumerics.FluxFunction;
import rpnumerics.RPNUMERICS;
import rpnumerics.HugoniotParams;
import rpnumerics.PhasePoint;


public class HugoniotCubeFunction extends CubeFunction  {

//
//  private RealVector  differenceF;
//  private RealVector  differenceH;
    private FluxFunction flux_;
    private AccumulationFunction accumulation_;

  public HugoniotCubeFunction(FluxFunction fluxFunction,AccumulationFunction accumulation,HugoniotParams params){
    super(fluxFunction,params);
    flux_=fluxFunction;
    accumulation_=accumulation;

  }


  public RealVector value(RealVector U) {

      PhasePoint uMinus = new PhasePoint(RPNUMERICS.createHugoniotCalc().getUMinus().getCoords());//edsonlan
      RealVector differenceF, differenceH;

      differenceF = flux_.F(U);
      differenceH = getAccumulation().H(U);

//    differenceF.sub(RPNUMERICS.hugoniotCurveCalc().getFMinus());
      differenceF.sub(uMinus);

      differenceH.sub(getAccumulation().H(uMinus));//edsonlan

      RealVector result = new RealVector(RPNUMERICS.domainDim() - 1);

      for (int i = 0; i < (RPNUMERICS.domainDim() - 1); i++) {

          result.setElement(i, differenceF.getElement(0) * differenceH.getElement(i + 1) - differenceF.getElement(i + 1) * differenceH.getElement(0));

      }

      return result;

    }

  public RealMatrix2 deriv(RealVector U) {

    return new RealMatrix2(U.getSize(),U.getSize());

  }

    public AccumulationFunction getAccumulation() {
        return accumulation_;
    }


}
