#ifndef _RIEMANNSOLVER_
#define _RIEMANNSOLVER_

#include <vector>
#include "WaveCurve.h"
#include "eigen.h"

// For the RiemannPlotter (to compute the characteristics):
//
#include "plotter.h"

#ifndef RIEMANNSOLVER_ERROR
#define RIEMANNSOLVER_ERROR 0
#endif

#ifndef RIEMANNSOLVER_OK
#define RIEMANNSOLVER_OK 1
#endif

class RiemannSolver {
    private:
    protected:
        static double min(double x, double y);
        static double max(double x, double y);

        static void minmax(const std::vector<Curve> &wave_curve, double &min, double &max);

        static void half_profile(const std::vector<Curve> &c, int subc, int subc_point,
                                 std::vector<RealVector> &profile);

        static double alpha(const RealVector &p0, const RealVector &p1, const RealVector &p);

        
    public:
        static void sample_grid(const std::vector<double> &real_grid, int n, std::vector<int> &index, std::vector<double> &alpha);

        static int saturation_profiles(const std::vector<Curve> &one_wave_curve, // Family 0, forward
                                       const std::vector<Curve> &two_wave_curve, // Family 1, backward
                                       const RealVector &pmin, const RealVector &pmax, 
                                       double time,
                                       std::vector<RealVector> &profile);

        static void characteristics(const FluxFunction *f, const AccumulationFunction *a,
                                    const std::vector<RealVector> &profile, 
                                    double time, int n, std::vector<std::vector<std::vector<RealVector> > > &characteristics);
};

#endif // _RIEMANNSOLVER_

