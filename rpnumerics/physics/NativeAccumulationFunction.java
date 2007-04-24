package rpnumerics.physics;

import wave.util.RealVector;
import wave.util.RealMatrix2;
import wave.util.HessianMatrix;




public class NativeAccumulationFunction implements AccumulationFunction {

private NativeAccumulationFunctionFacade facade_;
  private AccumulationParams params_;


    public NativeAccumulationFunction (NativeAccumulationFunctionFacade fac, AccumulationParams param){

	facade_=fac;
	params_=param;
    }


    public RealVector H (RealVector d){

	return facade_.h(d,params_.getParams());
    }


    public RealMatrix2 DH (RealVector d){

	return facade_.dh(d,params_.getParams());
    }


    public HessianMatrix D2H (RealVector d){

	return facade_.d2h(d,params_.getParams());
    }


    public AccumulationParams accumulationParams(){

	return params_;
    }
}
