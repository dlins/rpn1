#ifndef _CONTACTREGIONBOUNDARY_
#define _CONTACTREGIONBOUNDARY_

class SubPhysics;

#include "ImplicitFunction.h"
#include "ContourMethod.h"
#include "Curve.h"

class ContactRegionBoundary: public ImplicitFunction {
    private:
    protected:
        SubPhysics *subphysics;
        int family;
    public:
        ContactRegionBoundary(SubPhysics *s);
        virtual ~ContactRegionBoundary();

        int function_on_square(double *foncub, int i, int j);
        void curve(int fam, std::vector<RealVector> &curve);
};

#endif // _CONTACTREGIONBOUNDARY_

