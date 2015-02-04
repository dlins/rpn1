#include <iostream>
#include "StoneSubPhysics.h"
#include "DeadVolatileVolatileGasSubPhysics.h"
#include "JDSubPhysics.h"
#include "Brooks_CoreySubPhysics.h"
#include "CoreyQuadSubPhysics.h"
#include "KovalSubPhysics.h"
#include "FoamSubPhysics.h"
#include "ICDOWSubPhysics.h"
#include "SorbieSubPhysics.h"
#include "TPCWSubPhysics.h"
#include "Quad2SubPhysics.h"

#include <FL/Fl.H>
#include <FL/fl_ask.H>
#include <FL/Fl_Double_Window.H>
#include <FL/Fl_Round_Button.H>
#include <FL/Fl_Choice.H>
#include <FL/Fl_Tile.H>
#include <FL/Fl_Float_Input.H>
#include <FL/Fl_Scroll.H>
#include "canvas.h"
#include "canvasmenuscroll.h"
#include "curve2d.h"
#include "segmentedcurve.h"
#include "MultiColoredCurve.h"
#include "WaveCurvePlot.h"
#include "LSODE.h"
#include "Inflection_Curve.h"
#include "CharacteristicPolynomialLevels.h"
#include "TestTools.h"
#include "ContactRegionBoundary.h"

SubPhysics *subphysics;

Fl_Double_Window *win;
    Fl_Tile *tile;
    Canvas *canvas;

    Fl_Group *scroll_grp;
    CanvasMenuScroll *scroll;
    Fl_Box *point_box;
    Fl_Button        *clear_all_curves, *nozoom;

Fl_Double_Window *sidewin;
    Fl_Scroll *sidescroll;
    Fl_Group *lastgrp = 0;

Fl_Group *parametersgrp;
    Fl_Box *parametersgrplabel;
    std::vector<Fl_Float_Input*> parameters_input;
    Fl_Button                    *restore_parameters;
    std::vector<Parameter*>      parameters;
    std::vector<double>          original_parameters;

Fl_Group *hugoniotgrp;
    Fl_Round_Button *hugoniotrnd;
    Fl_Choice *Hugoniot_method_choice, *shock_case_choice;

Fl_Group *rargrp;
    Fl_Box *rargrplabel;
    Fl_Round_Button *rarefactionrnd;
//    Fl_Round_Button *wavecurvernd;

    Fl_Group *rarfamgrp, *rarincgrp;
    Fl_Round_Button *rarfamslowbtn, *rarfamfastbtn;
    Fl_Round_Button *rarincbtn, *rardecbtn;

    Fl_Group *eigentypegrp;
    std::vector<Fl_Round_Button*> eigenrndvec;
    Fl_Round_Button *eigenfam, *eigentype;

    std::vector<bool (*)(const eigenpair&, const eigenpair&)> eigen_order_function;
    std::vector<std::string> eigen_name;

Fl_Group *wavecurvegrp;
    Fl_Box *wavecurvegrplabel;
    Fl_Round_Button *wavecurvernd;

    Fl_Group *wavecurvefamgrp, *wavecurveincgrp;
    Fl_Round_Button *wavecurvefamslowbtn, *wavecurvefamfastbtn;
    Fl_Round_Button *wavecurveincbtn, *wavecurvedecbtn;

    Fl_Choice *wavecurvechoice;
    std::vector<int> wavecurve_points;

    Fl_Button *riemannwizardbtn;

    Fl_Double_Window *riemannwizardwin;
        Fl_Box *riemanninfo;

Fl_Group *coincidencegrp;
    Fl_Round_Button *coincidencernd;
    Fl_Button *coincidencecompute;

Fl_Group *inflectiongrp;
    Fl_Round_Button *inflectionrnd;
    Fl_Button *inflectioncompute;

Fl_Group *eigencontournumericalgrp;
    Fl_Round_Button *eigennumericalrnd;
    Fl_Round_Button *eigennumericalslow, *eigennumericalfast;

Fl_Group *eigencontourexactgrp;
    Fl_Round_Button *eigenexactrnd;
    Fl_Round_Button *eigenlambdas, *eigenlambdae;

Fl_Group *ellipticregionboundarygrp;
    Fl_Round_Button *ellipticregionboundaryrnd;
    Fl_Button *ellipticregionboundarybtn;

Fl_Group *discriminantcontourgrp;
    Fl_Round_Button *discriminantcontourrnd;
    Fl_Box *discriminantcontourbox;

Fl_Group *doublecontactgrp;
    Fl_Round_Button *doublecontactrnd;
    Fl_Button *doublecontactbtn;

Fl_Group *contactregionboundarygrp;
    Fl_Round_Button *contactregionboundaryrnd;
    Fl_Button *contactregionboundarybtn;
    
std::vector<HugoniotCurve*> Hugoniot_methods;
std::vector<int>            reference_point_cases;

std::vector<Fl_Group*>                   optionsgrp;
std::vector<Fl_Round_Button*>            optionsbtn;
std::vector<void (*)(Fl_Widget*, void*)> optionsfnc;

void Hugoniotcb(Fl_Widget*, void*){
    int pointsize = subphysics->boundary()->minimums().size();

    RealVector p(pointsize);

    if (pointsize == 3) p(2) = 1.0;

    canvas->getxy(p(0), p(1));

    std::cout << p << std::endl;

    if (!subphysics->boundary()->inside(p)) return;

    ReferencePoint ref(p, subphysics->flux(), subphysics->accumulation(), 0);

    HugoniotCurve *hugoniot = Hugoniot_methods[Hugoniot_method_choice->value()];
    int type = reference_point_cases[shock_case_choice->value()];

    std::vector<HugoniotPolyLine> classified_curve;
    hugoniot->curve(ref, type, classified_curve);

    std::cout << "Hugoniot curve size: " << classified_curve.size() << std::endl;

    if (classified_curve.size() > 0){
        MultiColoredCurve *mcc = new MultiColoredCurve(classified_curve, -100.0, 100.0, 100);
        canvas->add(mcc);

        std::stringstream ss;
        ss << "Hugoniot (" << shock_case_choice->text(shock_case_choice->value()) << ")";
        scroll->add(ss.str().c_str(), canvas, mcc);
    }

//    std::vector<Curve> c;
//    hugoniot->curve(ref, type, c);

//    WaveCurve w;

//    for (int i = 0; i < c.size(); i++){
//        w.wavecurve.push_back(c[i]);
//        w.wavecurve.back().type = SHOCK_CURVE;

////        Curve2D *curve = new Curve2D(c[i].curve, 0.0, 0.0, 1.0);
////        canvas->add(curve);
//    }

//    WaveCurvePlot *r = new WaveCurvePlot(w, CURVE2D_SOLID_LINE);
//    canvas->add(r);
//    scroll->add("Hugoniot", canvas, r);

    return;
}

//double WE(void*, const RealVector &p){
//    CoreyQuadSubPhysics *corey = new CoreyQuadSubPhysics;

//    std::vector<int> type;
//    std::vector<std::string> name;
//    std::vector<void*> object;
//    std::vector<double (*)(void*, const RealVector &)> function;

//    corey->bifurcation_curve()->list_of_secondary_bifurcation_curves(type, name, object, function);
//    
//    return (*function[1])(object[0], p);
//}

