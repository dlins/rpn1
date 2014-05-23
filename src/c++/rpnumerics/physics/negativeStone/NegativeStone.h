/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) NegativeStone.h
 */

#ifndef _NegativeStone_H
#define _NegativeStone_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "SubPhysics.h"
#include "IsoTriang2DBoundary.h"
#include "StoneParams.h"
#include "Multid.h"
#include "StoneNegativeFluxFunction.h"
#include "StoneHugoniot.h"
#include "StoneAccumulation.h"
#include "HugoniotContinuation2D2D.h"
#include "Hugoniot_Curve.h"
#include "Double_Contact.h"
//#include "Shock.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


//TODO  Fisicas Corey e Triphase removidas . Pablo irá reprograma-las , se necessario


class NegativeStone:public SubPhysics {

private:


    
public:


    NegativeStone(const NegativeStone &);

    NegativeStone();

    virtual ~NegativeStone();

    Boundary * defaultBoundary()const;

    void setParams(vector<string>);
    
    vector<double>  * getParams();


    SubPhysics * clone()const;

};




#endif //! _NegativeStone_H
