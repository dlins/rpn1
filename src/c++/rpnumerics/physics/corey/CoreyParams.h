/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) CoreyFluxParams.h
 */

#ifndef _CoreyFluxParams_H
#define _CoreyFluxParams_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */

#include "FluxParams.h"
#include "RealVector.h"
/*
 * ---------------------------------------------------------------
 * Definitions:
 */

class CoreyParams:public FluxParams {
    
private:
    
    double vel_;
    double muw_, muo_, mug_;
    double grw_, gro_, grg_;
    
    int index_; //TODO (?????)
    
    
    const static FluxParams & defaultParams();
    
    
    
public:
    
//    const static FluxParams & DEFAULT_FLUX_PARAMS;
    
    CoreyParams();
    
    CoreyParams(const RealVector & params, int index);
    
    double vel()const ;
    
    double muw()const ;
    
    double muo()const ;
    
    double mug()const ;
    
    double grw()const;
    
    double gro()const ;
    
    double grg()const ;
    
};

inline double CoreyParams::vel()const { return params().component(0); }

inline double CoreyParams::muw()const { return
        params().component(1); }

inline double CoreyParams::muo()const { return params().component(2); }

inline double CoreyParams::mug()const { return params().component(3); }

inline double CoreyParams::grw()const { return params().component(4); }

inline double CoreyParams::gro()const { return params().component(5); }

inline double CoreyParams::grg()const { return params().component(6); }

inline const FluxParams & CoreyParams::defaultParams()  {


    RealVector  paramsVector(7);
      
    paramsVector.component(0)=1;

    paramsVector.component(1)=(double)1/3;
    paramsVector.component(2)=(double)1/3;
    paramsVector.component(3)=(double)1/3;
    paramsVector.component(4)=0;
    paramsVector.component(5)=0;
    paramsVector.component(6)=0;
    
    
    CoreyParams * fluxParams = new CoreyParams(paramsVector, 0); // TODO Index zero ???
    
    return *fluxParams;
    
}

#endif //! _CoreyFluxParams_H