void rarefactioncb(Fl_Widget*, void*){
    RarefactionCurve *rarefaction_curve = subphysics->rarefaction_curve();
    if (rarefaction_curve == 0) return;
    
    int increase = (rarincbtn->value() == 1) ? RAREFACTION_SPEED_SHOULD_INCREASE : RAREFACTION_SPEED_SHOULD_DECREASE;
    
    int family   = (rarfamslowbtn->value() == 1) ? 0 : 1;
    
    int dimension = subphysics->boundary()->minimums().size();

    RealVector initial_point(dimension);
    canvas->getxy(initial_point(0), initial_point(1));

    if (dimension == 3) initial_point(2) = 1.0;

//    initial_point(0) = 0.425506;
//    initial_point(1) = 0.169554;

//    initial_point(0) = 0.1;
//    initial_point(1) = 0.1;

//    RealVector initial_direction(2);
//    initial_direction(0) = 1.0;
//    initial_direction(1) = 0.0;

    LSODE lsode;
    ODE_Solver *odesolver = &lsode;

    double deltaxi = 1e-4;
    std::vector<RealVector> inflection_point;
    Curve rarcurve;

    int rar_stopped_because;
    RealVector final_direction;

    int edge;

    int info_rar = rarefaction_curve->curve(initial_point,
                                            family,
                                            increase,
                                            RAREFACTION /* INTEGRAL_CURVE */,
                                            RAREFACTION_INITIALIZE /*RAREFACTION_DONT_INITIALIZE*/,
                                            0 /*&initial_direction*/,
                                            odesolver,
                                            deltaxi,
                                            0, 0,
                                            rarcurve,
                                            inflection_point,
                                            final_direction,
                                            rar_stopped_because,
                                            edge);
                                            
    if (rarcurve.curve.size() > 0){
        WaveCurve w;
        w.wavecurve.push_back(rarcurve);

        Curve2D *r = new Curve2D(rarcurve.curve, 1.0, 0.0, 0.0, CURVE2D_SOLID_LINE/* | CURVE2D_MARKERS*/);
//        WaveCurvePlot *r = new WaveCurvePlot(w, CURVE2D_SOLID_LINE | CURVE2D_ARROWS, 0.0, 2.0, 10);

        canvas->add(r);

        std::stringstream ss;
        ss << "Rar. Init. = " << initial_point << ", size = " << rarcurve.curve.size();
        scroll->add(ss.str().c_str(), canvas, r);

//        // Test
//        {
//            std::vector<Extension*> e;
//            subphysics->list_of_extension_methods(e);

//            std::vector<RealVector> domain_polygon, image_polygon;
//            std::vector<RealVector> ext_curve;
//            e[0]->extension_curve(rarcurve.curve, domain_polygon, image_polygon, ext_curve);

//            std::cout << "ext_curve.size() = " << ext_curve.size() << std::endl;
//            
//            if (ext_curve.size() > 0) {
//                Curve2D *c = new Curve2D(ext_curve, 0.0, 1.0, 0.0);
//                canvas->add(c);
//                scroll->add("Extension", canvas, c);
//            }
//        }
    }

    if (inflection_point.size() > 0){
        Curve2D *ip = new Curve2D(inflection_point, 0.0, 0.0, 0.0, CURVE2D_MARKERS);
        canvas->add(ip);
        scroll->add("Integral points", canvas, ip);
    }
    
    return;
}

void wincb(Fl_Widget *w, void*){
    Fl_Double_Window *thiswindow = (Fl_Double_Window*)w;

    if (thiswindow == win){
        delete subphysics;

        exit(0);
    }

    return;
}

void clear_curves(Fl_Widget*, void*){
    scroll->clear_all_graphics();

    return;
}

void nozoomcb(Fl_Widget*, void*){
    canvas->nozoom();

    return;
}

void parametercb(Fl_Widget *w, void *param_obj){
//    for (int i = 0; i < parameters_input.size(); i++){
//        std::stringstream ss;
//        ss << parameters_input[i]->value();
//        
//        double v;
//        ss >> v;
//        parameters[i]->value(v);
//    }

//    subphysics->gridvalues()->clear_computations();

    // The observer-subject paradigm is tested here. Only the Parameter that was changed
    // notifies of it to the GridValues.
    //
    Fl_Float_Input *input = (Fl_Float_Input*)w;
    Parameter *parameter = (Parameter*)param_obj;

    std::stringstream ss;
    ss << input->value();
        
    double v;
    ss >> v;
    parameter->value(v);

    return;
}

void add_parameters(int px, const std::vector<Parameter*> &vp){
    for (int i = 0; i < vp.size(); i++){
        Fl_Float_Input *input = new Fl_Float_Input(px, parametersgrp->y() + 10 + (25 + 10)*i, parametersgrp->w() - 10 - px, 25, vp[i]->name().c_str());
        std::stringstream ss;
        ss << vp[i]->value();
        input->value(ss.str().c_str());
        input->callback(parametercb, (void*)vp[i]);

        parametersgrp->add(input);

        parameters_input.push_back(input);

        original_parameters.push_back(vp[i]->value());
    }

    return;
}

void opcb(Fl_Widget*, void*){
    for (int i = 0; i < optionsbtn.size(); i++){
        if (optionsbtn[i]->value() == 1){
            optionsgrp[i]->activate();
            canvas->setextfunc(optionsfnc[i], canvas, 0);
        }
        else optionsgrp[i]->deactivate();
    }

    return;
}

void coincidencecomputecb(Fl_Widget*, void*){
    Coincidence_Contour *coincidence_contour = subphysics->coincidence_contour();
    
    if (coincidence_contour != 0){
        std::vector<RealVector> coincidence_curve;
        coincidence_contour->curve(subphysics->flux(), subphysics->accumulation(), 
                                   *(subphysics->gridvalues()), coincidence_curve);

        if (coincidence_curve.size() > 0){
            SegmentedCurve *sc = new SegmentedCurve(coincidence_curve, 0.0, 0.0, 0.0);
            canvas->add(sc);

            scroll->add("Coincidence", canvas, sc);
        }
    }

    return;
}

void inflectioncomputecb(Fl_Widget*, void*){
    Inflection_Curve *ic = subphysics->inflection_curve();

    for (int fam = 0; fam < 2; fam++){
        std::vector<RealVector> inflection_curve;
        ic->curve(subphysics->flux(), subphysics->accumulation(), *(subphysics->gridvalues()), fam, inflection_curve);

        if (inflection_curve.size() > 0){
            SegmentedCurve *sc = new SegmentedCurve(inflection_curve, 0.0, 1.0, .5);
            canvas->add(sc);

            std::stringstream ss;
            ss << "Inflection, fam = " << fam;
            scroll->add(ss.str().c_str(), canvas, sc);
        }
    }

    return;
}

void eigenordercb(Fl_Widget*, void*){
//    if (eigenfam->value() == 1){
//        Eigen::set_order_eigenpairs(eigen_order_function[0]);
//    }
//    else {
//        Eigen::set_order_eigenpairs(eigen_order_function[1]);
//    }

    for (int i = 0; i < eigenrndvec.size(); i++){
        if (eigenrndvec[i]->value() == 1){
            Eigen::set_order_eigenpairs(eigen_order_function[i]);

            break;
        }
    }

    subphysics->gridvalues()->clear_computations();

    return;
}

void restore_parameterscb(Fl_Widget*, void*){
    for (int i = 0; i < original_parameters.size(); i++){
        std::stringstream ss;
        ss << original_parameters[i];

        parameters_input[i]->value(ss.str().c_str());

        parameters[i]->value(original_parameters[i]);
    }

    subphysics->gridvalues()->clear_computations();

    return;
}

