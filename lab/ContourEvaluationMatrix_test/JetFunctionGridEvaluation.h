
/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) FunctionEvaluations.h
 **/

#ifndef JET_FUNCTION_GRID_EVALUATION_H
#define JET_FUNCTION_GRID_EVALUATION_H

#define RP_SUCCESSFUL 1

/*
 *  Includes:
 */
 
#include <stdio.h>
#include <stdlib.h>
#include "MultipleMatrix.h"
#include "RpFunction.h"

/*
 * 			Definitions:
 */

class JetFunctionGridEvaluation {
	
  public:

  /*
   * Important Notice: due working time constraints, I did not take into consideration WaveState on this 
   * implementation of code, it is just enough to run Contour properly, i.e., the double coordinates.
   * 
   * Suggestion for future implementations: Create a WaveGenerator class and paas it as constructor parameter.
   * 
   */
   
	JetFunctionGridEvaluation(RpFunction &function, EvaluationsGrid &grid);      
	~JetFunctionGridEvaluation(void);    
    
    JetMatrix& getJet(int indexes[], JetMatrix &jet); 
    
  private:  
  	EvaluationsGrid grid;
  	  	
};

#endif //	JET_FUNCTION_GRID_EVALUATION_H