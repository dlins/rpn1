#include "Bisection.h"
#include <iostream>

int function(const double &x, double &y, void *o, void *d){
    y = (x - 15.0)*(x + 15.0);
//    y = x;

    return BISECTION_FUNCTION_OK;
}

int main(){
    double a = 20.0;
    double b = -10.0;

    double c = 3.14159256;
    int info = Bisection::bisection_method(a, b, 1e-10, c, &function, 0, 0);

    std::cout << "Info = " << info << ", c = " << c << std::endl;

    return 0;
}

