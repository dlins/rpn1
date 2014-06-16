/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) JNIBoundaryExtensionExtension.cc
 **/


//! JNIBoundaryExtensionExtension
/*!
	
TODO:
	
NOTE : 

@ingroup JNI
 */


#include "rpnumerics_EllipticBoundaryExtensionCalc.h"
#include "EllipticExtension.h"
#include "JNIDefs.h"
#include "RpNumerics.h"
#include "Debug.h"
#include <vector>
#include <iostream>


using std::vector;
using namespace std;

JNIEXPORT jobject JNICALL Java_rpnumerics_EllipticBoundaryExtensionCalc_nativeCalc
(JNIEnv * env, jobject obj, int characteristic, int family) {

    jclass realVectorClass = env->FindClass(REALVECTOR_LOCATION);

    jclass realSegmentClass = env->FindClass(REALSEGMENT_LOCATION);

    jclass arrayListClass = env->FindClass("java/util/ArrayList");

    jclass ellipticBoundaryExtensionClass = env->FindClass(ELLIPTICEXTENSIONBOUNDARY_LOCATION);

    jmethodID realVectorConstructorDoubleArray = env->GetMethodID(realVectorClass, "<init>", "([D)V");
    jmethodID realSegmentConstructor = (env)->GetMethodID(realSegmentClass, "<init>", "(Lwave/util/RealVector;Lwave/util/RealVector;)V");

    jmethodID arrayListConstructor = env->GetMethodID(arrayListClass, "<init>", "()V");
    jmethodID arrayListAddMethod = env->GetMethodID(arrayListClass, "add", "(Ljava/lang/Object;)Z");
    jmethodID ellipticBoundaryConstructor = env->GetMethodID(ellipticBoundaryExtensionClass, "<init>", "(Ljava/util/List;Ljava/util/List;)V");

    int dimension = RpNumerics::getPhysics().domain().dim();


    jobject leftSegmentsArray = env->NewObject(arrayListClass, arrayListConstructor, NULL);
    jobject rightSegmentsArray = env->NewObject(arrayListClass, arrayListConstructor, NULL);


    // Storage space for the segments:


    GridValues * gv = RpNumerics::getGridFactory().getGrid("bifurcation");

    EllipticExtension ellipticBoundaryExtension;

    std::vector<RealVector> elliptic_extension_on_curve;
    std::vector<RealVector> elliptic_extension_on_domain;



    if ( Debug::get_debug_level() == 5 ) {
        ////cout << "Familia: " << family << endl;
        ////cout << "Charatristic: " << characteristic << endl;
    }

    ellipticBoundaryExtension.curve(&RpNumerics::getPhysics().fluxFunction(), &RpNumerics::getPhysics().accumulation(), characteristic, family,
            *gv, elliptic_extension_on_curve, elliptic_extension_on_domain);

    if ( Debug::get_debug_level() == 5 ) {
        ////cout << "Curva: " << elliptic_extension_on_curve.size() << " " << "Domain:  " << elliptic_extension_on_domain.size() << endl;
    }

    if (elliptic_extension_on_curve.size() == 0 || elliptic_extension_on_domain.size() == 0)return NULL;


    for (unsigned int i = 0; i < elliptic_extension_on_curve.size() / 2; i++) {


        jdoubleArray eigenValRLeft = env->NewDoubleArray(dimension);
        jdoubleArray eigenValRRight = env->NewDoubleArray(dimension);

        double * leftCoords = (double *) elliptic_extension_on_curve.at(2 * i);
        double * rightCoords = (double *) elliptic_extension_on_curve.at(2 * i + 1);




        env->SetDoubleArrayRegion(eigenValRLeft, 0, dimension, leftCoords);
        env->SetDoubleArrayRegion(eigenValRRight, 0, dimension, rightCoords);

        //Construindo left e right points
        jobject realVectorLeftPoint = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, eigenValRLeft);

        jobject realVectorRightPoint = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, eigenValRRight);


        jobject realSegment = env->NewObject(realSegmentClass, realSegmentConstructor, realVectorLeftPoint, realVectorRightPoint);
        env->CallObjectMethod(leftSegmentsArray, arrayListAddMethod, realSegment);

    }

    for (unsigned int i = 0; i < elliptic_extension_on_domain.size() / 2; i++) {



        jdoubleArray eigenValRLeft = env->NewDoubleArray(dimension);
        jdoubleArray eigenValRRight = env->NewDoubleArray(dimension);

        double * leftCoords = (double *) elliptic_extension_on_domain.at(2 * i);
        double * rightCoords = (double *) elliptic_extension_on_domain.at(2 * i + 1);


        env->SetDoubleArrayRegion(eigenValRLeft, 0, dimension, leftCoords);
        env->SetDoubleArrayRegion(eigenValRRight, 0, dimension, rightCoords);

        //Construindo left e right points
        jobject realVectorLeftPoint = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, eigenValRLeft);

        jobject realVectorRightPoint = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, eigenValRRight);
        jobject realSegment = env->NewObject(realSegmentClass, realSegmentConstructor, realVectorLeftPoint, realVectorRightPoint);


        env->CallObjectMethod(rightSegmentsArray, arrayListAddMethod, realSegment);

    }


    jobject result = env->NewObject(ellipticBoundaryExtensionClass, ellipticBoundaryConstructor, leftSegmentsArray, rightSegmentsArray);


    return result;



}
















