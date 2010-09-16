#ifndef _VectorField_H
#define	_VectorField_H
#include "WavePoint.h"

typedef int(*VectorFunction)(int, double, double*, double*);


class VectorField {
    
    public:
        VectorField(const VectorFunction);
        double sigmaCalc(const RealVector &);

        VectorFunction getFunction();
    
        private:
    
            VectorFunction f_;
};


#endif	/* _VectorField_H */

