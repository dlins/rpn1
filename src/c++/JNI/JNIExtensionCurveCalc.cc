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
#include "rpnumerics_ExtensionCurveCalc.h"
#include "RpNumerics.h"
#include "RealVector.h"
#include "JNIDefs.h"
#include "Implicit_Extension_Curve.h"
#include "RectBoundary.h"

#include <vector>

//#include "Rarefaction_Extension.h"


using std::vector;

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


JNIEXPORT jobject JNICALL Java_rpnumerics_ExtensionCurveCalc_nativeCalc(JNIEnv * env, jobject obj, jobject segmentList, jobject areaPoints, jint family, jint characteristicWhere, jboolean singular, jint inSideArea) {


    jclass realVectorClass = env->FindClass(REALVECTOR_LOCATION);
    jclass realSegmentClass = env->FindClass(REALSEGMENT_LOCATION);
    jclass arrayListClass = env->FindClass("java/util/ArrayList");

    jclass bifurcationCurveClass = env->FindClass(BIFURCATIONCURVE_LOCATION);


    jmethodID realVectorConstructorDoubleArray = env->GetMethodID(realVectorClass, "<init>", "([D)V");
    jmethodID realSegmentConstructor = (env)->GetMethodID(realSegmentClass, "<init>", "(Lwave/util/RealVector;Lwave/util/RealVector;)V");
    jmethodID toDoubleMethodID = (env)->GetMethodID(realVectorClass, "toDouble", "()[D");
    jmethodID getElementID = (env)->GetMethodID(arrayListClass, "get", "(I)Ljava/lang/Object;");
    jmethodID sizeID = (env)->GetMethodID(arrayListClass, "size", "()I");
    jmethodID getPoint1ID = (env)->GetMethodID(realSegmentClass, "p1", "()Lwave/util/RealVector;");
    jmethodID getPoint2ID = (env)->GetMethodID(realSegmentClass, "p2", "()Lwave/util/RealVector;");

    jmethodID arrayListConstructor = env->GetMethodID(arrayListClass, "<init>", "()V");
    jmethodID arrayListAddMethod = env->GetMethodID(arrayListClass, "add", "(Ljava/lang/Object;)Z");
    jmethodID bifurcationCurveConstructor = env->GetMethodID(bifurcationCurveClass, "<init>", "(Ljava/util/List;Ljava/util/List;)V");


    //Input processing
     int dimension = RpNumerics::physicsVector_->at(0)->boundary()->minimums().size();

    int segmentListSize = env->CallIntMethod(segmentList, sizeID);
    std::vector<RealVector> original_curve;

    for (int i = 0; i < segmentListSize; i++) {
        jobject segment = env->CallObjectMethod(segmentList, getElementID, i);
        jobject p1 = env->CallObjectMethod(segment, getPoint1ID);
        jobject p2 = env->CallObjectMethod(segment, getPoint2ID);
        jdoubleArray p1Array = (jdoubleArray) env->CallObjectMethod(p1, toDoubleMethodID);
        jdoubleArray p2Array = (jdoubleArray) env->CallObjectMethod(p2, toDoubleMethodID);

        double temp1 [dimension];
        env->GetDoubleArrayRegion(p1Array, 0, dimension, temp1);

        double temp2 [dimension];
        env->GetDoubleArrayRegion(p2Array, 0, dimension, temp2);

        RealVector nativeRealVector1(dimension, temp1);
        original_curve.push_back(nativeRealVector1);

        RealVector nativeRealVector2(dimension, temp2);
        original_curve.push_back(nativeRealVector2);
    }

    //Getting area Vertices
    int areaPointsSize = env->CallIntMethod(areaPoints, sizeID);

    std::vector<RealVector> areaVertices;

    for (int i = 0; i < areaPointsSize; i++) {
        jobject areaVertex = env->CallObjectMethod(areaPoints, getElementID, i);
        jdoubleArray p1Array = (jdoubleArray) env->CallObjectMethod(areaVertex, toDoubleMethodID);


        double temp1 [dimension];
        env->GetDoubleArrayRegion(p1Array, 0, dimension, temp1);

        RealVector nativeRealVector1(dimension, temp1);
        areaVertices.push_back(nativeRealVector1);

    }

    jobject leftSegmentsArray = env->NewObject(arrayListClass, arrayListConstructor, NULL);

    jobject rightSegmentsArray = env->NewObject(arrayListClass, arrayListConstructor, NULL);


    // Storage space for the segments:


    std::vector<RealVector> curve_segments;
    std::vector<RealVector> domain_segments;


    
    const FluxFunction * flux = RpNumerics::physicsVector_->at(0)->flux();
    const AccumulationFunction * accum = RpNumerics::physicsVector_->at(0)->accumulation();
    

    GridValues * gv = RpNumerics::physicsVector_->at(0)->gridvalues();



    Implicit_Extension_Curve extension;


    cout << "Flux: " << flux << endl;
    cout << "Accum: " << accum << endl;



    extension.prepare_extension(flux, accum, flux, accum, gv, characteristicWhere, singular, family);


    cout << "Inside area: " << inSideArea << endl;
    cout << "Characteristic: " << characteristicWhere << endl;
    cout << "Singular: " << singular << endl;
    cout << "Family: " << family << endl;
    cout << "Original curve: " << original_curve.size() << endl;


//
//    for (int i = 0; i < original_curve.size(); i++) {
//
//
//        cout << "Ponto: " << original_curve[i] << endl;
//
//
//    }





    if (inSideArea == 0) {
         std::vector<RealVector> domain_polygon;
          std::vector<RealVector> image_polygon;
         
         extension.extension_curve(original_curve,
            domain_polygon,
            image_polygon,domain_segments);
         
         curve_segments=original_curve;

        
//        extension.extension_of_curve(fluxFunction, accumFunction, fluxFunction, accumFunction, *gv, characteristicWhere, singular, family,
//                original_curve, curve_segments, domain_segments);

    } else {

        extension.curve_in_subdomain(flux, accum, *gv,areaVertices,characteristicWhere, singular, family,
                original_curve, curve_segments, domain_segments);

    }




    cout << "Curva: " << curve_segments.size() << " Dominio: " << domain_segments.size() << endl;

    if (curve_segments.size() == 0 || domain_segments.size() == 0) {


        return NULL;
    }


    for (unsigned int i = 0; i < curve_segments.size() / 2; i++) {

        jdoubleArray eigenValRLeft = env->NewDoubleArray(dimension);
        jdoubleArray eigenValRRight = env->NewDoubleArray(dimension);



        double * leftCoords = (double *) curve_segments.at(2 * i);
        double * rightCoords = (double *) curve_segments.at(2 * i + 1);


        env->SetDoubleArrayRegion(eigenValRLeft, 0, dimension, leftCoords);
        env->SetDoubleArrayRegion(eigenValRRight, 0, dimension, rightCoords);


        //Construindo left e right points
        jobject realVectorLeftPoint = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, eigenValRLeft);

        jobject realVectorRightPoint = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, eigenValRRight);


        jobject realSegment = env->NewObject(realSegmentClass, realSegmentConstructor, realVectorLeftPoint, realVectorRightPoint);
        env->CallObjectMethod(leftSegmentsArray, arrayListAddMethod, realSegment);

    }




    for (unsigned int i = 0; i < domain_segments.size() / 2; i++) {

        jdoubleArray eigenValRLeft = env->NewDoubleArray(dimension);
        jdoubleArray eigenValRRight = env->NewDoubleArray(dimension);


        double * leftCoords = (double *) domain_segments.at(2 * i);
        double * rightCoords = (double *) domain_segments.at(2 * i + 1);


        env->SetDoubleArrayRegion(eigenValRLeft, 0, dimension, leftCoords);
        env->SetDoubleArrayRegion(eigenValRRight, 0, dimension, rightCoords);


        //Construindo left e right points
        jobject realVectorLeftPoint = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, eigenValRLeft);

        jobject realVectorRightPoint = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, eigenValRRight);

        jobject realSegment = env->NewObject(realSegmentClass, realSegmentConstructor, realVectorLeftPoint, realVectorRightPoint);


        env->CallObjectMethod(rightSegmentsArray, arrayListAddMethod, realSegment);

    }

    jobject result = env->NewObject(bifurcationCurveClass, bifurcationCurveConstructor, leftSegmentsArray, rightSegmentsArray);


    return result;

}
