/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) MultipleLoop.h
 **/

#ifndef MULTIPLE_LOOP_H
#define MULTIPLE_LOOP_H


#define RP_SUCCESSFUL 1

/*
 ** ---------------------------------------------------------------
 ** Includes:
 */
#include <stdio.h>
#include <stdlib.h>

/*
 ** ---------------------------------------------------------------
 ** Definitions:
 */


/*!@brief  ThE MultipleLoop class defines operators to deal with a Loop of N-Dimension
 *
 */
class MultipleLoop {
    
public:
	MultipleLoop(int** loopLimits, int dim);   
    ~MultipleLoop(void);
    
    int getLoopSize();
    int* getIndex(int pos);
  
  private:
   	int numberOfLoops;
 	int *limits;
	int *pt1;

	int loopsize;
};



#endif //MULTIPLE_LOOP_H