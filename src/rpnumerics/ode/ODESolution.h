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
    
    ODESolution(vector<RealVector> , vector<double> );
    ODESolution();
    
    virtual ~ODESolution();
    
    vector<double> getTimes() const ;
    
    int getFlag() ;
    
    void addCoords(const RealVector);
    void addTimes(const double );
    
    vector<RealVector> getCoords()  const;
    
protected :
    
    vector <RealVector> coords_;
    
    vector <double> times_;
    
    int flag_;
    
};

inline void ODESolution::addCoords(const RealVector coord){ coords_.push_back(coord);}

inline void ODESolution::addTimes(const double time){times_.push_back(time);}

inline vector<RealVector> ODESolution::getCoords()const  {return coords_;}

inline vector <double> ODESolution::getTimes() const {return times_;}

#endif	/* _ODESolution_H */

