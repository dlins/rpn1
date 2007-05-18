#define DIM 2
#define FUNCTIONS_ARRAY_SIZE 2

class FluxFunction {

    public:

    virtual void F (double [],double [],double[])=0;
    virtual void DF (double [][DIM],double [],double[])=0;
    virtual void D2F (double [][DIM][DIM],double [],double[])=0;

};


FluxFunction ** getFluxFunctionArray();



