/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) MultipleMatrix.h
 **/

#ifndef MULTIPLE_MATRIX_H
#define MULTIPLE_MATRIX_H


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

/*
 * 
 * 			The MultipleMatrix class defines operators to deal with a Matrix of N-Dimension of void elements.
 * 			
 * 			ALERT!!! The elements pointed by the matrix coordinates are not destroyed when the object destructor 
 * 			is called. It is up to the client to do that.
 * 
 */
 
class MultipleMatrix {
	
  public:
	MultipleMatrix();
	MultipleMatrix(int[] matrixLimits, int dim);   // vai de zero a n-1, estah errado...
    ~MultipleMatrix(void);
    
    int setElement(int* coordinates, void* element);
    int getElement(void** element, int* coordinates);
  
  protected:
  	void setMatrixLimits(int[] matrixLimits);
  	
  private:
   	int numberOfMatrixes;
  	int* lengths;
  	int loopsize;
  	
  	void** matrix;
  	bool matrixCreated; 
  	
  	int getPos(int* A); 
  	  
};



#endif //	MULTIPLE_MATRIX_H