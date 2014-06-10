#include "Bisection.h"
#include <iostream>

// Given a function f, and an interval [a, b] (or [b, a]), such that f(a)*f(b) < 0.0, this
// method finds a value c in the interval, such that f(c) = 0.0, approximately,
// by means of the bisection method.
//

int Bisection::bisection_method(const double &t_in,  const RealVector &p_in,
                                const double &t_fin, const RealVector &p_fin,
                                double epsilon, 
                                double &c_t, RealVector &p_c,
                                //int (*f)(const RealVector &x, RealVector &y, int *f_o, int *f_d), int *function_object, int *function_data,
                                int (*ode_flux)(int*, double*, double*, double*, int*, double*), int *function_object, double *function_data,
//                                int (*ode_solver)(int (*field)(const RealVector &, RealVector &, int *, int *), int * /*function_object*/, int * /*function_data*/, 
                                const ODE_Solver *odesolver,
                                //int (*ode_solver)(int (*field)(int*, double*, double*, double*, int*, double*), int * /*function_object*/, double * /*function_data*/,
                                //                  const double init_time,  const RealVector &init_point,  
                                //                  const double final_time,       RealVector &final_point,
                                //                  int *, int *), int *ode_solver_object, int *ode_solver_data,
                                int (*signal_event)(const RealVector & where, double & event_measure, int *seo, int *sed), int *signal_event_object, int *signal_event_data){

    double a_t(t_in), b_t(t_fin);
    RealVector p_a(p_in), p_b(p_fin);

    double f_a;
    int info_f_a = (*signal_event)(p_a, f_a, signal_event_object, signal_event_data);

    double f_b;
    int info_f_b = (*signal_event)(p_b, f_b, signal_event_object, signal_event_data);

//    std:://cout << "Bisection. After signal_event." << std::endl;

    if (info_f_a == BISECTION_FUNCTION_ERROR || 
        info_f_b == BISECTION_FUNCTION_ERROR){
        c_t = t_fin;
        p_c = p_fin;

        //cout << "c_t = " << c_t << std::endl;
        //cout << "p_c = " << p_c << std::endl;

        return BISECTION_FUNCTION_ERROR;
    }

    if (f_a*f_b > 0.0) {
        c_t = t_fin;
        p_c = p_fin;

        //cout << "c_t = " << c_t << std::endl;
        //cout << "p_c = " << p_c << std::endl;
        //cout << "Bisection, equal sign: f_a = " << f_a << ", f_b = " << f_b << std::endl;


        return BISECTION_EQUAL_SIGN;
    }

    // TODO: This is an extremely ugly hack. As a matter of fact, the need for the following 
    //       lines makes the switch to a Newton method desirable.
    //       
    if (f_a == 0.0){
        c_t = t_in;
        p_c = p_in;

        return BISECTION_CONVERGENCE_OK;
    }
    else if (f_b == 0.0){
        c_t = t_fin;
        p_c = p_fin;

        return BISECTION_CONVERGENCE_OK;
    }

    //cout << "Bisection. Proceed with the iterations." << std::endl;

    double tol = epsilon*(std::abs(t_fin - t_in));
    int max_count = 50;
    int count = 1;

    double c_old(a_t);
    c_t = b_t;

    int original_precision = std::cout.precision();
    std::cout.precision(10);

    while (fabs(c_old - c_t) > tol && count <= max_count){
//        std:://cout << "c_old = " << c_old << ", c = " << c << ", fabs(c_old - c) = " << fabs(c_old - c) << std::endl;
//        std:://cout << "    a = " << a << ",f(a) = " << f_a << ", b = " << b << ", f(b) = " << f_b<< std::endl << std::endl;

        c_old = c_t;

//        double alpha = f_b/(f_b - f_a); 
        double alpha = 0.5;
        c_t = alpha*a_t + (1.0 - alpha)*b_t;

        //int info_solver = (*ode_solver)(ode_flux, function_object, function_data, 
        //                                a_t, p_a,  
        //                                c_t, p_c,
        //                                ode_solver_object, ode_solver_data);
        int info_solver = odesolver->integrate_step(ode_flux, function_object, function_data, 
                                                    a_t, p_a,  
                                                    c_t, p_c/*,
                                                    ode_solver_object, ode_solver_data*/);

        if (info_solver == BISECTION_CONVERGENCE_ERROR) return BISECTION_CONVERGENCE_ERROR;

        double f_c;
        int info_f_c = (*signal_event)(p_c, f_c, signal_event_object, signal_event_data);

        if (info_f_c == BISECTION_FUNCTION_ERROR) return BISECTION_FUNCTION_ERROR;

        if (f_c*f_b >= 0.0){
            b_t = c_t;
            f_b = f_c;
            p_b = p_c;
        }
        else {
            a_t = c_t;
            f_a = f_c;
            p_a = p_c;
        }

        count++;

        //cout << "Bisection. It. = " << count << ", p_a = " << p_a << ", p_c = " << p_c << ", p_b = "  << p_b << std::endl;
    }

    //cout << "RF. count = " << count << std::endl;

    // Biased to the right.
    p_c = p_b;
    c_t = b_t;

    std::cout.precision(original_precision);

    if (count > max_count) return BISECTION_CONVERGENCE_ERROR;
    else                   return BISECTION_CONVERGENCE_OK;
}

