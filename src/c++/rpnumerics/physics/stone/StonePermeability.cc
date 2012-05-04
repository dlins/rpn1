/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) StonePermeability.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "StonePermeability.h"
#include <iostream>

using namespace std;

/*
 * ---------------------------------------------------------------
 * Definitions:
 */

/** For the Corey model it is implemented the one in:
 *     LaForce and Johns (2004) - "Analytical theory for three-phase partially miscible
 *                                 flow in ternary systems", SPE 89438
 *  Even if in the next papers one allows the gas critical saturation to be differente
 *  to zero. LaForce and Jonhs is the only paper that sets for saturations bigger than 1
 *  minus the two other residual saturations, the actual saturation as the endpoint
 *  permeability of such phase.
 *
 *  There is also a small references to this kind of permeabilities in:
 *     Juanes and Patzek (2002) - "Three-phase displacement theory: An improved
 *                                 description of relative permeabilities", SPE 77539
 *  or:
 *     Foulser, Goodyear and Sims (1992) - "Two and three phase relative permeabilities
 *                                          at high capillary numbers", CIM 92-72
 *
 *  A basic comparation for the construction is made in:
 *     Parker & Lenhard (1990) - "Determining three-phase permeability-saturation-pressure
 *                                relations from two-phase system measurements", JPSE 4
 *
 **/



StonePermeability::StonePermeability(const StonePermParams & params) : params_(new StonePermParams(params)) {

    expw_ = params_->component(0);
    expg_ = params_->component(1);
    expo_ = params_->component(2);

    expow_ = params_->component(3);
    expog_ = params_->component(4);


    krw_p_ = params_->component(5);
    krg_p_ = params_->component(6);
    kro_p_ = params_->component(7);


    cnw_ = params_->component(8);
    cng_ = params_->component(9);
    cno_ = params_->component(10);


    CN = cnw_ + cng_ + cno_;

    lw_ = params_->component(11);
    lg_ = params_->component(12);
    lo_ = params_->component(13);

    low_ = params_->component(14);
    log_ = params_->component(15);

    epsl_ = params_->component(16);

    if (params_->component(17) == 0.0)
        NegativeWaterSaturation = false;
    else
        NegativeWaterSaturation = true;

    if (params_->component(18) == 0.0)
        NegativeGasSaturation = false;
    else
        NegativeGasSaturation = true;

    if (params_->component(19) == 0.0)
        NegativeOilSaturation = false;
    else
        NegativeOilSaturation = true;


    // Estes sao krw_p/(pakman:denkw)
    denkw_ = krw_p_ / ((lw_ + (1. - lw_) * pow(1. - CN, expw_ - 1.))*(1. - CN));
    denkg_ = krg_p_ / ((lg_ + (1. - lg_) * pow(1. - CN, expg_ - 1.))*(1. - CN));
    denko_ = kro_p_ / ((lo_ + (1. - lo_) * pow(1. - CN, expo_ - 1.))*(1. - CN));

    // Estes os antigos 1/(pakman:denkow)
    denkow_ = 1. / ((low_ + (1. - low_) * pow(1. - CN, expow_ - 1.))*(1. - CN));
    denkog_ = 1. / ((log_ + (1. - log_) * pow(1. - CN, expog_ - 1.))*(1. - CN));
    //




}

StonePermeability::StonePermeability(const StonePermeability & copy) : params_(new StonePermParams(copy.params())) {

    expw_ = copy.expw_;
    expg_ = copy.expg_;
    expo_ = copy.expo_;

    expow_ = copy.expow_;
    expog_ = copy.expog_;

    krw_p_ = copy.krw_p_;
    krg_p_ = copy.krg_p_;
    kro_p_ = copy.kro_p_;

    cnw_ = copy.cnw_;
    cng_ = copy.cng_;
    cno_ = copy.cno_;

    CN = copy.CN;

    lw_ = copy.lw_;
    lg_ = copy.lg_;
    lo_ = copy.lo_;

    low_ = copy.low_;
    log_ = copy.log_;

    epsl_ = copy.epsl_;


    NegativeWaterSaturation = copy.NegativeWaterSaturation;
    NegativeGasSaturation = copy.NegativeGasSaturation;
    NegativeOilSaturation = copy.NegativeOilSaturation;


    denkw_ = copy.denkw_;
    denkg_ = copy.denkg_;
    denko_ = copy.denko_;

    denkow_ = copy.denkow_;
    denkog_ = copy.denkog_;


}

StonePermeability::~StonePermeability() {
    delete params_;
}

