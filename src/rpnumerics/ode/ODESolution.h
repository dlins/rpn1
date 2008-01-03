#ifndef _ODESolution_H
#define	_ODESolution_H

#include "WavePoint.h"
#include <vector>
#include <iostream>
#include "RealVector.h"

using std::vector;

class ODESolution {
public:
    
    //
    // Constants
    //
    
    static int STOP_OUT_OF_BOUNDARY;
    static int STOP_ON_POINCARE_SECTION;
    static int MADE_MAXIMUM_STEP_NUMBER;
    
    ODESolution(vector<RealVector> , vector<double> , int);
    ODESolution();
    virtual ~ODESolution();
    
    vector<double> getTimes();

    int getFlag() ;
    
    void addCoords(const RealVector);
    void addTimes(const double );
    
    vector<RealVector> getCoords();
    
protected :
        
    vector <RealVector> coords_;
    
    vector <double> times_;

    int flag_;
    
};


#endif	/* _ODESolution_H */

