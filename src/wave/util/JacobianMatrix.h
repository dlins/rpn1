/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) JacobianMatrix.h
 */

#ifndef _JacobianMatrix_H
#define _JacobianMatrix_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "RealMatrix2.h"
#include "except.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


/*!@brief Utility class to a store second derivative matrix
 * 
 *  This matrix has n rows and n columns 
 *
 * @ingroup wave
 */

class JacobianMatrix: public RealMatrix2 {
private:
    int n_comps_;

    class RangeViolation : public exception { };
public:

    JacobianMatrix(const int n_comps);
    JacobianMatrix(const JacobianMatrix & jacobianMatrix);
    

//    friend std::ostream & operator<<(std::ostream& , const JacobianMatrix& );
    
    
    int n_comps(void) const;
    void resize(int n_comps);
    
};



inline int JacobianMatrix::n_comps(void) const {
    return n_comps_;
}

inline void JacobianMatrix::resize(int n_comps) {
    
    RealMatrix2::resize(n_comps,n_comps);
    n_comps_ = n_comps;
}



#endif //! _JacobianMatrix_H
