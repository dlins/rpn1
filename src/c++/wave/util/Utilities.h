#ifndef _UTILITIES_
#define _UTILITIES_

#include <limits>
#include <vector>
#include "RealVector.h"

#define BISECTIONONSEGMENT_OK    0
#define BISECTIONONSEGMENT_ERROR 1

#define UTILITIES_BISECTION_OK    0
#define UTILITIES_BISECTION_ERROR 1

class Utilities {
    private:
    protected:
        static bool validate_alpha(double alpha){
            return (alpha >= 0.0 && alpha <= 1.0);
        }
    public:
        // Convert a vector of RealVectors into a vector of segments.
        //
        static void points_to_segments(std::vector<RealVector> &v);

        // Bisection to find the zero of function_for_bisection, given a generic object obj.
        //
        static int bisection_on_segment(void* obj, double (*function_for_bisection)(void*, const RealVector&), const RealVector &p, const RealVector &q, RealVector &r);

        static void alpha_convex_combination_projection(const RealVector &p0, const RealVector &p1, const RealVector &p, double &alpha, RealVector &proj);
        static void pick_point_from_continuous_curve(const std::vector<RealVector> &curve, const RealVector &p, RealVector &closest_point);
        static void pick_point_from_segmented_curve(const std::vector<RealVector> &curve, const RealVector &p, RealVector &closest_point);

        static int find_point_on_level_curve(void *obj, double (*function_for_bisection)(void*, const RealVector &), const RealVector &p, const RealVector &q0, RealVector &point_on_level_curve);
};

#endif // _UTILITIES_

