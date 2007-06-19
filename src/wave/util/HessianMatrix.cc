#include "HessianMatrix.h"

HessianMatrix::HessianMatrix(const int n){
    
    data_= new double[n];
}


HessianMatrix::~HessianMatrix(){
    delete (data_);
}