#ifndef _ACCUM2COMP2PHASESADIMENSIONALIZED_
#define _ACCUM2COMP2PHASESADIMENSIONALIZED_

#include <stdio.h>
#include <stdlib.h>
#include "AccumulationFunction.h"
#include "Accum2Comp2PhasesAdimensionalized_Params.h"

#include "Thermodynamics_SuperCO2_WaterAdimensionalized.h"

class Accum2Comp2PhasesAdimensionalized : public AccumulationFunction {
public:


    class ReducedAccum2Comp2PhasesAdimensionalized:public AccumulationFunction {
    private:
        Accum2Comp2PhasesAdimensionalized * totalAccum_;
    public:

        ReducedAccum2Comp2PhasesAdimensionalized(Accum2Comp2PhasesAdimensionalized *);
        RpFunction * clone() const;

        virtual ~ReducedAccum2Comp2PhasesAdimensionalized();

        int jet(const WaveState &u, JetMatrix &m, int degree) const; // ONLY DEGREE 0
    };


    Accum2Comp2PhasesAdimensionalized(const Accum2Comp2PhasesAdimensionalized &);
    Accum2Comp2PhasesAdimensionalized(const Accum2Comp2PhasesAdimensionalized_Params &);
    RpFunction * clone() const;

    virtual~Accum2Comp2PhasesAdimensionalized();

    int jet(const WaveState &u, JetMatrix &m, int degree) const;
    ReducedAccum2Comp2PhasesAdimensionalized * getReducedAccumulation()const;


private:
    // Fluid dynamics
    double phi; // = 0.38;

    // Thermodynamics
    Thermodynamics_SuperCO2_WaterAdimensionalized *TD;
    ReducedAccum2Comp2PhasesAdimensionalized * reducedAccum_;
protected:

};

#endif // _ACCUM2COMP2PHASESADIMENSIONALIZED_

