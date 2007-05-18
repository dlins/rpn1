#include <jni.h>
#include <math.h>
#include <stdio.h>
#include <stdlib.h>
#include "rpnumerics_NativeHugoniotFacade.h"
#include "mcd.h"


#define F77_SIZE 2 /* Define the Fortran 77's array return dimension */

/*- Signature of Flux Functions for C and Fortran --*/

#ifdef F_CODE

/*This functions is for Fortran 77 only */

extern void value_(double *,int *,double *,int *,double *,double * ); /*(Input array , lenght of input array, output array,lenght of output array,fluxparams array) */

extern void derive_(double *,int *,double *,int *,double *,double *);

#else

/*This functions returns a vector allocated with the results */

double * value (double *,int , int *,double *,double *); /* (Input array , lenght of input array, lenght of output array,parameters array) */

double * derive(double *,int , int *,double *,double *);


#endif

jdoubleArray HugoniotNative (JNIEnv *env , jobject obj , jdoubleArray jda,jdoubleArray phase,jdoubleArray fluxParams,int n){

  jint i,sizeIn, sizePhasePoint,sizeFluxParams, sizeOut=0; 
  
  jdouble *in, *out, *phasePoint,*fParams; /* Pointer to C arrays */
  
  jdoubleArray darray; /* The returned Java primitive array */
  
  sizeIn=(*env)->GetArrayLength(env,jda); /* Catch the Java primitive array size */

  sizePhasePoint=(*env)->GetArrayLength(env,phase); /* Catch the Java primitive array size */

  sizeFluxParams=(*env)->GetArrayLength(env,fluxParams); /* Catch the Java primitive array size */

  in=(double *) calloc(sizeIn,sizeof(double)); /* Alocates space to the buffer array */

  phasePoint=(double *) calloc(sizePhasePoint,sizeof(double)); /* Alocates space to the buffer array */

  fParams=(double *) calloc(sizeFluxParams,sizeof(double)); /* Alocates space to the buffer array */

  if ((in == NULL)||(phasePoint == NULL )||(fParams==NULL)){

    printf ("in ou phasePoint ou fParams sao nulos em HugoniotNative\n");

    
    return;
    
  }
 
  (*env)->GetDoubleArrayRegion(env,jda,0,sizeIn,in); /* Copy the java primitive array passed as a parameter to the C buffer array */
  
  (*env)->GetDoubleArrayRegion(env,phase,0,sizePhasePoint,phasePoint); /* Copy the java primitive array passed as a parameter to the C buffer array */
  
  (*env)->GetDoubleArrayRegion(env,fluxParams,0,sizeFluxParams,fParams); /* Copy the java primitive array passed as a parameter to the C buffer array */

  
#ifdef F_CODE 

  out = (double *) calloc (pow(F77_SIZE,n),sizeof(double));

  switch (n){

  case 1:

  value_(in,&sizeIn,out,&sizeOut,phasePoint,fluxParams); /* Calls the Flux Function */

  break;

  case 2:

  derive_(in,&sizeIn,out,&sizeOut,phasePoint,fluxParams); /* Calls the Flux Function */

  break;

  }
  
#else

  switch (n){

  case 1:

  out=value(in,sizeIn,&sizeOut,phasePoint,fParams); /* Calls the Flux Function */

  break;

  case 2:

  out=derive(in,sizeIn,&sizeOut,phasePoint,fParams); /* Calls the Flux Function */

  break;

  }

  if (sizeOut==0){

    printf ("sizeOut nao foi modificado em JNIHugoniot\n");

    return;
  }

#endif

  darray = (*env)->NewDoubleArray(env,sizeOut);/* Creates a new Java primitive array */
  
  (*env)->SetDoubleArrayRegion(env,darray,0,sizeOut,out); /* Copy the Flux Function returned array to the Java 
							   *primitive array */
  free(in);            /* Free resources */

  free(out);

  free(phasePoint);

  free(fParams);

#ifdef _MCD_CHECK
  
  showMemStats();
  
#endif
  
  return darray;
  
}


JNIEXPORT jdoubleArray JNICALL 

Java_rpnumerics_NativeHugoniotFacade_valueNative (JNIEnv *env , jobject obj, jdoubleArray jda,jdoubleArray phasePoint,jdoubleArray fluxParams) {

  return  HugoniotNative(env ,obj,jda,phasePoint,fluxParams,1);

}

JNIEXPORT jdoubleArray JNICALL 

Java_rpnumerics_NativeHugoniotFacade_derivNative (JNIEnv *env , jobject obj, jdoubleArray jda,jdoubleArray phasePoint,jdoubleArray fluxParams) {

  return  HugoniotNative(env , obj, jda,phasePoint,fluxParams,2);

}


