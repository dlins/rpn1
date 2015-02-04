#include "FoamSubPhysics.h"

FoamSubPhysics::FoamSubPhysics() : ThreePhaseFlowSubPhysics(){
    muw_parameter = new Parameter(std::string("muw"), 1.0);
    muo_parameter = new Parameter(std::string("muo"), 5.0);
    mug_parameter = new Parameter(std::string("mug0"), 2e-2); // Notice that, even though the name is changed, this parameter is just mug.

    grw_parameter = new Parameter(std::string("grw"), 1.0);
    gro_parameter = new Parameter(std::string("gro"), 1.0);
    grg_parameter = new Parameter(std::string("grg"), 1.0);

    vel_parameter = new Parameter(std::string("vel"), 1.0);

    cnw_parameter = new Parameter(std::string("cnw"), 0.1);
    cno_parameter = new Parameter(std::string("cno"), 0.1);
    cng_parameter = new Parameter(std::string("cng"), 0.0);

    nw_parameter = new Parameter(std::string("nw"), 2.0);
    no_parameter = new Parameter(std::string("no"), 2.0);
    ng_parameter = new Parameter(std::string("ng"), 2.0);

    equation_parameter_.push_back(muw_parameter);
    equation_parameter_.push_back(muo_parameter);
    equation_parameter_.push_back(mug_parameter);

    equation_parameter_.push_back(grw_parameter);
    equation_parameter_.push_back(gro_parameter);
    equation_parameter_.push_back(grg_parameter);

    equation_parameter_.push_back(vel_parameter);

    equation_parameter_.push_back(cnw_parameter);
    equation_parameter_.push_back(cno_parameter);
    equation_parameter_.push_back(cng_parameter);

    equation_parameter_.push_back(nw_parameter);
    equation_parameter_.push_back(no_parameter);
    equation_parameter_.push_back(ng_parameter);

    // TODO: These values must be set correctly!
    epdry = new Parameter(std::string("epdry"), 100.0);
    fdry  = new Parameter(std::string("fdry"), 0.0);
    foil  = new Parameter(std::string("foil"), 0.0);
    fmdry = new Parameter(std::string("fmdry"), 0.3);
    fmmob = new Parameter(std::string("fmmob"), 2000.0); // Was: 55000.0
    fmoil = new Parameter(std::string("fmoil"), 0.3);
    epoil = new Parameter(std::string("epoil"), 0.0, 5.0, 3.0); // The author of the model lets this parameter 
                                                                // vary between 0 and 5, but it is effectively varying between 2 and 5.

    floil = cno_parameter; // This will remain thus until the difference between floil and cno is figured out.
                           // TODO: When that happens, add floil to the list of parameters.
                           //       Right now adding it would cause a segfault at dtor-time.


    equation_parameter_.push_back(epdry);
    equation_parameter_.push_back(fdry);
    equation_parameter_.push_back(foil);
    equation_parameter_.push_back(fmdry);
    equation_parameter_.push_back(fmmob);
    equation_parameter_.push_back(fmoil);
    equation_parameter_.push_back(epoil);

    // Permeability.
    //
    permeability_ = new FoamPermeability(cnw_parameter, cno_parameter, cng_parameter,
                                         nw_parameter, no_parameter, ng_parameter,
                                         this);

    // Gas viscosity.
    //
    fdry_switch = new Parameter(std::string("fdry switch"), 1.0);
    equation_parameter_.push_back(fdry_switch);

    fo_switch = new Parameter(std::string("fo switch"), 1.0);
    equation_parameter_.push_back(fo_switch);

    viscosity_ = new FoamViscosity(mug_parameter, 
                                   epdry,
                                   fdry,
                                   foil,
                                   fmdry,
                                   fmmob,
                                   fmoil,
                                   floil, 
                                   epoil,
                                   fdry_switch, fo_switch, // KILL IT LATER!
                                   this);

    // Flux.
    //
    flux_ = new FoamFluxFunction(grw_parameter, gro_parameter, grg_parameter,
                                 muw_parameter, muo_parameter,
                                 vel_parameter,
                                 (FoamPermeability*)permeability_,
                                 (FoamViscosity*)viscosity_);

    // GridValues.
    //
    std::vector<int> number_of_cells(2);
    number_of_cells[0] = 513;
    number_of_cells[1] = 513;

    gridvalues_ = new GridValues(boundary_, boundary_->minimums(), boundary_->maximums(), number_of_cells);
    for (int i = 0; i < equation_parameter_.size(); i++) equation_parameter_[i]->add(gridvalues_);

    // ImplicitHugoniot.
    //
    ImplicitHugoniotCurve *ihc = new ImplicitHugoniotCurve(flux_, accumulation_, boundary_);
    ihc->subphysics(this);

    hugoniot_curve.push_back(ihc);

    // ThreePhaseFlowImplicitHugoniot.
    //
    ThreePhaseFlowImplicitHugoniotCurve *tpfih = new ThreePhaseFlowImplicitHugoniotCurve(this);
    hugoniot_curve.push_back(tpfih);

    // Rarefaction.
    //
//    rarefactioncurve_ = new RarefactionCurve(accumulation_, flux_, boundary_);
    rarefactioncurve_ = new RarefactionCurve(this);

    // Shock curve.
    //
    hugoniotcontinuation_ = new ThreePhaseFlowHugoniotContinuation(this);
    hugoniot_curve.push_back(hugoniotcontinuation_);

    shockcurve_ = new ShockCurve(this);
    shockcurve_->distance_to_contact_region(2e-2);

    // Parameter to control the maximum distance to the contact region,
    // and its observer to change said distance in the shokcurve.
    //
    fso = new FoamShockObserver(this);
    max_distance_to_contact_region_parameter_ = new Parameter(std::string("Dist. contact"), 2e-2);
    max_distance_to_contact_region_parameter_->add(fso);
    equation_parameter_.push_back(max_distance_to_contact_region_parameter_);

//    shockcurve_ = new ShockCurve(hugoniotcontinuation_);

    // Composite.
//    Stone_Explicit_Bifurcation_Curves bc((StoneFluxFunction*)flux);
    compositecurve_ = new CompositeCurve(accumulation_, flux_, boundary_, shockcurve_, 0/*&bc*/);

    odesolver_ = new LSODE;

    // TEST HugoniotODE
//    EulerSolver *euler = new EulerSolver(boundary_, 5);
    HugoniotODE *hode = new HugoniotODE(this, odesolver_);
    hugoniot_curve.push_back(hode);

    // WaveCurve.
    //
//    wavecurvefactory_ = new WaveCurveFactory(accumulation_, flux_, boundary_, odesolver_, rarefactioncurve_, shockcurve_, compositecurve_);
//    wavecurvefactory_ = new WaveCurveFactory(accumulation_, flux_, boundary_, odesolver_, rarefactioncurve_, shockcurve_, compositecurve_);
    wavecurvefactory_ = new WaveCurveFactory(this);

    // Inflection.
    //
    inflection_curve_ = new Inflection_Curve;

//    // BifurcationCurve.
//    //
//    bifurcationcurve_ = new FoamTransitionalLine(this);

    // Mobility.
    //
    mobility_ = new ThreePhaseFlowMobility(this);

    info_subphysics_ = std::string("Foam");
}

FoamSubPhysics::~FoamSubPhysics(){
    delete mobility_;
    delete inflection_curve_;
    delete wavecurvefactory_;
    delete odesolver_;
    delete compositecurve_;
    delete fso;
    delete shockcurve_;
    delete rarefactioncurve_;
    for (int i = 0; i < hugoniot_curve.size(); i++) delete hugoniot_curve[i];

    delete gridvalues_;
    delete flux_;

    delete viscosity_;

    delete permeability_;

    for (int i = 0; i < equation_parameter_.size(); i++) delete equation_parameter_[i];
}

bool FoamSubPhysics::parameter_consistency(){
    return fmoil->value() > floil->value();
}

