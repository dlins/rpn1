#ifdef __GNUC__
#pragma implementation
#endif
#include <iostream>
#include <label.h>

#include "Extractable.h"

using namespace std; 

Extractable::Extractable(void)
{
}

Extractable::~Extractable(void)
{
}

Extractable::Status
Extractable::extract_first(istream &is)
{
// TODO: write this!
//	is.seekg(0);
	return extract_next(is);
}

Extractable::Status
Extractable::extract_last(istream &is)
{
// TODO: write this!
// TEMPORARY:
	return extract_next(is);
}

Extractable::Status
Extractable::extract_next(istream &is)
{
	if (!is)
		return BAD_INPUT;

	if (!(is >> label(search_string()))) {
		is.clear();	// allow previous values to be retained
		return NOT_FOUND;
	}

	extract(is);
	if (!is)
		return BAD_INPUT;

	return FOUND;
}

istream &
operator>>(istream &is, Extractable &extractable)
{
	if (extractable.extract_next(is) == Extractable::NOT_FOUND)
		is.clear(ios::badbit);
	return is;
}
