/* IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) AccumulationParams.h
 **/

#ifndef _AccumulationParams_H
#define	_AccumulationParams_H
#include "RealVector.h"

class AccumulationParams{
    public:
        
        AccumulationParams(const char *, const RealVector &, const int);
        
        
        //! Individual Accumulation parameters  accessor
        const double  getElement(const int ) const ;
        //! Complete Accumulation parameters  accessor
        const  RealVector & getParams() const ;
        //! Accumulation parameters  mutator
        void setParams(const double * params);
        //! Accumulation parameters mutator
        void setParams(const RealVector params);
        //! Accumulation parameters mutator
        void setParam(const int , const double );
        //! Physics ID accessor
        const char * getPhysicsID() const;
        //! Default Accumulation parameters accessor
        virtual  AccumulationParams defaultParams();
        
        private:
            
            RealVector params_;
};


inline const RealVector & AccumulationParams::getParams(){ return params_;}

inline void AccumulationParams::setParams(const double * params){ params_= new RealVector(params);}

inline void AccumulationParams::setParams(const RealVector params){ params_=params;}

inline void AccumulationParams::setParam(const int index, const double value){params_(index)=value;}

inline double AccumulationParams::getElement(int index){ return params_(index);}


#endif	/* _AccumulationParams_H */

