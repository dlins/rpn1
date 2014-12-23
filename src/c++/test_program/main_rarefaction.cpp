#include <iostream>
#include "Brooks_CoreySubPhysics.h"

int main(){
    Brooks_CoreySubPhysics *subphysics = new Brooks_CoreySubPhysics;

    const FluxFunction *flux = subphysics->flux();
    const AccumulationFunction *accumulation = subphysics->accumulation();
    const Boundary *boundary = subphysics->boundary();

    for (int i = 0; i < 10000; i++) RarefactionCurve rarefaction(accumulation, flux, boundary);

    return 0;
}

