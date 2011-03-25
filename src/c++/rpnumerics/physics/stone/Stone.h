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
#include "AccumulationFunction.h" // Isto não deveria ser necessário. Ver se pode ser eliminado.
#include "Multid.h"               // Isto não deveria ser necessário. Ver se pode ser eliminado.
#include "StoneFluxFunction.h"

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
