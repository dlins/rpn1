/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) RarefactionFlow.h
 **/

#ifndef _RarefactionFlow_H
#define _RarefactionFlow_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "WaveFlow.h"
#include "PhasePoint.h"
#include "RealVector.h"
#include "JacobianMatrix.h"
#include "HessianMatrix.h"
#include "ReturnCodes.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */

class RarefactionFlow : WaveFlow {

private:

	static const int FIRST = 1;

	PhasePoint * referenceVector_;
	int familyIndex_;// = 1;

public:

	//! default constructor
	RarefactionFlow(void);

	//! reference vector constructor
	RarefactionFlow(const PhasePoint & phasePoint);

	//! reference vector and family index constructor
	RarefactionFlow(const PhasePoint & phasePoint,int familyIndex);

	//! the jet methods
	virtual int jet(const WaveState &u,JetMatrix &out,int degree);

	//! the reference vector accessor 
        PhasePoint & referenceVector() const;

        RarefactionFlow & operator=(const RarefactionFlow &source);

};

inline RarefactionFlow::RarefactionFlow() :
	referenceVector_(new PhasePoint(2)),familyIndex_(1)//TODO Fix implementation
{
}

inline RarefactionFlow::RarefactionFlow(const PhasePoint & phasePoint) :
	referenceVector_(new PhasePoint(phasePoint)),familyIndex_(1)
{
}

inline PhasePoint & RarefactionFlow::referenceVector(void)const 
{
	return *referenceVector_;
}

inline RarefactionFlow & RarefactionFlow::operator =(const RarefactionFlow &source ){
    
    if (this==&source) return *this;
    delete referenceVector_;
    referenceVector_=new PhasePoint(source.referenceVector_->size());
    int i;
    for (i=0; i < referenceVector_->size();i++){
        referenceVector_->component(i)=source.referenceVector().component(i);
    }
    return *this;
    
}

int RarefactionFlow::jet(const WaveState &u, JetMatrix &out, int degree = 0) {
    
    WaveState  in = u;
    
//    int stateSpaceDim = u.stateSpace();
    
    int stateSpaceDim= in.stateSpace();

    JetMatrix df(stateSpaceDim);
    
    // TODO should WaveFlow have a FluxFunction associated now ???
//    flux().jet(u, df, 1); //TODO Acessar via RpNumerics
    
    double * eigenValR = new double[stateSpaceDim];
    double * eigenValI = new double[stateSpaceDim];
    RealVector * eigenVec  = new RealVector[stateSpaceDim];
    
    // TODO should JacobianMatrix be a RealMatrix2 ? (an alias ??)

    JacobianMatrix  eigenCalcMatrix(2);
    
    df.jacobian(eigenCalcMatrix);
    
//      void fillEigenData(int stateSpaceDim, RealMatrix2 * df, double * eigenValR, double * eigenValI, RealVector * eigenVec);
    
    eigenCalcMatrix.fillEigenData(stateSpaceDim, eigenCalcMatrix, *eigenValR, *eigenValI,* eigenVec);
    
    // getting eigenvalues and eigenvectors sorted
    // with increasing absolute value of real part
    RealVector::sortEigenData(stateSpaceDim, eigenValR, eigenValI, eigenVec);
    
    RealVector rarefactionVector = eigenVec[familyIndex_];
    
    if (rarefactionVector.dot(referenceVector_->operator()()) < 0.)
        rarefactionVector.negate();

    delete referenceVector_;
    referenceVector_= new  PhasePoint(rarefactionVector);//(rarefactionVector);
      
    out.f(rarefactionVector);
    
    delete [] eigenValR;
    delete [] eigenValI;
    delete [] eigenVec;
    
    return OK;
    
    
    
}


#endif //! _RarefactionFlow_H
