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

// Expects kowj.size() == 1.
//
int StonePermeability::kowden_jet(double sow, int degree, JetMatrix &kowj){
    if (sow < 0.){
        if (degree >= 0){
            double kowden = 0.;
            kowj.set(0, kowden);

            if (degree >= 1){
                double dkowden_dsw = 0.;
                kowj.set(0, 0, dkowden_dsw);

                if (degree >= 2){
                    double d2kowden_dsw2 = 0.;
                    kowj.set(0, 0, 0, d2kowden_dsw2);
                }
            }
        }
    }
    else {
        if (degree >= 0){
            double kowden = (low_ + (1. - low_)*pow(sow, expow_ - 1.))/denkow_;
            kowj.set(0, kowden);

            if (degree >= 1){
                double dkowden_dsw = -(expow_ - 1.)*(1. - low_)*pow(sow, expow_ - 2.)/denkow_;
                kowj.set(0, 0, dkowden_dsw);

                if (degree >= 2){
                    double d2kowden_dsw2 = (expow_ - 2.)*(expow_ - 1.)*(1. - low_)*pow(sow, expow_ - 3.)/denkow_;
                    kowj.set(0, 0, 0, d2kowden_dsw2);
                }
            }
        }
    }

    return degree;
}

// Expects kogj.size() == 1.
//
int StonePermeability::kogden_jet(double sog, int degree, JetMatrix &kogj){
    if (sog < 0.){
        if (degree >= 0){
            double kogden = 0.;
            kogj.set(0, kogden);

            if (degree >= 1){
                double dkogden_dsg = 0.;
                kogj.set(0, 0, dkogden_dsg);

                if (degree >= 2){
                    double d2kogden_dsg2 = 0.;
                    kogj.set(0, 0, 0, d2kogden_dsg2);
                }
            }
        }
    }
    else {
        if (degree >= 0){
            double kogden = (log_ + (1. - log_)*pow(sog, expog_ - 1.))/denkog_;
            kogj.set(0, kogden);

            if (degree >= 1){
                double dkogden_dsg = -(expog_ - 1.)*(1. - log_)*pow(sog, expog_ - 2.)/denkog_;
                kogj.set(0, 0, dkogden_dsg);

                if (degree >= 2){
                    double d2kogden_dsg2 = (expog_ - 2.)*(expog_ - 1.)*(1. - log_)*pow(sog, expog_ - 3.)/denkog_;
                    kogj.set(0, 0, 0, d2kogden_dsg2);
                }
            }
        }
    }

    return degree;
}

// Expects that w.size() == 2.
//
int StonePermeability::PermeabilityWater_jet(const RealVector &state, int degree, JetMatrix &water){
    double sw = state.component(0);
    double so = state.component(1);
    double sg = 1.0 - sw - so;

    double swcnw = sw - cnw_;
    if (swcnw < 0.){ 
        if (degree >= 0){
            double kw = 0.;
            water.set(0, kw);

            if (degree >= 1){
                double dkw_dsw = 0.;
                double dkw_dso = 0.;

                water.set(0, 0, dkw_dsw);
                water.set(0, 1, dkw_dso);

                if (degree >= 2){
                    double d2kw_dsw2 = 0.;
                    double d2kw_dswso = 0.;
                    double d2kw_dsosw = d2kw_dswso;
                    double d2kw_dso2 = 0.;

                    water.set(0, 0, 0, d2kw_dsw2);
                    water.set(0, 0, 1, d2kw_dswso);
                    water.set(0, 1, 0, d2kw_dsosw);
                    water.set(0, 1, 1, d2kw_dso2);
                }
            }
        }
    }
    else {
        if (degree >= 0){
            double kw = (lw_ + (1. - lw_)*pow(swcnw, expw_ - 1.))*swcnw/denkw_;
            water.set(0, kw);

            if (degree >= 1){
                double dkw_dsw = (lw_ + (1. - lw_)*expw_*pow(swcnw, expw_ - 1.))/denkw_;
                double dkw_dso = 0.;

                water.set(0, 0, dkw_dsw);
                water.set(0, 1, dkw_dso);

                if (degree >= 2){
                    double d2kw_dsw2 = (1. - lw_)*expw_*(expw_ - 1.)*pow(swcnw, expw_ - 2.)/denkw_;
                    double d2kw_dswso = 0.;
                    double d2kw_dsosw = d2kw_dswso;
                    double d2kw_dso2 = 0.;

                    water.set(0, 0, 0, d2kw_dsw2);
                    water.set(0, 0, 1, d2kw_dswso);
                    water.set(0, 1, 0, d2kw_dsosw);
                    water.set(0, 1, 1, d2kw_dso2);
                }
            }
        }
    }

    return degree;
}

