/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) ODEStopGenerator.h
 */

#ifndef _ODEStopGenerator_H
#define _ODEStopGenerator_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */


/*
 * ---------------------------------------------------------------
 * Definitions:
 */


class ODEStopGenerator {
    
    
public:
    virtual ~ODEStopGenerator();
    
    virtual ODEStopGenerator * clone() const =0;
    
    virtual bool getStatus() const =0;
    

    
};

#endif //! _ODEStopGenerator_H
