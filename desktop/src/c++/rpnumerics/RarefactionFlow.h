/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) RarefactionFlow.h
 */

#ifndef _RarefactionFlow_H
#define _RarefactionFlow_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "WaveFlow.h"
#include "WaveState.h"

#include <math.h>

/*
 * ---------------------------------------------------------------
 * Definitions:
 */

/*! Definition of class RarefactionFlow.
 * TODO:
 * NOTE :
 *
 * @ingroup rpnumerics
 */

class RarefactionFlow : public WaveFlow {
private:

    int familyIndex_;

    int timeDirection_;

    RealVector * referenceVector_;


public:

    RarefactionFlow(const RealVector &, const int, const int, const FluxFunction &);

    RarefactionFlow(const RarefactionFlow &);

    const RealVector & getReferenceVector() const;

    void setReferenceVector(const RealVector &);


        void setReferenceVectorComponent(const int, const double);
    
        double getReferenceVectorComponent(const int)const;


    int getFamilyIndex() const;
    void setFamilyIndex(int);

    int direction()const;
    void setDirection(int);


    virtual ~RarefactionFlow();

};

inline int RarefactionFlow::getFamilyIndex() const {
    return familyIndex_;
}

inline void RarefactionFlow::setFamilyIndex(int familyIndex) {
    familyIndex_ = familyIndex;
}

inline int RarefactionFlow::direction() const {
    return timeDirection_;
}

inline void RarefactionFlow::setDirection(int timeDirection) {
    timeDirection_ = timeDirection;
}

inline const RealVector & RarefactionFlow::getReferenceVector() const {
    return *referenceVector_;
}

inline void RarefactionFlow::setReferenceVector(const RealVector & referenceVector) {
    for (int i = 0; i < referenceVector.size(); i++) {
        referenceVector_->component(i) = referenceVector.component(i);
    }
}

inline void RarefactionFlow::setReferenceVectorComponent(const int index, const double value) {
    referenceVector_->component(index) = value;

}

inline double RarefactionFlow::getReferenceVectorComponent(const int index) const {

    return referenceVector_->component(index);

}

#endif //! _RarefactionFlow_H