void eigennumericalcb(Fl_Widget*, void*){
    RealVector p(3);
    canvas->getxy(p(0), p(1));
    p(2) = 1;

    int fam = (eigennumericalslow->value() == 1) ? 0: 1;

    CharacteristicPolynomialLevels cpl;

    std::vector<RealVector> curve;
    double lev;
    cpl.eigenvalue_curve(subphysics->flux(), subphysics->accumulation(), 
                         *(subphysics->gridvalues()), 
                         p, fam, 
                         curve, lev);

    if (curve.size() > 0){
        SegmentedCurve *sc = new SegmentedCurve(curve, 0.0, 0.0, 1.0);
        canvas->add(sc);

        std::stringstream ss;
        ss << "Lambda = " << lev << ", fam = " << fam << ", size = " << curve.size();

        scroll->add(ss.str().c_str(), canvas, sc);
    }

    return;
}

void eigenexactcb(Fl_Widget*, void*){
    RealVector p(3);
    canvas->getxy(p(0), p(1));
    p(2) = 1;

    int fam = (eigenlambdas->value() == 1) ? LAMBDA_S: LAMBDA_E;

    CharacteristicPolynomialLevels cpl;

    std::vector<RealVector> curve;
    cpl.eigenvalue_curve(subphysics->coincidence(),
                         subphysics->flux(), subphysics->accumulation(), 
                         *(subphysics->gridvalues()), 
                         p, fam, 
                         curve);

    if (curve.size() > 0){
        SegmentedCurve *sc = new SegmentedCurve(curve, 0.0, 0.0, 1.0);
        canvas->add(sc);

        std::stringstream ss;
        ss << "Lambda " << ((eigenlambdas->value() == 1) ? "s" : "e");

        scroll->add(ss.str().c_str(), canvas, sc);
    }

    return;
}

void ellipticregionboundarycb(Fl_Widget*, void*){
    CharacteristicPolynomialLevels cpl;

    double level = 0.0;
    std::vector<RealVector> curve;
    cpl.discriminant_curve(subphysics->flux(), subphysics->accumulation(), 
                           *(subphysics->gridvalues()), 
                           level, curve);

    if (curve.size() > 0){
        SegmentedCurve *sc = new SegmentedCurve(curve, 0.0, 0.0, 1.0);
        canvas->add(sc);

        std::stringstream ss;
        ss << "Elliptic region";

        scroll->add(ss.str().c_str(), canvas, sc);
    }

    return;
}

// Parameters.
//
void add_parameters_group(){
    subphysics->equation_parameter(parameters);
        
    std::vector<AuxiliaryFunction*> vaf;
    subphysics->auxiliary_functions(vaf);
    for (int i = 0; i < vaf.size(); i++){
        std::vector<Parameter*> vp;
        vaf[i]->parameter(vp);
            
        for (int j = 0; j < vp.size(); j++) parameters.push_back(vp[j]);
    }
    
    int x, y, w;
    if (lastgrp == 0){
        x = sidescroll->x() + 15;
        y = sidescroll->y();
        w = sidescroll->w() - 40;
    }
    else {
        x = lastgrp->x();
        y = lastgrp->y() + lastgrp->h();
        w = lastgrp->w();
    }
    
    parametersgrplabel = new Fl_Box(x, y + 30, w, 25, "Parameters");
    parametersgrplabel->align(FL_ALIGN_LEFT | FL_ALIGN_INSIDE);

    parametersgrp = new Fl_Group(parametersgrplabel->x(), parametersgrplabel->y() + parametersgrplabel->h() + 1, parametersgrplabel->w(), 10 + parameters.size()*(25 + 10) + (25 + 10));
            
    int px = 130;
    add_parameters(px, parameters);

    restore_parameters = new Fl_Button(px, 
                                       parametersgrp->y() + 10 + parameters.size()*(25 + 10),
                                       parametersgrp->w() - px - 10, 25, 
                                       "Restore parameters");
    restore_parameters->callback(restore_parameterscb);
            
    parametersgrp->end();
    parametersgrp->box(FL_EMBOSSED_BOX);
            
    if (parameters.size() == 0) parametersgrp->deactivate();
            
    lastgrp = parametersgrp;

    return;
}

void Hugoniot_method_choicecb(Fl_Widget*, void*){
    std::vector<std::string> names;

    Hugoniot_methods[Hugoniot_method_choice->value()]->list_of_reference_points(reference_point_cases, names);

    shock_case_choice->clear();
    for (int i = 0; i < names.size(); i++){
        shock_case_choice->add(names[i].c_str());
    }

    shock_case_choice->value(0);

    return;
}

// Hugoniots.
//
void add_Hugoniots_group(){
    int x, y, w;
    if (lastgrp == 0){
        x = sidescroll->x() + 15;
        y = sidescroll->y();
        w = sidescroll->w() - 40;
    }
    else {
        x = lastgrp->x();
        y = lastgrp->y() + lastgrp->h();
        w = lastgrp->w();
    }

    hugoniotgrp = new Fl_Group(x, 
                               y + 35,
                               w,
                               80);
    {
        int Hugoniot_method_choice_x = 170;
        Hugoniot_method_choice = new Fl_Choice(Hugoniot_method_choice_x, 
                                               hugoniotgrp->y() + 10, 
                                               hugoniotgrp->w() - 10 - Hugoniot_method_choice_x, 
                                               25, 
                                               "Hugoniot methods");
        Hugoniot_method_choice->callback(Hugoniot_method_choicecb);

        subphysics->list_of_Hugoniot_methods(Hugoniot_methods);
        std::cout << "Hugoniot_methods.size() = " << Hugoniot_methods.size() << std::endl;


        for (int i = 0; i < Hugoniot_methods.size(); i++){
            Hugoniot_method_choice->add(Hugoniot_methods[i]->Hugoniot_info().c_str());
        }
                
        shock_case_choice = new Fl_Choice(Hugoniot_method_choice->x(), 
                                          Hugoniot_method_choice->y() + Hugoniot_method_choice->h() + 10, 
                                          Hugoniot_method_choice->w(), 
                                          Hugoniot_method_choice->h(), 
                                          "Reference point at");
    }
    hugoniotgrp->end();
    hugoniotgrp->box(FL_EMBOSSED_BOX);
            
    hugoniotrnd = new Fl_Round_Button(hugoniotgrp->x(), hugoniotgrp->y() - 25, hugoniotgrp->w(), 25, "Hugoniots");
    hugoniotrnd->type(FL_RADIO_BUTTON);
    hugoniotrnd->callback(opcb);

    // Deactivate if no Hugoniot is present.
    //            
    if (Hugoniot_methods.size() == 0){
        hugoniotrnd->deactivate();
        hugoniotgrp->deactivate();
    }
    else {
        Hugoniot_method_choice->value(0);
        Hugoniot_method_choice->do_callback();

        optionsgrp.push_back(hugoniotgrp);
        optionsbtn.push_back(hugoniotrnd);
        optionsfnc.push_back(&Hugoniotcb);
    }

    lastgrp = hugoniotgrp;

    return;
}

