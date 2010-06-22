#ifndef _JETCOMPOSITE_
#define _JETCOMPOSITE_

#include "FluxFunction.h"
#include "fill_with_jet.h"
#include "jetrarefaction.h"
//#include "curves_origin.h"
#include "eigen.h"
#include "Boundary.h"

extern "C" {
//#include "testvar.h"

//#include "clsode.h"
//#include "field.h"
#include "matrix.h"
//#include "alloc.h"
//#include "grid.h" // To generate regular grids (these in turn can be used to generate vector fields)
#include <stdio.h>
#include <stdlib.h>
#include <math.h> // To use fabs
}

extern "C" int composite(int *, double *, double *, double *, int *, double *);
extern "C" int compositecurve(int *, double *, struct store *, int, double *, int, int, double, double, int, 
                     Boundary*,
                     FluxFunction*, int, double*, 
                     double,
                     double*);


#endif // _JETCOMPOSITE_
