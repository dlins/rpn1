/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) StationaryPoint.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "StationaryPoint.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


StationaryPoint::StationaryPoint(const RealVector & coords, std::vector<eigenpair> &ep) : coords_(new RealVector(coords)) {

//    double r; // Real part of the eigenvalue
//    double i; // Imaginary part of the eigenvalue
//
//    vector<double> vlr; // Real part of the left-eigenvector
//    vector<double> vli; // Imaginary part of the left-eigenvector
//    vector<double> vrr; // Real part of the right-eigenvector
//    vector<double> vri; // Imaginary part of the right-eigenvector
//


    for (int i = 0; i < ep.size(); i++) {

        eigenpair e;

        e.r = ep[i].r;
        e.i = ep[i].i;
        e.vlr = ep[i].vlr;
        e.vli = ep[i].vli;
        e.vrr = ep[i].vrr;
        e.vri = ep[i].vri;

        epVector_.push_back(e);

    }
}



std::ostream & StationaryPoint::operator<<(std::ostream &out, const StationaryPoint &r) {

    out<<"teste"<<endl;

    return out;
}





StationaryPoint::~StationaryPoint() {
    delete coords_;
}