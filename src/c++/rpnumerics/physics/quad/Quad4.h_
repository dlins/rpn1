/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) Quad2.h
 **/

#ifndef _Quad4_H
#define _Quad4_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */

#include "SubPhysics.h"
#include "Quad4FluxFunction.h"
#include "Quad4AccumulationFunction.h"
#include "Quad4FluxParams.h"
#include "RectBoundary.h"
#include "Multid.h"
#include "Space.h"

class Quad4 : public SubPhysics {
private:



    char * FLUX_ID;

    const char * DEFAULT_SIGMA;
    const char * DEFAULT_XZERO;


public:
    Quad4(const Quad4FluxParams &);

    Quad4(const Quad4&);

    virtual ~Quad4();

    SubPhysics * clone() const;

    Boundary * defaultBoundary()const;
    vector<double> *  getParams();


};


#endif //! _Quad4_H
