/**A
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) WaveFlow.h
 **/


#ifndef _WaveFlow_H
#define	_WaveFlow_H

#include "RpFunction.h"
#include "RealVector.h"

//! Definition of class WaveFlow.
/*!

 TODO:
 NOTE :

 @ingroup rpnumerics
 */

class WaveFlow : public RpFunctionDeriv2{
    
    
    public :

        //! XZero accessor 
        const  RealVector getXZero();

        //! XZero mutator 
        void setXZero(const RealVector &);
        
};


#endif	/* _WaveFlow_H */

