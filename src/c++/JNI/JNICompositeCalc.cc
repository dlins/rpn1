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
#include "rpnumerics_CompositeCalc.h"
#include "RpNumerics.h"
#include "RealVector.h"
#include "JNIDefs.h"
#include "Rarefaction_Extension.h"
#include "Stone.h"
#include "StoneAccumulation.h"
#include "RectBoundary.h"
#include <vector>


using std::vector;

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


JNIEXPORT jobject JNICALL Java_rpnumerics_CompositeCalc_nativeCalc(JNIEnv * env, jobject obj, jint xResolution, jint yResolution, jobject initialPoint, jint increase, jint curveFamily, jint domainFamily, jint characteristicWhere) {

    cout << "chamando JNI composite calc" << endl;

    jclass classPhasePoint = (env)->FindClass(PHASEPOINT_LOCATION);
    jclass hugoniotSegmentClass = (env)->FindClass(HUGONIOTSEGMENTCLASS_LOCATION);
    jclass realVectorClass = env->FindClass(REALVECTOR_LOCATION);
    jclass arrayListClass = env->FindClass("java/util/ArrayList");
    jclass compositeCurveClass = env->FindClass(COMPOSITECURVE_LOCATION);

    jmethodID toDoubleMethodID = (env)->GetMethodID(classPhasePoint, "toDouble", "()[D");
    jmethodID realVectorConstructorDoubleArray = env->GetMethodID(realVectorClass, "<init>", "([D)V");
    jmethodID hugoniotSegmentConstructor = (env)->GetMethodID(hugoniotSegmentClass, "<init>", "(Lwave/util/RealVector;DLwave/util/RealVector;DI)V");
    jmethodID arrayListConstructor = env->GetMethodID(arrayListClass, "<init>", "()V");
    jmethodID arrayListAddMethod = env->GetMethodID(arrayListClass, "add", "(Ljava/lang/Object;)Z");
    jmethodID compositeCurveConstructor = env->GetMethodID(compositeCurveClass, "<init>", "(Ljava/util/List;Ljava/util/List;)V");


    //Input processing
    jdoubleArray phasePointArray = (jdoubleArray) (env)->CallObjectMethod(initialPoint, toDoubleMethodID);

    int dimension = env->GetArrayLength(phasePointArray);

    double input [dimension];

    env->GetDoubleArrayRegion(phasePointArray, 0, dimension, input);

    env->DeleteLocalRef(phasePointArray);

    RealVector inputPoint(dimension, input);
    cout << inputPoint << endl;

    jobject leftSegmentsArray = env->NewObject(arrayListClass, arrayListConstructor, NULL);

    jobject rightSegmentsArray = env->NewObject(arrayListClass, arrayListConstructor, NULL);


    // Storage space for the segments:


    std::vector<RealVector> curve_segments;
    std::vector<RealVector> domain_segments;
    std::vector<RealVector> rarefaction_segments;


    int number_of_grid_points[2];

    number_of_grid_points[0] = xResolution;
    number_of_grid_points[1] = yResolution;


    if (RpNumerics::getPhysics().ID().compare("Stone") == 0) {

        cout << "Chamando com stone" << endl;

        dimension = 2;


        double expw, expg, expo;
        expw = expg = expo = 2.0;
        double expow, expog;
        expow = expog = 2.0;
        double cnw, cng, cno;
        cnw = cng = cno = 0.0;
        double lw, lg;
        lw = lg = 0.0;
        double low, log;
        low = log = 0.0;
        double epsl = 0.0;

        StonePermParams stonepermparams(expw, expg, expo, expow, expog, cnw, cng, cno, lw, lg, low, log, epsl);

        StonePermeability stonepermeability(stonepermparams);

//        double grw = 1.0;
//        double grg = 0.5;
//        double gro = 0.7;
//
//        double muw = 1.0;
//        double mug = 1.0;
//        double muo = 1.0;
//
//        double vel = 0.0;


        double grw = 1.0;
        double grg = 0.5;
        double gro = 1.0;

        double muw = 1.0;
        double mug = 1.0;
        double muo = 1.0;

        double vel = 0.0;


        RealVector p(7);
        p.component(0) = grw;
        p.component(1) = grg;
        p.component(2) = gro;
        p.component(3) = muw;
        p.component(4) = mug;
        p.component(5) = muo;
        p.component(6) = vel;

        StoneParams stoneparams(p);
        StoneFluxFunction stoneflux(stoneparams, stonepermparams);

        // Create the (trivial) accumulation function
        StoneAccumulation stoneaccum;


        int singular = 1;

        //        const RealVector & pmin = RpNumerics::getPhysics().boundary().minimums();
        //        const RealVector & pmax = RpNumerics::getPhysics().boundary().maximums();

        RealVector pmin(2);
        RealVector pmax(2);


        pmin.component(0) = 0.0;
        pmin.component(1) = 0.0;


        pmax.component(0) = 1.0;
        pmax.component(1) = 1.0;



        cout << pmin << endl;
        cout << pmax << endl;

        cout << "Curve Family: " << curveFamily << endl;
        cout << "Domain Family: " << domainFamily << endl;
        cout << "Characteristic : " << characteristicWhere << endl;
        cout << "Increase: " << increase << endl;
        cout << "x resolution: " << xResolution << endl;
        cout << "y resolution: " << yResolution << endl;


        vector<bool> testBooleanVector;

        testBooleanVector.push_back(true);
        testBooleanVector.push_back(true);
        RectBoundary * testBoundary = new RectBoundary(pmin, pmax, testBooleanVector);

        Rarefaction_Extension::extension_curve(&stoneflux,
                &stoneaccum,
                inputPoint,
                .001,
                curveFamily,
                increase,
                testBoundary,
                pmin, pmax, number_of_grid_points, // For the domain.
                domainFamily,
                &stoneflux, &stoneaccum,
                characteristicWhere, singular,rarefaction_segments,
                curve_segments,
                domain_segments);

        delete testBoundary;

        printf("curve.size()  = %d\n", curve_segments.size());
        printf("domain.size() = %d\n", domain_segments.size());
        printf("rarefaction.size() = %d\n", rarefaction_segments.size());



    }

    if (curve_segments.size() == 0 || domain_segments.size() == 0) {
        printf("curve.size()  = %d\n", curve_segments.size());
        printf("domain.size() = %d\n", domain_segments.size());
        return NULL;
    }


    for (unsigned int i = 0; i < curve_segments.size() / 2; i++) {
        //    for (unsigned int i = 0; i < right_vrs.size() / 2; i++) {

        //        cout << "Coordenada : " << left_vrs.at(2 * i) << endl;
        //        cout << "Coordenada : " << left_vrs.at(2 * i + 1) << endl;


        jdoubleArray eigenValRLeft = env->NewDoubleArray(dimension);
        jdoubleArray eigenValRRight = env->NewDoubleArray(dimension);


        double * leftCoords = (double *) curve_segments.at(2 * i);
        double * rightCoords = (double *) curve_segments.at(2 * i + 1);


        //
        //        double * leftCoords = (double *) right_vrs.at(2 * i);
        //        double * rightCoords = (double *) right_vrs.at(2 * i + 1 );


        env->SetDoubleArrayRegion(eigenValRLeft, 0, dimension, leftCoords);
        env->SetDoubleArrayRegion(eigenValRRight, 0, dimension, rightCoords);


        //Construindo left e right points
        jobject realVectorLeftPoint = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, eigenValRLeft);

        jobject realVectorRightPoint = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, eigenValRRight);

        int pointType = 0;

        double leftSigma = 0;
        double rightSigma = 0;
        //            cout << "type of " << j << " = " << classified[i].type << endl;
        //            cout << "speed of " << j << " = " << classified[i].vec[j].component(dimension + m) << endl;
        //            cout << "speed of " << j + 1 << " = " << classified[i].vec[j + 1].component(dimension + m) << endl;

        jobject hugoniotSegment = env->NewObject(hugoniotSegmentClass, hugoniotSegmentConstructor, realVectorLeftPoint, leftSigma, realVectorRightPoint, rightSigma, pointType);
        env->CallObjectMethod(leftSegmentsArray, arrayListAddMethod, hugoniotSegment);

    }




    for (unsigned int i = 0; i < domain_segments.size() / 2; i++) {

        //        cout << "Coordenada : " << left_vrs.at(2 * i) << endl;
        //        cout << "Coordenada : " << left_vrs.at(2 * i + 1) << endl;


        jdoubleArray eigenValRLeft = env->NewDoubleArray(dimension);
        jdoubleArray eigenValRRight = env->NewDoubleArray(dimension);


        double * leftCoords = (double *) domain_segments.at(2 * i);
        double * rightCoords = (double *) domain_segments.at(2 * i + 1);


        env->SetDoubleArrayRegion(eigenValRLeft, 0, dimension, leftCoords);
        env->SetDoubleArrayRegion(eigenValRRight, 0, dimension, rightCoords);


        //Construindo left e right points
        jobject realVectorLeftPoint = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, eigenValRLeft);

        jobject realVectorRightPoint = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, eigenValRRight);

        int pointType = 0;

        double leftSigma = 0;
        double rightSigma = 0;
        //            cout << "type of " << j << " = " << classified[i].type << endl;
        //            cout << "speed of " << j << " = " << classified[i].vec[j].component(dimension + m) << endl;
        //            cout << "speed of " << j + 1 << " = " << classified[i].vec[j + 1].component(dimension + m) << endl;

        jobject hugoniotSegment = env->NewObject(hugoniotSegmentClass, hugoniotSegmentConstructor, realVectorLeftPoint, leftSigma, realVectorRightPoint, rightSigma, pointType);
        env->CallObjectMethod(rightSegmentsArray, arrayListAddMethod, hugoniotSegment);

    }

    jobject result = env->NewObject(compositeCurveClass, compositeCurveConstructor, leftSegmentsArray, rightSegmentsArray);


    //    env->DeleteLocalRef(eigenValRLeft);
    //    env->DeleteLocalRef(eigenValRRight);
    //    env->DeleteLocalRef(hugoniotSegmentClass);
    //    env->DeleteLocalRef(realVectorClass);
    //    env->DeleteLocalRef(arrayListClass);



    return result;

}
