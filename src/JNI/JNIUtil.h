//
// File:   JNIUtil.h
// Author: edsonlan
//
// Created on August 27, 2007, 4:46 PM
//

#ifndef _JNIUTIL_H
#define	_JNIUTIL_H
#include <jni.h>

class JNIUtil {
    
public:
    
    jobject phasePointConstructor( double * coords, const int coordssize) const;
    
    jobjectArray realVectorArrayConstructor(double ** data, const int datazise, const int elementsize) const;
    
    jobject realMatrixConstructor(double *, const int ) const;
    
//    jobject realVectorConstructor(const int size) const ;
    
    jobject realVectorConstructor(double * data, const int datasize ) const ;
    
    double * phasePointToDouble(const jobject,int &)const ;
    
    //TODO Instavel !!
    double * orbitPointToDouble (const jobject,int &) const ;
    
    jobject arrayListConstructor();
    
    void arrayListAdd(jobject list, jobject element ) const; //TODO procurar a assinatura do metodo add da classe Array List !!!

    JNIUtil(JNIEnv *);
    
    virtual ~JNIUtil();
    
private:
    

    
//    static JNIUtil * instance_;
    
    JNIEnv * envPointer;
    
    jclass orbitPointClass_,phasePointClass_, realMatrix2Class_, realVectorClass_, arrayListClass_;
    
    jmethodID phasePointConstructor_, realMatrix2Constructor_, realVectorConstructorDoubleArray_,
            realVectorConstructorSize_, arrayListConstructor_, arrayListAddMethod_,toDoubleMethodID_,toDoublePhasePointMethodID_;
    
};

#endif	/* _JNIUTIL_H */

