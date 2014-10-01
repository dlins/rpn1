/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) ContinuationShockFlow.h
 */

#ifndef _ContinuationShockFlow_H
#define _ContinuationShockFlow_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "ShockFlow.h"
#include "eigen.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


extern"C" {
    double ddot_(int *, double *, int *, double *, int *);
}

class ContinuationShockFlow : public ShockFlow {
private:

    int familyIndex_;
    int direction_;
    void fill_with_jet(const FluxFunction & flux_object, int n, double *in, int degree, double *F, double *J, double *H);
    RealVector *startPoint_;
    RealVector *referenceVector_;

public:

    ContinuationShockFlow(const RealVector &, const int, const int, const FluxFunction &);

    ContinuationShockFlow(const ContinuationShockFlow &);

    virtual ~ContinuationShockFlow();

    int getFamily()const;

    int direction()const;

    double diff(int n, double x[], double y[]);

    double inner_product(int n, double x[], double y[]);

    double shockspeed(int n, int family, int typeofspeed, double Um[], double Up[]);

    void dH(int n, double Um[], double U[], double *dHdu);

    void normalize(int rows, int cols, double *v);

    int shockfield(int n, double Um[], int m, double *Up, int family, double *dHdu);

    int shock(int *neq, double *xi, double *in, double *out, int *nparam, double *param);

    int flux(const RealVector &, RealVector &);
    int fluxDeriv(const RealVector &, JacobianMatrix &);
    int fluxDeriv2(const RealVector &, HessianMatrix &);

    const RealVector & getStartPoint() const;

    const RealVector & getReferenceVector()const;

    void setReferenceVector(const RealVector & );


    WaveFlow * clone()const;

};

inline int ContinuationShockFlow::getFamily()const {
    return familyIndex_;
}

inline int ContinuationShockFlow::direction()const {
    return direction_;
}


#endif //! _ContinuationShockFlow_H
