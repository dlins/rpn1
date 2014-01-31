#ifndef STONE_EXPLICIT_BIFURCATION_CURVES
#define STONE_EXPLICIT_BIFURCATION_CURVES

#include "Explicit_Bifurcation_Curves.h"
#include "StoneFluxFunction.h"
#include "IsoTriang2DBoundary.h"

class Stone_Explicit_Bifurcation_Curves : public Explicit_Bifurcation_Curves {
    private:
    protected:
        StoneFluxFunction *f;
    public:
        Stone_Explicit_Bifurcation_Curves(StoneFluxFunction *ff);
        virtual ~Stone_Explicit_Bifurcation_Curves();

        void expl_sec_bif_crv(int side_opposite_vertex, int nos, 
                              std::vector<RealVector> &vertex_to_umbilic, 
                              std::vector<RealVector> &umbilic_to_side);

        void line(const RealVector &p, const RealVector &q, int nos, std::vector<RealVector> &v);
};

#endif // STONE_EXPLICIT_BIFURCATION_CURVES

