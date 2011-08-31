/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) JNIHugoniotCurveCalc.cc
 **/


//! Definition of JNIHugoniotCurveCalc
/*!
	
TODO:
	
NOTE : 

@ingroup JNI
 */


#include "rpnumerics_CoincidenceCurveCalc.h"
#include "JNIDefs.h"
#include "RpNumerics.h"
#include <vector>
#include <iostream>
#include "ContourMethod.h"
#include "CoincidenceTPCW.h"


using std::vector;
using namespace std;

JNIEXPORT jobject JNICALL Java_rpnumerics_CoincidenceCurveCalc_nativeCalc(JNIEnv * env, jobject obj) {

    cout << "Em coincidence nativo: " << endl;

    jclass classPhasePoint = (env)->FindClass(PHASEPOINT_LOCATION);

    jclass hugoniotSegmentClass = (env)->FindClass(HUGONIOTSEGMENTCLASS_LOCATION);

    jclass realVectorClass = env->FindClass(REALVECTOR_LOCATION);

    jclass arrayListClass = env->FindClass("java/util/ArrayList");

    jclass coincidenceCurveClass = env->FindClass(COINCIDENCECURVE_LOCATION);

    jmethodID toDoubleMethodID = (env)->GetMethodID(classPhasePoint, "toDouble", "()[D");
    jmethodID realVectorConstructorDoubleArray = env->GetMethodID(realVectorClass, "<init>", "([D)V");
    jmethodID hugoniotSegmentConstructor = (env)->GetMethodID(hugoniotSegmentClass, "<init>", "(Lwave/util/RealVector;DLwave/util/RealVector;DI)V");
    jmethodID arrayListConstructor = env->GetMethodID(arrayListClass, "<init>", "()V");
    jmethodID arrayListAddMethod = env->GetMethodID(arrayListClass, "add", "(Ljava/lang/Object;)Z");
    //    jmethodID hugoniotPointTypeConstructor = (env)->GetMethodID(hugoniotPointTypeClass, "<init>", "([D[D)V");
    jmethodID coincidenceCurveConstructor = env->GetMethodID(coincidenceCurveClass, "<init>", "(Ljava/util/List;)V");


    //Input processing
    //    jdoubleArray phasePointArray = (jdoubleArray) (env)->CallObjectMethod(uMinus, toDoubleMethodID);
    //
    //    int dimension = env->GetArrayLength(phasePointArray);
    //
    //    double input [dimension];
    //
    //
    //    env->GetDoubleArrayRegion(phasePointArray, 0, dimension, input);
    //
    //    env->DeleteLocalRef(phasePointArray);
    //
    //Calculations using the input

    jobject segmentsArray = env->NewObject(arrayListClass, arrayListConstructor, NULL);


    //    Test testFunction;
    int dimension = 3;
    //-------------------------------------------------------------------
    RealVector Uref(dimension);

//    double phi = 0.38;


//    double T_Typical = 304.63;
//    double Rho_typical = 998.2;
//    double U_typical = 4.22e-3;



//    Thermodynamics_SuperCO2_WaterAdimensionalized TD(Physics::getRPnHome(), T_Typical, Rho_typical, U_typical);

    //    Thermodynamics_SuperCO2_WaterAdimensionalized TD(Physics::getRPnHome());

//    double cnw = 0., cng = 0., expw = 2., expg = 2.;
//    FracFlow2PhasesHorizontalAdimensionalized fh(cnw, cng, expw, expg, &TD);


    TPCW & tpcw = (TPCW &) RpNumerics::getPhysics().getSubPhysics(0);
    const Boundary & physicsBoundary = RpNumerics::getPhysics().boundary();

     Flux2Comp2PhasesAdimensionalized * fluxFunction = (Flux2Comp2PhasesAdimensionalized *) & tpcw.fluxFunction();

    Accum2Comp2PhasesAdimensionalized * accumulationFunction = (Accum2Comp2PhasesAdimensionalized *) & tpcw.accumulation();

    CoincidenceTPCW coincidenceFunction((Flux2Comp2PhasesAdimensionalized*) & tpcw.fluxFunction(), (Accum2Comp2PhasesAdimensionalized*) & tpcw.accumulation());


    RealVector min(physicsBoundary. minimums());
    RealVector max(physicsBoundary. maximums());

    cout << "Valor de min antes:" << min << endl;
    cout << "Valor de max antes:" << max << endl;

    tpcw.preProcess(min);
    tpcw.preProcess(max);

    cout << "Valor de min depois:" << min << endl;
    cout << "Valor de max depois:" << max << endl;

    RectBoundary tempBoundary(min, max);

    ContourMethod method(dimension, RpNumerics::getPhysics().fluxFunction(), RpNumerics::getPhysics().accumulation(), tempBoundary, &coincidenceFunction);

    vector<HugoniotPolyLine> hugoniotPolyLineVector;
    method.unclassifiedCurve(Uref, hugoniotPolyLineVector);

    RealVector minDimension(RpNumerics::getPhysics().boundary().minimums());
    RealVector maxDimension(RpNumerics::getPhysics().boundary().maximums());

    for (int i = 0; i < hugoniotPolyLineVector.size(); i++) {

        for (unsigned int j = 0; j < hugoniotPolyLineVector[i].vec.size() - 1; j++) {

            int m = (hugoniotPolyLineVector[i].vec[0].size() - dimension - 1) / 2; // Number of valid eigenvalues


            hugoniotPolyLineVector[i].vec[j].component(2) = maxDimension.component(2);
            hugoniotPolyLineVector[i].vec[j + 1].component(2) = maxDimension.component(2);

           tpcw.postProcess(hugoniotPolyLineVector[i].vec);

            //
            //            cout << "type of " << j << " = " << hugoniotPolyLineVector[i].type << endl;
            //            cout << "coord 1 " << j << " = " << hugoniotPolyLineVector[i].vec[j] << endl;
            //            cout << "coord 2 " << j + 1 << " = " << hugoniotPolyLineVector[i].vec[j + 1] << endl;

            jdoubleArray eigenValRLeft = env->NewDoubleArray(dimension);
            jdoubleArray eigenValRRight = env->NewDoubleArray(dimension);

            double * leftCoords = (double *) hugoniotPolyLineVector[i].vec[j];
            double * rightCoords = (double *) hugoniotPolyLineVector[i].vec[j + 1];

            env->SetDoubleArrayRegion(eigenValRLeft, 0, dimension, leftCoords);
            env->SetDoubleArrayRegion(eigenValRRight, 0, dimension, rightCoords);


            //Construindo left e right points
            jobject realVectorLeftPoint = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, eigenValRLeft);
            jobject realVectorRightPoint = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, eigenValRRight);

            int pointType = hugoniotPolyLineVector[i].type;

            double leftSigma = hugoniotPolyLineVector[i].vec[j].component(dimension + m);
            double rightSigma = hugoniotPolyLineVector[i].vec[j + 1].component(dimension + m);

            //            double leftSigma = 0;
            //            double rightSigma = 0;
            //

            //            cout<<"Antes de criar hugoniot segment"<<endl;
            jobject hugoniotSegment = env->NewObject(hugoniotSegmentClass, hugoniotSegmentConstructor, realVectorLeftPoint, leftSigma, realVectorRightPoint, rightSigma, 17);
            env->CallObjectMethod(segmentsArray, arrayListAddMethod, hugoniotSegment);

        }


    }



    // Limpando

    //        env->DeleteLocalRef(realVectorLeftPoint);

    //        env->DeleteLocalRef(realVectorRightPoint);

    //        env->DeleteLocalRef(hugoniotSegment);





    jobject result = env->NewObject(coincidenceCurveClass, coincidenceCurveConstructor, segmentsArray);

    //    env->DeleteLocalRef(eigenValRLeft);
    //    env->DeleteLocalRef(eigenValRRight);
    env->DeleteLocalRef(hugoniotSegmentClass);
    env->DeleteLocalRef(realVectorClass);
    env->DeleteLocalRef(arrayListClass);



    return result;


}
















