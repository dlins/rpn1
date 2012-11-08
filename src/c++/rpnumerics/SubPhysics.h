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

/*
 * ---------------------------------------------------------------
 * Definitions:
 */



class SubPhysics {
private:


    Hugoniot_Locus * hugoniotFunction_;
    Double_Contact_Function * doubleContactFunction_;
    Boundary * boundary_;
    Space * space_;
    const char * ID_;
    int type_;

protected:

    FluxFunction * fluxFunction_;
    AccumulationFunction * accumulationFunction_;

public:


    SubPhysics(const Boundary &, const Space &, const char *, int);

    SubPhysics(const FluxFunction &, const AccumulationFunction &, const Boundary &, const Space &, const char *, int);

    void fluxParams(const FluxParams &);

    void accumulationParams(const AccumulationParams &);

    const AccumulationFunction & accumulation() const;

    const Boundary & boundary() const;

    void boundary(const Boundary &);

    const FluxFunction & fluxFunction() const;

    Hugoniot_Locus * getHugoniotFunction() const;

    void setHugoniotFunction(Hugoniot_Locus *);
    
    void setDoubleContactFunction(Double_Contact_Function *tif);
    
    Double_Contact_Function * getDoubleContactFunction();

    const Space & domain() const;

    const char * ID() const;

    virtual SubPhysics * clone()const = 0;

    virtual Boundary * defaultBoundary()const = 0;
    
    virtual const Boundary * getPreProcessedBoundary()const ;

    virtual ~SubPhysics();

    virtual void setParams(vector<string>);

    const int type() const;

    virtual void preProcess(RealVector &);
    virtual void postProcess(vector<RealVector> &);
    virtual void postProcess(RealVector &);


};



#endif //! _SubPhysics_H
