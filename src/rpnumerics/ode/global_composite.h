#ifndef GLOBAL_COMPOSITE_H
#define GLOBAL_COMPOSITE_H

// Maximum number of rows and columns of the matrix being considered, 
// and consequently of the number of its eigenvalues
#define EIG_MAX 2      

// Epsilon value, minimum threshold. 
// Any number x such that abs(x) < EPS will be considered 
// equal to zero in certain circumstances
#define EPS 1e-30      

// Maximum number of points in the orbit
#define PNT_MAX 10000  

// Functions that successfully carry on their task will return SUCCESSFUL_PROCEDURE,
// since most of the results are not floats or ints, but vectors and as such must
// be returned by means of pointers (which are passed in the parameters).
//
// Functions that encounter some kind of problem will return ABORTED_PROCEDURE.
// In this case the results of the functions should be disregarded, i.e., 
// a function invoking another function MUST check wether the invoked function
// could or could not successfully perform its task.
//
#define SUCCESSFUL_PROCEDURE 0                    
#define ABORTED_PROCEDURE !SUCCESSFUL_PROCEDURE

// Last viable eigenvalue
double lasteigenvalue;

// Reference speed (See the identity in Proposition 10.11 of 
// "An Introduction to Conservation Laws: 
// Theory and Applications to Multi-Phase Flow" (monotonicity
// of the rarefaction).
double ref_speed;

// Reference vector whose inner product with the eigenvector
// must be positive.
double re[EIG_MAX];

// Index of the family
int index_eigen;

// xi and Delta xi
double xi, deltaxi;

#endif // GLOBAL_COMPOSITE_H
