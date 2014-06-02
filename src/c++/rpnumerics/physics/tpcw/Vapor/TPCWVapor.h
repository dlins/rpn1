/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) TPCWVapor.h
 */

#ifndef _TPCWVapor_H
#define _TPCWVapor_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "SubPhysics.h"


#include"Thermodynamics.h"
#include "Hugoniot_TP.h"

#include "FluxSinglePhaseVaporAdimensionalized.h"
#include "AccumulationSinglePhaseVaporAdimensionalized.h"
#include "SinglePhaseBoundary.h"
#include "HugoniotContinuation3D2D.h"
#include "Double_Contact_TP.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


class TPCWVapor : public SubPhysics {
private:
    Thermodynamics *tc_;
    VLE_Flash_TPCW *flash_;
    
    Boundary * defaultBoundary(VLE_Flash_TPCW *)const;

public:

    TPCWVapor(const RealVector &,Thermodynamics * ,VLE_Flash_TPCW *, const string &);


    TPCWVapor(const TPCWVapor &);

    SubPhysics * clone()const;

    Boundary * defaultBoundary()const;

    //    void setParams(vector<string> params) ;


    void setParams(vector<string>);

    vector<double> * getParams();
    void preProcess(RealVector &);
    void postProcess(vector<RealVector> &);
    void postProcess(RealVector &);

    void boundary(const Boundary &);




    virtual ~TPCWVapor();


};

#endif //! _TPCWVapor_H
