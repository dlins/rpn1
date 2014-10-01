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

#ifndef COMPOSITE_REACHED_BOUNDARY
#define COMPOSITE_REACHED_BOUNDARY 10
#endif

#ifndef COMPOSITE_EXHAUSTED_RAREFACTION
#define COMPOSITE_EXHAUSTED_RAREFACTION 20
#endif

#ifndef COMPOSITE_DID_NOT_EXHAUST_RAREFACTION
#define COMPOSITE_DID_NOT_EXHAUST_RAREFACTION 30
#endif

#ifndef COMPOSITE_REACHED_DOUBLE_CONTACT
#define COMPOSITE_REACHED_DOUBLE_CONTACT 40
#endif

#ifndef COMPOSITE_EXHAUSTED_RAREFACTION_AND_REACHED_DOUBLE_CONTACT
#define COMPOSITE_EXHAUSTED_RAREFACTION_AND_REACHED_DOUBLE_CONTACT 50
#endif

#ifndef COMPOSITE_ERROR
#define COMPOSITE_ERROR 60
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
        static int curve(const std::vector<RealVector> &rarcurve, int origin, int family, int increase, int number_ignore_doub_contact,
                          FluxFunction *ff, AccumulationFunction *aa, 
                          Boundary *boundary, std::vector<RealVector> &compcurve);

//        static int curve(const std::vector<RealVector> &rarcurve, int origin, int family, int increase, int number_ignore_doub_contact,
//                          FluxFunction *ff, AccumulationFunction *aa, 
//                          Boundary *boundary, std::vector<RealVector> &compcurve, std::vector<RealVector> &shock_curve_temp);
};

#endif // _COMPOSITECURVE_

