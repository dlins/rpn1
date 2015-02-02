#ifndef _GAUSSLEGENDREINTEGRAL_
#define _GAUSSLEGENDREINTEGRAL_

#include <vector>
#include <math.h>
#include "RealVector.h"

class GaussLegendreIntegral {
    private:
    protected:
        std::vector<double> position, weight;
    public:
        GaussLegendreIntegral();
        virtual ~GaussLegendreIntegral();

       virtual double integrate(double (*f)(double));                     // Integral over the interval [-1, 1]
       virtual double integrate(double (*f)(double), double a, double b); // Integral over the interval [a, b]
       virtual double average(double (*f)(double), double a, double b);   // Average over the interval [a, b]

       virtual double integrate(double (*f)(int*, double), int *obj);                     // Integral over the interval [-1, 1]
       virtual double integrate(double (*f)(int*, double), int *obj, double a, double b); // Integral over the interval [a, b]
       virtual double average(double (*f)(int*, double), int *obj, double a, double b);   // Average over the interval [a, b]

       virtual RealVector average(RealVector (*f)(int*, const RealVector&), int *obj, 
                                  const RealVector &apoint, 
                                  const RealVector &bpoint);
};

#endif // _GAUSSLEGENDREINTEGRAL_

