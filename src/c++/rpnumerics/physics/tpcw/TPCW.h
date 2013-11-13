/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) TPCW.h
 */

#ifndef _TPCW_H
#define _TPCW_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "SubPhysics.h"
#include "Accum2Comp2PhasesAdimensionalized.h"
#include "Flux2Comp2PhasesAdimensionalized.h"
#include "Hugoniot_TP.h"
#include "HugoniotContinuation3D2D.h"
#include "ShockContinuationMethod3D2D.h"
#include "RectBoundary.h"
#include "Multid.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


class TPCW : public SubPhysics {
private:

    Thermodynamics_SuperCO2_WaterAdimensionalized * TD;
   


public:

    //    TPCW(const FluxFunction &, const AccumulationFunction &,  const Thermodynamics_SuperCO2_WaterAdimensionalized &);


    TPCW(const RealVector &, const string &);


    TPCW(const TPCW &);

    SubPhysics * clone()const;

    Boundary * defaultBoundary()const;
    
//    void setParams(vector<string> params) ;


    void setParams(vector<string>);

    vector<double> *  getParams();
    void preProcess(RealVector &);
    void postProcess(vector<RealVector> &);
    void postProcess(RealVector &);

    void boundary(const Boundary &);
    


    double T2Theta(double)const;
    double Theta2T(double)const;

    virtual ~TPCW();
};

#endif //! _TPCW_H

