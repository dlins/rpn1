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
#include "Debug.h"
#include <vector>
#include "RarefactionCurve.h"
#include "ShockCurve.h"
#include "CompositeCurve.h"
#include "LSODE.h"
#include "WaveCurveFactory.h"

#include "Inflection_Curve.h"

using std::vector;

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


/*
 * Class:     rpnumerics_WaveCurveCalc
 * Method:    boundaryNativeCalc
 * Signature: (Lrpnumerics/OrbitPoint;IIII)Lrpnumerics/RpSolution;
 */
JNIEXPORT jobject JNICALL Java_rpnumerics_WaveCurveCalc_nativeCalc
(JNIEnv * env, jobject obj, jobject initialPoint, jint familyIndex, jint timeDirection, int origin, jint s) {

    

    cout << "Valor de edge " << s << endl;


    unsigned int i;

    jclass classOrbitPoint = (env)->FindClass(ORBITPOINT_LOCATION);
    jclass classWaveCurve = (env)->FindClass(WAVECURVE_LOCATION);
    jclass arrayListClass = env->FindClass("java/util/ArrayList");


    jclass shockCurveClass = (env)->FindClass(SHOCKCURVE_LOCATION);
    jclass classRarefactionOrbit = (env)->FindClass(RAREFACTIONCURVE_LOCATION);
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

    const Boundary * boundary = &RpNumerics::getPhysics().boundary();

    const FluxFunction * flux = &RpNumerics::getPhysics().fluxFunction();
    
    cout<<"Parametros na chamada: "<<flux->fluxParams().params()<<endl;
    
    const AccumulationFunction * accum = &RpNumerics::getPhysics().accumulation();

    RarefactionCurve rc(accum, flux, boundary);

    HugoniotContinuation_nDnD hug(flux, accum, boundary);
    ShockCurve sc(&hug);

    CompositeCurve cmp(accum, flux, boundary, &sc);

    LSODE lsode;
    ODE_Solver *odesolver;
    odesolver = &lsode;


    WaveCurveFactory wavecurvefactory(accum, flux, boundary, odesolver, &rc, &sc, &cmp);


    WaveCurve hwc;
    int reason_why, edge;

    if (timeDirection == 20)//TODO REMOVE !!!

        timeDirection = RAREFACTION_SPEED_SHOULD_INCREASE; //WAVE_FORWARD;

    if (timeDirection == 22)

        timeDirection = RAREFACTION_SPEED_SHOULD_DECREASE; //WAVE_BACKWARD;




    if (origin == 0) {

        wavecurvefactory.wavecurve(realVectorInput, familyIndex, timeDirection, &hug, hwc, reason_why, edge);
    }

    if (origin == 1) {
        wavecurvefactory.wavecurve_from_boundary(realVectorInput, s, familyIndex, timeDirection, &hug, hwc, reason_why, edge);
    }



    if (origin == 2) {

        Inflection_Curve inflectionCurve;

        std::vector<RealVector> left_vrs;

        GridValues * gv = RpNumerics::getGridFactory().getGrid("bifurcationcurve");

        inflectionCurve.curve(& RpNumerics::getPhysics().fluxFunction(), & RpNumerics::getPhysics().accumulation(), *gv, familyIndex, left_vrs);

//        vector<RealVector> newCurve;
//        for (int i = 0; i < left_vrs.size() / 2; i++) {
//            bool invalidPoint = true;
//
//            for (int j = 0; j < 2; j++) {
//                RealVector point = left_vrs[2 * i + j];
//
////                validPoint = (!isnan(std::abs(point(0))) && !isnan(std::abs(point(1))));
//                  invalidPoint = ((point(0)!=point(0)) || ((point(1)!=point(1))));
//
//            }
//
//
//            if (!invalidPoint) {
//                newCurve.push_back(left_vrs[2 * i]);
//                newCurve.push_back(left_vrs[2 * i + 1]);
//            }
//
//        }


        cout << "Valor de origin" << origin << endl;\


        cout << "Ponto entrado: " << realVectorInput << endl;
        cout << "Direcao: " << timeDirection << endl;

        cout << "Reason" << reason_why << endl;
        cout << "Edge" << s << endl;



//        cout << "Nova curva" << endl;
//
//        for (int i = 0; i < newCurve.size(); i++) {
//            cout <<"i: "<<i<<" "<<newCurve[i] << endl;
//
//        }


        wavecurvefactory.wavecurve_from_inflection(left_vrs, realVectorInput, familyIndex, timeDirection, &hug, hwc, reason_why, edge);
    }









    jobject waveCurve = (env)->NewObject(classWaveCurve, waveCurveConstructor, familyIndex, timeDirection);


    jobject waveCurveBranchForward = env->NewObject(classWaveCurve, waveCurveConstructor, familyIndex, timeDirection); //First branch for now


    //
    for (int i = 0; i < hwc.wavecurve.size(); i++) {
        //
        Curve wc = hwc.wavecurve[i];
        std::vector<RealVector> coords = wc.curve;
        int relatedCurvesIndexVector = wc.back_curve_index;
        std::vector<int> correspondingPointIndexVector = wc.back_pointer;
        if (coords.size() > 0) {

            jobjectArray orbitPointArray = (jobjectArray) (env)->NewObjectArray(coords.size(), classOrbitPoint, NULL);
            for (int j = 0; j < coords.size(); j++) {

                RealVector tempVector = coords.at(j);
                double * dataCoords = tempVector;
                //Reading only coodinates
                jdoubleArray jTempArray = (env)->NewDoubleArray(tempVector.size());

                (env)->SetDoubleArrayRegion(jTempArray, 0, tempVector.size(), dataCoords);

                //Lambda is the last component.
                double lambda = 0;
                jobject orbitPoint = (env)->NewObject(classOrbitPoint, orbitPointConstructor, jTempArray, lambda);

                env->CallObjectMethod(orbitPoint, setCorrespondingCurveIndexID, relatedCurvesIndexVector);
                env->CallObjectMethod(orbitPoint, setCorrespondingPointIndexID, correspondingPointIndexVector[j]);
                (env)->SetObjectArrayElement(orbitPointArray, j, orbitPoint);


            }


            switch (wc.type) {
                case 1:
                {
                    jobject rarefactionOrbit = (env)->NewObject(classRarefactionOrbit, rarefactionOrbitConstructor, orbitPointArray, familyIndex, timeDirection);
                    env->CallVoidMethod(rarefactionOrbit, setCurveTypeID, 1);
                    env->CallVoidMethod(waveCurveBranchForward, waveCurveAddBranch, rarefactionOrbit);
                    env->CallVoidMethod(rarefactionOrbit, setCurveIndexID, i);
                    //                    env->CallVoidMethod(rarefactionOrbit, setInitialSubCurveID, curves[i].initial_subcurve);

                }
                    break;

                case 2:
                {
                    jobject compositeCurve = (env)->NewObject(classComposite, compositeConstructor, orbitPointArray, timeDirection, familyIndex);
                    env->CallVoidMethod(compositeCurve, setCurveTypeID, 2);
                    env->CallVoidMethod(waveCurveBranchForward, waveCurveAddBranch, compositeCurve);
                    env->CallVoidMethod(compositeCurve, setCurveIndexID, i);
                    //                    env->CallVoidMethod(compositeCurve, setInitialSubCurveID, curves[i].initial_subcurve);
                }
                    break;


                case 3:
                {
                    cout << "No shock" << endl;
                    jobject shockCurve = (env)->NewObject(shockCurveClass, shockCurveConstructor, orbitPointArray, familyIndex, timeDirection);
                    env->CallVoidMethod(shockCurve, setCurveTypeID, 3);
                    env->CallVoidMethod(waveCurveBranchForward, waveCurveAddBranch, shockCurve);
                    env->CallVoidMethod(shockCurve, setCurveIndexID, i);
                    //                    env->CallVoidMethod(shockCurve, setInitialSubCurveID, curves[i].initial_subcurve);
                }
                    break;

                default:

                    cout << "Tipo de curva nao conhecido !!" << endl;

            }

        } else {
            cout << "CURVA " << i << " VAZIA !! tipo: " << wc.type << endl;
        }
    }

    env->CallObjectMethod(waveCurve, waveCurveAddBranch, waveCurveBranchForward);

    return waveCurve;













}