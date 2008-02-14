/* IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) FluxParams.h
 */

#ifndef _FluxParams_H
#define	_FluxParams_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "RealVector.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


class FluxParams {
    
private:
    RealVector *params_;
    
    
public:
    
    FluxParams(const int size, double *coords);
    FluxParams(const RealVector & params);
    
    FluxParams(const FluxParams & params);
    
    virtual ~FluxParams(void);
    
    RealVector &params(void) const ;

    RealVector operator()(void) const;
    
    
    void set(const RealVector & params);
    
    double component(int index) const ;
    void component(int index, double value);
    
    bool operator==(const FluxParams & fluxParams);
    FluxParams & operator=(const FluxParams &);
    
    
};

inline  RealVector & FluxParams::params(void) const {
    return *params_;
}



inline void FluxParams::set(const RealVector & params){//TODO Create a range check
    
    delete params_;
    params_= new RealVector(params);
    
}

inline double FluxParams::component(int index) const {
    return params_->component(index);
}

inline void FluxParams::component(int index, double value) {
    params_->component(index) = value;
}

inline FluxParams & FluxParams::operator=(const FluxParams & source){
    
    if (*this== source)
        return *this;
    set(source.params());
    return *this;
    
}

inline bool FluxParams::operator==(const FluxParams & fluxParams) {
    int i ;
    
    for (i=0;i < params_->size();i++){
        
        if (params_->component(i)!=fluxParams.component(i))
            return false;
    }
    return true;
}

#endif	//! _FluxParams_H
