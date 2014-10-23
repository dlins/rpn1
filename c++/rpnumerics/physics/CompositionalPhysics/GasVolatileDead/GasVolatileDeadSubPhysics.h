#ifndef _GASVOLATILEDEADSUBPHYSICS_
#define _GASVOLATILEDEADSUBPHYSICS_

#include "SubPhysics.h"
#include "GasVolatileDeadThermodynamics.h"
#include "GasVolatileDeadHydrodynamics.h"
#include "GasVolatileDeadCoincidence.h"
#include "GasVolatileDeadEvaporationExtension.h"

#include "GasVolatileDeadFluxFunction.h"
#include "GasVolatileDeadAccumulationFunction.h"
#include "GasVolatileDeadCompositeCurve.h"
#include "LSODE.h"

#include "RectBoundary.h"

#include "Hugoniot_TP.h"
//#include "RarefactionCurve.h"

#include "HugoniotContinuation3D2D.h"
#include "HugoniotContinuation_nDnD.h"

#define GASVOLATILEDEAD_GENERIC_POINT 0

class GasVolatileDeadSubPhysics : public SubPhysics {
    private:
    protected:
        Parameter *B_parameter, *D_parameter, *mu_oB_parameter, *mu_oD_parameter, *mu_G_parameter;
        GasVolatileDeadThermodynamics *thermo;

        GasVolatileDeadHydrodynamics  *hydro;

        Parameter *re_parameter, *rg_parameter, *phi_parameter;

        GasVolatileDeadEvaporationExtension *evap_;
    public:
        GasVolatileDeadSubPhysics();
        virtual ~GasVolatileDeadSubPhysics();

        void shock_cases(std::vector<int> &type, std::vector<std::string> &name) const;
};

#endif // _GASVOLATILEDEADSUBPHYSICS_
