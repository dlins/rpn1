#include "GenericFluxCalc.h"

int calc_jet(int size, double *v) 
{

	for (int i=0; i<size; i++) {
		v[i] = i*i;
	}

	return 0;
}
