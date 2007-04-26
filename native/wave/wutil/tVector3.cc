#include <iostream>
#include "Vector3.h"

int
main()
{
	Vector3 b;
	b(0) = 1.;
	b(1) = 2.;
	b(2) = 3.;
	cerr << "b: " << b << endl;

	Matrix3 A;
	A(0,0) = 1.;
	A(0,1) = 2.;
	A(0,2) = 3.;
	A(1,0) = 0.;
	A(1,1) = 1.;
	A(1,2) = 4.;
	A(2,0) = 0.;
	A(2,1) = 0.;
	A(2,2) = 1.;
	cerr << "A:\n" << A;
	cerr << "trace is:\n" << trace(A) << endl;
	cerr << "transpose is:\n" << transpose(A);
	cerr << "deviator is:\n" << deviator(A);

	FactorMatrix3 F(factor(A));
	cerr << "F:\n" << F;

	Vector3 x(solve(F, b));
	cerr << "x: " << x << endl;
	cerr << "A*x: " << A*x << endl;

	return 0;
}
