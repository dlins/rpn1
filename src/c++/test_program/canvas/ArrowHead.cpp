#include "ArrowHead.h"

ArrowHead::ArrowHead(const std::vector<Point2D> &p, const std::vector<Point2D> &o, double rr, double gg, double bb) : GraphicObject(rr, gg, bb){
    for (int i = 0; i < o.size(); i++){
        pos.push_back(p[i]);
        pos_temp.push_back(p[i]);

        // Normalize just in case.
        double norm = sqrt(o[i].x*o[i].x + o[i].y*o[i].y);
      
        orn.push_back(Point2D(o[i].x/norm, o[i].y/norm));
        head.push_back(Point2D(p[i].x + o[i].x, p[i].y + o[i].y));
    }
}

ArrowHead::~ArrowHead(){
}

// Arrow if orientation is (1, 0), parallel to the horizon:
//
//  p2:  o
//        \
//  pos:   o
//        /
//  p3   o 
//
// p2 = (pos_x - w, pos_y + h)
// p3 = (pos_x - w, pos_y - h)
//
void ArrowHead::draw(void){
    Fl_Color current = fl_color();

    fl_color(fl_rgb_color((uchar)(255.0*r), (uchar)(255.0*g), (uchar)(255.0*b)));

    for (int i = 0; i < pos.size(); i++){
        double pos_x = fl_transform_x(pos_temp[i].x, pos_temp[i].y);
        double pos_y = fl_transform_y(pos_temp[i].x, pos_temp[i].y);

        // The angle is given by the head - pos_temp:
        //
        double head_x = fl_transform_x(head[i].x, head[i].y);
        double head_y = fl_transform_y(head[i].x, head[i].y);
        double theta = atan2(head_y - pos_y, head_x - pos_x);
        double c = cos(theta);
        double s = sin(theta);

        // Width and height of the arrow.
        int w, h;
        w = h = 10;

        // After rotating:
        // p2 = (-c*w - s*h + pos_x, -s*w + c*h + p_y)
        // p3 = (-c*w + s*h + pos_x, -s*w - c*h + p_y)

        fl_push_matrix();
            fl_begin_line();
            fl_line(-c*w - s*h + pos_x, -s*w + c*h + pos_y, pos_x, pos_y); // Segment p2-pos
            fl_line(pos_x, pos_y, -c*w + s*h + pos_x, -s*w - c*h + pos_y); // Segment pos-p3
            fl_end_line();
        fl_pop_matrix();

//            // One possible solution:
//            fl_begin_line();
//                fl_transformed_vertex(-c*w - s*h + pos_x, -s*w + c*h + pos_y);
//                fl_transformed_vertex(pos_x, pos_y); // Segment p2-pos
//                fl_transformed_vertex(-c*w + s*h + pos_x, -s*w - c*h + pos_y); // Segment pos-p3
//            fl_end_line();
    }

    fl_color(current);

    return;
}

void ArrowHead::minmax(Point2D &min, Point2D &max){
    double minx, miny, maxx, maxy;
    minx = maxx = pos[0].x; miny = maxy = pos[0].y;

    for (int i = 1; i < pos.size(); i++){
        if (pos[i].x > maxx) maxx = pos[i].x;
        if (pos[i].x < minx) minx = pos[i].x;

        if (pos[i].y > maxy) maxy = pos[i].y;
        if (pos[i].y < miny) miny = pos[i].y;
    }

    min = Point2D(minx, miny);
    max = Point2D(maxx, maxy);

    return;
}

void ArrowHead::transform(const double *matrix){
    for (int i = 0; i < pos.size(); i++){
        // The position is transformed as usual
        double x = pos[i].x;
        double y = pos[i].y;

        pos_temp[i].x = x*matrix[0] + y*matrix[1] + matrix[2];
        pos_temp[i].y = x*matrix[3] + y*matrix[4] + matrix[5];

        // The orientation is tricky. First add the original point to its orientation...
        double dx = pos[i].x + orn[i].x;
        double dy = pos[i].y + orn[i].y;

        // ...and transform it. This point will help find the angle in when draw()ing.
        head[i].x = dx*matrix[0] + dy*matrix[1] + matrix[2];
        head[i].y = dx*matrix[3] + dy*matrix[4] + matrix[5];
    }

    return;
}

