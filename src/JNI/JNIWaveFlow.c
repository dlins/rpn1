#include <jni.h>
#include <math.h>
#include <stdio.h>
#include <stdlib.h>
#include "rpnumerics_NativeShockFlowFacade.h"


#define F77_SIZE 2 /* Define the Fortran 77's array return dimension */

#define DIM 2


/*- Signature of Flux Functions for C and Fortran --*/

        double static in[DIM];

        double static out [DIM];
        double static out2 [DIM][DIM];
        double static out3 [DIM][DIM][DIM];

#ifdef F_CODE

/*These functions is for Fortran 77 only */

void flux_(double *,int *,double *,int *,double *,double,double * ); /*(Input array , lenght of input array, output array,lenght of output array) */

void fderiv_(double *,int *,double *,int *,double *,double,double * );

void f2deriv_(double *,int *,double *,int *,double *,double,double * );

#else

/*These functions returns a vector allocated with the results */

void flowNative (double [], double [],double [],double, double [] ); /* (Input array , Output array, Phasepoint array,sigma, parameters array) */

void flowDerivNative (double [][], double [],double [],double, double [] );

void flowDeriv2Native (double [][][], double [],double [],double, double [] );

#endif

jdoubleArray FlowNative (JNIEnv *env , jobject obj , jdoubleArray jda,jdoubleArray phase,jdouble sigma,jdoubleArray fluxParams,int n){

  jint i,j,k,sizePhasePoint,sizeFluxParams;

  jdouble outJAVA2[DIM*DIM],outJAVA3[DIM*DIM*DIM], *phasePoint,*fParams; /* Pointer to C arrays */

  sizePhasePoint=(*env)->GetArrayLength(env,phase); /* Catch the Java primitive array size */

  sizeFluxParams=(*env)->GetArrayLength(env,fluxParams); /* Catch the Java primitive array size */

  phasePoint=(double *) calloc(sizePhasePoint,sizeof(double)); /* Alocates space to the phasepoint array */

  fParams=(double *) calloc(sizeFluxParams,sizeof(double)); /* Alocates space to the parameters array */

  if ((in == NULL)||(phasePoint == NULL )||(fParams==NULL)){

       printf ("in ou phasePoint ou fParams sao nulos em JNIWaveFlow\n");

    return;

  }

  (*env)->GetDoubleArrayRegion(env,jda,0,DIM,in); /* Copy the java primitive array passed as a parameter to the C buffer array */

  (*env)->GetDoubleArrayRegion(env,phase,0,sizePhasePoint,phasePoint); /* Copy the java primitive array passed as a parameter to the C buffer array */

  (*env)->GetDoubleArrayRegion(env,fluxParams,0,sizeFluxParams,fParams); /* Copy the java primitive array passed as a parameter to the C buffer array */

#ifdef F_CODE

  out = (double *) calloc (pow(F77_SIZE,n),sizeof(double));

  switch (n){

  case 1:

     flux_(in,&sizeIn,out,&sizeOut,phasePoint,sigma,fParams); /* Calls the Flux Function */

  break;

  case 2:

    fderiv_(in,&sizeIn,out,&sizeOut,phasePoint,sigma,fParams); /* Calls the Flux Function */

  break;

  case 3:

    f2deriv_(in,&sizeIn,out,&sizeOut,phasePoint,sigma,fParams); /* Calls the Flux Function */

  break;

  }

#else

  switch (n){

  case 1:
      flowNative(out,in,phasePoint,sigma,fParams); /* Calls the native flow Function */

      jdoubleArray darray = (*env)->NewDoubleArray(env,DIM);/* Creates a new Java primitive array */

      (*env)->SetDoubleArrayRegion(env,darray,0,DIM,out); /* Copy the Flow Function returned array to the Java */
      free (phasePoint);
      free(fParams);
      return darray;


  case 2:

      flowDerivNative(out2,in,phasePoint,sigma,fParams);/* Calls the native flow Function */

      int ij;
      for ( i =0; i < DIM; i++){
          for (j =0; j < DIM; j++){
              ij=i*DIM+j;
              outJAVA2[ij]=out2[i][j];
          }
      }

      jdoubleArray darray2 = (*env)->NewDoubleArray(env,DIM*DIM);/* Creates a new Java primitive array */

      (*env)->SetDoubleArrayRegion(env,darray2,0,DIM*DIM,outJAVA2); /* Copy the Flow Function returned array to the Java */
      free (phasePoint);
      free(fParams);
      return darray2;


    case 3:

        flowDeriv2Native(out3,in,phasePoint,sigma,fParams);/* Calls the native flow Function */

        int ijk;

        for ( i =0; i < DIM; i++){
            for (j =0; j < DIM; j++){
                for (k=0; j < DIM; k++){

                }
                ijk=pow (DIM,2)*i+j*DIM+k;
                outJAVA3[ijk]=out3[i][j][k];
            }
        }
        free (phasePoint);
        free(fParams);
        jdoubleArray darray3 = (*env)->NewDoubleArray(env,DIM*DIM*DIM);/* Creates a new Java primitive array */
        (*env)->SetDoubleArrayRegion(env,darray3,0,DIM*DIM*DIM,outJAVA3); /* Copy the Flow Function returned array to the Java */
        return darray3;


  }

#endif

}


JNIEXPORT jdoubleArray JNICALL

Java_rpnumerics_NativeShockFlowFacade_flowNative  (JNIEnv *env , jobject obj, jdoubleArray jda, jdoubleArray phasePoint,jdouble sigma,jdoubleArray fluxParams) {


  return  FlowNative (env , obj, jda,phasePoint,sigma,fluxParams,1);

}

JNIEXPORT jdoubleArray JNICALL

Java_rpnumerics_NativeShockFlowFacade_flowDerivNative  (JNIEnv *env , jobject obj, jdoubleArray jda, jdoubleArray phasePoint,jdouble sigma,jdoubleArray fluxParams) {

  return  FlowNative(env , obj, jda,phasePoint,sigma,fluxParams,2);

}

JNIEXPORT jdoubleArray JNICALL

Java_rpnumerics_NativeShockFlowFacade_flowDeriv2Native  (JNIEnv *env , jobject obj, jdoubleArray jda, jdoubleArray phasePoint,jdouble sigma,jdoubleArray fluxParams) {

  return  FlowNative(env , obj, jda,phasePoint,sigma,fluxParams,3);

}

