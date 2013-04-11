#ifndef _SIMPLEPOLARPLOT_
#define _SIMPLEPOLARPLOT_

#include <vector>
#include <deque>
#include <stdio.h>
#include <math.h>
#include "RealVector.h"
#include "Boundary.h"

#ifndef PI
#define PI 3.14159256
#endif

#ifndef TWOPI
#define TWOPI (2.0*PI)
#endif

#ifndef SIMPLEPOLARPLOT_MAX_RATIO
#define SIMPLEPOLARPLOT_MAX_RATIO 1000.0
#endif

class SimplePolarPlot {
    private:
    protected:
    public:
        static void curve(void *Object,
                          const Boundary *b, 
                          void (*polarfunc)(void *o, double theta, RealVector &out), 
                          const RealVector &polar_domain, int n_max, 
                          std::vector<std::deque<RealVector> > &out);

        static void simple_curve(void *Object,
                                 const Boundary *b,
                                 void (*polarfunc)(void *o, double theta, RealVector &out), 
                                 std::vector<std::deque<RealVector> > &out);

        static void periodic_curve(void *Object,
                                   const Boundary *b,
                                   void (*polarfunc)(void *o, double theta, double &num, double &den),
                                   double theta0, double period, int n_max,
                                   std::vector<std::deque<RealVector> > &out);
};

#endif 