void StonePermeability::Diff_PermabilityWater(double sw, double so, double sg,
                        double &kw, double &dkw_dsw, double &dkw_dso,
                        double &d2kw_dsw2, double &d2kw_dswso, double &d2kw_dso2) {
    double swcnw = sw - cnw_;
    if (!NegativeWaterSaturation && (swcnw <= 0.)) {
        kw = 0.;
        dkw_dsw = 0.;
        dkw_dso = 0.;

        d2kw_dsw2  = 0.;
        d2kw_dswso = 0.;
        d2kw_dso2  = 0.;
    } else if (!NegativeWaterSaturation && (swcnw > 1. - CN)) {
        kw = krw_p_;

        dkw_dsw = 0.;
        dkw_dso = 0.;

        d2kw_dsw2  = 0.;
        d2kw_dswso = 0.;
        d2kw_dso2  = 0.;
    } else {
        // Because of the use of flags that alow sw = cnw to pass, we need the following condition:
        //
        double powsw2 = ( (swcnw != 0.0) ? pow(swcnw, expw_ - 2.) : 0.0);
        double powsw1 = swcnw*powsw2;

        kw = (lw_ + (1. - lw_) * powsw1) * swcnw*denkw_;
        dkw_dsw = (lw_ + (1. - lw_) * expw_ * powsw1) * denkw_;
        dkw_dso = 0.; // Zero, kw do not depend on so

        d2kw_dsw2  = (1. - lw_) * expw_ * (expw_ - 1.) * powsw2*denkw_;
        d2kw_dswso = 0.; // Zero, kw do not depend on so
        d2kw_dso2  = 0.; // Zero, kw do not depend on so
    }

    return;
}

/** The complex part of the permeability calculation resides here, for the oil, where a
 *  mixture of models is possible: epsl = 0 means Corey, epsl = 1, Stone.
 *     lo, low, lw, log, etcetera represent linear against the mixtured Corey-Stone model.
 *  In the same spirit of ideas for saturations below the critical ones, we expand the
 *  Stone model with the value for that saturation as the critical. This means, if water
 *  saturation is lesser than cnw, we take new_sw = cnw and new_sg = 1 - cnw - so; the oil
 *  saturation remains. (Notice the separation in cases.)
 *
 *            O
 *            /\
 *           /\ \          <-- ko is the endpoint oil permeability.
 *          /  \/\
 *         /   /\ \
 *        /   /  \ \       <-- Region 2 has the Stone with general powers.
 *       /1  / 2  \3\          Region 1 and 3 are evaluated with the new variables
 *      /   /      \ \         matching the critical or connate saturations.
 *     /---+--------+-\
 *    /___/__________\_\   <-- ko is identically zero beneath cno.
 *   G                  W
 *
 **/