// Rarefactions.
//
void add_rarefactions_group(){
    int x, y, w;
    if (lastgrp == 0){
        x = sidescroll->x() + 15;
        y = sidescroll->y();
        w = sidescroll->w() - 40;
    }
    else {
        x = lastgrp->x();
        y = lastgrp->y() + lastgrp->h();
        w = lastgrp->w();
    }

    Eigen::list_order_eigenpairs(eigen_order_function, eigen_name);

    rargrplabel = new Fl_Box(x, 
                             y + 10,
                             w,
                             25,
                             "Rarefaction");
    rargrplabel->align(FL_ALIGN_LEFT | FL_ALIGN_INSIDE);

    rargrp = new Fl_Group(rargrplabel->x(), 
                              rargrplabel->y() + rargrplabel->h() + 1,
                              rargrplabel->w(),
                              100 + 10 + 10 + 10 + (25 + 10) + /*(25 + 10) +*/ (10 + 25)*eigen_order_function.size());
    {
        rarefactionrnd = new Fl_Round_Button(rargrp->x() + 10, rargrp->y() + 10, rargrp->w() - 20, 25, "Rarefaction");
        rarefactionrnd->type(FL_RADIO_BUTTON);
        rarefactionrnd->value(0);
        rarefactionrnd->callback(opcb);
        sidescroll->add(rarefactionrnd);

//        wavecurvernd = new Fl_Round_Button(rarefactionrnd->x(), rarefactionrnd->y() + rarefactionrnd->h() + 10, rarefactionrnd->w(), 25, "Wavecurve");
//        wavecurvernd->type(FL_RADIO_BUTTON);
//        wavecurvernd->value(0);
//        wavecurvernd->callback(opcb);
//        sidescroll->add(wavecurvernd);

        rarfamgrp = new Fl_Group(rarefactionrnd->x(), rarefactionrnd->y() + rarefactionrnd->h() + 10, (rargrp->w() - 30)/2, 80 /*rargrp->h() - 20*/);
        {
            rarfamslowbtn = new Fl_Round_Button(rarfamgrp->x() + 10, rarfamgrp->y() + 10,
                                                rarfamgrp->w() - 20, 25,
                                                "Slow family");
            rarfamslowbtn->type(FL_RADIO_BUTTON);
            rarfamslowbtn->value(1);
                                                        
            rarfamfastbtn = new Fl_Round_Button(rarfamslowbtn->x(), rarfamslowbtn->y() + rarfamslowbtn->h() + 10,
                                                rarfamslowbtn->w(), rarfamslowbtn->h(), 
                                                "Fast family");
            rarfamfastbtn->type(FL_RADIO_BUTTON);
            rarfamfastbtn->value(0);
        }
        rarfamgrp->end();
        rarfamgrp->box(FL_EMBOSSED_BOX);
                
        rarincgrp = new Fl_Group(rarfamgrp->x() + rarfamgrp->w() + 10, rarfamgrp->y(),
                                 rarfamgrp->w(), rarfamgrp->h());
        {
            rarincbtn = new Fl_Round_Button(rarincgrp->x() + 10, rarincgrp->y() + 10,
                                            rarincgrp->w() - 20, 25,
                                            "Increase speed");
            rarincbtn->type(FL_RADIO_BUTTON);
            rarincbtn->value(1);
                    
            rardecbtn = new Fl_Round_Button(rarincbtn->x(), rarincbtn->y() + rarincbtn->h() + 10,
                                            rarincbtn->w(), rarincbtn->h(), 
                                            "Decrease speed");
            rardecbtn->type(FL_RADIO_BUTTON);
            rardecbtn->value(0);                   
        }
        rarincgrp->end();
        rarincgrp->box(FL_EMBOSSED_BOX);

        // Subgroup with the types of available orderings for eigenpairs.
        //
        eigentypegrp = new Fl_Group(rargrp->x() + 10, 
                                    rarfamgrp->y() + rarfamgrp->h() + 20,
                                    rargrp->w() - 20,
                                    10 + (10 + 25)*eigen_order_function.size(),
                                    "Eigenpair-ordering options");
        {
            for (int i = 0; i < eigen_order_function.size(); i++){
                Fl_Round_Button *r = new Fl_Round_Button(eigentypegrp->x() + 10, 
                                                         eigentypegrp->y() + 10 + (25 + 10)*i,
                                                         eigentypegrp->w() - 20,
                                                         25,
                                                         eigen_name[i].c_str());

                r->type(FL_RADIO_BUTTON);
                r->callback(eigenordercb);

                if (i == 0) r->value(1);

                eigenrndvec.push_back(r);
            }
        }
        eigentypegrp->end();
        eigentypegrp->box(FL_EMBOSSED_BOX);
        eigentypegrp->align(FL_ALIGN_TOP | FL_ALIGN_LEFT);

        eigenordercb(0, 0);
    }
    rargrp->end();
    rargrp->box(FL_EMBOSSED_BOX);
            
    // Deactivate if the rarefaction is not present.
    //
    if (subphysics->rarefaction_curve() == 0){
        rargrp->deactivate();
        rarefactionrnd->deactivate();
    }
    else {
        optionsgrp.push_back(rargrp);
        optionsbtn.push_back(rarefactionrnd);
        optionsfnc.push_back(&rarefactioncb);
   }
         
//            // Deactivate if the wavecurve is not present.
//            //
//            if (subphysics->rarefaction_curve() == 0){
//                rargrp->deactivate();
//                rarefactionrnd->deactivate();
//            }
//            else {
//                optionsgrp.push_back(rargrp);
//                optionsbtn.push_back(rarefactionrnd);
//                optionsfnc.push_back(&rarefactioncb);
//            }

    lastgrp = rargrp;

    return;
}

// Wavecurves.
//
void wavecurvefactioncb(Fl_Widget*, void*){
//    subphysics->set_graphics(canvas, scroll);

    WaveCurveFactory *wcf = subphysics->wavecurvefactory();
    if (wcf == 0) return;
    
    int increase = (wavecurveincbtn->value() == 1) ? RAREFACTION_SPEED_SHOULD_INCREASE : RAREFACTION_SPEED_SHOULD_DECREASE;
    
    int family   = (wavecurvefamslowbtn->value() == 1) ? 0 : 1;
    
    int dimension = subphysics->boundary()->minimums().size();

    RealVector initial_point(dimension);
    canvas->getxy(initial_point(0), initial_point(1));

//    // Test only, can be eliminated afterwards.
//    //
//    {
//        std::stringstream ss;
//        ss << "At " << initial_point << ", inside contact region = " << subphysics->inside_contact_region(initial_point, family);
//        TestTools::pause(ss);
//    }

    if (dimension == 3) initial_point(2) = 1.0;

//    initial_point(0) = 0.16106;
//    initial_point(1) = 0.220297;

//    initial_point(0) = 0.266444;
//    initial_point(1) = 0.657434;

//    initial_point(0) = 0.35;
//    initial_point(1) = 0.101;

//    // Initial point, wavecurve enters the contact region.
//    initial_point(0) = 0.425506;
//    initial_point(1) = 0.169554;

//    Robert's 27-01-2015, mug0 = .02. The wavecurves initiated at both this and the next points 
//    are valid now, with a contact following the shock. At FoamSubPhysics:     shockcurve_->distance_to_contact_region(2e-2);
//    initial_point(0) = 0.3778;
//    initial_point(1) = 0.1516;

//    // The shock following a composite ends not followed by a contact. (SOLVED)
//    initial_point(0) = 0.369902;
//    initial_point(1) = 0.152228;

    // The shock following a composite changes direction.
//    initial_point(0) = 0.351431;
//    initial_point(1) = 0.160891;



//    // The composite returns.
//    initial_point(0) = 0.448215;
//    initial_point(1) = 0.214109;

    // Composite ends and the next shock only has one point.    
//    initial_point(0) = 0.6109;
//    initial_point(1) = 0.1751;

//    initial_point(0) = 0.15;
//    initial_point(1) = 0.15;

//    initial_point(0) = 0.1;
//    initial_point(1) = 0.1;

//    initial_point(0) = 0.26629;
//    initial_point(1) = 0.73193;

    std::cout << "Wavecurve callback, initial point = " << initial_point << std::endl;
    if (!subphysics->boundary()->inside(initial_point)){
        std::stringstream ss;
        ss << "Initial point " << initial_point << " lies outside boundary! Aborting.";

        TestTools::pause(ss);
        return;
    }

    #ifdef TESTCOMPOSITE
    subphysics->composite()->set_graphics(canvas, scroll);
    #endif

    WaveCurve hwc;
    int reason_why, edge;
    int type = wavecurve_points[wavecurvechoice->value()];

//        std::vector<int> biftype;
//        std::vector<std::string> name;
//        std::vector<void*> object;
//        std::vector<double (*)(void*, const RealVector &)> function;
//        subphysics->bifurcation_curve()->list_of_secondary_bifurcation_curves(biftype, 
//                                                    name,
//                                                    object,
//                                                    function);    

//    int bifindex = 1;

//    for (int i = 0; i < function.size(); i++) std::cout << (void*)function[i] << std::endl;

    int info = wcf->wavecurve(type, initial_point, family, increase, subphysics->Hugoniot_continuation(), 0/*object[bifindex]*/, 0/*function[bifindex]*/, hwc, reason_why, edge);

    std::cout << "After wavecurvefactory." << std::endl;

    if (info == WAVECURVEFACTORY_INVALID_PARAMETERS) TestTools::pause("The wavecurve could not be computed because\nthe family and/or the monotonicity are wrong.");

    if (hwc.wavecurve.size() > 0){
        WaveCurvePlot *wcp = new WaveCurvePlot(hwc, initial_point/*, CURVE2D_MARKERS | CURVE2D_SOLID_LINE*/);
        canvas->add(wcp);

        std::stringstream ss;
        ss << "Wavecurve. Initial = " << initial_point;
        for (int i = 0; i < hwc.wavecurve.size(); i++) ss << ", " << hwc.wavecurve[i].curve.size();

        scroll->add(ss.str().c_str(), canvas, wcp);
    }

    return;
}

