/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) HugoniotContourMethod.h
 */

#ifndef _HugoniotContourMethod_H
#define _HugoniotContourMethod_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "HyperCube.h"
#include "RealSegment.h"
#include "HugoniotFunctionClass.h"

#include "ShockMethod.h"


#include <iostream>
#include <vector>

/*
 * ---------------------------------------------------------------
 * Definitions:
 */

class Test : public HugoniotFunctionClass {
public:

    double HugoniotFunction(const RealVector &p) {
        return p.component(0) * p.component(0) + p.component(1) * p.component(1) - 0.2;
    }
};

class ContourMethod : public ShockMethod {
private:
    int * res;
    double * rect;

    int dimension;

    HyperCube hc;

    //Mkcube pointers
    int * cvert_;
    int *ncvert;
    int *bsvert_;
    int *perm_;

    //Mkcube pointers
    int *face_;
    int *facptr_;
    int *fnbr_; // face_[m_ + 1][dimf_], facptr_[nsimp_][nsface_]
    int dimf_;
    int nsimp_;
    int *comb_; // bsvert_[n_ + 1][n_], comb_[numberOfCombinations][m_ + 1]
    int *storn_;
    int *storm_;

    int inpdom(double *u);

    //double f(double x, double y );

    HugoniotFunctionClass *hugoniot;

protected:
public:

    ContourMethod(int dimension,const FluxFunction &, const AccumulationFunction &, const Boundary &, HugoniotFunctionClass *);

    ~ContourMethod();

    //int curv2d(int sn, int seglim, double f, double *rect, int *res, int ifirst) ;
    //int curv2d(int sn, int seglim, double f, double *rect, int *res, int ifirst, std::vector<RealSegment> &vrs) ;
    int curv2d(int sn, int seglim, double f, double *rect, int *res, int ifirst, std::vector<RealVector> &vrs);

    void curve(const RealVector &, vector<HugoniotPolyLine> &);


};

#endif //! _HugoniotContourMethod_H

