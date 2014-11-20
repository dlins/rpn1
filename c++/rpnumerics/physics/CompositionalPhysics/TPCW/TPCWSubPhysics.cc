#include "TPCWSubPhysics.h"

TPCWSubPhysics::TPCWSubPhysics(){
    P_parameter = new Parameter(std::string("P"), 100.900000e5);
    equation_parameter_.push_back(P_parameter);

//    mc_parameter = new Parameter(std::string("mc"), 0.044);
//    mw_parameter = new Parameter(std::string("mw"), 0.018);
//    equation_parameter_.push_back(mc_parameter);
//    equation_parameter_.push_back(mw_parameter);

    // Molecular weights.
    //
    mc = 0.044;
    mw = 0.018;

    // Flux parameters.
    //
    abs_perm_parameter = new Parameter(std::string("abs_perm"), 3e-12);
    sin_beta_parameter = new Parameter(std::string("sin_beta"), 0.0);
    cnw_parameter      = new Parameter(std::string("cnw"), 0.0);
    cng_parameter      = new Parameter(std::string("cng"), 0.0);
    expw_parameter     = new Parameter(std::string("expw"), 2.0);
    expg_parameter     = new Parameter(std::string("expg"), 2.0);

    equation_parameter_.push_back(abs_perm_parameter);
    equation_parameter_.push_back(sin_beta_parameter);
    equation_parameter_.push_back(cnw_parameter);
    equation_parameter_.push_back(cng_parameter);
    equation_parameter_.push_back(expw_parameter);
    equation_parameter_.push_back(expg_parameter);

    // Accumulation parameter.
    //
    phi_parameter = new Parameter(std::string("phi"), 0.38);
    equation_parameter_.push_back(phi_parameter);

    double const_gravity = 9.8;
    bool has_gravity = false;
    bool has_horizontal = true;

    // Molar densities.
    //
    mdv = new MolarDensity(MOLAR_DENSITY_VAPOR,  P_parameter);
    mdl = new MolarDensity(MOLAR_DENSITY_LIQUID, P_parameter);

    // Flash.
    //
    flash = new VLE_Flash_TPCW(mdl, mdv);

    // Thermodynamics.
    //
    tc = new Thermodynamics(mc, mw, "./hsigmaC_spline.txt", P_parameter);
    tc->set_flash(flash);

    // Flux.
    //

    // Accumulation.
    //
    accumulation_ = new Accum2Comp2PhasesAdimensionalized(phi_parameter, tc);
}

TPCWSubPhysics::~TPCWSubPhysics(){
    delete accumulation_;
    delete tc;
    delete flash;
    delete mdl;
    delete mdv;

    for (int i = equation_parameter_.size() - 1; i >= 0; i--) delete equation_parameter_[i];
}

