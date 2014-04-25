#ifndef EXPLICIT_BIFURCATION_CURVES
#define EXPLICIT_BIFURCATION_CURVES

#include <vector>
#include "RealVector.h"

#define SHOCK_CROSSED_EXPLICIT_BIFURCATION       1
#define SHOCK_DID_NOT_CROSS_EXPLICIT_BIFURCATION 2

class Explicit_Bifurcation_Curves {
    private:
    protected:
    public:
        // Explicit secondary bifurcation curve.
        //
        virtual void expl_sec_bif_crv(int vertex, int nos, 
                                      std::vector<RealVector> &vertex_to_umbilic, 
                                      std::vector<RealVector> &umbilic_to_side) = 0;

        // The shock curve crossed the secondary bifurcation.
        //
        virtual int cross_sec_bif(const RealVector &previous_point, const RealVector &point, RealVector &crossing_point, int &region) = 0;

        // Find the correspondences on the secondary bifurcations
        //
        virtual void sec_bif_correspondence(int side_opposite_vertex, int nos, 
                                            std::vector<RealVector> &point, 
                                            std::vector<RealVector> &correspondent_point) = 0;
};

#endif // EXPLICIT_BIFURCATION_CURVES

