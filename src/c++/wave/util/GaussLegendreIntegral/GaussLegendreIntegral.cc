#include "GaussLegendreIntegral.h"

GaussLegendreIntegral::GaussLegendreIntegral(){
    position.resize(5);
    position[0] = -(1.0/3.0)*sqrt(5.0 + 2.0*sqrt(10.0/7.0));
    position[1] = -(1.0/3.0)*sqrt(5.0 - 2.0*sqrt(10.0/7.0));
    position[2] = 0.0;
    position[3] = -position[1];
    position[4] = -position[0];

    weight.resize(5);
    weight[0] = (322.0 - 13.0*sqrt(70.0))/900.0;
    weight[1] = (322.0 + 13.0*sqrt(70.0))/900.0;
    weight[2] = 128/225.0;
    weight[3] = weight[1];
    weight[4] = weight[0];
}

GaussLegendreIntegral::~GaussLegendreIntegral(){
}

double GaussLegendreIntegral::integrate(double (*f)(double)){
    double sum = 0.0;

    for (int i = 0; i < weight.size(); i++) sum += weight[i]*(    (*f)(position[i])    );

    return sum;
}

double GaussLegendreIntegral::integrate(double (*f)(double), double a, double b){
    return average(*f, a, b)*(b - a);
}

double GaussLegendreIntegral::average(double (*f)(double), double a, double b){
    double sum = 0.0;
    double w = (b - a)*.5;
    double s = (b + a)*.5;

    for (int i = 0; i < weight.size(); i++) sum += weight[i]*(    (*f)(position[i]*w + s)    );

    return sum*.5;
}


// With object-passing.
//
double GaussLegendreIntegral::integrate(double (*f)(int*, double), int *obj){
    double sum = 0.0;

    for (int i = 0; i < weight.size(); i++) sum += weight[i]*(    (*f)(obj, position[i])    );

    return sum;
}

double GaussLegendreIntegral::integrate(double (*f)(int*, double), int *obj, double a, double b){
    return average(*f, obj, a, b)*(b - a);
}

double GaussLegendreIntegral::average(double (*f)(int*, double), int *obj, double a, double b){
    double sum = 0.0;
    double w = (b - a)*.5;
    double s = (b + a)*.5;

    for (int i = 0; i < weight.size(); i++) sum += weight[i]*(    (*f)(obj, position[i]*w + s)    );

    return sum*.5;
}

RealVector GaussLegendreIntegral::average(RealVector (*f)(int*, const RealVector&), int *obj, 
                                          const RealVector &apoint, 
                                          const RealVector &bpoint){

    RealVector w = (bpoint - apoint)*.5;
    RealVector s = (bpoint + apoint)*.5;

    // To initialize sum.
    //
    RealVector sum = weight[0]*(    (*f)(obj, position[0]*w + s)    );

    for (int i = 1; i < weight.size(); i++){
        RealVector p = (position[i]*w + s);

        sum += weight[i]*(    (*f)(obj, p)    );
    }

    return sum*.5;
}

