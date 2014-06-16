/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) TPCWLiquid.h
 */

#ifndef _TPCWLiquid_H
#define _TPCWLiquid_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "SubPhysics.h"


#include"Thermodynamics.h"
#include "Hugoniot_TP.h"
#include "FluxSinglePhaseLiquidAdimensionalized.h"
#include "AccumulationSinglePhaseLiquidAdimensionalized.h"
#include "HugoniotContinuation3D2D.h"
#include "SinglePhaseBoundary.h"
#include "Double_Contact_TP.h"


/*
 * ---------------------------------------------------------------
 * Definitions:
 */


class TPCWLiquid :public SubPhysics{

private:
    Thermodynamics *tc_ ;
    
    VLE_Flash_TPCW *flash_;

    Boundary * defaultBoundary(VLE_Flash_TPCW * flash)const;

public:
    

    
    TPCWLiquid(const RealVector &, Thermodynamics *, VLE_Flash_TPCW *, const string &);


    TPCWLiquid(const TPCWLiquid &);

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

    virtual ~TPCWLiquid();

};

#endif //! _TPCWLiquid_H
