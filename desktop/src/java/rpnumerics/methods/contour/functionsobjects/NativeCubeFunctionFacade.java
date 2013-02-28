/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */


package rpnumerics.methods.contour.functionsobjects;

import wave.util.PointNDimension;


public class NativeCubeFunctionFacade {




public NativeCubeFunctionFacade (String libName){


//  System.load(libName);

   System.loadLibrary(libName);


}

private native double nativeFunction(double Tminus ,double Sminus,double Tplus,double Splus);
private native double nativeFunction1(double Tminus ,double Sminus,double Tplus,double Splus);
private native double nativeFunction2(double Tminus ,double Sminus,double Tplus,double Splus);


public double nativeFunction(PointNDimension point){

  try {
    double x = 0;
    double y = 0;
    double z = 0;
    double w = 0;

    x = point.getCoordinate(1);
    y = point.getCoordinate(2);
    z = point.getCoordinate(3);
    w = point.getCoordinate(4);

    return nativeFunction(x, y, z, w);
  }
  catch (Exception ex) {
    System.out.println ("Error in NativeCubeFunction.nativeFunction()"+ex);
    return 0;
  }


}


public double nativeFunction1(PointNDimension point){

  try {
    double x = 0;
    double y = 0;
    double z = 0;
    double w = 0;

    x = point.getCoordinate(1);
    y = point.getCoordinate(2);
    z = point.getCoordinate(3);
    w = point.getCoordinate(4);

    return nativeFunction1(x, y, z, w);
  }
  catch (Exception ex) {
    System.out.println ("Error in NativeCubeFunction.nativeFunction()"+ex);
    return 0;
  }


}


public double nativeFunction2(PointNDimension point){

  try {
    double x = 0;
    double y = 0;
    double z = 0;
    double w = 0;

    x = point.getCoordinate(1);
    y = point.getCoordinate(2);
    z = point.getCoordinate(3);
    w = point.getCoordinate(4);

    return nativeFunction2(x, y, z, w);
  }
  catch (Exception ex) {
    System.out.println ("Error in NativeCubeFunction.nativeFunction()"+ex);
    return 0;
  }


}



}
