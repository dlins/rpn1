#ifndef _FLUX2COMP2PHASESADIMENSIONALIZED_
#define _FLUX2COMP2PHASESADIMENSIONALIZED_

#include <stdio.h>
#include <stdlib.h>
#include "FluxFunction.h"

#include "Flux2Comp2PhasesAdimensionalized_Params.h"

#define FLUX2COMP2PHASESADIMENSIONALIZED_PURE_GRAVITY    0
#define FLUX2COMP2PHASESADIMENSIONALIZED_GRAVITY      1
#define FLUX2COMP2PHASESADIMENSIONALIZED_HORIZONTAL   2

class Flux2Comp2PhasesAdimensionalized : public FluxFunction {

    class FracFlow2PhasesVerticalAdimensionalized {
    private:
        Flux2Comp2PhasesAdimensionalized * fluxComplete_;
    public:
        FracFlow2PhasesVerticalAdimensionalized(Flux2Comp2PhasesAdimensionalized *);

        int Diff_FracFlow2PhasesVerticalAdimensionalized(double sw, double Theta, int degree, JetMatrix &m);
    };

    class FracFlow2PhasesHorizontalAdimensionalized {
    private:
        Flux2Comp2PhasesAdimensionalized * fluxComplete_;

    public:
        FracFlow2PhasesHorizontalAdimensionalized(Flux2Comp2PhasesAdimensionalized *);

        int Diff_FracFlow2PhasesHorizontalAdimensionalized(double sw, double Theta, int degree, JetMatrix &m);

    };

    class ReducedFlux2Comp2PhasesAdimensionalized {
    private:
        Flux2Comp2PhasesAdimensionalized * fluxComplete_;
    public:

        ReducedFlux2Comp2PhasesAdimensionalized(Flux2Comp2PhasesAdimensionalized *);

        int jet(const WaveState &u, JetMatrix &m, int degree) const;
    };

private:


    // Fluid dynamics
    double abs_perm, sin_beta, const_gravity, grav;

    bool has_gravity;
    bool has_horizontal;

    double cnw;
    double cng;
    double expw;
    double expg;


    // Thermodynamics

    Thermodynamics_SuperCO2_WaterAdimensionalized *TD;



    // FracFlows
    FracFlow2PhasesHorizontalAdimensionalized *FH;
    FracFlow2PhasesVerticalAdimensionalized *FV;

    //ReducedFlux
    ReducedFlux2Comp2PhasesAdimensionalized *reducedFlux;


    double T_typical_;




protected:

public:

    Flux2Comp2PhasesAdimensionalized(const Flux2Comp2PhasesAdimensionalized &);
    Flux2Comp2PhasesAdimensionalized(const Flux2Comp2PhasesAdimensionalized_Params &);
    Flux2Comp2PhasesAdimensionalized * clone() const;

    virtual ~Flux2Comp2PhasesAdimensionalized();

    int jet(const WaveState &u, JetMatrix &m, int degree) const;
    void type(int t);

    Thermodynamics_SuperCO2_WaterAdimensionalized * getThermo();

    FracFlow2PhasesHorizontalAdimensionalized * getHorizontalFlux();
    FracFlow2PhasesVerticalAdimensionalized * getVerticalFlux();
    ReducedFlux2Comp2PhasesAdimensionalized * getReducedFlux();




};

#endif // _FLUX2COMP2PHASESADIMENSIONALIZED_

