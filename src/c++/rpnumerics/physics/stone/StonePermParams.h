/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) StonePermParams.h
 */

#ifndef _StonePermParams_H
#define _StonePermParams_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */


/*
 * ---------------------------------------------------------------
 * Definitions:
 */


class StonePermParams {
    
private:

    
public:
    
    StonePermParams(double lw, double low, double lg, double log, double cnw, double cno, double cng, double epsl);
    StonePermParams();
    void reset();
    
};



#endif //! _StonePermParams_H
