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
#include "rpnumerics_IntegralCurveCalc.h"
#include "RpNumerics.h"
#include "RealVector.h"
#include "JNIDefs.h"
#include <vector>
#include <time.h>
#include "Integral_Curve.h"

using std::vector;

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


JNIEXPORT jobject JNICALL Java_rpnumerics_IntegralCurveCalc_calc(JNIEnv * env, jobject obj, jobject initialPoint, jint familyIndex) {


    unsigned int i;

    jclass classOrbitPoint = (env)->FindClass(ORBITPOINT_LOCATION);

    jclass classIntegralCurve = (env)->FindClass(INTEGRALCURVE_LOCATION);

    jclass realVectorClass = env->FindClass(REALVECTOR_LOCATION);

    jclass arrayListClass = env->FindClass("java/util/ArrayList");


    jmethodID arrayListConstructor = env->GetMethodID(arrayListClass, "<init>", "()V");
    jmethodID arrayListAddMethod = env->GetMethodID(arrayListClass, "add", "(Ljava/lang/Object;)Z");

    jmethodID realVectorConstructorID = env->GetMethodID(realVectorClass, "<init>", "(I)V");
    jmethodID integralCurveConstructor = (env)->GetMethodID(classIntegralCurve, "<init>", "([Lrpnumerics/OrbitPoint;ILjava/util/List;)V");

    jmethodID orbitPointConstructor = (env)->GetMethodID(classOrbitPoint, "<init>", "([D)V");
    jmethodID toDoubleMethodID = (env)->GetMethodID(classOrbitPoint, "toDouble", "()[D");

    jmethodID setElementMethodID = (env)->GetMethodID(realVectorClass, "setElement", "(ID)V");

    //Input processing
    jdoubleArray inputPhasePointArray = (jdoubleArray) (env)->CallObjectMethod(initialPoint, toDoubleMethodID);

    double input [env->GetArrayLength(inputPhasePointArray)];

    env->GetDoubleArrayRegion(inputPhasePointArray, 0, env->GetArrayLength(inputPhasePointArray), input);

    RealVector realVectorInput(env->GetArrayLength(inputPhasePointArray));


    for (i = 0; i < (unsigned int) realVectorInput.size(); i++) {

        realVectorInput.component(i) = input[i];

    }

    env->DeleteLocalRef(inputPhasePointArray);


    int dimension = RpNumerics::getPhysics().domain().dim();

    vector <RealVector> coords;
    vector<RealVector> inflectionPoints;

    Boundary * tempBoundary = RpNumerics::getPhysics().boundary().clone();

    double deltaxi = 1e-3;


    const FluxFunction * fluxFunction = &RpNumerics::getPhysics().fluxFunction();
    const AccumulationFunction * accumulationFunction = &RpNumerics::getPhysics().accumulation();

    cout << "Flux params " << fluxFunction->fluxParams().params() << endl;
    cout << "Accum params " << accumulationFunction->accumulationParams().params() << endl;

    Integral_Curve iCurve(fluxFunction, accumulationFunction, tempBoundary);


    iCurve.integral_curve(realVectorInput,
            deltaxi,
            familyIndex,
            coords, inflectionPoints);


    delete tempBoundary;

    if (coords.size() == 0) {
        return NULL;
    }

    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    //Orbit members creation


    jobject inflectionPointList = env->NewObject(arrayListClass, arrayListConstructor, NULL);



    for (int i = 0; i < inflectionPoints.size(); i++) {

        jobject inflectionPoint = env->NewObject(realVectorClass, realVectorConstructorID, dimension);


        for (int j = 0; j < dimension; j++) {

            env->CallVoidMethod(inflectionPoint, setElementMethodID, j, inflectionPoints[i].component(j));

        }

        env->CallObjectMethod(inflectionPointList, arrayListAddMethod, inflectionPoint);

    }

    jobjectArray orbitPointArray = (jobjectArray) (env)->NewObjectArray(coords.size(), classOrbitPoint, NULL);

    for (i = 0; i < coords.size(); i++) {

        RealVector tempVector = coords.at(i);

        double * dataCoords = tempVector;

        jdoubleArray jTempArray = (env)->NewDoubleArray(tempVector.size());

        (env)->SetDoubleArrayRegion(jTempArray, 0, tempVector.size(), dataCoords);

        jobject orbitPoint = (env)->NewObject(classOrbitPoint, orbitPointConstructor, jTempArray);

        (env)->SetObjectArrayElement(orbitPointArray, i, orbitPoint);

        env->DeleteLocalRef(jTempArray);

        env->DeleteLocalRef(orbitPoint);

    }

    //Building the orbit

    jobject integralCurve = (env)->NewObject(classIntegralCurve, integralCurveConstructor, orbitPointArray, familyIndex, inflectionPointList);



    //Cleaning up

    coords.clear();

    env->DeleteLocalRef(orbitPointArray);
    env->DeleteLocalRef(classOrbitPoint);

    return integralCurve;

}
