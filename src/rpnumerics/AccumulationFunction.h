/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) AccumulationFunction.h
 **/
#ifndef _AccumulationFunction_H
#define	_AccumulationFunction_H

//! Definition of class AccumulationFunction.
/*!


 TODO:
 NOTE :

 @ingroup rpnumerics
 */


class AccumulationFunction:public RpFuntionDeriv2{
    
    public:
        
        
        //! Accumulation params accessor
        
        const  AccumulationParams & accumulationParams() const  = 0;
        
        
        private:

            AccumulationParams params_;
        
};


inline const AccumulationParams AccumulationFunction::accumulationParams(){return params_;}


#endif	/* _AccumulationFunction_H */

