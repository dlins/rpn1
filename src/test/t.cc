#include <iostream>
#include "JetMatrix.h"
#include "except.h"

using namespace std;


//-----------------------------------------------
int main(void)
{
	double * v, * u;
	int i, j, k;
	JetMatrix jet(2);
	cout << "NComp: " << jet.n_comps() << std::endl;
	
	i = 0;
	jet(i, 2.1);
	cout << "Comp(" << i << ") = " << jet(i) <<  std::endl;
	
	i = 1;
	jet(i, 3.4);
	cout << "Comp(" << i << ") = " << jet(i) <<  std::endl;
	
	//-------------------------
	
	i = 0; j = 0;
	jet(i, j, 4.4);
	cout << "Comp(" << i << ", " << j << ") = " << jet(i,j) <<  std::endl;
	
	i = 0; j = 0, k = 0; jet(i, j, k, 1.1);
	i = 0; j = 0, k = 1; jet(i, j, k, 1.2);
	i = 0; j = 1, k = 0; jet(i, j, k, 1.3);
	i = 0; j = 1, k = 1; jet(i, j, k, 1.4);
	i = 1; j = 0, k = 0; jet(i, j, k, 1.5);
	i = 1; j = 0, k = 1; jet(i, j, k, 1.6);
	i = 1; j = 1, k = 0; jet(i, j, k, 1.7);
	i = 1; j = 1, k = 1; jet(i, j, k, 1.8);

	cout << "Comp(" << i << ", " << j << ", " << k << ") = " << jet(i,j,k) <<  std::endl;

	JetMatrix jat(jet);
	i = 0; j = 0, k = 0; jet(i, j, k, 10.1);


	v = jat();
	for (i=0; i<14; i++) cout << "v: " << v[i] <<  std::endl;

	cout << std::endl;
	u = jet();
	for (i=0; i<14; i++) cout << "u: " << u[i] <<  std::endl;

	return 0;
}


