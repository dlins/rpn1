#ifndef _Inserter_h
#define _Inserter_h
#ifdef __GNUC__
#pragma interface
#endif

#include "Insertable.h"

class Inserter
	: public ADV
{
public:
	Inserter(const Insertable &insertable__, ostream &os__);
	~Inserter(void);

	void update(void);

private:
	const Insertable &insertable(void) const;
	ostream &os(void) const;

	const Insertable &insertable_;
	ostream &os_;
};

#endif /* _Inserter_h */
