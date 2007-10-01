#include "RK4BPMethod.h"
#include <vector>

ODESolution   RK4BPMethod::solve(const RealVector &initialPoint, int timeDirection){
    
    int dimension = initialPoint.size(), i=0;
//    double t =0; //unused;
//    double deltat= 0.1;
    
//    RealVector out(dimension);
    
//    RealVector in(dimension);
//
//    for (i=0;i < dimension;i++)    {
//
//        in(i)=initialPoint(i);
//
//    }
    
    
    cout << "Entrada x: " << initialPoint(0) << endl;
    
    cout << "Entrada y: " << initialPoint(1) << endl;
    
    ODESolution  ret;//  new ODESolution();
    
    vector<RealVector> resultList = ret.getCoords();
    
    
    while ( i < 100){ //Criterio de parada
        
//        rk4method_teste(profile_.getDimension(), 0, profile_.getDeltat(), in, in, profile_.getFunction());
        
        RealVector teste(dimension);
        
        teste(0)=initialPoint(0)+0.001*i;
        teste(1)=initialPoint(0)+0.001*i;
        
//        ret->addCoords(in);
        
//        ret->addCoords(teste);
        
        cout << "Saida x: " << teste(0) << endl;

        cout << "Saida y: " << teste(1) << endl;
        
        i++;
        
    }
    
    return ret;
    
}

RK4BPMethod::RK4BPMethod(const ODESolverProfile &profile):profile_(profile){}

//RK4BPMethod::RK4BPMethod(){}


RK4BPMethod::~RK4BPMethod(){}




//int RK4BPMethod::rk4method_teste(int n, double t, double deltat,  const RealVector &p, RealVector &out, RpFunction &function){
//    
//    double * input = new double[n];
//    double *output = new double [n];
//    
//    int i, returned ;
//    
//    for ( i=0;i < n;i++){
//        
//        input[i]=p(i);
//    }
//    
//    returned= rk4_teste(n, t, deltat, input, output, function);
//    
//    for (i=0;i < n;i++){
////        out.components()[i]=output[i];
//        
//        out(i)=output[i];
//    }
//    
//    delete(input);
//    delete(output);
//    
//    return  0;
//    
//    
//}

