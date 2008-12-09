#include "Quad2FluxParams.h"




const double Quad2FluxParams::DEFAULT_A[2] = { 0., 0. };
const double Quad2FluxParams::DEFAULT_B[2][2] = { { 0., .1 }, { -.1, 0. } };
const double Quad2FluxParams::DEFAULT_C[2][2][2] = { { { -1., 0. }, { 0., 1. } }, { { 0., 1. }, { 1., 0. } } };
//

Quad2FluxParams::Quad2FluxParams(void) :FluxParams(RealVector(14)) 
{
 
    int m=2;

        for (int i = 0; i < m; i++) {
            component(i, DEFAULT_A[i]);
            for (int j = 0; j < m; j++) {
            component(m + i * m + j, DEFAULT_B[i] [j]);
                for (int k = 0; k < m; k++) {
                    component(m + m * m + i * m * m + j * m + k, DEFAULT_C[i] [j] [k]);
                }
            }
        }
    
    
    
}

Quad2FluxParams::~Quad2FluxParams(){}

Quad2FluxParams::Quad2FluxParams(const RealVector & params) :FluxParams(params) 
{
}



