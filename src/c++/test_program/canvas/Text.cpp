#include "Text.h"

Text::Text(const std::string &s, const RealVector &p, const RealVector &sh, int r, int g, int b) : GraphicObject(r, g, b){
    string_ = s;
    pos_ = p;
    shift_in_pixels_ = sh;

    textcolor = fl_rgb_color(r*255.0, g*255.0, b*255.0);
}

Text::~Text(){
}

void Text::draw(void){
    Fl_Color c = fl_color();
    int f = fl_font();
    int s = fl_size();

    {
        fl_color(textcolor);
        fl_font(FL_HELVETICA, 14);

        int sw, sh;
        fl_measure(string_.c_str(), sw, sh);

        double xtemp, ytemp;
        xtemp = fl_transform_x(transformed_pos_(0), transformed_pos_(1));
        ytemp = fl_transform_y(transformed_pos_(0), transformed_pos_(1));

        fl_push_matrix();
            int tx = xtemp - sw/2 + shift_in_pixels_(0);
            int ty = ytemp + fl_descent() + shift_in_pixels_(1);

            fl_draw(string_.c_str(), tx, ty);
        fl_pop_matrix();
    }

    // Restore.
    //
    fl_font(f, s);
    fl_color(c);

    return;
}

void Text::minmax(Point2D &min, Point2D &max){
    int sw, sh;
    fl_measure(string_.c_str(), sw, sh);

    if (cm.rows() > 0){
        DoubleMatrix invcm = inverse(cm);

        RealVector invvec(2);
        invvec(0) = std::abs(invcm(0, 0)*(-sw/2 + shift_in_pixels_(0)) + invcm(0, 1)*(fl_descent() + shift_in_pixels_(1)));
        invvec(1) = std::abs(invcm(1, 0)*(-sw/2 + shift_in_pixels_(0)) + invcm(1, 1)*(fl_descent() + shift_in_pixels_(1)));

        min.x = transformed_pos_(0) - invvec(0);
        min.y = transformed_pos_(1) - invvec(1);

        max.x = transformed_pos_(0) + invvec(0);
        max.y = transformed_pos_(1) + invvec(1);
    }

    return;
}

void Text::transform(const double *matrix){
    DoubleMatrix m(3, 3, matrix);
    transformed_pos_ = m*pos_;

    return;
}
