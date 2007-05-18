#ifndef _Extractable_h
#define _Extractable_h
#ifdef __GNUC__
#pragma interface
#endif

#include <iostream>

class Extractable
{
public:
	Extractable(void);
	virtual ~Extractable(void);

	enum Status { FOUND, NOT_FOUND, BAD_INPUT };

	Status extract_first(istream &is);
	Status extract_last(istream &is);
	Status extract_next(istream &is);

protected:
	virtual const char *search_string(void) const = 0;
	virtual void extract(istream &is) = 0;
};

istream &operator>>(istream &is, Extractable &extractable);

#endif /* _Extractable_h */
