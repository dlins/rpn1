#ifndef _EVAP_EXTENSION_
#define _EVAP_EXTENSION_

#include "Extension.h"

#include "Coincidence.h"
#include "Flux2Comp2PhasesAdimensionalized.h"
#include "Utilities.h"

// Only valid for TPCW-like classes.
//
class Evap_Extension : public Extension {
    private:
    protected:
        // The component index such that p(i) = extension(p(i)).
        //
        unsigned int index_of_constant;

        // The other component.
        //
        unsigned int index_of_non_constant;

        const Coincidence *coincidence;

        const Flux2Comp2PhasesAdimensionalized *flux_;
    public:
        Evap_Extension(const Flux2Comp2PhasesAdimensionalized *f, const Coincidence *c);
        virtual ~Evap_Extension();

        // 
        int extension(const RealVector &p, RealVector &ext_p);

        std::string name() const {
            return std::string("Evaporation extension");
        }
};

#endif // _EVAP_EXTENSION_

