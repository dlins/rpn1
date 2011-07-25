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
#include <stdio.h>


#include "RpNumerics.h"
#include "RealVector.h"
#include "JNIDefs.h"
#include <vector>


using std::vector;

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


JNIEXPORT jobject JNICALL Java_rpnumerics_ShockCurveCalc_calc(JNIEnv * env, jobject obj, jstring methodName, jdouble newtonTolerance, jobject initialPoint, jint familyIndex, jint timeDirection) {

    unsigned int i;

    jclass rpnumericsClass = (env)->FindClass(RPNUMERICS_LOCATION);

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

    //---------------------------------------------------------------------------------------

    //  jmethodID getParamValueID = env->GetStaticMethodID(rpnumericsClass, "getParamValue", "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;");
    //    jmethodID getParamPhysicsValueID = env->GetStaticMethodID(rpnumericsClass, "getPhysicsParamValue", "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;");
    //    const char * paramName = "tolerance";
    //    const char * curveName = "Newton";
    //    jstring cName = env->NewStringUTF(curveName);
    //    jstring pName = env->NewStringUTF(paramName);
    //    jstring paramValue = (jstring) env->CallStaticObjectMethod(rpnumericsClass, getParamValueID, cName, pName);
    //    const char * nativeParamValue = env->GetStringUTFChars(paramValue, NULL);
    //    cout << "Valor do parametro em Java:" << nativeParamValue << endl;

    //    const char * curve2Name = "thermodynamics";
    //    const char * param2Name = "porosity";

    //    jstring c2Name = env->NewStringUTF(curve2Name);
    //    jstring p2Name = env->NewStringUTF(param2Name);
    //    jstring physicsParamValue = (jstring) env->CallStaticObjectMethod(rpnumericsClass, getParamPhysicsValueID, c2Name, p2Name);

    //    nativeParamValue = env->GetStringUTFChars(physicsParamValue, NULL);
    //    cout << "Valor do parametro em Java(Set RpnHome):" << nativeParamValue << endl;

    //---------------------------------------------------------------------------------------


    //Input processing
    jdoubleArray inputPhasePointArray = (jdoubleArray) (env)->CallObjectMethod(initialPoint, toDoubleMethodID);

    double input [env->GetArrayLength(inputPhasePointArray)];

    env->GetDoubleArrayRegion(inputPhasePointArray, 0, env->GetArrayLength(inputPhasePointArray), input);

    RealVector realVectorInput(env->GetArrayLength(inputPhasePointArray));


    SubPhysics & physics = RpNumerics::getPhysics().getSubPhysics(0);


    for (i = 0; i < (unsigned int) realVectorInput.size(); i++) {
        realVectorInput.component(i) = input[i];
    }


    physics.preProcess(realVectorInput);


    cout << "Ponto de entrada: " << realVectorInput << endl;


    for (i = 0; i < (unsigned int) realVectorInput.size(); i++) {

        input[i] = realVectorInput.component(i);

    }


    env->DeleteLocalRef(inputPhasePointArray);

    int dimension = realVectorInput.size();

    for (int i = 0; i < realVectorInput.size(); i++) {
        cout << "input " << i << input[i] << endl;


    }



    double tol = 1e-10;

    int t = 11;

    cout << "time antes:" << timeDirection << endl;
    if (timeDirection == 20)
        timeDirection = 1;
    else
        timeDirection = -1;


    const Boundary & physicsBoundary = RpNumerics::getPhysics().boundary();

    RealVector min(physicsBoundary. minimums());
    RealVector max(physicsBoundary. maximums());



    physics.preProcess(min);
    physics.preProcess(max);

    cout << "min: " << min << endl;
    cout << "max: " << max << endl;


    vector<bool> testBoundary;

    testBoundary.push_back(true);
    testBoundary.push_back(true);
    testBoundary.push_back(false);



    RectBoundary tempBoundary(min, max, testBoundary);

    //    double deltaxi = 1e-3;



    cout << "Valor timDirection" << timeDirection << endl;

    cout << "Valor de family" << familyIndex << endl;

    vector<HugoniotPolyLine> hugoniotPolyLineVector;
    //    ShockContinuationMethod3D2D method(dimension, familyIndex, RpNumerics::getPhysics().fluxFunction(), RpNumerics::getPhysics().accumulation(), RpNumerics::getPhysics().boundary(), input, tol, newtonTolerance, t);

    ShockContinuationMethod3D2D method(dimension, familyIndex, RpNumerics::getPhysics().fluxFunction(), RpNumerics::getPhysics().accumulation(), tempBoundary, input, tol, newtonTolerance, t);

    method.curve(realVectorInput, timeDirection, hugoniotPolyLineVector);


    cout << "Tamanho da curva" << hugoniotPolyLineVector.size() << endl;

    //Classify

    jobject segmentsArray = env->NewObject(arrayListClass, arrayListConstructor, NULL);
    //    cout << "Numero de hugo poly: " << hugoniotPolyLineVector.size() << endl;

    for (i = 0; i < hugoniotPolyLineVector.size(); i++) {

        physics.postProcess(hugoniotPolyLineVector[i].vec);

        for (unsigned int j = 0; j < hugoniotPolyLineVector[i].vec.size() - 1; j++) {

            int m = (hugoniotPolyLineVector[i].vec[0].size() - dimension - 1) / 2; // Number of valid eigenvalues

            jdoubleArray eigenValRLeft = env->NewDoubleArray(dimension);
            jdoubleArray eigenValRRight = env->NewDoubleArray(dimension);

            double * leftCoords = (double *) hugoniotPolyLineVector[i].vec[j];
            double * rightCoords = (double *) hugoniotPolyLineVector[i].vec[j + 1];

            env->SetDoubleArrayRegion(eigenValRLeft, 0, dimension, leftCoords);
            env->SetDoubleArrayRegion(eigenValRRight, 0, dimension, rightCoords);


            //Construindo left e right points
            jobject realVectorLeftPoint = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, eigenValRLeft);
            jobject realVectorRightPoint = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, eigenValRRight);

            int pointType = hugoniotPolyLineVector[i].type;

            double leftSigma = hugoniotPolyLineVector[i].vec[j].component(dimension + m);
            double rightSigma = hugoniotPolyLineVector[i].vec[j + 1].component(dimension + m);

            //            double leftSigma = 0;
            //            double rightSigma = 0;
            //


            jobject hugoniotSegment = env->NewObject(hugoniotSegmentClass, hugoniotSegmentConstructor, realVectorLeftPoint, leftSigma, realVectorRightPoint, rightSigma, pointType);
            env->CallObjectMethod(segmentsArray, arrayListAddMethod, hugoniotSegment);

        }


    }

    if (hugoniotPolyLineVector.size() == 0) {
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


    //    coords.clear();
    hugoniotPolyLineVector.clear();
    //    env->DeleteLocalRef(orbitPointArray);
    //    env->DeleteLocalRef(classOrbitPoint);
    //    env->DeleteLocalRef(classShockCurve);

    //    return shockCurve;

    return result;


}
