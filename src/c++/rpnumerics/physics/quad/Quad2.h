/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) Quad2.h
 **/

#ifndef _Quad2_H
#define _Quad2_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */

#include "SubPhysics.h"
#include "Quad2FluxFunction.h"
#include "Quad2AccumulationFunction.h"
#include "Quad2HugoniotFunction.h"
#include "Quad2FluxParams.h"
#include "RectBoundary.h"
#include "Shock.h"

class Quad2 : public SubPhysics {
private:

    const char * DEFAULT_SIGMA;
    
    const char * DEFAULT_XZERO;

public:
    Quad2(const Quad2FluxParams &);

    Quad2(const Quad2&);

    virtual ~Quad2();

    SubPhysics * clone() const;

    Boundary * defaultBoundary()const;

    void setParams(vector<string>);
    vector<double> * getParams();

};





#endif //! _Quad2_H
