/* IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) AccumulationParams.h
 **/

#ifndef _AccumulationParams_H
#define	_AccumulationParams_H

#include "RealVector.h"

class AccumulationParams {

private:

	RealVector *params_;
	RealVector  *initParams_;

public:
	
	AccumulationParams();
	AccumulationParams(const RealVector & params);
	virtual ~AccumulationParams();

	const RealVector & params(void) const;
	const double component(int index) const;
	void params(int size, double * coords);
	void params(const RealVector & params);
	void component(int index, double value);

	AccumulationParams defaultParams(void);

};


#endif	/* _AccumulationParams_H */

