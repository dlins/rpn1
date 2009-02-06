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
#include "FluxFunction.h"

#include "RarefactionFlow.h"
#include "ShockFlow.h"

#include "RarefactionMethod.h"

#include "ODESolver.h"

class RpNumerics {
    
    
private:
    
    static  Physics * physics_;
    
    static const ShockFlow * shockFlow_;
        
    static const ODESolver * odeSolver_;
    
    static double sigma;
    
    
public:
    
    static  Physics & getPhysics();
    
    static  const FluxFunction & getFlux();
    
    static  const AccumulationFunction & getAccumulation();
    
    static const ODESolver & getODESolver();
    
    static  void initODESolver();
    
    static void setSigma(double );
    
    static double getSigma();
          
    static void setPhysics(const Physics &);
    
    static void clean();
    
};

inline Physics & RpNumerics::getPhysics(){return *physics_;}

inline  const FluxFunction & RpNumerics::getFlux() {return physics_->fluxFunction();}

inline  const AccumulationFunction & RpNumerics::getAccumulation() {return physics_->accumulation();}

inline void RpNumerics::setPhysics(const Physics & physics){
    delete physics_;
    physics_=physics.clone();
}

inline void RpNumerics::setSigma(double s){sigma=s;}

inline double RpNumerics::getSigma(){return sigma;}

inline const ODESolver & RpNumerics::getODESolver(){return *odeSolver_;}



#endif	/* _JNIDEFS_H */

