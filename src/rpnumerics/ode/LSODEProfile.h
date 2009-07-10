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
#include  "Boundary.h"
#include <math.h>

/*
 * ---------------------------------------------------------------
 * Definitions:
 */

#define SUCCESSFUL_PROCEDURE 2


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

class LSODEProfile:public ODESolverProfile{
    
private:
    
    
//         f: The field's function
//         j: The field's Jacobian, for mf = 24 or 25. Otherwise, pass
//         a dummy function.
    
    double * atol_;
    double *  rwork_;
    double *  param_;
    
    int * iwork_;
    
    double deltaxi_;
    double rtol_;
    
    int  neq_;
    int itol_;
    int itask_;
    
    int  iopt_;
    int   lrw_;
    int   liw_;
    int    mf_;
    int paramLength_;
    Boundary * boundary_;
    int maxStepNumber_;

    
public:
    
    LSODEProfile(const WaveFlow &, const Boundary &, int NmaxSteps, int ,  int , double, int , double , int , const double * );
    
    LSODEProfile(const LSODEProfile &);
    
    virtual ~LSODEProfile();
    
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

    void setParamComponent(int ,const double);
    
    const Boundary & boundary()const ;
    
    int maxStepNumber()const;

    
};

inline const Boundary & LSODEProfile::boundary()const {return *boundary_;}

inline int LSODEProfile::maxStepNumber()const {return maxStepNumber_;}

inline double LSODEProfile::relativeTolerance()const{return rtol_;}
inline int LSODEProfile::task()const{return itask_;}
inline int LSODEProfile::opt()const{return iopt_;}

inline double  LSODEProfile::rworkComponent(const int i) const {return rwork_[i];}
inline int  LSODEProfile::iworkComponent(const int i) const {return iwork_[i];}

inline int LSODEProfile::lengthRWork()const {return lrw_;}
inline int LSODEProfile::lengthIWork() const{return liw_;}

inline  int LSODEProfile::methodFlag() const{return mf_;}

inline double LSODEProfile::deltaTime() const {return deltaxi_;}

inline int LSODEProfile::paramLength() const {return paramLength_;}

inline  void LSODEProfile::setParamComponent(const int index,const double newValue){param_[index]=newValue;}

inline double  LSODEProfile::paramComponent(const int index) const {
    
    if (index < 0 || index > paramLength_){
        
        cout << "Erro in param length! "<< endl;
        return 0;
    }
    
    return param_[index];
}
inline int LSODEProfile::absoluteToleranceType() const {return itol_;}
inline int LSODEProfile::numberOfEquations()const{return neq_;}

inline double LSODEProfile::absoluteToleranceComponent(const int index)const{
    
    if (index <0 || index > neq_) {
        
        cout << "Error in absolute tolerance index" << endl;
        return 0;
    }
//    cout <<"Valor de atol "<<index<<" "<<atol_[index]<<endl;
    return atol_[index];
}

#endif //! _LSODEProfile_H
