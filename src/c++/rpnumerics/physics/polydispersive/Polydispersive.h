#ifndef _POLYDISPERSIVE_
#define _POLYDISPERSIVE_

#include <stdio.h>
#include <stdlib.h>
#include "FluxFunction.h"

#include "Polydispersive_Params.h"

class Polydispersive : public FluxFunction {
    private:
        double phimax;
        double V1inf, V2inf;
        double n1, n2;

        // Slip velocities
//        int Slip_jet(double phi1, double phi2, JetMatrix &uj, int degree) const;

        // Hindered settling factor
//        int Settling_jet(double phi1, double phi2, JetMatrix &vj, int degree) const;

    protected:
    public:
        Polydispersive(const Polydispersive_Params &);
    Polydispersive(const Polydispersive &);
        Polydispersive * clone() const;

        ~Polydispersive();

        int jet(const WaveState &Phi, JetMatrix &flux, int degree) const;

        int Slip_jet(double phi1, double phi2, JetMatrix &uj, int degree) const;
        int Settling_jet(double phi1, double phi2, JetMatrix &vj, int degree) const;

};

#endif // _POLYDISPERSIVE_

