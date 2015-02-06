#include "ThreePhaseFlowSubPhysics.h"

ThreePhaseFlowSubPhysics::ThreePhaseFlowSubPhysics() : SubPhysics() {
    number_of_families_ = 2;

    boundary_ = new Three_Phase_Boundary;

    G_vertex.resize(2);
    G_vertex(0) = 0.0;
    G_vertex(1) = 0.0;

    W_vertex.resize(2);
    W_vertex(0) = 1.0;
    W_vertex(1) = 0.0;

    O_vertex.resize(2);
    O_vertex(0) = 0.0;
    O_vertex(1) = 1.0;

    info_subphysics_ = std::string("ThreePhaseFlow base class");

    label_.push_back(std::string("sw"));
    label_.push_back(std::string("so"));

    // Extension curve.
    //
    iec = new Implicit_Extension_Curve;
    extension_curve.push_back(iec);

    // Flux and accumulation will be instantiated in the derived classes.
    //
    flux_ = 0;
    accumulation_ = new ThreePhaseFlowAccumulation;

    // HugoniotContinuation will be instantiated in the derived classes because of the flux and accumulation.
    //
    hugoniotcontinuation_ = 0;

    // The permeability will be instantiated in the derived classes.
    //
    permeability_ = 0;

    // The viscosity will be instantiated in the derived classes.
    //
    viscosity_ = 0;

    // Double_Contact.
    //
    doublecontact_ = new Double_Contact;

    // Projection matrix.
    //
    double m[9] = {1.0, .5, 0.0, 0.0, sqrt(3)/2, 0.0, 0.0, 0.0, 1.0};
    transformation_matrix_ = DoubleMatrix(3, 3, m);

//    transformation_matrix_ = DoubleMatrix::eye(3);
}

ThreePhaseFlowSubPhysics::~ThreePhaseFlowSubPhysics(){
    delete doublecontact_;

    if (accumulation_ != 0) delete accumulation_;
    delete iec;
    delete boundary_;

}

//bool ThreePhaseFlowSubPhysics::inside_contact_region(const RealVector &p, int family){
//    if (family == 0) {
//        if (p(0) < cnw_parameter->value() ||
//            p(1) < cno_parameter->value() ||
//            (1.0 - p(0) - p(1)) < cng_parameter->value()) return true;
//    }
//    else if (family == 1) {
//        if (p(0) < cnw_parameter->value() && 
//            p(1) < cno_parameter->value()) return true;
//    }

//    return false;
//}

//double ThreePhaseFlowSubPhysics::distance_to_contact_region(const RealVector &p){
//    RealVector cn(3);
//    cn(0) = cnw_parameter->value();
//    cn(1) = cno_parameter->value();
//    cn(2) = cng_parameter->value();

//    RealVector paug(3);
//    paug(0) = p(0);
//    paug(1) = p(1);
//    paug(2) = 1.0 - p(0) - p(1);

//    RealVector res = paug - cn;
//    double min_distance = std::numeric_limits<double>::infinity();

//    for (int i = 0; i < 3; i++){
//        if (res(i) < 0.0) return 0.0;

//        min_distance = std::min(min_distance, res(i));
//    }

//    return min_distance;
//}

