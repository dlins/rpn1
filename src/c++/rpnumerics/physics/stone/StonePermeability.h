/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) StonePermeability.h
 */

#ifndef _StonePermeability_H
#define _StonePermeability_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "StonePermParams.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


class StonePermeability{
    
private:
  
    StonePermParams * params_;
    double denkw_, denkg_, denkow_, denkog_;
    
public:
    
    StonePermeability(const StonePermParams & params);
    StonePermeability(const StonePermeability & );
    virtual ~StonePermeability();
    
    const StonePermParams & params() const ;

};


inline const StonePermParams & StonePermeability::params() const{ return *params_; }

#endif //! _StonePermeability_H
