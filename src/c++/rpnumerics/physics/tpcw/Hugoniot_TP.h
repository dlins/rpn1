#ifndef _HUGONIOT_TP_
#define _HUGONIOT_TP_


#include "Hugoniot_Locus.h"

class Hugoniot_TP : public Hugoniot_Locus {
    private:
        RealVector Fref, Gref;
        RealVector Uref;

        const FluxFunction *ff;
        const AccumulationFunction *aa;
 
    public:
        Hugoniot_TP(){gv = 0;}
        ~Hugoniot_TP();

        double complete_points(const RealVector &Uplus);

        int function_on_square(double *foncub, int i, int j);

        int classified_curve(const FluxFunction *f, const AccumulationFunction *a, 
                             GridValues &g, const RealVector &r, 
                             std::vector<HugoniotPolyLine> &hugoniot_curve);

        int curve(const FluxFunction *f, const AccumulationFunction *a, 
                  GridValues &g, const RealVector &r,
                  std::vector<RealVector> &hugoniot_curve);

        void map(const RealVector &p, double &f, RealVector &map_Jacobian);

        bool improvable(void);
};

#endif // _HUGONIOT_TP_

