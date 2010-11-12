/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) HugoniotCurve.h
 */

#ifndef _HugoniotCurve_H
#define _HugoniotCurve_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "HugoniotSegment.h"
#include <vector>
/*
 * ---------------------------------------------------------------
 * Definitions:
 */


class HugoniotCurve {

private:

    vector<HugoniotSegment> * segmentsList_;
    RealVector * xZero_;

public:


    HugoniotCurve (const RealVector &,const vector<HugoniotSegment> &);

    virtual ~HugoniotCurve();

    HugoniotCurve(const HugoniotCurve &);

    const vector<HugoniotSegment> & segments() const;
    const RealVector & getXZero()const;

};



#endif //! _HugoniotCurve_H
