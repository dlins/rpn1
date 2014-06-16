/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) Quad3.h
 **/

#ifndef _Quad3_H
#define _Quad3_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */

#include "SubPhysics.h"
#include "Quad3FluxFunction.h"
#include "Quad3AccumulationFunction.h"
#include "Quad3FluxParams.h"
#include "RectBoundary.h"
#include "Multid.h"
#include "Space.h"

class Quad3 : public SubPhysics {
private:

    const char * DEFAULT_SIGMA;
    const char * DEFAULT_XZERO;




public:
    Quad3(const Quad3FluxParams &);

    Quad3(const Quad3&);

    virtual ~Quad3();

    const char * ID(void)const;

    SubPhysics * clone() const;

    Boundary * defaultBoundary()const;
    
    vector<double> *  getParams();


};



#endif //! _Quad3_H
