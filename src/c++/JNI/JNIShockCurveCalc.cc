/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) JNIShockCurveCalc.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "rpnumerics_ShockCurveCalc.h"
#include "ShockContinuationMethod.h"
#include "LSODESolver.h"
#include "LSODEProfile.h"

#include "ContinuationShockFlow.h"
#include "ShockContinuationMethod3D2D.h"


#include "ColorCurve.h"


#include "RpNumerics.h"
#include "RealVector.h"
#include "JNIDefs.h"
#include <vector>


using std::vector;

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


JNIEXPORT jobject JNICALL Java_rpnumerics_ShockCurveCalc_calc(JNIEnv * env, jobject obj, jstring methodName, jstring flowName, jobject initialPoint, jint familyIndex, jint timeDirection) {

    unsigned int i;

    jclass classOrbitPoint = (env)->FindClass(ORBITPOINT_LOCATION);
    jclass realVectorClass = env->FindClass(REALVECTOR_LOCATION);
    jclass hugoniotSegmentClass = (env)->FindClass(HUGONIOTSEGMENTCLASS_LOCATION);
    jclass arrayListClass = env->FindClass("java/util/ArrayList");
    jclass hugoniotCurveClass = env->FindClass(HUGONIOTCURVE_LOCATION);
    jclass phasePointClass = (env)->FindClass(PHASEPOINT_LOCATION);

    jmethodID realVectorConstructorDoubleArray = env->GetMethodID(realVectorClass, "<init>", "([D)V");
    jmethodID toDoubleMethodID = (env)->GetMethodID(classOrbitPoint, "toDouble", "()[D");
    jmethodID hugoniotSegmentConstructor = (env)->GetMethodID(hugoniotSegmentClass, "<init>", "(Lwave/util/RealVector;DLwave/util/RealVector;DI)V");
    jmethodID arrayListConstructor = env->GetMethodID(arrayListClass, "<init>", "()V");
    jmethodID arrayListAddMethod = env->GetMethodID(arrayListClass, "add", "(Ljava/lang/Object;)Z");
    jmethodID hugoniotCurveConstructor = env->GetMethodID(hugoniotCurveClass, "<init>", "(Lrpnumerics/PhasePoint;Ljava/util/List;)V");
    jmethodID phasePointConstructor = (env)->GetMethodID(phasePointClass, "<init>", "(Lwave/util/RealVector;)V");

    //Input processing
    jdoubleArray inputPhasePointArray = (jdoubleArray) (env)->CallObjectMethod(initialPoint, toDoubleMethodID);

    double input [env->GetArrayLength(inputPhasePointArray)];

    env->GetDoubleArrayRegion(inputPhasePointArray, 0, env->GetArrayLength(inputPhasePointArray), input);

    RealVector realVectorInput(env->GetArrayLength(inputPhasePointArray));

    for (i = 0; i < (unsigned int) realVectorInput.size(); i++) {
        realVectorInput.component(i) = input[i];

    }

    env->DeleteLocalRef(inputPhasePointArray);

    int dimension = realVectorInput.size();


    vector <RealVector> coords;

    //    double Ur [dimension];
    //    Ur[0]=0.47;
    //    Ur[1] = 0.182590;
    //    Ur[2] = 1.0;

    double tol = 10e-10;
    double epsilon = 10e-4;
    int t = 11;

    ShockContinuationMethod3D2D method(dimension, familyIndex, RpNumerics::getPhysics().fluxFunction(), RpNumerics::getPhysics().accumulation(), RpNumerics::getPhysics().boundary(), input, tol, epsilon, t);

//    int edge;

    method.curve(realVectorInput, timeDirection, coords);

    //Classify

    const vector < RealVector> tempCurve = coords;

    std::vector<RealVector> out_color;

    ColorCurve::preprocess_data(tempCurve, realVectorInput, 2, RpNumerics::getPhysics().fluxFunction(), RpNumerics::getPhysics().accumulation(), 11, out_color);


    std::vector<HugoniotPolyLine> classified;
    ColorCurve::classify_segments(out_color, classified);
    jobject segmentsArray = env->NewObject(arrayListClass, arrayListConstructor, NULL);

    for (i = 0; i < classified.size(); i++) {

        HugoniotPolyLine tempPoly = classified.at(i);

        vector <RealVector> tempVec = tempPoly.vec;

        int pointType;


        jobject realVectorLeftPoint;

        jobject realVectorRightPoint;


        for (unsigned int j = 0; j < classified[i].vec.size() - 1; j++) {
            int m = (classified[i].vec[0].size() - dimension - 1) / 2; // Number of valid eigenvalues

            jdoubleArray eigenValRLeft = env->NewDoubleArray(dimension);
            jdoubleArray eigenValRRight = env->NewDoubleArray(dimension);


            double * leftCoords = (double *) classified[i].vec[j];
            double * rightCoords = (double *) classified[i].vec[j + 1];


            env->SetDoubleArrayRegion(eigenValRLeft, 0, dimension, leftCoords);
            env->SetDoubleArrayRegion(eigenValRRight, 0, dimension, rightCoords);


            //Construindo left e right points
            realVectorLeftPoint = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, eigenValRLeft);
            realVectorRightPoint = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, eigenValRRight);

            pointType = classified[i].type;

            double leftSigma = classified[i].vec[j].component(dimension + m);
            double rightSigma = classified[i].vec[j + 1].component(dimension + m);
            //            cout << "type of " << j << " = " << classified[i].type << endl;
            //            cout << "speed of " << j << " = " << classified[i].vec[j].component(dimension + m) << endl;
            //            cout << "speed of " << j + 1 << " = " << classified[i].vec[j + 1].component(dimension + m) << endl;

            jobject hugoniotSegment = env->NewObject(hugoniotSegmentClass, hugoniotSegmentConstructor, realVectorLeftPoint, leftSigma, realVectorRightPoint, rightSigma, pointType);
            env->CallObjectMethod(segmentsArray, arrayListAddMethod, hugoniotSegment);

        }


    }

    if (coords.size() == 0) {
        return NULL;

    }

    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //Orbit memebers creation

    //    //Building the orbit



    jdoubleArray xzeroArray = env->NewDoubleArray(dimension);
    env->SetDoubleArrayRegion(xzeroArray, 0, dimension, input);
    jobject realVectorxzero = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, xzeroArray);

    jobject phasePointxzero = env->NewObject(phasePointClass, phasePointConstructor, realVectorxzero);

    jobject result = env->NewObject(hugoniotCurveClass, hugoniotCurveConstructor, phasePointxzero, segmentsArray);

    //Cleaning up


    coords.clear();
    classified.clear();
    //    env->DeleteLocalRef(orbitPointArray);
    //    env->DeleteLocalRef(classOrbitPoint);
    //    env->DeleteLocalRef(classShockCurve);

    //    return shockCurve;

    return result;


}
