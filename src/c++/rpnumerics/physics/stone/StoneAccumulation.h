#ifndef _STONEACCUMULATION_
#define _STONEACCUMULATION_

#include "AccumulationFunction.h"

class StoneAccumulation : public AccumulationFunction {
    private:
    protected:
    public:
        StoneAccumulation();
        ~StoneAccumulation();

        int jet(const WaveState &w, JetMatrix &m, int degree) const;

        RpFunction * clone() const;
};

#endif // _STONEACCUMULATION_

