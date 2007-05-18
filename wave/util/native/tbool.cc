#include <iostream>
#include "bool.h"

int
main()
{
	bool b;
	cerr << "false = " << false << "\n";
	cerr << "true = " << true << "\n";
	cerr << "Will the Yankees win the pennant? ";
	cin >> b;
	if (!cin) {
		cerr << "ERROR: tbool: invalid input\n";
		return -1;
	}
	cerr << "Your answer is '" << b << "'.\n";
	return 0;
}
