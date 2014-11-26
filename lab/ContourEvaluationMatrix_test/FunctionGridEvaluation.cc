
#include "FunctionGridEvaluation.h"

FunctionGridEvaluation::FunctionGridEvaluation(RpFunction& function, int** boundaries, int* divisions) : JetFunctionGridEvaluation(function, boundaries, divisions){

}

FunctionGridEvaluation::~FunctionGridEvaluation(void) {

}
    
RealVector FunctionGridEvaluation::getF(int* coordinates, RealVector &F) {

}

JacobianMatrix FunctionGridEvaluation::getDF(int* coordinates, JacobianMatrix &DF) {

}

HessianMatrix FunctionGridEvaluation::getD2F(int* coordinates, HessianMatrix &D2F) {

}