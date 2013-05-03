/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) Capillarity.h
 */

#ifndef _Capillarity_H
#define _Capillarity_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "CapilParams.h"
#include  "RealVector.h"
#include  "RealMatrix2.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


class Capillarity {
    
private:
    
    CapilParams * params_;
    
public:
    
    Capillarity(const CapilParams & );
    
    Capillarity(const Capillarity &);
    
    virtual ~Capillarity();
        
    void jacobian(const RealVector & U, RealMatrix2 &)const ;
    
    double dpcowdsw(double sw)const;
    
    double dpcogdsw(double sg)const;
    
    double dpcogdso(double sg)const;
    
    CapilParams & params()const;
    
    
};

inline  CapilParams & Capillarity::params() const{ return *params_;}
    
    

#endif //! _Capillarity_H
