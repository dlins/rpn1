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
#include <iostream>
/*
 * ---------------------------------------------------------------
 * Definitions:
 */


class ContourMethod {
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
    int *storn;
    int *storm;

    int inpdom(double *u);

    double f(double x, double y );


public:

    
int curv2d(int sn, int seglim, double f, double *rect, int *res, int ifirst) ;




};

#endif //! _HugoniotContourMethod_H
