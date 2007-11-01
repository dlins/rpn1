
#ifndef _ODESigmaSolution_H
#define	_ODESigmaSolution_H

#include "ODESolution.h"

class ODESigmaSolution:public ODESolution{
    
    public:
//        ODESigmaSolution();
        
//        ODESigmaSolution(RealVector const * ,  double const * times, double const * sigma);
        double * getSigmaArray();
        
        private:
            
            double * sigma_;
            
};


#endif	/* _ODESigmaSolution_H */

