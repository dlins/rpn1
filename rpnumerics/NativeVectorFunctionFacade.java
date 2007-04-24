/* Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpnumerics;

import rpnumerics.physics.Physics;
import wave.util.HessianMatrix;
import wave.util.RealVector;
import wave.util.RealMatrix2;


public class NativeVectorFunctionFacade {


    private native double [] valueNative (double [] input ,double[] phasePoint);

    private native double [] derivNative  (double [] input ,double [] phasePoint);

    private String libName_;



    public NativeVectorFunctionFacade (String libName){

	System.loadLibrary(libName);

	libName_=libName;

    }

    public RealVector value( RealVector d,PhasePoint pPoint){

      double dataOut[];

      double [] phasePoint = pPoint.getCoords().toDouble();


      dataOut=valueNative(d.toDouble(),phasePoint);


      if (dataOut==null)

        System.out.println("point is null");

       return new RealVector(dataOut);


    }

    public RealMatrix2 deriv( RealVector d,PhasePoint pPoint) throws  RpException{

	double dataOut[];

	RealMatrix2 returned;
        double [] phasePoint = pPoint.getCoords().toDouble();
	dataOut=derivNative(d.toDouble(),phasePoint);

	returned = new RealMatrix2(RPNUMERICS.domainDim()-1,RPNUMERICS.domainDim(),dataOut);

	if (returned.getNumRow() > (RPNUMERICS.domainDim()-1)){

	    throw new RpException("Wrong dimension in native calculation");
	}

	return returned;

    }


    public String getLibName(){

	return libName_;
    }

}
