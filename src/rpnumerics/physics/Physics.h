/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) Physics.h
 **/

#include "FluxFunction.h"
#ifndef _Physics_H
#define	_Physics_H



//! Definition of class Physics.
/*!

 TODO:
 NOTE :

 @ingroup rpnumerics
 */


class Physics {
    
    public :
        
        
        
        //! FluxFunction accessor
        virtual const  FluxFunction & flux() const =0;
        //! AccumulationFunction accessor
        virtual const   AccumulationFunction & accumulation() const =0;
        //! PhaseSpace accessor
        virtual const   PhaseSpace & domain() const =0;
        //! Boundary accessor
        virtual const   Boundary & boundary() const=0;
        
};

#endif	/* _Physics_H */

