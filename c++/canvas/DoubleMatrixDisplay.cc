#include "DoubleMatrixDisplay.h"

DoubleMatrixDisplay::DoubleMatrixDisplay(DoubleMatrix *d) : GraphicObject(0, 0, 0), dm(d){
    mincolor  = FL_RED;
    zerocolor = FL_WHITE;
    maxcolor  = FL_BLUE;
}

DoubleMatrixDisplay::~DoubleMatrixDisplay(){
}
    
void DoubleMatrixDisplay::extremes(double &inferior, double &superior){
    inferior =  std::numeric_limits<double>::max();
    superior = -std::numeric_limits<double>::max();
    
    for (int i = 0; i < dm->rows(); i++){
        for (int j = 0; j < dm->cols(); j++){
            if (dm->operator()(i, j) < inferior) inferior = dm->operator()(i, j);
            if (dm->operator()(i, j) > superior) superior = dm->operator()(i, j);
        }
    }
    
    return;
}
        
void DoubleMatrixDisplay::draw(){
    double inferior, superior;
    extremes(inferior, superior);
    
    Fl_Color current = fl_color();
    
    for (int i = 0; i < dm->rows(); i++){
        for (int j = 0; j < dm->cols(); j++){
            double v = dm->operator()(i, j);

            double weight;
            Fl_Color c;

            if (v > 0){
                weight = (v - superior)/(0.0 - superior);
                c = fl_color_average(zerocolor, maxcolor, weight);
            }
            else {
                weight = (v - inferior)/(0.0 - inferior);
                c = fl_color_average(zerocolor, mincolor, weight);
            }

//            double weight = (dm->operator()(i, j) - superior)/(inferior - superior);
//            Fl_Color c = fl_color_average(mincolor, maxcolor, weight);

            fl_color(c);
            
             fl_begin_polygon();
                 fl_vertex(j,     dm->rows() - i);
                 fl_vertex(j + 1, dm->rows() - i);
                 fl_vertex(j + 1, dm->rows() - (i + 1));
                 fl_vertex(j,     dm->rows() - (i + 1));
             fl_end_polygon();
        }
    }
    
    fl_color(current);
    
    return;
}

void DoubleMatrixDisplay::minmax(Point2D &pmin, Point2D &pmax){
    pmin.x = pmin.y = 0.0;
    
    pmax.x = dm->cols();
    pmax.y = dm->rows();
    
    return;
}
