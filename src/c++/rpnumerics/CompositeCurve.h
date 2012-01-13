#ifndef _COMPOSITECURVE_
#define _COMPOSITECURVE_

#include <vector>
#include <stdio.h>

//#include "RealVector.h"
#include "FluxFunction.h"
#include "AccumulationFunction.h"
#include "Shock.h"
#include "Boundary.h"
//#include "eigen.h"
//#include "ShockMethod.h"


//extern "C" void dgesv_(int*, int*, double*, int*, int*, double*, int*, int*);

#ifndef COMPOSITE_OK
#define COMPOSITE_OK 0
#endif

#ifndef COMPOSITE_ERROR
#define COMPOSITE_ERROR 1
#endif

#ifndef COMPOSITE_FROM_NORMAL_RAREFACTION
#define COMPOSITE_FROM_NORMAL_RAREFACTION 2
#endif

#ifndef COMPOSITE_FROM_STACK_RAREFACTION
#define COMPOSITE_FROM_STACK_RAREFACTION 3
#endif

#ifndef COMPOSITE_FROM_NORMAL_RAREFACTION_START
#define COMPOSITE_FROM_NORMAL_RAREFACTION_START 3
#endif

class CompositeCurve {
    private:
//        FluxFunction         *F;
//        AccumulationFunction *G;
//        Boundary *boundary;

//        double zero_level_function(const RealVector &rarpoint, RealVector &Un, int &info);
//        int cdgesv(int n, double *A, double *b, double *x);
//        
    protected:
    public:
        static void curve(const std::vector<RealVector> &rarcurve, int origin, int family, int increase, 
                          FluxFunction *ff, AccumulationFunction *aa, 
                          Boundary *boundary, std::vector<RealVector> &compcurve);
};

#endif // _COMPOSITECURVE_

