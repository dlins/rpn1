#ifndef _COLORCURVE_
#define _COLORCURVE_

#include <vector>

//#include "../global_composite.h" // SUCCESSFUL OR ABORTED
#include "FluxFunction.h"
#include "AccumulationFunction.h"
//#include "RealVector.h"
//#include "fill_with_jet.h"
#include "eigen.h"
//#include "Shockcurve_Adaptive_Hypersurface_Newton.h"

#define INTERPOLATION_ERROR -1
#define INTERPOLATION_OK     0

#define _SHOCK_SIMPLE_ACCUMULATION_  10  // Traditional rarefaction, using dgeev.
#define _SHOCK_GENERAL_ACCUMULATION_ 11  // Rarefaction with generalized eigenpairs, using dggev.

struct HugoniotPolyLine {
    public:
        std::vector<RealVector> vec;
        int type;

        HugoniotPolyLine(){vec.clear(); type = 0;};
        ~HugoniotPolyLine(){vec.clear();};
};

// Color table. Could be anywhere.
struct ColorTable{
    public:
        static int color[16][3];
};

class ColorCurve {
    private:
        static inline int sgn(double x);
        static int interpolate(const RealVector &p, const RealVector &q, std::vector<RealVector> &r);
        static int classify_point(const RealVector &p);
        static double shockspeed(int n, double Um[], double Up[], const FluxFunction &ff, const AccumulationFunction &aa, int type);
        static void fill_with_jet(const RpFunction & flux_object, int n, double *in, int degree, double *F, double *J, double *H);
    protected:
    public:
        static void classify_segments(const std::vector<RealVector> &input, std::vector<HugoniotPolyLine> &output);
        static int preprocess_data(const std::vector<RealVector> &curve, const RealVector &Uref, int noe, 
                                   const FluxFunction & ff, const AccumulationFunction &aa, int type,
                                    std::vector<RealVector> &out);
};

#endif // _COLORCURVE_

