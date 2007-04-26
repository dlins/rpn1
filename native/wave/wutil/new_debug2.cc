#include <stdio.h>
#include <stddef.h>

extern "C" {

// fast dumb first-fit allocator
// J. Coplien, Advance C++, p. 73.

struct Head {
	long length;
	struct Head *next;
};

static Head pool = { 0, 0 };

static const long POOL_SIZE = 500000;
static char heap[POOL_SIZE];
static Head *h = (Head *) heap;

// THE NEXT LINE IS MACHINE AND COMPILER DEPENDENT
const long WRDSIZE = sizeof(void *);

static long current_malloc_total = 0;

char *
malloc(size_t nbytes)
{
	current_malloc_total += nbytes;
	fprintf(stdout, "malloc:\tnbytes = %d\tcurrent_malloc_total = %d\n",
		nbytes, current_malloc_total);
	// First, look in free list.
	/***
	if (pool.next) {
		Head *prev = &pool;
		for (Head *cur = &pool; cur; cur = cur->next) {
			if (cur->length >= nbytes) {
				prev->next = cur->next;
				return (char *) (cur + 1);
			}
			else
				prev = cur;
		}
	}
	***/
	// Nothing is on free list; get new block from heap.
	h = (Head *) (((int) (((char *) h + WRDSIZE - 1))/WRDSIZE)*WRDSIZE);
	h->next = 0;
	h->length = nbytes;
	char *where = (char *) (h + 1);
	h = (Head *) (where + nbytes);
	if ((char *) h > heap + POOL_SIZE) {
		fprintf(stderr, "malloc: POOL_SIZE depleted\n");
		exit(-1);
	}
	return where;
}

void
free(void *ptr)
{
	Head *p = (Head *) ptr - 1;
	current_malloc_total -= p->length;
	fprintf(stdout, "free:\tnbytes = %d\tcurrent_malloc_total = %d\n",
		p->length, current_malloc_total);
	p->next = pool.next;
	pool.next = p;
}

}

static long current_new_total = 0;

void *
operator new(size_t size)
{
	current_new_total += size;
	fprintf(stdout, "new:\tsize = %d\tcurrent_new_total = %d\n",
		size, current_new_total);
	size_t *p = (size_t *) malloc(size + sizeof(int));
	*p = size;
	return (void *) (((char *) p) + sizeof(int));
}

void
operator delete(void *p)
{
	char *cp = (char *) (((char *) p) - sizeof(int));
	size_t size = * (int *) cp;
	current_new_total -= size;
	fprintf(stdout, "delete:\tsize = %d\tcurrent_new_total = %d\n",
		size, current_new_total);
	free(cp);
}
