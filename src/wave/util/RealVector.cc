#include "RealVector.h"

RealVector::RealVector(void) : Vector(2) {}

RealVector::RealVector(int size) :  Vector(size) {}

RealVector::RealVector(const int size, double * coords) :  Vector(size, coords) { }

RealVector::RealVector(const RealVector & copy):Vector(copy.size()){
    int i;
    for (i=0;i < size(); i++){
        operator ()(i)=copy.operator ()(i);
    }
}

//RealVector & RealVector::operator =(const RealVector & source){
//    
//    if (this==&source) return *this;
//    
//    int i;
//    
//    for (i=0;i < size();i++){
//        
//        operator ()(i)=source.operator ()(i);
//        
//    }
//    
//    return *this;
//    
//    
//}


bool RealVector::operator ==(const RealVector &test) {
    int i ;
    for (i=0;i < size();i++)
        if (test.component(i)!=component(i)) return false;
    return true;
}

void RealVector::negate(){
    
    int i;
    
    for (i=0;i < size();i++)
        component(i)=component(i)*(-1);
    
}


double RealVector::dot(const RealVector v1){
    
    
    if (size()!=v1.size()) THROW  (RealVector::RangeViolation());
    
    int i;
    double result;
    
    for (i=0; i< v1.size();i++){
        
        result+=component(i)*v1.component(i);
    }
    
    return result;
    
}

RealVector::~RealVector(){}

void RealVector::sortEigenData(int n, double * eigenValR, double * eigenValI, RealVector * eigenVec) {
    int i = 0;
    int d1, d2, j;
    bool flag = true;
    RealVector * sortVec = new RealVector[4];
    for (i = 0; i < 4; i++){
        sortVec[i] =  RealVector(n);
    
    }

    double sortSpaceR [4] = {0, 0, 0, 0};
    double sortSpaceI [4] = {0, 0, 0, 0};
    while (flag) {
        flag = false;
        i = 0;
        if (eigenValI[i] != 0)
            d1 = 2;
        else
            d1 = 1;
        while (i + d1 < n) {
            if (eigenValI[i + d1] != 0)
                d2 = 2;
            else
                d2 = 1;
            if (eigenValR[i] > eigenValR[i + d1]) {
                flag = true;
                for (j = 0; j < d2; j++) {
                    sortSpaceR[j] = eigenValR[i + d1 + j];
                    sortSpaceI[j] = eigenValI[i + d1 + j];
                    sortVec[j] = eigenVec[i + d1 + j];
                }
                for (j = 0; j < d1; j++) {
                    sortSpaceR[d2 + j] = eigenValR[i + j];
                    sortSpaceI[d2 + j] = eigenValI[i + j];
                    sortVec[d2 + j] = eigenVec[i + j];
                }
                for (j = 0; j < d1 + d2; j++) {
                    eigenValR[i + j] = sortSpaceR[j];
                    eigenValI[i + j] = sortSpaceI[j];
                    eigenVec[i + j] = sortVec[j];
                }
                i = i + d2;
            }
            else {
                i = i + d1;
                d1 = d2;
            }
        }
    }
}

//
// No need for accessor and mutator methods! Use 'Vector' methods to access components.
//
//void RealVector::setVal(int vindx [], double val){
//}
//
//double RealVector::getVal(int vindex []) const{
//}
