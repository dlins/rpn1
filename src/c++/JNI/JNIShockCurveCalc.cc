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




#include "ColorCurve.h"
#include <stdio.h>


#include "RpNumerics.h"
#include "RealVector.h"
#include "JNIDefs.h"
#include <vector>
#include "Rarefaction.h"
#include "HugoniotContinuation.h"
#include "Viscosity_Matrix.h"

using std::vector;

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


JNIEXPORT jobject JNICALL Java_rpnumerics_ShockCurveCalc_calc(JNIEnv * env, jobject obj, jstring methodName, jdouble newtonTolerance, jobject initialPoint, jint familyIndex, jint increase) {


    unsigned int i;



    jclass classWaveCurve = (env)->FindClass(WAVECURVE_LOCATION);
    jclass arrayListClass = env->FindClass("java/util/ArrayList");

    jclass classOrbitPoint = (env)->FindClass(ORBITPOINT_LOCATION);
    jclass shockCurveClass = (env)->FindClass(SHOCKCURVE_LOCATION);

    jmethodID waveCurveConstructor = (env)->GetMethodID(classWaveCurve, "<init>", "(II)V");
    jmethodID shockCurveConstructor = (env)->GetMethodID(shockCurveClass, "<init>", "([Lrpnumerics/OrbitPoint;II)V");
    jmethodID orbitPointConstructor = (env)->GetMethodID(classOrbitPoint, "<init>", "([DD)V");
    jmethodID toDoubleMethodID = (env)->GetMethodID(classOrbitPoint, "toDouble", "()[D");

    
    jmethodID waveCurveAddBranch = (env)->GetMethodID(classWaveCurve, "add", "(Lrpnumerics/WaveCurveBranch;)V");
    
    //Input processing
    jdoubleArray inputPhasePointArray = (jdoubleArray) (env)->CallObjectMethod(initialPoint, toDoubleMethodID);

    double input [env->GetArrayLength(inputPhasePointArray)];

    env->GetDoubleArrayRegion(inputPhasePointArray, 0, env->GetArrayLength(inputPhasePointArray), input);

    RealVector realVectorInput(env->GetArrayLength(inputPhasePointArray));


    for (i = 0; i < (unsigned int) realVectorInput.size(); i++) {

        realVectorInput.component(i) = input[i];

    }

    env->DeleteLocalRef(inputPhasePointArray);

    //    dimension;
    //

    vector <vector<RealVector> > allCoords, shock_alt;

    //    RealVector * originalDirection = new RealVector(realVectorInput.size());
    //
    //    originalDirection->component(0) = 0;
    //    originalDirection->component(1) = 0;


    if (increase == RAREFACTION_SPEED_INCREASE)
        increase = WAVE_FORWARD;

    if (increase == RAREFACTION_SPEED_DECREASE)
        increase = WAVE_BACKWARD;

    FluxFunction * fluxFunction = (FluxFunction *) & RpNumerics::getPhysics().getSubPhysics(0).fluxFunction();
    AccumulationFunction * accumulationFunction = (AccumulationFunction *) & RpNumerics::getPhysics().getSubPhysics(0).accumulation();

    Boundary * tempBoundary = (Boundary *) RpNumerics::getPhysics().getSubPhysics(0).getPreProcessedBoundary();

    int info_shock_curve, info_shock_curve_alt;

    int dim = realVectorInput.size();

    RealVector original_direction(dim);

    RpNumerics::getPhysics().getSubPhysics(0).preProcess(realVectorInput);

    HugoniotContinuation * shock = RpNumerics::getPhysics().getSubPhysics(0).getShockMethod();

    Viscosity_Matrix viscosityMatrix;

    ReferencePoint referencePoint(realVectorInput, fluxFunction, accumulationFunction, &viscosityMatrix);

    shock->set_reference_point(referencePoint);

    shock->curve(allCoords);

    cout << "Tamanho de coords: " << allCoords.size() << endl;
    ;

    //    shock->curveCalc(realVectorInput, true, realVectorInput, increase, familyIndex, SHOCK_FOR_ITSELF,
    //            &original_direction, 0,
    //            fluxFunction, accumulationFunction, tempBoundary,
    //            coords, info_shock_curve,
    //            shock_alt,
    //            info_shock_curve_alt, newtonTolerance);

    //Orbit members creation

    //    if (coords.size() == 0) return NULL;




    jobject waveCurveBranchForward = env->NewObject(classWaveCurve, waveCurveConstructor, familyIndex, increase);

    for (i = 0; i < allCoords.size(); i++) {

        jobjectArray orbitPointArray = (jobjectArray) (env)->NewObjectArray(allCoords[i].size(), classOrbitPoint, NULL);
        cout<<"Tamanho de coords: "<<i<<"  "<<allCoords[i].size()<<endl;
        for (int j = 0; j < allCoords[i].size(); j++) {

            RealVector tempVector = allCoords[i].at(j);

            double lambda = tempVector.component(tempVector.size() - 1);

            double * dataCoords = tempVector;

            jdoubleArray jTempArray = (env)->NewDoubleArray(tempVector.size());

            (env)->SetDoubleArrayRegion(jTempArray, 0, tempVector.size(), dataCoords);

            jobject orbitPoint = (env)->NewObject(classOrbitPoint, orbitPointConstructor, jTempArray, lambda);

            (env)->SetObjectArrayElement(orbitPointArray, j, orbitPoint);

            env->DeleteLocalRef(jTempArray);

            env->DeleteLocalRef(orbitPoint);

        }

        jobject shockCurve = (env)->NewObject(shockCurveClass, shockCurveConstructor, orbitPointArray, familyIndex, increase);

        env->CallVoidMethod(waveCurveBranchForward, waveCurveAddBranch, shockCurve);

    }

    //Building the orbit




    //Cleaning up

    //    coords.clear();

//    env->DeleteLocalRef(orbitPointArray);
    env->DeleteLocalRef(classOrbitPoint);
    env->DeleteLocalRef(shockCurveClass);

    return waveCurveBranchForward;



}
