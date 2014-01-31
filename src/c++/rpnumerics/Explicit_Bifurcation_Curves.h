#ifndef EXPLICIT_BIFURCATION_CURVES
#define EXPLICIT_BIFURCATION_CURVES

#include <vector>
#include "RealVector.h"

class Explicit_Bifurcation_Curves {
    private:
    protected:
    public:
        // Explicit secondary bifurcation curve.
        //
        virtual void expl_sec_bif_crv(int vertex, int nos, 
                                      std::vector<RealVector> &vertex_to_umbilic, 
                                      std::vector<RealVector> &umbilic_to_side){

            // Only clear.
            //
            vertex_to_umbilic.clear();
            umbilic_to_side.clear();

            return;
        }
};

#endif // EXPLICIT_BIFURCATION_CURVES

