#ifndef _POLYDISPERSIVE_PARAMS_
#define _POLYDISPERSIVE_PARAMS_

#include "FluxParams.h"

class Polydispersive_Params : public FluxParams {
    private:
    protected:
    public:
        Polydispersive_Params(const double phimax,
                              const double V1inf, const double V2inf,
                              const double n1, const double n2);
        Polydispersive_Params();

        ~Polydispersive_Params();

};

#endif // _POLYDISPERSIVE_PARAMS_

