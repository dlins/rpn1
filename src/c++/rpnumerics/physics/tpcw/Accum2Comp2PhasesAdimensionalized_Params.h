#ifndef _ACCUM2COMP_PHASESADIMENSIONALIZED_PARAMS_
#define _ACCUM2COMP_PHASESADIMENSIONALIZED_PARAMS_



#include "AccumulationParams.h"
//#include "Thermodynamics.h"
#include "Thermodynamics.h"

class Accum2Comp2PhasesAdimensionalized_Params : public AccumulationParams {
private:

    Thermodynamics *TD_;
protected:
public:
    Accum2Comp2PhasesAdimensionalized_Params( Thermodynamics *, double);
    Accum2Comp2PhasesAdimensionalized_Params(const Accum2Comp2PhasesAdimensionalized_Params &);

    virtual ~Accum2Comp2PhasesAdimensionalized_Params();
    double getPhi() const;

    Thermodynamics * get_thermodynamics(void) const;
};

#endif //_ACCUM2COMP_PHASESADIMENSIONALIZED_PARAMS_

