#include "TPCW_ExtSat_Extension.h"

TPCW_ExtSat_Extension::TPCW_ExtSat_Extension(const Flux2Comp2PhasesAdimensionalized *f) : Extension(), flux_(f) {
    // We assume that the component which is constant along the extension is the second one.
    //
    index_of_constant = 1;

    // We assume that the component which is NOT constant along the extension is the first one.
    //
    index_of_non_constant = 0;
}

TPCW_ExtSat_Extension::~TPCW_ExtSat_Extension(){
}

int TPCW_ExtSat_Extension::extension(const RealVector &p, RealVector &ext_p){
    double expw = flux_->get_expw();
    double expg = flux_->get_expg();

    // Write a Newton-method-like method for the other cases.
    //
    if (expw != 2.0 || expg != 2.0) return EXTENSION_ERROR;

    double cnw = flux_->get_cnw();
    double cng = flux_->get_cng();

    double s_hat_r = 1.0 + cnw + cng;

    ext_p = p;

    double s_w_0 = 1.0 - p(index_of_non_constant); // TODO: In the case of TPCW, saturation. Helmut, is it "s_sigma_0" or "1.0 - s_sigma_0"???
    double s_sigma_0 = p(index_of_non_constant); // TODO: In the case of TPCW, saturation. Helmut, is it "s_sigma_0" or "1.0 - s_sigma_0"???
    double u0 = p(2);

    // Given s0, fe, se, compute b, c:
    //
    double s_sigma_hat_0 = (s_sigma_0 - cng)/s_hat_r;

    JetMatrix Fjet(3);
    Flux2Comp2PhasesAdimensionalized::FracFlow2PhasesHorizontalAdimensionalized *FH = flux_->getHorizontalFlux();

    JetMatrix horizontal(2);
    FH->Diff_FracFlow2PhasesHorizontalAdimensionalized(s_w_0, p(1), 0, horizontal); // TODO: In the case of TPCW, saturation. Helmut, is it "s_sigma_0" or "1.0 - s_sigma_0"???

    // TODO: According to the documentation provided by Helmut, the viscosity ratio should be multiplied by .95/.5, or not.
    double f_sigma_0 = horizontal.get(0);

    Thermodynamics *thermo = flux_->getThermo();

    double Theta = p(1);
    double T = thermo->Theta2T(Theta);

    double muw, dmuw_dT, d2muw_dT2;
    thermo->muw(T, muw, dmuw_dT, d2muw_dT2);

    double mug, dmug_dT, d2mug_dT2;
    thermo->mug(T, mug, dmug_dT, d2mug_dT2);

    double mu = (mug/muw)*(.5/.95);

    double C0 = s_sigma_hat_0*s_sigma_hat_0 + mu*(1.0 - s_sigma_hat_0)*(1.0 - s_sigma_hat_0);

////    double invA = 1.0/((1.0 - 2.0*s_sigma_hat_0)*(1.0 + mu));
////    double B = invA*(2.0*mu*s_sigma_hat_0*(1.0 + mu));
////    double C = -invA*mu*mu;

////    double x1, x2;
////    int info = Utilities::Bhaskara(B, C, x1, x2);

    double C1 = 2.0*mu*s_sigma_hat_0*(1.0 - s_sigma_hat_0);
//    double A = C1*(1.0 + mu);
//    double B = C0*(2.0*mu*s_sigma_hat_0 - mu) - 2.0*mu*C1;
//    double C = mu*(C1 - s_sigma_hat_0*C0);

//    double x1, x2;
//    int info = Utilities::Bhaskara(A, B, C, x1, x2);

//    if (info == BHASKARA_COMPLEX_ROOTS) return EXTENSION_ERROR;

//    if ((x1 < 0.0 || x1 > 1.0) && (x2 < 0.0 || x2 > 1.0)) return EXTENSION_ERROR;

//    double root;
//    if      ((x1 < 0.0 || x1 > 1.0) && (x2 >= 0.0 || x2 <= 1.0)) root = x2;
//    else if ((x2 < 0.0 || x2 > 1.0) && (x1 >= 0.0 || x1 <= 1.0)) root = x1;
//    else root = (std::fabs(s_sigma_0 - x1) < std::fabs(s_sigma_0 - x2)) ? x1 : x2;

//    ext_p(index_of_non_constant) = root*s_hat_r + cng;
    ext_p(index_of_non_constant) = -(C0*(2.0*mu*s_sigma_hat_0 - mu) - 2.0*mu*C1 + s_sigma_hat_0*C1*(1.0 + mu))/(C1*(1.0 + mu));

    return EXTENSION_OK;
}

