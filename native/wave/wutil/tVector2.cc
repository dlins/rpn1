#include <iostream>
#include "Vector2.h"

int
main()
{
	Vector2 b;
	b(0) = 1.;
	b(1) = 2.;
	cerr << "b: " << b << endl;

	Matrix2 A;
	A(0,0) = 1.;
	A(0,1) = 1.;
	A(1,0) = 0.;
	A(1,1) = 1.;
	cerr << "A:\n" << A;
	cerr << "trace is:\n" << trace(A) << endl;
	cerr << "transpose is:\n" << transpose(A);
	cerr << "deviator is:\n" << deviator(A);

	FactorMatrix2 F(factor(A));
	cerr << "F:\n" << F;

	Vector2 x(solve(F, b));
	cerr << "x: " << x << endl;
	cerr << "A*x: " << A*x << endl;

	Vector2 evals;
	Matrix2 evects;
	A.eigen(evals, evects);
	cerr << "evals of A: " << evals << endl;
	cerr << "evects of A:\n" << evects;

	Matrix2 B;
	B(0,0) = 1.1;
	B(0,1) = 1.;
	B(1,0) = 0.;
	B(1,1) = 1.;
	cerr << "B:\n" << B;

	B.eigen(evals, evects);
	cerr << "evals of B: " << evals << endl;
	cerr << "evects of B:\n" << evects;

	return 0;
}
