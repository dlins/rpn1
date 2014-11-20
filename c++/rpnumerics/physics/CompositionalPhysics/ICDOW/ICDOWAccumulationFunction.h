#ifndef _ICDOWACCUMULATIONFUNCTION_
#define _ICDOWACCUMULATIONFUNCTION_

#include "AccumulationFunction.h"
#include "ICDOWChemistry.h"

#define JETTESTER_ENABLED_ICDOWACCUMULATION

#ifdef JETTESTER_ENABLED_ICDOWACCUMULATION
//#include "JetTester.h"
#include "TestableJet.h"
#endif

class ICDOWAccumulationFunction: public AccumulationFunction
                                 #ifdef JETTESTER_ENABLED_ICDOWACCUMULATION
                                 , public TestableJet
                                 #endif
{
    private:
    protected:
        ICDOWChemistry *chemistry;
        
        Parameter *phi_parameter;
    public:
        ICDOWAccumulationFunction(Parameter *phi, ICDOWChemistry *ch);
        virtual ~ICDOWAccumulationFunction();

        int reduced_jet(const WaveState &state, JetMatrix &m, int degree) const;
        int reduced_jet(const RealVector &u, JetMatrix &m, int degree) const {
            WaveState w(u);

            int info = reduced_jet(w, m, degree);

            return info;
        }

        int jet(const WaveState &u, JetMatrix &m, int degree) const;

        #ifdef JETTESTER_ENABLED_ICDOWACCUMULATION
        static int testable_accumulation_jet(void *obj, const RealVector &state, int degree, JetMatrix &jm){
            const ICDOWAccumulationFunction *icdowaccum = (const ICDOWAccumulationFunction*)obj;

            int info = icdowaccum->jet(state, jm, degree);
            return info;
        }

        void list_of_functions(std::vector<int (*)(void*, const RealVector&, int degree, JetMatrix&)> &list,
                               std::vector<std::string> &name){
            list.clear();
            list.push_back(&testable_accumulation_jet);

            name.clear();
            name.push_back(std::string("ICDOW Accumulation"));

            return;
        }
        #endif  
};

#endif // _ICDOWACCUMULATIONFUNCTION_

