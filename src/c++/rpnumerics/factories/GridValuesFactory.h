/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) GridValuesFactory.h
 */

#ifndef _GridValuesFactory_H
#define _GridValuesFactory_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include <map>

#include "GridValues.h"
//#include "RpNumerics.h"
#include "Physics.h"
#include <map>
/*
 * ---------------------------------------------------------------
 * Definitions:
 */


class GridValuesFactory {

private:

    std::map<string , GridValues *> * gridArray_;


    const Physics * physics_;


    vector<int> getDefaultGridResolution(const string &);


public:


    GridValues * getGrid(const string &);
    void setResolution(const string &,vector<int>);
    GridValuesFactory(const Physics *);

};

#endif //! _GridValuesFactory_H
