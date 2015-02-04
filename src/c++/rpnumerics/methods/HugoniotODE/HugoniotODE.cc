#include "HugoniotODE.h"

HugoniotODE::HugoniotODE(SubPhysics *s, ODE_Solver *ode): HugoniotCurve(s->flux(), s->accumulation(), s->boundary(), s){
    odesolver = ode;

    info_ = std::string("HugoniotODE");
}

HugoniotODE::~HugoniotODE(){
}

void HugoniotODE::list_of_reference_points(std::vector<int> &type, std::vector<std::string> &name) const {
    type.clear();
    type.push_back(HUGONIOTODE_GENERIC_POINT);

    name.clear();
    name.push_back(std::string("Generic point"));

    return;
}

int HugoniotODE::field(int *neq, double *xi, double *in, double *out, int *obj, double* /* Not used */){
    HugoniotODE *hode = (HugoniotODE*)obj;
     
    RealVector p(*neq, in);
    JetMatrix Fjet, Gjet;
    hode->f->jet(p, Fjet, 1);
    hode->a->jet(p, Gjet, 1);

    RealVector H;
    DoubleMatrix nablaH;

//    std::cout << Fjet << std::endl;
//    std::cout << Gjet << std::endl;

    hode->hug->jet_Hugoniot(Fjet.function(), Fjet.Jacobian(), 
                            Gjet.function(), Gjet.Jacobian(), 
                            H, nablaH);

    RealVector nablaH_vec(2);
    for (int i = 0; i < 2; i++) nablaH_vec(i) = nablaH(i, 0);
    normalize(nablaH_vec);

//    std::cout << nablaH << std::endl;
//    std::cout << nablaH_vec << std::endl;
//    TestTools::pause();

    if (nablaH_vec*hode->refvec < 0.0) nablaH_vec = -nablaH_vec;

    for (int i = 0; i < 2; i++) out[i] = nablaH_vec(i);

//    std::cout << "Field: " << nablaH_vec << std::endl;

    return FIELD_OK;   
}

void HugoniotODE::add_point_to_curve(const RealVector &p, Curve &curve){
//    curve.back_pointer.push_back(curve.back_pointer.size() - 1);

    curve.curve.push_back(p);

//    RealVector point_eigenvalues;
//    all_eigenvalues(p, family, point_eigenvalues);

//    std::cout.precision(32);
//    std::cout << point_eigenvalues << std::endl;

//    curve.eigenvalues.push_back(point_eigenvalues);

//    curve.speed.push_back(point_eigenvalues(family));

    return;
}

void HugoniotODE::curve_engine(const RealVector &initial_point, const RealVector &rv, Curve &c){
    refvec = rv;
//    std::cout << "refvec = " << refvec << std::endl;

    RealVector point(initial_point), next_point;

    double xi = 0.0;
    double delta_xi = 1e-3;

    double next_xi = xi + delta_xi;

    while (true){
        int info_odesolver = odesolver->integrate_step(&HugoniotODE::field, 
                                                       0,
                                                       (int*)this, 0 /*double *function_data*/,
                                                       xi,      point,
                                                       next_xi, next_point);

//        if (info_odesolver == ODE_SOLVER_ERROR){
//            std::cout << "RarefactionCurve: The solver failed to find the next point (Error = " << info_odesolver << "). Aborting..." << std::endl; 

//            return RAREFACTION_ERROR;
//        }

//        if (info_odesolver == ODE_SOLVER_POINT_OUTSIDE_DOMAIN){
//            std::cout << "RarefactionCurve: The solver passed a point outside the domain to the field (Error = " << info_odesolver << "). Aborting..." << std::endl; 

//            return RAREFACTION_ERROR;
//        }

        // Has the rarefacion curve reached the boundary?
        //
        RealVector intersection_point;
        int edge;
        int info_intersect = boundary->intersection(next_point, point, intersection_point, edge);

        if (info_intersect == BOUNDARY_INTERSECTION_FOUND){
//            // Add the point, etc.
//            //
//            add_point_to_curve(intersection_point, rarcurve);

//            // The final direction is not needed, but will be added only for completeness.
//            //
//            final_direction = intersection_point - next_point;
//            normalize(final_direction);
//            rarcurve.final_direction = final_direction;

//            rarcurve.last_point = intersection_point;

//            reason_why = RAREFACTION_REACHED_BOUNDARY;
//            rarcurve.reason_to_stop = RAREFACTION_REACHED_BOUNDARY;

            return;
        }

        add_point_to_curve(next_point, c);

        // Update the reference vector.
        //
        refvec = next_point - point;
        point = next_point;

        // Update the independent parameter.
        //
        xi = next_xi;
        next_xi += delta_xi;
    }
}

void HugoniotODE::curve(const ReferencePoint &ref, int type, std::vector<Curve> &c){
    c.clear();

    hug = subphysics->Hugoniot_continuation();
    hug->set_reference_point(ref);

    int n = ref.point.size();
    double epsilon = 1e-2;

    for (int family = 0; family < subphysics->number_of_families(); family++){
        RealVector r(n);
        for (int i = 0; i < n; i++) r(i) = ref.e[family].vrr[i];

        std::vector<RealVector> reference_vector;
        reference_vector.push_back(r);
        reference_vector.push_back(-r);

        for (int i = 0; i < reference_vector.size(); i++){
            Curve ctemp;
            curve_engine(ref.point + epsilon*reference_vector[i], reference_vector[i], ctemp); // Generalize later (initial_point != ref.point)

            c.push_back(ctemp);

//            std::cout << ref.point + epsilon*reference_vector[i] << std::endl;
        }
    }

//    TestTools::pause();

    return;
}

