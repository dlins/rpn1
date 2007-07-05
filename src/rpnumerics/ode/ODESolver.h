#include "RealVector.h"
#include  "VectorField.h"
#include "ODESolverProfile.h"
#include "ODESolution.h"

class ODESolver {
    
    public:
    
  virtual ODESolution solve (const RealVector & ,int )=0;
  virtual ODESolverProfile getProfile()=0;

  virtual VectorField getVectorField()=0;
  
    
    
};
