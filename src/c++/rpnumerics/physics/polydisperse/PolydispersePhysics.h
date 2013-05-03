/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) PolydispersePhysics.h
 */

#ifndef _PolydispersePhysics_H
#define _PolydispersePhysics_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "Polydisperse.h"
#include "SubPhysics.h"
#include "IsoTriang2DBoundary.h"
#include "StoneAccumulation.h"
#include "Quad2AccumulationFunction.h"


/*
 * ---------------------------------------------------------------
 * Definitions:
 */


//  SubPhysics(const FluxFunction &,const AccumulationFunction &,const Boundary &,const Space &,const char *,int );

class PolydispersePhysics:public SubPhysics {

private:
 Boundary * defaultBoundary() const;


public:

//    PolydispersePhysics(const Polydisperse &, const Boundary &, const Space &);

    void setParams(vector<string>);
    vector<double> * getParams();

    PolydispersePhysics();

    PolydispersePhysics(const PolydispersePhysics &);
    virtual ~PolydispersePhysics();

    SubPhysics * clone()const;


};




#endif //! _PolydispersePhysics_H
