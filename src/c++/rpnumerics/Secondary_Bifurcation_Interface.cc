/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) Secondary_Bifurcation_Interface.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "Secondary_Bifurcation_Interface.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


void Secondary_Bifurcation_Interface::setLeftGrid(GridValues * grid){
    
    leftGrid_=grid;
    
}


void Secondary_Bifurcation_Interface::setRightGrid(GridValues * grid){
    rightGrid_=grid;
}

void Secondary_Bifurcation_Interface::setEdge(int edge){
    edge_=edge;
}

 int Secondary_Bifurcation_Interface::bifurcationCurve(std::vector<RealVector> &,std::vector<RealVector> &) {
    
}