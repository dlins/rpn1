/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) Permeability.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "Permeability.h"
#include <iostream>

using namespace std;
/*
 * ---------------------------------------------------------------
 * Definitions:
 */


Permeability::Permeability(const PermParams & params):params_(new PermParams(params)) {

    denkw_ = (params_->lw() + (1. - params_->lw()) * (1. - params_->cnw())) * (1. - params_->cnw());
    denkg_ = (params_->lg() + (1. - params_->lg()) * (1. - params_->cng())) * (1. - params_->cng());
    denkow_ = (params_->low() + (1. - params_->low()) * (1. - params_->cno())) * (1. - params_->cno());
    denkog_ = (params_->log() + (1. - params_->log()) * (1. - params_->cno())) * (1. - params_->cno());
}


Permeability::Permeability(const Permeability & copy){
    
    params_=new PermParams(copy.params());
    
}

double Permeability::kw(double sw, double so, double sg)const {
    double swcnw = sw - params_->cnw();
    if (swcnw <= 0.)
        return 0.;
    return (params_->lw() + (1. - params_->lw()) * swcnw) * swcnw / denkw_;
}

double Permeability::kowden(double sw, double so, double sg)const {
    double sow = 1. - sw - params_->cno();
    if (sow <= 0.0)
        return 0.0;
    return (params_->low() + (1. - params_->low()) * sow) / denkow_;
}


double Permeability::kogden(double sw, double so, double sg)const {
    double sog = 1. - sg - params_->cno();
    if (sog <= 0.0)
        return 0.0;
    return (params_->log() + (1. - params_->log()) * sog) / denkog_;
}


double Permeability::ko(double sw, double so, double sg)const {
    double socno = so - params_->cno();
    if (socno <= 0.)
        return 0.;
    return (params_->epsl() * (1. - params_->cno()) * kogden(sw, so, sg) * kowden(sw, so, sg) +
            (1. - params_->epsl()) * socno) * socno;
}




double Permeability::kg(double sw, double so, double sg)const {
    double sgcng = sg - params_->cng();
    if (sgcng <= 0.)
        return 0.;
    return (params_->lg() + (1. - params_->lg()) * sgcng) * sgcng / denkg_;
}

double Permeability::dkwdso(double sw, double so, double sg)const {
    return 0.;
}

double Permeability::dkwdsw(double sw, double so, double sg)const {
    double swcnw = sw - params_->cnw();
    if (swcnw <= 0.)
        return 0.;
    return (params_->lw() + 2. * (1. - params_->lw()) * swcnw) / denkw_;
}

// computes the derivative of kow divided by den = 1. - sw + params_->cnw() relative to sw
double Permeability::dkowdendsw(double sw, double so, double sg)const {
    double sow = 1. - sw - params_->cno();
    if (sow <= 0.0)
        return 0.0;
    return (-1. + params_->low()) / denkow_;
}

double Permeability::dkogdendsg(double sw, double so, double sg)const {
    double sog = 1. - sw - params_->cno();
    if (sog <= 0.0)
        return 0.0;
    return (-1. + params_->log()) / denkog_;
}

double Permeability::dkodsw(double sw, double so, double sg)const {
    double socno = so - params_->cno();
    if (socno <= 0.)
        return 0.;
    return (-dkogdendsg(sw, so, sg) * kowden(sw, so, sg) + kogden(sw, so, sg) * dkowdendsw(sw, so, sg)) * socno *
            (1. - params_->cno()) * params_->epsl();
}

double Permeability::dkodso(double sw, double so, double sg)const {
    double socno = so - params_->cno();
    if (socno <= 0.)
        return 0.;
    return (-dkogdendsg(sw, so, sg) * socno + kogden(sw, so, sg)) * kowden(sw, so, sg) * (1. - params_->cno()) *
            params_->epsl() + 2. * (1. - params_->epsl()) * socno;
}

double Permeability::dkgdsw(double sw, double so, double sg)const {
    double sgcng = sg - params_->cng();
    if (sgcng <= 0.)
        return 0.;
    return -(params_->lg() + 2. * (1. - params_->lg()) * sgcng) / denkg_;
}

double Permeability::dkgdso(double sw, double so, double sg)const {
    double sgcng = sg - params_->cng();
    if (sgcng <= 0.)
        return 0.;
    return -(params_->lg() + 2. * (1. - params_->lg()) * sgcng) / denkg_;
}

double Permeability::dkgdsg(double sw, double so, double sg)const {
    double sgcng = sg - params_->cng();
    if (sgcng <= 0.)
        return 0.;
    return (params_->lg() + 2. * (1. - params_->lg()) * sgcng) / denkg_;
}

double Permeability::dkwdww(double sw, double so, double sg)const {
    if (sw <= params_->cnw())
        return 0.;
    return 2. * (1. - params_->lw()) / denkw_;
}

double Permeability::dkwdwo(double sw, double so, double sg)const {
    return 0.;
}

double Permeability::dkwdoo(double sw, double so, double sg)const {
    return 0.;
}

// computes the second derivative of kow divided by
// den = 1. - sw + params_->cnw() relative to sw
double Permeability::dkowdendww(double sw, double so, double sg)const {
    return 0.0;
}

// computes the second derivative of kog divided by
// den = 1. - sg + params_->cng() relative to sg
double Permeability::dkogdendgg(double sw, double so, double sg)const {
    return 0.0;
}

// computes the second derivative of ko relative to sw
double Permeability::dkodww(double sw, double so, double sg)const {
    double socno = so - params_->cno();
    if (socno <= 0.)
        return 0.;
    return (dkogdendgg(sw, so, sg) * kowden(sw, so, sg) - 2. * dkogdendsg(sw, so, sg) * dkowdendsw(sw, so, sg) +
            kogden(sw, so, sg) * dkowdendww(sw, so, sg)) * socno * (1. - params_->cno()) * params_->epsl();
}

// computes the mixed second derivative of ko relative to sw and so
double Permeability::dkodwo(double sw, double so, double sg)const {
    double socno = so - params_->cno();
    if (socno <= 0.)
        return 0.;
    double dkowdendsw_ = dkowdendsw(sw, so, sg);
    double kowden_ = kowden(sw, so, sg);
    double dkogdendsg_ = dkogdendsg(sw, so, sg);
    return (-dkogdendsg_ * kowden_ + kogden(sw, so, sg) * dkowdendsw_) + socno *
            (dkogdendgg(sw, so, sg) * kowden_ - dkogdendsg_ * dkowdendsw_) * (1. - params_->cno()) * params_->epsl();
}

// computes the second derivative of ko relative to so
double Permeability::dkodoo(double sw, double so, double sg)const {
    double socno = so - params_->cno();
    if (socno <= 0.)
        return 0.;
    return (-2. * dkogdendsg(sw, so, sg) + socno * dkogdendgg(sw, so, sg)) * kowden(sw, so, sg) * (1. - params_->cno()) *
            params_->epsl() + 2. * (1. - params_->epsl());
}

double Permeability::dkgdww(double sw, double so, double sg)const {
    if (sg <= params_->cng())
        return 0.;
    return 2. * (1. - params_->lg()) / denkg_;
}

double Permeability::dkgdwo(double sw, double so, double sg)const {
    if (sg <= params_->cng())
        return 0.;
    return 2. * (1. - params_->lg()) / denkg_;
}

double Permeability::dkgdoo(double sw, double so, double sg)const {
    if (sg <= params_->cng())
        return 0.;
    return 2. * (1. - params_->lg()) / denkg_;
}
Permeability::~Permeability(){delete params_;}


//! Code comes here! daniel@impa.br

