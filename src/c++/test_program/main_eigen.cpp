#include <iostream>
#include "Eigenproblem2x2.h"

int main(){
    Eigenproblem2x2 ep;

    double a[4] = {1.0, -2.0, 2.0, 4.0};
    DoubleMatrix A(2, 2, a);

    std::vector<Eigenvalue> evs;
    ep.find_eigenvalues(A, evs);

    for (int i = 0; i < evs.size(); i++) std::cout << "i = " << i << ", is real = " << evs[i].is_real << ", lambda = " << evs[i].real << " + i*" << evs[i].imaginary << std::endl;

    return 0;
}