void riemannwizardbtncb(Fl_Widget*, void*){

    ExternalFunctionSignature extfunc = canvas->getextfunc();
    return;
}

void doublecontactbtncb(Fl_Widget*, void*){
    Double_Contact *dc = subphysics->double_contact();

    std::vector<RealVector> left_curve, right_curve;

    int lfam = 1;
    int rfam = 1;

    dc->curve(subphysics->flux(), subphysics->accumulation(), subphysics->gridvalues(), lfam,
              subphysics->flux(), subphysics->accumulation(), subphysics->gridvalues(), rfam,
              left_curve, right_curve);

    if (left_curve.size() > 0 && right_curve.size() > 0){
        {
            SegmentedCurve *sc = new SegmentedCurve(left_curve, 0.0, 0.0, 1.0);
            canvas->add(sc);

            scroll->add("Double contact (left)", canvas, sc);
        }

        {
            SegmentedCurve *sc = new SegmentedCurve(right_curve, 0.0, 0.0, 1.0);
            canvas->add(sc);

            scroll->add("Double contact (right)", canvas, sc);
        }
    }

    return;
}

void add_wavecurve_group(){
    int x, y, w;
    if (lastgrp == 0){
        x = sidescroll->x() + 15;
        y = sidescroll->y();
        w = sidescroll->w() - 40;
    }
    else {
        x = lastgrp->x();
        y = lastgrp->y() + lastgrp->h();
        w = lastgrp->w();
    }

    Eigen::list_order_eigenpairs(eigen_order_function, eigen_name);

    wavecurvegrplabel = new Fl_Box(x, 
                             y + 10,
                             w,
                             25,
                             "Wavecurve");
    wavecurvegrplabel->align(FL_ALIGN_LEFT | FL_ALIGN_INSIDE);

    wavecurvegrp = new Fl_Group(wavecurvegrplabel->x(), 
                              wavecurvegrplabel->y() + wavecurvegrplabel->h() + 1,
                              wavecurvegrplabel->w(),
                              100 + 10 + 25 + 10 + 25 + 10 + 25);
    {
        wavecurvernd = new Fl_Round_Button(wavecurvegrp->x() + 10, wavecurvegrp->y() + 10, wavecurvegrp->w() - 20, 25, "Wavecurve");
        wavecurvernd->type(FL_RADIO_BUTTON);
        wavecurvernd->value(0);
        wavecurvernd->callback(opcb);
        sidescroll->add(wavecurvernd);

        wavecurvefamgrp = new Fl_Group(wavecurvernd->x(), wavecurvernd->y() + wavecurvernd->h() + 10, (wavecurvegrp->w() - 30)/2, 80 /*wavecurvegrp->h() - 20*/);
        {
            wavecurvefamslowbtn = new Fl_Round_Button(wavecurvefamgrp->x() + 10, wavecurvefamgrp->y() + 10,
                                                wavecurvefamgrp->w() - 20, 25,
                                                "Slow family");
            wavecurvefamslowbtn->type(FL_RADIO_BUTTON);
            wavecurvefamslowbtn->value(1);
                                                        
            wavecurvefamfastbtn = new Fl_Round_Button(wavecurvefamslowbtn->x(), wavecurvefamslowbtn->y() + wavecurvefamslowbtn->h() + 10,
                                                wavecurvefamslowbtn->w(), wavecurvefamslowbtn->h(), 
                                                "Fast family");
            wavecurvefamfastbtn->type(FL_RADIO_BUTTON);
            wavecurvefamfastbtn->value(0);
        }
        wavecurvefamgrp->end();
        wavecurvefamgrp->box(FL_EMBOSSED_BOX);
                
        wavecurveincgrp = new Fl_Group(wavecurvefamgrp->x() + wavecurvefamgrp->w() + 10, wavecurvefamgrp->y(),
                                 wavecurvefamgrp->w(), wavecurvefamgrp->h());
        {
            wavecurveincbtn = new Fl_Round_Button(wavecurveincgrp->x() + 10, wavecurveincgrp->y() + 10,
                                            wavecurveincgrp->w() - 20, 25,
                                            "Increase speed");
            wavecurveincbtn->type(FL_RADIO_BUTTON);
            wavecurveincbtn->value(1);
                    
            wavecurvedecbtn = new Fl_Round_Button(wavecurveincbtn->x(), wavecurveincbtn->y() + wavecurveincbtn->h() + 10,
                                            wavecurveincbtn->w(), wavecurveincbtn->h(), 
                                            "Decrease speed");
            wavecurvedecbtn->type(FL_RADIO_BUTTON);
            wavecurvedecbtn->value(0);                   
        }
        wavecurveincgrp->end();
        wavecurveincgrp->box(FL_EMBOSSED_BOX);

        {
            int Wavecurve_points = 170;
            wavecurvechoice = new Fl_Choice(Wavecurve_points,
                                            wavecurveincgrp->y() + wavecurveincgrp->h() + 10,
                                            wavecurvegrp->w() - 10 - Wavecurve_points, 
                                            25,
                                            "Initial point");

            if (subphysics->wavecurvefactory() != 0){
                std::vector<int> type;
                std::vector<std::string> name;

                subphysics->wavecurvefactory()->list_of_initial_points(type, name);
                if (name.size() > 0){
                    for (int i = 0; i < name.size(); i++){
                        wavecurve_points.push_back(type[i]);
                        wavecurvechoice->add(name[i].c_str());
                    }

                    wavecurvechoice->value(0);
                }
            }

            riemannwizardbtn = new Fl_Button(wavecurvegrp->x() + 10, 
                                             wavecurvechoice->y() + wavecurvechoice->h() + 10,
                                             wavecurvegrp->w() - 20,
                                             25,
                                             "Riemann solver wizard");
            riemannwizardbtn->callback(riemannwizardbtncb);
        }
    }
    wavecurvegrp->end();
    wavecurvegrp->box(FL_EMBOSSED_BOX);
            
    // Deactivate if the wavecurveefaction is not present.
    //
    if (subphysics->wavecurvefactory() == 0){
        wavecurvegrp->deactivate();
        wavecurvernd->deactivate();
    }
    else {
        optionsgrp.push_back(wavecurvegrp);
        optionsbtn.push_back(wavecurvernd);
        optionsfnc.push_back(&wavecurvefactioncb);
   }

   lastgrp = wavecurvegrp;

   return;
}

