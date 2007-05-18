#ifndef _Insertable_h
#define _Insertable_h
#ifdef __GNUC__
#pragma interface
#endif

#include <iostream>
#include "ADTADV.h"

using namespace std;

class Insertable
	: public virtual ADT
{
public:
	Insertable(void);
	virtual ~Insertable(void);

	virtual void insert(ostream &os) const = 0;
};

ostream &operator<<(ostream &os, const Insertable &insertable);

#endif /* _Insertable_h */
