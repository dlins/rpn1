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
#include "rpnumerics_WaveCurveCalc.h"

#include "RpNumerics.h"
#include "RealVector.h"
#include "JNIDefs.h"
#include <vector>


#include "WaveCurve.h"


using std::vector;

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


JNIEXPORT jobject JNICALL Java_rpnumerics_WaveCurveCalc_nativeCalc(JNIEnv * env, jobject obj, jobject initialPoint, jint familyIndex, jint timeDirection) {


    unsigned int i;

    jclass classOrbitPoint = (env)->FindClass(ORBITPOINT_LOCATION);
    jclass classWaveCurve = (env)->FindClass(WAVECURVE_LOCATION);
    jclass arrayListClass = env->FindClass("java/util/ArrayList");



    jmethodID waveCurveConstructor = (env)->GetMethodID(classWaveCurve, "<init>", "(Ljava/util/List;[III)V");
    jmethodID orbitPointConstructor = (env)->GetMethodID(classOrbitPoint, "<init>", "([D)V");
    jmethodID toDoubleMethodID = (env)->GetMethodID(classOrbitPoint, "toDouble", "()[D");


    jmethodID arrayListConstructor = env->GetMethodID(arrayListClass, "<init>", "()V");
    jmethodID arrayListAddMethod = env->GetMethodID(arrayListClass, "add", "(Ljava/lang/Object;)Z");

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

    vector <Curve> curves;


    const Boundary * boundary = &RpNumerics::getPhysics().boundary();

    const FluxFunction * fluxFunction = &RpNumerics::getPhysics().fluxFunction();
    const AccumulationFunction * accumulationFunction = &RpNumerics::getPhysics().accumulation();


    cout << "Flux params " << fluxFunction->fluxParams().params() << endl;
    cout << "Accum params " << accumulationFunction->accumulationParams().params() << endl;


    WaveCurve wc(fluxFunction, accumulationFunction, boundary);

    cout << "Parametros " << realVectorInput << " " << familyIndex << " " << timeDirection << endl;

      if (timeDirection == RAREFACTION_SPEED_INCREASE)

        timeDirection = WAVE_FORWARD;

    if (timeDirection == RAREFACTION_SPEED_DECREASE)

        timeDirection = WAVE_BACKWARD;


    wc.wavecurve(realVectorInput, familyIndex, timeDirection, curves);


//   wc.half_wavecurve(RAREFACTION_CURVE, realVectorInput, familyIndex, 10, curves);

//    wc.half_wavecurve(SHOCK_CURVE, realVectorInput, familyIndex, 10, curves);



    cout << "Saí" << endl;

    cout << "tamanho de curves: " << curves.size() << endl;


    for (int i = 0; i < curves.size(); i++) {




        cout << "Tipos de curva: " << curves[i].type << endl;

        //        std::vector<RealVector> coords = curvesElement.curve;


    }



    jintArray curvesTypeArray = (env)->NewIntArray(curves.size());

    int tempCurveTypeArray[curves.size()];

    jobject curvesArrayList = env->NewObject(arrayListClass, arrayListConstructor, NULL);


    for (int i = 0; i < curves.size(); i++) {

        tempCurveTypeArray[i] = curves[i].type;

        std::vector<RealVector> coords = curves[i].curve;

        if (coords.size() == 0) {
            return NULL;
        }


        jobjectArray orbitPointArray = (jobjectArray) (env)->NewObjectArray(coords.size(), classOrbitPoint, NULL);

        for (int j = 0; j < coords.size(); j++) {

            RealVector tempVector = coords.at(j);

            if (tempVector.size() == 2) {

                tempVector.resize(3);
                tempVector(2) = 0;

            }

            double  * dataCoords = tempVector ;

            //Reading only coodinates
            jdoubleArray jTempArray = (env)->NewDoubleArray(tempVector.size());

            (env)->SetDoubleArrayRegion(jTempArray, 0, tempVector.size(), dataCoords);

            //Lambda is the last component.
            jobject orbitPoint = (env)->NewObject(classOrbitPoint, orbitPointConstructor, jTempArray);

            (env)->SetObjectArrayElement(orbitPointArray, j, orbitPoint);



        }


        env->CallObjectMethod(curvesArrayList, arrayListAddMethod, orbitPointArray);




    }


    env->SetIntArrayRegion(curvesTypeArray, 0, curves.size(), tempCurveTypeArray);


    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    //Orbit members creation



    //Building the orbit

    jobject waveCurve = (env)->NewObject(classWaveCurve, waveCurveConstructor, curvesArrayList, curvesTypeArray, familyIndex, timeDirection);


    return waveCurve;
    //
    //
    //    //Cleaning up
    //
    //    coords.clear();
    //
    //    env->DeleteLocalRef(orbitPointArray);
    //    env->DeleteLocalRef(classOrbitPoint);
    //    env->DeleteLocalRef(classWaveCurve);



}
