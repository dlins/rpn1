/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) JNIHugoniotCurveCalc.cc
 **/


//! Definition of JNIHugoniotCurveCalc
/*!
	
TODO:
	
NOTE : 

@ingroup JNI
 */


#include "rpnumerics_InflectionCurveCalc.h"
#include "JNIDefs.h"
#include "RpNumerics.h"
#include <vector>
#include <iostream>
#include "ContourMethod.h"
#include "CoincidenceTPCW.h"
#include "ColorCurve.h"
#include "Inflection_Curve.h"
#include "StoneFluxFunction.h"
#include "StoneAccumulation.h"


using std::vector;
using namespace std;

JNIEXPORT jobject JNICALL Java_rpnumerics_InflectionCurveCalc_nativeCalc(JNIEnv * env, jobject obj, jint family) {

    jclass hugoniotSegmentClass = (env)->FindClass(HUGONIOTSEGMENTCLASS_LOCATION);

    jclass realVectorClass = env->FindClass(REALVECTOR_LOCATION);

    jclass arrayListClass = env->FindClass("java/util/ArrayList");

    jclass inflectionCurveClass = env->FindClass(INFLECTIONCURVE_LOCATION);

    jmethodID realVectorConstructorDoubleArray = env->GetMethodID(realVectorClass, "<init>", "([D)V");

    jmethodID hugoniotSegmentConstructor = (env)->GetMethodID(hugoniotSegmentClass, "<init>", "(Lwave/util/RealVector;DLwave/util/RealVector;DDDDDI)V");

    jmethodID arrayListConstructor = env->GetMethodID(arrayListClass, "<init>", "()V");
    jmethodID arrayListAddMethod = env->GetMethodID(arrayListClass, "add", "(Ljava/lang/Object;)Z");

    jmethodID inflectionCurveConstructor = env->GetMethodID(inflectionCurveClass, "<init>", "(ILjava/util/List;)V");


    jobject segmentsArray = env->NewObject(arrayListClass, arrayListConstructor, NULL);


    //    Test testFunction;

    //-------------------------------------------------------------------
    SubPhysics & subPhysics = RpNumerics::getPhysics().getSubPhysics(0);

    int dimension = subPhysics.domain().dim();

    const Boundary & boundary = subPhysics.boundary();

    int cells [2];

    cells[0] = 128;
    cells[1] = 128;

    Inflection_Curve inflectionCurve((FluxFunction*) RpNumerics::getPhysics().fluxFunction().clone(), (AccumulationFunction*) RpNumerics::getPhysics().accumulation().clone(),
            boundary.minimums(), boundary.maximums(), cells);

    std::vector<RealVector> left_vrs;

    inflectionCurve.curve(family, left_vrs);
 

    int tamanho = left_vrs.size();


    cout << "Tamanho do vetor de pontos: " << tamanho << endl;
    cout << "Familia da inflexao: " << family << endl;


    for (int i = 0; i < left_vrs.size() / 2; i++) {

        jdoubleArray eigenValRLeft = env->NewDoubleArray(dimension);
        jdoubleArray eigenValRRight = env->NewDoubleArray(dimension);


        cout << "Ponto: " << 2*i << left_vrs[2 * i] << endl;
        cout << "Ponto: " << 2*i+1 << left_vrs[2 * i +1] << endl;

        double * leftCoords = (double *) left_vrs[2 * i];
        double * rightCoords = (double *) left_vrs[2 * i + 1];


        env->SetDoubleArrayRegion(eigenValRLeft, 0, dimension, leftCoords);
        env->SetDoubleArrayRegion(eigenValRRight, 0, dimension, rightCoords);


        //Construindo left e right points
        jobject realVectorLeftPoint = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, eigenValRLeft);
        jobject realVectorRightPoint = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, eigenValRRight);

        int pointType = 10;

        double leftSigma = 0;
        double rightSigma = 0;

        double leftLambda1 = 0;
        double leftLambda2 = 0;

        double rightLambda1 = 0;
        double rightLambda2 = 0;
        jobject hugoniotSegment = env->NewObject(hugoniotSegmentClass, hugoniotSegmentConstructor, realVectorLeftPoint, leftSigma, realVectorRightPoint, rightSigma, leftLambda1, leftLambda2, rightLambda1, rightLambda2, pointType);
        env->CallObjectMethod(segmentsArray, arrayListAddMethod, hugoniotSegment);
    }




    jobject result = env->NewObject(inflectionCurveClass, inflectionCurveConstructor, family, segmentsArray);

    // Limpando

    env->DeleteLocalRef(hugoniotSegmentClass);
    env->DeleteLocalRef(realVectorClass);
    env->DeleteLocalRef(arrayListClass);


    return result;


}
















