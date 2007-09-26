
#ifndef _Jacobian_H
#define	_Jacobian_H

#include "JetMatrix.h"



class JacobianMatrix : public JetMatrix{
    
    //! the value accessor at vindx index
    void setVal(int vindx [], double val);
    //! the value mutator at vindx index
    double getVal(int vindex []) const;
    
    
};


#endif	/* _Jacobian_H */

