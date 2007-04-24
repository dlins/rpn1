/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpnumerics.methods.contour.samplefunctions;

import wave.util.PointNDimension;
import wave.util.RealVector;
import wave.util.RealMatrix2;
import rpnumerics.methods.contour.functionsobjects.CubeFunction;
import rpnumerics.methods.contour.functionsobjects.NativeCubeFunctionFacade;
import rpnumerics.physics.FluxFunction;
import rpnumerics.HugoniotParams;

public class NativeCubeFunction2 extends CubeFunction {

NativeCubeFunctionFacade facade_;

//    public NativeCubeFunction2 (NativeCubeFunctionFacade facade){
//
//      facade_=facade;
//
//    }

//    public NativeCubeFunction2 (NativeCubeFunctionFacade facade){
//
//      super(numberOfComponents);
//
//      facade_=facade;
//
//    }


  public NativeCubeFunction2 (NativeCubeFunctionFacade facade,FluxFunction fluxfunction,HugoniotParams params){

      super(fluxfunction,params);

      facade_=facade;

    }



    public double function (PointNDimension point){

      return facade_.nativeFunction2(point);

    }




    public RealVector value (RealVector v){
      return v;
    }


    public RealMatrix2 deriv (RealVector v){
      return new RealMatrix2(3,3);
    }
  }


