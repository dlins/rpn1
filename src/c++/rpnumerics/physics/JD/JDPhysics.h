/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) JDPhysics.h
 */

#ifndef _JDPhysics_H
#define _JDPhysics_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "JDFluxFunction.h"
#include "SubPhysics.h"
#include "RectBoundary.h"
#include "JDAccumulationFunction.h"
#include "ShockMethod.h"
#include "Double_Contact.h"
#include "JDFluxFunction.h"
#include "JDEvap_Extension.h"
#include "JDEvaporationCompositeCurve.h"
#include "HugoniotContinuation2D2D.h"
#include "ImplicitHugoniotCurve.h"
#include "CoincidenceJD.h"
/*
 * ---------------------------------------------------------------
 * Definitions:
 */


class JDPhysics:public SubPhysics {

private:
 Boundary * defaultBoundary() const;

public:

    JDPhysics();

    ~JDPhysics();

    void setParams(vector<string>);
    vector<double> *  getParams();

    SubPhysics * clone()const;


};



#endif //! _JDPhysics_H
