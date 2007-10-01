#include "ODESolverProfile.h"

//ODESolverProfile::ODESolverProfile(){}

ODESolverProfile::ODESolverProfile(int dimension, double delta, RpFunction & function):dimension_(dimension),delta_(delta),function_(function){}
//ODESolverProfile::ODESolverProfile():function_(new ShockRarefaction()){}
