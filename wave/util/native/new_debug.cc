#include <stdio.h>
#include <stddef.h>

#define DEBUG
// TODO:  Figure out why this fails.  (It's OK if used only locally, by
//	  removing the extern "C" direcetives, but it fails when XView
//	  and XGL are used.)
// #define MY_MALLOC

#ifndef MY_MALLOC
#include <malloc.h>
#else

extern "C" {

// Taken from Kernighan and Ritchie, p. 175ff.

#include <sys/unistd.h>

typedef int ALIGN;

union header {
	struct {
		union header *ptr;
		size_t size;
	} s;
	ALIGN x;
};

typedef union header HEADER;

static HEADER base;
static HEADER *allocp = NULL;

#ifdef DEBUG
static long current_malloc_total = 0;
#endif

static void
actual_free(void *ap)
{
	register HEADER *p, *q;

	p = (HEADER *) ap - 1;
	for (q = allocp; !(p > q && p < q->s.ptr); q = q->s.ptr)
		if (q >= q->s.ptr && (p > q || p < q->s.ptr))
			break;

	if (p + p->s.size == q->s.ptr) {
		p->s.size += q->s.ptr->s.size;
		p->s.ptr = q->s.ptr->s.ptr;
	}
	else
		p->s.ptr = q->s.ptr;
	if (q + q->s.size == p) {
		q->s.size += p->s.size;
		q->s.ptr = p->s.ptr;
	}
	else
		q->s.ptr = p;
	allocp = q;
}

void
free(void *ap)
{
#ifdef DEBUG
	HEADER *p = (HEADER *) ap - 1;
	current_malloc_total -= p->s.size*sizeof(HEADER);
	fprintf(stdout, "free:\tsize = %d\tcurrent_malloc_total = %d\n",
		p->s.size*sizeof(HEADER), current_malloc_total);
#endif
	actual_free(ap);
}

static const int NALLOC = 128;

static HEADER *
morecore(size_t nu)
{
	register caddr_t cp;
	register HEADER *up;
	register int rnu;

	rnu = NALLOC * ((nu + NALLOC - 1) / NALLOC);
	cp = sbrk(rnu * sizeof(HEADER));
	if ((int) cp == -1)
		return NULL;
	up = (HEADER *) cp;
	up->s.size = rnu;
	actual_free((char *) (up + 1));
	return allocp;
}

char *
malloc(size_t nbytes)
{
	register HEADER *p, *q;
	register int nunits;

	nunits = 1 + (nbytes + sizeof(HEADER) - 1)/sizeof(HEADER);
	if ((q = allocp) == NULL) {
		base.s.ptr = allocp = q = &base;
		base.s.size = 0;
	}
	for (p = q->s.ptr; ; q = p, p = p->s.ptr) {
		if (p->s.size >= nunits) {
			if (p->s.size == nunits)
				q->s.ptr = p->s.ptr;
			else {
				p->s.size -= nunits;
				p += p->s.size;
				p->s.size = nunits;
			}
			allocp = q;
#ifdef DEBUG
	current_malloc_total += p->s.size*sizeof(HEADER);
	fprintf(stdout, "malloc:\tsize = %d\tcurrent_malloc_total = %d\n",
		p->s.size*sizeof(HEADER), current_malloc_total);
#endif
			return (char *) (p + 1);
		}
		if (p == allocp)
			if ((p = morecore(nunits)) == NULL)
				return NULL;
	}
}

} /* extern "C" */

#endif MY_MALLOC

#ifdef DEBUG
static long current_new_total = 0;
#endif

void *
operator new(size_t size)
{
#ifdef DEBUG
	current_new_total += size;
	fprintf(stdout, "new:\tsize = %d\tcurrent_new_total = %d\n",
		size, current_new_total);
#endif
	size_t *p = (size_t *) malloc(size + sizeof(int));
	*p = size;
	return (void *) (((char *) p) + sizeof(int));
}

void
operator delete(void *p)
{
	char *cp = (char *) (((char *) p) - sizeof(int));
#ifdef DEBUG
	size_t size = * (int *) cp;
	current_new_total -= size;
	fprintf(stdout, "delete:\tsize = %d\tcurrent_new_total = %d\n",
		size, current_new_total);
#endif
	free(cp);
}
