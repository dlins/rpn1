#ifndef _FRACFLOW2PHASESVERTICALADIMENSIONALIZED_
#define _FRACFLOW2PHASESVERTICALADIMENSIONALIZED_

#include <stdio.h>
#include <stdlib.h>
#include <math.h>

#include "JetMatrix.h"

#include "Thermodynamics_SuperCO2_WaterAdimensionalized.h"

class FracFlow2PhasesVerticalAdimensionalized {
private:
    double cnw, cng, expw, expg;

    Thermodynamics_SuperCO2_WaterAdimensionalized *TD;
    double T_typical_;
protected:
public:
    FracFlow2PhasesVerticalAdimensionalized(double cnw_, double cng_, double expw_, double expg_, Thermodynamics_SuperCO2_WaterAdimensionalized *TD_);
    virtual ~FracFlow2PhasesVerticalAdimensionalized();

    int Diff_FracFlow2PhasesVerticalAdimensionalized(double sw, double Theta, int degree, JetMatrix &m);
};

#endif //  _FRACFLOW2PHASESVERTICALADIMENSIONALIZED_

