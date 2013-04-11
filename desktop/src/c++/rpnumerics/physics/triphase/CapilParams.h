/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) CapilParams.h
 */

#ifndef _CapilParams_H
#define _CapilParams_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */

#include "RealVector.h"
/*
 * ---------------------------------------------------------------
 * Definitions:
 */


class CapilParams {
    
private:
    
//    double acg_;
//    double acw_;
//    double lcg_;
//    double lcw_;
    
    RealVector * data_;
    
    
public:
    
    CapilParams( double acw, double acg, double lcw, double lcg );
    CapilParams();
    virtual ~CapilParams();
    
    void setParam(int ,double);
    double getParam(int );
    int size();
    
    CapilParams(const CapilParams & copy ) ;
    
    double acg( )const ;
    double acw( )const ;
    double lcg( )const ;
    double lcw( )const ;
    
    void reset( );
    
};

#endif //! _CapilParams_H
