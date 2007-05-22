#include  "RK4BPMethod.h"



ODESolution RK4BPMethod::solve(const RealVector &initialPoint, int timeDirection){
    
    
    return ODESolution();
}


ODESolverProfile RK4BPMethod::getProfile(){return profile_;}

RK4BPMethod::RK4BPMethod(const ODESolverProfile &profile):profile_(profile){}



int RK4BPMethod::rk4(int n, double t, double deltat, const RealVector &p,  RealVector &out, int (*f)(int, double, double*, double*)){
    
    double * input = new double[n];
    double *output = new double [n];
    
    int i,returned ;
    
    for (i=0;i < n;i++){
        
        input[i]=p(i);
    }
    
    returned= RK4(n,t,deltat,input,output,f);

        for (i=0;i < n;i++){
            out.components()[i]=output[i];    
    }

    free(input);
    free(output);
    
    return returned;
    
}
