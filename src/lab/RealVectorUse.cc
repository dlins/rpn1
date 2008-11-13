/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) real_vector_use.cc
 *
 * Example of RealVector use
 *
 **/

#include <iostream.h>
#include "Lab.h"

//#define DEBUG 1


double init_vector(int i);

// Dummy array initializator
double init_vector(int i) 
{
	double phi = 0.5;
	return i*i + phi;
}

RealVector real_vector_use(void)
{
// RealVector class extends Vector class  functionalities (both under "lib-rpn/wave/util"
// directory) and are used by RpFunction to calculate JetMatrix objects (F, DF and D2F values).
	
// Example A: use of RealVector without OO.
	int size_a = 5;
	double *a;
	RealVector rv_a(size_a);

	// For instance, RealVector object is implemented internally as an array of doubles.
	// Using a conversion operator Vector class method, it is possible to have full access to this array.
	a = rv_a; 

	// Now you just need to load values to this array
	for (int i=0; i<size_a; i++) {
		a[i] = init_vector(i);
	}

	// To have access to this array using and ReadlVector access method, use the '(int i)' operator.
#ifdef DEBUG
	for (int i=0; i<size_a; i++) {
		cout << "rv_a(" << i << ") = " << rv_a(i) << "\n";
	}
#endif

// Example B: use of RealVector with OO
	double *b;
	// Construct a RealVector object from an array of doubles
	RealVector rv_b(*a);
	int size_b = rv_a.size() + 3;

	// Change the array size
	rv_b.resize(size_b);

	// Have access to arrat components using the 'component(int i)' method
	for (int i=0; i < rv_b.size(); i++) {
		b = &rv_b.component(i);
		*b = init_vector(i);
	}

#ifdef DEBUG
	cout << "\n";
	for (int i=0; i < rv_b.size(); i++) {
		cout << "rv_b(" << i << ") = " << rv_b(i) << "\n";
	}
#endif

// Example C: a manual RealVector initialization
// This example can be use to create a RealVector that will be used to construct a FluxParam 
	double *c;
	RealVector rv_c(10);

	c = rv_c;

	c[0] = -1.0;
	c[1] = 0.0;
	c[2] = 1.0;
	c[3] = 0.0;
	c[4] = 0.0;

	c[5] = 1.0;
	c[6] = 1.0;
	c[7] = 0.0;
	c[8] = 0.0;
	c[9] = 0.0;

	return rv_c;
}
