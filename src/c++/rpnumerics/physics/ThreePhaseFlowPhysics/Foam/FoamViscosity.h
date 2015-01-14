#ifndef _FOAMVISCOSITY_
#define _FOAMVISCOSITY_

#include "Parameter.h"
#include "ThreePhaseFlowViscosity.h"

class FoamViscosity: public ThreePhaseFlowViscosity {
    private:
    protected:
        Parameter *mug0_parameter; // mug0

        Parameter *epdry_parameter;
        Parameter *fdry_parameter;
        Parameter *foil_parameter;
        Parameter *fmdry_parameter;
        Parameter *fmmob_parameter;
        Parameter *fmoil_parameter;
        Parameter *floil_parameter;
        Parameter *epoil_parameter;

        // TEMPORARY HACK, KILL IT LATER!
        Parameter *fdry_switch_parameter, *fo_switch_parameter;
        void trivial_jet(JetMatrix &j){
            j.resize(1);
            j.set(0, 1.0);
            j.set(0, 0, 0.0);
            j.set(0, 0, 0, 0.0);

            return;
        }

        void Fo(double so, int degree, JetMatrix &fo_jet);
        void viscosity_increase(const WaveState &u, int degree, JetMatrix &vi);
    public:
        FoamViscosity(Parameter *mug0, 
                      Parameter *epdry,
                      Parameter *fdry,
                      Parameter *foil,
                      Parameter *fmdry,
                      Parameter *fmmob,
                      Parameter *fmoil,
                      Parameter *floil,
                      Parameter *epoil,
                      Parameter *fdry_switch, Parameter *fo_switch, // TEMPORARY HACK, KILL IT LATER! 
                      ThreePhaseFlowSubPhysics *t);
        ~FoamViscosity();

        int gas_viscosity_jet(const WaveState &w, int degree, JetMatrix &mug_jet);

        double gas_viscosity(const RealVector &p);

        void Fdry(double s, int degree, JetMatrix &fdry_jet);
        void Fdry_positive(double x, int degree, JetMatrix &fdry_jet);
        void Fdry_normalized(double x, int degree, JetMatrix &fdry_jet);
};

#endif // _FOAMVISCOSITY_

