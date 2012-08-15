#ifndef _RIEMANNSOLVER_
#define _RIEMANNSOLVER_

#include <vector>
#include "WaveCurve.h"

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
        static int saturation_profiles(const std::vector<Curve> &one_wave_curve, // Family 0, forward
                                       const std::vector<Curve> &two_wave_curve, // Family 1, backward
                                       const RealVector &pmin, const RealVector &pmax, 
                                       double time,
                                       std::vector<RealVector> &profile);
};

#endif // _RIEMANNSOLVER_