// Coincidence contour plot.
//
void add_coincidence_group(){
    int x, y, w;
    if (lastgrp == 0){
        x = sidescroll->x() + 15;
        y = sidescroll->y();
        w = sidescroll->w() - 40;
    }
    else {
        x = lastgrp->x();
        y = lastgrp->y() + lastgrp->h();
        w = lastgrp->w();
    }

    coincidencegrp = new Fl_Group(x, y + 35, w, 45);
    {
        coincidencecompute = new Fl_Button(coincidencegrp->x() + 10, coincidencegrp->y() + 10, 
                                           coincidencegrp->w() - 20, coincidencegrp->h() - 20,
                                           "Compute the coincidence");
        coincidencecompute->callback(coincidencecomputecb);
    }
    coincidencegrp->end();
    coincidencegrp->box(FL_EMBOSSED_BOX);

    coincidencernd = new Fl_Round_Button(coincidencegrp->x(), coincidencegrp->y() - 25, coincidencegrp->w(), 25, "Coincidence");
    coincidencernd->type(FL_RADIO_BUTTON);
    coincidencernd->value(0);
    coincidencernd->callback(opcb);

    // Deactivate if the coincidence is not present.
    //
    if (subphysics->coincidence_contour() == 0){
        coincidencernd->deactivate();
        coincidencegrp->deactivate();
    } 
    else {
        optionsgrp.push_back(coincidencegrp);
        optionsbtn.push_back(coincidencernd);
        optionsfnc.push_back(0);
    }

    lastgrp = coincidencegrp;

    return;
}

// Inflection contour plot.
//
void add_inflection_group(){
    int x, y, w;
    if (lastgrp == 0){
        x = sidescroll->x() + 15;
        y = sidescroll->y();
        w = sidescroll->w() - 40;
    }
    else {
        x = lastgrp->x();
        y = lastgrp->y() + lastgrp->h();
        w = lastgrp->w();
    }

    inflectiongrp = new Fl_Group(x, y + 35, w, 45);
    {
        inflectioncompute = new Fl_Button(inflectiongrp->x() + 10, inflectiongrp->y() + 10, 
                                          inflectiongrp->w() - 20, inflectiongrp->h() - 20,
                                          "Compute the inflection");
        inflectioncompute->callback(inflectioncomputecb);
    }
    inflectiongrp->end();
    inflectiongrp->box(FL_EMBOSSED_BOX);

    inflectionrnd = new Fl_Round_Button(inflectiongrp->x(), inflectiongrp->y() - 25, inflectiongrp->w(), 25, "Inflection");
    inflectionrnd->type(FL_RADIO_BUTTON);
    inflectionrnd->value(0);
    inflectionrnd->callback(opcb);

    // Deactivate if the inflection is not present.
    //
    if (subphysics->inflection_curve() == 0){
        inflectionrnd->deactivate();
        inflectiongrp->deactivate();
    }
    else {
        optionsgrp.push_back(inflectiongrp);
        optionsbtn.push_back(inflectionrnd);
        optionsfnc.push_back(0);
    }

    lastgrp = inflectiongrp;

    return;
}

// Numerical eigenvalues contour plot.
//
void add_numerical_eigenvalue_contour_group(){
    int x, y, w;
    if (lastgrp == 0){
        x = sidescroll->x() + 15;
        y = sidescroll->y();
        w = sidescroll->w() - 40;
    }
    else {
        x = lastgrp->x();
        y = lastgrp->y() + lastgrp->h();
        w = lastgrp->w();
    }

    eigencontournumericalgrp = new Fl_Group(x, y + 35, w, 45);
    {
        eigennumericalslow = new Fl_Round_Button(eigencontournumericalgrp->x() + 10, 
                                                 eigencontournumericalgrp->y() + 10, 
                                                 (eigencontournumericalgrp->w() - 30)/2, 
                                                 25,
                                                 "Slow");
        eigennumericalslow->type(FL_RADIO_BUTTON);
        eigennumericalslow->value(1);

        eigennumericalfast = new Fl_Round_Button(eigennumericalslow->x() + eigennumericalslow->w() + 10,
                                                 eigennumericalslow->y(), 
                                                 eigennumericalslow->w(), 
                                                 eigennumericalslow->h(),
                                                 "Fast");
        eigennumericalfast->type(FL_RADIO_BUTTON);
        eigennumericalfast->value(0);
    }
    eigencontournumericalgrp->end();
    eigencontournumericalgrp->box(FL_EMBOSSED_BOX);

    eigennumericalrnd = new Fl_Round_Button(eigencontournumericalgrp->x(), eigencontournumericalgrp->y() - 25, eigencontournumericalgrp->w(), 25, "Eigencontour (numerical)");
    eigennumericalrnd->type(FL_RADIO_BUTTON);
    eigennumericalrnd->value(0);
    eigennumericalrnd->callback(opcb);

    // It is always possible to compute the eigencontour with numerical eigenvalues.
    //
    optionsgrp.push_back(eigencontournumericalgrp);
    optionsbtn.push_back(eigennumericalrnd);
    optionsfnc.push_back(eigennumericalcb);

    lastgrp = eigencontournumericalgrp;

    return;
}

// Exact eigenvalues contour plot.
//
void add_exact_eigenvalue_contour_group(){
    int x, y, w;
    if (lastgrp == 0){
        x = sidescroll->x() + 15;
        y = sidescroll->y();
        w = sidescroll->w() - 40;
    }
    else {
        x = lastgrp->x();
        y = lastgrp->y() + lastgrp->h();
        w = lastgrp->w();
    }

    eigencontourexactgrp = new Fl_Group(x, y + 35, w, 45);
    {
        eigenlambdas = new Fl_Round_Button(eigencontourexactgrp->x() + 10, 
                                           eigencontourexactgrp->y() + 10, 
                                           (eigencontourexactgrp->w() - 30)/2, 
                                           25,
                                           "Lambda s");
        eigenlambdas->type(FL_RADIO_BUTTON);
        eigenlambdas->value(1);

        eigenlambdae = new Fl_Round_Button(eigenlambdas->x() + eigenlambdas->w() + 10,
                                           eigenlambdas->y(), 
                                           eigenlambdas->w(), 
                                           eigenlambdas->h(),
                                           "Lambda e");
        eigenlambdae->type(FL_RADIO_BUTTON);
        eigenlambdae->value(0);
    }
    eigencontourexactgrp->end();
    eigencontourexactgrp->box(FL_EMBOSSED_BOX);

    eigenexactrnd = new Fl_Round_Button(eigencontourexactgrp->x(), eigencontourexactgrp->y() - 25, eigencontourexactgrp->w(), 25, "Eigencontour (exact)");
    eigenexactrnd->type(FL_RADIO_BUTTON);
    eigenexactrnd->value(0);
    eigenexactrnd->callback(opcb);

    // Deactivate if the coincidence is not present.
    //
    if (subphysics->coincidence() == 0){
        eigenexactrnd->deactivate();
        eigencontourexactgrp->deactivate();
    }
    else {
        optionsgrp.push_back(eigencontourexactgrp);
        optionsbtn.push_back(eigenexactrnd);
        optionsfnc.push_back(eigenexactcb);
    }

    lastgrp = eigencontourexactgrp;

    return;
}

