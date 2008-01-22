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
    
    static const Physics * physics_;
    
    static const ShockFlow * shockFlow_;
    
    static const RarefactionFlow * rarefactionFlow_;
    
    static const RarefactionMethod * rarefactionMethod_;
    
    static const ODESolver * odeSolver_;
    
    static double sigma;
    
    static int familyIndex_;
    
public:
    
    static const Physics * getPhysics();
    
    static const FluxFunction * getFlux();
    
    static const ShockFlow * getShockFlow();
    
    static const   RarefactionFlow * getRarefactionFlow();
    
    static void setRarefactionFlow(const RarefactionFlow &);
    
    static const ODESolver * getODESolver();
    
    static void setRarefactionMethod(const RarefactionMethod &);
    
    static const RarefactionMethod * getRarefactionMethod(const char *);
    
    static  void initODESolver();
    
    static void setSigma(double );
    
    static double getSigma();
    
    static void setFamilyIndex(int);
    
    static int getFamilyIndex();
    
    static void setPhysics(const Physics &);
    
    static void setShockFlow(const ShockFlow &);
    
    static void clean();
    
};

inline void RpNumerics::setRarefactionMethod(const RarefactionMethod & rarefactionMethod){
    delete rarefactionMethod_;
    rarefactionMethod_= rarefactionMethod.clone();
}

inline  const RarefactionMethod * RpNumerics::getRarefactionMethod(const char * methodName) {
    
    if (!strcmp(methodName,"ContinuationRarefactionMethod")){
        
    }
    return rarefactionMethod_;
}

inline void RpNumerics::setFamilyIndex(int familyIndex){familyIndex_=familyIndex;}

inline int RpNumerics::getFamilyIndex(){return familyIndex_;}

inline const RarefactionFlow * RpNumerics::getRarefactionFlow(){return rarefactionFlow_;}

inline void RpNumerics::setRarefactionFlow(const RarefactionFlow & rarefactionFlow){
    delete rarefactionFlow_;
    rarefactionFlow_= new RarefactionFlow(rarefactionFlow);}

inline const Physics * RpNumerics::getPhysics(){return physics_;}

inline const  FluxFunction * RpNumerics::getFlux() {return physics_->fluxFunction();}

inline const ShockFlow *  RpNumerics::getShockFlow() {return shockFlow_;}

inline void RpNumerics::setPhysics(const Physics & physics){
    delete physics_;
    physics_=physics.clone();
}

inline void RpNumerics::setSigma(double s){sigma=s;}

inline double RpNumerics::getSigma(){return sigma;}

inline void RpNumerics::setShockFlow(const ShockFlow &flow) {
    delete shockFlow_;
    shockFlow_ = (ShockFlow *)flow.clone();
}

inline const ODESolver * RpNumerics::getODESolver(){return odeSolver_;}



#endif	/* _JNIDEFS_H */

