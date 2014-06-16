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
#include <math.h>
#include "StonePermParams.h"
#include "JetMatrix.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


class StonePermeability{
    
    private:
    protected:
        StonePermParams * params_;

        double expw_, expg_, expo_;
        double expow_, expog_;
        double cnw_, cng_, cno_;
        double lw_, lg_;
        double low_, log_;
        double epsl_;

        double denkw_, denkg_, denkow_, denkog_;
    
        int kowden_jet(double sow, int degree, JetMatrix &kowj);
        int kogden_jet(double sog, int degree, JetMatrix &kogj);
    public:
        StonePermeability(const StonePermParams & params);
        StonePermeability(const StonePermeability & );
        virtual ~StonePermeability();
    
        const StonePermParams & params() const;

        void Diff_PermabilityWater(double, double, double, double&, double&, double&, double&, double&, double&);
        void Diff_PermabilityOil(double, double, double, double&, double&, double&, double&, double&, double&);
        void Diff_PermabilityGas(double, double, double, double&, double&, double&, double&, double&, double&);

        int PermeabilityWater_jet(const RealVector &state, int degree, JetMatrix &water);
        int PermeabilityOil_jet(const RealVector &state, int degree, JetMatrix &oil);
        int PermeabilityGas_jet(const RealVector &state, int degree, JetMatrix &gas);

        void reduced_permeability(const RealVector &state, RealVector &rp) const;
};


inline const StonePermParams & StonePermeability::params() const{ return *params_; }

#endif //! _StonePermeability_H
