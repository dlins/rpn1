/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) LSODESolution.h
 */

#ifndef _LSODESolution_H
#define _LSODESolution_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "ODESolution.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */

class LSODESolution:public ODESolution {

private:
    
    double * y_;
    double t_;
    double tout_;
    int istate_;

public:
    
    int state()const;
    

};

#endif //! _LSODESolution_H
