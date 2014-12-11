#ifndef _POINT2D_
#define _POINT2D_

class Point2D  {
    public:
        double x, y;
        Point2D();
        Point2D(double, double);
        Point2D(const Point2D &);
        Point2D(const Point2D *);
        ~Point2D();
};

#endif // _POINT2D_

