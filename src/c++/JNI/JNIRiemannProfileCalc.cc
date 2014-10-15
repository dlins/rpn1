/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) JNIHugoniotCurveCalc.cc
 **/



/*!
	
TODO:
	
NOTE : 

@ingroup JNI
 */

#include "rpnumerics_RiemannProfileCalc.h"
#include "RiemannProblem.h"
#include "WaveCurveFactory.h"
#include "JNIDefs.h"
#include "RpNumerics.h"
#include <vector>
#include <iostream>


using std::vector;
using namespace std;

JNIEXPORT jobject JNICALL Java_rpnumerics_RiemannProfileCalc_nativeCalc
(JNIEnv * env, jobject obj, jobject pmin, jobject pmax, jintArray waveCurveIDArray) {



    jclass arrayListClass = env->FindClass("java/util/ArrayList");
    jclass realVectorClass = env->FindClass(REALVECTOR_LOCATION);
    jclass diagramClass = env->FindClass(DIAGRAM_LOCATION);
    jclass diagramLineClass = env->FindClass(DIAGRAMLINE_LOCATION);


    jmethodID diagramConstructor = env->GetMethodID(diagramClass, "<init>", "(Ljava/util/List;)V");
    jmethodID diagramLineDefaultConstructor = env->GetMethodID(diagramLineClass, "<init>", "()V");




    jmethodID diagramSetInfoMethod = env->GetMethodID(diagramClass, "setInfo", "(Ljava/lang/String;)V");
    jmethodID addPartMethodID = env->GetMethodID(diagramLineClass, "addPart", "(Ljava/util/List;)V");
    jmethodID setTypeMethodID = env->GetMethodID(diagramLineClass, "setType", "(II)V");
    jmethodID arrayListConstructor = env->GetMethodID(arrayListClass, "<init>", "()V");
    jmethodID realVectorConstructorDoubleArray = env->GetMethodID(realVectorClass, "<init>", "([D)V");
    jmethodID toDoubleMethodID = (env)->GetMethodID(realVectorClass, "toDouble", "()[D");
    jmethodID arrayListAddMethod = env->GetMethodID(arrayListClass, "add", "(Ljava/lang/Object;)Z");

    int dimension = 2;

    double limitsBuffer[dimension];

    //Processing downLeft area limit


    jdoubleArray downLeftArray = (jdoubleArray) env->CallObjectMethod(pmin, toDoubleMethodID);
    env->GetDoubleArrayRegion(downLeftArray, 0, env->GetArrayLength(downLeftArray), limitsBuffer);
    RealVector nativeDownLeft(dimension, limitsBuffer);

    //Processing topRight area limit


    jdoubleArray topRightArray = (jdoubleArray) env->CallObjectMethod(pmax, toDoubleMethodID);
    env->GetDoubleArrayRegion(topRightArray, 0, env->GetArrayLength(topRightArray), limitsBuffer);
    RealVector nativeTopRight(dimension, limitsBuffer);

    //Processing forward wavecurve orbit list


    int waveCurves = env->GetArrayLength(waveCurveIDArray);

    int nativeWaveCurvesIDArray [2];


    env->GetIntArrayRegion(waveCurveIDArray, 0, waveCurves, nativeWaveCurvesIDArray);


    cout << "ID da curva 0 " << nativeWaveCurvesIDArray[0] << endl;
    cout << "ID da curva 1 " << nativeWaveCurvesIDArray[1] << endl;




    cout << RpNumerics::getWaveCurve(nativeWaveCurvesIDArray[0]) << endl;
    cout << RpNumerics::getWaveCurve(nativeWaveCurvesIDArray[1]) << endl;



    const WaveCurve * waveCurve1 = RpNumerics::getWaveCurve(nativeWaveCurvesIDArray[0]);
    const WaveCurve * waveCurve2 = RpNumerics::getWaveCurve(nativeWaveCurvesIDArray[1]);


    cout << "Curva 0 " << waveCurve1 << waveCurve1->reference_point.point << endl;
    cout << "Curva 1 " << waveCurve2 << waveCurve2->reference_point.point << endl;



    RealVector intersectionPoint(dimension);
    int subcurve1;
    int subcurve2;

    int subPoint1;
    int subPoint2;


//
//    if (RpNumerics::getPhysics().domain().dim() == 3) {
//        nativeDownLeft.resize(3);
//        nativeDownLeft.component(2) = 1.0;
//        nativeTopRight.resize(3);
//        nativeTopRight.component(2) = 1.0;
//
//    }



    cout << "AREA: " << nativeDownLeft << " " << nativeTopRight << endl;


    int intersectionCode = WaveCurveFactory::intersection(*waveCurve1, *waveCurve2, nativeDownLeft, nativeTopRight,
            intersectionPoint, subcurve1, subPoint1, subcurve2, subPoint2);


    stringstream interPointStream;

    interPointStream << intersectionPoint;


    cout << "Ponto R em C: " << interPointStream.str().c_str() << endl;

    jstring interPointString = env->NewStringUTF(interPointStream.str().c_str());






    if (intersectionCode == WAVE_CURVE_INTERSECTION_NOT_FOUND) {
        cout << "Intersection not found !" << endl;
        return NULL;
    }


    RiemannProblem riemanProblem;

    vector<RealVector> profile;
    vector<double> speedVector;

    riemanProblem.profile(*waveCurve1, subcurve1, subPoint1, 0,
            *waveCurve2, subcurve2, subPoint2, 1,
            profile, speedVector);

    if (profile.size() == 0) {
        return NULL;
    }


    //        
    //                cout << "Coordenadas do perfil" << endl;
    //                for (int i = 0; i < profile.size(); i++) {
    //                    
    ////                    cout<<"Tamanho de um ponto do profile: "<<profile.at(i).size()<<endl;
    //    //                cout  << profile.at(i)(0) << " " << profile.at(i)(1) << endl;
    //                    cout  << profile.at(i) << endl;
    //                }
    ////                for (int i = 0; i < speedVector.size(); i++) {
    ////            
    ////                    cout  << speedVector.at(i)<< endl;
    ////            
    ////                }
    ////



    int numberOfLines = profile.at(0).size();


    jobject diagramLinesList = env->NewObject(arrayListClass, arrayListConstructor, NULL); //Linhas do diagrama


    for (int i = 0; i < numberOfLines; i++) {

        jobject speedLine = env->NewObject(diagramLineClass, diagramLineDefaultConstructor, NULL);

        jobject speedLinePartList = env->NewObject(arrayListClass, arrayListConstructor, NULL);

        for (int j = 0; j < profile.size(); j++) {


            RealVector profileCoords(2);

            profileCoords.component(0) = speedVector[j];
            profileCoords.component(1) = profile[j].component(i);


            jdoubleArray speedArray = env->NewDoubleArray(2);
            env->SetDoubleArrayRegion(speedArray, 0, dimension, (double *) profileCoords);
            jobject realVector = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, speedArray);
            env->CallObjectMethod(speedLinePartList, arrayListAddMethod, realVector);


        }


        env->CallObjectMethod(speedLine, addPartMethodID, speedLinePartList);
        env->CallObjectMethod(speedLine, setTypeMethodID, 0, i); //Setando o tipo 
        env->CallObjectMethod(diagramLinesList, arrayListAddMethod, speedLine);


    }


    jobject diagram = (env)->NewObject(diagramClass, diagramConstructor, diagramLinesList);


    env->CallObjectMethod(diagram, diagramSetInfoMethod, interPointString);
        
    return diagram;


}

