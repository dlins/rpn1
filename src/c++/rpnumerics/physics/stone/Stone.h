/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) Stone.h
 */

#ifndef _Stone_H
#define _Stone_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "SubPhysics.h"
#include "IsoTriang2DBoundary.h"
#include "StoneParams.h"
#include "AccumulationFunction.h"
#include "Multid.h"
#include "StoneFluxFunction.h"
#include "StoneHugoniotFunctionClass.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */



class Stone:public SubPhysics {

private:


    
public:


    Stone(const Stone &);

    Stone();

    virtual ~Stone();

    Boundary * defaultBoundary()const;


    SubPhysics * clone()const;

};




#endif //! _Stone_H
