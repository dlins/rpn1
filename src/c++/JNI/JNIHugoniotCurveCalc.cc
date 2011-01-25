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

    //    jclass hugoniotPointTypeClass = (env)->FindClass(HUGONIOTPOINTTYPE_LOCATION);

    jclass hugoniotCurveClass = env->FindClass(HUGONIOTCURVE_LOCATION);

    jmethodID toDoubleMethodID = (env)->GetMethodID(classPhasePoint, "toDouble", "()[D");
    jmethodID realVectorConstructorDoubleArray = env->GetMethodID(realVectorClass, "<init>", "([D)V");
    jmethodID hugoniotSegmentConstructor = (env)->GetMethodID(hugoniotSegmentClass, "<init>", "(Lwave/util/RealVector;DLwave/util/RealVector;DI)V");
    jmethodID arrayListConstructor = env->GetMethodID(arrayListClass, "<init>", "()V");
    jmethodID arrayListAddMethod = env->GetMethodID(arrayListClass, "add", "(Ljava/lang/Object;)Z");
    //    jmethodID hugoniotPointTypeConstructor = (env)->GetMethodID(hugoniotPointTypeClass, "<init>", "([D[D)V");
    jmethodID hugoniotCurveConstructor = env->GetMethodID(hugoniotCurveClass, "<init>", "(Lrpnumerics/PhasePoint;Ljava/util/List;)V");

    //    int i;

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




    double const_gravity = 9.8;
    double abs_perm = 3e-12;
    double phi = 0.38;
    
    Thermodynamics_SuperCO2_WaterAdimensionalized TD(Physics::getRPnHome());


    double cnw = 0., cng = 0., expw = 2., expg = 2.;
    FracFlow2PhasesHorizontalAdimensionalized fh(cnw, cng, expw, expg, TD);


    ReducedTPCWHugoniotFunctionClass tpcwhc(Uref, abs_perm, phi, const_gravity, &TD, &fh);

    //    StoneHugoniotFunctionClass stoneHugoniotFunction(Uref,(const StoneFluxFunction &) RpNumerics::getPhysics().fluxFunction());

    CoincidenceTPCW coincidence(&TD, &fh, phi);

    SubinflectionTPCW inflection(&TD, &fh, phi);

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

    ContourMethod method(dimension, RpNumerics::getPhysics().fluxFunction(), RpNumerics::getPhysics().accumulation(), RpNumerics::getPhysics().boundary(), &tpcwhc);

    //        ContourMethod method(dimension, RpNumerics::getPhysics().fluxFunction(), RpNumerics::getPhysics().accumulation(), RpNumerics::getPhysics().boundary(), &coincidence);
    //    ContourMethod method(dimension, RpNumerics::getPhysics().fluxFunction(), RpNumerics::getPhysics().accumulation(), RpNumerics::getPhysics().boundary(), &inflection);
    //       ContourMethod method(dimension, RpNumerics::getPhysics().fluxFunction(), RpNumerics::getPhysics().accumulation(), RpNumerics::getPhysics().boundary(), &stoneHugoniotFunction);

    //    std::vector<RealVector> vrs;
    //    method.curv2d(0, 2000, 0.0, &rect[0], &res[0], 1, vrs);

    vector<HugoniotPolyLine> hugoniotPolyLineVector;
    //    method.curve(Uref, hugoniotPolyLineVector);

    //    method.unclassifiedCurve(Uref, hugoniotPolyLineVector);
    method.classifiedCurve(Uref, hugoniotPolyLineVector);


    //    tpcwhc.completCurve(vrs);
    //    for (int i = 0; i < hugoniotPolyLineVector.size(); i++) {
    //
    //        cout << "Numero de pontos na polyline: " << i << " " << hugoniotPolyLineVector[i].vec.size() << endl;
    //        cout << "coord 1: " << i << " " << hugoniotPolyLineVector[i].vec[0].size() << endl;
    //        cout << "coord 2: " << i << " " << hugoniotPolyLineVector[i].vec[1].size() << endl;
    //
    //    }


    //    std::vector<RealVector> vrs3d;
    //
    //    vrs3d.resize(vrs.size());
    //    for (unsigned int i = 0; i < vrs.size(); i++) {
    //        double s;
    //        double u;
    //        tpcwhc.CompleteHugoniot(s, u, vrs[i]);
    //        vrs3d[i].resize(3);
    //        vrs3d[i].component(0) = vrs[i].component(0);
    //        //        vrs3d[i].component(1) = TD.Theta2T(vrs[i].component(1));
    //        vrs3d[i].component(1) = vrs[i].component(1);
    //        //        cout<<"Valor de u"<<u<<endl;
    //        //        cout << "Valor de s" << s << endl;
    //
    //        vrs3d[i].component(2) = u; //TD.U2u(u);
    //    }

