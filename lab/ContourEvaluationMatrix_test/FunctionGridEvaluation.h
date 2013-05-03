/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) FunctionGridEvaluation.h
 **/

#ifndef FUNCTION_GRID_EVALUATION_H
#define FUNCTION_GRID_EVALUATION_H


#define RP_SUCCESSFUL 1

/*
 *  Includes:
 */
 
#include <stdio.h>
#include <stdlib.h>
#include "JetFunctionGridEvaluation.h"

/*
 * 			Definitions:
 */

class FunctionGridEvaluation : public JetFunctionGridEvaluation {
	
  public:
	FunctionGridEvaluation(RpFunction& function, EvaluationsGrid &grid);   
    ~FunctionGridEvaluation(void);    
    
    void getF(int* coordinates, RealVector &F);
    void getDF(int* coordinates, JacobianMatrix &DF);
    void getD2F(int* coordinates, HessianMatrix &D2F);   
  	  
};

#endif // FUNCTION_GRID_EVALUATION_H
