/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) JNIWaveCurveSpeedDiagram.cc
 **/

//! Definition of JNIWaveCurveSpeedDiagram
/*!
 *
 * TODO:
 *
 * NOTE :
 *
 * @ingroup JNI
 */

#include "rpnumerics_WaveCurve.h"

#include "RpNumerics.h"
#include "JNIDefs.h"
#include "WaveCurve.h"
#include <iostream>

JNIEXPORT jobject JNICALL Java_rpnumerics_WaveCurve_nativeDiagramCalc
(JNIEnv *env, jobject obj, jint family, jint curveID) {


    jclass arrayListClass = env->FindClass("java/util/ArrayList");
    jclass realVectorClass = env->FindClass(REALVECTOR_LOCATION);
    jclass diagramClass = env->FindClass(DIAGRAM_LOCATION);
    jclass diagramLineClass = env->FindClass(DIAGRAMLINE_LOCATION);


    jmethodID diagramConstructor = env->GetMethodID(diagramClass, "<init>", "(Ljava/util/List;)V");
    jmethodID diagramLineConstructor = env->GetMethodID(diagramLineClass, "<init>", "(Ljava/util/List;)V");
    
    jmethodID diagramLineDefaultConstructor = env->GetMethodID(diagramLineClass, "<init>", "(V)V");
    
    jmethodID addPartMethodID = env->GetMethodID(diagramLineClass, "<addPart>", "(Ljava/util/List;)V");
    
    
    jmethodID setTypeMethodID = env->GetMethodID(diagramLineClass, "<setType>", "(II)V");
    
    

    jmethodID arrayListConstructor = env->GetMethodID(arrayListClass, "<init>", "()V");
    jmethodID arrayListAddMethod = env->GetMethodID(arrayListClass, "add", "(Ljava/lang/Object;)Z");
    jmethodID realVectorConstructorDoubleArray = env->GetMethodID(realVectorClass, "<init>", "([D)V");

    int dimension = 2;


    std::vector<Curve> arclength_speed, arclength_eigenvalues;
    std::vector<Curve> arclength_reference_eigenvalues;


    const WaveCurve * waveCurve = RpNumerics::getWaveCurve(curveID);


    waveCurve->speed_map(arclength_speed, arclength_eigenvalues, arclength_reference_eigenvalues);


    jobject diagramLinesList = env->NewObject(arrayListClass, arrayListConstructor, NULL);


    jobject speedLine = env->NewObject(diagramLineClass, diagramLineDefaultConstructor);
    
    jobject speedLineList = env->NewObject(arrayListClass, arrayListConstructor, NULL); //ADDING SPEED 

    for (int i = 0; i < arclength_speed.size(); i++) {

        jobject speedLinePartList = env->NewObject(arrayListClass, arrayListConstructor, NULL);

        for (int j = 0; j < arclength_speed[i].curve.size(); j++) {

            jdoubleArray speedArray = env->NewDoubleArray(dimension);

            env->SetDoubleArrayRegion(speedArray, 0, dimension, (double *) arclength_speed[i].curve.at(j));

            jobject realVector = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, speedArray);

            //            cout << arclength_speed[i].curve.at(j) << endl;

            env->CallObjectMethod(speedLinePartList, arrayListAddMethod, realVector);


        }
        env->CallObjectMethod(speedLine, addPartMethodID, speedLinePartList);

//        env->CallObjectMethod(speedLineList, arrayListAddMethod, speedLinePartList);

    }


//    jobject speedLine = env->NewObject(diagramLineClass, diagramLineConstructor, speedLineList);
    
    
    env->CallObjectMethod(diagramLinesList, arrayListAddMethod, speedLine);


    //


    //REF POINT SPEED 

    for (int i = 0; i < arclength_reference_eigenvalues.size(); i++) {

        jobject linePartsList = env->NewObject(arrayListClass, arrayListConstructor, NULL);

        jobject speedLinePartList = env->NewObject(arrayListClass, arrayListConstructor, NULL);

        for (int j = 0; j < arclength_reference_eigenvalues[i].curve.size(); j++) {

            jdoubleArray speedArray = env->NewDoubleArray(dimension);

            env->SetDoubleArrayRegion(speedArray, 0, dimension, (double *) arclength_reference_eigenvalues[i].curve.at(j));

            jobject realVector = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, speedArray);

            //            cout << arclength_reference_eigenvalues[i].curve.at(j) << endl;

            env->CallObjectMethod(speedLinePartList, arrayListAddMethod, realVector);

        }

        env->CallObjectMethod(linePartsList, arrayListAddMethod, speedLinePartList);


        jobject eigenLine = env->NewObject(diagramLineClass, diagramLineConstructor, linePartsList);

//        env->CallObjectMethod(diagramLinesList, arrayListAddMethod, eigenLine);
    }






    cout << "Tamanho de autovalores: " << arclength_reference_eigenvalues.size() << endl;


    for (int eigenValueIndex = 1; eigenValueIndex < dimension+1; eigenValueIndex++) {// Numero de auto valores


        jobject eigenLineList = env->NewObject(arrayListClass, arrayListConstructor, NULL); //EIGEN VALUES 


        for (int i = 0; i < arclength_eigenvalues.size(); i++) { //Quantidade de subcurvas da curva de onda


            jobject speedLinePartList = env->NewObject(arrayListClass, arrayListConstructor, NULL);


                for (int j = 0; j < arclength_eigenvalues[i].curve.size(); j++) { //Pontos dentro de cada subcurva


                    jdoubleArray speedArray = env->NewDoubleArray(dimension);

                    double temp[dimension];

                    temp[0] = arclength_eigenvalues[i].curve[j](0);
                    temp[1] = arclength_eigenvalues[i].curve[j](eigenValueIndex);


                    env->SetDoubleArrayRegion(speedArray, 0, dimension, temp);

                    jobject realVector = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, speedArray);

                    //            cout << arclength_speed[i].curve.at(j) << endl;

                    env->CallObjectMethod(speedLinePartList, arrayListAddMethod, realVector);


                }

                env->CallObjectMethod(eigenLineList, arrayListAddMethod, speedLinePartList);

            }

            jobject eigenLine = env->NewObject(diagramLineClass, diagramLineConstructor, eigenLineList);

//            env->CallObjectMethod(diagramLinesList, arrayListAddMethod, eigenLine);

        }







        //






        jobject diagram = (env)->NewObject(diagramClass, diagramConstructor, diagramLinesList);

        return diagram;




    }




