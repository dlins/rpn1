package rpnumerics;

import rpnumerics.physics.Physics;
import wave.util.HessianMatrix;
import wave.util.RealVector;
import wave.util.RealMatrix2;
import rpnumerics.RPNUMERICS;


public class NativeShockFlowFacade {

    private native double [] flowNative (double [] d,double [] phasePoint,double sigma,double [] fluxParams );

    private native double [] flowDerivNative (double [] d,double [] phasePoint,double sigma,double [] fluxParams );

    private native double [] flowDeriv2Native (double [] d,double [] phasePoint,double sigma,double [] fluxParams );

    private String libName_;

    private double [] fluxParams_;


    public NativeShockFlowFacade (String libName){

	System.loadLibrary(libName);

	libName_=libName;

	fluxParams_= RPNUMERICS.fluxFunction().fluxParams().getParams().toDouble();


    }


    public RealVector flux ( RealVector d,RealVector phasePoint,double sigma){

	RealVector returned = new RealVector (flowNative (d.toDouble(),phasePoint.toDouble(),sigma,fluxParams_));

 	return returned;

    }


    public RealMatrix2 fluxDeriv ( RealVector d,RealVector phasePoint,double sigma){

	RealMatrix2 returned = new RealMatrix2(RPNUMERICS.domainDim(),RPNUMERICS.domainDim(),flowDerivNative (d.toDouble(),phasePoint.toDouble(),sigma,fluxParams_));

 	return returned;

    }

    public  HessianMatrix fluxDeriv2 ( RealVector d,RealVector phasePoint,double sigma){

	HessianMatrix returned = new HessianMatrix(flowDeriv2Native (d.toDouble(),phasePoint.toDouble(),sigma,fluxParams_));

 	return returned;

    }


}
