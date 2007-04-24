package rpnumerics.physics;

import wave.util.RealVector;
import wave.util.RealMatrix2;
import wave.util.HessianMatrix;

public class NativeAccumulationFunctionFacade{


    private native double [] hNative(double [] d ,double [] param);

    private native double [] dhNative (double [] d,double [] param);

    private native double [] d2hNative (double [] d,double [] param);

    private String libName_;


    public NativeAccumulationFunctionFacade (String libName){
	System.loadLibrary(libName);
	libName_=libName;
    }


    public RealVector h (RealVector d,RealVector param){
	RealVector returned = new RealVector(hNative(d.toDouble(),param.toDouble()));
 	return returned;
     }

    public  RealMatrix2 dh (RealVector d,RealVector param){

 	double dataOut[];
	RealMatrix2 returned;
	dataOut=dhNative(d.toDouble(),param.toDouble());
 	returned = new RealMatrix2((int)(Math.sqrt((double)dataOut.length)),(int)(Math.sqrt((double)dataOut.length)),dataOut);
	return returned;
    }

    public  HessianMatrix d2h (RealVector d,RealVector param){

	double dataOut[];
	HessianMatrix returned;
	dataOut=d2hNative(d.toDouble(),param.toDouble());
        returned = new HessianMatrix (dataOut);
        return returned;

    }

    public String getLibName(){

	return libName_;
    }

}


