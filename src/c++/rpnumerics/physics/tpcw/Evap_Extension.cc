#include "Evap_Extension.h"

Evap_Extension::Evap_Extension(const Flux2Comp2PhasesAdimensionalized *f, const Coincidence *c) : Extension(), flux_(f), coincidence(c) {
    // We assume that the component which is constant along the extension is the second one.
    //
    index_of_constant = 1;

    // We assume that the component which is NOT constant along the extension is the first one.
    //
    index_of_non_constant = 0;
}

Evap_Extension::~Evap_Extension(){
}

int Evap_Extension::extension(const RealVector &p, RealVector &ext_p){
    double fe, se;

    // Only works if fe, se != Inf.
    //
    if (coincidence->extension_basis(p, fe, se)){
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

        // TODO: Notice that from this point onwards all s_sigma_0 should be replaced by s_sigma_hat_0.
        // TODO: Helmut, check this.

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

        // TODO: Helmut, C0 has a hat or not?
        double C0 = s_sigma_0*s_sigma_0 + mu*(1.0 - s_sigma_0)*(1.0 - s_sigma_0);

        double alpha_e = (f_sigma_0 - fe)/(s_sigma_0 - se);
        double alpha_hat_e = alpha_e*s_hat_r;

        double A_Bhaskara = C0*alpha_hat_e + C0*alpha_hat_e*mu;
        double B_Bhaskara = -2.0*C0*alpha_hat_e*mu + 2.0*mu*s_sigma_0 - mu;
        double C_Bhaskara = C0*alpha_hat_e*mu - mu*s_sigma_0;

        double x1, x2;
        int info = Utilities::Bhaskara(B_Bhaskara/A_Bhaskara, C_Bhaskara/A_Bhaskara, x1, x2);

        if (info == BHASKARA_COMPLEX_ROOTS) return EXTENSION_ERROR;

        if ((x1 < 0.0 || x1 > 1.0) && (x2 < 0.0 || x2 > 1.0)) return EXTENSION_ERROR;

        double root;
        if      ((x1 < 0.0 || x1 > 1.0) && (x2 >= 0.0 || x2 <= 1.0)) root = x2;
        else if ((x2 < 0.0 || x2 > 1.0) && (x1 >= 0.0 || x1 <= 1.0)) root = x1;
        else root = (std::fabs(s_sigma_0 - x1) < std::fabs(s_sigma_0 - x2)) ? x1 : x2;

        ext_p(index_of_non_constant) = root*s_hat_r + cng;
    }

    return EXTENSION_OK;
}

//int Evap_Extension::evap_extension(const RealVector &p, RealVector &ext_p){
//    double fe, se;

//    // Only works if fe, se != Inf.
//    //
//    if (coincidence->extension_basis(p, fe, se)){
//        double expw = flux_->get_expw();
//        double expg = flux_->get_expg();

//        // Write a Newton-method-like method for the other cases.
//        //
//        if (expw != 2.0 || expg != 2.0) return false;

//        ext_p = p;

//        // TODO: Helmut has to check these.
//        //
//        double s_w_0 = 1.0 - p(index_of_non_constant); // TODO: In the case of TPCW, saturation. Helmut, is it "s_sigma_0" or "1.0 - s_sigma_0"???
//        double s_sigma_0 = p(index_of_non_constant); // TODO: In the case of TPCW, saturation. Helmut, is it "s_sigma_0" or "1.0 - s_sigma_0"???
//        double u0 = p(2);

//        // Given s0, fe, se, compute b, c:
//        //
//        JetMatrix Fjet(3);
//        Flux2Comp2PhasesAdimensionalized::FracFlow2PhasesHorizontalAdimensionalized *FH = flux_->getHorizontalFlux();

//        JetMatrix horizontal(2);
//        FH->Diff_FracFlow2PhasesHorizontalAdimensionalized(s_w_0, p(1), 0, horizontal); // TODO: In the case of TPCW, saturation. Helmut, is it "s_sigma_0" or "1.0 - s_sigma_0"???

//        // TODO: According to the documentation provided by Helmut, the viscosity ratio should be multiplied by .95/.5, or not.
//        double f_sigma_0 = horizontal.get(0);

//        Thermodynamics *thermo = flux_->getThermo();

//        double Theta = p(1);
//        double T = thermo->Theta2T(Theta);

//        double muw, dmuw_dT, d2muw_dT2;
//        thermo->muw(T, muw, dmuw_dT, d2muw_dT2);

//        double mug, dmug_dT, d2mug_dT2;
//        thermo->mug(T, mug, dmug_dT, d2mug_dT2);

//        double mu = (mug/muw)*(.5/.95);

//        // TODO: Helmut, C0 has a hat or not?
//        double C0 = s_sigma_0*s_sigma_0 + mu*(1.0 - s_sigma_0)*(1.0 - s_sigma_0);

//        double alpha_e = (f_sigma_0 - fe)/(s_sigma_0 - se);

//        double A_Bhaskara = C0*alpha_e + C0*alpha_e*mu;
//        double B_Bhaskara = -2.0*C0*alpha_e*mu + 2.0*mu*s_sigma_0 - mu;
//        double C_Bhaskara = C0*alpha_e*mu - mu*s_sigma_0;

//        double x1, x2;
//        int info = Utilities::Bhaskara(B_Bhaskara/A_Bhaskara, C_Bhaskara/A_Bhaskara, x1, x2);
//        std::cout << "After Bhaskara: x1 = " << x1 << ", x2 = " << x2 << std::endl;

////        x1 = 1.0 - x1;
////        x2 = 1.0 - x2;

//        if (info == BHASKARA_COMPLEX_ROOTS) return EVAP_EXTENSION_ERROR;

//        if ((x1 < 0.0 || x1 > 1.0) && (x2 < 0.0 || x2 > 1.0)) return EVAP_EXTENSION_ERROR;

//        double root;
//        if      ((x1 < 0.0 || x1 > 1.0) && (x2 >= 0.0 || x2 <= 1.0)) root = x2;
//        else if ((x2 < 0.0 || x2 > 1.0) && (x1 >= 0.0 || x1 <= 1.0)) root = x1;
//        else root = (std::fabs(s_sigma_0 - x1) < std::fabs(s_sigma_0 - x2)) ? x1 : x2;

//        std::cout << "The selected root was: " << root << std::endl;

//        ext_p(index_of_non_constant) = root;
//    }

//    return EVAP_EXTENSION_OK;
//}

