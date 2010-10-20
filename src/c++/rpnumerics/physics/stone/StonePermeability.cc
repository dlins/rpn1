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


StonePermeability::StonePermeability(const StonePermParams & params) : params_(new StonePermParams(params)) {
    expw_  = params_->component(0);
    expg_  = params_->component(1);
    expo_  = params_->component(2);

    expow_ = params_->component(3);
    expog_ = params_->component(4);

    cnw_   = params_->component(5);
    cng_   = params_->component(6);
    cno_   = params_->component(7);

    lw_    = params_->component(8);
    lg_    = params_->component(9);

    low_   = params_->component(10);
    log_   = params_->component(11);
        
    epsl_  = params_->component(12);

    denkw_ = (lw_ + (1. - lw_)*pow(1. - cnw_, expw_ - 1.))*(1. - cnw_);
    denkg_ = (lg_ + (1. - lg_)*pow(1. - cng_, expg_ - 1.))*(1. - cng_);

    denkow_ = (low_ + (1. - low_)*pow(1. - cno_, expow_ - 1.))*(1. - cno_);
    denkog_ = (log_ + (1. - log_)*pow(1. - cno_, expog_ - 1.))*(1. - cno_);
}

StonePermeability::StonePermeability(const StonePermeability & copy): params_(new StonePermParams(copy.params())) {
    
    expw_  = copy.expw_;
    expg_  = copy.expg_;
    expo_  = copy.expo_;

    expow_ = copy.expow_;
    expog_ = copy.expog_;

    cnw_   = copy.cnw_;
    cng_   = copy.cng_;
    cno_   = copy.cno_;

    lw_    = copy.lw_;
    lg_    = copy.lg_;

    low_   = copy.low_;
    log_   = copy.log_;
        
    epsl_  = copy.epsl_;

    denkw_ = (lw_ + (1. - lw_)*pow(1. - cnw_, expw_ - 1.))*(1. - cnw_);
    denkg_ = (lg_ + (1. - lg_)*pow(1. - cng_, expg_ - 1.))*(1. - cng_);

    denkow_ = (low_ + (1. - low_)*pow(1. - cno_, expow_ - 1.))*(1. - cno_);
    denkog_ = (log_ + (1. - log_)*pow(1. - cno_, expog_ - 1.))*(1. - cno_);

}


StonePermeability::~StonePermeability() {
    delete params_;
}

void StonePermeability::Diff_PermabilityWater(double sw, double so, double sg, double &kw, double &dkw_dsw, double &dkw_dso, double &d2kw_dsw2, double &d2kw_dswso, double &d2kw_dso2){
    double swcnw = sw - cnw_;
    if (swcnw < 0.){ 
        kw = 0.;
        dkw_dsw = 0.;
        dkw_dso = 0.;

        d2kw_dsw2 = 0.;
        d2kw_dswso = 0.;
        d2kw_dso2 = 0.;
    }
    else {
        kw = (lw_ + (1. - lw_)*pow(swcnw, expw_ - 1.))*swcnw/denkw_;
        dkw_dsw = (lw_ + (1. - lw_)*expw_*pow(swcnw, expw_ - 1.))/denkw_;
        dkw_dso = 0.;		// Zero, kw do not depend on so

        d2kw_dsw2 = (1. - lw_)*expw_*(expw_ - 1.)*pow(swcnw, expw_ - 2.)/denkw_;
        d2kw_dswso = 0.;	// Zero, kw do not depend on so
        d2kw_dso2 = 0.;		// Zero, kw do not depend on so
    }

    return;
}

