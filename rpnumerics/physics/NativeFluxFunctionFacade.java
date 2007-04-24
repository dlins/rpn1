package rpnumerics.physics;

import wave.util.RealVector;
import wave.util.RealMatrix2;
import wave.util.HessianMatrix;

public class NativeFluxFunctionFacade implements FluxFunction{

        /**@todo Pedir ao Carlos os arquivos do Contour que ele esta usando, para rodar e ver o erro acontecendo
         *
         */


    private native void fNative(double []out,double [] d ,double [] param,int index);

    private native void dfNative ( double[] out,double [] d, double [] param, int index);

    private native void d2fNative (double[] out, double [] d, double [] param, int index);

    private FluxParams params_;

    public NativeFluxFunctionFacade (FluxParams params){  params_=params;  }


    public RealVector F (RealVector d){


        int dimension = d.getSize();

        double out [] = new double[dimension];

        fNative(out,d.toDouble(),params_.getParams().toDouble(),params_.getIndex());

        RealVector returned = new RealVector(out);


        System.out.println("valor de f: "+ returned.toString());


        return returned;



     }

    public  RealMatrix2 DF (RealVector d){

        int dimension = (int)Math.pow(d.getSize(),2);

        double out []= new double[dimension];

        dfNative(out,d.toDouble(), params_.getParams().toDouble(), params_.getIndex());

        RealMatrix2 	returned = new RealMatrix2(d.getSize(),d.getSize(),out);

        System.out.println("Valor de df: "+ returned.toString());

	return returned;
    }

    public  HessianMatrix D2F (RealVector d){

	HessianMatrix returned;

        int dimension = (int) Math.pow(d.getSize(),3);

        double out [] = new double[dimension];

        d2fNative(out, d.toDouble(), params_.getParams().toDouble(), params_.getIndex());

	returned = new HessianMatrix (out);

	return returned;

    }



    public FluxParams fluxParams() {return params_; }


}


