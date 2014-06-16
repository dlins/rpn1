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
protected:

public:

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

    class ReducedFlux2Comp2PhasesAdimensionalized: public FluxFunction {
    private:
        Flux2Comp2PhasesAdimensionalized * fluxComplete_;
    public:

        ReducedFlux2Comp2PhasesAdimensionalized(Flux2Comp2PhasesAdimensionalized *);

        int jet(const WaveState &u, JetMatrix &m, int degree) const;

       RpFunction * clone() const;
    };


    Thermodynamics * getThermo()const ;

    FracFlow2PhasesHorizontalAdimensionalized * getHorizontalFlux()const ;
    FracFlow2PhasesVerticalAdimensionalized * getVerticalFlux()const;
    ReducedFlux2Comp2PhasesAdimensionalized * getReducedFlux()const;

    Flux2Comp2PhasesAdimensionalized(const Flux2Comp2PhasesAdimensionalized &);
    Flux2Comp2PhasesAdimensionalized(const Flux2Comp2PhasesAdimensionalized_Params &);


    virtual ~Flux2Comp2PhasesAdimensionalized();

    int jet(const WaveState &u, JetMatrix &m, int degree) const;
    RpFunction * clone() const;
    void type(int t);

    void fluxParams(const FluxParams &);


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

    Thermodynamics *TD;



    // FracFlows
    FracFlow2PhasesHorizontalAdimensionalized *FH;
    FracFlow2PhasesVerticalAdimensionalized *FV;

    //ReducedFlux
    ReducedFlux2Comp2PhasesAdimensionalized *reducedFlux;


    double T_typical_;











};

#endif // _FLUX2COMP2PHASESADIMENSIONALIZED_

