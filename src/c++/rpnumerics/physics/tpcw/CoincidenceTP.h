#ifndef _COINCIDENCETP_
#define _COINCIDENCETP_

#include "FluxFunction.h"
#include "AccumulationFunction.h"
#include "Matrix.h"
#include "RealVector.h"

#include "ImplicitFunction.h"
#include "ContourMethod.h"
#include "GridValues.h"

#include <stdio.h>
#include "Thermodynamics.h"
#include "Flux2Comp2PhasesAdimensionalized.h"
#include "Accum2Comp2PhasesAdimensionalized.h"

#define CHARACTERISTIC_SPEED_EVAPORATION 0
#define CHARACTERISTIC_SPEED_SATURATION  1

class CoincidenceTP : public ImplicitFunction {
    private:
    protected:
        const Thermodynamics *td;
        const Flux2Comp2PhasesAdimensionalized *fluxFunction_;
        double phi;

        double lambdas_function(const RealVector &u);
        double lambdae_function(const RealVector &u);

        static int coincidence_on_square(CoincidenceTP *obj, double *foncub, int i, int j);

        static int evaporation_on_square(CoincidenceTP *obj, double *foncub, int i, int j);
        double evaporation_level; // To be improved as a grid.

        static int saturation_on_square(CoincidenceTP *obj, double *foncub, int i, int j);
        double saturation_level; // To be improved as a grid.

        int (*fos)(CoincidenceTP *obj, double *foncub, int i, int j);
    public:
    // TODO: Note a fluxo introduzido...
    //    CoincidenceTPCW(const Flux2Comp2PhasesAdimensionalized *,const Accum2Comp2PhasesAdimensionalized *);


        CoincidenceTP(const Flux2Comp2PhasesAdimensionalized *);
        CoincidenceTP(){gv = 0;}
        ~CoincidenceTP();

        int function_on_square(double *foncub, int i, int j){
            return (*fos)(this, foncub, i, j);
        }

        int curve(const FluxFunction *f, const AccumulationFunction *a, 
                  GridValues &g, std::vector<RealVector> &coincidence_curve);

        int characteristic_speed_curve(const FluxFunction *f, const AccumulationFunction *a, 
                                       GridValues &g, 
                                       const RealVector &p, int type, 
                                       std::vector<RealVector> &curve, double &lev);
};

#endif // _COINCIDENCETP_

