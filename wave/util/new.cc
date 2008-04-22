#ifdef __GNUC__
#pragma implementation
#endif
#include <stddef.h>

// fast dumb first-fit allocator
// J. Coplien, Advance C++, p. 73.

struct Head {
	unsigned long length;
	struct Head *next;
};

static Head pool = { 0, 0 };

static char heap[50000];

static Head *h = (Head *) heap;

typedef char * Char_p;

// THE NEXT LINE IS MACHINE AND COMPILER DEPENDENT
const unsigned long WRDSIZE = sizeof(void *);

void *
operator new(size_t nbytes)
{
	// First, look in free list.
	if (pool.next) {
		Head *prev = &pool;
		for (Head *cur = &pool; cur; cur = cur->next) {
			if (cur->length >= nbytes) {
				prev->next = cur->next;
				return (void *) (cur + 1);
			}
			else
				prev = cur;
		}
	}
	// Nothing is on free list; get new block from heap.
	h = (Head *) (((int) (((char *) h + WRDSIZE - 1))/WRDSIZE)*WRDSIZE);
	h->next = 0;
	h->length = nbytes;
	char *where = (char *) (h + 1);
	h = (Head *) (where + nbytes);
	return (void *) where;
}

void
operator delete(void *ptr)
{
	Head *p = (Head *) ptr - 1;
	p->next = pool.next;
	pool.next = p;
}
