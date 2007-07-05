#ifndef RK4_H
#define RK4_H

#include "testvar.h"


#ifdef TESTRK4
#define TEST_RK4
#endif 



#define SUCCESSFUL_PROCEDURE 0                    
#define ABORTED_PROCEDURE !SUCCESSFUL_PROCEDURE

#include <stdio.h>
#include "VectorField.h"



int RK4(int, double, double, double *, double *, int(*)(int, double, double*, double*));


#endif // RK4_H
