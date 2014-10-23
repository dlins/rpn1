#include "TrivialTestSubPhysics.h"

TrivialTestSubPhysics::TrivialTestSubPhysics() : SubPhysics() {
    flux_         = new TrivialTestFluxFunction;
    accumulation_ = new StoneAccumulation;
    
    RealVector pmin(2), pmax(2);
    pmin(0) = pmin(1) = 0.0;
    pmax(0) = pmax(1) = 1.0;
    
    boundary_ = new RectBoundary(pmin, pmax);
    
    std::vector<int> number_of_cells(2);
    number_of_cells[0] = 5;
    number_of_cells[1] = 5;
    gridvalues_ = new GridValues(boundary_, pmin, pmax, number_of_cells);
}

TrivialTestSubPhysics::~TrivialTestSubPhysics(){
    delete gridvalues_;
    delete boundary_;
    delete accumulation_;
    delete flux_;
}

void TrivialTestSubPhysics::shock_cases(std::vector<int> &type, std::vector<std::string> &name) const {
    type.clear();
    name.clear();
    
    return;
}
