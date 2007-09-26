#include "RealVector.h"

RealVector::RealVector(int size):Vector(size){


}

RealVector::RealVector(){}

bool RealVector::operator ==(const RealVector &test){
    
    int i ;
    for (i=0;i < size();i++){
        
        if (test.component(i)!=component(i))
            return false;
    }
    return true;
    
}
void RealVector::setVal(int vindx [], double val){
    
}
double RealVector::getVal(int vindex []) const{
    
}
