/* IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) HessianMatrix.h
 **/



#ifndef _HessianMatrix_H
#define	_HessianMatrix_H
#include <math.h>

class HessianMatrix{

    public :
        //!Constructor 
        HessianMatrix(const int n);
        ~HessianMatrix();
        //! Mutator
        void setElement(const int i , const int j , const int k, const double value);
        //!Accessor
        double getElement (const int i , const int j , const int k);
        
        
        private:
            double * data_;
            int dim_;
            
};

inline double HessianMatrix::getElement(const int i,const int j,const int k){
    
      int index=((int)(pow(dim_,2)*i))+dim_*j+k;
	return data_[index];
}


inline void HessianMatrix::setElement(const int i,const int j, const int k, const double value){
    
      int index=((int)(pow(dim_,2)*i))+dim_*j+k;
      data_[index]=value;
      
}

#endif	/* _HessianMatrix_H */