void StonePermeability::Diff_PermabilityOil(double sw, double so, double sg, double &ko, double &dko_dsw, double &dko_dso, double &d2ko_dsw2, double &d2ko_dswso, double &d2ko_dso2){
    double socno = so - cno_;

    double sow = 1. - sw - cno_;
    double sog = 1. - sg - cno_;

    double kowden, dkowden_dsw, d2kowden_dsw2;
    double kogden, dkogden_dsg, d2kogden_dsg2;

    if (sow < 0.){
        kowden = 0.;
        dkowden_dsw = 0.;
        d2kowden_dsw2 = 0.;
    }
    else {
        kowden = (low_ + (1. - low_)*pow(sow, expow_ - 1.))/denkow_;
        dkowden_dsw = -(expow_ - 1.)*(1. - low_)*pow(sow, expow_ - 2.)/denkow_;
        d2kowden_dsw2 = (expow_ - 2.)*(expow_ - 1.)*(1. - low_)*pow(sow, expow_ - 3.)/denkow_;
    }

    if (sog < 0.){
        kogden = 0.;
        dkogden_dsg = 0.;
        d2kogden_dsg2 = 0.;
    }
    else {
        kogden = (log_ + (1. - log_)*pow(sog, expog_ - 1.))/denkog_;
        dkogden_dsg = -(expog_ - 1.)*(1. - log_)*pow(sog, expog_ - 2.)/denkog_;
        d2kogden_dsg2 = (expog_ - 2.)*(expog_ - 1.)*(1. - log_)*pow(sog, expog_ - 3.)/denkog_;
    }

    if (socno < 0.){ 
        ko = 0.;
        dko_dsw = 0.;
        dko_dso = 0.;

        d2ko_dsw2 = 0.;
        d2ko_dswso = 0.;
        d2ko_dso2 = 0.;
    }
    else {
        ko = (socno*(1. - cno_)*kogden*kowden)*epsl_ + (1. - epsl_)*pow(socno, expo_);
        dko_dsw = (-dkogden_dsg*kowden + kogden*dkowden_dsw)*socno*(1. - cno_)*epsl_;
        dko_dso = (-socno*dkogden_dsg + kogden)*(1. - cno_)*kowden*epsl_ + (1. - epsl_)*expo_*pow(socno, expo_ - 1.);

        d2ko_dsw2 = (d2kogden_dsg2*kowden + kogden*d2kowden_dsw2 - 2.*dkogden_dsg*dkowden_dsw)*socno*(1. - cno_)*epsl_;
        d2ko_dswso = ((d2kogden_dsg2*kowden - dkogden_dsg*dkowden_dsw)*socno + (-dkogden_dsg*kowden + kogden*dkowden_dsw))*(1. - cno_)*epsl_;
        d2ko_dso2 = (-2.*dkogden_dsg + socno*d2kogden_dsg2)*(1. - cno_)*kowden*epsl_ + (1. - epsl_)*expo_*(expo_ - 1.)*pow(socno, expo_ - 2.);
    }

    return;
}

void StonePermeability::Diff_PermabilityGas(double sw, double so, double sg, double &kg, double &dkg_dsw, double &dkg_dso, double &d2kg_dsw2, double &d2kg_dswso, double &d2kg_dso2){
    double sgcng = sg - cng_;
    if (sgcng < 0.){
        kg = 0.;
        dkg_dsw = 0.;
        dkg_dso = 0.;

        d2kg_dsw2 = 0.;
        d2kg_dswso = 0.;
        d2kg_dso2 = 0.;
    }
    else {
        kg = (lg_ + (1. - lg_)*pow(sgcng, expg_ - 1.))*sgcng/denkg_;
        dkg_dsw = -(lg_ + (1. - lg_)*expg_*pow(sgcng, expg_ - 1.))/denkg_;
        dkg_dso = -(lg_ + (1. - lg_)*expg_*pow(sgcng, expg_ - 1.))/denkg_;

        d2kg_dsw2 = (1. - lg_)*expg_*(expg_ - 1.)*pow(sgcng, expg_ - 2.)/denkg_;
        d2kg_dswso = (1. - lg_)*expg_*(expg_ - 1.)*pow(sgcng, expg_ - 2.)/denkg_;
        d2kg_dso2 = (1. - lg_)*expg_*(expg_ - 1.)*pow(sgcng, expg_ - 2.)/denkg_;
    }

    return;
}