// Elliptic region.
//
void add_elliptic_region(){
    int x, y, w;
    if (lastgrp == 0){
        x = sidescroll->x() + 15;
        y = sidescroll->y();
        w = sidescroll->w() - 40;
    }
    else {
        x = lastgrp->x();
        y = lastgrp->y() + lastgrp->h();
        w = lastgrp->w();
    }

    ellipticregionboundarygrp = new Fl_Group(x, y + 35, w, 45);
    {
        ellipticregionboundarybtn = new Fl_Button(ellipticregionboundarygrp->x() + 10,
                                                  ellipticregionboundarygrp->y() + 10,
                                                  ellipticregionboundarygrp->w() - 20,
                                                  ellipticregionboundarygrp->h() - 20,
                                                  "Compute the boundary of the elliptic region");
        ellipticregionboundarybtn->callback(ellipticregionboundarycb);
    }
    ellipticregionboundarygrp->end();
    ellipticregionboundarygrp->box(FL_EMBOSSED_BOX);

    ellipticregionboundaryrnd = new Fl_Round_Button(ellipticregionboundarygrp->x(), ellipticregionboundarygrp->y() - 25, ellipticregionboundarygrp->w(), 25, "Boundary of the elliptic region");
    ellipticregionboundaryrnd->type(FL_RADIO_BUTTON);
    ellipticregionboundaryrnd->value(0);
    ellipticregionboundaryrnd->callback(opcb);

    // It is always possible to compute the boundary of the elliptic region.
    //
    optionsgrp.push_back(ellipticregionboundarygrp);
    optionsbtn.push_back(ellipticregionboundaryrnd);
    optionsfnc.push_back(0);

    lastgrp = ellipticregionboundarygrp;

    return;
}

// Discriminant contour.
//
void discriminantcontourcb(Fl_Widget*, void*){
    RealVector point(2);
    canvas->getxy(point(0), point(1));

    CharacteristicPolynomialLevels cpl;

    double level;
    std::vector<RealVector> curve;

    cpl.discriminant_curve(subphysics->flux(), subphysics->accumulation(), 
                           *(subphysics->gridvalues()), 
                           point,
                           curve, level);

    if (curve.size() > 0){
        SegmentedCurve *sc = new SegmentedCurve(curve, 0.0, 0.0, 1.0);
        canvas->add(sc);

        std::stringstream ss;
        ss << "Discriminant contour, p = " << point << ", level = " << level;

        scroll->add(ss.str().c_str(), canvas, sc);
    }

    return;
}

void add_discriminant_contour(){
    int x, y, w;
    if (lastgrp == 0){
        x = sidescroll->x() + 15;
        y = sidescroll->y();
        w = sidescroll->w() - 40;
    }
    else {
        x = lastgrp->x();
        y = lastgrp->y() + lastgrp->h();
        w = lastgrp->w();
    }

    discriminantcontourgrp = new Fl_Group(x, y + 35, w, 45);
    {
        discriminantcontourbox = new Fl_Box(discriminantcontourgrp->x() + 10,
                                            discriminantcontourgrp->y() + 10,
                                            discriminantcontourgrp->w() - 20,
                                            discriminantcontourgrp->h() - 20,
                                            "Click on the canvas");
    }
    discriminantcontourgrp->end();
    discriminantcontourgrp->box(FL_EMBOSSED_BOX);

    discriminantcontourrnd = new Fl_Round_Button(discriminantcontourgrp->x(), discriminantcontourgrp->y() - 25, discriminantcontourgrp->w(), 25, "Discriminant contour");
    discriminantcontourrnd->type(FL_RADIO_BUTTON);
    discriminantcontourrnd->value(0);
    discriminantcontourrnd->callback(opcb);

    // It is always possible to compute the discriminant contour.
    //
    optionsgrp.push_back(discriminantcontourgrp);
    optionsbtn.push_back(discriminantcontourrnd);
    optionsfnc.push_back(discriminantcontourcb);

    lastgrp = discriminantcontourgrp;

    return;
}

// Double contact.
//
void add_double_contact(){
    int x, y, w;
    if (lastgrp == 0){
        x = sidescroll->x() + 15;
        y = sidescroll->y();
        w = sidescroll->w() - 40;
    }
    else {
        x = lastgrp->x();
        y = lastgrp->y() + lastgrp->h();
        w = lastgrp->w();
    }

    doublecontactgrp = new Fl_Group(x, y + 35, w, 45);
    {
        doublecontactbtn = new Fl_Button(doublecontactgrp->x() + 10,
                                         doublecontactgrp->y() + 10,
                                         doublecontactgrp->w() - 20,
                                         doublecontactgrp->h() - 20,
                                         "Compute the double contact");
        doublecontactbtn->callback(doublecontactbtncb);
    }
    doublecontactgrp->end();
    doublecontactgrp->box(FL_EMBOSSED_BOX);

    doublecontactrnd = new Fl_Round_Button(doublecontactgrp->x(), doublecontactgrp->y() - 25, doublecontactgrp->w(), 25, "Double contact");
    doublecontactrnd->type(FL_RADIO_BUTTON);
    doublecontactrnd->value(0);
    doublecontactrnd->callback(opcb);

    // It is always possible to compute the double contact.
    //
    optionsgrp.push_back(doublecontactgrp);
    optionsbtn.push_back(doublecontactrnd);
    optionsfnc.push_back(0);

    lastgrp = doublecontactgrp;

    return;
}

// Contact region boundary.
//
void contactregionboundarycb(Fl_Widget*, void*){


    ContactRegionBoundary *crb = subphysics->contact_region_boundary();

    for (int fam = 0; fam < subphysics->number_of_families(); fam++){
        std::vector<RealVector> curve;
        crb->curve(fam, curve);


        if (curve.size() > 1){
            SegmentedCurve *c = new SegmentedCurve(curve, 0.0, 0.0, 1.0);
            canvas->add(c);

            std::stringstream ss;
            ss << "Contact region, family = " << fam;
            scroll->add(ss.str().c_str(), canvas, c);
        }
    }

    return;
}

void add_contact_region_boundary(){
    int x, y, w;
    if (lastgrp == 0){
        x = sidescroll->x() + 15;
        y = sidescroll->y();
        w = sidescroll->w() - 40;
    }
    else {
        x = lastgrp->x();
        y = lastgrp->y() + lastgrp->h();
        w = lastgrp->w();
    }

    contactregionboundarygrp = new Fl_Group(x, y + 35, w, 45);
    {
        contactregionboundarybtn = new Fl_Button(contactregionboundarygrp->x() + 10,
                                                 contactregionboundarygrp->y() + 10,
                                                 contactregionboundarygrp->w() - 20,
                                                 contactregionboundarygrp->h() - 20,
                                                 "Compute the boundary of the contact region");
        contactregionboundarybtn->callback(contactregionboundarycb);
    }
    contactregionboundarygrp->end();
    contactregionboundarygrp->box(FL_EMBOSSED_BOX);

    contactregionboundaryrnd = new Fl_Round_Button(contactregionboundarygrp->x(), contactregionboundarygrp->y() - 25, contactregionboundarygrp->w(), 25, "Boundary of the contact region");
    contactregionboundaryrnd->type(FL_RADIO_BUTTON);
    contactregionboundaryrnd->value(0);
    contactregionboundaryrnd->callback(opcb);

    // It is always possible to compute the boundary of the contact region.
    //
    optionsgrp.push_back(contactregionboundarygrp);
    optionsbtn.push_back(contactregionboundaryrnd);
    optionsfnc.push_back(0);

    lastgrp = contactregionboundarygrp;

    return;
}

