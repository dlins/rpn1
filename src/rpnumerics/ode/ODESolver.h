#include "RealVector.h"
#include  "VectorField.h"
#include "ODESolverProfile.h"
#include "ODESolution.h"

class ODESolver {
    
public:
    
    virtual ~ODESolver();
    virtual ODESolution solve(const RealVector & , int )=0;
    virtual ODESolverProfile getProfile()=0;
    
    
};
