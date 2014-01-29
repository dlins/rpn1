/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) StoneNegativePermeability.h
 */

#ifndef _StoneNegativePermeability_H
#define _StoneNegativePermeability_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include <math.h>
#include "StoneNegativePermParams.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


class StoneNegativePermeability{
    
private:
    StoneNegativePermParams * params_;

    double expw_, expg_, expo_;
    double expow_, expog_;

    double krw_p_, krg_p_, kro_p_;
    // It is akward to have a connate saturation of gas different to zero.
    
    double cnw_, cng_, cno_;
    double CN;
    double lw_, lg_,lo_;
    double low_, log_;
    double epsl_;

    double denkw_, denkg_,  denko_,denkow_, denkog_;

    
    // The following flags allow to have negative saturations.
    bool NegativeWaterSaturation, NegativeGasSaturation, NegativeOilSaturation;




    
public:
    
    StoneNegativePermeability(const StoneNegativePermParams & params);
    StoneNegativePermeability(const StoneNegativePermeability & );
    virtual ~StoneNegativePermeability();
    
    const StoneNegativePermParams & params() const ;

    void Diff_PermabilityWater(double, double, double, double&, double&, double&, double&, double&, double&);
    void Diff_PermabilityOil(double, double, double, double&, double&, double&, double&, double&, double&);
    void Diff_PermabilityGas(double, double, double, double&, double&, double&, double&, double&, double&);

};


inline const StoneNegativePermParams & StoneNegativePermeability::params() const{ return *params_; }

#endif //! _StoneNegativePermeability_H