// Expects that w.size() == 2.
//
int StonePermeability::PermeabilityOil_jet(const RealVector &state, int degree, JetMatrix &oil){
    double sw = state.component(0);
    double so = state.component(1);
    double sg = 1.0 - sw - so;

    double sow = 1. - sw - cno_;
    JetMatrix kowj(1);
    kowden_jet(sow, degree, kowj);

    double sog = 1. - sg - cno_;
    JetMatrix kogj(1);
    kogden_jet(sog, degree, kogj);

    double socno = so - cno_;

    if (socno < 0.){ 
        if (degree >= 0){
            double ko = 0.0;
            oil.set(0, ko);

            if (degree >= 1){
                double dko_dsw = 0.0;
                double dko_dso = 0.0;

                oil.set(0, 0, dko_dsw);
                oil.set(0, 1, dko_dso);

                if (degree >= 2){
                    double d2ko_dsw2 = 0.0;
                    double d2ko_dswso = 0.0;
                    double d2ko_dsosw = d2ko_dswso;
                    double d2ko_dso2 = 0.;

                    oil.set(0, 0, 0, d2ko_dsw2);
                    oil.set(0, 0, 1, d2ko_dswso);
                    oil.set(0, 1, 0, d2ko_dsosw);
                    oil.set(0, 1, 1, d2ko_dso2);
                }
            }
        }
    }
    else {
        if (degree >= 0){
            double kowden = kowj.get(0);
            double kogden = kogj.get(0);

            double ko = (socno*(1. - cno_)*kogden*kowden)*epsl_ + (1. - epsl_)*pow(socno, expo_);
            oil.set(0, ko);

            if (degree >= 1){
                double dkowden_dsw = kowj.get(0, 0);
                double dkogden_dsg = kogj.get(0, 0);

                double dko_dsw = (-dkogden_dsg*kowden + kogden*dkowden_dsw)*socno*(1. - cno_)*epsl_;
                double dko_dso = (-socno*dkogden_dsg + kogden)*(1. - cno_)*kowden*epsl_ + (1. - epsl_)*expo_*pow(socno, expo_ - 1.);

                oil.set(0, 0, dko_dsw);
                oil.set(0, 1, dko_dso);

                if (degree >= 2){
                    double d2kowden_dsw2 = kowj.get(0, 0, 0);
                    double d2kogden_dsg2 = kogj.get(0, 0, 0);

                    double d2ko_dsw2 = (d2kogden_dsg2*kowden + kogden*d2kowden_dsw2 - 2.*dkogden_dsg*dkowden_dsw)*socno*(1. - cno_)*epsl_;
                    double d2ko_dswso = ((d2kogden_dsg2*kowden - dkogden_dsg*dkowden_dsw)*socno + (-dkogden_dsg*kowden + kogden*dkowden_dsw))*(1. - cno_)*epsl_;
                    double d2ko_dsosw = d2ko_dswso;
                    double d2ko_dso2 = (-2.*dkogden_dsg + socno*d2kogden_dsg2)*(1. - cno_)*kowden*epsl_ + (1. - epsl_)*expo_*(expo_ - 1.)*pow(socno, expo_ - 2.);

                    oil.set(0, 0, 0, d2ko_dsw2);
                    oil.set(0, 0, 1, d2ko_dswso);
                    oil.set(0, 1, 0, d2ko_dsosw);
                    oil.set(0, 1, 1, d2ko_dso2);
                }
            }
        }
    }

    return degree;
}

// Expects that w.size() == 2.
//
int StonePermeability::PermeabilityGas_jet(const RealVector &state, int degree, JetMatrix &gas){
    double sw = state.component(0);
    double so = state.component(1);
    double sg = 1.0 - sw - so;

    double sgcng = sg - cng_;

    if (sgcng < 0.){ 
        if (degree >= 0){
            double kg = 0.;
            gas.set(0, kg);

            if (degree >= 1){
                double dkg_dsw = 0.;
                double dkg_dso = 0.;

                gas.set(0, 0, dkg_dsw);
                gas.set(0, 1, dkg_dso);

                if (degree >= 2){
                    double d2kg_dsw2 = 0.;
                    double d2kg_dswso = 0.;
                    double d2kg_dsosw = d2kg_dswso;
                    double d2kg_dso2 = 0.;

                    gas.set(0, 0, 0, d2kg_dsw2);
                    gas.set(0, 0, 1, d2kg_dswso);
                    gas.set(0, 1, 0, d2kg_dsosw);
                    gas.set(0, 1, 1, d2kg_dso2);
                }
            }
        }
    }
    else {
        if (degree >= 0){
            double kg = (lg_ + (1. - lg_)*pow(sgcng, expg_ - 1.))*sgcng/denkg_;
            gas.set(0, kg);

            if (degree >= 1){
                double dkg_dsw = -(lg_ + (1. - lg_)*expg_*pow(sgcng, expg_ - 1.))/denkg_;
                double dkg_dso = -(lg_ + (1. - lg_)*expg_*pow(sgcng, expg_ - 1.))/denkg_;

                gas.set(0, 0, dkg_dsw);
                gas.set(0, 1, dkg_dso);

                if (degree >= 2){
                    double d2kg_dsw2 = (1. - lg_)*expg_*(expg_ - 1.)*pow(sgcng, expg_ - 2.)/denkg_;
                    double d2kg_dswso = (1. - lg_)*expg_*(expg_ - 1.)*pow(sgcng, expg_ - 2.)/denkg_;
                    double d2kg_dsosw = d2kg_dswso;
                    double d2kg_dso2 = (1. - lg_)*expg_*(expg_ - 1.)*pow(sgcng, expg_ - 2.)/denkg_;

                    gas.set(0, 0, 0, d2kg_dsw2);
                    gas.set(0, 0, 1, d2kg_dswso);
                    gas.set(0, 1, 0, d2kg_dsosw);
                    gas.set(0, 1, 1, d2kg_dso2);
                }
            }
        }
    }

    return degree;
}

