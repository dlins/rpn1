#include "ODESigmaSolver.h"
#include "ODESigmaSolution.h"



ODESigmaSolver::ODESigmaSolver(const VectorField & vf, const ODESolverProfile &profile ):vf_(vf), RK4BPMethod(profile){}





ODESolution  ODESigmaSolver::solve(const RealVector & initialPoint, int times){
    
    bool stop=false;
    int steps=0;
    RealVector next, current;
    
    double * sigma = new double[getProfile().getStopEvaluator().getMaxSteps()];
    double * timesArray = new double[getProfile().getStopEvaluator().getMaxSteps()];
    RealVector * result = new RealVector[getProfile().getStopEvaluator().getMaxSteps()];

    
    while(getProfile().getStopEvaluator().getMaxSteps()<steps && !stop){
        
        rk4(initialPoint.size(), 0, getProfile().getDeltat(), current, next, getVectorField().getFunction());
        
        sigma[steps]= getVectorField().sigmaCalc(next);
        
        result[steps]=next;
        stop=getProfile().getStopEvaluator().check(next);
        current=next;
        steps++;
        
    }
    
    return  ODESigmaSolution(result,sigma,timesArray);
    
    
}
ODESolverProfile ODESigmaSolver::getProfile(){return profile_;}
VectorField ODESigmaSolver::getVectorField(){return vf_;}



