#ifndef _QUAD2SUBPHYSICS_
#define _QUAD2SUBPHYSICS_

#include "SubPhysics.h"
#include "Quad2FluxFunction.h"
#include "Quad2AccumulationFunction.h"
#include "LSODE.h"
#include "ImplicitHugoniotCurve.h"
#include "HugoniotContinuation2D2D.h"
#include "WaveCurveFactory.h"
#include "RectBoundary.h"
#include "Implicit_Extension_Curve.h"

class Quad2SubPhysics: public SubPhysics {
    private:
    protected:
        Parameter *a1_parameter, *b1_parameter, *c1_parameter, *d1_parameter, *e1_parameter;
        Parameter *a2_parameter, *b2_parameter, *c2_parameter, *d2_parameter, *e2_parameter;

    public:
        Quad2SubPhysics();
        virtual ~Quad2SubPhysics();
};

#endif // _QUAD2SUBPHYSICS_

