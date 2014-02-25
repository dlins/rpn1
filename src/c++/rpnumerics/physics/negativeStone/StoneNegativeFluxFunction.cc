/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) StoneNegativeFluxFunction.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "StoneNegativeFluxFunction.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */

/* Para testar a nova interface de parametros de fluxo da stone*/
StoneNegativeFluxFunction::StoneNegativeFluxFunction(const StoneParams & params, const StoneNegativePermParams & permParams) : FluxFunction(FluxParams(params.params())), perm_(new StoneNegativePermeability(permParams)) {

}


StoneNegativeFluxFunction::StoneNegativeFluxFunction(const StoneNegativeFluxFunction & copy) : FluxFunction(copy.fluxParams()), perm_(new StoneNegativePermeability(copy.perm())) {

}

void StoneNegativeFluxFunction::setPermParams(const StoneNegativePermParams & permParams) {

    delete perm_;

    perm_=new StoneNegativePermeability(permParams);


}

RpFunction * StoneNegativeFluxFunction::clone() const {
    return new StoneNegativeFluxFunction(*this);
}

StoneNegativeFluxFunction::~StoneNegativeFluxFunction() {
    delete perm_;
}

int StoneNegativeFluxFunction::jet(const WaveState &w, JetMatrix &m, int degree) const {


    double grw = fluxParams().component(0);
    double grg = fluxParams().component(1);
    double gro = fluxParams().component(2);

    double muw = fluxParams().component(3);
    double mug = fluxParams().component(4);
    double muo = fluxParams().component(5);

    double vel = fluxParams().component(6);

    double sw = w(0);
    double so = w(1);
    double sg = 1.0 - sw - so;

    double kw, dkw_dsw, dkw_dso, d2kw_dsw2, d2kw_dswso, d2kw_dso2;
    perm_->Diff_PermabilityWater(sw, so, sg, kw, dkw_dsw, dkw_dso, d2kw_dsw2, d2kw_dswso, d2kw_dso2);

    double ko, dko_dsw, dko_dso, d2ko_dsw2, d2ko_dswso, d2ko_dso2;
    perm_->Diff_PermabilityOil(sw, so, sg, ko, dko_dsw, dko_dso, d2ko_dsw2, d2ko_dswso, d2ko_dso2);

    double kg, dkg_dsw, dkg_dso, d2kg_dsw2, d2kg_dswso, d2kg_dso2;
    perm_->Diff_PermabilityGas(sw, so, sg, kg, dkg_dsw, dkg_dso, d2kg_dsw2, d2kg_dswso, d2kg_dso2);

    double lkw = kw / muw; // Water mobility
    double lko = ko / muo; // Oil mobility
    double lkg = kg / mug; // Gas mobility

    double lk = lkw + lko + lkg; // Total mobility


    if (degree >= 0) {
        m.set(0, (lkw / lk) * (vel + lko * (grw - gro) + lkg * (grw - grg))); // fw
        m.set(1, (lko / lk) * (vel + lkw * (gro - grw) + lkg * (gro - grg))); // fo

        if (degree >= 1) {
            double ldkw_dsw = dkw_dsw / muw;
            double ldko_dsw = dko_dsw / muo;
            double ldkg_dsw = dkg_dsw / mug;
            double ldk_dsw = ldkw_dsw + ldko_dsw + ldkg_dsw;

            double ldkw_dso = dkw_dso / muw;
            double ldko_dso = dko_dso / muo;
            double ldkg_dso = dkg_dso / mug;
            double ldk_dso = ldkw_dso + ldko_dso + ldkg_dso;

            double ld2kw_dsw2 = d2kw_dsw2 / muw;
            double ld2kw_dswso = d2kw_dswso / muw;
            double ld2kw_dso2 = d2kw_dso2 / muw;

            double ld2ko_dsw2 = d2ko_dsw2 / muo;
            double ld2ko_dswso = d2ko_dswso / muo;
            double ld2ko_dso2 = d2ko_dso2 / muo; // Was:           double ld2ko_dso2= d2kw_dso2/ muo;

            double ld2kg_dsw2 = d2kg_dsw2 / mug;
            double ld2kg_dswso = d2kg_dswso / mug;
            double ld2kg_dso2 = d2kg_dso2 / mug;

            double to = vel + lkw * (gro - grw) + lkg * (gro - grg);
            double tw = vel + lko * (grw - gro) + lkg * (grw - grg);

            double dtodso = (gro - grg) * ldkg_dso;
            double dtodsw = (gro - grw) * ldkw_dsw + (gro - grg) * ldkg_dso;
            double dtwdso = (grw - gro) * ldko_dso + (grw - grg) * ldkg_dso;
            double dtwdsw = ldko_dsw * (grw - gro) + (grw - grg) * ldkg_dsw;

            double ld2k_dsw2 = ld2kw_dsw2 + ld2ko_dsw2 + ld2kg_dsw2;
            double ld2k_dswso = ld2kw_dswso + ld2ko_dswso + ld2kg_dswso;
            double ld2k_dso2 = ld2kw_dso2 + ld2ko_dso2 + ld2kg_dso2;

            double zgwdsw = (lk * ldkw_dsw - lkw * ldk_dsw) / (lk * lk);
            double zgwdso = (lk * ldkw_dso - lkw * ldk_dso) / (lk * lk);
            double zgodsw = (lk * ldko_dsw - lko * ldk_dsw) / (lk * lk);
            double zgodso = (lk * ldko_dso - lko * ldk_dso) / (lk * lk);

            m.set(0, 0, zgwdsw * tw + (lkw / lk) * dtwdsw); // dfw_dsw
            m.set(0, 1, zgwdso * tw + (lkw / lk) * dtwdso); // dfw_dso
            m.set(1, 0, zgodsw * to + (lko / lk) * dtodsw); // dfo_dsw
            m.set(1, 1, zgodso * to + (lko / lk) * dtodso); // dfo_dso

            if (degree == 2) {
                double zgfww = ((lk * ld2kw_dsw2 - lkw * ld2k_dsw2) / lk - 2. * ldk_dsw * zgwdsw) / lk;
                double zgfwo = ((lk * ld2kw_dswso - lkw * ld2k_dswso) / lk - (ldk_dsw * zgwdso + zgwdsw * ldk_dso)) / lk;
                double zgfoo = ((lk * ld2kw_dso2 - lkw * ld2k_dso2) / lk - 2. * ldk_dso * zgwdso) / lk;
                double zggww = ((lk * ld2ko_dsw2 - lko * ld2k_dsw2) / lk - 2. * ldk_dsw * zgodsw) / lk;
                double zggwo = ((lk * ld2ko_dswso - lko * ld2k_dswso) / lk - (ldk_dsw * zgodso + zgodsw * ldk_dso)) / lk;
                double zggoo = ((lk * ld2ko_dso2 - lko * ld2k_dso2) / lk - 2. * ldk_dso * zgodso) / lk;

                double dtwdww = ld2ko_dsw2 * (grw - gro) + (grw - grg) * ld2kg_dsw2;
                double dtwdwo = ld2ko_dswso * (grw - gro) + (grw - grg) * ld2kg_dswso;
                double dtwdoo = ld2ko_dso2 * (grw - gro) + (grw - grg) * ld2kg_dso2;
                double dtodww = ld2kw_dsw2 * (gro - grw) + (gro - grg) * ld2kg_dsw2;
                double dtodwo = (gro - grg) * ld2kg_dswso;
                double dtodoo = (gro - grg) * ld2kg_dso2;

                m.set(0, 0, 0, zgfww * tw + 2. * zgwdsw * dtwdsw + (lkw / lk) * dtwdww); // d2fw_dsw2;
                m.set(0, 0, 1, zgfwo * tw + zgwdsw * dtwdso + zgwdso * dtwdsw + (lkw / lk) * dtwdwo); // d2fw_dswso;
                m.set(0, 1, 0, zgfwo * tw + zgwdsw * dtwdso + zgwdso * dtwdsw + (lkw / lk) * dtwdwo); // d2fw_dsosw;
                m.set(0, 1, 1, zgfoo * tw + 2. * zgwdso * dtwdso + (lkw / lk) * dtwdoo); // d2fw_dso2;

                m.set(1, 0, 0, zggww * to + 2. * zgodsw * dtodsw + (lko / lk) * dtodww); // d2fo_dsw2;
                m.set(1, 0, 1, zggwo * to + zgodso * dtodsw + zgodsw * dtodso + (lko / lk) * dtodwo); // d2fo_dswso;
                m.set(1, 1, 0, zggwo * to + zgodso * dtodsw + zgodsw * dtodso + (lko / lk) * dtodwo); // d2fo_dsosw;
                m.set(1, 1, 1, zggoo * to + 2. * zgodso * dtodso + (lko / lk) * dtodoo); // d2fo_dso2;

            }
        } else return 0;
    }
    return 2; //SUCCESSFUL_PROCEDURE;
}




