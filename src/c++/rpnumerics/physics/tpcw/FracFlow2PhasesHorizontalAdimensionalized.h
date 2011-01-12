#ifndef _FRACFLOW2PHASESHORIZONTALADIMENSIONALIZED_
#define _FRACFLOW2PHASESHORIZONTALADIMENSIONALIZED_

#include <stdio.h>
#include <stdlib.h>
#include <math.h>

#include "JetMatrix.h"

#include "Thermodynamics_SuperCO2_WaterAdimensionalized.h"

class FracFlow2PhasesHorizontalAdimensionalized {
    private:
        double cnw, cng, expw, expg;

        Thermodynamics_SuperCO2_WaterAdimensionalized *TD;
        double T_typical_;
    protected:
    public:
        FracFlow2PhasesHorizontalAdimensionalized(double cnw_, double cng_, double expw_, double expg_,const  Thermodynamics_SuperCO2_WaterAdimensionalized  &TD_);
        virtual ~FracFlow2PhasesHorizontalAdimensionalized();

        int Diff_FracFlow2PhasesHorizontalAdimensionalized(double sw, double Theta, int degree, JetMatrix &m);
};

#endif //  _FRACFLOW2PHASESHORIZONTALADIMENSIONALIZED_

