/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpnumerics.methods.contour.functionsobjects;

import wave.util.RealVector;
import wave.util.RealMatrix2;
import rpnumerics.physics.FluxFunction;
import rpnumerics.RPNUMERICS;
import rpnumerics.physics.AccumulationFunction;
import rpnumerics.HugoniotParams;


public class HugoniotCubeFunction extends CubeFunction  {


  private RealVector  differenceF;
  private RealVector  differenceH;


  public HugoniotCubeFunction(FluxFunction fluxFunction,HugoniotParams params){
    super(fluxFunction,params);

  }


  public RealVector value(RealVector U) {


    RealVector differenceF,differenceH;

    differenceF = RPNUMERICS.fluxFunction().F(U);
    differenceH= RPNUMERICS.accumulationFunction().H(U);

    differenceF.sub(RPNUMERICS.hugoniotCurveCalc().getFMinus());

    differenceH.sub(RPNUMERICS.accumulationFunction().H(RPNUMERICS.hugoniotCurveCalc().getUMinus().getCoords()));

    RealVector result= new RealVector (RPNUMERICS.domainDim()-1);

    for (int i=0;i <  (RPNUMERICS.domainDim()-1);i++){

      result.setElement(i,differenceF.getElement(0)*differenceH.getElement(i+1)-differenceF.getElement(i+1)*differenceH.getElement(0));

    }

    return result;

  }

  public RealMatrix2 deriv(RealVector U) {

    return new RealMatrix2(U.getSize(),U.getSize());

  }




}
