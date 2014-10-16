#ifndef _COREYQUADTRANSITIONALLINE_
#define _COREYQUADTRANSITIONALLINE_

#include "RealVector.h"
#include "BifurcationCurve.h"
#include "CoreyQuadSubPhysics.h"

#define COREYQUADTRANSITIONALLINE_W_E 0
#define COREYQUADTRANSITIONALLINE_O_B 1
#define COREYQUADTRANSITIONALLINE_G_D 2

class CoreyQuadTransitionalLine: public BifurcationCurve {
    private:
    protected:
        CoreyQuadSubPhysics *corey_;
    public:
        CoreyQuadTransitionalLine(CoreyQuadSubPhysics *c);
        virtual ~CoreyQuadTransitionalLine();

        virtual void list_of_secondary_bifurcation_curves(std::vector<int> &type, std::vector<std::string> &name){
            type.clear();
            name.clear();

            type.push_back(COREYQUADTRANSITIONALLINE_W_E);
            name.push_back(std::string("W-E"));

            type.push_back(COREYQUADTRANSITIONALLINE_O_B);
            name.push_back(std::string("O-B"));

            type.push_back(COREYQUADTRANSITIONALLINE_G_D);
            name.push_back(std::string("G-D"));

            return;
        }

        virtual double evaluate_point(int type, const RealVector &p);
};

#endif // _COREYQUADTRANSITIONALLINE_

