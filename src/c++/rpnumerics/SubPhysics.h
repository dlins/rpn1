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
#include "HugoniotFunctionClass.h"
#include "Boundary.h"
#include "Space.h"
#include  "Multid.h"
#include "eigen.h"
#include "Hugoniot_Locus.h"
#include "ThreeImplicitFunctions.h"
#include "Double_Contact_Function.h"
#include "methods/ShockMethod.h"
#include <sstream>

/*
 * ---------------------------------------------------------------
 * Definitions:
 */



class SubPhysics {
private:


    Hugoniot_Locus * hugoniotFunction_;
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

    Hugoniot_Locus * getHugoniotFunction() const;
    
    Viscosity_Matrix * getViscosityMatrix() const;
    
    void setViscosityMatrix(Viscosity_Matrix *);

    void setHugoniotFunction(Hugoniot_Locus *);
    
    void setDoubleContactFunction(Double_Contact_Function *tif);
    
    Double_Contact_Function * getDoubleContactFunction();
    
    void setShockMethod(ShockMethod *);
    
    ShockMethod * getShockMethod();

    const Space & domain() const;

    const char * ID() const;

    virtual SubPhysics * clone()const = 0;

    virtual Boundary * defaultBoundary()const = 0;
    
    const Boundary * getPreProcessedBoundary()const ;

    virtual ~SubPhysics();

    virtual void setParams(vector<string>);
    
    virtual vector<double> * getParams()=0;

    const int type() const;

    virtual void preProcess(RealVector &);
    virtual void postProcess(vector<RealVector> &);
    virtual void postProcess(RealVector &);


};



#endif //! _SubPhysics_H
