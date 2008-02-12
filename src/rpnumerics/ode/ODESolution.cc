#include "ODESolution.h"

int ODESolution::STOP_OUT_OF_BOUNDARY =1;
int ODESolution::STOP_ON_POINCARE_SECTION=2;
int ODESolution::MADE_MAXIMUM_STEP_NUMBER=0;


ODESolution::ODESolution(vector <RealVector>  coords , vector <double> times , int flag ):coords_(coords),times_(times),flag_(flag){}


ODESolution::~ODESolution(){
    coords_.clear();
    times_.clear();
    
}

int ODESolution::getFlag(){return flag_;}





