/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) CompositeFlow.h
 */

#ifndef _CompositeFlow_H
#define _CompositeFlow_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "WaveFlow.h"
#include "RarefactionFlow.h"
#include "ShockFlow.h"
#include "eigen.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */



extern"C" {
//    void dgeev_(char *, char *, int *, double *, int*, double *, double *,
//            double *, int *, double *, int *, double *, int *,
//            int *);

    void dgesv_(int *, int *, double *, int *, int *, double *, int *, int *);

}

class CompositeFlow : public WaveFlow {
private:

    int composite(int *neq, double *xi, double *in, double *out, int *nparam, double *param);

    void fill_with_jet(const FluxFunction & flux_object, int n, double *in, int degree, double *F, double *J, double *H);


    int cdgesv(int n, double *A, double *b, double *x);

    RarefactionFlow * rarefactionFlow_;

    ShockFlow * shockFlow_;

    int family_;

public:
    CompositeFlow(int, const RarefactionFlow &, const ShockFlow &, const FluxFunction &);
    CompositeFlow(const CompositeFlow &);

    int flux(const RealVector &, RealVector &);
    int fluxDeriv(const RealVector &, JacobianMatrix &);
    int fluxDeriv2(const RealVector &, HessianMatrix &);
    int getFamily() const;

    const RarefactionFlow & getRarefactionFlow()const ;
    
    const ShockFlow & getShockFlow()const ;
    
    WaveFlow * clone()const;


};

inline int CompositeFlow::getFamily()const {
    return family_;
}

#endif //! _CompositeFlow_H
