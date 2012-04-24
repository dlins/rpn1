/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) StonePermParams.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "StonePermParams.h"
#include <iostream>
using namespace std;

/*
 * ---------------------------------------------------------------
 * Definitions:
 */

StonePermParams::StonePermParams(double expw, double expg, double expo,
        double expow, double expog, double krw_p, double krg_p, double kro_p,
        double cnw, double cng, double cno,
        double lw, double lg, double lo,
        double low, double log,
        double epsl, double negWS, double negGS, double negOS) :comp(new RealVector(20)) {

    comp->component(0) = expw;
    comp->component(1) = expg;
    comp->component(2) = expo;

    comp->component(3) = expow;
    comp->component(4) = expog;


    comp->component(5) = krw_p;
    comp->component(6) = krg_p;
    comp->component(7) = kro_p;


    comp->component(8) = cnw;
    comp->component(9) = cng;
    comp->component(10) = cno;

    comp->component(11) = lw;
    comp->component(12) = lg;
    comp->component(13) = lo;


    comp->component(14) = low;
    comp->component(15) = log;

    comp->component(16) = epsl;

    comp->component(17) = negWS;
    comp->component(18) = negGS;
    comp->component(19) = negOS;

}

StonePermParams::StonePermParams(const RealVector & permVector) : comp(new RealVector(20)) {

    for (int i = 0; i < permVector.size(); i++) {
        comp->component(i) = permVector(i);
    }

}

StonePermParams::StonePermParams() : comp(new RealVector(20)) {
    reset();

}

StonePermParams::StonePermParams(const StonePermParams & copy) : comp(new RealVector(copy.params())) {
}

StonePermParams::~StonePermParams() {
    delete comp;
}

void StonePermParams::reset() {

    comp->component(0) = 2.0;
    comp->component(1) = 2.0;
    comp->component(2) = 2.0;

    comp->component(3) = 2.0;
    comp->component(4) = 2.0;

    comp->component(5) = 1.0;
    comp->component(6) = 1.0;
    comp->component(7) = 1.0;

    comp->component(8) = 0.0;
    comp->component(9) = 0.0;
    comp->component(10) = 0.0;

    comp->component(11) = 0.0;
    comp->component(12) = 0.0;
    comp->component(13) = 0.0;

    comp->component(14) = 0.0;
    comp->component(15) = 0.0;

    comp->component(16) = 0.0;

    comp->component(17) = 0.0;
    comp->component(18) = 0.0;
    comp->component(19) = 0.0;


}

double StonePermParams::component(int i) {
    return comp->component(i);
}

