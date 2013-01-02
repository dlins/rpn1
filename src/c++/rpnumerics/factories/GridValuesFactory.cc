/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) GridValuesFactory.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "GridValuesFactory.h"
#include <stdexcept>

/*
 * ---------------------------------------------------------------
 * Definitions:
 */




GridValuesFactory::GridValuesFactory(const Physics * physics) : gridArray_(new map<string, GridValues *>()), physics_(physics) {

}

GridValues * GridValuesFactory::getGrid(const string & gridName) {


    
    if (gridArray_->count(gridName) == 1) {
        return gridArray_->operator [](gridName);
    } else {
    cout <<"Nome do grid:"<<gridName<<endl;
    cout <<"Nome da subfisica :"<<physics_->getSubPhysics(0).ID()<<endl;
        const Boundary* boundary = physics_->getSubPhysics(0).getPreProcessedBoundary();

        cout << "Min: " << boundary->minimums() << endl;
        cout << "Max: " << boundary->maximums() << endl;

        cout << "Parametros flux: em gv " << physics_->getSubPhysics(0).fluxFunction().fluxParams().params() << endl;
        cout << "Parametros accum em gv: " << physics_->getSubPhysics(0).accumulation().accumulationParams().params() << endl;

        vector<int> noc = getDefaultGridResolution(gridName);

        GridValues * returnedGrid = new GridValues(boundary, boundary->minimums(), boundary->maximums(), noc);


        gridArray_->operator [](gridName) = returnedGrid;

        cout << "Criando grid values " << gridName << endl;


        return returnedGrid;
    }


}

void GridValuesFactory::setResolution(const string & gridName, vector<int> newResolution) {

    GridValues * grid = getGrid(gridName);

    //    const Boundary* boundary = &physics_->boundary();

    const Boundary* boundary = physics_->getSubPhysics(0).getPreProcessedBoundary();

    grid->set_grid(boundary, boundary->minimums(), boundary->maximums(), newResolution);

}

void GridValuesFactory::updateGrids() {

    map<string, GridValues *>::iterator it;


    vector<int> resolution;

    //      const Boundary* boundary = &physics_->boundary();


    const Boundary* boundary = physics_->getSubPhysics(0).getPreProcessedBoundary();

    for (it = gridArray_->begin(); it != gridArray_->end(); it++) {

        GridValues * factoryElement = (*it).second;

        resolution.clear();

        resolution.push_back(factoryElement->grid.rows());
        resolution.push_back(factoryElement->grid.cols());

        factoryElement->set_grid(boundary, boundary->minimums(), boundary->maximums(), resolution);

    }


}

vector<int> GridValuesFactory::getDefaultGridResolution(const string & gridName) {


    std::vector<int> noc(2);

    noc[0] = 30;
    noc[1] = 30;


    if (gridName.compare("hugoniotcurve") == 0) {
        noc[0] = 250;
        noc[1] = 250;
    }

    if (gridName.compare("doublecontactcurve") == 0) {
        noc[0] = 30;
        noc[1] = 30;
    }

    if (gridName.compare("bifurcation") == 0) {
        noc[0] = 128;
        noc[1] = 128;
    }
    return noc;


}






