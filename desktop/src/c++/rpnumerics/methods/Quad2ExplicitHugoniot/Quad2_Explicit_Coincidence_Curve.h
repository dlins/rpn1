#ifndef _QUAD2_EXPLICIT_COINCIDENCE_CURVE_
#define _QUAD2_EXPLICIT_COINCIDENCE_CURVE_

#include "SimplePolarPlot.h"
#include "Quad2_Explicit.h"

class Quad2_Explicit_Coincidence_Curve : public Quad2_Explicit {
    private:
    protected:
    public:
        Quad2_Explicit_Coincidence_Curve(const Quad2FluxFunction *ff, 
                                         const Viscosity_Matrix *vvm,
                                         const RectBoundary *b);

        ~Quad2_Explicit_Coincidence_Curve();

        // To be invoked by a PolarPlot of some kind, such as SimplePolarPlot.
        static void coincidence_point(void *o, double phi, RealVector &x);

        void polar_plot(std::vector<std::deque<RealVector> > &out);
};

#endif // _QUAD2_EXPLICIT_COINCIDENCE_CURVE_

