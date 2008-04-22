/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) RealVector.h
 **/


#ifndef _RealVector_H
#define	_RealVector_H

#include "Vector.h"

//!
/*! @brief Class that represents a n dimensional vector
 *
 *
 * @ingroup wave
 */


class RealVector :public Vector {
    
public:
    
    /*!Default constructor
     *
     *Will be created a vector with dimension two
     *
     */
    RealVector(void);
    
    /*! Create a vector with a arbitrary dimension
     */
    
    RealVector(int);
    
    /*! Create a vector with a array of doubles
     *@param size Length of the array
     *@param data Array of doubles
     */
    
    RealVector(const  int size ,double * data );
    
    RealVector(const RealVector &);
    
    virtual ~RealVector(void);
    
    bool operator==(const RealVector &);
    
    /*! Multiply all components by -1
     */
    
    void negate();
    
    /*! Return the internal product with other RealVector
     *@param vector A RealVector with the same size
     */
    
    double dot(const RealVector & vector);
    
    static void sortEigenData(int n, double * eigenValR, double * eigenValI, RealVector * eigenVec);
    
    
};
#endif	/* _RealVector_H */

