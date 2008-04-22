/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) Permeability.h
 */

#ifndef _Permeability_H
#define _Permeability_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "PermParams.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


class Permeability{
    
private:
    
    
    double denkw_, denkg_, denkow_, denkog_;
  
    PermParams * params_;
    
public:
    
    Permeability(const PermParams & params);
    
    Permeability(const Permeability & );
    
    virtual ~Permeability();
    
    const PermParams & params() const ;
    
    double kw(double sw, double so, double sg)const;
    
    double kowden(double sw, double so, double sg)const;
        
    double kogden(double sw, double so, double sg)const;
    
    double ko(double sw, double so, double sg)const;
    
    double kg(double sw, double so, double sg)const;
        
    double dkwdso(double sw, double so, double sg)const;
        
    double dkwdsw(double sw, double so, double sg)const;
        
    // computes the derivative of kow divided by den = 1. - sw + params_.cnw() relative to sw
    double dkowdendsw(double sw, double so, double sg)const;
        
    double dkogdendsg(double sw, double so, double sg)const;
        
    double dkodsw(double sw, double so, double sg)const;
        
    double dkodso(double sw, double so, double sg)const;
        
    double dkgdsw(double sw, double so, double sg)const;
        
    double dkgdso(double sw, double so, double sg)const;
        
    double dkgdsg(double sw, double so, double sg)const;
        
    double dkwdww(double sw, double so, double sg)const;
        
    double dkwdwo(double sw, double so, double sg)const;
    
    double dkwdoo(double sw, double so, double sg)const;
        
    // computes the second derivative of kow divided by
    // den = 1. - sw + params_.cnw() relative to sw
    double dkowdendww(double sw, double so, double sg)const;
        
    // computes the second derivative of kog divided by
    // den = 1. - sg + params_.cng() relative to sg
    double dkogdendgg(double sw, double so, double sg)const;
        
    // computes the second derivative of ko relative to sw
    double dkodww(double sw, double so, double sg)const;
        
    // computes the mixed second derivative of ko relative to sw and so
    double dkodwo(double sw, double so, double sg)const;
        
    // computes the second derivative of ko relative to so
    double dkodoo(double sw, double so, double sg)const;
        
    double dkgdww(double sw, double so, double sg)const;
        
    double dkgdwo(double sw, double so, double sg)const;
        
    double dkgdoo(double sw, double so, double sg)const;
        
};


inline const PermParams & Permeability::params() const{ return *params_; }

#endif //! _Permeability_H
