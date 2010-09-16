#include "alloc.h"

// The initialization procedure for the structure above.
// The user specifies the number of rows and the initial number
// of columns. The counter is set to zero, and enough space
// is requested of the system to store a rows times cols matrix of doubles.
void init_store(struct store* s, int rows, int cols){
    s->count = 0;    
    s->rows  = rows;
    s->cols  = cols;
    s->st    = (double*)malloc((s->cols)*(s->rows)*sizeof(double));
}

// This function returns a pointer to struct store, after initializing it.
struct store *create_store(int rows, int cols){
    struct store *s;
    s = (struct store *)malloc(sizeof(struct store));
    init_store(s, rows, cols);
    return s;
}

// Adds the contents of array t to st (the storage area of s).
void add(struct store *s, double *t){
    int i;
 
    // If there is not enough room, the storage area is duplicated
    // and the number of columns is updated.
    if (s->count == s->cols){
        s->st = (double*) realloc(s->st, 2*(s->cols)*(s->rows)*sizeof(double));
        s->cols = 2*(s->cols);        
    }
    
    // Store t in s->st.
    for (i = 0; i < s->rows; i++) s->st[s->rows*s->count+i] = t[i];
            
    // Update the counter
    s->count++;
    return;
}

// Retrieves the indx-th element of s->st and stores it in t.
void get(struct store *s, double *t, int indx){
    int i;
    for (i = 0; i < s->rows; i++){
        t[i] = s->st[indx*s->rows + i];
    }
    return;
}

// Destroys s by freeing the memory that was allocated to it.
void destroy(struct store *s){
    free(s->st);
    free(s);
    return;
}

// Pops the last element of s.
void pop(struct store *s){
    if (s->count > 0) s->count--;

    return;
}

