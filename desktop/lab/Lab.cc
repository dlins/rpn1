/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) Lab.cc
 **/

#include "Lab.h"

int main(int argc, char argv[])
{
	RealVector r_vector = real_vector_use();
	Quad2FluxParams q2f_params(r_vector);
	Quad2FluxFunction q2f_function(q2f_params);

	///////////////////////////////////////////
	// EXAMPLE A - Quad2
	cout << "----------------------------------------\n";
	cout << "EXAMPLE A - Quad2\n";
	cout << "----------------------------------------\n\n";
	RealVector rv_a(10);
	double *a = rv_a;
	double sigma = 2.41;
	a[0] = 1.0;
	a[1] = 0.5;
	cout << "Array a: " << a[0] << ", " << a[1] << "\n";
	QuadWaveState state_a(rv_a, sigma);

	JetMatrix a_jet(2);
	a_jet(0, 2.4);
	a_jet(1, 3.1);
	cout << "Jet a: " << a_jet(0) << ", " << a_jet(1) << "\n\n";

	q2f_function.jet(state_a, a_jet, 2);
	cout << "Jet-F:   " << a_jet(0)     << ", " << a_jet(1)     << "\n";
	cout << "Jet-DF:  " << a_jet(0,0)   << ", " << a_jet(0,1)   << ", " << a_jet(1,0)<< ", "   << a_jet(1,1)   << "\n";
	cout << "Jet-D2F: " << a_jet(0,0,0) << ", " << a_jet(0,0,1) << ", " << a_jet(0,1,0)<< ", " << a_jet(0,1,1) << ", "\
	                    << a_jet(1,0,0) << ", " << a_jet(1,0,1) << ", " << a_jet(1,1,0)<< ", " << a_jet(1,1,1) << "\n\n";


	///////////////////////////////////////////
	// EXAMPLE B - Generic C function
	cout << "----------------------------------------\n";
	cout << "EXAMPLE B - Generic C function\n";
	cout << "----------------------------------------\n\n";
	RealVector rv_b(10);
        double *b = rv_b;

	b[0] = -1.0; b[1] = 0.0;
	b[2] =  1.0; b[3] = 0.0;
	b[4] =  0.0; b[5] = 1.0;
	b[6] =  1.0; b[7] = 0.0;
	b[8] =  0.0; b[9] = 0.0;

	GenericFluxParams params(rv_b);
	GenericFluxFunction gff(params);
	JetMatrix b_jet(2);

	gff.jet(state_a, b_jet, 2);
	b = b_jet();

	for (int i=0; i < b_jet.n_comps()*7; i++)
	    cout << "b[" << i << "] = " << b[i] << "\n";

	cout << "\n";
	cout << "Jet-F:   " << b_jet(0)     << ", " << b_jet(1)     << "\n";
	cout << "Jet-DF:  " << b_jet(0,0)   << ", " << b_jet(0,1)   << ", " << b_jet(1,0)<< ", "   << b_jet(1,1)   << "\n";
	cout << "Jet-D2F: " << b_jet(0,0,0) << ", " << b_jet(0,0,1) << ", " << b_jet(0,1,0)<< ", " << b_jet(0,1,1) << ", "\
	                    << b_jet(1,0,0) << ", " << b_jet(1,0,1) << ", " << b_jet(1,1,0)<< ", " << b_jet(1,1,1) << "\n\n";

	///////////////////////////////////////////
	// EXAMPLE 3 - Generic (C++ Class)
	cout << "----------------------------------------\n";
	cout << "EXAMPLE 3 - Generic (C++ Class)\n";
	cout << "----------------------------------------\n\n";
	int dim = 2; int degree = 2;
	// RealVector for Flux Param
	RealVector rv_c(10); double *c = rv_c;
	// RealVector for Wave State
	RealVector rv_s(dim); double *s = rv_s;

	c[0] = -1.0; c[1] = 0.0;
	c[2] =  1.0; c[3] = 0.0;
	c[4] =  0.0; c[5] = 1.0;
	c[6] =  1.0; c[7] = 0.0;
	c[8] =  0.0; c[9] = 0.0;

	s[0] = 2.1; s[1] = 3.5;

	MyFluxParams my_params(rv_c);
	MyFluxFunction my_ff(my_params);
	MyWaveState state_c(rv_s);
	JetMatrix c_jet(dim);

	my_ff.jet(state_c, c_jet, degree);
	c = c_jet();

	cout << "\n";
	cout << "Jet-F:   " << c_jet(0)     << ", " << c_jet(1)     << "\n";
	cout << "Jet-DF:  " << c_jet(0,0)   << ", " << c_jet(0,1)   << ", " << c_jet(1,0)<< ", "   << c_jet(1,1)   << "\n";
	cout << "Jet-D2F: " << c_jet(0,0,0) << ", " << c_jet(0,0,1) << ", " << c_jet(0,1,0)<< ", " << c_jet(0,1,1) << ", "\
	                    << c_jet(1,0,0) << ", " << c_jet(1,0,1) << ", " << c_jet(1,1,0)<< ", " << c_jet(1,1,1) << "\n\n";


	cout << "===============================================\n";
	cout << "Jet: \n";
	for (int i=0; i<=c_jet.size(); i++ )
	cout <<  c[i]  << "\n";

	double *j;
	j = c_jet()+2;
	cout << "===============================================\n";
	cout << "Jet: \n";
	
	
	//int n = 2;
	//double Q[n*n], QQ[n][n];
	//cout <<  (double[2][2])j[0][0]  << "\n";
	// IT DOESN'T WORK
	//Q = *j;



	return 0;
}


