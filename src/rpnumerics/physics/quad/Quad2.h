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
#include "Physics.h"
#include "Quad2FluxFunction.h"
//#include "AccumulationFunction.h"
#include "Quad2FluxParams.h"
#include "RectBoundary.h"
#include "Multid.h"
#include "Space.h"

class Quad2 : Physics {

private:
	Quad2FluxParams params_;
	Quad2FluxFunction fluxFunction_;
	//AccumulationFunction accumulationFunction_;
	Boundary * boundary_;

public:
	Quad2(const Quad2FluxParams &);

	const char * FLUX_ID;
	const char * DEFAULT_SIGMA;
	const char * DEFAULT_XZERO;
	const char * ID(void);

	const FluxParams & params(void) const;
	const FluxFunction & fluxFunction(void) const;
        //const AccumulationFunction & accumulation() const;
	const Space & domain(void) const;
	const Boundary & boundary(void) const;
	void boundary (const Boundary & boundary);
};



#endif //! _Quad2_H
