#ifndef _ODESolution_H
#define	_ODESolution_H

#include "WavePoint.h"
#include <vector>
#include <iostream>
#include "RealVector.h"

class ODESolution {
public:
    
    //
    // Constants
    //
    
    static int STOP_OUT_OF_BOUNDARY;
    static int STOP_ON_POINCARE_SECTION;
    static int MADE_MAXIMUM_STEP_NUMBER;
    
    ODESolution(const RealVector *, double *, int);
    ODESolution();
    virtual ~ODESolution();
    
    double getTimes();
    int getFlag() ;
   
    void addCoords(const RealVector coord);
    
    vector<RealVector> getCoords();
    
protected :
    
    vector<RealVector> coords_;
    
    double * times_;
    int flag_;
    
};

inline vector<RealVector> ODESolution::getCoords(){return coords_;}
inline void ODESolution::addCoords(const RealVector coord){ coords_.push_back(coord);}

#endif	/* _ODESolution_H */

