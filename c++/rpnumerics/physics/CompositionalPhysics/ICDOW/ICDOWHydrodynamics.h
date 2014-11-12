#ifndef _ICDOWHYDRODYNAMICS_
#define _ICDOWHYDRODYNAMICS_

#include "Parameter.h"
#include "JetMatrix.h"

class ICDOWHydrodynamics {
    private:
    protected:
        Parameter *muw_parameter, *muo_parameter;
        Parameter *grw_parameter, *gro_parameter;
        Parameter *vel_parameter;
        Parameter *swc_parameter, *lambda_parameter;
    public:
        ICDOWHydrodynamics(Parameter *muw, Parameter *muo,
                           Parameter *grw, Parameter *gro,
                           Parameter *vel,
                           Parameter *swc, Parameter *lambda);
        virtual ~ICDOWHydrodynamics();

        void water_fractional_flow(double sw, int degree, JetMatrix &fw_jet);

        void oil_fractional_flow(double sw, int degree, JetMatrix &fo_jet);
};

#endif // _ICDOWHYDRODYNAMICS_

