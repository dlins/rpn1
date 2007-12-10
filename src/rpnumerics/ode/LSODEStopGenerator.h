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
    
    int functionStatus_;
    int totalPoints_;
    int maxPoints_;
    
    
public:
    
    
    LSODEStopGenerator(const int );

    LSODEStopGenerator * clone() const ;
    virtual ~LSODEStopGenerator();
    
    void setFunctionStatus(const int);
    int getFunctionStatus() const ;
    
    int totalPoints() const ;
    void setTotalPoints(const int );
    
    bool getStatus()const ;
    
    
};

#endif //! _LSODEStopGenerator_H
