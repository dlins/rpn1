#include "SimplePolarPlot.h"
#include "Debug.h"

//void SimplePolarPlot::curve(const Boundary *b, 
//                            void (*polarfunc)(double theta, RealVector &out), 
//                            const RealVector &polar_domain, double max_delta_theta, 
//                            std::vector<std::vector<RealVector> > &out){

//    out.clear();

//    // Maximum number of divisions of the domain
//    //
//    int n_max = (int)(    1.0 + (polar_domain.component(1) - polar_domain.component(0))/max_delta_theta    );
//    int pos = 0;

//    // Regular increment of the parameter
//    //
//    double delta_theta = (polar_domain.component(1) - polar_domain.component(0))/(double)n_max;

//    // Parameter
//    //
//    double theta = polar_domain.component(0);
//    double theta0 = polar_domain.component(0);

//    // TODO: Decide what to do with curves which first and last points are the same, or not.
//    //
//    while (pos < n_max){
//        std::vector<RealVector> temp;
//        bool go_on = true;

//        while(go_on){
//            RealVector xy(2);
//            (*polarfunc)(theta, xy);
//            temp.push_back(xy);

//            theta = theta0 + pos*delta_theta;
//            pos++;

//            //if (theta >= polar_domain.component(1)) go_on = false;
//            if (pos > n_max) go_on = false;
//        }

//        out.push_back(temp);
//    }

//    return;
//}

void SimplePolarPlot::curve(void *Object,
                            const Boundary *boundary, 
                            void (*polarfunc)(void *o, double theta, RealVector &out), 
                            const RealVector &polar_domain, int n_max, 
                            std::vector<std::deque<RealVector> > &out){

    out.clear();

    // Maximum number of divisions of the domain
    //
//    int n_max = (int)(    1.0 + (polar_domain.component(1) - polar_domain.component(0))/max_delta_theta    );
    int pos = 0;

    // Regular increment of the parameter
    //
    double delta_theta = (polar_domain.component(1) - polar_domain.component(0))/(double)n_max;

    // Parameter
    //
    double theta = polar_domain.component(0);
    double theta0 = polar_domain.component(0);

    // TODO: Decide what to do with curves which first and last points are the same, or not.
    //
    while (pos < n_max){
        // Find a point inside the domain
        RealVector point(2), old_point(2);
        bool first_point_found = false;        

        while (!first_point_found && pos < n_max){
            (*polarfunc)(Object, theta, point);

            first_point_found = boundary->inside(point);
            if ( Debug::get_debug_level() == 5 ) {
                printf("First. pos = %d, theta = %g, found = %d\n", pos, theta, first_point_found);
            }

            if (!first_point_found){
                theta = theta0 + pos*delta_theta;
                pos++;
                old_point = point;
            }
        }

        // Leave if no point is found inside this domain for the given theta as initial angle...
        //
        if (!first_point_found) break;

        // ...otherwise find all the points that lie on the border and inside the domain.
        //
        std::deque<RealVector> temp;
        RealVector border_point;
        int edge;

        if (pos > 0){
            boundary->intersection(point, old_point, border_point, edge);

            temp.push_back(border_point);
            temp.push_back(point);
        }

        bool go_on = true;

        while (go_on){
            theta = theta0 + pos*delta_theta;
            pos++;
//            if (pos > n_max) go_on = false;

            (*polarfunc)(Object, theta, point);

            go_on = boundary->inside(point) && (pos < n_max); 

            if (go_on){
                temp.push_back(point);
                old_point = point;
            }
            else {
                boundary->intersection(point, old_point, border_point, edge);
                temp.push_back(border_point);
                break;
            }

//            theta = theta0 + pos*delta_theta;
//            pos++;

            //if (theta >= polar_domain.component(1)) go_on = false;
//            if (pos > n_max) go_on = false;
        }

        out.push_back(temp);
    }

    return;
}

void SimplePolarPlot::simple_curve(void *Object,
                                   const Boundary *b,
                                   void (*polarfunc)(void *o, double theta, RealVector &out), 
                                   std::vector<std::deque<RealVector> > &out){
    RealVector polar_domain(2);
    polar_domain.component(0) = 0.0;
    polar_domain.component(1) = TWOPI;

//    polar_domain.component(0) = -PI/2.0;
//    polar_domain.component(1) =  PI/2.0;

    curve(Object, b, polarfunc, polar_domain, 1000, out);
    return;
}

void SimplePolarPlot::periodic_curve(void *Object, 
                                     const Boundary *boundary,
                                     void (*polarfunc)(void *o, double theta, double &num, double &den),
                                     double theta0, double period, int n_max,
                                     std::vector<std::deque<RealVector> > &out){

    out.clear();

    int pos = 0;

    // Parameter
    //
    double theta = theta0;

    // Regular increment of the parameter
    //
    double delta_theta = fabs(period)/(double)n_max;

    // Numerator and denominator to be used by the polar function.
    //
    double num, den;

    // TODO: Decide what to do with curves which first and last points are the same, or not.
    //
    while (pos < n_max){
        // Find a point inside the domain
        RealVector point(2), old_point(2);
        bool first_point_found = false;        

        while (!first_point_found && pos < n_max){

            (*polarfunc)(Object, theta, num, den);
            point.component(0) = (num/den)*cos(theta);
            point.component(1) = (num/den)*sin(theta);

            first_point_found = boundary->inside(point);
            if ( Debug::get_debug_level() == 5 ) {
                printf("First. pos = %d, theta = %g, found = %d\n", pos, theta, first_point_found);
            }

            if (!first_point_found){
                theta = theta0 + pos*delta_theta;
                pos++;
                old_point = point;
            }
        }

        // Leave if no point is found inside this domain for the given theta as initial angle...
        //
        if (!first_point_found) break;

        // ...otherwise find all the points that lie on the border and inside the domain.
        //
        std::deque<RealVector> temp;
        RealVector border_point;
        int edge;

        if (pos > 0){
            boundary->intersection(point, old_point, border_point, edge);

            temp.push_back(border_point);
            temp.push_back(point);
        }

        bool go_on = true;

        while (go_on){
            theta = theta0 + pos*delta_theta;
            pos++;
//            if (pos > n_max) go_on = false;

            (*polarfunc)(Object, theta, num, den);
            point.component(0) = (num/den)*cos(theta);
            point.component(1) = (num/den)*sin(theta);

            go_on = boundary->inside(point) && (pos < n_max); 

            if (go_on){
                temp.push_back(point);
                old_point = point;
            }
            else {
                boundary->intersection(point, old_point, border_point, edge);
                temp.push_back(border_point);
                break;
            }

//            theta = theta0 + pos*delta_theta;
//            pos++;

            //if (theta >= polar_domain.component(1)) go_on = false;
//            if (pos > n_max) go_on = false;
        }

        out.push_back(temp);
    }

    return;
}

