#include <jni.h>
#include <math.h>
#include <stdio.h>
#include <stdlib.h>
#include "rpnumerics_physics_NativeFluxFunctionFacade.h"
#include "FluxFunction.h"


        double static in[DIM];

        double static outBuffer[DIM];
        double static outBuffer2 [DIM][DIM];
        double static outBuffer3 [DIM][DIM][DIM];

        double static stripBuffer2[DIM*DIM];
        double static stripBuffer3[DIM*DIM*DIM];


        static FluxFunction ** fluxFunctionArray= getFluxFunctionArray();


            JNIEXPORT void JNICALL

                    Java_rpnumerics_physics_NativeFluxFunctionFacade_fNative (JNIEnv *env , jobject obj, jdoubleArray output,jdoubleArray input, jdoubleArray jdaParam,jint index) {

                jint sizeParam;

                jdouble *param;

                sizeParam= env->GetArrayLength(jdaParam); /* Catch the Java primitive array size */

                param=(double *) calloc(sizeParam,sizeof(jdouble)); /* Alocates space to the parameters array */

                env->GetDoubleArrayRegion(input, 0, DIM, in);

                fluxFunctionArray[index]->F(outBuffer,in, param); /* Calls the Flux Function */

                env->SetDoubleArrayRegion(output,0,DIM,outBuffer); /* Copy the Flux Function returned array to the Java */

                free(param);

            }

            JNIEXPORT void JNICALL

                    Java_rpnumerics_physics_NativeFluxFunctionFacade_dfNative (JNIEnv *env , jobject obj, jdoubleArray output,jdoubleArray input, jdoubleArray jdaParam,jint index) {

                jdouble *param;
                jint sizeParam;

                sizeParam= env->GetArrayLength(jdaParam); /* Catch the Java primitive array size */

                param=(double *) calloc(sizeParam,sizeof(jdouble)); /* Alocates space to the parameters array */

                env->GetDoubleArrayRegion(input, 0, DIM, in);

                fluxFunctionArray[index]->DF(outBuffer2,in,param);

                int ij,i,j;
                for ( i =0; i < DIM; i++){
                    for (j =0; j < DIM; j++){
                        ij=i*DIM+j;
                        stripBuffer2[ij]=outBuffer2[i][j];
                    }
                }

                env->SetDoubleArrayRegion(output,0,DIM*DIM,stripBuffer2);  /* Copy the Flux Function returned array to the Java primitive array */

                free(param);
            }

            JNIEXPORT void JNICALL

                    Java_rpnumerics_physics_NativeFluxFunctionFacade_d2fNative (JNIEnv *env , jobject obj,jdoubleArray output, jdoubleArray input, jdoubleArray jdaParam,jint index) {


                jint sizeParam;

                jdouble *param;
                param=(double *) calloc(sizeParam,sizeof(jdouble)); /* Alocates space to the parameters array */

                env->GetDoubleArrayRegion(input, 0, DIM, in);
                fluxFunctionArray[index]->D2F(outBuffer3,in, param); /* Calls the Flux Function */

                int ijk,i,j,k;

                for ( i =0; i < DIM; i++){
                    for (j =0; j < DIM; j++){
                        for (k=0; k < DIM; k++){
                            ijk=(int)(pow (DIM,2)*i+j*DIM+k);
                            stripBuffer3[ijk]=outBuffer3[i][j][k];
                        }
                    }
                }
                env->SetDoubleArrayRegion(output,0,DIM*DIM*DIM,stripBuffer3); /* Copy the Flux Function returned array to the Java primitive array */

                free(param);

            }

