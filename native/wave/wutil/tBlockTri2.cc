#include <iostream>
#include <math.h>
#include "BlockTri2.h"

int
main()
{
	const int n_eqn = 6;
	BlockTriMatrix2 M(n_eqn);
	BlockTriVector2 x(n_eqn), y(n_eqn), y_before(n_eqn), z(n_eqn);

	Matrix2 L, D, U;
	L(0,0) = -.4;
	L(1,0) = -.04;
	L(0,1) = -.04;
	L(1,1) = -.4;
	D(0,0) = 1.;
	D(1,0) = 0.;
	D(0,1) = 0.;
	D(1,1) = 1.;
	U(0,0) = -.4;
	U(1,0) = +.04;
	U(0,1) = +.04;
	U(1,1) = -.4;
	for (int i = 0; i < n_eqn; i++) {
		M.lower_diagonal()(i) = L;
		M.diagonal()(i) = D;
		M.upper_diagonal()(i) = U;
		double x = 2.*M_PI*i/(double)(n_eqn);
		y(i)(0) = sin(x);
		y(i)(1) = cos(x);
	}
	y_before = y;
	cerr << "M before:\n";
	cerr << "lower:\n" << M.lower_diagonal();
	cerr << "diag:\n" << M.diagonal();
	cerr << "upper:\n" << M.upper_diagonal();
	cerr << "y before:\n" << y_before;

	solve(M, x, y);

	z(0) = D*x(0) + U*x(1);
	for (int i = 1; i < n_eqn-1; i++)
		z(i) = L*x(i-1) + D*x(i) + U*x(i+1);
	z(n_eqn-1) = L*x(n_eqn-2) + D*x(n_eqn-1);

	cerr << "M after:\n";
	cerr << "lower:\n" << M.lower_diagonal();
	cerr << "diag:\n" << M.diagonal();
	cerr << "upper:\n" << M.upper_diagonal();
	cerr << "y after:\n" << y;

	cerr << "x:\n" << x;
	cerr << "M*x:\n" << z;
	cerr << "M*x - y_before:\n" << z - y_before;

	return 0;
}
