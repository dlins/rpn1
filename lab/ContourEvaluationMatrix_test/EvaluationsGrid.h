/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) EvaluationsGrid.h
 **/

#ifndef EVALUATIONS_GRID_H
#define EVALUATIONS_GRID_H

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

class EvaluationsGrid {
	
  public:
	EvaluationsGrid(double limits[][2], int divisions[], int dim);
    ~EvaluationsGrid(void);    
         
   	int getNumberOfDivisions(int divisionsArray[]); 
  	int getLimits(double limitsArray[][2]);
  	int getCoordinates(double coordinatesArray[], int indexesArray[]);  
  	int getIndexes(int indexesArray[], double coordinatesArray[]);
  	
  	int setValueAtIndex(void* object, int indexArray[]);
  	int setValueAtCoordinate(void* object, double coordinatesArray[]);
  	
  	int getValueAtIndex(void** object, int indexArray[]);
  	int getValueAtCoordinate(void** object, double coordinatesArray[]);
  	
  protected:
  	void setMatrix(MultipleMatrix* evaluationsMatrix);
  private:
  	double limits[][2];
  	int divisions[];
  	int dim;
  	
  	MultipleMatrix* evaluationsMatrix; 
};

#endif //	EVALUATIONS_GRID_H