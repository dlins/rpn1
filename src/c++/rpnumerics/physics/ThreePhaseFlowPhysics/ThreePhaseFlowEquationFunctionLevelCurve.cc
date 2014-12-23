#include "ThreePhaseFlowEquationFunctionLevelCurve.h"

ThreePhaseFlowEquationFunctionLevelCurve::ThreePhaseFlowEquationFunctionLevelCurve(const RpFunction *rpf, GridValues *g): EquationFunctionLevelCurve(rpf, g){
}

ThreePhaseFlowEquationFunctionLevelCurve::~ThreePhaseFlowEquationFunctionLevelCurve(){
}

double ThreePhaseFlowEquationFunctionLevelCurve::fw_level_function(EquationFunctionLevelCurve *obj, const RealVector &p){
    return obj->EquationFunctionLevelCurve::level(p, 0);
}

double ThreePhaseFlowEquationFunctionLevelCurve::fo_level_function(EquationFunctionLevelCurve *obj, const RealVector &p){
    return obj->EquationFunctionLevelCurve::level(p, 1);
}

double ThreePhaseFlowEquationFunctionLevelCurve::fg_level_function(EquationFunctionLevelCurve *obj, const RealVector &p){
    return 1.0 - obj->EquationFunctionLevelCurve::level(p, 0) - obj->EquationFunctionLevelCurve::level(p, 1);
}

// Component is type (water, oil, gas).
//
void ThreePhaseFlowEquationFunctionLevelCurve::curve(const RealVector &ref, int component, std::vector<RealVector> &c){
    if      (component == WATER_FLUX) level_function = &fw_level_function;
    else if (component == OIL_FLUX)   level_function = &fo_level_function;
    else if (component == GAS_FLUX)   level_function = &fg_level_function;

    component_ = component;
    level_ = level(ref, component_);

        std::vector<RealVector> curve;
        std::vector< std::deque <RealVector> > deque_curves;
        std::vector <bool> is_circular;

        int method = SEGMENTATION_METHOD;
        int info = ContourMethod::contour2d(this, curve, deque_curves, is_circular, method);

        c = curve;

    return;
}

void ThreePhaseFlowEquationFunctionLevelCurve::curve(double l, int component, std::vector<RealVector> &c){
    if (gv != 0){
        if      (component == WATER_FLUX) level_function = &fw_level_function;
        else if (component == OIL_FLUX)   level_function = &fo_level_function;
        else if (component == GAS_FLUX)   level_function = &fg_level_function;

        component_ = component;
        level_ = l;

        std::vector<RealVector> curve;
        std::vector< std::deque <RealVector> > deque_curves;
        std::vector <bool> is_circular;

        int method = SEGMENTATION_METHOD;
        int info = ContourMethod::contour2d(this, curve, deque_curves, is_circular, method);

        c = curve;
    }

    return;
}

double ThreePhaseFlowEquationFunctionLevelCurve::level(const RealVector &ref, int component){
    if      (component == WATER_FLUX) return fw_level_function(this, ref);
    else if (component == OIL_FLUX)   return fo_level_function(this, ref);
    else if (component == GAS_FLUX)   return fg_level_function(this, ref);
}

