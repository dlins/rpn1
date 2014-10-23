#ifndef _GASVOLATILEDEADEVAPORATIONEXTENSION_
#define _GASVOLATILEDEADEVAPORATIONEXTENSION_

#include "Extension.h"
#include "GasVolatileDeadCoincidence.h"
#include "GasVolatileDeadFluxFunction.h"
#include "GasVolatileDeadAccumulationFunction.h"
#include "Utilities.h"

class GasVolatileDeadEvaporationExtension : public Extension {
    private:
    protected:
        const GasVolatileDeadFluxFunction *flux;
        const GasVolatileDeadAccumulationFunction *accumulation;
        GasVolatileDeadCoincidence *god;
        Parameter *phi_parameter;
    public:
        GasVolatileDeadEvaporationExtension(const GasVolatileDeadFluxFunction *f, const GasVolatileDeadAccumulationFunction *a, GasVolatileDeadCoincidence *g, Parameter *phi);
        ~GasVolatileDeadEvaporationExtension();

        int extension(const RealVector &p, RealVector &ext_p);
        std::string name() const {return std::string("GasVolatileDeadEvaporationExtension");}
        int extension_type(){return EXPLICIT_EXTENSION;}
};

#endif // _GASVOLATILEDEADEVAPORATIONEXTENSION_

