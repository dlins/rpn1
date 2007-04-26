#include <iostream>
#include "Vector1.h"

int
main()
{
	Vector1 b;
	b(0) = 1.;
	cerr << "b: " << b << endl;

	Matrix1 A;
	A(0,0) = 2.;
	cerr << "A:\n" << A;
	cerr << "transpose is:\n" << transpose(A);
	cerr << "trace is:\n" << trace(A) << endl;
	cerr << "deviator is:\n" << deviator(A);

	FactorMatrix1 F(factor(A));
	cerr << "F:\n" << F;

	Vector1 x(solve(F, b));
	cerr << "x: " << x << endl;
	cerr << "A*x: " << A*x << endl;

	Vector1 evals;
	Matrix1 evects;
	A.eigen(evals, evects);
	cerr << "evals of A: " << evals << endl;
	cerr << "evects of A:\n" << evects;

	Matrix1 B;
	B(0,0) = 1.1;
	cerr << "B:\n" << B;

	B.eigen(evals, evects);
	cerr << "evals of B: " << evals << endl;
	cerr << "evects of B:\n" << evects;

	return 0;
}
