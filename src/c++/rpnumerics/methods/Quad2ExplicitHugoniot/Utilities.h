#ifndef _UTILITIES_
#define _UTILITIES_

#include <iostream>
#include <vector>
#include <math.h>

// For Utilities::quadratic_equation
#ifndef COMPLEX_ROOTS
#define COMPLEX_ROOTS 0
#endif

#ifndef REAL_ROOTS
#define REAL_ROOTS 1
#endif
// For Utilities::quadratic_equation

// For Utilities::solve1
#ifndef SOLVE1_NO_ZERO
#define SOLVE1_NO_ZERO (-1)
#endif

#ifndef SOLVE1_NO_CONVERGENCE
#define SOLVE1_NO_CONVERGENCE 0
#endif

#ifndef SOLVE1_OK
#define SOLVE1_OK 1
#endif
// For Utilities::solve1

class Utilities {
    private:
    protected:
        static double cubic_polynomial(double m, double aa, double bb, double cc);

        static int solve1(double &xn, double xa, double xb,
                          double aa, double bb, double cc,
                          double tol);

    public:
        static int quadratic_equation(double a, double b, double &x1, double &x2);
        static int cubic_equation(std::vector<double> &x, double a, double b, double c);
};

#endif // _UTILITIES_

