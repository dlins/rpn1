/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) JetMatrix.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "JetMatrix.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


JetMatrix::JetMatrix(void) :n_comps_(1), size_(3), v_(size_), c0_(false), c1_(false), c2_(false){ }

JetMatrix::JetMatrix(const int n_comps) :n_comps_(n_comps), size_(n_comps * (1 + n_comps * (1 + n_comps))),
        v_(size_),
        c0_(false),
        c1_(false),
        c2_(false) {
    
}

JetMatrix::JetMatrix(const JetMatrix & jetMatrix) :
	n_comps_(jetMatrix.n_comps_), size_(n_comps_ * (1 + n_comps_ * (1 + n_comps_))),
        v_(Vector(jetMatrix.v_)), c0_(false), c1_(false), c2_(false) {  
}

JetMatrix::JetMatrix(int degree, int n_comps, double * values) :
	n_comps_(n_comps), size_(n_comps_ * (1 + n_comps_ * (1 + n_comps_))),
        v_(Vector(size_, values)), c0_(true), c1_(false), c2_(false) {

	if ( degree >= 1 ) c1_ = true;
	if ( degree >= 2 ) c2_ = true;
}

int JetMatrix::n_comps(void) const { return n_comps_;  }

JetMatrix::~JetMatrix(){};

void JetMatrix::resize(int n_comps) {
    size_ = (n_comps * (1 + n_comps * (1 + n_comps)));
    v_.resize(size_);
    n_comps_ = n_comps;
}









