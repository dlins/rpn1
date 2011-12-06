#ifndef _IMPLICITFUNCTION_
#define _IMPLICITFUNCTION_

#include <stdio.h>
#include "RealVector.h"

class ImplicitFunction {
    private:
    protected:
    public:
        virtual int function_on_square(double *foncub, int i, int j, int is_square) = 0;

        virtual bool improvable(void){return false;}
        virtual void map(const RealVector &p, double &f, RealVector &map_Jacobian) {
//            f = 0.0;
//            map_Jacobian.component(0) = 0.0;
//            map_Jacobian.component(1) = 0.0;

            printf("ATTENTION, it is using NULL ImplicitFunction::map()\n");

            return;
        }
};

#endif // _IMPLICITFUNCTION_

