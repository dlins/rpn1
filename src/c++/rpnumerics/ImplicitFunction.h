#ifndef _IMPLICITFUNCTION_
#define _IMPLICITFUNCTION_

#include "GridValues.h"

class ImplicitFunction {
    private:
    protected:
        GridValues *gv;

    public:
        ImplicitFunction(){gv = 0;}
        ~ImplicitFunction(){}

        virtual int function_on_square(double *foncub, int i, int j) = 0;

        virtual bool improvable(void){return false;}

        virtual void map(const RealVector &p, double &f, RealVector &map_Jacobian) {
            /*
            f = 0.0;
            map_Jacobian.component(0) = 0.0;
            map_Jacobian.component(1) = 0.0;
            */

            printf("ATTENTION, it is using NULL ImplicitFunction::map()\n");

            return;
        }

        GridValues * grid_value(void){return gv;}
};

#endif // _IMPLICITFUNCTION_

