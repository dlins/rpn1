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




JNIEXPORT jobject JNICALL Java_rpnumerics_WaveCurveCalc_nativeCalc
(JNIEnv * env, jobject obj, jobject initialPoint, jobject configuration) {



    unsigned int i;

    jclass classOrbitPoint = (env)->FindClass(ORBITPOINT_LOCATION);
    jclass classWaveCurve = (env)->FindClass(WAVECURVE_LOCATION);
    jclass arrayListClass = env->FindClass("java/util/ArrayList");
    jclass rpnCurveClass = env->FindClass(RPNCURVE_LOCATION);
    jclass classConfiguration = env->FindClass(CONFIGURATION_LOCATION);

    //     public void setXi(double [] xi)


    jclass fundamentalCurveClass = env->FindClass(FUNDAMENTALCURVE_LOCATION);


    jclass shockCurveClass = (env)->FindClass(SHOCKCURVE_LOCATION);
    jclass classRarefactionOrbit = (env)->FindClass(RAREFACTIONCURVE_LOCATION);
    jclass classComposite = (env)->FindClass(COMPOSITECURVE_LOCATION);
    jclass classWaveCurveOrbit = (env)->FindClass(WAVECURVEORBIT_LOCATION);
    jclass classWaveCurveBranch = (env)->FindClass(WAVECURVEBRANCH_LOCATION);




    jmethodID setXIMethodID = (env)->GetMethodID(fundamentalCurveClass, "setXi", "([D)V");

    jmethodID setCorrespondingCurveIndexID = (env)->GetMethodID(classOrbitPoint, "setCorrespondingCurveIndex", "(I)V");
    jmethodID setCorrespondingPointIndexID = (env)->GetMethodID(classOrbitPoint, "setCorrespondingPointIndex", "(I)V");
    jmethodID setLambdaID = (env)->GetMethodID(classOrbitPoint, "setLambda", "(D)V");
    jmethodID setIDMethodID = (env)->GetMethodID(rpnCurveClass, "setId", "(I)V");
    jmethodID getParamMethodID = (env)->GetMethodID(classConfiguration, "getParam", "(Ljava/lang/String;)Ljava/lang/String;");




    jmethodID setCurveTypeID = (env)->GetMethodID(classWaveCurveOrbit, "setCurveType", "(I)V");
    jmethodID setCurveIndexID = (env)->GetMethodID(classWaveCurveOrbit, "setCurveIndex", "(I)V");
    jmethodID setInitialSubCurveID = (env)->GetMethodID(classWaveCurveOrbit, "setInitialSubCurve", "(Z)V");
    jmethodID setReferencePointID = (env)->GetMethodID(classWaveCurveBranch, "setReferencePoint", "(Lrpnumerics/OrbitPoint;)V");


    jmethodID shockCurveConstructor = (env)->GetMethodID(shockCurveClass, "<init>", "([Lrpnumerics/OrbitPoint;II)V");
    jmethodID rarefactionOrbitConstructor = (env)->GetMethodID(classRarefactionOrbit, "<init>", "([Lrpnumerics/OrbitPoint;II)V");
    jmethodID compositeConstructor = (env)->GetMethodID(classComposite, "<init>", "([Lrpnumerics/OrbitPoint;II)V");



    jmethodID waveCurveConstructor = (env)->GetMethodID(classWaveCurve, "<init>", "(II)V");
    jmethodID orbitPointConstructor = (env)->GetMethodID(classOrbitPoint, "<init>", "([D[DD)V");
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


    RpNumerics::getPhysics().getSubPhysics(0).preProcess(realVectorInput);
    env->DeleteLocalRef(inputPhasePointArray);

    const Boundary * boundary = RpNumerics::getPhysics().getSubPhysics(0).getPreProcessedBoundary();

    const FluxFunction * flux = &RpNumerics::getPhysics().fluxFunction();

    //    cout << "Parametros na chamada: " << flux->fluxParams().params() << endl;

    const AccumulationFunction * accum = &RpNumerics::getPhysics().accumulation();

    RarefactionCurve rc(accum, flux, boundary);

    HugoniotContinuation * hug = RpNumerics::getPhysics().getSubPhysics(0).getHugoniotContinuationMethod();

    ShockCurve * shock = RpNumerics::getPhysics().getSubPhysics(0).getShockMethod();

    CompositeCurve * cmp = RpNumerics::getPhysics().getSubPhysics(0).getCompositeCurve();

    LSODE lsode;
    ODE_Solver *odesolver;
    odesolver = &lsode;

    int dimension = realVectorInput.size();


    WaveCurveFactory wavecurvefactory(accum, flux, boundary, odesolver, &rc, shock, cmp);



    jstring jedge = (jstring) env->CallObjectMethod(configuration, getParamMethodID, env->NewStringUTF("edge"));

    jstring jorigin = (jstring) env->CallObjectMethod(configuration, getParamMethodID, env->NewStringUTF("origin"));
    jstring jcurve = (jstring) env->CallObjectMethod(configuration, getParamMethodID, env->NewStringUTF("curve"));
    jstring jdirection = (jstring) env->CallObjectMethod(configuration, getParamMethodID, env->NewStringUTF("direction"));


    jstring jfamily = (jstring) env->CallObjectMethod(configuration, getParamMethodID, env->NewStringUTF("family"));


    string edge(env->GetStringUTFChars(jedge, NULL));
    string origin(env->GetStringUTFChars(jorigin, NULL));
    string curve(env->GetStringUTFChars(jcurve, NULL));
    string direction(env->GetStringUTFChars(jdirection, NULL));

    string family(env->GetStringUTFChars(jfamily, NULL));


    int timeDirection;
    std::stringstream stream(direction);
    stream >> timeDirection;


    int edgeNumber;
    std::stringstream streamEdge(edge);
    streamEdge >> edgeNumber;


    int originNumber;
    std::stringstream streamOrigin(origin);
    streamOrigin >> originNumber;


    int curveNumber;
    std::stringstream streamCurve(curve);
    streamCurve >> curveNumber;


    int familyNumber;
    std::stringstream streamFamily(family);
    streamFamily >> familyNumber;

    //
    //    cout << "Valor de origin" << originNumber << endl;
    //
    //    cout << "Ponto entrado: " << realVectorInput << endl;
    //
    //
    //    cout << "Curve index: " << curveNumber << endl;
    //    cout << "Direcao: " << timeDirection << endl;
    //    cout << "Family" << familyNumber << endl;
    //    cout << "Edge" << edgeNumber << endl;


    WaveCurve * hwc = new WaveCurve();

    int reason_why, s;


    if (originNumber == 11) {

        wavecurvefactory.wavecurve(realVectorInput, familyNumber, timeDirection, hug, *hwc, reason_why, s);
    }

    if ((originNumber == 1) || (originNumber == 2) || (originNumber == 3)) {
        wavecurvefactory.wavecurve_from_boundary(realVectorInput, edgeNumber, familyNumber, timeDirection, hug, *hwc, reason_why, s);
    }



    if (originNumber == 12) {

        Inflection_Curve inflectionCurve;

        std::vector<RealVector> left_vrs;

        GridValues * gv = RpNumerics::getGridFactory().getGrid("bifurcationcurve");

        inflectionCurve.curve(& RpNumerics::getPhysics().fluxFunction(), & RpNumerics::getPhysics().accumulation(), *gv, familyNumber, left_vrs);

        wavecurvefactory.wavecurve_from_inflection(left_vrs, realVectorInput, familyNumber, timeDirection, hug, *hwc, reason_why, s);
    }


    if (originNumber == 13) {

        WaveCurve * waveCurve = RpNumerics::getWaveCurve(curveNumber);
        wavecurvefactory.wavecurve_from_wavecurve(*waveCurve, realVectorInput, hug, *hwc, reason_why, s);

    }



//    for (int j = 0; j < hwc->wavecurve.size(); j++) {
//
//        Curve testeWC = hwc->wavecurve[j];
//
//        vector<double> xiVector = testeWC.xi;
//
//        cout <<"Curva: "<<j<<endl;
//
//        for (int n = 0; n < xiVector.size(); n++) {
//            cout << "xi=" << xiVector[n] << endl;
//        }
//
//
//
//
//
//    }















    double speedAtReferencePoint = hwc->reference_point.e[familyNumber].r;


    double nativeEigenValues [dimension];

    for (int i = 0; i < dimension; i++) {


        nativeEigenValues[i] = hwc->reference_point.e[i].r;

    }

    jdoubleArray eigenValuesArray = (env)->NewDoubleArray(dimension);

    (env)->SetDoubleArrayRegion(eigenValuesArray, 0, dimension, nativeEigenValues);


    jdoubleArray refPointCoords = (env)->NewDoubleArray(dimension);

    (env)->SetDoubleArrayRegion(refPointCoords, 0, dimension, (double *) hwc->reference_point.point);

    jobject referenceOrbitPoint = (env)->NewObject(classOrbitPoint, orbitPointConstructor, refPointCoords, eigenValuesArray, speedAtReferencePoint);



    cout << "Antes de adicionar curva de onda: " << RpNumerics::getCurrentCurveID() << endl;
    RpNumerics::addWaveCurve(hwc);

    jobject waveCurve = (env)->NewObject(classWaveCurve, waveCurveConstructor, familyNumber, timeDirection);

    env->CallVoidMethod(waveCurve, setIDMethodID, RpNumerics::getCurrentCurveID());

    RpNumerics::increaseCurveID();

    jobject waveCurveBranchForward = env->NewObject(classWaveCurve, waveCurveConstructor, familyNumber, timeDirection); //First branch for now







    for (int i = 0; i < hwc->wavecurve.size(); i++) {

        Curve wc = hwc->wavecurve[i];

        vector<double> xiVector = wc.xi;

        double nativeXi [xiVector.size()];

        for (int n = 0; n < xiVector.size(); n++) {
            nativeXi[n] = xiVector.at(n);

        }
        
        
        jdoubleArray xiArray = (env)->NewDoubleArray(xiVector.size());

        (env)->SetDoubleArrayRegion(xiArray, 0, xiVector.size(), nativeXi);

        std::vector<RealVector> coords = wc.curve;
        int relatedCurvesIndexVector = wc.back_curve_index;
        std::vector<int> correspondingPointIndexVector = wc.back_pointer;
        if (coords.size() > 0) {

            jobjectArray orbitPointArray = (jobjectArray) (env)->NewObjectArray(coords.size(), classOrbitPoint, NULL);
            for (int j = 0; j < coords.size(); j++) {

                RealVector tempVector = coords.at(j);


                RealVector resizedVector(tempVector);
                RpNumerics::getPhysics().getSubPhysics(0).postProcess(resizedVector);

                double * dataCoords = resizedVector;

                //Reading only coodinates
                jdoubleArray jTempArray = (env)->NewDoubleArray(tempVector.size());

                (env)->SetDoubleArrayRegion(jTempArray, 0, tempVector.size(), dataCoords);

                //Lambda is the last component.
                double speed = wc.speed[j];

                jdoubleArray jeigenValuesArray = (env)->NewDoubleArray(dimension);

                RealVector eigenValue = wc.eigenvalues[i];

                (env)->SetDoubleArrayRegion(jeigenValuesArray, 0, eigenValue.size(), (double *) eigenValue);

                jobject orbitPoint = (env)->NewObject(classOrbitPoint, orbitPointConstructor, jTempArray, jeigenValuesArray, speed);

                env->CallObjectMethod(orbitPoint, setCorrespondingCurveIndexID, relatedCurvesIndexVector);
                env->CallObjectMethod(orbitPoint, setCorrespondingPointIndexID, correspondingPointIndexVector[j]);
                (env)->SetObjectArrayElement(orbitPointArray, j, orbitPoint);


            }


            switch (wc.type) {
                case 1:
                {
                    jobject rarefactionOrbit = (env)->NewObject(classRarefactionOrbit, rarefactionOrbitConstructor, orbitPointArray, familyNumber, timeDirection);
                    env->CallVoidMethod(rarefactionOrbit, setXIMethodID, xiArray);
                    env->CallVoidMethod(rarefactionOrbit, setCurveTypeID, 1);
                    env->CallVoidMethod(waveCurveBranchForward, waveCurveAddBranch, rarefactionOrbit);
                    env->CallVoidMethod(rarefactionOrbit, setCurveIndexID, i);
                   

                }
                    break;

                case 2:
                {
                    jobject compositeCurve = (env)->NewObject(classComposite, compositeConstructor, orbitPointArray, timeDirection, familyNumber);
                    env->CallVoidMethod(compositeCurve, setXIMethodID, xiArray);
                    env->CallVoidMethod(compositeCurve, setCurveTypeID, 2);
                    env->CallVoidMethod(waveCurveBranchForward, waveCurveAddBranch, compositeCurve);
                    env->CallVoidMethod(compositeCurve, setCurveIndexID, i);

                }
                    break;


                case 3:
                {
                    //cout << "No shock" << endl;
                    jobject shockCurve = (env)->NewObject(shockCurveClass, shockCurveConstructor, orbitPointArray, familyNumber, timeDirection);
                    env->CallVoidMethod(shockCurve, setXIMethodID, xiArray);
                    env->CallVoidMethod(shockCurve, setCurveTypeID, 3);
                    env->CallVoidMethod(waveCurveBranchForward, waveCurveAddBranch, shockCurve);
                    env->CallVoidMethod(shockCurve, setCurveIndexID, i);

                }
                    break;

                default:

                    return NULL; //cout << "Tipo de curva nao conhecido !!" << endl;

            }

        } else {
            //cout << "CURVA " << i << " VAZIA !! tipo: " << wc.type << endl;
        }
    }

    env->CallObjectMethod(waveCurve, waveCurveAddBranch, waveCurveBranchForward);

    env->CallVoidMethod(waveCurve, setReferencePointID, referenceOrbitPoint);

    return waveCurve;

















}