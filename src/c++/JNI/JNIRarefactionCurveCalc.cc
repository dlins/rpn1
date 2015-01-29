/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) JNIRarefactionCurveCalc.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "rpnumerics_RarefactionCurveCalc.h"

#include "RpNumerics.h"
#include "RealVector.h"
#include "JNIDefs.h"
#include <vector>

//#include "TPCW.h"
#include "RarefactionCurve.h"
#include "LSODE.h"


using std::vector;

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


JNIEXPORT jobject JNICALL Java_rpnumerics_RarefactionCurveCalc_calc(JNIEnv * env, jobject obj, jstring methodName, jstring flowName, jobject initialPoint, jint familyIndex, jint timeDirection) {


    unsigned int i;

    jclass classOrbitPoint = (env)->FindClass(ORBITPOINT_LOCATION);
    jclass classRarefactionOrbit = (env)->FindClass(RAREFACTIONCURVE_LOCATION);
    jclass classWaveCurveBranch = (env)->FindClass(WAVECURVEBRANCH_LOCATION);

    jmethodID rarefactionOrbitConstructor = (env)->GetMethodID(classRarefactionOrbit, "<init>", "([Lrpnumerics/OrbitPoint;II)V");
    jmethodID orbitPointConstructor = (env)->GetMethodID(classOrbitPoint, "<init>", "([D[DD)V");
    jmethodID toDoubleMethodID = (env)->GetMethodID(classOrbitPoint, "toDouble", "()[D");




    jclass fundamentalCurveClass = env->FindClass(FUNDAMENTALCURVE_LOCATION);

    jmethodID setXIMethodID = (env)->GetMethodID(fundamentalCurveClass, "setXi", "([D)V");


    jmethodID setReferencePointID = (env)->GetMethodID(classWaveCurveBranch, "setReferencePoint", "(Lrpnumerics/OrbitPoint;)V");

    //Input processing
    jdoubleArray inputPhasePointArray = (jdoubleArray) (env)->CallObjectMethod(initialPoint, toDoubleMethodID);

    double input [env->GetArrayLength(inputPhasePointArray)];

    env->GetDoubleArrayRegion(inputPhasePointArray, 0, env->GetArrayLength(inputPhasePointArray), input);

    RealVector realVectorInput(env->GetArrayLength(inputPhasePointArray));


    for (i = 0; i < (unsigned int) realVectorInput.size(); i++) {

        realVectorInput.component(i) = input[i];

    }

    env->DeleteLocalRef(inputPhasePointArray);
    //

    vector <RealVector> coords;

    RarefactionCurve *rarefaction_curve = RpNumerics::physicsVector_->at(0)->rarefaction_curve();


    //    double deltaxi = 1e-3; // This is the original value (Rodrigo/ Panters)


    int dimension = realVectorInput.size();


    cout << "Ponto de entrada: " << realVectorInput << endl;
    cout << "Direcao : " << timeDirection << endl;


    const FluxFunction * flux = RpNumerics::physicsVector_->at(0)->flux();
    const AccumulationFunction * accum = RpNumerics::physicsVector_->at(0)->accumulation();



    vector<RealVector> inflectionPoints;




    //cout << "Ponto de entrada apos pos process: " << realVectorInput << endl;

//    RarefactionCurve rc(accum, flux, tempBoundary);


    double deltaxi = 1e-3;
    std::vector<RealVector> inflection_point;
    Curve rarcurve;

    int rar_stopped_because;
    int edge;
    RealVector final_direction;

    LSODE lsode;
    ODE_Solver *odesolver;

    odesolver = &lsode;

    int info_rar = rarefaction_curve->curve(realVectorInput,
            familyIndex,
            timeDirection,
            RAREFACTION,
            RAREFACTION_INITIALIZE,
            0,
            odesolver,
            deltaxi,
            rarcurve,
            inflection_point,
            final_direction,
            rar_stopped_because,
            edge);


    ReferencePoint referencePoint(realVectorInput, flux, accum, 0);

    double lambda = referencePoint.e[familyIndex].r;

    double nativeEigenValues [dimension];

    for (int i = 0; i < dimension; i++) {

        nativeEigenValues[i] = referencePoint.e[i].r;

    }

    jdoubleArray eigenValuesArray = (env)->NewDoubleArray(dimension);

    (env)->SetDoubleArrayRegion(eigenValuesArray, 0, dimension, nativeEigenValues);


    jdoubleArray refPointCoords = (env)->NewDoubleArray(dimension);

    (env)->SetDoubleArrayRegion(refPointCoords, 0, dimension, input);

    jobject referenceOrbitPoint = (env)->NewObject(classOrbitPoint, orbitPointConstructor, refPointCoords, eigenValuesArray, lambda);


    



    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    //Orbit members creation

    jobjectArray orbitPointArray = (jobjectArray) (env)->NewObjectArray(rarcurve.curve.size(), classOrbitPoint, NULL);

    for (i = 0; i < rarcurve.curve.size(); i++) {

        RealVector tempVector = rarcurve.curve.at(i);

        double lambda = rarcurve.speed[i];

        //        for (int k = 0; k < rarcurve.eigenvalues[i].size(); k++) {
        //            //cout << " i: " << i << " " << rarcurve.eigenvalues[i][k] << endl;
        //
        //        }


        //        double * dataCoords = tempVector;

        RealVector resizedVector(tempVector);
//        RpNumerics::getPhysics().getSubPhysics(0).postProcess(resizedVector);

//       cout << tempVector << endl;


        double * dataCoords = resizedVector;



        jdoubleArray jTempArray = (env)->NewDoubleArray(tempVector.size());

        (env)->SetDoubleArrayRegion(jTempArray, 0, tempVector.size(), dataCoords);



        jdoubleArray jeigenValuesArray = (env)->NewDoubleArray(dimension);
        RealVector eigenValue = rarcurve.eigenvalues[i];



        (env)->SetDoubleArrayRegion(jeigenValuesArray, 0, eigenValue.size(), (double *) eigenValue);

        jobject orbitPoint = (env)->NewObject(classOrbitPoint, orbitPointConstructor, jTempArray, jeigenValuesArray, lambda);

        (env)->SetObjectArrayElement(orbitPointArray, i, orbitPoint);

        env->DeleteLocalRef(jTempArray);

        env->DeleteLocalRef(orbitPoint);

    }

    //Building the orbit

    jobject rarefactionOrbit = (env)->NewObject(classRarefactionOrbit, rarefactionOrbitConstructor, orbitPointArray, familyIndex, timeDirection);

    env->CallVoidMethod(rarefactionOrbit, setReferencePointID, referenceOrbitPoint);



    //Cleaning up

    coords.clear();

    env->DeleteLocalRef(orbitPointArray);
    env->DeleteLocalRef(classOrbitPoint);
    env->DeleteLocalRef(classRarefactionOrbit);

    return rarefactionOrbit;

}

