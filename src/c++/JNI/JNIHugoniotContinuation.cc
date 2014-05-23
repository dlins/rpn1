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
//#include "rpnumerics_HugoniotContinuationCurveCalc.h"
//
//
//
//
//#include "ColorCurve.h"
//#include <stdio.h>
//
//
//#include "RpNumerics.h"
//#include "RealVector.h"
//#include "JNIDefs.h"
//#include <vector>
//#include "Rarefaction.h"
//#include "HugoniotContinuation.h"
//#include "Viscosity_Matrix.h"
//
//using std::vector;
//
///*
// * ---------------------------------------------------------------
// * Definitions:
// */
//
//JNIEXPORT jobject JNICALL Java_rpnumerics_HugoniotContinuationCurveCalc_calc
//(JNIEnv * env, jobject obj, jobject initialPoint, jint increase) {
//
//
//
//
//
//    //JNIEXPORT jobject JNICALL Java_rpnumerics_ShockCurveCalc_calc(JNIEnv * env, jobject obj, jobject initialPoint, jint subPhysicsIndex, jint familyIndex, jint increase) {
//
//
//    unsigned int i;
//
//
//
//    jclass classWaveCurve = (env)->FindClass(WAVECURVE_LOCATION);
//    jclass arrayListClass = env->FindClass("java/util/ArrayList");
//
//    jclass classOrbitPoint = (env)->FindClass(ORBITPOINT_LOCATION);
//    jclass shockCurveClass = (env)->FindClass(SHOCKCURVE_LOCATION);
//
//    jmethodID waveCurveConstructor = (env)->GetMethodID(classWaveCurve, "<init>", "(II)V");
//    jmethodID shockCurveConstructor = (env)->GetMethodID(shockCurveClass, "<init>", "([Lrpnumerics/OrbitPoint;II)V");
//    jmethodID orbitPointConstructor = (env)->GetMethodID(classOrbitPoint, "<init>", "([DD)V");
//    jmethodID toDoubleMethodID = (env)->GetMethodID(classOrbitPoint, "toDouble", "()[D");
//
//
//    jmethodID waveCurveAddBranch = (env)->GetMethodID(classWaveCurve, "add", "(Lrpnumerics/WaveCurveBranch;)V");
//
//    //Input processing
//    jdoubleArray inputPhasePointArray = (jdoubleArray) (env)->CallObjectMethod(initialPoint, toDoubleMethodID);
//
//    double input [env->GetArrayLength(inputPhasePointArray)];
//
//    env->GetDoubleArrayRegion(inputPhasePointArray, 0, env->GetArrayLength(inputPhasePointArray), input);
//
//    RealVector realVectorInput(env->GetArrayLength(inputPhasePointArray));
//
//
//    for (i = 0; i < (unsigned int) realVectorInput.size(); i++) {
//
//        realVectorInput.component(i) = input[i];
//
//    }
//
//    string physicsID(RpNumerics::getPhysics().getSubPhysics(0).ID());
//
//    //    if (physicsID.compare("TPCW") == 0) {
//    //        realVectorInput.resize(3);
//    //        realVectorInput.component(2) = 1.0;
//    //    }
//    RpNumerics::getPhysics().getSubPhysics(0).preProcess(realVectorInput);
//    env->DeleteLocalRef(inputPhasePointArray);
//
//
//    vector <vector<RealVector> > allCoords, shock_alt;
//
//
//
//    if (increase == RAREFACTION_SPEED_INCREASE)
//        increase = WAVE_FORWARD;
//
//    if (increase == RAREFACTION_SPEED_DECREASE)
//        increase = WAVE_BACKWARD;
//
//    const FluxFunction * fluxFunction = &RpNumerics::getPhysics().fluxFunction();
//    const AccumulationFunction * accumulationFunction = &RpNumerics::getPhysics().accumulation();
//
//    HugoniotContinuation * shock = RpNumerics::getPhysics().getSubPhysics(0).getHugoniotContinuationMethod();
//
//    Viscosity_Matrix viscosityMatrix;
//
//    ReferencePoint referencePoint(realVectorInput, fluxFunction, accumulationFunction, &viscosityMatrix);
//
//    shock->set_reference_point(referencePoint);
//
//    shock->curve(allCoords);
//
//    if (allCoords.size() == 0) return NULL;
//
//    jobject waveCurveBranchForward = env->NewObject(classWaveCurve, waveCurveConstructor, 0, increase);
//
//    for (i = 0; i < allCoords.size(); i++) {
//
//        jobjectArray orbitPointArray = (jobjectArray) (env)->NewObjectArray(allCoords[i].size(), classOrbitPoint, NULL);
//
//        for (int j = 0; j < allCoords[i].size(); j++) {
//
//            RealVector tempVector = allCoords[i].at(j);
//
//            RpNumerics::getPhysics().getSubPhysics(0).postProcess(tempVector);
//
//            double lambda = tempVector.component(tempVector.size() - 1);
//
//            double * dataCoords = tempVector;
//
//            jdoubleArray jTempArray = (env)->NewDoubleArray(tempVector.size());
//
//            (env)->SetDoubleArrayRegion(jTempArray, 0, tempVector.size(), dataCoords);
//
//
//            jobject orbitPoint = (env)->NewObject(classOrbitPoint, orbitPointConstructor, jTempArray, lambda);
//
//            (env)->SetObjectArrayElement(orbitPointArray, j, orbitPoint);
//
//            env->DeleteLocalRef(jTempArray);
//
//            env->DeleteLocalRef(orbitPoint);
//
//        }
//
//        jobject shockCurve = (env)->NewObject(shockCurveClass, shockCurveConstructor, orbitPointArray, 0, increase);
//
//        env->CallVoidMethod(waveCurveBranchForward, waveCurveAddBranch, shockCurve);
//
//    }
//
//    //Building the orbit
//
//    //Cleaning up
//
//    allCoords.clear();
//
//    //    env->DeleteLocalRef(orbitPointArray);
//    env->DeleteLocalRef(classOrbitPoint);
//    env->DeleteLocalRef(shockCurveClass);
//
//    return waveCurveBranchForward;
//
//
//
//}
