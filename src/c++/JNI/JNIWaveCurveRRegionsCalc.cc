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
#include "rpnumerics_WaveCurveRRegionsCalc.h"

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
#include "WaveCurve.h"



using std::vector;

/*
 * ---------------------------------------------------------------
 * Definitions:
 */

/*
 * Class:     rpnumerics_WaveCurveRRegionsCalc
 * Method:    nativeCalc
 * Signature: (I)Lrpnumerics/RpSolution;
 */
JNIEXPORT jobject JNICALL Java_rpnumerics_WaveCurveRRegionsCalc_nativeCalc
(JNIEnv * env, jobject obj, jint waveCurveID) {

    //cout<<"No JNI RRegions"<<endl;

    unsigned int i;

    jclass classOrbitPoint = (env)->FindClass(ORBITPOINT_LOCATION);
    jclass classWaveCurve = (env)->FindClass(WAVECURVE_LOCATION);
    jclass arrayListClass = env->FindClass("java/util/ArrayList");
    jclass rpnCurveClass = env->FindClass(RPNCURVE_LOCATION);
    jclass classConfiguration = env->FindClass(CONFIGURATION_LOCATION);


    jclass shockCurveClass = (env)->FindClass(SHOCKCURVE_LOCATION);
    jclass classRarefactionOrbit = (env)->FindClass(RAREFACTIONCURVE_LOCATION);
    jclass classComposite = (env)->FindClass(COMPOSITECURVE_LOCATION);
    jclass classWaveCurveOrbit = (env)->FindClass(WAVECURVEORBIT_LOCATION);
    jclass classWaveCurveRRegions = env->FindClass(WAVECURVERREGIONS_LOCATION);



    jmethodID setCorrespondingCurveIndexID = (env)->GetMethodID(classOrbitPoint, "setCorrespondingCurveIndex", "(I)V");
    jmethodID setCorrespondingPointIndexID = (env)->GetMethodID(classOrbitPoint, "setCorrespondingPointIndex", "(I)V");
    jmethodID setLambdaID = (env)->GetMethodID(classOrbitPoint, "setLambda", "(D)V");
    jmethodID setIDMethodID = (env)->GetMethodID(rpnCurveClass, "setId", "(I)V");
    jmethodID getParamMethodID = (env)->GetMethodID(classConfiguration, "getParam", "(Ljava/lang/String;)Ljava/lang/String;");





    jmethodID setCurveTypeID = (env)->GetMethodID(classWaveCurveOrbit, "setCurveType", "(I)V");
    jmethodID setCurveIndexID = (env)->GetMethodID(classWaveCurveOrbit, "setCurveIndex", "(I)V");
    jmethodID setInitialSubCurveID = (env)->GetMethodID(classWaveCurveOrbit, "setInitialSubCurve", "(Z)V");




    jmethodID shockCurveConstructor = (env)->GetMethodID(shockCurveClass, "<init>", "([Lrpnumerics/OrbitPoint;II)V");
    jmethodID rarefactionOrbitConstructor = (env)->GetMethodID(classRarefactionOrbit, "<init>", "([Lrpnumerics/OrbitPoint;II)V");
    jmethodID compositeConstructor = (env)->GetMethodID(classComposite, "<init>", "([Lrpnumerics/OrbitPoint;II)V");


    jmethodID waveCurveRRegionsConstructor = (env)->GetMethodID(classWaveCurveRRegions, "<init>", "(Ljava/util/List;)V");
    jmethodID waveCurveConstructor = (env)->GetMethodID(classWaveCurve, "<init>", "(II)V");
    jmethodID orbitPointConstructor = (env)->GetMethodID(classOrbitPoint, "<init>", "([DD)V");
    jmethodID toDoubleMethodID = (env)->GetMethodID(classOrbitPoint, "toDouble", "()[D");



    jmethodID waveCurveAddBranch = (env)->GetMethodID(classWaveCurve, "add", "(Lrpnumerics/WaveCurveBranch;)V");


    jmethodID arrayListConstructor = env->GetMethodID(arrayListClass, "<init>", "()V");
    jmethodID arrayListAddMethod = env->GetMethodID(arrayListClass, "add", "(Ljava/lang/Object;)Z");



    const Boundary * boundary = &RpNumerics::getPhysics().boundary();

    const FluxFunction * flux = &RpNumerics::getPhysics().fluxFunction();

    //cout << "Parametros na chamada: " << flux->fluxParams().params() << endl;

    const AccumulationFunction * accum = &RpNumerics::getPhysics().accumulation();

    RarefactionCurve rc(accum, flux, boundary);

    HugoniotContinuation_nDnD hug(flux, accum, boundary);
    ShockCurve sc(&hug);

    CompositeCurve cmp(accum, flux, boundary, &sc, 0);

    LSODE lsode;
    ODE_Solver *odesolver;
    odesolver = &lsode;



    vector<WaveCurve> waveCurveVector;


    //cout<<"Antes de waveCurve: "<<waveCurveID<<endl;
    const WaveCurve * waveCurve = RpNumerics::getWaveCurve(waveCurveID);
    //cout<<"Depois de waveCurve"<<endl;

    WaveCurveFactory wavecurvefactory(accum, flux, boundary, odesolver, &rc, &sc, &cmp);


    wavecurvefactory.R_regions(&hug, *waveCurve, waveCurveVector);




    if (waveCurveVector.size() == 0) {
        return NULL;
    }

    jobject waveCurveArray = env->NewObject(arrayListClass, arrayListConstructor, NULL);

    for (int n = 0; n < waveCurveVector.size(); n++) {
        WaveCurve nativeWaveCurve = waveCurveVector[n];



        RpNumerics::addWaveCurve(new WaveCurve(waveCurveVector[n]));


        jobject waveCurve = (env)->NewObject(classWaveCurve, waveCurveConstructor, nativeWaveCurve.family, nativeWaveCurve.increase);


        env->CallVoidMethod(waveCurve, setIDMethodID, RpNumerics::getCurrentCurveID());

        RpNumerics::increaseCurveID();




        jobject waveCurveBranchForward = env->NewObject(classWaveCurve, waveCurveConstructor, nativeWaveCurve.family, nativeWaveCurve.increase); //First branch for now


        //
        for (int i = 0; i < nativeWaveCurve.wavecurve.size(); i++) {
            //
            Curve wc = nativeWaveCurve.wavecurve[i];
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
                    double lambda = wc.speed[j];





                    jobject orbitPoint = (env)->NewObject(classOrbitPoint, orbitPointConstructor, jTempArray, lambda);

                    env->CallObjectMethod(orbitPoint, setCorrespondingCurveIndexID, relatedCurvesIndexVector);
                    env->CallObjectMethod(orbitPoint, setCorrespondingPointIndexID, correspondingPointIndexVector[j]);
                    (env)->SetObjectArrayElement(orbitPointArray, j, orbitPoint);


                }


                switch (wc.type) {
                    case 1:
                    {
                        jobject rarefactionOrbit = (env)->NewObject(classRarefactionOrbit, rarefactionOrbitConstructor, orbitPointArray, nativeWaveCurve.family, nativeWaveCurve.increase);
                        env->CallVoidMethod(rarefactionOrbit, setCurveTypeID, 1);
                        env->CallVoidMethod(waveCurveBranchForward, waveCurveAddBranch, rarefactionOrbit);
                        env->CallVoidMethod(rarefactionOrbit, setCurveIndexID, i);
                        //                    env->CallVoidMethod(rarefactionOrbit, setInitialSubCurveID, curves[i].initial_subcurve);

                    }
                        break;

                    case 2:
                    {
                        jobject compositeCurve = (env)->NewObject(classComposite, compositeConstructor, orbitPointArray, nativeWaveCurve.family, nativeWaveCurve.increase);
                        env->CallVoidMethod(compositeCurve, setCurveTypeID, 2);
                        env->CallVoidMethod(waveCurveBranchForward, waveCurveAddBranch, compositeCurve);
                        env->CallVoidMethod(compositeCurve, setCurveIndexID, i);
                        //                    env->CallVoidMethod(compositeCurve, setInitialSubCurveID, curves[i].initial_subcurve);
                    }
                        break;


                    case 3:
                    {
                        //cout << "No shock" << endl;
                        jobject shockCurve = (env)->NewObject(shockCurveClass, shockCurveConstructor, orbitPointArray,nativeWaveCurve.family, nativeWaveCurve.increase);
                        env->CallVoidMethod(shockCurve, setCurveTypeID, 3);
                        env->CallVoidMethod(waveCurveBranchForward, waveCurveAddBranch, shockCurve);
                        env->CallVoidMethod(shockCurve, setCurveIndexID, i);
                        //                    env->CallVoidMethod(shockCurve, setInitialSubCurveID, curves[i].initial_subcurve);
                    }
                        break;

                    default:

                        return NULL;//cout << "Tipo de curva nao conhecido !!" << endl;

                }

            } else {
                //cout << "CURVA " << i << " VAZIA !! tipo: " << wc.type << endl;
            }
        }

        env->CallObjectMethod(waveCurve, waveCurveAddBranch, waveCurveBranchForward);

        env->CallObjectMethod(waveCurveArray, arrayListAddMethod, waveCurve);



    }
    
    
    jobject waveCurveRRegions = (env)->NewObject(classWaveCurveRRegions, waveCurveRRegionsConstructor, waveCurveArray);
    
    
    return waveCurveRRegions;

















}