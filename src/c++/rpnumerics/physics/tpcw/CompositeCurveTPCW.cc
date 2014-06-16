#include "CompositeCurveTPCW.h"

CompositeCurveTPCW::CompositeCurveTPCW(const AccumulationFunction *a, const FluxFunction *f, const Boundary *b, ShockCurve *s, Explicit_Bifurcation_Curves *ebc, Evap_Extension *ee) : CompositeCurve(a,f, b, s, ebc), evapextension(ee) {
    cout<<"no construtor"<<this<<endl;
}

CompositeCurveTPCW::~CompositeCurveTPCW(){
    cout<<"Morte"<<endl;
}

int CompositeCurveTPCW::curve(const AccumulationFunction *RarAccum, const FluxFunction *RarFlux,
                              const Boundary *RarBoundary, 
                              const Curve &rarcurve,
                              const RealVector &composite_initial_point,
                              int last_point_in_rarefaction,
                              const ODE_Solver *odesolver,
                              double deltaxi,
                              int where_composite_begins, int fam, 
                              Curve &new_rarcurve,
                              Curve &compositecurve,
                              RealVector &final_direction,
                              int &reason_why,
                              int &edge){
    
    compositecurve.type = COMPOSITE_CURVE;
    compositecurve.family = fam;

    int rarsize = rarcurve.curve.size();
    

    RealVector direction = rarcurve.curve[rarsize - 1] - rarcurve.curve[rarsize - 2];
    normalize(direction);

    std::cout << "Before computing the composite: direction = " << direction << ", fam = " << fam << std::endl;

    if (std::abs(direction(0)) - .95 < .1 && fam == 1 && rarcurve.increase == SPEED_DECREASE){

        cout<<"Caso 1"<<endl;
        int corresponding_rar = last_point_in_rarefaction;

        RealVector ext_p;
        for (int i = rarsize - 1; i >= 0; i--){
            evapextension->extension(rarcurve.curve[i], ext_p);

            if (!boundary->inside(ext_p)){
                RealVector r;
                int info_intersection;
                if (compositecurve.curve.size() > 1){
                    info_intersection = boundary->intersection(compositecurve.curve.back(), ext_p, r, edge);
                }
                else {
                    info_intersection = boundary->intersection(rarcurve.curve.back(), ext_p, r, edge);
                }

                if (info_intersection == BOUNDARY_INTERSECTION_FOUND){
                    add_point_to_curve(r, corresponding_rar, rarcurve, compositecurve);
                }
                else {
                    return COMPOSITE_ERROR;
                }

                return COMPOSITE_REACHED_BOUNDARY;
            }
            else {
                add_point_to_curve(ext_p, corresponding_rar, rarcurve, compositecurve);
            }

            // Update index.
            //
            corresponding_rar--;            
        }

        int compositesize = compositecurve.curve.size();
        final_direction = compositecurve.curve[compositecurve.curve.size() - 1] - compositecurve.curve[compositecurve.curve.size() - 2];
        normalize(final_direction);
        reason_why = COMPOSITE_COMPLETED;

        compositecurve.last_point = compositecurve.curve.back();
        compositecurve.reason_to_stop = COMPOSITE_COMPLETED;
        compositecurve.final_direction = final_direction;

        return COMPOSITE_OK;
    }
    else {
        cout<<"Caso 2"<<endl;

        return CompositeCurve::curve(RarAccum, RarFlux, RarBoundary, rarcurve, 
                                     composite_initial_point, 
                                     last_point_in_rarefaction, 
                                     odesolver, deltaxi, 
                                     where_composite_begins, 
                                     fam, 
                                     new_rarcurve, 
                                     compositecurve, 
                                     final_direction, 
                                     reason_why, edge);
    }
}