/*
 * Class:     rpnumerics_RarefactionCurveCalc
 * Method:    boundaryNativeCalc
 * Signature: (Lrpnumerics/OrbitPoint;III)Lrpnumerics/RpSolution;
 */
JNIEXPORT jobject JNICALL Java_rpnumerics_RarefactionCurveCalc_boundaryNativeCalc
(JNIEnv *env, jobject obj, jobject initialPoint, jint familyIndex, jint timeDirection, jint edge) {


//
//
//    unsigned int i;
//
//    jclass classOrbitPoint = (env)->FindClass(ORBITPOINT_LOCATION);
//    jclass classRarefactionOrbit = (env)->FindClass(RAREFACTIONCURVE_LOCATION);
//
//    jmethodID rarefactionOrbitConstructor = (env)->GetMethodID(classRarefactionOrbit, "<init>", "([Lrpnumerics/OrbitPoint;II)V");
//    jmethodID orbitPointConstructor = (env)->GetMethodID(classOrbitPoint, "<init>", "([DD)V");
//    jmethodID toDoubleMethodID = (env)->GetMethodID(classOrbitPoint, "toDouble", "()[D");
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
//    env->DeleteLocalRef(inputPhasePointArray);
//
//    //    vector <RealVector> coords;
//
//    const Boundary * tempBoundary = RpNumerics::getPhysics().getSubPhysics(0).getPreProcessedBoundary();
//
//    const FluxFunction * flux = &RpNumerics::getPhysics().fluxFunction();
//    const AccumulationFunction * accum = &RpNumerics::getPhysics().accumulation();
//
//
//
//    RpNumerics::getPhysics().getSubPhysics(0).preProcess(realVectorInput);
//
//    RarefactionCurve rc(accum, flux, tempBoundary);
//
//
//    double deltaxi = 1e-3;
//    std::vector<RealVector> inflection_point;
//    Curve rarcurve;
//
//    int rar_stopped_because;
//    int s;
//    RealVector final_direction;
//
//    LSODE lsode;
//    ODE_Solver *odesolver;
//
//    odesolver = &lsode;
//
//    //    realVectorInput(0)=0.538996;
//    //    realVectorInput(1)=0.461004;
//
//    ////cout << "Ponto de entrada: " << realVectorInput << " edge " << edge << " familyIndex " << familyIndex << " timedirection " << timeDirection << endl;
//    ////cout << " rar for itself " << RAREFACTION << " odesolver " << odesolver << " deltaxi " << deltaxi << endl;
//
//    int info_rar = rc.curve_from_boundary(realVectorInput, edge,
//            familyIndex,
//            timeDirection,
//            RAREFACTION,
//            odesolver,
//            deltaxi,
//            rarcurve,
//            inflection_point,
//            final_direction,
//            rar_stopped_because,
//            s);
//
//
//    ////cout << "final direction : " << final_direction << endl;
//    ////cout << "rar_stop : " << rar_stopped_because << endl;
//
//    ////cout << "Tamanho de rar curve: " << rarcurve.curve.size() << endl;
//
//    RpNumerics::getPhysics().getSubPhysics(0).postProcess(rarcurve.curve);
//    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//
//    //Orbit members creation
//
//    jobjectArray orbitPointArray = (jobjectArray) (env)->NewObjectArray(rarcurve.curve.size(), classOrbitPoint, NULL);
//
//    for (i = 0; i < rarcurve.curve.size(); i++) {
//
//        RealVector tempVector = rarcurve.curve.at(i);
//
//        double lambda = rarcurve.speed[i];
//
//
//        if (Debug::get_debug_level() == 5) {
//            ////cout << tempVector << endl;
//        }
//
//        double * dataCoords = tempVector;
//
//        //Reading only coodinates
//        jdoubleArray jTempArray = (env)->NewDoubleArray(tempVector.size());
//
//        (env)->SetDoubleArrayRegion(jTempArray, 0, tempVector.size(), dataCoords);
//
//        //Lambda is the last component.
//        jobject orbitPoint = (env)->NewObject(classOrbitPoint, orbitPointConstructor, jTempArray, lambda);
//
//        (env)->SetObjectArrayElement(orbitPointArray, i, orbitPoint);
//
//        env->DeleteLocalRef(jTempArray);
//
//        env->DeleteLocalRef(orbitPoint);
//
//    }
//
//    //Building the orbit
//
//    jobject rarefactionOrbit = (env)->NewObject(classRarefactionOrbit, rarefactionOrbitConstructor, orbitPointArray, familyIndex, timeDirection);
//
//
//    //Cleaning up
//
//
//
//    env->DeleteLocalRef(orbitPointArray);
//    env->DeleteLocalRef(classOrbitPoint);
//    env->DeleteLocalRef(classRarefactionOrbit);
//
//    return rarefactionOrbit;










}
