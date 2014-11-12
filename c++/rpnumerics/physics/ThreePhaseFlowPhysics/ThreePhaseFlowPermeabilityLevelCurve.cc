#include "ThreePhaseFlowPermeabilityLevelCurve.h"

ThreePhaseFlowPermeabilityLevelCurve::ThreePhaseFlowPermeabilityLevelCurve(ThreePhaseFlowSubPhysics *s): ImplicitFunction(), subphysics_(s), permeability_(s->permeability()) {
}

ThreePhaseFlowPermeabilityLevelCurve::ThreePhaseFlowPermeabilityLevelCurve(ThreePhaseFlowSubPhysics *s, ThreePhaseFlowPermeability *p): ImplicitFunction(), subphysics_(s), permeability_(p) {
}

ThreePhaseFlowPermeabilityLevelCurve::~ThreePhaseFlowPermeabilityLevelCurve(){
}

double ThreePhaseFlowPermeabilityLevelCurve::water_permeability(ThreePhaseFlowPermeabilityLevelCurve *obj, const RealVector &p){
    JetMatrix permj;
    obj->permeability_->PermeabilityWater_jet(p, 0, permj);

    return permj.get(0);
}

double ThreePhaseFlowPermeabilityLevelCurve::oil_permeability(ThreePhaseFlowPermeabilityLevelCurve *obj, const RealVector &p){
    JetMatrix permj;
    obj->permeability_->PermeabilityOil_jet(p, 0, permj);

    return permj.get(0);
}

double ThreePhaseFlowPermeabilityLevelCurve::gas_permeability(ThreePhaseFlowPermeabilityLevelCurve *obj, const RealVector &p){
    JetMatrix permj;
    obj->permeability_->PermeabilityGas_jet(p, 0, permj);

    return permj.get(0);
}

int ThreePhaseFlowPermeabilityLevelCurve::function_on_square(double *foncub, int i, int j){
    int is_square = gv->cell_type(i, j);
    double f_aux[4];

    for (int l = 0; l < 2; l++) {
        for (int k = 0; k < 2; k++) {
            f_aux[l * 2 + k] = (*permeabilityfunction)(this, gv->grid(i + l, j + k)) - level_;
        }
    }

    foncub[1] = f_aux[0]; // Was: foncub[0][1]
    foncub[0] = f_aux[2]; // Was: foncub[0][0]
    foncub[3] = f_aux[1]; // Was: foncub[0][2]

    // Only useful if the cell is a square.
    //
    if (is_square == CELL_IS_SQUARE) foncub[2] = f_aux[3]; // Was: foncub[0][2]

    return 1;
}

void ThreePhaseFlowPermeabilityLevelCurve::curve(const RealVector &ref, int type, std::vector<RealVector> &c){
    gv = subphysics_->gridvalues();

    if (gv != 0){
        if      (type == WATER_PERMEABILITY_CURVE) permeabilityfunction = &water_permeability;
        else if (type == OIL_PERMEABILITY_CURVE)   permeabilityfunction = &oil_permeability;
        else if (type == GAS_PERMEABILITY_CURVE)   permeabilityfunction = &gas_permeability;

        level_ = (*permeabilityfunction)(this, ref);

        std::vector<RealVector> curve;
        std::vector< std::deque <RealVector> > deque_curves;
        std::vector <bool> is_circular;

        int method = SEGMENTATION_METHOD;
        int info = ContourMethod::contour2d(this, curve, deque_curves, is_circular, method);

        c = curve;

//        for (int i = 0; i < hugoniot_curve.size()/2; i++){
//            Curve temp;
//            temp.curve.push_back(hugoniot_curve[2*i]);
//            temp.curve.push_back(hugoniot_curve[2*i + 1]);

//            c.push_back(temp);
//        }
    }

    return;
}

double ThreePhaseFlowPermeabilityLevelCurve::level(const RealVector &ref, int type){
    if      (type == WATER_PERMEABILITY_CURVE) return water_permeability(this, ref);
    else if (type == OIL_PERMEABILITY_CURVE)   return oil_permeability(this, ref);
    else if (type == GAS_PERMEABILITY_CURVE)   return gas_permeability(this, ref);
}

