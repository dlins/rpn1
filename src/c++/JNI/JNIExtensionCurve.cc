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


#include "rpnumerics_BoundaryExtensionCurveCalc.h"

#include "JNIDefs.h"
#include "RpNumerics.h"
#include <vector>
#include <iostream>
#include "TPCW.h"
#include "Extension_Curve.h"
#include "Boundary_ExtensionTPCW.h"




using std::vector;
using namespace std;

JNIEXPORT jobject JNICALL Java_rpnumerics_BoundaryExtensionCurveCalc_nativeCalc
(JNIEnv * env, jobject obj, jint xResolution, jint yResolution, jint edgeResolution,jint curveFamily, jint domainFamily, jint edge, jint characteristicWhere) {

    jclass hugoniotSegmentClass = (env)->FindClass(HUGONIOTSEGMENTCLASS_LOCATION);

    jclass realVectorClass = env->FindClass(REALVECTOR_LOCATION);

    jclass arrayListClass = env->FindClass("java/util/ArrayList");

    jclass extensionCurveClass = env->FindClass(BOUNDARYEXTENSIONCURVE_LOCATION);

    jmethodID realVectorConstructorDoubleArray = env->GetMethodID(realVectorClass, "<init>", "([D)V");

    jmethodID hugoniotSegmentConstructor = (env)->GetMethodID(hugoniotSegmentClass, "<init>", "(Lwave/util/RealVector;DLwave/util/RealVector;DI)V");

    jmethodID arrayListConstructor = env->GetMethodID(arrayListClass, "<init>", "()V");
    jmethodID arrayListAddMethod = env->GetMethodID(arrayListClass, "add", "(Ljava/lang/Object;)Z");
    jmethodID extensionCurveConstructor = env->GetMethodID(extensionCurveClass, "<init>", "(Ljava/util/List;Ljava/util/List;)V");

    //Input processing

    int dimension;

    //Calculations using the input

    jobject leftSegmentsArray = env->NewObject(arrayListClass, arrayListConstructor, NULL);

    jobject rightSegmentsArray = env->NewObject(arrayListClass, arrayListConstructor, NULL);

    // Storage space for the segments:

    vector<RealVector> curve_segments;
    vector<RealVector> domain_segments;

    int * number_of_domain_pnts = new int[2];


    number_of_domain_pnts[0] = xResolution;
    number_of_domain_pnts[1] = yResolution;

    if (RpNumerics::getPhysics().ID().compare("Stone") == 0) {

        cout << "Chamando com stone" << endl;

        dimension = 2;
        // Create the Double Contact
        // Grid (the same one for the left- and right-domains)


        RealVector pmin(2);
        pmin.component(0) = 0.0;
        pmin.component(1) = 0.0;

        RealVector pmax(2);
        pmax.component(0) = 1.0;
        pmax.component(1) = 1.0;


        cout << "Resolucao do x: " << xResolution << endl;
        cout << "Resolucao do y: " << yResolution << endl;

        // Over the x axis.
        int curve_points = 51;
        double delta = 1.0 / (double) curve_points;

        RealVector p1(2), p2(2);
        std::vector<RealVector> original_curve_segments;
        for (int i = 0; i < curve_points - 1; i++) {
            p1.component(0) = (double) i*delta;
            p1.component(1) = 0.0;

            p2.component(0) = ((double) i + 1) * delta;
            p2.component(1) = 0.0;

            original_curve_segments.push_back(p1);
            original_curve_segments.push_back(p2);
        }

        Extension_Curve ec(pmin, pmax, number_of_domain_pnts, (FluxFunction *) RpNumerics::getPhysics().fluxFunction().clone(), (AccumulationFunction *) RpNumerics::getPhysics().accumulation().clone());


        // Storage space for the segments:
        //    int characteristic_where = CHARACTERISTIC_ON_CURVE;
        //        int characteristic_where = CHARACTERISTIC_ON_DOMAIN;
        int singular = 0;

        cout << "Familia da curva" << curveFamily << endl;
        cout << "Familia do dominio" << domainFamily << endl;
        cout << "characteristic " << characteristicWhere << endl;
        cout << "edge " << edge << endl;


        ec.compute_extension_curve(characteristicWhere, singular,
                original_curve_segments, curveFamily,

                (FluxFunction *) RpNumerics::getPhysics().fluxFunction().clone(), (AccumulationFunction *) RpNumerics::getPhysics().accumulation().clone(),
                domainFamily,
                curve_segments,
                domain_segments);

    }


    if (RpNumerics::getPhysics().ID().compare("TPCW") == 0) {

        cout << "Chamando extension com tpcw" << endl;
        dimension = 3;

        Thermodynamics_SuperCO2_WaterAdimensionalized td(Physics::getRPnHome());

        int info = td.status_after_init();
        printf("Thermodynamics = %p,  info = %d\n\n\n", &td, info);

        // Create Horizontal & Vertical FracFlows
        double cnw = 0., cng = 0., expw = 2., expg = 2.;
        FracFlow2PhasesHorizontalAdimensionalized * fh = new FracFlow2PhasesHorizontalAdimensionalized(cnw, cng, expw, expg, td);
        FracFlow2PhasesVerticalAdimensionalized * fv = new FracFlow2PhasesVerticalAdimensionalized(cnw, cng, expw, expg, td);

        // Create the Flux and its params
        double abs_perm = 20e-12;
        double sin_beta = 0.0;
        double const_gravity = 9.8;
        bool has_gravity = false, has_horizontal = true;

        Flux2Comp2PhasesAdimensionalized_Params flux_params(abs_perm, sin_beta, const_gravity,
                has_gravity, has_horizontal,
                td,
                fh, fv);

        Flux2Comp2PhasesAdimensionalized flux(flux_params);

        // Create the Accum and its params
        double phi = 1.0;
        Accum2Comp2PhasesAdimensionalized_Params accum_params(td, &phi);
        Accum2Comp2PhasesAdimensionalized accum(accum_params);


        // Reduced stuff
        ReducedFlux2Comp2PhasesAdimensionalized_Params reduced_flux_params(abs_perm, &td, fh);
        ReducedFlux2Comp2PhasesAdimensionalized reduced_flux(reduced_flux_params);


        ReducedAccum2Comp2PhasesAdimensionalized_Params reduced_accum_params(&td, &phi);
        ReducedAccum2Comp2PhasesAdimensionalized reduced_accum(reduced_accum_params);


        //                RealVector pmin(2);
        //                pmin.component(0) = 0.0;
        //                pmin.component(1) = td.T2Theta(304.63);
        //                RealVector pmax(2);
        //                pmax.component(0) = 1.0;
        //                pmax.component(1) = td.T2Theta(450.0);
        //
        //




        SubPhysics & physics = RpNumerics::getPhysics().getSubPhysics(0);
        const Boundary & physicsBoundary = RpNumerics::getPhysics().boundary();

        RealVector min(2);

        RealVector max(2);


        min.component(0) = physicsBoundary.minimums().component(0);
        min.component(1) = physicsBoundary.minimums().component(1);

        max.component(0) = physicsBoundary.maximums().component(0);
        max.component(1) = physicsBoundary.maximums().component(1);


        physics.preProcess(min);
        physics.preProcess(max);

        cout << "Min: " << min << endl;
        cout << "Max: " << max << endl;

        //        RealVector pmin(2);
        //        pmin.component(0) = RpNumerics::getPhysics().boundary().minimums().component(0);
        //        pmin.component(1) = RpNumerics::getPhysics().boundary().minimums().component(1);
        //        RealVector pmax(2);
        //        pmax.component(0) = RpNumerics::getPhysics().boundary().maximums().component(0);
        //        pmax.component(1) = RpNumerics::getPhysics().boundary().maximums().component(1);

        int * number_of_grid_pnts = new int[2];


        number_of_grid_pnts[0] = xResolution;
        number_of_grid_pnts[1] = yResolution;

        int singular = 1;

        cout << "Resolucao x " << number_of_grid_pnts[0] << endl;
        ;
        cout << "Resolucao y " << number_of_grid_pnts[1] << endl;

        cout << "Familia da curva" << curveFamily << endl;
        cout << "Familia do dominio" << domainFamily << endl;
        cout << "characteristic " << characteristicWhere << endl;
        cout << "edge " << edge << endl;
        cout<<"edgeResolution: "<<edgeResolution<<endl;

        const FluxFunction * curve_flux = &flux;
        const AccumulationFunction *curve_accum = &accum;

        const FluxFunction * curve_reduced_flux = &reduced_flux;
        const AccumulationFunction *curve_reduced_accum = &reduced_accum;


        //TODO Pegar o parametro hardcoded 101 (number_of_temperature_steps) da interface

        Boundary_ExtensionTPCW::extension_curve(curve_flux, curve_accum,
                curve_reduced_flux, curve_reduced_accum,
                edge,edgeResolution,
                curveFamily,
                min, max, number_of_grid_pnts, // For the domain.
                domainFamily,
                curve_flux, curve_accum,
                curve_reduced_flux, curve_reduced_accum,
                characteristicWhere, singular,
                curve_segments,
                domain_segments);


        physics.postProcess(curve_segments);
        physics.postProcess(domain_segments);


        delete fv;
        delete fh;

    }

    delete number_of_domain_pnts;

    //    printf("curve_segments.size()  = %d\n", curve_segments.size());
    //    printf("domain_segments.size() = %d\n", domain_segments.size());


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

        int pointType = 20;

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

        int pointType = 20;

        double leftSigma = 0;
        double rightSigma = 0;
        //            cout << "type of " << j << " = " << classified[i].type << endl;
        //            cout << "speed of " << j << " = " << classified[i].vec[j].component(dimension + m) << endl;
        //            cout << "speed of " << j + 1 << " = " << classified[i].vec[j + 1].component(dimension + m) << endl;

        jobject hugoniotSegment = env->NewObject(hugoniotSegmentClass, hugoniotSegmentConstructor, realVectorLeftPoint, leftSigma, realVectorRightPoint, rightSigma, pointType);
        env->CallObjectMethod(rightSegmentsArray, arrayListAddMethod, hugoniotSegment);

    }




    jobject result = env->NewObject(extensionCurveClass, extensionCurveConstructor, leftSegmentsArray, rightSegmentsArray);


    //    env->DeleteLocalRef(eigenValRLeft);
    //    env->DeleteLocalRef(eigenValRRight);
    //    env->DeleteLocalRef(hugoniotSegmentClass);
    //    env->DeleteLocalRef(realVectorClass);
    //    env->DeleteLocalRef(arrayListClass);



    return result;


}
















