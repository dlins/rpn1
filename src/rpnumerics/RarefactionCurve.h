/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) RarefactionCurve.h
 */

#ifndef _RarefactionCurve_H
#define _RarefactionCurve_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */

#include <vector>
#include "RealVector.h"
/*
 * ---------------------------------------------------------------
 * Definitions:
 */


class RarefactionCurve {
    
private:
    
    vector <RealVector> coords_;
    
    
public :
    
    RarefactionCurve(vector<RealVector>);
    vector<RealVector> getPoints();
    
};

inline vector<RealVector> RarefactionCurve::getPoints(){return coords_;}

#endif //! _RarefactionCurve_H
