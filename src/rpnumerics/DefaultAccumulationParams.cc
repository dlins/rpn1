/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) DefaultAccumulationParams.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "DefaultAccumulationParams.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */

inline AccumulationParams DefaultAccumulationParams::defaultParams(void)
{
	return * new DefaultAccumulationParams();
}
