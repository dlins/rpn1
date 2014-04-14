#ifndef _EXPLICITHUGONIOT_
#define _EXPLICITHUGONIOT_

#include "FluxFunction.h"
#include "Boundary.h"
#include "ReferencePoint.h"
#include "Curve.h"
#include "ParametricPlot.h"
#include "Hugoniot_Locus.h"

// TODO: This class only works appropiately if the accumulation is trivial.
//       Add a check here or in the Physics to make sure.
//
class ExplicitHugoniot:public Hugoniot_Locus {
    private:
    protected:
        const Boundary *b;

        // No need for a true ReferencePoint here (only the "point" field will be used).
        //
        RealVector referencepoint;

        // Angles
        double phi_begin, phi_end;
    public:
        ExplicitHugoniot(const Boundary *bb);
        virtual ~ExplicitHugoniot();

        virtual void curve(const RealVector &ref,  std::vector<Curve> &curve);

        virtual RealVector fobj(double phi) = 0;
        static RealVector f(void *obj, double phi);
};

#endif // _EXPLICITHUGONIOT_

