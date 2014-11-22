#ifndef _FLUX2COMP2PHASESADIMENSIONALIZED_
#define _FLUX2COMP2PHASESADIMENSIONALIZED_

#include <stdio.h>
#include <stdlib.h>
#include "FluxFunction.h"

//#include "Flux2Comp2PhasesAdimensionalized_Params.h"

#include "Parameter.h"
#include "Thermodynamics.h"

#define FLUX2COMP2PHASESADIMENSIONALIZED_PURE_GRAVITY 0
#define FLUX2COMP2PHASESADIMENSIONALIZED_GRAVITY      1
#define FLUX2COMP2PHASESADIMENSIONALIZED_HORIZONTAL   2

class Flux2Comp2PhasesAdimensionalized : public FluxFunction {
    public:
        // SubinflectionTP uses this inner-class, ASAP make a jet with this and leave it in the
        // public area of this class.
        //
        class FracFlow2PhasesHorizontalAdimensionalized {
            private:
                Flux2Comp2PhasesAdimensionalized *fluxComplete_;
            public:
                FracFlow2PhasesHorizontalAdimensionalized(Flux2Comp2PhasesAdimensionalized *);
                virtual ~FracFlow2PhasesHorizontalAdimensionalized(){}

                int Diff_FracFlow2PhasesHorizontalAdimensionalized(double sw, double Theta, int degree, JetMatrix &m);
        };
    private:
    protected:
        class FracFlow2PhasesVerticalAdimensionalized {
            private:
                Flux2Comp2PhasesAdimensionalized *fluxComplete_;
            public:
                FracFlow2PhasesVerticalAdimensionalized(Flux2Comp2PhasesAdimensionalized *);
                virtual ~FracFlow2PhasesVerticalAdimensionalized(){}

                int Diff_FracFlow2PhasesVerticalAdimensionalized(double sw, double Theta, int degree, JetMatrix &m);
        };

        class ReducedFlux2Comp2PhasesAdimensionalized {
            private:
                Flux2Comp2PhasesAdimensionalized *fluxComplete_;
            public:
                ReducedFlux2Comp2PhasesAdimensionalized(Flux2Comp2PhasesAdimensionalized *);
                virtual ~ReducedFlux2Comp2PhasesAdimensionalized(){}

                int jet(const WaveState &u, JetMatrix &m, int degree) const;
        };

        // Fluid dynamics
//        double abs_perm, sin_beta, const_gravity, grav;

        Parameter *abs_perm_parameter, *sin_beta_parameter;
        Parameter *cnw_parameter, *cng_parameter;
        Parameter *expw_parameter, *expg_parameter;

        bool has_gravity;
        bool has_horizontal;

        double const_gravity;

        // Thermodynamics.
        //
        Thermodynamics *TD;

        // Fractional flows.
        //
        FracFlow2PhasesHorizontalAdimensionalized *FH;
        FracFlow2PhasesVerticalAdimensionalized *FV;

        //Reduced flux.
        //
        ReducedFlux2Comp2PhasesAdimensionalized *reducedFlux;

        double T_typical_;
    public:
        FracFlow2PhasesHorizontalAdimensionalized* getHorizontalFlux()const ;
        FracFlow2PhasesVerticalAdimensionalized*   getVerticalFlux()const;
        ReducedFlux2Comp2PhasesAdimensionalized*   getReducedFlux()const;

        Flux2Comp2PhasesAdimensionalized(Parameter *abs_perm, Parameter *sin_beta, 
                                         Parameter *cnw, Parameter *cng,
                                         Parameter *expw, Parameter *expg,
                                         bool hasgrav, bool hashor,
                                         Thermodynamics *td);
//        Flux2Comp2PhasesAdimensionalized(const Flux2Comp2PhasesAdimensionalized &);
//        Flux2Comp2PhasesAdimensionalized(const Flux2Comp2PhasesAdimensionalized_Params &);
        virtual ~Flux2Comp2PhasesAdimensionalized();

        int jet(const WaveState &u, JetMatrix &m, int degree) const;
        void type(int t);

        Thermodynamics* getThermo(){return TD;}
};

#endif // _FLUX2COMP2PHASESADIMENSIONALIZED_

