/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) WaveCurve.h
 */

#ifndef _WaveCurve_H
#define _WaveCurve_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include <vector>
#include "RPnCurve.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


class WaveCurve : public RPnCurve {
private:
    vector <RPnCurve> curvesVector_;
    vector <double> speedArray_;
    int familyIndex_;
    RealVector & referenceVector_;

public:
    WaveCurve(vector<RealVector>,int );
    WaveCurve(vector <RPnCurve> ,int );
    void add(const RPnCurve &);
    vector<RPnCurve> getCurves()const;
    int getFamilyIndex()const;
    vector<double> getSpeed()const;
    void addSpeed(double);
    void setReferenceVector(const RealVector &);
    const RealVector & getRefereceVector()const;
    virtual const string  getType() const =0;
    ~WaveCurve();
};

inline void WaveCurve::add(const RPnCurve & curve) {
    curvesVector_.push_back(curve);
}

inline vector<RPnCurve> WaveCurve::getCurves()const {
    return curvesVector_;
}

inline int WaveCurve::getFamilyIndex() const {
    return familyIndex_;
}

inline vector<double> WaveCurve::getSpeed()const {
    return speedArray_;
}

inline void WaveCurve::addSpeed(double speed) {
    speedArray_.push_back(speed);
}

inline const RealVector & WaveCurve::getRefereceVector() const {
    return referenceVector_;
}

inline void WaveCurve::setReferenceVector(const RealVector& newReference){
    referenceVector_=newReference;
}

#endif //! _WaveCurve_H
