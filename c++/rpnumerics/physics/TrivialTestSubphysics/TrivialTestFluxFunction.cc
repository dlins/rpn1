#include "TrivialTestFluxFunction.h"

TrivialTestFluxFunction::TrivialTestFluxFunction() : FluxFunction() {
}

TrivialTestFluxFunction::~TrivialTestFluxFunction(){
}
        
int TrivialTestFluxFunction::jet(const WaveState &u, JetMatrix &m, int degree) const {
    double x = u(0);
    double y = u(1);
    
    m.resize(2);
    
    if (degree >= 0){
        m.set(0, x*x*y);
        m.set(1, x*y*y);
        
        if (degree >= 1){
            m.set(0, 0, 2.0*x*y);
            m.set(0, 1, x*x);
            
            m.set(1, 0, y*y);
            m.set(1, 1, 2.0*x*y);
            
            if (degree >= 2){
                m.set(0, 0, 0, 2.0*y);
                m.set(0, 0, 1, 2.0*x);
                m.set(0, 1, 0, 2.0*x);
                m.set(0, 1, 1, 0.0);
                
                m.set(1, 0, 0, 0.0);
                m.set(1, 0, 1, 2.0*y);
                m.set(1, 1, 0, 2.0*y);
                m.set(1, 1, 1, 2.0*x);
            }
        }
    }
    
    return degree;
}

