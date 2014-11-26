/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) EvaluationsGrid.h
 **/

#ifndef TRIANGLE_DOMAIN_GRID_H
#define TRIANGLE_DOMAIN_GRID_H

#define RP_SUCCESSFUL 1

/*
 *  Includes:
 */
 
#include <stdio.h>
#include <stdlib.h>

/*
 * 			Definitions:
 */

#include "FunctionsResultsDefinitions.h"

class TriangleDomainGrid : public EvaluationsGrid {
	
  public:
	TriangleDomainGrid(double limits[2][2], int divisions);
    ~TriangleDomainGrid(void);    
  	
  private:

};

#endif //	TRIANGLE_DOMAIN_GRID_H