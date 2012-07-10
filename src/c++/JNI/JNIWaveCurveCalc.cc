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


    jclass shockCurveClass = (env)->FindClass(SHOCKCURVE_LOCATION);
    jclass classRarefactionOrbit = (env)->FindClass(RAREFACTIONORBIT_LOCATION);
    jclass classComposite = (env)->FindClass(COMPOSITECURVE_LOCATION);

    jmethodID shockCurveConstructor = (env)->GetMethodID(shockCurveClass, "<init>", "([Lrpnumerics/OrbitPoint;II)V");
    jmethodID rarefactionOrbitConstructor = (env)->GetMethodID(classRarefactionOrbit, "<init>", "([Lrpnumerics/OrbitPoint;II)V");
    jmethodID compositeConstructor = (env)->GetMethodID(classComposite, "<init>", "([Lrpnumerics/OrbitPoint;II)V");



    jmethodID waveCurveConstructor = (env)->GetMethodID(classWaveCurve, "<init>", "(II)V");
    jmethodID orbitPointConstructor = (env)->GetMethodID(classOrbitPoint, "<init>", "([D)V");
    jmethodID toDoubleMethodID = (env)->GetMethodID(classOrbitPoint, "toDouble", "()[D");



    jmethodID waveCurveAddBranch = (env)->GetMethodID(classWaveCurve, "add", "(Lrpnumerics/WaveCurveBranch;)V");


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

    if (timeDirection == RAREFACTION_SPEED_INCREASE)//TODO REMOVE !!!

        timeDirection = WAVE_FORWARD;

    if (timeDirection == RAREFACTION_SPEED_DECREASE)

        timeDirection = WAVE_BACKWARD;


    wc.wavecurve(realVectorInput, familyIndex, timeDirection, curves);

    

    jobject waveCurveBranch = env->NewObject(classWaveCurve, waveCurveConstructor, familyIndex, timeDirection); //Only one branch for now

    for (int i = 0; i < curves.size(); i++) {

        std::vector<RealVector> coords = curves[i].curve;

//        cout << "Tipo da curva: " << curves[i].type<<" Tamanho da curva: "<< coords.size() << endl;

        jobjectArray orbitPointArray = (jobjectArray) (env)->NewObjectArray(coords.size(), classOrbitPoint, NULL);
//        cout << "Tipo da curva: " << curves[i].type << endl;

        for (int j = 0; j < coords.size(); j++) {

            RealVector tempVector = coords.at(j);

            if (tempVector.size() == 2) {

                tempVector.resize(3);
                tempVector(2) = 0;

            }
            //cout<<tempVector<<endl;

            double * dataCoords = tempVector;

            //Reading only coodinates
            jdoubleArray jTempArray = (env)->NewDoubleArray(tempVector.size());

            (env)->SetDoubleArrayRegion(jTempArray, 0, tempVector.size(), dataCoords);

            //Lambda is the last component.
            jobject orbitPoint = (env)->NewObject(classOrbitPoint, orbitPointConstructor, jTempArray);

            (env)->SetObjectArrayElement(orbitPointArray, j, orbitPoint);


        }


        switch (curves[i].type) {
            case 1:
            {
                jobject rarefactionOrbit = (env)->NewObject(classRarefactionOrbit, rarefactionOrbitConstructor, orbitPointArray, familyIndex, timeDirection);
                env->CallObjectMethod(waveCurveBranch, waveCurveAddBranch, rarefactionOrbit);
            }
                break;
            case 2:
            {
                jobject shockCurve = (env)->NewObject(shockCurveClass, shockCurveConstructor, orbitPointArray, familyIndex, timeDirection);
                env->CallObjectMethod(waveCurveBranch, waveCurveAddBranch, shockCurve);
            }
                break;
            case 3:
            {
                jobject compositeCurve = (env)->NewObject(classComposite, compositeConstructor, orbitPointArray, timeDirection, familyIndex);
                env->CallObjectMethod(waveCurveBranch, waveCurveAddBranch, compositeCurve);
            }
                break;

            default:
                cout << "Tipo de curva nÃ£o conhecido !!" << endl;
        }

    }



    //Building the orbit

    jobject waveCurve = (env)->NewObject(classWaveCurve, waveCurveConstructor, familyIndex, timeDirection);


    env->CallObjectMethod(waveCurve, waveCurveAddBranch, waveCurveBranch);

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