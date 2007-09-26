//
// File:   JNICurves.h
// Author: edsonlan
//
// Created on August 30, 2007, 2:21 PM
//

#ifndef _JNICURVES_H
#define	_JNICURVES_H

#include <vector>
#include "RealVector.h"
#include "JNIUtil.h"
#include "JNIDefs.h"



class JNICurve{
    
public:
//    static JNICurve * instance(JNIEnv* );
    
    jobject orbitConstructor(const vector<RealVector>, const int timeDirection)  const ;
    
    jobject orbitConstructorTeste(const vector<double *> coords, const int timeDirection) const ;
    
    jobject manifoldConstructor(const jobject, const jobject, int) const;
    
    jobject connectionOrbitConstructor(const jobject, const jobject, const jobject) const;
    
    
    jobject orbitConstructorTesteArray( double ** coords, const int timeDirection) const;
//     "(Lrpnumerics/PhasePoint;[D[D[Lwave/util/RealVector;ILwave/util/RealMatrix2;Lwave/util/RealMatrix2;ILwave/util/RealMatrix2;I)V");
    
    
    jobject stationaryPointConstructor(const double *, const int, const double *, const int,  const double *, const int , jobjectArray , const int , jobject, jobject, const int, jobject, jobject , const int) const ;

    JNICurve(JNIEnv *);
    
    virtual ~JNICurve();
    
private:
    
    
    
    JNIEnv * envPointer;
    
    static JNICurve * instance_;
    
    JNIUtil * utilInstance;
    
    jclass classOrbitPoint_, classOrbit_, classManifold_, classConnectionOrbit_, classStationaryPoint_;
    
    jmethodID orbitPointConstructor_, orbitConstructor_, manifoldConstructor_, connectionOrbitConstructor_, stationaryPointConstructor_;
};

#endif	/* _JNICURVES_H */

