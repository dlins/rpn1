#ifndef _ACCUMULATIONSINGLEPHASEVAPORADIMENSIONALIZED_PARAMS_
#define _ACCUMULATIONSINGLEPHASEVAPORADIMENSIONALIZED_PARAMS_

#include <string>

#include "AccumulationParams.h"

#include "Thermodynamics.h"

class AccumulationSinglePhaseVaporAdimensionalized_Params : public AccumulationParams {
    private:
        std::string hsigmaC;
    protected:
    public:
        AccumulationSinglePhaseVaporAdimensionalized_Params(double mc, double mw, double Press, 
                                                    const char *hsigmaC,
                                                    double Tr, double Tw,
                                                    double Cr,
                                                    double cw,
                                                    double T_typical,
                                                    double Rho_typical,
                                                    double U_typical,
                                                    double h_typical,
                                                    double phi);
        ~AccumulationSinglePhaseVaporAdimensionalized_Params();

        const char * get_name(void);
};

#endif // _ACCUMULATIONSINGLEPHASEVAPORADIMENSIONALIZED_PARAMS_

