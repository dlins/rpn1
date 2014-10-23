/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpnumerics.methods.contour.samplefunctions;

import wave.util.*;
import rpnumerics.methods.contour.functionsobjects.CubeFunction;
import rpnumerics.methods.contour.functionsobjects.NativeCubeFunctionFacade;
import rpnumerics.FluxFunction;
import rpnumerics.HugoniotParams;

public class NativeCubeFunction extends CubeFunction {

NativeCubeFunctionFacade facade_;

//    public NativeCubeFunction (NativeCubeFunctionFacade facade){
//
//      facade_=facade;
//
//    }

    public NativeCubeFunction (NativeCubeFunctionFacade facade,FluxFunction fluxfunction,HugoniotParams params){

      super(fluxfunction,params);

      facade_=facade;

    }

    public double function (PointNDimension point){

      return facade_.nativeFunction(point);

    }


    public RealVector value (RealVector v){
      return v;
    }


    public RealMatrix2 deriv (RealVector v){
      return new RealMatrix2(3,3);
    }
  }

