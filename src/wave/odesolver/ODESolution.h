#ifndef _ODESolution_H
#define	_ODESolution_H

#include "WavePoint.h"

class ODESolution {
    public:

        //
        // Constants
        //
        
        static int STOP_OUT_OF_BOUNDARY;
        static int STOP_ON_POINCARE_SECTION;
        static int MADE_MAXIMUM_STEP_NUMBER;
        
        ODESolution(const RealVector *, double *, int);
        ODESolution();
        ~ODESolution();
        
        double getTimes();
        int getFlag() ;

        RealVector * getCoords();
        
        protected :
            RealVector * coords_;
            double * times_;
            int flag_;

};


#endif	/* _ODESolution_H */

