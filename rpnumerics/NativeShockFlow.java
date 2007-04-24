package rpnumerics;

import rpnumerics.physics.*;
import wave.util.RealVector;
import wave.util.RealMatrix2;
import wave.util.HessianMatrix;



public class NativeShockFlow extends ShockFlow {


    private NativeShockFlowFacade facade_;


    public NativeShockFlow (NativeShockFlowFacade fac, ShockFlowParams fparam){
        super(fparam);
	facade_=fac;

    }

    public RealVector flux(RealVector d){
      RealVector ret;
      ret =facade_.flux(d,flowParams_.getCoords(),flowParams_.getSigma());
      return	ret;

    }

    public RealMatrix2 fluxDeriv(RealVector d){

	return facade_.fluxDeriv(d,flowParams_.getCoords(),flowParams_.getSigma());


    }

    public  HessianMatrix fluxDeriv2( RealVector d ){

	return facade_.fluxDeriv2(d,flowParams_.getCoords(),flowParams_.getSigma());


    }





}