/*
 * Class:     rpnumerics_RiemannProfileCalc
 * Method:    nativeAllProfileCalc
 * Signature: (IILwave/util/RealVector;Lwave/util/RealVector;)Lrpnumerics/RpSolution;
 */
JNIEXPORT jobject JNICALL Java_rpnumerics_RiemannProfileCalc_nativeAllProfileCalc
(JNIEnv * env, jobject obj, jint firstWaveCurveID, jint secondWaveCurveID, jobject secondWaveCurveRefPoint, jobject pointOnSecondWaveCurve) {

    jclass realVectorClass = env->FindClass(REALVECTOR_LOCATION);
    jclass arrayListClass = env->FindClass("java/util/ArrayList");
    jclass diagramClass = env->FindClass(DIAGRAM_LOCATION);
    jclass diagramLineClass = env->FindClass(DIAGRAMLINE_LOCATION);


    jmethodID diagramConstructor = env->GetMethodID(diagramClass, "<init>", "(Ljava/util/List;)V");
    jmethodID diagramLineDefaultConstructor = env->GetMethodID(diagramLineClass, "<init>", "()V");
    jmethodID addPartMethodID = env->GetMethodID(diagramLineClass, "addPart", "(Ljava/util/List;)V");


    jmethodID setTypeMethodID = env->GetMethodID(diagramLineClass, "setType", "(II)V");
    jmethodID arrayListAddMethod = env->GetMethodID(arrayListClass, "add", "(Ljava/lang/Object;)Z");


    jmethodID arrayListConstructor = env->GetMethodID(arrayListClass, "<init>", "()V");
    jmethodID realVectorConstructorDoubleArray = env->GetMethodID(realVectorClass, "<init>", "([D)V");



    jmethodID toDoubleMethodID = (env)->GetMethodID(realVectorClass, "toDouble", "()[D");

    int dimension = 2;

    cout << "ID da curva 0 " << firstWaveCurveID << endl;
    cout << "ID da curva 1 " << secondWaveCurveID << endl;


    const WaveCurve * waveCurve1 = RpNumerics::getWaveCurve(firstWaveCurveID);
    const WaveCurve * waveCurve2 = RpNumerics::getWaveCurve(secondWaveCurveID);


    cout << "Curva 0 " << waveCurve1 << endl;
    cout << "Curva 1 " << waveCurve2 << endl;


    jdoubleArray inputPhasePointArray = (jdoubleArray) (env)->CallObjectMethod(secondWaveCurveRefPoint, toDoubleMethodID);

    double input [env->GetArrayLength(inputPhasePointArray)];

    env->GetDoubleArrayRegion(inputPhasePointArray, 0, env->GetArrayLength(inputPhasePointArray), input);

    RealVector nativesecondWaveCurveRefPoint(env->GetArrayLength(inputPhasePointArray));

    for (unsigned int i = 0; i < (unsigned int) nativesecondWaveCurveRefPoint.size(); i++) {
        nativesecondWaveCurveRefPoint.component(i) = input[i];
    }



    jdoubleArray inputPhasePointArray2 = (jdoubleArray) (env)->CallObjectMethod(pointOnSecondWaveCurve, toDoubleMethodID);

    double input2 [env->GetArrayLength(inputPhasePointArray2)];

    env->GetDoubleArrayRegion(inputPhasePointArray2, 0, env->GetArrayLength(inputPhasePointArray2), input2);

    RealVector nativePointOnSecondWaveCurve(env->GetArrayLength(inputPhasePointArray2));

    for (unsigned int i = 0; i < (unsigned int) nativePointOnSecondWaveCurve.size(); i++) {
        nativePointOnSecondWaveCurve.component(i) = input2[i];
    }



    cout << "Ponto sobre a segunda curva de onda: " << nativePointOnSecondWaveCurve << endl;

    int firstWaveCurveSubIndex;
    int firstWaveCurveSegmentIndex;

    int secondWaveCurveSubIndex;
    int secondWaveCurveSegmentIndex;

    RealVector R;
    double speed_on_point;
    

    Utilities::pick_point_from_wavecurve(*waveCurve1, waveCurve2->reference_point.point, firstWaveCurveSubIndex, firstWaveCurveSegmentIndex, R, speed_on_point);

    Utilities::pick_point_from_wavecurve(*waveCurve2, nativePointOnSecondWaveCurve, secondWaveCurveSubIndex, secondWaveCurveSegmentIndex, R, speed_on_point);

    RiemannProblem rp;

    vector<RealVector> profile;
    vector<double> speedVector;

    rp.all_increase_profile(*waveCurve1, firstWaveCurveSubIndex, firstWaveCurveSegmentIndex, 0,
            *waveCurve2, secondWaveCurveSubIndex, secondWaveCurveSegmentIndex, 1,
            profile, speedVector);




    int numberOfLines = profile.at(0).size();


    jobject diagramLinesList = env->NewObject(arrayListClass, arrayListConstructor, NULL); //Linhas do diagrama


    for (int i = 0; i < numberOfLines; i++) {

        jobject speedLine = env->NewObject(diagramLineClass, diagramLineDefaultConstructor, NULL);

        jobject speedLinePartList = env->NewObject(arrayListClass, arrayListConstructor, NULL);

        for (int j = 0; j < profile.size(); j++) {


            RealVector profileCoords(2);

            profileCoords.component(0) = speedVector[j];
            profileCoords.component(1) = profile[j].component(i);

            //        RealVector tempVector = profile.at(i);
            //        tempVector.resize(dimension + 1);
            //        tempVector[dimension] = speedVector[i];

//            cout << profileCoords << endl;



            jdoubleArray speedArray = env->NewDoubleArray(dimension);
            env->SetDoubleArrayRegion(speedArray, 0, dimension, (double *) profileCoords);
            jobject realVector = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, speedArray);
            env->CallObjectMethod(speedLinePartList, arrayListAddMethod, realVector);


        }


        env->CallObjectMethod(speedLine, addPartMethodID, speedLinePartList);
        env->CallObjectMethod(speedLine, setTypeMethodID, 0, i); //Setando o tipo 
        env->CallObjectMethod(diagramLinesList, arrayListAddMethod, speedLine);

    }


    jobject diagram = (env)->NewObject(diagramClass, diagramConstructor, diagramLinesList);


    return diagram;



}












