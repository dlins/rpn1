#ifndef _COREY_QUADRATIC_
#define _COREY_QUADRATIC_

#include <stdio.h>
#include <stdlib.h>
#include "FluxFunction.h"

#include "CoreyQuad_Params.h"

class CoreyQuad : public FluxFunction {
    private:
//        double grw, grg, gro;
//        double muw, mug, muo;
//        double vel;
//
////        Permeability *perm;
//        double krw_p, krg_p, kro_p;
//        double cnw, cng, cno;
    protected:
    public:
        CoreyQuad(const CoreyQuad_Params &);
        CoreyQuad(const CoreyQuad &);
        CoreyQuad * clone() const;

        ~CoreyQuad();

        int jet(const WaveState &u, JetMatrix &m, int degree) const;
};

#endif // _COREY_QUADRATIC_

