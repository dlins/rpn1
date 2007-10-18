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

//! Definition of class WaveFlow.
/*!

 TODO:
 NOTE :

 @ingroup rpnumerics
 */

class WaveFlow : public RpFunction {
    
    
      PhasePoint getXZero();

    void setXZero (PhasePoint  & xzero);

};

#endif	/* _WaveFlow_H */

