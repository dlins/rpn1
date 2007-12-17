/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) LSODEProfile.h
 */

#ifndef _LSODEProfile_H
#define _LSODEProfile_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "ODESolverProfile.h"
#include "LSODEStopGenerator.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */
// The interface to the LSODE solver.
//
// The parameters are:
//
//         f: The field's function
//       neq: The number of equations
//         y: The point where the solution is to be computed --  entrada !!
//         t: The time at which the solution will begin to be computed  -- para ODESolution
//      tout: The time at which the solution stops being computed -- para ODESolution
//      itol: 1 (scalar) or 2 (array), see atol (below)
//      rtol: Relative tolerance parameter (scalar)
//      atol: Absolute tolerance parameter (scalar or array), see itol
//     itask: 1 for normal computation of of output values of y at t = tout
//    istate: Integer flag. Set istate = 1.
//      iopt: 0 to indicate no optional inputs are used
//     rwork: Array of reals, of length at least:
//       lrw= 20 + 16*neq                      for mf = 10,
//            22 +  9*neq + neq**2             for mf = 21 or 22,
//            22 + 10*neq + (2*ml + mu)*neq    for mf = 24 or 25.
//       lrw: Declared length of rwork
//     iwork: Array of integers of length at least:
//      liw=  20                               for mf = 10,
//            20 + neq                         for mf = 21, 22, 24 or 25.
//            If mf = 24 or 25, iwork(0) = ml and iwork(1) = mu.
//       liw: Declared length of iwork
//         j: The field's Jacobian, for mf = 24 or 25. Otherwise, pass
//            a dummy function.
//        mf: Method flag:
//            10: Nonstiff (Adams) method, no Jacobian used.
//            21: Stiff (BDF) method, user-supplied full Jacobian.
//            22: Stiff method, internally generated full Jacobian.
//            24: Stiff method, user-supplied banded Jacobian.
//            25: Stiff method, internally generated banded Jacobian.
//
// The output is stored at:
//         y: Thus, y is a vector for input/output.
//         t: Normally should be tout.
//    istate: Takes one of the following at output:  -- para ODESolution
//             2: Successful,
//            -1: Excessive work performed at this call, perhaps mf is wrong,
//            -2: Excessive accuracy requested (tolerance is too small),
//            -3: Illegal input detected (see printed output),
//            -4: Repeated error tests failures (check all input values),
//            -5: Repeated convergence failures (check Jacobian and mf),
//            -6: Error weight became zero, atol or atol(i) vanished.
//
//            -7: f or jac returned with an error. This was added by R. Morante
//                to the lsode solver in Fortran.

class LSODEProfile:public ODESolverProfile {
    
private:
    
    
//         f: The field's function
//         j: The field's Jacobian, for mf = 24 or 25. Otherwise, pass
//         a dummy function.
    
    
    double rtol_;
    double * atol_;
    double deltaxi_;
    
    int  neq_;
    int itol_;
    int itask_;
    
    int  iopt_;
    int   lrw_;
    int   liw_;
    int    mf_;
    int paramLength_;
    
    int * iwork_;
    double *  rwork_;
    double *  param_;
    
    
    
public:
//              neq  ,  itol, rtol, itask,istate, iopt , rwork , lrw ,iwork, liw , mf,
    LSODEProfile(const RpFunction &, const LSODEStopGenerator &, int ,  int , double, int ,double , int , const double * );
    LSODEProfile(const LSODEProfile &);
    
    ~LSODEProfile();
    
    LSODEProfile & operator=(const LSODEProfile &);
    
    int numberOfEquations() const ;
    
    int absoluteToleranceType() const ;
    int lengthRWork() const ;
    int lengthIWork() const ;
    int task() const ;
    
    int opt()const ;
    int methodFlag()const  ;
    double relativeTolerance() const ;
    int paramLength() const;    
    double absoluteToleranceComponent(const int) const ;    
    double deltaTime()const ;
    

    int  iworkComponent(const int )const;
    double  rworkComponent(const int)const  ;
    

    double paramComponent(const int) const ;
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
};

#endif //! _LSODEProfile_H
