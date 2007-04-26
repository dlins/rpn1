#include <iostream.h>
#include <math.h>
#include <stdio.h>
#include "BlockTriInf2.h"

#ifndef M_PI
#define M_PI 3.141592653
#endif

int
main()
{
    const int n_eqn = 20;
    BlockTriInfMatrix2 M(n_eqn);
    BlockTriInfVector2 x(n_eqn), y(n_eqn), y_before(n_eqn), z(n_eqn);

    Matrix2 LL, L, D;

    for (int i = 0; i < n_eqn; i++) {

	i++;

	LL(0,0) = i-1;
        LL(1,0) = -1;
        LL(0,1) = i+1;
        LL(1,1) = i;
#define TEST
#ifdef TEST
        LL(0,0) = 0;
        LL(1,0) = 0;
        LL(0,1) = 0;
        LL(1,1) = 0;
#endif

        L(0,0) = i+1;
        L(1,0) = i;
        L(0,1) = i-1;
        L(1,1) = -i;

	D(0,0) = -100;
	D(1,0) = 10;
	D(0,1) = 10;
	D(1,1) = 100;

	i--;

        M.lower_lower_diagonal()(i) = LL;
        M.lower_diagonal()(i) = L;
        M.diagonal()(i) = D;
        y(i)(0) = i;
        y(i)(1) = i+1;
    }

    y_before = y;
    cerr << "M before:\n";
    cerr << "lower_lower:\n" << M.lower_lower_diagonal();
    cerr << "lower:\n" << M.lower_diagonal();
    cerr << "diag:\n" << M.diagonal();
    cerr << "y before:\n" << y_before;

    solve(M, x, y);

    // Compute z = M*x, which should equal y.
    z(0) = M.diagonal()(0)*x(0);
    z(1) = M.lower_diagonal()(0)*x(0) + M.diagonal()(1)*x(1);
    for (int i = 2; i < n_eqn; i++)
       z(i) = M.lower_lower_diagonal()(i-1)*x(i-2)
           + M.lower_diagonal()(i-1)*x(i-1) + M.diagonal()(i)*x(i);

    cerr << "M after:\n";
    cerr << "lower_lower:\n" << M.lower_lower_diagonal();
    cerr << "lower:\n" << M.lower_diagonal();
    cerr << "diag:\n" << M.diagonal();
    cerr << "y after:\n" << y;

    cerr << "x:\n" << x;
    cerr << "M*x:\n" << z;
    cerr << "M*x - y_before:\n" << z - y_before;

    return 0;
}
