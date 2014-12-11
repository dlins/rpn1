#include <iostream>
#include "StoneSubPhysics.h"
#include "GasVolatileDeadSubPhysics.h"
#include "JDSubPhysics.h"
#include "Brooks_CoreySubPhysics.h"

#include <FL/Fl.H>
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

SubPhysics *subphysics;

Fl_Double_Window *win;
    Fl_Tile *tile;
    Canvas *canvas;

    Fl_Group *scroll_grp;
    CanvasMenuScroll *scroll;
    Fl_Button        *clear_all_curves, *nozoom;

Fl_Double_Window *sidewin;
    Fl_Scroll *sidescroll;

Fl_Group *parametersgrp;
    Fl_Box *parametersgrplabel;
    std::vector<Fl_Float_Input*> parameters_input;
    Fl_Button                    *restore_parameters;
    std::vector<Parameter*>      parameters;
    std::vector<double>          original_parameters;

Fl_Group *hugoniotgrp;
    Fl_Round_Button *hugoniotrnd;
    Fl_Choice *Hugoniot_method_choice, *shock_case_choice;

Fl_Group *rarwavegrp;
    Fl_Box *rarwavegrplabel;
    Fl_Round_Button *rarefactionrnd;
    Fl_Round_Button *wavecurvernd;

    Fl_Group *rarfamgrp, *rarincgrp;
    Fl_Round_Button *rarfamslowbtn, *rarfamfastbtn;
    Fl_Round_Button *rarincbtn, *rardecbtn;

    Fl_Group *eigentypegrp;
    std::vector<Fl_Round_Button*> eigenrndvec;
    Fl_Round_Button *eigenfam, *eigentype;

    std::vector<bool (*)(const eigenpair&, const eigenpair&)> eigen_order_function;
    std::vector<std::string> eigen_name;

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
    
std::vector<HugoniotCurve*> Hugoniot_methods;
std::vector<int>            shock_cases;

std::vector<Fl_Group*>                   optionsgrp;
std::vector<Fl_Round_Button*>            optionsbtn;
std::vector<void (*)(Fl_Widget*, void*)> optionsfnc;

void Hugoniotcb(Fl_Widget*, void*){
    RealVector p(3);
    canvas->getxy(p(0), p(1));
    p(2) = 1;

    ReferencePoint ref(p, subphysics->flux(), subphysics->accumulation(), 0);

    HugoniotCurve *hugoniot = Hugoniot_methods[Hugoniot_method_choice->value()];
    int type = shock_cases[shock_case_choice->value()];

    std::vector<HugoniotPolyLine> classified_curve;
    hugoniot->curve(ref, type, classified_curve);
    std::cout << "Size = " << classified_curve.size() << std::endl;

    if (classified_curve.size() > 0){
        MultiColoredCurve *mcc = new MultiColoredCurve(classified_curve, -100.0, 100.0, 100);
        canvas->add(mcc);
        scroll->add("Hugoniot", canvas, mcc);
    }

    return;
}

