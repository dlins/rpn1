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
    jclass classWaveCurveOrbit = (env)->FindClass(WAVECURVEORBIT_LOCATION);



    jmethodID setCorrespondingCurveIndexID = (env)->GetMethodID(classOrbitPoint, "setCorrespondingCurveIndex", "(I)V");
    jmethodID setCorrespondingPointIndexID = (env)->GetMethodID(classOrbitPoint, "setCorrespondingPointIndex", "(I)V");
    jmethodID setLambdaID = (env)->GetMethodID(classOrbitPoint, "setLambda", "(D)V");




    jmethodID setCurveTypeID = (env)->GetMethodID(classWaveCurveOrbit, "setCurveType", "(I)V");
    jmethodID setCurveIndexID = (env)->GetMethodID(classWaveCurveOrbit, "setCurveIndex", "(I)V");
    jmethodID setInitialSubCurveID = (env)->GetMethodID(classWaveCurveOrbit, "setInitialSubCurve", "(Z)V");




    jmethodID shockCurveConstructor = (env)->GetMethodID(shockCurveClass, "<init>", "([Lrpnumerics/OrbitPoint;II)V");
    jmethodID rarefactionOrbitConstructor = (env)->GetMethodID(classRarefactionOrbit, "<init>", "([Lrpnumerics/OrbitPoint;II)V");
    jmethodID compositeConstructor = (env)->GetMethodID(classComposite, "<init>", "([Lrpnumerics/OrbitPoint;II)V");



    jmethodID waveCurveConstructor = (env)->GetMethodID(classWaveCurve, "<init>", "(II)V");
    jmethodID orbitPointConstructor = (env)->GetMethodID(classOrbitPoint, "<init>", "([DD)V");
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



    if (timeDirection == RAREFACTION_SPEED_INCREASE)//TODO REMOVE !!!

        timeDirection = WAVE_FORWARD;

    if (timeDirection == RAREFACTION_SPEED_DECREASE)

        timeDirection = WAVE_BACKWARD;

    cout << "Parametros " << realVectorInput << " " << familyIndex << " " << timeDirection << endl;

    jobject waveCurve = (env)->NewObject(classWaveCurve, waveCurveConstructor, familyIndex, timeDirection);


    jobject waveCurveBranchForward = env->NewObject(classWaveCurve, waveCurveConstructor, familyIndex, timeDirection); //First branch for now

    wc.wavecurve(realVectorInput, familyIndex, timeDirection, curves);

    for (int i = 0; i < curves.size(); i++) {


        std::vector<RealVector> coords = curves[i].curve;
        std::vector<int> relatedCurvesIndexVector = curves[i].related_curve;
        std::vector<int> correspondingPointIndexVector=curves[i].corresponding_point_in_related_curve;
        if (coords.size() > 0) {

            cout<<"tamanho de coords: "<<coords.size()<<endl;

            jobjectArray orbitPointArray = (jobjectArray) (env)->NewObjectArray(coords.size(), classOrbitPoint, NULL);
            cout << "Tipo da curva: " << curves[i].type << endl;
            for (int j = 0; j < coords.size(); j++) {


                RealVector tempVector = coords.at(j);

                //cout<<tempVector<<endl;

                double * dataCoords = tempVector;

                //Reading only coodinates
                jdoubleArray jTempArray = (env)->NewDoubleArray(tempVector.size()-1);

                (env)->SetDoubleArrayRegion(jTempArray, 0, tempVector.size()-1, dataCoords);

                //Lambda is the last component.
                double lambda = tempVector.component(tempVector.size()-1);
                jobject orbitPoint = (env)->NewObject(classOrbitPoint, orbitPointConstructor, jTempArray,lambda);
//                env->CallVoidMethod(orbitPoint,setLambdaID,tempVector(tempVector.size()-1));

                env->CallObjectMethod(orbitPoint, setCorrespondingCurveIndexID, relatedCurvesIndexVector[j]);
                env->CallObjectMethod(orbitPoint,setCorrespondingPointIndexID, correspondingPointIndexVector[j]);

                (env)->SetObjectArrayElement(orbitPointArray, j, orbitPoint);


            }


            switch (curves[i].type) {
                case 1:
                {
                    jobject rarefactionOrbit = (env)->NewObject(classRarefactionOrbit, rarefactionOrbitConstructor, orbitPointArray, familyIndex, timeDirection);
                    env->CallVoidMethod(rarefactionOrbit, setCurveTypeID, 1);
                    env->CallVoidMethod(waveCurveBranchForward, waveCurveAddBranch, rarefactionOrbit);
                    env->CallVoidMethod(rarefactionOrbit, setCurveIndexID, i);
                    env->CallVoidMethod(rarefactionOrbit, setInitialSubCurveID, curves[i].initial_subcurve);

                }
                    break;
                case 2:
                {
                    jobject shockCurve = (env)->NewObject(shockCurveClass, shockCurveConstructor, orbitPointArray, familyIndex, timeDirection);
                    env->CallVoidMethod(shockCurve, setCurveTypeID, 2);
                    env->CallVoidMethod(waveCurveBranchForward, waveCurveAddBranch, shockCurve);
                    env->CallVoidMethod(shockCurve, setCurveIndexID, i);
                    env->CallVoidMethod(shockCurve, setInitialSubCurveID, curves[i].initial_subcurve);
                }
                    break;
                case 3:
                {
                    jobject compositeCurve = (env)->NewObject(classComposite, compositeConstructor, orbitPointArray, timeDirection, familyIndex);
                    env->CallVoidMethod(compositeCurve, setCurveTypeID, 3);
                    env->CallVoidMethod(waveCurveBranchForward, waveCurveAddBranch, compositeCurve);
                    env->CallVoidMethod(compositeCurve, setCurveIndexID, i);
                    env->CallVoidMethod(compositeCurve, setInitialSubCurveID, curves[i].initial_subcurve);
                }
                    break;

                default:
                    cout << "Tipo de curva nao conhecido !!" << endl;
            }

        } else {

            cout << "CURVA " << i << " VAZIA !! tipo: " << curves[i].type << endl;

        }


    }

    env->CallObjectMethod(waveCurve, waveCurveAddBranch, waveCurveBranchForward);

    return waveCurve;
   


}