#include <stdio.h>
#include "polyroot.h"

int
main(int /* argc */, char ** /* argv */)
{
	int n;
	double x[3];

	n = cubic_roots(0., 0., 1., x);
	printf("n = %d:  roots = %g, %g, %g\n", n, x[0], x[1], x[2]);
	n = cubic_roots(0., 0., 0., x);
	printf("n = %d:  roots = %g, %g, %g\n", n, x[0], x[1], x[2]);
	n = cubic_roots(-2., 1., 0., x);
	printf("n = %d:  roots = %g, %g, %g\n", n, x[0], x[1], x[2]);
	n = cubic_roots(2., 1., 0., x);
	printf("n = %d:  roots = %g, %g, %g\n", n, x[0], x[1], x[2]);
	n = cubic_roots(-3., 2., 0., x);
	printf("n = %d:  roots = %g, %g, %g\n", n, x[0], x[1], x[2]);

	return 0;
}
