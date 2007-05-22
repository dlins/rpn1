//
// File:   ODESigmaSolution.h
// Author: edsonlan
//
// Created on May 17, 2007, 2:10 PM
//

#ifndef _ODESigmaSolution_H
#define	_ODESigmaSolution_H

#include "ODESolution.h"

class ODESigmaSolution:public ODESolution{
    
    public:
        ODESigmaSolution();
        
        ODESigmaSolution(RealVector const * ,  double const * times, double const * sigma);
        double * getSigmaArray();
        
        private:
            
            double * sigma_;
            
};


#endif	/* _ODESigmaSolution_H */

