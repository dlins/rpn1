#ifndef _ICDOWFLUXFUNCTION_
#define _ICDOWFLUXFUNCTION_

#include "FluxFunction.h"
#include "ICDOWChemistry.h"
#include "ICDOWHydrodynamics.h"

#define JETTESTER_ENABLED_ICDOWFLUX

#ifdef JETTESTER_ENABLED_ICDOWFLUX
//#include "JetTester.h"
#include "TestableJet.h"
#endif

class ICDOWFluxFunction: public FluxFunction
                         #ifdef JETTESTER_ENABLED_ICDOWFLUX
                         , public TestableJet
                         #endif
{
    private:
    protected:
        ICDOWChemistry      *chemistry;
        ICDOWHydrodynamics  *hydro;
    public:
        ICDOWFluxFunction(ICDOWChemistry *ch, ICDOWHydrodynamics *hy);
        virtual ~ICDOWFluxFunction();

        int reduced_jet(const WaveState &u, JetMatrix &m, int degree) const;
        int reduced_jet(const RealVector &u, JetMatrix &m, int degree) const {
            WaveState w(u);

            int info = reduced_jet(w, m, degree);

            return info;
        }

        int jet(const WaveState &u, JetMatrix &m, int degree) const;

        #ifdef JETTESTER_ENABLED_ICDOWFLUX
        static int testable_flux_jet(void *obj, const RealVector &state, int degree, JetMatrix &jm){
            const ICDOWFluxFunction *icdowflux = (const ICDOWFluxFunction*)obj;

            int info = icdowflux->jet(state, jm, degree);
            return info;
        }

        void list_of_functions(std::vector<int (*)(void*, const RealVector&, int degree, JetMatrix&)> &list,
                               std::vector<std::string> &name){
            list.clear();
            list.push_back(&testable_flux_jet);

            name.clear();
            name.push_back(std::string("ICDOW Flux"));

            return;
        }
        #endif
};

#endif // _ICDOWFLUXFUNCTION_

