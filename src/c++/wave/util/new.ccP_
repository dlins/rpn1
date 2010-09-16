#ifdef __GNUC__
#pragma implementation
#endif
#include "<T>new.h"
#include "<T>.h"
#include <ostream.h>
#include <sys/stdtypes.h>
#include <malloc.h>

static int current_total = 0;

void *
<T>::operator new(size_t size)
{
	current_total += size;
	cerr << "<T>::new: size = " << size << " current_total = "
		<< current_total << "\n";
	return ::new char[size];
}

void
<T>::operator delete(void *p, size_t size)
{
	current_total -= size;
	cerr << "<T>::delete: size = " << size << " current_total = "
		<< current_total << "\n";
	::delete [size] ((char *) p);
}
