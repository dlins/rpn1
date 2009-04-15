/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) ODEStopGenerator.h
 */

#ifndef _ODEStopGenerator_H
#define _ODEStopGenerator_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include <iostream>
#include "RealVector.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */

using namespace std;

class ODEStopGenerator {
    
    
public:
    virtual ~ODEStopGenerator();
    
    virtual ODEStopGenerator * clone() const =0;

    virtual bool check(const RealVector &)const = 0;
    

    
};

#endif //! _ODEStopGenerator_H
