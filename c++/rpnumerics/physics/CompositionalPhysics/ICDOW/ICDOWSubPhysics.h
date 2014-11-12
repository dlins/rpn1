#ifndef _ICDOWSUBPHYSICS_
#define _ICDOWSUBPHYSICS_

#include "SubPhysics.h"
#include "ICDOWChemistry.h"
#include "ICDOWHydrodynamics.h"

#include "ICDOWFluxFunction.h"
#include "ICDOWAccumulationFunction.h"
//#include "ICDOWCompositeCurve.h"
#include "LSODE.h"

#include "RectBoundary.h"

#include "Hugoniot_TP.h"
//#include "RarefactionCurve.h"

#include "HugoniotContinuation3D2D.h"
#include "HugoniotContinuation_nDnD.h"

class ICDOWSubPhysics : public SubPhysics {
    private:
    protected:
        Parameter *muw_parameter, *muo_parameter;
        Parameter *grw_parameter, *gro_parameter;
        Parameter *vel_parameter;
        Parameter *swc_parameter, *lambda_parameter;
        Parameter *phi_parameter;

        ICDOWHydrodynamics  *hydro;
        ICDOWChemistry      *chemistry;
    public:
        ICDOWSubPhysics();
        virtual ~ICDOWSubPhysics();
};

#endif // _ICDOWSUBPHYSICS_
