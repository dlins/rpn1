#include "except.h"

class TestException : public exception {};

int
main()
{
	THROW(TestException());
	return 0;
}
