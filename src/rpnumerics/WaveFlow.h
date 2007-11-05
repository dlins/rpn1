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
<<<<<<< .mine
#include "PhasePoint.h"
=======
>>>>>>> .r236

//! Definition of class WaveFlow.
/*!

 TODO:
 NOTE :

 @ingroup rpnumerics
 */

class WaveFlow : public RpFunction {
    
<<<<<<< .mine
public:
	~WaveFlow(void);
=======
    
      PhasePoint getXZero();
>>>>>>> .r236

<<<<<<< .mine
	virtual const PhasePoint XZero(void) const = 0;
	virtual void XZero(const PhasePoint & xzero) const = 0;
=======
    void setXZero (PhasePoint  & xzero);
>>>>>>> .r236

};

<<<<<<< .mine
inline WaveFlow::~WaveFlow(void)
{
}

=======
>>>>>>> .r236
#endif	/* _WaveFlow_H */

