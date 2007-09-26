/* IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) HessianMatrix.h
 *
 */

#ifndef HessianMatrix_H
#define	HessianMatrix_H

#include "JetMatrix.h"
#include "Vector2.h"



class HessianMatrix: public Matrix2,public JetMatrix {
    
public :
    //!Constructor
    HessianMatrix(const int n);
    virtual ~HessianMatrix();
    
    //! the value accessor at vindx index
    void setVal(int vindx [], double val);
    //! the value mutator at vindx index
    double getVal(int vindex []) const;
};



#endif	/* _HessianMatrix_H */

