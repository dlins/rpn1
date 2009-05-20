/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) RPnCurve.h
 */

#ifndef _RPnCurve_H
#define _RPnCurve_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "RealVector.h"
#include <vector>

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


class RPnCurve {

private:
 vector <RealVector> resultList_;
    
public:
    

    RPnCurve (vector<RealVector>);
    RPnCurve & operator=(const RPnCurve &);
    void add(const RealVector &);
    vector<RealVector> getCoords() const;
    

};

#endif //! _RPnCurve_H
