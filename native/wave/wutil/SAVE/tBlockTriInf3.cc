#include <iostream.h>
#include <math.h>
#include <stdio.h>
#include "BlockTriInf3.h"

#ifndef M_PI
#define M_PI 3.141592653
#endif

int
main()
{
    const int n_eqn = 5;
    BlockTriInfMatrix3 M(n_eqn);
    BlockTriInfVector3 x(n_eqn), y(n_eqn), y_before(n_eqn), z(n_eqn);

    Matrix3 LL, L, D;

    for (int i = 0; i < n_eqn; i++) {

	i++;

	LL(0,0) = i-1;
        LL(1,0) = -1;
        LL(2,0) = -1;
        LL(0,1) = i+1;
        LL(1,1) = i+3;
        LL(2,1) = i-2;
        LL(0,2) = i+1;
        LL(1,2) = 2*i;
        LL(2,2) = i-2;

        L(0,0) = i+1;
        L(1,0) = i;
        L(2,0) = i+2;
        L(0,1) = i-1;
        L(1,1) = -i;
        L(2,1) = -i+5;
        L(0,2) = i+10;
        L(1,2) = i/2;
        L(2,2) = i-20;

        D(0,0) = i-4;
	D(1,0) = +.04;
	D(2,0) = +.44;
	D(0,1) = +.04;
	D(1,1) = -.4;
	D(2,1) = -.24;
	D(0,2) = i+5;
	D(1,2) = i/4;
	D(2,2) = i;

	i--;

        M.lower_lower_diagonal()(i) = LL;
        M.lower_diagonal()(i) = L;
        M.diagonal()(i) = D;
        y(i)(0) = i;
        y(i)(1) = i+1;
        y(i)(2) = i+2;
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