void on_move_coords(Fl_Widget*, void*){
    RealVector p(2);
    canvas->getxy(p(0), p(1));

    std::stringstream s;
    s << "Mouse over " << p;

    point_box->copy_label(s.str().c_str());    

    return;
}

int main(){
    Fl::scheme("gtk+");
    subphysics = 0;

    // Create the subphysics.
    //
//    subphysics = new DeadVolatileVolatileGasSubPhysics;
//    subphysics = new JDSubPhysics;
//    subphysics = new ICDOWSubPhysics();

//    subphysics = new StoneSubPhysics;
//    subphysics = new Brooks_CoreySubPhysics;
    subphysics = new CoreyQuadSubPhysics;
//    subphysics = new KovalSubPhysics;
//    subphysics = new FoamSubPhysics;
//    subphysics = new SorbieSubPhysics;
//    subphysics = new TPCWSubPhysics;
//    subphysics = new Quad2SubPhysics;

    if (subphysics == 0) {
        fl_alert("No subphysics was loaded. Aborting...");
        exit(0);
    }

    std::cout << "SubPhysics \"" << subphysics->info_subphysics() << "\" created successfully." << std::endl;

    // Main window.
    //
    int scrollwidth = 300;
    win = new Fl_Double_Window(10, 10, 900 + scrollwidth + 20, 900);
//    tile = new Fl_Tile(0, 0, win->w(), win->h());
    {
        // Canvas.
        //
        canvas = new Canvas(0, 0, win->w() - 20 - scrollwidth, win->h());

        std::vector<std::vector<RealVector> > b;
        subphysics->boundary()->physical_boundary(b);

        for (int i = 0; i < b.size(); i++){
            if (b[i].size() > 1){
                Curve2D *c = new Curve2D(b[i], 0.0, 0.0, 0.0);

                canvas->add(c);
            }
        }

        canvas->set_transform_matrix(subphysics->transformation_matrix().data());
        canvas->nozoom();

        canvas->xlabel(subphysics->xlabel().c_str());
        canvas->ylabel(subphysics->ylabel().c_str());

        // Scroll.
        //
        scroll_grp = new Fl_Group(canvas->x() + canvas->w() + 1, canvas->y(), scrollwidth, canvas->h());
        scroll = new CanvasMenuScroll(canvas->x() + canvas->w() + 10, 20, scrollwidth, win->h() - 40 - 25 - 35, "Curves");

        // Show the point under the mouse.
        //
        point_box = new Fl_Box(scroll->x(), scroll->y() + scroll->h() + 10, scroll->w(), 25);
        point_box->box(FL_EMBOSSED_BOX);
        canvas->on_move(&on_move_coords, canvas, 0);

        clear_all_curves = new Fl_Button(point_box->x(), point_box->y() + point_box->h() + 10, (point_box->w() - 10)/2, 25, "Clear all curves");
        clear_all_curves->callback(clear_curves);

        nozoom = new Fl_Button(clear_all_curves->x() + clear_all_curves->w() + 10, 
                               clear_all_curves->y(),
                               clear_all_curves->w(),
                               clear_all_curves->h(),
                               "No zoom");
        nozoom->callback(nozoomcb);

        scroll_grp->end();

        // For test-plotting.
        #ifdef TESTWAVECURVEFACTORY
        subphysics->wavecurvefactory()->canvas = canvas;
        subphysics->wavecurvefactory()->scroll = scroll;
        #endif
    }
//    tile->end();
    win->end();
    win->copy_label(subphysics->info_subphysics().c_str());
    win->resizable(win);
    win->callback(wincb);
    win->show();

    sidewin = new Fl_Double_Window(win->x() + win->w() + 1, 
                                   win->y(),
                                   500, 
                                   win->h(),
                                   "Subphysics-related");
    {
        sidescroll = new Fl_Scroll(10, 20, sidewin->w() - 20, sidewin->h() - 20 - 30);
        sidescroll->type(Fl_Scroll::VERTICAL);

        lastgrp = 0;
        {
            // Parameters.
            //
            add_parameters_group();
            std::cout << "Added parameters." << std::endl;

            // Hugoniots.
            //
            add_Hugoniots_group();
            std::cout << "Added Hugoniots." << std::endl;
            
            // Rarefaction options, including the eigenpairs-ordering options available.
            //
            add_rarefactions_group();
            std::cout << "Added rarefaction." << std::endl;

            // Add wavecurves.
            //
            add_wavecurve_group();
            std::cout << "Added wavecurve." << std::endl;

            // Coincidence.
            //
            add_coincidence_group();
            std::cout << "Added coincidence." << std::endl;

            // Inflection.
            // 
            add_inflection_group();
            std::cout << "Added inflection." << std::endl;

            // Numerical eigenvalue contour.
            //
            add_numerical_eigenvalue_contour_group();
            std::cout << "Added numerical eigenvalues." << std::endl;

            // Exact eigenvalue contour.
            //
            add_exact_eigenvalue_contour_group();
            std::cout << "Added exact eigenvalues." << std::endl;

            // Elliptic region.
            //
            add_elliptic_region();
            std::cout << "Added elliptic region." << std::endl;

            // Discriminant contour.
            //
            add_discriminant_contour();
            std::cout << "Added discriminant contour." << std::endl;

            // Double contact.
            //
            add_double_contact();
            std::cout << "Added double contact." << std::endl;

            // Contact region boundary.
            //
            add_contact_region_boundary();
            std::cout << "Added contact region boundary." << std::endl;
        }
        sidescroll->end();
        
        // Activate the first group.
        //
        if (optionsbtn.size() > 0) optionsbtn.front()->value(1);

        opcb(0, 0);
    }
    sidewin->end();
    sidewin->callback(wincb);
    sidewin->show();

//    // Test purposes!
//    {
//        CoreyQuadSubPhysics *corey = new CoreyQuadSubPhysics;

//        std::vector<int> type;
//        std::vector<std::string> name;
//        std::vector<void*> object;
//        std::vector<double (*)(void*, const RealVector &)> function;
//        corey->bifurcation_curve()->list_of_secondary_bifurcation_curves(type, 
//                                                    name,
//                                                    object,
//                                                    function);    

//        for (int i = 0; i < name.size(); i++) std::cout << "name[" << i << "] = \"" << name[i] << "\"" << std::endl;

//        // WE
//        {
//            std::vector<RealVector> v;
//            v.push_back(corey->W());
//            v.push_back(corey->E());

//            Curve2D *c = new Curve2D(v, 0.0, 0.0, 0.0);
//            canvas->add(c);
//        }
//    }

//    // Test.
//    //
//    {
//        RealVector pp(2);
//        pp(0) = 0.30934054;
//        pp(1) = 0.11556151;

//        Curve2D *c = new Curve2D(pp, 0.0, 0.0, 0.0, CURVE2D_MARKERS);
//        canvas->add(c);
//    }

    return Fl::run();
}

