#ifndef _KOVALACCUMULATIONFUNCTION_
#define _KOVALACCUMULATIONFUNCTION_

#include "AccumulationFunction.h"
#include "Parameter.h"

class KovalAccumulationFunction: public AccumulationFunction {
    private:
    protected:
        Parameter *phi_parameter_;
    public:
        KovalAccumulationFunction(Parameter *phi);
        virtual ~KovalAccumulationFunction();

        int jet(const WaveState &state, JetMatrix &a, int degree) const;
};

#endif // _KOVALACCUMULATIONFUNCTION_

