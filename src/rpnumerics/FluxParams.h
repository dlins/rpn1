/* IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) FluxParams.h
 **/

#ifndef _FluxParams_H
#define	_FluxParams_H
#include "RealVector.h"

class FluxParams{
    public:
        
        FluxParams(const char *, const RealVector &, const int);
        
        
        //! Individual flux parameters  accessor
        const double  getElement(const int ) const ;
        //! Complete flux parameters  accessor
        const  RealVector & getParams() const ;
        //! Flux parameters  mutator
        void setParams(const double * params);
        //! Flux parameters mutator
        void setParams(const RealVector params);
        //! Flux parameters mutator
        void setParam(const int , const double );
        //! Physics ID accessor
        const char * getPhysicsID() const;
        //! Default flux parameters accessor
        virtual  FluxParams defaultParams();
        
        private:
            
            RealVector params_;
            
            char *physicsID_;
            
};


inline const RealVector & FluxParams::getParams(){ return params_;}

inline void FluxParams::setParams(const double * params){ params_= new RealVector(params);}

inline void FluxParams::setParams(const RealVector params){ params_=params;}

inline void FluxParams::setParam(const int index, const double value){params_(index)=value;}

inline double FluxParams::getElement(int index){ return params_(index);}

inline const char * FluxParams::getPhysicsID(){return physicsID_;}

#endif	/* _FluxParams_H */

