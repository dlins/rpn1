#include "RK4SolverProfile.h"


RK4SolverProfile::RK4SolverProfile(ODEStopEvaluator & sGenerator):ODESolverProfile(sGenerator){}

    

double RK4SolverProfile::getDeltat(){return deltat_;}
