#include <jni.h>
#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#include "funsteam.cc"

//#include "funcoesoil.cc"


JNIEXPORT jdouble JNICALL Java_rpnumerics_methods_contour_NativeCubeFunctionFacade_nativeFunction (JNIEnv *env , jobject obj, jdouble Tmenos, jdouble  Sgmenos, jdouble Tmais, jdouble  Sgmais){

  double x = Tmenos;
  double y= Sgmenos;
  double z= Tmais;
  double w=Sgmais;

//  return RHLthree1(273,400,x,y,z,w);

 return x*x + y*y + z*z + w*w - 2;

// // return x*x + y*y + z*z + w*w - 2;

//  return   ( Hugoniotimpl( Tmenos,Sgmenos ,  Tmais, Sgmais));


}



JNIEXPORT jdouble JNICALL Java_rpnumerics_methods_contour_NativeCubeFunctionFacade_nativeFunction1 (JNIEnv *env, jobject obj , jdouble Tmenos, jdouble Sgmenos, jdouble Tmais, jdouble Sgmais){

  double x = Tmenos;
  double y = Sgmenos;
  double z = Tmais;
  double w =Sgmais;

//return  RHLthree2(273,400,x,y,z,w);

  return x-y;

// return   dHugoniotimpldSg( Tmenos,Sgmenos, Tmais, Sgmais);

}

JNIEXPORT jdouble JNICALL Java_rpnumerics_methods_contour_NativeCubeFunctionFacade_nativeFunction2 (JNIEnv *env ,  jobject obj , jdouble Tmenos, jdouble Sgmenos, jdouble Tmais, jdouble Sgmais){

  double x = Tmenos;
  double y = Sgmenos;
  double z = Tmais;
  double w =Sgmais;

  return z-w;

/*   return z+w; */

//return dHugoniotimpldT (Tmenos,Sgmenos, Tmais, Sgmais);

}

