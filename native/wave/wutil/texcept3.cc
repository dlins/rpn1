#include <stdlib.h>
#include <iostream>
#include "except.h"

class TestException : public exception {};

void
sub()
{
	THROW(TestException());
}

int
main(int argc, char *argv[])
{
#ifdef except_h_HAS_EXCEPTIONS
	try {
#endif /* except_h_HAS_EXCEPTIONS */
		sub();
#ifdef except_h_HAS_EXCEPTIONS
	}
	catch (exception &e) {
		cerr << "ERROR: " << argv[0] << ": " << e.what() << endl;
		abort();
	}
#endif /* except_h_HAS_EXCEPTIONS */

	return 0;
}
