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
    FluxParams(RealVector & params);
    
    FluxParams(const FluxParams & params);
    virtual ~FluxParams(void);
    
    RealVector &params(void) const ;
    RealVector &operator()(void); //TODO Fix implementation
    RealVector operator()(void) const;
    

    void params(const RealVector & params);
    
    double component(int index) const ;
    void component(int index, double value);
    
    bool operator==(const FluxParams & fluxParams); 
    
    
};

FluxParams::FluxParams(RealVector & params) :params_(new RealVector(params.size())) {

int i ;

for (i=0; i< params_->size();i++){
    
    params_->component(i)=params.component(i);
}


}

FluxParams::FluxParams(const int size,  double * coords):params_(new RealVector(size,coords)){}

FluxParams::FluxParams(const FluxParams &params){//TODO Create a range check
    
    int i;
    for (i=0; i < params_->size();i++){
        params_->component(i)=params.component(i);
    }

}


inline FluxParams::~FluxParams() {
    delete params_;
    
}

inline  RealVector & FluxParams::params(void) const {
    return *params_;
}

inline void FluxParams::params(const RealVector & params){//TODO Create a range check
    
    int i;
    
    for (i=0; i < params_->size();i++){
        
        params_->component(i)=params.component(i);
        
    }
    
    
}

inline double FluxParams::component(int index) const {
    return params_->component(index);
}

inline void FluxParams::component(int index, double value) {
    params_->component(index) = value;
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
