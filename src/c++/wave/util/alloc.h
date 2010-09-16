#ifndef ALLOC_H
#define ALLOC_H

#include <stdio.h>
#include <stdlib.h>

// The structure that holds the data.
struct store{
    int     count;  // How many elements are stored
    int     rows;   // The number of rows
    int     cols;   // The number of columns
    double  *st;    // A pointer to an array where the data is stored
};

void init_store(struct store*, int, int);
struct store *create_store(int, int);
void add(struct store *, double *);
void get(struct store *, double *, int);
void destroy(struct store *);
void pop(struct store *);

#endif
