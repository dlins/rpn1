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
/*
 * ---------------------------------------------------------------
 * Definitions:
 */


class CapilParams {
    
private:
    
    double acg_;
    double acw_;
    double lcg_;
    double lcw_;
    
    
public:
    
    CapilParams( double acw, double acg, double lcw, double lcg );
    
    virtual ~CapilParams();
    
    CapilParams(const CapilParams & copy ) ;
    
    double acg( )const ;
    double acw( )const ;
    double lcg( )const ;
    double lcw( )const ;
    
    void reset( );
    
};

#endif //! _CapilParams_H
