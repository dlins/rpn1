#include "StoneSubPhysics.h"
#include "HugoniotContinuation_nDnD.h"
#include "Model/SubPhysics.h"

StoneSubPhysics::StoneSubPhysics() : ThreePhaseFlowSubPhysics(){
    permeability_ = new StonePermeability(this);
    auxiliaryfunctions_.push_back(permeability_);

    grw_parameter = new Parameter(std::string("grw"), 1.0);
    grg_parameter = new Parameter(std::string("grg"), 1.0);
    gro_parameter = new Parameter(std::string("gro"), 1.0);

    muw_parameter = new Parameter(std::string("muw"), 1.0);
    mug_parameter = new Parameter(std::string("mug"), 1.0);
    muo_parameter = new Parameter(std::string("muo"), 1.0);

    // TODO: Introduce these parameters into the flux's equations. When
    //       When that happens, add these parameters to the parameter list
    //       so the user can access them.
    //
    cnw_parameter = new Parameter(std::string("cnw"), 0.0); std::cout << "cnw_parameter = " << (void*)cnw_parameter << std::endl;
    cno_parameter = new Parameter(std::string("cno"), 0.0);
    cng_parameter = new Parameter(std::string("cng"), 0.0);

    vel_parameter = new Parameter(std::string("vel"), 1.0);

    equation_parameter_.push_back(grw_parameter);
    equation_parameter_.push_back(gro_parameter);
    equation_parameter_.push_back(grg_parameter);

    equation_parameter_.push_back(muw_parameter);
    equation_parameter_.push_back(muo_parameter);
    equation_parameter_.push_back(mug_parameter);

    equation_parameter_.push_back(cnw_parameter);
    equation_parameter_.push_back(cno_parameter);
    equation_parameter_.push_back(cng_parameter);

    equation_parameter_.push_back(vel_parameter);

    flux_ = new StoneFluxFunction(grw_parameter, grg_parameter, gro_parameter, muw_parameter, mug_parameter, muo_parameter, vel_parameter, (StonePermeability*)permeability_);
//    std::cout << "Stone flux: " << (void*)flux_ << std::endl;

    // GridValues.
    //
    std::vector<int> number_of_cells(2);
    number_of_cells[0] = 128;
    number_of_cells[1] = 128;

    gridvalues_ = new GridValues(boundary_, boundary_->minimums(), boundary_->maximums(), number_of_cells);
    for (int i = 0; i < equation_parameter_.size(); i++) equation_parameter_[i]->add(gridvalues_);
    
//    cout<<"Em subphysics "<<(void * )gridvalues_<<endl;

    // HugoniotContinuation.
    //
//    hugoniotcontinuation_ = new HugoniotContinuation_nDnD(flux_, accumulation_, boundary_);

    // ImplicitHugoniot.
    //
    ImplicitHugoniotCurve *ihc = new ImplicitHugoniotCurve(flux_, accumulation_, boundary_);
    ihc->subphysics(this);

    hugoniot_curve.push_back(ihc);

    // ThreePhaseFlowImplicitHugoniot.
    //
    ThreePhaseFlowImplicitHugoniotCurve *tpfih = new ThreePhaseFlowImplicitHugoniotCurve(this);
    hugoniot_curve.push_back(tpfih);

    // Explicit Bifurcation Curves.
    //
    explicitbifurcationcurves_ = new Stone_Explicit_Bifurcation_Curves((StoneFluxFunction*)flux_);

    // Rarefaction.
    //
//    rarefactioncurve_ = new RarefactionCurve(accumulation_, flux_, boundary_);
    rarefactioncurve_ = new RarefactionCurve(this);

    // Shock curve.
    //
//    hugoniotcontinuation_ = new HugoniotContinuation2D2D(flux_, accumulation_, boundary_);
//    
//    hugoniot_curve.push_back(hugoniotcontinuation_);
////    hugoniotcontinuation_ = new HugoniotContinuation_nDnD(flux_, accumulation_, boundary_);

//    shockcurve_ = new ShockCurve(hugoniotcontinuation_);

    hugoniotcontinuation_ = new ThreePhaseFlowHugoniotContinuation(this);
    hugoniot_curve.push_back(hugoniotcontinuation_);

    shockcurve_ = new ShockCurve(this);
    shockcurve_->distance_to_contact_region(2e-2);

    // Composite.
    explicitbifurcationcurves_ = new Stone_Explicit_Bifurcation_Curves((StoneFluxFunction*)flux_);
    compositecurve_ = new CompositeCurve(accumulation_, flux_, boundary_, shockcurve_, explicitbifurcationcurves_);

    odesolver_ = new LSODE;

    // WaveCurve.
    //
//    wavecurvefactory_ = new WaveCurveFactory(accumulation_, flux_, boundary_, odesolver_, rarefactioncurve_, shockcurve_, compositecurve_);
    wavecurvefactory_ = new WaveCurveFactory(this);

    // Inflection.
    //
    inflection_curve_ = new Inflection_Curve;

    // Viscosity.
    //
    viscosity_ = new StoneViscosity(this);

    // Mobility.
    //
    mobility_ = new ThreePhaseFlowMobility(this);

    // Info.
    //
    info_subphysics_ = std::string("Stone");
}


StoneSubPhysics::~StoneSubPhysics(){
    delete mobility_;
    delete viscosity_;
    delete inflection_curve_;
    delete wavecurvefactory_;
    delete odesolver_;
    delete compositecurve_;
    delete explicitbifurcationcurves_;
    delete shockcurve_;
    delete rarefactioncurve_;

    // Not sure if this should really be done like this.
    // Perhaps it is best to eliminate only the HugoniotCurves that were instantiated
    // in this class, and let the rest be deleted at the father's dtor. 
    //
    for (int i = 0; i < hugoniot_curve.size(); i++) delete hugoniot_curve[i];

    delete flux_;

//    // When these parameters are active in the flux, eliminate these lines.
//    delete cng_parameter;
//    delete cno_parameter;
//    delete cnw_parameter;

    for (int i = equation_parameter_.size() - 1; i >=0; i--) delete equation_parameter_[i];

    delete permeability_;
}

