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
#include "WaveFlow.h"
#include "eigen.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */

#define SUCCESSFUL_PROCEDURE 2                    
#define ABORTED_PROCEDURE (-7)

extern"C" {
    void dgeev_(char *, char *, int *, double *, int*, double *, double *,
            double *, int *, double *, int *, double *, int *,
            int *);


    double ddot_(int *, double *, int *, double *, int *);
}

class ContinuationShockFlow : public WaveFlow {
private:

    int familyIndex_;
    int direction_;
    int cdgeev(int n, double *A, struct eigen *e) const;
    void fill_with_jet(const FluxFunction & flux_object, int n, double *in, int degree, double *F, double *J, double *H);

public:

    ContinuationShockFlow(const int , const int , const FluxFunction &);

    int getFamily();
    
    int direction();
    
    double diff(int n, double x[], double y[]);
    
    double inner_product(int n, double x[], double y[]);

    double shockspeed(int n, int family, int typeofspeed, double Um[], double Up[]);

    void dH(int n, double Um[], double U[], double *dHdu);
 
    void normalize(int rows, int cols, double *v);

    int shockfield(int n, double Um[], int m, double *Up, int family, double *dHdu);

    int shock(int *neq, double *xi, double *in, double *out, int *nparam, double *param);
};

inline int ContinuationShockFlow::getFamily(){return familyIndex_;}
inline int ContinuationShockFlow::direction(){return direction_;}

#endif //! _ContinuationShockFlow_H
