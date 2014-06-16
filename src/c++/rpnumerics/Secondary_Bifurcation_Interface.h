/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) Secondary_Bifurcation_Interface.h
 */

#ifndef _Secondary_Bifurcation_Interface_H
#define _Secondary_Bifurcation_Interface_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */

#include "RealVector.h"
#include "GridValues.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


class Secondary_Bifurcation_Interface  {
protected:

    GridValues *leftGrid_;
    GridValues *rightGrid_;
    
    int edge_;

public:

    virtual int bifurcationCurve(std::vector<RealVector> &,std::vector<RealVector> &) ;

    void setLeftGrid(GridValues *);
    void setRightGrid(GridValues *);
    
    void setEdge(int );
    

};

#endif //! _Secondary_Bifurcation_Interface_H
