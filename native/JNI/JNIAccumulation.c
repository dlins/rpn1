#include <jni.h>
#include <math.h>
#include <stdio.h>
#include <stdlib.h>
#include "rpnumerics_physics_NativeAccumulationFunctionFacade.h"
#include "mcd.h"

#define F77_SIZE 4 /* Define the Fortran 77's array return dimension */


/*- Signature of Flux Functions for C and Fortran --*/

#ifdef F_CODE

/*This functions is for Fortran 77 only */

/*(Input array , lenght of input array, lenght of output array, parameters array) */
extern void  h_(double *,int *,int *,double * );

extern void dh_(double *,int *,double *,int *,double * );

extern void d2h_(double *,int *,double *,int *,double * );


#else

/*This functions returns a vector allocated with the results */

double * h (double *,int , int *,double *); /* (Input array , lenght of input array, lenght of output array, parameters array) */

double * dh(double *,int , int *,double *);

double * d2h (double *,int, int *,double *);

#endif

jdoubleArray AccumulationNative (JNIEnv *env , jobject obj , jdoubleArray jda, jdoubleArray jdaParam,int n){

  jint i,sizeIn,sizeParam, sizeOut=0;

  jdouble *in, *out,*param; /* Pointer to C arrays */

  jdoubleArray darray; /* The returned Java primitive array */

  sizeIn=(*env)->GetArrayLength(env,jda); /* Catch the Java primitive array size */

  sizeParam=(*env)->GetArrayLength(env,jdaParam); /* Catch the Java primitive array size */

  in=(double *) calloc(sizeIn,sizeof(double)); /* Alocates space to the buffer array */

  param=(double *) calloc(sizeParam,sizeof(double)); /* Alocates space to the buffer array */

  if ((in == NULL)||(param==NULL)){

    return;

  }

  (*env)->GetDoubleArrayRegion(env,jda,0,sizeIn,in); /* Copy the java primitive array passed as a parameter to the C buffer array */

  (*env)->GetDoubleArrayRegion(env,jdaParam,0,sizeParam,param); /* Copy the java primitive array passed as a parameter to the C buffer array */


#ifdef F_CODE

  //  out = (double *) calloc (pow(F77_SIZE,n),sizeof(double));

  switch (n){

  case 1:

    // f_(in,&sizeIn,out,&sizeOut,param); /* Calls the Flux Function */


    out=  h_(in,&sizeIn,&sizeOut,param); /* Calls the Flux Function */


  break;

  case 2:

  dh_(in,&sizeIn,out,&sizeOut,param); /* Calls the Flux Function */

  break;

  case 3:

  d2h_(in,&sizeIn,out,&sizeOut,param); /* Calls the Flux Function */

  break;

  }


#else

  switch (n){

  case 1:

  out=h(in,sizeIn,&sizeOut,param); /* Calls the Flux Function */

  break;

  case 2:

  out=dh(in,sizeIn,&sizeOut,param); /* Calls the Flux Function */

  break;

  case 3:

  out=d2h(in,sizeIn,&sizeOut,param); /* Calls the Flux Function */

  break;

  }

  if (sizeOut==0){

    return;
  }

#endif

  darray = (*env)->NewDoubleArray(env,sizeOut);/* Creates a new Java primitive array */

  (*env)->SetDoubleArrayRegion(env,darray,0,sizeOut,out); /* Copy the Flux Function returned array to the Java
							   *primitive array */
  free(in);            /* Free resources */

  free(param);

  free(out);

#ifdef _MCD_CHECK

  showMemStats();

#endif

  return darray;

}


JNIEXPORT jdoubleArray JNICALL Java_rpnumerics_physics_NativeAccumulationFunctionFacade_hNative

 (JNIEnv *env , jobject obj, jdoubleArray jda, jdoubleArray jdaParam) {

  return  AccumulationNative(env ,obj,jda,jdaParam,1);

}

JNIEXPORT jdoubleArray JNICALL Java_rpnumerics_physics_NativeAccumulationFunctionFacade_dhNative

 (JNIEnv *env , jobject obj, jdoubleArray jda, jdoubleArray jdaParam) {

  return  AccumulationNative(env , obj, jda,jdaParam,2);

}

JNIEXPORT jdoubleArray JNICALL Java_rpnumerics_physics_NativeAccumulationFunctionFacade_d2hNative

(JNIEnv *env , jobject obj, jdoubleArray jda, jdoubleArray jdaParam) {

  return  AccumulationNative(env , obj, jda, jdaParam,3);

}
