/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) TriangleDomainMatrix.h
 **/

#ifndef TRIANGLE_DOMAIN_MATRIX_H
#define TRIANGLE_DOMAIN_MATRIX_H


#define RP_SUCCESSFUL 1

/*
 ** ---------------------------------------------------------------
 ** Includes:
 */
 
#include <stdio.h>
#include <stdlib.h>

/*
 * 			Definitions:
 */

#include "FunctionsResultsDefinitions.h"

class TriangleDomainMatrix : MultipleMatrix {
	
public:
	TriangleDomainMatrix(int divisions);   
    ~TriangleDomainMatrix(void);
    
    int setElement(int* coordinates, void* element);
    int getElement(void** element, int* coordinates);
  
  private:
     int divisions;  
};



#endif //	TRIANGLE_DOMAIN_MATRIX_H