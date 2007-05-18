#include <iostream>
#include "Vector4.h"

int
main()
{
	Vector4 b;
	cout << "Number of components: " << Vector4::n_comps() << endl;
	b(0) = -1.0;
	b(1) = 3.0;
	b(2) = 4.0;
	b(3) = 1.0;
	cerr << "b: " << b << endl;

	Matrix4 A;
	A(0,0) = 2.0;
	A(0,1) = 1.0;
	A(0,2) = 1.0;
	A(0,3) = 0.0;
	A(1,0) = -2.0;
	A(1,1) = 0.0;
	A(1,2) = -2.0;
	A(1,3) = -1.0;
	A(2,0) = 2.0;
	A(2,1) = 3.0;
	A(2,2) = 1.0;
	A(2,3) = -1.0;
	A(3,0) = 4.0;
	A(3,1) = 1.0;
	A(3,2) = 9.0;
	A(3,3) = 2.0;

	cerr << "A:\n" << A;
	cerr << "trace is:\n" << trace(A) << endl;
	cerr << "deviator is:\n" << deviator(A);
	cerr << "one norm is:\n" << matrix_one_norm(A) << endl;
	cerr << "transpose is:\n" << transpose(A);

	FactorMatrix4 F(factor(A));

	cerr << "F:\n" << F;

	Vector4	x(solve(F, b));

	cerr << "x: " << x << endl;
	cerr << "A*x: " << A*x << endl;

	return 0;
}
