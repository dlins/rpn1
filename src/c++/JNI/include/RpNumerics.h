/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) RpNumerics.h
 **/

#ifndef _RpNumerics_H
#define	_RpNumerics_H


#include "StationaryPoint.h"
#include "WaveCurve.h"
#include "eigen.h"
#include <map>

#include "RectBoundary.h"
#include "IsoTriang2DBoundary.h"


#include "JDSubPhysics.h"
#include "StoneSubPhysics.h"
#include "CoreyQuadSubPhysics.h"


#include "JNIDefs.h"

#include <vector>
#include <string.h>
#include <iostream>

class RpNumerics {
private:

    static vector<StationaryPoint* > * stationaryPointVector_;

    static double sigma;


public:
    static map<int, WaveCurve *> *waveCurveMap_;

    static std::vector<SubPhysics*> * physicsVector_;

    static std::vector<Parameter*> * physicsParams_;

    static std::map<string, AuxiliaryFunction *> * physicsAuxFunctionsMap_;

    static WaveCurve * getWaveCurve(int);



    static void addWaveCurve(int, WaveCurve *);

    static int getCurrentCurveID();

    static void increaseCurveID();

    static vector<StationaryPoint *> & getStationaryPointVector();

    static const FluxFunction & getFlux();

    static const AccumulationFunction & getAccumulation();

    static void setSigma(double);

    static double getSigma();

    static void clean();

    static void clearCurveMap();

    static void removeCurve(int);

    static void setEigenOrderFunction(const string &);

    static map<string, bool (*)(const eigenpair&, const eigenpair&) > *orderFunctionMap_;

    static void fillPhysicsParams();





};

inline WaveCurve * RpNumerics::getWaveCurve(int n) {
    return waveCurveMap_->at(n);
}

inline void RpNumerics::addWaveCurve(int curveID, WaveCurve * wc) {
    waveCurveMap_->insert(std::pair<int, WaveCurve *>(curveID, wc));


}

inline vector<StationaryPoint *> & RpNumerics::getStationaryPointVector() {
    return *stationaryPointVector_;
}

inline void RpNumerics::setEigenOrderFunction(const string & functionName) {

    Eigen::set_order_eigenpairs(orderFunctionMap_->at(functionName));

}

inline void RpNumerics::clearCurveMap() {


    for (std::map<int, WaveCurve *>::iterator it = waveCurveMap_->begin(); it != waveCurveMap_->end(); ++it) {
        delete it->second;
    }

    waveCurveMap_->clear();

}

inline void RpNumerics::removeCurve(int curveID) {


    if (waveCurveMap_->count(curveID) == 1) {
        delete waveCurveMap_->at(curveID);
        waveCurveMap_->erase(curveID);
    }


}

inline void RpNumerics::setSigma(double s) {
    sigma = s;
}

inline double RpNumerics::getSigma() {
    return sigma;
}






#endif	/* _JNIDEFS_H */

