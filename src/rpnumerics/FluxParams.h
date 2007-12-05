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




#endif	//! _FluxParams_H
