/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class rpnumerics_RPNUMERICS */

#ifndef _Included_rpnumerics_RPNUMERICS
#define _Included_rpnumerics_RPNUMERICS
#ifdef __cplusplus
extern "C" {
#endif
/* Inaccessible static: INCREASING_LAMBDA */
/* Inaccessible static: physics_ */
/* Inaccessible static: libName_ */
/* Inaccessible static: errorControl_ */
/* Inaccessible static: odeSolver_ */
/* Inaccessible static: shockProfile_ */
/* Inaccessible static: rarefactionProfile_ */
/* Inaccessible static: bifurcationProfile_ */
/* Inaccessible static: shockRarefactionProfile_ */
/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    initNative
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_rpnumerics_RPNUMERICS_initNative
  (JNIEnv *, jclass, jstring);

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    setFamilyIndex
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_rpnumerics_RPNUMERICS_setFamilyIndex
  (JNIEnv *, jobject, jint);

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    setTimeDirection
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_rpnumerics_RPNUMERICS_setTimeDirection
  (JNIEnv *, jobject, jint);

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    clean
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_rpnumerics_RPNUMERICS_clean
  (JNIEnv *, jclass);

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    physicsID
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_rpnumerics_RPNUMERICS_physicsID
  (JNIEnv *, jclass);

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    boundary
 * Signature: ()Lwave/util/Boundary;
 */
JNIEXPORT jobject JNICALL Java_rpnumerics_RPNUMERICS_boundary
  (JNIEnv *, jclass);

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    domainDim
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_rpnumerics_RPNUMERICS_domainDim
  (JNIEnv *, jclass);

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    domain
 * Signature: ()Lwave/multid/Space;
 */
JNIEXPORT jobject JNICALL Java_rpnumerics_RPNUMERICS_domain
  (JNIEnv *, jclass);

#ifdef __cplusplus
}
#endif
#endif
