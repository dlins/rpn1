///*
// * IMPA - Fluid Dynamics Laboratory
// *
// * RPn Project
// *
// * @(#) JNIShockCurveCalc.cc
// */
//
///*
// * ---------------------------------------------------------------
// * Includes:
// */
//#include "rpnumerics_CharacteristicsCurveCalc.h"
//
//
//
//
//#include "ColorCurve.h"
//#include <stdio.h>
//#include "RiemannSolver.h"
//#include "RealVector.h"
//#include "JNIDefs.h"
//#include <vector>
//#include "RpNumerics.h"
//#include ""
//
//
//using std::vector;
//
///*
// * ---------------------------------------------------------------
// * Definitions:
// */
//JNIEXPORT jobject JNICALL Java_rpnumerics_CharacteristicsCurveCalc_nativeCalc
//(JNIEnv * env, jobject obj, jobjectArray riemannOrbitPoints, jint samplingRate) {
//
//
//    jclass classCharacteristics = (env)->FindClass(CHARACTERISTICSCURVE_LOCATION);
//
//    jclass arrayListClass = env->FindClass("java/util/ArrayList");
//
//    jclass classPhasePoint = (env)->FindClass(PHASEPOINT_LOCATION);
//
//    jclass classOrbitPoint = (env)->FindClass(ORBITPOINT_LOCATION);
//
//    jclass realVectorClass = env->FindClass(REALVECTOR_LOCATION);
//
//
//
//    jmethodID toDoubleMethodID = (env)->GetMethodID(classPhasePoint, "toDouble", "()[D");
//
//    jmethodID arrayListConstructor = env->GetMethodID(arrayListClass, "<init>", "()V");
//
//    jmethodID arrayListAddMethod = env->GetMethodID(arrayListClass, "add", "(Ljava/lang/Object;)Z");
//
//    jmethodID characteristicsCurveConstructor = env->GetMethodID(classCharacteristics, "<init>", "(Ljava/util/List;)V");
//
//    jmethodID realVectorConstructorDoubleArray = env->GetMethodID(realVectorClass, "<init>", "([D)V");
//
//    jmethodID getLambdaID = (env)->GetMethodID(classOrbitPoint, "getLambda", "()D");
//
//    jmethodID phasePointConstructorID = env->GetMethodID(classPhasePoint, "<init>", "(Lwave/util/RealVector;)V");
//
//    //Input processing
//
//    int dimension = RpNumerics::getPhysics().domain().dim();
//
//
//    const FluxFunction * fluxFunction = &RpNumerics::getPhysics().fluxFunction();
//    const AccumulationFunction * accumulationFunction = &RpNumerics::getPhysics().accumulation();
//
//
//    int orbitPointArraySize = env->GetArrayLength(riemannOrbitPoints);
//
//    std::vector<RealVector> riemannProfileVector;
//
//    for (int j = 0; j < orbitPointArraySize; j++) {
//
//        jobject orbitPoint = env->GetObjectArrayElement(riemannOrbitPoints, j);
//
//        jdoubleArray orbitPointCoordsArray = (jdoubleArray) (env)->CallObjectMethod(orbitPoint, toDoubleMethodID);
//
//        double pointBuffer [dimension + 1];
//
//        env->GetDoubleArrayRegion(orbitPointCoordsArray, 0, dimension, pointBuffer);
//
//        pointBuffer[dimension] = (env)->CallDoubleMethod(orbitPoint, getLambdaID);
//
//        RealVector riemannProfilePoint(dimension + 1, pointBuffer);
//
//        riemannProfileVector.push_back(riemannProfilePoint);
//
//    }
//
//    std::vector<std::vector<std::vector<RealVector> > > characteristics;
//
//    if ( Debug::get_debug_level() == 5 ) {
//        ////cout <<"Sampling: "<<samplingRate<<endl;
//    }
//    
//    RiemannSolver::characteristics(fluxFunction, accumulationFunction, riemannProfileVector,0.45, samplingRate, characteristics);
//    
//    
//
//    if ( Debug::get_debug_level() == 5 ) {
//        // Modified below
//        FILE *fid = fopen("characteristics.txt", "w");
//
//        // Number of families
//        fprintf(fid, "%d\n", characteristics.size());
//
//        for (int i = 0; i < characteristics.size(); i++){
//            // Number of lines per family
//            fprintf(fid, "%d\n", characteristics[i].size());
//    
//            for (int j = 0; j < characteristics[i].size(); j++){
//                // Number of point per line per family
//                fprintf(fid, "%d\n", characteristics[i][j].size());
//    
//                for (int k = 0; k < characteristics[i][j].size(); k++){
//                    // Points
//                    fprintf(fid, "%g %g\n", characteristics[i][j][k].component(0), characteristics[i][j][k].component(1));
//                }
//            }
//        }
//        fclose(fid);
//    }
//
//
//    // Modified above
//
//
//    jobject characteristicsList = env->NewObject(arrayListClass, arrayListConstructor);
//    for (int i = 0; i < characteristics.size(); i++) {//Family vector
//        vector<vector<RealVector> > familyVector = characteristics.at(i);
//
//        jobject familyLinesList = env->NewObject(arrayListClass, arrayListConstructor);
//
//        for (int j = 0; j < familyVector.size(); j++) {//Lines set
//            vector< RealVector> linePoints = familyVector.at(j);
//            jobjectArray lineCoordsArray = env->NewObjectArray(linePoints.size(), classPhasePoint, NULL);
//
//            for (int k = 0; k < linePoints.size(); k++) {//Line 
//                RealVector linePoint = linePoints.at(k);
//
//                double * linePointCoods = (double *) linePoint;
//                jdoubleArray linePointDoubleArray = env->NewDoubleArray(2);
//                env->SetDoubleArrayRegion(linePointDoubleArray, 0, 2, linePointCoods);
//
//                jobject linePointRealVector = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, linePointDoubleArray);
//                jobject linePointPhasePoint = env->NewObject(classPhasePoint, phasePointConstructorID, linePointRealVector);
//                env->SetObjectArrayElement(lineCoordsArray, k, linePointPhasePoint);
//
//                if ( Debug::get_debug_level() == 5 ) {
//                    ////cout << linePoint << endl;
//                }
//
//            }
//
//            env->CallObjectMethod(familyLinesList, arrayListAddMethod, lineCoordsArray);
//            if ( Debug::get_debug_level() == 5 ) {
//                ////cout << "Fim da linha: " << j << endl;
//            }
//
//        }
//
//        env->CallObjectMethod(characteristicsList, arrayListAddMethod, familyLinesList);
//
//        if ( Debug::get_debug_level() == 5 ) {
//            ////cout << "Fim da familia: " << i << endl;
//        }
//
//    }
//
//
//
//    jobject result = env->NewObject(classCharacteristics, characteristicsCurveConstructor, characteristicsList);
//
//
//
//
//
//    return result;
//
//
//
//
//
//
//
//
//
//
//}
