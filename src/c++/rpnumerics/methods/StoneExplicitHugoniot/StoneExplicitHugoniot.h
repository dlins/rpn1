#ifndef _STONEEXPLICITHUGONIOT_
#define _STONEEXPLICITHUGONIOT_

#include <math.h>
#include "StoneFluxFunction.h"

class StoneExplicitHugoniot {
    private:
    protected:
        const FluxParams & fluxParams(void) const;

        RealVector reference;

        double muw, mug, muo;
        bool valid_;

        static double sign(double a, double b);
    public:
        StoneExplicitHugoniot(const StoneFluxFunction *ff);
        ~StoneExplicitHugoniot();

        void set_reference_point(const RealVector &ref);

        static void PolarHugoniot(void *o, double theta, RealVector &out);
};

#endif // _STONEEXPLICITHUGONIOT_

