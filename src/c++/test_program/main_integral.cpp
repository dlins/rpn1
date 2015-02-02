#include <iostream>
#include "GaussLegendreIntegral.h"

double f(double x){
    double n = 5.0;

    return pow(x, n);
}

RealVector fr(int*, const RealVector &p){
    RealVector r(1);
    r(0) = p(0);

    return r;
}


int main(){
    GaussLegendreIntegral gli;
//    std::cout.precision(32);
//    std::cout << gli.integrate(*f) << std::endl;
//    std::cout << gli.integrate(*f, 1.0, 3.0) << std::endl;

    RealVector apoint(1);
    apoint(0) = 1.0;

    RealVector bpoint(1);
    bpoint(0) = 2.0;

    std::cout << "apoint  = " << apoint << std::endl;
    std::cout << "bpoint  = " << bpoint << std::endl;
    std::cout << "Average = " << gli.average(&fr, (int*)0, apoint, bpoint) << std::endl;

    return 0;
}

