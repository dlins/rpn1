/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) LSODEStopGenerator.h
 */

#ifndef _LSODEStopGenerator_H
#define _LSODEStopGenerator_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "ODEStopGenerator.h"
#include "LSODEProfile.h"
#define SUCCESSFUL_PROCEDURE 2
//#define ABORTED_PROCEDURE(-7)
//#define COMPLEX_EIGENVALUE(-7)
//#define LAMBDA_ERROR(-7)
//#define LAMBDA_NOT_INCREASING(-7)
//#define LAMBDA_NOT_DECREASING(-7)



/*
 * ---------------------------------------------------------------
 * Definitions:
 */


class LSODEStopGenerator :public ODEStopGenerator{
    
private:
    
    
    //Stop Criterion
    
    
    int totalPoints_;
    int maxPoints_;
    int functionStatus_;
    LSODEProfile * profile_;
    
public:
    
    LSODEStopGenerator(const LSODEStopGenerator &);
    LSODEStopGenerator(const LSODEProfile &);
    
    
    LSODEStopGenerator * clone() const ;
    virtual ~LSODEStopGenerator();
    
    
    void ncreaseTotalPoints();
    
    int getMaxPoints() const ;
    
    void increaseTotalPoints();
    void setFunctionStatus( int) ;
    int getFunctionStatus() const ;
    
    int totalPoints() const ;
    void setTotalPoints(const int );
    
    bool check (const RealVector &)const ;
    
    
};

#endif //! _LSODEStopGenerator_H
