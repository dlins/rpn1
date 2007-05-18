#ifdef __GNUC__
#pragma implementation
#endif
#include <iostream>
#include "Inserter.h"

Inserter::Inserter(const Insertable &insertable__, ostream &os__)
	: ADV(insertable__),
	  insertable_(insertable__),
	  os_(os__)
{
}

Inserter::~Inserter(void)
{
}

inline const Insertable &
Inserter::insertable(void) const
{
	return insertable_;
}

inline ostream &
Inserter::os(void) const
{
	return os_;
}

void
Inserter::update(void)
{
	os() << insertable() << endl;
}
