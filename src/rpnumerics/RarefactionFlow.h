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

class RarefactionFlow: public WaveFlow{
    
protected:
    
    int familyIndex_;
    
    int timeDirection_;
    
    RealVector * referenceVector_;
   
       
public:

    
    RarefactionFlow(const int, const int ,const FluxFunction &);
    
    RarefactionFlow(const RarefactionFlow &);

    virtual const RealVector & getReferenceVector() const;
    
    virtual void setReferenceVector(const RealVector & );
    
    int getFamilyIndex() const;
    void setFamilyIndex(int);
    
    int direction()const ;
    void setDirection(int);
    

    virtual ~RarefactionFlow();
    
};


inline int RarefactionFlow::getFamilyIndex() const{return familyIndex_;}

inline void RarefactionFlow::setFamilyIndex(int familyIndex ) {familyIndex_=familyIndex;}

inline int RarefactionFlow::direction() const{return timeDirection_;}

inline void RarefactionFlow::setDirection(int timeDirection){timeDirection_=timeDirection;}

inline const RealVector & RarefactionFlow::getReferenceVector() const {return *referenceVector_;}

inline void RarefactionFlow::setReferenceVector(const RealVector & referenceVector) {
    for (int i = 0; i < referenceVector_->size();i++){
        referenceVector_->operator()(i)=referenceVector(i);
    }
}

#endif //! _RarefactionFlow_H