void rarefactioncb(Fl_Widget*, void*){
    RarefactionCurve *rarefaction_curve = subphysics->rarefaction_curve();
    if (rarefaction_curve == 0) return;
    
    int increase = (rarincbtn->value() == 1) ? RAREFACTION_SPEED_SHOULD_INCREASE : RAREFACTION_SPEED_SHOULD_DECREASE;
    
    int family   = (rarfamslowbtn->value() == 1) ? 0 : 1;
    
    int dimension = subphysics->boundary()->minimums().size();

    RealVector initial_point(dimension);
    canvas->getxy(initial_point(0), initial_point(1));

    if (dimension == 3) initial_point(2) = 1.0;

    LSODE lsode;
    ODE_Solver *odesolver = &lsode;

    double deltaxi = 1e-3;
    std::vector<RealVector> inflection_point;
    Curve rarcurve;

    int rar_stopped_because;
    RealVector final_direction;

    int edge;

    int info_rar = rarefaction_curve->curve(initial_point,
                                            family,
                                            increase,
                                            RAREFACTION,
                                            RAREFACTION_INITIALIZE,
                                            0,
                                            odesolver,
                                            deltaxi,
                                            rarcurve,
                                            inflection_point,
                                            final_direction,
                                            rar_stopped_because,
                                            edge);
                                            
    if (rarcurve.curve.size() > 0){
        WaveCurve w;
        w.wavecurve.push_back(rarcurve);

//        Curve2D *r = new Curve2D(rarcurve.curve, 1.0, 0.0, 0.0, CURVE2D_SOLID_LINE);
        WaveCurvePlot *r = new WaveCurvePlot(w, CURVE2D_SOLID_LINE | CURVE2D_ARROWS, 0.0, 2.0, 10);

        canvas->add(r);

        std::stringstream ss;
        ss << "Integral curve " << initial_point << ", size = " << rarcurve.curve.size();
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

    if (thiswindow == win) exit(0);

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

void parametercb(Fl_Widget*, void*){
    for (int i = 0; i < parameters_input.size(); i++){
        std::stringstream ss;
        ss << parameters_input[i]->value();
        
        double v;
        ss >> v;
        parameters[i]->value(v);
    }

    subphysics->gridvalues()->clear_computations();
    
    return;
}

void add_parameters(int px, const std::vector<Parameter*> &vp){
    for (int i = 0; i < vp.size(); i++){
        Fl_Float_Input *input = new Fl_Float_Input(px, parametersgrp->y() + 10 + (25 + 10)*i, parametersgrp->w() - 10 - px, 25, vp[i]->name().c_str());
        std::stringstream ss;
        ss << vp[i]->value();
        input->value(ss.str().c_str());
        input->callback(parametercb);

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
        ss << "Lambda, fam = " << fam;

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

int main(){
    // Create the subphysics.
    //
//    subphysics = new StoneSubPhysics;
//    subphysics = new GasVolatileDeadSubPhysics;
//    subphysics = new JDSubPhysics;
    subphysics = new Brooks_CoreySubPhysics;

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
        scroll = new CanvasMenuScroll(canvas->x() + canvas->w() + 10, 20, scrollwidth, win->h() - 40 - 25, "Curves");

        clear_all_curves = new Fl_Button(scroll->x(), scroll->y() + scroll->h() + 10, (scroll->w() - 10)/2, 25, "Clear all curves");
        clear_all_curves->callback(clear_curves);

        nozoom = new Fl_Button(clear_all_curves->x() + clear_all_curves->w() + 10, 
                               clear_all_curves->y(),
                               clear_all_curves->w(),
                               clear_all_curves->h(),
                               "No zoom");
        nozoom->callback(nozoomcb);

        scroll_grp->end();
    }
//    tile->end();
    win->end();
    win->copy_label(subphysics->info_subphysics().c_str());
    win->resizable(win);
    win->callback(wincb);
    win->show();

    std::cout << "Main window created." << std::endl;

    sidewin = new Fl_Double_Window(win->x() + win->w() + 1, 
                                   win->y(),
                                   500, 
                                   win->h(),
                                   "Subphysics-related");
    {
        sidescroll = new Fl_Scroll(10, 20, sidewin->w() - 20, sidewin->h() - 20 - 30);
        sidescroll->type(Fl_Scroll::VERTICAL);
        {
            // Parameters.
            //
            {
            subphysics->equation_parameter(parameters);
        
            std::vector<AuxiliaryFunction*> vaf;
            subphysics->auxiliary_functions(vaf);
            for (int i = 0; i < vaf.size(); i++){
                std::vector<Parameter*> vp;
                vaf[i]->parameter(vp);
            
                for (int j = 0; j < vp.size(); j++) parameters.push_back(vp[j]);
            }
        
            parametersgrplabel = new Fl_Box(sidescroll->x() + 15, sidescroll->y() + 30, sidescroll->w() - 40, 25, "Parameters");
            parametersgrplabel->align(FL_ALIGN_LEFT | FL_ALIGN_INSIDE);

            parametersgrp = new Fl_Group(parametersgrplabel->x(), parametersgrplabel->y() + parametersgrplabel->h() + 1, parametersgrplabel->w(), 10 + parameters.size()*(25 + 10) + (25 + 10));
            
            int px = 100;
            add_parameters(px, parameters);

            restore_parameters = new Fl_Button(px, 
                                               parametersgrp->y() + 10 + parameters.size()*(25 + 10),
                                               parametersgrp->w() - px - 10, 25, 
                                               "Restore parameters");
            restore_parameters->callback(restore_parameterscb);
            
            parametersgrp->end();
            parametersgrp->box(FL_EMBOSSED_BOX);
            
            if (parameters.size() == 0) parametersgrp->deactivate();
            
            }

            // Hugoniots.
            //
            hugoniotgrp = new Fl_Group(parametersgrp->x(), 
                                       parametersgrp->y() + parametersgrp->h() + 35,
                                       parametersgrp->w(),
                                       80);
            {
                int Hugoniot_method_choice_x = 170;
                Hugoniot_method_choice = new Fl_Choice(Hugoniot_method_choice_x, 
                                                       hugoniotgrp->y() + 10, 
                                                       hugoniotgrp->w() - 10 - Hugoniot_method_choice_x, 
                                                       25, 
                                                       "Hugoniot methods");

                subphysics->list_of_Hugoniot_methods(Hugoniot_methods);

                for (int i = 0; i < Hugoniot_methods.size(); i++){
                    Hugoniot_method_choice->add(Hugoniot_methods[i]->Hugoniot_info().c_str());
                }
                
                Hugoniot_method_choice->value(0);

                shock_case_choice = new Fl_Choice(Hugoniot_method_choice->x(), 
                                                  Hugoniot_method_choice->y() + Hugoniot_method_choice->h() + 10, 
                                                  Hugoniot_method_choice->w(), 
                                                  Hugoniot_method_choice->h(), 
                                                  "Shock cases");
                {
                    std::vector<std::string> names;

                    subphysics->shock_cases(shock_cases, names);

                    for (int i = 0; i < names.size(); i++){
                        shock_case_choice->add(names[i].c_str());
                    }
                    shock_case_choice->value(0);
                }
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
                optionsgrp.push_back(hugoniotgrp);
                optionsbtn.push_back(hugoniotrnd);
                optionsfnc.push_back(&Hugoniotcb);
            }
            
            // Rarefaction options, including the eigenpairs-ordering options available.
            //
            Eigen::list_order_eigenpairs(eigen_order_function, eigen_name);

            rarwavegrplabel = new Fl_Box(hugoniotgrp->x(), 
                                         hugoniotgrp->y() + hugoniotgrp->h() + 10,
                                         hugoniotgrp->w(),
                                         25,
                                         "Rarefaction/Wavecurve");
            rarwavegrplabel->align(FL_ALIGN_LEFT | FL_ALIGN_INSIDE);

            rarwavegrp = new Fl_Group(rarwavegrplabel->x(), 
                                      rarwavegrplabel->y() + rarwavegrplabel->h() + 1,
                                      rarwavegrplabel->w(),
                                      100 + 10 + 10 + 10 + (25 + 10) + (25 + 10) + (10 + 25)*eigen_order_function.size());
            {
                rarefactionrnd = new Fl_Round_Button(rarwavegrp->x() + 10, rarwavegrp->y() + 10, rarwavegrp->w() - 20, 25, "Rarefaction");
                rarefactionrnd->type(FL_RADIO_BUTTON);
                rarefactionrnd->value(0);
                rarefactionrnd->callback(opcb);
                sidescroll->add(rarefactionrnd);

                wavecurvernd = new Fl_Round_Button(rarefactionrnd->x(), rarefactionrnd->y() + rarefactionrnd->h() + 10, rarefactionrnd->w(), 25, "Wavecurve");
                wavecurvernd->type(FL_RADIO_BUTTON);
                wavecurvernd->value(0);
                wavecurvernd->callback(opcb);
                sidescroll->add(wavecurvernd);

                rarfamgrp = new Fl_Group(wavecurvernd->x(), wavecurvernd->y() + wavecurvernd->h() + 10, (rarwavegrp->w() - 30)/2, 80 /*rarwavegrp->h() - 20*/);
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
                
                eigentypegrp = new Fl_Group(rarwavegrp->x() + 10, 
                                            rarfamgrp->y() + rarfamgrp->h() + 20,
                                            rarwavegrp->w() - 20,
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
            rarwavegrp->end();
            rarwavegrp->box(FL_EMBOSSED_BOX);
            
            // Deactivate if the rarefaction is not present.
            //
            if (subphysics->rarefaction_curve() == 0){
                rarwavegrp->deactivate();
                rarefactionrnd->deactivate();
            }
            else {
                optionsgrp.push_back(rarwavegrp);
                optionsbtn.push_back(rarefactionrnd);
                optionsfnc.push_back(&rarefactioncb);
            }
         
//            // Deactivate if the wavecurve is not present.
//            //
//            if (subphysics->rarefaction_curve() == 0){
//                rarwavegrp->deactivate();
//                rarefactionrnd->deactivate();
//            }
//            else {
//                optionsgrp.push_back(rarwavegrp);
//                optionsbtn.push_back(rarefactionrnd);
//                optionsfnc.push_back(&rarefactioncb);
//            }

            // Coincidence.
            coincidencegrp = new Fl_Group(rarwavegrp->x(), rarwavegrp->y() + rarwavegrp->h() + 35, hugoniotgrp->w(), 45);
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

            // Inflection.
            // 
            inflectiongrp = new Fl_Group(coincidencegrp->x(), coincidencegrp->y() + coincidencegrp->h() + 35, hugoniotgrp->w(), 45);
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

            // Numerical eigenvalue contour.
            //
            eigencontournumericalgrp = new Fl_Group(inflectiongrp->x(), inflectiongrp->y() + inflectiongrp->h() + 35, inflectiongrp->w(), 45);
            {
//                Fl_Round_Button *eigennumericalslow, *eigennumericalfast;
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

            // Exact eigenvalue contour.
            //
            eigencontourexactgrp = new Fl_Group(eigencontournumericalgrp->x(), eigencontournumericalgrp->y() + eigencontournumericalgrp->h() + 35, eigencontournumericalgrp->w(), 45);
            {
//                Fl_Round_Button *eigennumericalslow, *eigennumericalfast;
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

            ellipticregionboundarygrp = new Fl_Group(eigencontourexactgrp->x(), eigencontourexactgrp->y() + eigencontourexactgrp->h() + 35, eigencontourexactgrp->w(), 45);
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

        }
        sidescroll->end();
        
        // Activate the first group.
        //
        if (optionsbtn.size() > 0) optionsbtn.front()->value(1);

        opcb(0, 0);
    }
    sidewin->end();
    //~ sidewin->resizable(sidewin);
    sidewin->callback(wincb);
    sidewin->show();

    Fl::scheme("gtk+");

    return Fl::run();
}
