/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) JNIOrbitCalc.cc
 **/


//! Definition of JNIOrbitCalc
/*!
	
TODO:
	
NOTE : 

@ingroup JNI
 */


#include "rpnumerics_TransitionalLineCalc.h"

#include "BifurcationCurve.h"
#include "RpNumerics.h"
#include "JNIDefs.h"
#include <vector>

using std::vector;

/*
 * Class:     rpnumerics_TransitionalLineCalc
 * Method:    calc
 * Signature: (Ljava/lang/String;)Lrpnumerics/RpSolution;
 */
JNIEXPORT jobject JNICALL Java_rpnumerics_TransitionalLineCalc_calc
(JNIEnv * env, jobject obj, jstring tName) {

    jclass stringClass = env->FindClass("Ljava/lang/String;");

    jclass realVectorClass = env->FindClass(REALVECTOR_LOCATION);
    
    jclass transitionalClass = env->FindClass("Lrpnumerics/TransitionalLine;");
    jclass classOrbit = (env)->FindClass(ORBIT_LOCATION);
    
    jclass classOrbitPoint = (env)->FindClass(ORBITPOINT_LOCATION);
    
    jmethodID transistionlLineConstructor = (env)->GetMethodID(transitionalClass, "<init>", "([Lrpnumerics/OrbitPoint;Ljava/lang/String;)V");
    
    
    jmethodID orbitPointConstructorID = (env)->GetMethodID(classOrbitPoint, "<init>", "(Lwave/util/RealVector;)V");
    jmethodID orbitConstructor_ = (env)->GetMethodID(classOrbit, "<init>", "([Lrpnumerics/OrbitPoint;I)V");
    jmethodID toDoubleMethodID = (env)->GetMethodID(classOrbitPoint, "toDouble", "()[D");
    jmethodID realVectorConstructorDoubleArrayID = env->GetMethodID(realVectorClass, "<init>", "([D)V");
    
    

    BifurcationCurve * bifurcation = RpNumerics::physicsVector_->at(0)->bifurcation_curve();
    
    string nativeName(env->GetStringUTFChars(tName, NULL));
    
    std::vector<int> type;
    std::vector<std::string> name;
    std::vector<void*> object;
    std::vector<double (*)(void*, const RealVector &) > function;



    
    bifurcation->list_of_secondary_bifurcation_curves(type, name, object, function);
    

   

    vector <RealVector> coords;
    
    for (int i = 0; i < name.size(); i++) {

        if(name[i].compare(nativeName)==0){

            BifurcationCurve * bifurcationCurve = (BifurcationCurve *) object[i];
            bifurcationCurve->curve(type[i],coords);
            
        }

    }
    
    
    
  


   jobjectArray orbitPointArray = (jobjectArray) (env)->NewObjectArray(coords.size(), classOrbitPoint, NULL);


    //Orbit memebers creation
    for (int i = 0; i < coords.size(); i++) {

        RealVector tempVector = coords.at(i);

        double * dataCoords = tempVector;


        //Reading only coodinates
        jdoubleArray jTempArray = (env)->NewDoubleArray(tempVector.size());

        (env)->SetDoubleArrayRegion(jTempArray, 0, tempVector.size(), dataCoords);

        jobject realVectorCoords = env->NewObject(realVectorClass, realVectorConstructorDoubleArrayID, jTempArray);

        //Lambda is the last component.
        jobject orbitPoint = (env)->NewObject(classOrbitPoint, orbitPointConstructorID, realVectorCoords);

        (env)->SetObjectArrayElement(orbitPointArray, i, orbitPoint);

        env->DeleteLocalRef(jTempArray);

        env->DeleteLocalRef(orbitPoint);

    }

    //Building the orbit


    jobject orbit = (env)->NewObject(transitionalClass,transistionlLineConstructor, orbitPointArray, tName);



    return orbit;








}
