#include <math.h>
#include <iostream>
#include "trapfpe.h"

int
main(int argc, char **argv)
{
	trapfpe();

	double f = sqrt(-1.);
	cout << "sqrt(-1.) = " << f << "\n";
	return 0;
}
