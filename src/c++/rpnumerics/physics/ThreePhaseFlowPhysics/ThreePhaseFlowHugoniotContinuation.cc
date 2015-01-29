#include "ThreePhaseFlowHugoniotContinuation.h"
#include "ThreePhaseFlowSubPhysics.h"


ThreePhaseFlowHugoniotContinuation::ThreePhaseFlowHugoniotContinuation(ThreePhaseFlowSubPhysics *s): HugoniotContinuation2D2D(s->flux(), s->accumulation(), s->boundary()){
    subphysics = s;

    info_ = std::string("ThreePhaseFlowHugoniotContinuation");
}

ThreePhaseFlowHugoniotContinuation::~ThreePhaseFlowHugoniotContinuation(){
}

RealVector ThreePhaseFlowHugoniotContinuation::orthogonalize(const RealVector &previous_direction, const DoubleMatrix &hyperplane){
    RealVector Hugoniot_direction(2);
    
    Hugoniot_direction(0) =  hyperplane(1, 0);
    Hugoniot_direction(1) = -hyperplane(0, 0);
    
    return Hugoniot_direction;
}

double ThreePhaseFlowHugoniotContinuation::H_water_vertex(ThreePhaseFlowHugoniotContinuation *obj, const RealVector &p){
    // Reduced permeabilities.
    //
    RealVector rp;
    obj->subphysics->permeability()->reduced_permeability(p, rp);

    // Permeabilities.
    //
    JetMatrix water;
    obj->subphysics->permeability()->PermeabilityWater_jet(p, 0, water);
    double lw = water.get(0);

    JetMatrix oil;
    obj->subphysics->permeability()->PermeabilityOil_jet(p, 0, oil);
    double lo = oil.get(0);

    JetMatrix gas;
    obj->subphysics->permeability()->PermeabilityGas_jet(p, 0, gas);
    double lg = gas.get(0);

    double vel = obj->subphysics->vel()->value();
    double grw = obj->subphysics->grw()->value();
    double gro = obj->subphysics->gro()->value();
    double grg = obj->subphysics->grg()->value();

    double rho_21 = gro - grw;
    double rho_31 = grg - grw;
    double rho_23 = gro - grg;
    double rho_32 = -rho_23;

    return rp(1)*(vel + lg*rho_23 + lw*rho_21) - rp(2)*(vel + lw*rho_31 + lo*rho_32);
}

double ThreePhaseFlowHugoniotContinuation::H_oil_vertex(ThreePhaseFlowHugoniotContinuation *obj, const RealVector &p){
    // Reduced permeabilities.
    //
    RealVector rp;
    obj->subphysics->permeability()->reduced_permeability(p, rp);

    // Permeabilities.
    //
    JetMatrix water;
    obj->subphysics->permeability()->PermeabilityWater_jet(p, 0, water);
    double lw = water.get(0);

    JetMatrix oil;
    obj->subphysics->permeability()->PermeabilityOil_jet(p, 0, oil);
    double lo = oil.get(0);

    JetMatrix gas;
    obj->subphysics->permeability()->PermeabilityGas_jet(p, 0, gas);
    double lg = gas.get(0);

    double vel = obj->subphysics->vel()->value();
    double grw = obj->subphysics->grw()->value();
    double gro = obj->subphysics->gro()->value();
    double grg = obj->subphysics->grg()->value();

    double rho_12 = grw - gro;
    double rho_13 = grw - grg;
    double rho_31 = -rho_13;
    double rho_32 = grg - gro;

    return rp(0)*(vel + lg*rho_13 + lo*rho_12) - rp(2)*(vel + lw*rho_31 + lo*rho_32);
}

double ThreePhaseFlowHugoniotContinuation::H_gas_vertex(ThreePhaseFlowHugoniotContinuation *obj, const RealVector &p){
    // Reduced permeabilities.
    //
    RealVector rp;
    obj->subphysics->permeability()->reduced_permeability(p, rp);

    // Permeabilities.
    //
    JetMatrix water;
    obj->subphysics->permeability()->PermeabilityWater_jet(p, 0, water);
    double lw = water.get(0);

    JetMatrix oil;
    obj->subphysics->permeability()->PermeabilityOil_jet(p, 0, oil);
    double lo = oil.get(0);

    JetMatrix gas;
    obj->subphysics->permeability()->PermeabilityGas_jet(p, 0, gas);
    double lg = gas.get(0);

    double vel = obj->subphysics->vel()->value();
    double grw = obj->subphysics->grw()->value();
    double gro = obj->subphysics->gro()->value();
    double grg = obj->subphysics->grg()->value();

    double rho_12 = grw - gro;
    double rho_21 = -rho_12;
    double rho_13 = grw - grg;
    double rho_23 = gro - grg; // rho23 *

    return rp(1)*(vel + lg*rho_23 + lw*rho_21) - rp(0)*(vel + lg*rho_13 + lo*rho_12);
}

//void ThreePhaseFlowHugoniotContinuation::jet_Hugoniot(const RealVector &F, const DoubleMatrix &JF, 
//                                                      const RealVector &G, const DoubleMatrix &JG, 
//                                                      const RealVector &C, const DoubleMatrix &JC, 
//                                                      RealVector &H, DoubleMatrix &nablaH){

//    RealVector diff_F = F - ref.F;
//    RealVector diff_G = G - ref.G;

//    H.resize(1);
//    H(0) = diff_F(0)*diff_G(1) - diff_F(1)*diff_G(0);

//    // nablaH = 2 x 1
//    nablaH.resize(2, 1);
//    for (int j = 0; j < 2; j++) nablaH(j, 0) = JF(0, j)*diff_G(1) + JG(1, j)*diff_F(0) - JF(1, j)*diff_G(0) - JG(0, j)*diff_F(1);

//    return;
//}

int ThreePhaseFlowHugoniotContinuation::fill_hyperplane(const RealVector &origin, DoubleMatrix &hyperplane){
    int info;

    double max_distance_to_contact_region_ = 5e-3;
    if (subphysics->distance_to_contact_region(origin) < max_distance_to_contact_region_){
        hyperplane.resize(2, 1);

        double sw = origin(0);
        double so = origin(1);

        double cnw = subphysics->cnw()->value();
        double cno = subphysics->cno()->value();

        if (std::abs(sw - cnw) < max_distance_to_contact_region_){
            hyperplane(0, 0) = 0.0;
            hyperplane(1, 0) = 1.0;

//            std::cout << "ThreePhaseFlowHugoniotContinuation filled hyperplane cnw." << std::endl;
        }
        else {
            hyperplane(0, 0) = 1.0;
            hyperplane(1, 0) = 0.0;

//            std::cout << "ThreePhaseFlowHugoniotContinuation filled hyperplane cno." << std::endl;
        }

        info = HUGONIOTCONTINUATION_HYPERPLANE_OK;
    }
    else info = HugoniotContinuation2D2D::fill_hyperplane(origin, hyperplane);

    return info;
}