void StonePermeability::Diff_PermabilityOil(double sw, double so, double sg,
                        double &ko, double &dko_dsw, double &dko_dso, 
                        double &d2ko_dsw2, double &d2ko_dswso, double &d2ko_dso2) {
    double socno = so - cno_;
    double sow = 1. - sw - cng_ - cno_;
    double sog = 1. - cnw_ - sg - cno_;

    // The auxiliar denominators could be depending upon sw, so or sg. Relevant position.
    double ko1den, dko1den_ds, d2ko1den_ds2;
    double ko2den, dko2den_ds, d2ko2den_ds2;
    double dko_Aux, dko_Aux2, d2ko_Aux;

    if (!NegativeOilSaturation && (socno <= 0.)) {
        ko = 0.;
        dko_dsw = 0.;
        dko_dso = 0.;

        d2ko_dsw2  = 0.;
        d2ko_dswso = 0.;
        d2ko_dso2  = 0.;
    } else if (!NegativeOilSaturation && (socno > 1. - CN)) {
        ko = kro_p_;
        dko_dsw = 0.;
        dko_dso = 0.;

        d2ko_dsw2  = 0.;
        d2ko_dswso = 0.;
        d2ko_dso2  = 0.;
    } else {
        if (!NegativeWaterSaturation && (sw < cnw_)) {
            // Because of the use of flags that alow so = cno to pass, we need the following condition:
            //
            double powso3 = ( (socno != 0.0) ? pow(socno, expog_ - 3.) : 0.0 );
            double powso2 = socno*powso3;
            double powso1 = socno*powso2;

            ko1den = (low_ + (1. - low_) * pow(1. - CN, expow_ - 1.)) * denkow_;
            ko2den = (log_ + (1. - log_) * powso1) * denkog_;
            dko2den_ds   = (1. - log_)*(expog_ - 1.) * powso2*denkog_;
            d2ko2den_ds2 = (1. - log_)*(expog_ - 2.)*(expog_ - 1.) * powso3*denkog_;

            dko_Aux  = 0.;
            dko_Aux2 = 0.;
            d2ko_Aux = 0.;
        } else if (!NegativeGasSaturation && (sg < cng_)) {
            // Because of the use of flags that alow so = cno to pass, we need the following condition:
            //
            double powso3 = ( (socno != 0.0) ? pow(socno, expow_ - 3.) : 0.0 );
            double powso2 = socno*powso3;
            double powso1 = socno*powso2;

            ko1den = (log_ + (1. - log_) * pow(1. - CN, expog_ - 1.)) * denkog_;
            ko2den = (low_ + (1. - low_) * powso1) * denkow_;
            dko2den_ds = (1. - low_)*(expow_ - 1.) * powso2*denkow_;
            d2ko2den_ds2 = (1. - low_)*(expow_ - 2.)*(expow_ - 1.) * powso3*denkow_;

            dko_Aux  = 0.;
            dko_Aux2 = 0.;
            d2ko_Aux = 0.;
        } else {
            // Because of the use of flags that alow sow = 0.0 to pass, we need the following condition:
            //
            double powsow3 = ( (sow != 0.0) ? pow(sow, expow_ - 3.) : 0.0 );
            double powsow2 = sow*powsow3;
            double powsow1 = sow*powsow2;

            // Because of the use of flags that alow sog = 0.0 to pass, we need the following condition:
            //
            double powsog3 = ( (sog != 0.0) ? pow(sog, expog_ - 3.) : 0.0 );
            double powsog2 = sog*powsog3;
            double powsog1 = sog*powsog2;

            ko1den       = (low_ + (1. - low_) * powsow1) * denkow_;
            dko1den_ds   = (1. - low_)*(expow_ - 1.) * powsow2*denkow_;
            d2ko1den_ds2 = (1. - low_)*(expow_ - 2.)*(expow_ - 1.) * powsow3*denkow_;

            ko2den = (log_ + (1. - log_) * powsog1) * denkog_;

            dko2den_ds   = (1. - log_)*(expog_ - 1.) * powsog2*denkog_;
            d2ko2den_ds2 = (1. - log_)*(expog_ - 2.)*(expog_ - 1.) * powsog3*denkog_;

            dko_Aux  = dko2den_ds * ko1den - ko2den*dko1den_ds;
            dko_Aux2 = d2ko2den_ds2 * ko1den - dko2den_ds*dko1den_ds;
            d2ko_Aux = d2ko2den_ds2 * ko1den - 2. * dko2den_ds * dko1den_ds + ko2den*d2ko1den_ds2;
        }
        // Because of the use of flags that alow so = cno to pass, we need the following condition:
        //
        double powso2 = ( (socno != 0.0) ? pow(socno, expo_ - 2.) : 0.0 );
        double powso1 = socno*powso2;
        double StoneC = epsl_ * kro_p_ * (1. - CN);

        ko         = StoneC * socno * ko1den * ko2den
                   + (1. - epsl_)*(lo_ + (1. - lo_) * powso1) * socno * denko_;
        dko_dsw    = StoneC * socno * dko_Aux;
                   // Plus zero Corey contribution.
        dko_dso    = StoneC * ko1den * (ko2den + socno * dko2den_ds)
                   + (1. - epsl_)*(lo_ + (1. - lo_) * expo_ * powso1) * denko_;
        d2ko_dsw2  = StoneC * socno * d2ko_Aux;
                   // Plus zero Corey contribution.
        d2ko_dswso = StoneC * (dko_Aux + socno * dko_Aux2);
                   // Plus zero Corey contribution.
        d2ko_dso2  = StoneC * ko1den * (2. * dko2den_ds + socno * d2ko2den_ds2)
                   + (1. - epsl_)*(1. - lo_) * expo_ * (expo_ - 1.) * powso2 * denko_;
    }

    return;
}

void StonePermeability::Diff_PermabilityGas(double sw, double so, double sg, 
                        double &kg, double &dkg_dsw, double &dkg_dso, 
                        double &d2kg_dsw2, double &d2kg_dswso, double &d2kg_dso2) {
    double sgcng = sg - cng_;
    if (!NegativeGasSaturation && (sgcng <= 0.)) {
        kg = 0.;
        dkg_dsw = 0.;
        dkg_dso = 0.;

        d2kg_dsw2  = 0.;
        d2kg_dswso = 0.;
        d2kg_dso2  = 0.;
    } else if (!NegativeGasSaturation && (sgcng > 1. - CN)) {
        kg = krg_p_;
        dkg_dsw = 0.;
        dkg_dso = 0.;

        d2kg_dsw2  = 0.;
        d2kg_dswso = 0.;
        d2kg_dso2  = 0.;
    } else {
        double powsg2 = pow(sgcng, expg_ - 2.);
        double powsg1 = sgcng*powsg2;

        kg = (lg_ + (1. - lg_) * powsg1) * sgcng*denkg_;

        dkg_dsw = -(lg_ + (1. - lg_) * expg_ * powsg1) * denkg_;
        dkg_dso = -(lg_ + (1. - lg_) * expg_ * powsg1) * denkg_;

        d2kg_dsw2  = (1. - lg_) * expg_ * (expg_ - 1.) * powsg2*denkg_;
        d2kg_dswso = (1. - lg_) * expg_ * (expg_ - 1.) * powsg2*denkg_;
        d2kg_dso2  = (1. - lg_) * expg_ * (expg_ - 1.) * powsg2*denkg_;
    }

    return;
}

