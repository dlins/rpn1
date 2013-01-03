/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) Stone.h
 */

#ifndef _Stone_H
#define _Stone_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "SubPhysics.h"
#include "IsoTriang2DBoundary.h"
#include "StoneParams.h"
#include "Multid.h"
#include "StoneFluxFunction.h"
#include "StoneHugoniotFunctionClass.h"
#include "StoneAccumulation.h"
#include "ShockMethod.h"
#include "Hugoniot_Curve.h"
#include "Double_Contact.h"
#include "Shock.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


//TODO  Fisicas Corey e Triphase removidas . Pablo irá reprograma-las , se necessario


class Stone:public SubPhysics {

private:


    
public:


    Stone(const Stone &);

    Stone();

    virtual ~Stone();

    Boundary * defaultBoundary()const;

    void setParams(vector<string>);
    
    vector<double>  * getParams();


    SubPhysics * clone()const;

};




#endif //! _Stone_H
