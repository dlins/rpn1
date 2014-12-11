/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) JNIRarefactionOrbitCalc.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "rpnumerics_CompositeCalc.h"
#include "RpNumerics.h"
#include "RealVector.h"
#include "JNIDefs.h"
#include "RectBoundary.h"
#include <vector>
#include "RarefactionCurve.h"

#include "CompositeCurve.h"
#include "LSODE.h"




using std::vector;

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


JNIEXPORT jobject JNICALL Java_rpnumerics_CompositeCalc_nativeCalc(JNIEnv * env, jobject obj, jobject initialPoint, jint increase, jint familyIndex) {

  
    unsigned int i;

    jclass classOrbitPoint = (env)->FindClass(ORBITPOINT_LOCATION);
    jclass classRarefactionOrbit = (env)->FindClass(COMPOSITECURVE_LOCATION);
    jclass classWaveCurveBranch = (env)->FindClass(WAVECURVEBRANCH_LOCATION);

    jmethodID rarefactionOrbitConstructor = (env)->GetMethodID(classRarefactionOrbit, "<init>", "([Lrpnumerics/OrbitPoint;II)V");
    jmethodID orbitPointConstructor = (env)->GetMethodID(classOrbitPoint, "<init>", "([D[DD)V");
    jmethodID toDoubleMethodID = (env)->GetMethodID(classOrbitPoint, "toDouble", "()[D");

    jmethodID setReferencePointID = (env)->GetMethodID(classWaveCurveBranch, "setReferencePoint", "(Lrpnumerics/OrbitPoint;)V");

    //Input processing
    jdoubleArray inputPhasePointArray = (jdoubleArray) (env)->CallObjectMethod(initialPoint, toDoubleMethodID);

    double input [env->GetArrayLength(inputPhasePointArray)];

    env->GetDoubleArrayRegion(inputPhasePointArray, 0, env->GetArrayLength(inputPhasePointArray), input);

    RealVector realVectorInput(env->GetArrayLength(inputPhasePointArray));


    for (i = 0; i < (unsigned int) realVectorInput.size(); i++) {

        realVectorInput.component(i) = input[i];

    }


    
    
int dimension = RpNumerics::physicsVector_->at(0)->boundary()->minimums().size();
     

    env->DeleteLocalRef(inputPhasePointArray);

//    // Storage space for the segments:
//
//    std::vector<RealVector> rarefactionCurve;
//    std::vector<RealVector> compositeCurve;

    
    
    const FluxFunction * flux = RpNumerics::physicsVector_->at(0)->flux();
    const AccumulationFunction * accum = RpNumerics::physicsVector_->at(0)->accumulation();
    


    //    const Boundary * boundary = &RpNumerics::getPhysics().boundary();

    const Boundary * tempBoundary = RpNumerics::physicsVector_->at(0)->boundary();

  
    if (increase == 20)

        increase = RAREFACTION_SPEED_SHOULD_INCREASE;

    if (increase == 22)

        increase = RAREFACTION_SPEED_SHOULD_DECREASE;

    vector<RealVector> inflectionPoints;

    RarefactionCurve rc(accum, flux, tempBoundary);

    double deltaxi = 1e-3;
    std::vector<RealVector> inflection_point;
    Curve rarcurve;

    int rar_stopped_because;
    int edge;
    RealVector final_direction;

    LSODE lsode;
    ODE_Solver *odesolver;

    odesolver = &lsode;

    int info_rar = rc.curve(realVectorInput,
            familyIndex,
            increase,
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
    
    
     if(info_rar==RAREFACTION_ERROR){
        return NULL;
    }
    
    

    CompositeCurve * cmp = RpNumerics::physicsVector_->at(0)->composite();
 
    Curve cmpcurve, new_rarcurve;

    int composite_stopped_because;

    //    int edge;

    int info_cmp = cmp->curve(accum, flux, tempBoundary,
            rarcurve,
            rarcurve.curve.back(), rarcurve.curve.size() - 1,
            odesolver, deltaxi,
            COMPOSITE_BEGINS_AT_INFLECTION, // COMPOSITE_BEGINS_AT_INFLECTION or COMPOSITE_AFTER_COMPOSITE.
            familyIndex,
            new_rarcurve,
            cmpcurve,
            final_direction,
            composite_stopped_because,
            edge);
    
    
    if(info_cmp==COMPOSITE_ERROR){
        return NULL;
    }
    


    ReferencePoint referencePoint(realVectorInput, flux, accum, 0);

    double speed = referencePoint.e[familyIndex].r;

    double nativeEigenValues [dimension];

    for (int i = 0; i < dimension; i++) {

        nativeEigenValues[i] = referencePoint.e[i].r;

    }
    
    

    jdoubleArray eigenValuesArray = (env)->NewDoubleArray(dimension);

    (env)->SetDoubleArrayRegion(eigenValuesArray, 0, dimension, nativeEigenValues);


    jdoubleArray refPointCoords = (env)->NewDoubleArray(dimension);

    (env)->SetDoubleArrayRegion(refPointCoords, 0, dimension, (double *) realVectorInput);

    jobject referenceOrbitPoint = (env)->NewObject(classOrbitPoint, orbitPointConstructor, refPointCoords, eigenValuesArray, speed);

    jobjectArray orbitPointArray = (jobjectArray) (env)->NewObjectArray(cmpcurve.curve.size() - 1, classOrbitPoint, NULL);

    for (int i = 1; i < cmpcurve.curve.size(); i++) {
  
        RealVector tempVector = cmpcurve.curve[i];

        
        double * dataCoords = tempVector;

        jdoubleArray jTempArray = (env)->NewDoubleArray(tempVector.size());

        (env)->SetDoubleArrayRegion(jTempArray, 0, tempVector.size(), dataCoords);

        double speed = cmpcurve.speed[i];

        //  cmpcurve.eigenvalues //Autovalores em cada ponto da composta


        jdoubleArray jeigenValuesArray = (env)->NewDoubleArray(dimension);
        RealVector eigenValue = cmpcurve.eigenvalues[i];
        (env)->SetDoubleArrayRegion(jeigenValuesArray, 0, eigenValue.size(), (double *) eigenValue);


        jobject orbitPoint = (env)->NewObject(classOrbitPoint, orbitPointConstructor, jTempArray, jeigenValuesArray, speed);


        (env)->SetObjectArrayElement(orbitPointArray, i - 1, orbitPoint);

        env->DeleteLocalRef(jTempArray);

        env->DeleteLocalRef(orbitPoint);

    }

    //Building the orbit

    jobject rarefactionOrbit = (env)->NewObject(classRarefactionOrbit, rarefactionOrbitConstructor, orbitPointArray, increase, familyIndex);


    env->CallVoidMethod(rarefactionOrbit, setReferencePointID, referenceOrbitPoint);

    //Cleaning up





    env->DeleteLocalRef(orbitPointArray);
    env->DeleteLocalRef(classOrbitPoint);
    env->DeleteLocalRef(classRarefactionOrbit);

    return rarefactionOrbit;



}
