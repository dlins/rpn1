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


#include "rpnumerics_HugoniotCurveCalcND.h"
#include "JNIDefs.h"
#include "RpNumerics.h"
#include <vector>
#include <iostream>
#include "ContourMethod.h"
#include "ReducedTPCWHugoniotFunctionClass.h"
#include "TPCW.h"
#include "StoneHugoniotFunctionClass.h"
#include "CoincidenceTPCW.h"
#include"SubinflectionTPCW.h"

using std::vector;
using namespace std;

JNIEXPORT void JNICALL Java_rpnumerics_HugoniotCurveCalcND_setUMinus
(JNIEnv * env, jobject obj, jobject uMinus) {

    printf("Seting UMinus\n");

}

JNIEXPORT jobject JNICALL Java_rpnumerics_HugoniotCurveCalcND_calc
(JNIEnv * env, jobject obj, jobject uMinus) {

    jclass classPhasePoint = (env)->FindClass(PHASEPOINT_LOCATION);

    jclass hugoniotSegmentClass = (env)->FindClass(HUGONIOTSEGMENTCLASS_LOCATION);

    jclass realVectorClass = env->FindClass(REALVECTOR_LOCATION);

    jclass arrayListClass = env->FindClass("java/util/ArrayList");

    jclass hugoniotCurveClass = env->FindClass(HUGONIOTCURVE_LOCATION);

    jmethodID toDoubleMethodID = (env)->GetMethodID(classPhasePoint, "toDouble", "()[D");
    jmethodID realVectorConstructorDoubleArray = env->GetMethodID(realVectorClass, "<init>", "([D)V");

    jmethodID hugoniotSegmentConstructor = (env)->GetMethodID(hugoniotSegmentClass, "<init>", "(Lwave/util/RealVector;DLwave/util/RealVector;DDDDDI)V");

    jmethodID arrayListConstructor = env->GetMethodID(arrayListClass, "<init>", "()V");
    jmethodID arrayListAddMethod = env->GetMethodID(arrayListClass, "add", "(Ljava/lang/Object;)Z");

    jmethodID hugoniotCurveConstructor = env->GetMethodID(hugoniotCurveClass, "<init>", "(Lrpnumerics/PhasePoint;Ljava/util/List;)V");

    //Input processing
    jdoubleArray phasePointArray = (jdoubleArray) (env)->CallObjectMethod(uMinus, toDoubleMethodID);

    int dimension = env->GetArrayLength(phasePointArray);

    double input [dimension];


    env->GetDoubleArrayRegion(phasePointArray, 0, dimension, input);

    env->DeleteLocalRef(phasePointArray);

    //Calculations using the input

    jobject segmentsArray = env->NewObject(arrayListClass, arrayListConstructor, NULL);


    //    Test testFunction;

    //-------------------------------------------------------------------


    RealVector Uref(dimension, input);

    cout << Uref << endl;


    //    for (unsigned int i = 0; i < dimension; i++) {
    //
    //        cout << "Valor de uref " << Uref.component(i) << endl;
    //
    //    }


    //    bool has_gravity = false;
    //    double Tref_rock = 273.15;
    //    double Tref_water = 274.3775;
    //    double pressure = 100.9;
    //    double Cr = 2.029e6;
    //    double Cw = 4297.;
    //    double rhoW_init = 998.2;
    //    double T_typical = 304.63;
    //    double Rho_typical = 998.2; // For the time being, this will be RhoWconst = 998 [kg/m^3]. In the future, this value should be the density of pure water at the temperature T_typical.
    //    double U_typical = 4.22e-6;
    //    double h_typical = Cw * (T_typical - Tref_water);

    //    Thermodynamics_SuperCO2_WaterAdimensionalized TD(Tref_rock, Tref_water, pressure,
    //            "/home/edsonlan/Java/rpn/src/c++/rpnumerics/physics/tpcw/rhosigmac_spline.txt",
    //            "/home/edsonlan/Java/rpn/src/c++/rpnumerics/physics/tpcw/rhosigmaw_spline.txt",
    //            "/home/edsonlan/Java/rpn/src/c++/rpnumerics/physics/tpcw/rhoac_spline.txt",
    //            "/home/edsonlan/Java/rpn/src/c++/rpnumerics/physics/tpcw/rhoaw_spline.txt",
    //            "/home/edsonlan/Java/rpn/src/c++/rpnumerics/physics/tpcw/rhoW_spline.txt",
    //            "/home/edsonlan/Java/rpn/src/c++/rpnumerics/physics/tpcw/hsigmaC_spline.txt",
    //            rhoW_init,
    //            Cr,
    //            Cw,
    //            T_typical,
    //            Rho_typical,
    //            h_typical,
    //            U_typical);




    //    double const_gravity = 9.8;
    //    double abs_perm = 3e-12;
    //    double phi = 0.38;
    //
    //    Thermodynamics_SuperCO2_WaterAdimensionalized TD(Physics::getRPnHome());
    //
    //
    //    double cnw = 0., cng = 0., expw = 2., expg = 2.;
    //    FracFlow2PhasesHorizontalAdimensionalized fh(cnw, cng, expw, expg, TD);
    //
    //
    //    ReducedTPCWHugoniotFunctionClass tpcwhc(Uref, abs_perm, phi, const_gravity, &TD, &fh);

    //    StoneHugoniotFunctionClass * hf = new StoneHugoniotFunctionClass(Uref, (const StoneFluxFunction &) RpNumerics::getPhysics().fluxFunction());
    //


    // Contour proper

    //    double rect[4];


    //    rect[0] = 0.0; // xmin
    //    rect[1] = 1.0; // xmax
    //    rect[2] = TD.T2Theta(300.0); // ymin
    //    rect[3] = TD.T2Theta(450.0); // ymax
    //
    //
    //    int res[2];
    //
    //    res[0] = 2;
    //    res[1] = 2;

    //    ContourMethod method(dimension, RpNumerics::getPhysics().fluxFunction(), RpNumerics::getPhysics().accumulation(), RpNumerics::getPhysics().boundary(), &tpcwhc);

    //        ContourMethod method(dimension, RpNumerics::getPhysics().fluxFunction(), RpNumerics::getPhysics().accumulation(), RpNumerics::getPhysics().boundary(), &coincidence);
    //    ContourMethod method(dimension, RpNumerics::getPhysics().fluxFunction(), RpNumerics::getPhysics().accumulation(), RpNumerics::getPhysics().boundary(), &inflection);

    //    RpNumerics::getPhysics().getSubPhysics(0).setHugoniotFunction(stoneHugoniotFunction);

    HugoniotFunctionClass *hf = RpNumerics::getPhysics().getSubPhysics(0).getHugoniotFunction();
    hf->setFluxFunction((FluxFunction *)RpNumerics::getPhysics().fluxFunction().clone());

    hf->setReferenceVector(Uref);

    ContourMethod method(dimension, RpNumerics::getPhysics().fluxFunction(), RpNumerics::getPhysics().accumulation(), RpNumerics::getPhysics().boundary(), hf); //stoneHugoniotFunction);


    const FluxFunction  &flux = RpNumerics::getPhysics().fluxFunction();

    for (int i = 0; i < flux.fluxParams().params().size(); i++) {


        cout<<"parametro de fluxo: "<<i<<" "<<flux.fluxParams().params().component(i)<<endl;


    }











    vector<HugoniotPolyLine> hugoniotPolyLineVector;

    method.classifiedCurve(Uref, hugoniotPolyLineVector);


    for (int i = 0; i < hugoniotPolyLineVector.size(); i++) {

        for (unsigned int j = 0; j < hugoniotPolyLineVector[i].vec.size() - 1; j++) {

            int m = (hugoniotPolyLineVector[i].vec[0].size() - dimension - 1) / 2; // Number of valid eigenvalues

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

            double leftLambda1 = hugoniotPolyLineVector[i].vec[j].component(dimension + m + 1);
            double leftLambda2 = hugoniotPolyLineVector[i].vec[j].component(dimension + m + 2);

            double rightLambda1 = hugoniotPolyLineVector[i].vec[j + 1].component(dimension + m + 1);
            double rightLambda2 = hugoniotPolyLineVector[i].vec[j + 1].component(dimension + m + 2);


            //            cout <<leftLambda1<<" "<<leftLambda2<<" "<<rightLambda1<<" "<<rightLambda2<<endl;

            //            cout<<"Antes de criar hugoniot segment"<<endl;
            jobject hugoniotSegment = env->NewObject(hugoniotSegmentClass, hugoniotSegmentConstructor, realVectorLeftPoint, leftSigma, realVectorRightPoint, rightSigma, leftLambda1, leftLambda2, rightLambda1, rightLambda2, pointType);
            env->CallObjectMethod(segmentsArray, arrayListAddMethod, hugoniotSegment);

        }


    }



    jobject result = env->NewObject(hugoniotCurveClass, hugoniotCurveConstructor, uMinus, segmentsArray);

    // Limpando


    env->DeleteLocalRef(hugoniotSegmentClass);
    env->DeleteLocalRef(realVectorClass);
    env->DeleteLocalRef(arrayListClass);




    return result;


}
















