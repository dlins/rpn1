#ifdef __GNUC__
#pragma implementation
#endif
#include <iostream>
#include "Insertable.h"

Insertable::Insertable(void)
{
}

Insertable::~Insertable(void)
{
}

ostream &operator<<(ostream &os, const Insertable &insertable)
{
	insertable.insert(os);
	return os;
}
