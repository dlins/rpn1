/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) RpNumerics.h
 **/

#ifndef _RpNumerics_H
#define	_RpNumerics_H

#include "Physics.h"
#include "GridValuesFactory.h"



class RpNumerics {
    
    
private:
    
    static  Physics * physics_;
    static GridValuesFactory * gridValuesFactory_;
    
    static double sigma;
    
    
public:
    
    static  Physics & getPhysics();

    static GridValuesFactory & getGridFactory();
    
    static  const FluxFunction & getFlux();
    
    static  const AccumulationFunction & getAccumulation();
    
    static void setSigma(double );
    
    static double getSigma();
          
    static void setPhysics(const Physics &);
    
    static void clean();


    
};

inline Physics & RpNumerics::getPhysics(){return *physics_;}

inline GridValuesFactory & RpNumerics::getGridFactory(){return *gridValuesFactory_;}

inline  const FluxFunction & RpNumerics::getFlux() {return physics_->fluxFunction();}

inline  const AccumulationFunction & RpNumerics::getAccumulation() {return physics_->accumulation();}

inline void RpNumerics::setPhysics(const Physics & physics){
    delete physics_;
    physics_=physics.clone();
    delete gridValuesFactory_;
    gridValuesFactory_=new GridValuesFactory(physics_);
}

inline void RpNumerics::setSigma(double s){sigma=s;}

inline double RpNumerics::getSigma(){return sigma;}


#endif	/* _JNIDEFS_H */

