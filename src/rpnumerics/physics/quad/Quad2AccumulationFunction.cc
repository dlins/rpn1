#include "Quad2AccumulationFunction.h"

Quad2AccumulationFunction::Quad2AccumulationFunction(void)
{
}

inline Quad2AccumulationFunction::Quad2AccumulationFunction(const AccumulationParams & params) :AccumulationFunction(params) {}
    
    
    inline Quad2AccumulationFunction::~Quad2AccumulationFunction(void) {}
    
    int Quad2AccumulationFunction::jet(const WaveState&, JetMatrix&, int){
        return 0;
    }
    

    
    
    
    
    
    