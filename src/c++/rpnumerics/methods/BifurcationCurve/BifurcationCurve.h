#ifndef _BIFURCATIONCURVE_
#define _BIFURCATIONCURVE_

#include <vector>
#include <string>
#include "RealVector.h"

class BifurcationCurve {
    private:
    protected:
    public:
        BifurcationCurve();
        virtual ~BifurcationCurve();

        virtual void list_of_secondary_bifurcation_curves(std::vector<int> &type, std::vector<std::string> &name) = 0;

        virtual double evaluate_point(int type, const RealVector &p) = 0;
};

#endif // _BIFURCATIONCURVE_

