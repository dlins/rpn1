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
#include "Stone.h"
#include "StoneAccumulation.h"
#include "RectBoundary.h"
#include <vector>
#include "RarefactionCurve.h"

#include "CompositeCurve.h"
#include "LSODE.h"


#include "Debug.h"

using std::vector;

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


JNIEXPORT jobject JNICALL Java_rpnumerics_CompositeCalc_nativeCalc(JNIEnv * env, jobject obj, jobject initialPoint, jint increase, jint familyIndex) {

    if (Debug::get_debug_level() == 5) {
        cout << "chamando JNI composite calc" << endl;
    }

    unsigned int i;

    jclass classOrbitPoint = (env)->FindClass(ORBITPOINT_LOCATION);
    jclass classRarefactionOrbit = (env)->FindClass(COMPOSITECURVE_LOCATION);

    jmethodID rarefactionOrbitConstructor = (env)->GetMethodID(classRarefactionOrbit, "<init>", "([Lrpnumerics/OrbitPoint;II)V");
    jmethodID orbitPointConstructor = (env)->GetMethodID(classOrbitPoint, "<init>", "([DD)V");
    jmethodID toDoubleMethodID = (env)->GetMethodID(classOrbitPoint, "toDouble", "()[D");

    //Input processing
    jdoubleArray inputPhasePointArray = (jdoubleArray) (env)->CallObjectMethod(initialPoint, toDoubleMethodID);

    double input [env->GetArrayLength(inputPhasePointArray)];

    env->GetDoubleArrayRegion(inputPhasePointArray, 0, env->GetArrayLength(inputPhasePointArray), input);

    RealVector realVectorInput(env->GetArrayLength(inputPhasePointArray));


    for (i = 0; i < (unsigned int) realVectorInput.size(); i++) {

        realVectorInput.component(i) = input[i];

    }

    env->DeleteLocalRef(inputPhasePointArray);

    // Storage space for the segments:

    std::vector<RealVector> rarefactionCurve;
    std::vector<RealVector> compositeCurve;

    if (Debug::get_debug_level() == 5) {
        cout << "Chamando com stone" << endl;
    }

    FluxFunction * flux = (FluxFunction *) RpNumerics::getPhysics().fluxFunction().clone();

    AccumulationFunction * accum = (AccumulationFunction *) RpNumerics::getPhysics().accumulation().clone();

    Boundary * boundary = RpNumerics::getPhysics().boundary().clone();

    if (Debug::get_debug_level() == 5) {
        cout << "Increase: " << increase << endl;
    }

    //Compute rarefaction

    if (Debug::get_debug_level() == 5) {
        cout << "Increase da rarefacao: " << increase << endl;
    }



    if (increase == 20)

        increase = RAREFACTION_SPEED_SHOULD_INCREASE;

    if (increase == 22)

        increase = RAREFACTION_SPEED_SHOULD_DECREASE;




    vector<RealVector> inflectionPoints;


    RarefactionCurve rc(accum, flux, boundary);


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



    //    WaveCurve hwc;
    //
    //    hwc.wavecurve.push_back(rarcurve);

    //
    //    future_curve_initial_point = rarcurve.last_point;
    //    future_curve_initial_direction = rarcurve.final_direction;


    HugoniotContinuation_nDnD hug(flux, accum, boundary);
    ShockCurve sc(&hug);

    CompositeCurve cmp(accum, flux, boundary, &sc,0);

    //    LSODE lsode;
    //    //   EulerSolver eulersolver(boundary, 1);
    //
    //    ODE_Solver *odesolver;
    //
    //    odesolver = &lsode;
    //   odesolver = &eulersolver;



    Curve cmpcurve, new_rarcurve;
    //    RealVector final_direction;

    int composite_stopped_because;

    //    int edge;

    int info_cmp = cmp.curve(accum, flux, boundary,
            rarcurve,
            rarcurve.curve.size() - 1, rarcurve.curve.size() - 1,
            odesolver, deltaxi,
            COMPOSITE_BEGINS_AT_INFLECTION, // COMPOSITE_BEGINS_AT_INFLECTION or COMPOSITE_AFTER_COMPOSITE.
            familyIndex,
            new_rarcurve,
            cmpcurve,
            final_direction,
            composite_stopped_because,
            edge);



    //    CompositeCurve::curve(rarefactionCurve, COMPOSITE_FROM_NORMAL_RAREFACTION, familyIndex,
    //            increase, 0, stoneflux, stoneaccum, tempBoundary, compositeCurve);
    //
    //    delete stoneflux;
    //    delete stoneaccum;
    //    delete tempBoundary;

//    if (cmpcurve.curve.size() == 0)
//        return NULL;

//    cout<<"Tamanho da curva composta: "<<cmpcurve.curve.size()<<endl;
//    cout <<cmpcurve.curve[0]<<endl;

    //Orbit members creation
    
    

    jobjectArray orbitPointArray = (jobjectArray) (env)->NewObjectArray(cmpcurve.curve.size()-1, classOrbitPoint, NULL);

    for (int i = 1; i < cmpcurve.curve.size(); i++) {

        RealVector tempVector = cmpcurve.curve[i];
//        cout<<tempVector<<endl;


        double * dataCoords = tempVector;

        jdoubleArray jTempArray = (env)->NewDoubleArray(tempVector.size());

        (env)->SetDoubleArrayRegion(jTempArray, 0, tempVector.size(), dataCoords);
        
        double lambda =cmpcurve.speed[i];

        jobject orbitPoint = (env)->NewObject(classOrbitPoint, orbitPointConstructor, jTempArray,lambda);

        (env)->SetObjectArrayElement(orbitPointArray, i-1, orbitPoint);

        env->DeleteLocalRef(jTempArray);

        env->DeleteLocalRef(orbitPoint);

    }

    //Building the orbit

    jobject rarefactionOrbit = (env)->NewObject(classRarefactionOrbit, rarefactionOrbitConstructor, orbitPointArray, increase, familyIndex);


    //Cleaning up



    env->DeleteLocalRef(orbitPointArray);
    env->DeleteLocalRef(classOrbitPoint);
    env->DeleteLocalRef(classRarefactionOrbit);

    return rarefactionOrbit;



}
