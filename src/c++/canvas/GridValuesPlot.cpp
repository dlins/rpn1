#include "GridValuesPlot.h"

GridValuesPlot::GridValuesPlot(const GridValues *g) : GraphicObject(0.0, 0.0, 0.0){
    for (int i = 0; i < g->cell_type.rows(); i++){
        for (int j = 0; j < g->cell_type.cols(); j++){
            if (g->cell_type(i, j) == CELL_IS_INVALID){
                std::vector<RealVector> v;

                v.push_back(g->grid(i, j));
                v.push_back(g->grid(i + 1, j));
                v.push_back(g->grid(i + 1, j + 1));
                v.push_back(g->grid(i, j + 1));
                v.push_back(g->grid(i, j));

                Curve2D *c = new Curve2D(v, 1.0, 0.0, 0.0);

                sons.push_back(c);
            }
        }
    }
}

GridValuesPlot::~GridValuesPlot(){
    for (int i = 0; i < sons.size(); i++) delete sons[i];
}

void GridValuesPlot::draw(){
    for (int i = 0; i < sons.size(); i++) sons[i]->draw();

    return;
}

void GridValuesPlot::transform(const double *matrix){
    for (int i = 0; i < sons.size(); i++) sons[i]->transform(matrix);

    return;
}