//    cout << "Numero de polylines: " << hugoniotPolyLineVector.size() << endl;

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

            //            double leftSigma = 0;
            //            double rightSigma = 0;
            //

            //            cout<<"Antes de criar hugoniot segment"<<endl;
            jobject hugoniotSegment = env->NewObject(hugoniotSegmentClass, hugoniotSegmentConstructor, realVectorLeftPoint, leftSigma, realVectorRightPoint, rightSigma, pointType);
            env->CallObjectMethod(segmentsArray, arrayListAddMethod, hugoniotSegment);

        }


    }

    //    //    ColorCurve::preprocess com os pontos em 3d (completo)
    //    //Chamar o classified para cada 2 de pontos retornado pelo preprocess . Esses dois pontos tem que estar dentro de um vector<RealVector>
    //    for (unsigned int i = 0; i < vrs3d.size() / 2; i++) {
    //
    //        //        cout<<"Coordenada : "<<vrs3d.at(2*i)<<endl;
    //        //        cout << "Coordenada : " << vrs3d.at(2 * i+1) << endl;
    //        jdoubleArray eigenValRLeft = env->NewDoubleArray(dimension);
    //        jdoubleArray eigenValRRight = env->NewDoubleArray(dimension);
    //
    //
    //        double * leftCoords = (double *) vrs3d.at(2 * i);
    //        double * rightCoords = (double *) vrs3d.at(2 * i + 1);
    //
    //
    //        env->SetDoubleArrayRegion(eigenValRLeft, 0, dimension, leftCoords);
    //        env->SetDoubleArrayRegion(eigenValRRight, 0, dimension, rightCoords);
    //
    //
    //        //Construindo left e right points
    //        jobject realVectorLeftPoint = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, eigenValRLeft);
    //        jobject realVectorRightPoint = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, eigenValRRight);
    //
    //        int pointType = 0;
    //
    //        double leftSigma = -1.0;
    //        double rightSigma = -1.0;
    //        //            cout << "type of " << j << " = " << classified[i].type << endl;
    //        //            cout << "speed of " << j << " = " << classified[i].vec[j].component(dimension + m) << endl;
    //        //            cout << "speed of " << j + 1 << " = " << classified[i].vec[j + 1].component(dimension + m) << endl;
    //
    //        jobject hugoniotSegment = env->NewObject(hugoniotSegmentClass, hugoniotSegmentConstructor, realVectorLeftPoint, leftSigma, realVectorRightPoint, rightSigma, pointType);
    //        env->CallObjectMethod(segmentsArray, arrayListAddMethod, hugoniotSegment);
    //
    //    }




    // Limpando

    //        env->DeleteLocalRef(realVectorLeftPoint);

    //        env->DeleteLocalRef(realVectorRightPoint);

    //        env->DeleteLocalRef(hugoniotSegment);





    jobject result = env->NewObject(hugoniotCurveClass, hugoniotCurveConstructor, uMinus, segmentsArray);

    //    env->DeleteLocalRef(eigenValRLeft);
    //    env->DeleteLocalRef(eigenValRRight);
    env->DeleteLocalRef(hugoniotSegmentClass);
    env->DeleteLocalRef(realVectorClass);
    env->DeleteLocalRef(arrayListClass);



    return result;


}
















