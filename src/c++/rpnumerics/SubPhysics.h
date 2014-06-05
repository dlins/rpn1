/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) SubPhysics.h
 */

#ifndef _SubPhysics_H
#define _SubPhysics_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */

#include "FluxFunction.h"
#include "AccumulationFunction.h"
#include "HugoniotContinuation.h"
#include "Boundary.h"
#include "Space.h"
#include  "Multid.h"
#include "eigen.h"
#include "HugoniotCurve.h"
#include "ThreeImplicitFunctions.h"
#include "Double_Contact_Function.h"
#include "methods/ShockMethod.h"
#include <map>
#include "HugoniotContinuation2D2D.h"
#include "Secondary_Bifurcation_Interface.h"
#include <sstream>
#include "CompositeCurve.h"
#include "Extension.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */



class SubPhysics {
private:




    Double_Contact_Function * doubleContactFunction_;
    ShockMethod * shock_method_;
    Boundary * boundary_;
    Space * space_;
    const char * ID_;
    int type_;



protected:

    FluxFunction * fluxFunction_;
    AccumulationFunction * accumulationFunction_;
    Viscosity_Matrix * viscosityMatrix_;
    Boundary * preProcessedBoundary_;
    std::map<string, HugoniotCurve *> * hugoniotCurveArray_;
    std::map<string, Extension *> * extensionCurveArray_;
    std::map<string, Secondary_Bifurcation_Interface *> * secondaryBifurcationArray_;
    
    HugoniotContinuation * hugoniot_continuation_method_;
    CompositeCurve * compositeCurve_;
    ShockCurve * shockCurve_;

public:


    SubPhysics(const Boundary &, const Space &, const char *, int);

    SubPhysics(const FluxFunction &, const AccumulationFunction &, const Boundary &, const Space &, const char *, int);

    //    SubPhysics(const FluxFunction &, const AccumulationFunction &, const Boundary &,  Viscosity_Matrix *,const Space &, const char *, int);

    void fluxParams(const FluxParams &);

    void accumulationParams(const AccumulationParams &);

    const AccumulationFunction & accumulation() const;

    const Boundary & getBoundary() const;

    virtual void boundary(const Boundary &);

    const FluxFunction & fluxFunction() const;


    Viscosity_Matrix * getViscosityMatrix() const;

    void setViscosityMatrix(Viscosity_Matrix *);

    
    HugoniotCurve * getHugoniotCurve(const string &);

    void setDoubleContactFunction(Double_Contact_Function *tif);

    Double_Contact_Function * getDoubleContactFunction();

    void setHugoniotContinuationMethod(HugoniotContinuation *);

    void setShockMethod(ShockMethod *);
    
    CompositeCurve * getCompositeCurve();

    ShockMethod * getShockMethod();

    HugoniotContinuation * getHugoniotContinuationMethod();

    const Space & domain() const;

    const char * ID() const;

    virtual SubPhysics * clone()const = 0;

    virtual Boundary * defaultBoundary()const = 0;

    const Boundary * getPreProcessedBoundary()const;

    Extension * getExtensionMethod(const string &);
    
    Secondary_Bifurcation_Interface * getSecondaryBifurcationMethod(const string &);

    virtual ~SubPhysics();

    virtual void setParams(vector<string>);

    virtual vector<double> * getParams() = 0;

    const int type() const;

    virtual void preProcess(RealVector &);
    virtual void postProcess(vector<RealVector> &);
    virtual void postProcess(RealVector &);


};



#endif //! _SubPhysics_H
