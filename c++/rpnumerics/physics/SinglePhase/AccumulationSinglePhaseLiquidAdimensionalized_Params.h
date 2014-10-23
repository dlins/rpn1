#ifndef _ACCUMULATIONSINGLEPHASELIQUIDADIMENSIONALIZED_PARAMS_
#define _ACCUMULATIONSINGLEPHASELIQUIDADIMENSIONALIZED_PARAMS_

#include <string>

#include "AccumulationParams.h"

//#include "Thermodynamics.h"

class AccumulationSinglePhaseLiquidAdimensionalized_Params : public AccumulationParams {
    private:
        std::string hsigmaC;
    protected:
    public:
        AccumulationSinglePhaseLiquidAdimensionalized_Params(double mc, double mw, double Press, 
                                                    const char *hsigmaC,
                                                    double Tr, double Tw,
                                                    double Cr,
                                                    double cw,
                                                    double T_typical,
                                                    double Rho_typical,
                                                    double U_typical,
                                                    double h_typical,
                                                    double phi);
        ~AccumulationSinglePhaseLiquidAdimensionalized_Params();

        const char * get_name(void);
};

#endif // _ACCUMULATIONSINGLEPHASELIQUIDADIMENSIONALIZED_PARAMS_

