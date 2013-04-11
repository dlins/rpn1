#ifndef _QUAD2_EXPLICIT_HUGONIOT_
#define _QUAD2_EXPLICIT_HUGONIOT_

#include <math.h>
#include "Quad2_Explicit.h"

class Quad2_Explicit_Hugoniot : public Quad2_Explicit {
    private:
    protected:
        const FluxParams & fluxParams(void) const;

        RealVector reference;

        static double sign(double a, double b);
    public:
        Quad2_Explicit_Hugoniot(const Quad2FluxFunction *ff);
        ~Quad2_Explicit_Hugoniot();

        void set_reference_point(const RealVector &ref);

        static void PolarHugoniot(void *o, double theta, RealVector &out);
};

#endif // _QUAD2_EXPLICIT_HUGONIOT_
