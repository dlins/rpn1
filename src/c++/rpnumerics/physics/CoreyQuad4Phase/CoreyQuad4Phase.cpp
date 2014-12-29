#include "CoreyQuad4Phase.h"

CoreyQuad4Phase::CoreyQuad4Phase(const CoreyQuad4Phase_Params &param) : FluxFunction(param) {
    //TODO USAR OS ELEMENTOS DO VETOR GUARDADO EM FLUXFUNCTION
}

CoreyQuad4Phase * CoreyQuad4Phase::clone() const {
    return new CoreyQuad4Phase(*this);
}

CoreyQuad4Phase::CoreyQuad4Phase(const CoreyQuad4Phase & copy) : FluxFunction(copy.fluxParams()) {
}

CoreyQuad4Phase::~CoreyQuad4Phase() {
}

int CoreyQuad4Phase::jet(const WaveState &w, JetMatrix &m, int degree) const {

    double grw = fluxParams().component(0);
    double grg = fluxParams().component(1);
    double gro = fluxParams().component(2);
    double grc = fluxParams().component(3);

    double muw = fluxParams().component(4);
    double mug = fluxParams().component(5);
    double muo = fluxParams().component(6);
    double muc = fluxParams().component(7);

    double vel = fluxParams().component(8);

//    double krw_p = fluxParams().component(9);
//    double krg_p = fluxParams().component(10);
//    double kro_p = fluxParams().component(11);
//    double krc_p = fluxParams().component(12);
//
//    double cnw = fluxParams().component(13);
//    double cng = fluxParams().component(14);
//    double cno = fluxParams().component(15);
//    double cnc = fluxParams().component(16);
//    double CN = cnw + cng + cno + cnc;

    double sw = w(0);
    double so = w(1);
    double sg = w(2);
    double sc = 1.0 - sw - so - sg;

    double kw, dkw_dsw, dkw_dso, dkw_dsg;
    double d2kw_dsw2, d2kw_dswso, d2kw_dswsg,
           d2kw_dso2, d2kw_dsosg, d2kw_dsg2;
//    double swcnw = sw - cnw;
//    if (swcnw < 0.){
//        kw         = 0.;
//        dkw_dsw    = 0.;
//        dkw_dso    = 0.;

//        d2kw_dsw2  = 0.;
//        d2kw_dswso = 0.;
//        d2kw_dso2  = 0.;
//    }
//    else if (swcnw > 1. - CN) {
//        kw         = krw_p;
//        dkw_dsw    = 0.;
//        dkw_dso    = 0.;

//        d2kw_dsw2  = 0.;
//        d2kw_dswso = 0.;
//        d2kw_dso2  = 0.;
//    }
//    else {
//        double denkw = krw_p/((1. - CN)*(1. - CN));
        kw         = sw * sw;
        dkw_dsw    = 2. * sw;
        dkw_dso    = 0.;
        dkw_dsg    = 0.;

        d2kw_dsw2  = 2.;
        d2kw_dswso = 0.;
        d2kw_dswsg = 0.;
        d2kw_dso2  = 0.;
        d2kw_dsosg = 0.;
        d2kw_dsg2  = 0.;
//    }

    double ko, dko_dsw, dko_dso, dko_dsg;
    double d2ko_dsw2, d2ko_dswso, d2ko_dswsg,
           d2ko_dso2, d2ko_dsosg, d2ko_dsg2;
//    double socno = so - cno;
//    if (socno < 0.){
//        ko         = 0.;
//        dko_dsw    = 0.;
//        dko_dso    = 0.;

//        d2ko_dsw2  = 0.;
//        d2ko_dswso = 0.;
//        d2ko_dso2  = 0.;
//    }
//    else if (socno > 1. - CN) {
//        ko         = kro_p;
//        dko_dsw    = 0.;
//        dko_dso    = 0.;

//        d2ko_dsw2  = 0.;
//        d2ko_dswso = 0.;
//        d2ko_dso2  = 0.;
//    }
//    else {
//        double denko = kro_p/((1. - CN)*(1. - CN));
        ko         = so * so;
        dko_dsw    = 0.;
        dko_dso    = 2. * so;
        dko_dsg    = 0.;

        d2ko_dsw2  = 0.;
        d2ko_dswso = 0.;
        d2ko_dswsg = 0.;
        d2ko_dso2  = 2.;
        d2ko_dsosg = 0.;
        d2ko_dsg2  = 0.;
//    }

    double kg, dkg_dsw, dkg_dso, dkg_dsg;
    double d2kg_dsw2, d2kg_dswso, d2kg_dswsg,
           d2kg_dso2, d2kg_dsosg, d2kg_dsg2;
//    double sgcng = sg - cng;
//    if (sgcng < 0.){
//        kg         = 0.;
//        dkg_dsw    = 0.;
//        dkg_dso    = 0.;

//        d2kg_dsw2  = 0.;
//        d2kg_dswso = 0.;
//        d2kg_dso2  = 0.;
//    }
//    else if (sgcng > 1. - CN){
//        kg         = krg_p;
//        dkg_dsw    = 0.;
//        dkg_dso    = 0.;

//        d2kg_dsw2  = 0.;
//        d2kg_dswso = 0.;
//        d2kg_dso2  = 0.;
//    }
//    else {
//        double denkg = krg_p/((1. - CN)*(1. - CN));
        kg         = sg * sg;
        dkg_dsw    = 0.;
        dkg_dso    = 0.;
        dkg_dsg    = 2. * sg;

        d2kg_dsw2  = 0.;
        d2kg_dswso = 0.;
        d2kg_dswsg = 0.;
        d2kg_dso2  = 0.;
        d2kg_dsosg = 0.;
        d2kg_dsg2  = 2.;
//    }

    double kc, dkc_dsw, dkc_dso, dkc_dsg;
    double d2kc_dsw2, d2kc_dswso, d2kc_dswsg,
           d2kc_dso2, d2kc_dsosg, d2kc_dsg2;
//    double sgcng = sg - cng;
//    if (sgcng < 0.){
//        kc         = 0.;
//        dkc_dsw    = 0.;
//        dkc_dso    = 0.;

//        d2kc_dsw2  = 0.;
//        d2kc_dswso = 0.;
//        d2kc_dso2  = 0.;
//    }
//    else if (sgcng > 1. - CN){
//        kc         = krg_p;
//        dkc_dsw    = 0.;
//        dkc_dso    = 0.;

//        d2kc_dsw2  = 0.;
//        d2kc_dswso = 0.;
//        d2kc_dso2  = 0.;
//    }
//    else {
//        double denkc = krg_p/((1. - CN)*(1. - CN));
        kc         = sc * sc;
        dkc_dsw    = - 2. * sc;
        dkc_dso    = - 2. * sc;
        dkc_dsg    = - 2. * sc;

        d2kc_dsw2  = 2.;
        d2kc_dswso = 2.;
        d2kc_dswsg = 2.;
        d2kc_dso2  = 2.;
        d2kc_dsosg = 2.;
        d2kc_dsg2  = 2.;
//    }


    if (degree >= 0) {
        double lw = kw / muw; // Water mobility
        double lo = ko / muo; // Oil mobility
        double lg = kg / mug; // Gas mobility
        double lc = kc / muc; // CO_2 mobility

        double l = lw + lo + lg + lc; // Total mobility

        double zw = lw / l;
        double zo = lo / l;
        double zg = lg / l;

        double vw = vel + lo * (grw - gro) + lg * (grw - grg) + lc * (grw - grc);
        double vo = vel + lw * (gro - grw) + lg * (gro - grg) + lc * (gro - grc);
        double vg = vel + lw * (grg - grw) + lo * (grg - gro) + lc * (grg - grc);

        m.set(0, zw * vw); // fw
        m.set(1, zo * vo); // fo
        m.set(2, zg * vg); // fg

        if (degree >= 1) {
            double dlw_dsw = dkw_dsw / muw;
            double dlo_dsw = dko_dsw / muo;
            double dlg_dsw = dkg_dsw / mug;
            double dlc_dsw = dkc_dsw / muc;

            double dlw_dso = dkw_dso / muw;
            double dlo_dso = dko_dso / muo;
            double dlg_dso = dkg_dso / mug;
            double dlc_dso = dkc_dso / muc;

            double dlw_dsg = dkw_dsg / muw;
            double dlo_dsg = dko_dsg / muo;
            double dlg_dsg = dkg_dsg / mug;
            double dlc_dsg = dkc_dsg / muc;

            double dl_dsw = dlw_dsw + dlo_dsw + dlg_dsw + dlc_dsw;
            double dl_dso = dlw_dso + dlo_dso + dlg_dso + dlc_dso;
            double dl_dsg = dlw_dsg + dlo_dsg + dlg_dsg + dlc_dsg;

            double dzw_dsw = (l * dlw_dsw - lw * dl_dsw) / (l * l);
            double dzw_dso = (l * dlw_dso - lw * dl_dso) / (l * l);
            double dzw_dsg = (l * dlw_dsg - lw * dl_dsg) / (l * l);

            double dzo_dsw = (l * dlo_dsw - lo * dl_dsw) / (l * l);
            double dzo_dso = (l * dlo_dso - lo * dl_dso) / (l * l);
            double dzo_dsg = (l * dlo_dsg - lo * dl_dsg) / (l * l);

            double dzg_dsw = (l * dlg_dsw - lg * dl_dsw) / (l * l);
            double dzg_dso = (l * dlg_dso - lg * dl_dso) / (l * l);
            double dzg_dsg = (l * dlg_dsg - lg * dl_dsg) / (l * l);

            double dvw_dsw = dlo_dsw * (grw - gro) + dlg_dsw * (grw - grg) + dlc_dsw * (grw - grc);
            double dvw_dso = dlo_dso * (grw - gro) + dlg_dso * (grw - grg) + dlc_dso * (grw - grc);
            double dvw_dsg = dlo_dsg * (grw - gro) + dlg_dsg * (grw - grg) + dlc_dsg * (grw - grc);

            double dvo_dsw = dlw_dsw * (gro - grw) + dlg_dsw * (gro - grg) + dlc_dsw * (gro - grc);
            double dvo_dso = dlw_dso * (gro - grw) + dlg_dso * (gro - grg) + dlc_dso * (gro - grc);
            double dvo_dsg = dlw_dsg * (gro - grw) + dlg_dsg * (gro - grg) + dlc_dsg * (gro - grc);

            double dvg_dsw = dlw_dsw * (grg - grw) + dlo_dsw * (grg - gro) + dlc_dsw * (grg - grc);
            double dvg_dso = dlw_dso * (grg - grw) + dlo_dso * (grg - gro) + dlc_dso * (grg - grc);
            double dvg_dsg = dlw_dsg * (grg - grw) + dlo_dsg * (grg - gro) + dlc_dsg * (grg - grc);

            m.set(0, 0, dzw_dsw * vw + zw * dvw_dsw); // dfw_dsw
            m.set(0, 1, dzw_dso * vw + zw * dvw_dso); // dfw_dso
            m.set(0, 2, dzw_dsg * vw + zw * dvw_dsg); // dfw_dsg
            m.set(1, 0, dzo_dsw * vo + zo * dvo_dsw); // dfo_dsw
            m.set(1, 1, dzo_dso * vo + zo * dvo_dso); // dfo_dso
            m.set(1, 2, dzo_dsg * vo + zo * dvo_dsg); // dfo_dsg
            m.set(2, 0, dzg_dsw * vg + zg * dvg_dsw); // dfg_dsw
            m.set(2, 1, dzg_dso * vg + zg * dvg_dso); // dfg_dso
            m.set(2, 2, dzg_dsg * vg + zg * dvg_dsg); // dfg_dsg

            if (degree == 2) {
                double d2lw_dsw2  = d2kw_dsw2 / muw;
                double d2lw_dswso = d2kw_dswso / muw;
                double d2lw_dswsg = d2kw_dswsg / muw;
                double d2lw_dso2  = d2kw_dso2 / muw;
                double d2lw_dsosg = d2kw_dsosg / muw;
                double d2lw_dsg2  = d2kw_dsg2 / muw;

                double d2lo_dsw2  = d2ko_dsw2 / muo;
                double d2lo_dswso = d2ko_dswso / muo;
                double d2lo_dswsg = d2ko_dswsg / muo;
                double d2lo_dso2  = d2ko_dso2 / muo;
                double d2lo_dsosg = d2ko_dsosg / muo;
                double d2lo_dsg2  = d2ko_dsg2 / muo;

                double d2lg_dsw2  = d2kg_dsw2 / mug;
                double d2lg_dswso = d2kg_dswso / mug;
                double d2lg_dswsg = d2kg_dswsg / mug;
                double d2lg_dso2  = d2kg_dso2 / mug;
                double d2lg_dsosg = d2kg_dsosg / mug;
                double d2lg_dsg2  = d2kg_dsg2 / mug;

                double d2lc_dsw2  = d2kc_dsw2 / muc;
                double d2lc_dswso = d2kc_dswso / muc;
                double d2lc_dswsg = d2kc_dswsg / muc;
                double d2lc_dso2  = d2kc_dso2 / muc;
                double d2lc_dsosg = d2kc_dsosg / muc;
                double d2lc_dsg2  = d2kc_dsg2 / muc;

                double d2l_dsw2  = d2lw_dsw2 + d2lo_dsw2 + d2lg_dsw2;
                double d2l_dswso = d2lw_dswso + d2lo_dswso + d2lg_dswso;
                double d2l_dswsg = d2lw_dswsg + d2lo_dswsg + d2lg_dswsg;
                double d2l_dso2  = d2lw_dso2 + d2lo_dso2 + d2lg_dso2;
                double d2l_dsosg = d2lw_dsosg + d2lo_dsosg + d2lg_dsosg;
                double d2l_dsg2  = d2lw_dsg2 + d2lo_dsg2 + d2lg_dsg2;

                double d2zw_dsw2 = ((l * d2lw_dsw2 - lw * d2l_dsw2) / l - 2. * dl_dsw * dzw_dsw) / l;
                double d2zw_dswso = ((l * d2lw_dswso - lw * d2l_dswso) / l - (dl_dsw * dzw_dso + dzw_dsw * dl_dso)) / l;
                double d2zw_dswsg = ((l * d2lw_dswsg - lw * d2l_dswsg) / l - (dl_dsw * dzw_dsg + dzw_dsw * dl_dsg)) / l;
                double d2zw_dso2 = ((l * d2lw_dso2 - lw * d2l_dso2) / l - 2. * dl_dso * dzw_dso) / l;
                double d2zw_dsosg = ((l * d2lw_dsosg - lw * d2l_dsosg) / l - (dl_dso * dzw_dsg + dzw_dso * dl_dsg)) / l;
                double d2zw_dsg2 = ((l * d2lw_dsg2 - lw * d2l_dsg2) / l - 2. * dl_dsg * dzw_dsg) / l;

                double d2zo_dsw2 = ((l * d2lo_dsw2 - lo * d2l_dsw2) / l - 2. * dl_dsw * dzo_dsw) / l;
                double d2zo_dswso = ((l * d2lo_dswso - lo * d2l_dswso) / l - (dl_dsw * dzo_dso + dzo_dsw * dl_dso)) / l;
                double d2zo_dswsg = ((l * d2lo_dswsg - lo * d2l_dswsg) / l - (dl_dsw * dzo_dsg + dzo_dsw * dl_dsg)) / l;
                double d2zo_dso2 = ((l * d2lo_dso2 - lo * d2l_dso2) / l - 2. * dl_dso * dzo_dso) / l;
                double d2zo_dsosg = ((l * d2lo_dsosg - lo * d2l_dsosg) / l - (dl_dso * dzo_dsg + dzo_dso * dl_dsg)) / l;
                double d2zo_dsg2 = ((l * d2lo_dsg2 - lo * d2l_dsg2) / l - 2. * dl_dsg * dzo_dsg) / l;

                double d2zg_dsw2 = ((l * d2lg_dsw2 - lg * d2l_dsw2) / l - 2. * dl_dsw * dzg_dsw) / l;
                double d2zg_dswso = ((l * d2lg_dswso - lg * d2l_dswso) / l - (dl_dsw * dzg_dso + dzg_dsw * dl_dso)) / l;
                double d2zg_dswsg = ((l * d2lg_dswsg - lg * d2l_dswsg) / l - (dl_dsw * dzg_dsg + dzg_dsw * dl_dsg)) / l;
                double d2zg_dso2 = ((l * d2lg_dso2 - lg * d2l_dso2) / l - 2. * dl_dso * dzg_dso) / l;
                double d2zg_dsosg = ((l * d2lg_dsosg - lg * d2l_dsosg) / l - (dl_dso * dzg_dsg + dzg_dso * dl_dsg)) / l;
                double d2zg_dsg2 = ((l * d2lg_dsg2 - lg * d2l_dsg2) / l - 2. * dl_dsg * dzg_dsg) / l;

                double dvw_dsw2 = d2lo_dsw2 * (grw - gro) + d2lg_dsw2 * (grw - grg) + d2lc_dsw2 * (grw - grc);
                double dvw_dswso = d2lo_dswso * (grw - gro) + d2lg_dswso * (grw - grg) + d2lc_dswso * (grw - grc);
                double dvw_dswsg = d2lo_dswsg * (grw - gro) + d2lg_dswsg * (grw - grg) + d2lc_dswsg * (grw - grc);
                double dvw_dso2 = d2lo_dso2 * (grw - gro) + d2lg_dso2 * (grw - grg) + d2lc_dso2 * (grw - grc);
                double dvw_dsosg = d2lo_dsosg * (grw - gro) + d2lg_dsosg * (grw - grg) + d2lc_dsosg * (grw - grc);
                double dvw_dsg2 = d2lo_dsg2 * (grw - gro) + d2lg_dsg2 * (grw - grg) + d2lc_dsg2 * (grw - grc);

                double dvo_dsw2 = d2lw_dsw2 * (gro - grw) + d2lg_dsw2 * (gro - grg) + d2lc_dsw2 * (gro - grc);
                double dvo_dswso = d2lw_dswso * (gro - grw) + d2lg_dswso * (gro - grg) + d2lc_dswso * (gro - grc);
                double dvo_dswsg = d2lw_dswsg * (gro - grw) + d2lg_dswsg * (gro - grg) + d2lc_dswsg * (gro - grc);
                double dvo_dso2 = d2lw_dso2 * (gro - grw) + d2lg_dso2 * (gro - grg) + d2lc_dso2 * (gro - grc);
                double dvo_dsosg = d2lw_dsosg * (gro - grw) + d2lg_dsosg * (gro - grg) + d2lc_dsosg * (gro - grc);
                double dvo_dsg2 = d2lw_dsg2 * (gro - grw) + d2lg_dsg2 * (gro - grg) + d2lc_dsg2 * (gro - grc);

                double dvg_dsw2 = d2lw_dsw2 * (grg - grw) + d2lo_dsw2 * (grg - gro) + d2lc_dsw2 * (grg - grc);
                double dvg_dswso = d2lw_dswso * (grg - grw) + d2lo_dswso * (grg - gro) + d2lc_dswso * (grg - grc);
                double dvg_dswsg = d2lw_dswsg * (grg - grw) + d2lo_dswsg * (grg - gro) + d2lc_dswsg * (grg - grc);
                double dvg_dso2 = d2lw_dso2 * (grg - grw) + d2lo_dso2 * (grg - gro) + d2lc_dso2 * (grg - grc);
                double dvg_dsosg = d2lw_dsosg * (grg - grw) + d2lo_dsosg * (grg - gro) + d2lc_dsosg * (grg - grc);
                double dvg_dsg2 = d2lw_dsg2 * (grg - grw) + d2lo_dsg2 * (grg - gro) + d2lc_dsg2 * (grg - grc);

                m.set(0, 0, 0, d2zw_dsw2 * vw + 2. * dzw_dsw * dvw_dsw + zw * dvw_dsw2); // d2fw_dsw2;
                m.set(0, 0, 1, d2zw_dswso * vw + dzw_dsw * dvw_dso + dzw_dso * dvw_dsw + zw * dvw_dswso); // d2fw_dswso;
                m.set(0, 0, 2, d2zw_dswsg * vw + dzw_dsw * dvw_dsg + dzw_dsg * dvw_dsw + zw * dvw_dswsg); // d2fw_dswsg;
                m.set(0, 1, 0, d2zw_dswso * vw + dzw_dsw * dvw_dso + dzw_dso * dvw_dsw + zw * dvw_dswso); // d2fw_dswso;
                m.set(0, 1, 1, d2zw_dso2 * vw + 2. * dzw_dso * dvw_dso + zw * dvw_dso2); // d2fw_dso2;
                m.set(0, 1, 2, d2zw_dsosg * vw + dzw_dso * dvw_dsg + dzw_dsg * dvw_dso + zw * dvw_dsosg); // d2fw_dsosg;
                m.set(0, 2, 0, d2zw_dswsg * vw + dzw_dsw * dvw_dsg + dzw_dsg * dvw_dsw + zw * dvw_dswsg); // d2fw_dswsg;
                m.set(0, 2, 1, d2zw_dsosg * vw + dzw_dso * dvw_dsg + dzw_dsg * dvw_dso + zw * dvw_dsosg); // d2fw_dsosg;
                m.set(0, 2, 2, d2zw_dsg2 * vw + 2. * dzw_dsg * dvw_dsg + zw * dvw_dsg2); // d2fw_dsg2;

                m.set(1, 0, 0, d2zo_dsw2 * vo + 2. * dzo_dsw * dvo_dsw + zo * dvo_dsw2); // d2fo_dsw2;
                m.set(1, 0, 1, d2zo_dswso * vo + dzo_dsw * dvo_dso + dzw_dso * dvo_dsw + zo * dvo_dswso); // d2fo_dswso;
                m.set(1, 0, 2, d2zo_dswsg * vo + dzo_dsw * dvo_dsg + dzw_dsg * dvo_dsw + zo * dvo_dswsg); // d2fo_dswsg;
                m.set(1, 1, 0, d2zo_dswso * vo + dzo_dsw * dvo_dso + dzw_dso * dvo_dsw + zo * dvo_dswso); // d2fo_dswso;
                m.set(1, 1, 1, d2zo_dso2 * vo + 2. * dzo_dso * dvo_dso + zo * dvo_dso2); // d2fo_dso2;
                m.set(1, 1, 2, d2zo_dsosg * vo + dzo_dso * dvo_dsg + dzw_dsg * dvo_dso + zo * dvo_dsosg); // d2fo_dsosg;
                m.set(1, 2, 0, d2zo_dswsg * vo + dzo_dsw * dvo_dsg + dzw_dsg * dvo_dsw + zo * dvo_dswsg); // d2fo_dswsg;
                m.set(1, 2, 1, d2zo_dsosg * vo + dzo_dso * dvo_dsg + dzw_dsg * dvo_dso + zo * dvo_dsosg); // d2fo_dsosg;
                m.set(1, 2, 2, d2zo_dsg2 * vo + 2. * dzo_dsg * dvo_dsg + zo * dvo_dsg2); // d2fo_dsg2;

                m.set(2, 0, 0, d2zg_dsw2 * vg + 2. * dzg_dsw * dvg_dsw + zg * dvg_dsw2); // d2fg_dsw2;
                m.set(2, 0, 1, d2zg_dswso * vg + dzg_dsw * dvg_dso + dzg_dso * dvg_dsw + zg * dvg_dswso); // d2fg_dswso;
                m.set(2, 0, 2, d2zg_dswsg * vg + dzg_dsw * dvg_dsg + dzg_dsg * dvg_dsw + zg * dvg_dswsg); // d2fg_dswsg;
                m.set(2, 1, 0, d2zg_dswso * vg + dzg_dsw * dvg_dso + dzg_dso * dvg_dsw + zg * dvg_dswso); // d2fg_dswso;
                m.set(2, 1, 1, d2zg_dso2 * vg + 2. * dzg_dso * dvg_dso + zg * dvg_dso2); // d2fg_dso2;
                m.set(2, 1, 2, d2zg_dsosg * vg + dzg_dso * dvg_dsg + dzg_dsg * dvg_dso + zg * dvg_dsosg); // d2fg_dsosg;
                m.set(2, 2, 0, d2zg_dswsg * vg + dzg_dsw * dvg_dsg + dzg_dsg * dvg_dsw + zg * dvg_dswsg); // d2fg_dswsg;
                m.set(2, 2, 1, d2zg_dsosg * vg + dzg_dso * dvg_dsg + dzg_dsg * dvg_dso + zg * dvg_dsosg); // d2fg_dsosg;
                m.set(2, 2, 2, d2zg_dsg2 * vg + 2. * dzg_dsg * dvg_dsg + zg * dvg_dsg2); // d2fg_dsg2;
            }
        }
    }
    return 2; //SUCCESSFUL_PROCEDURE;
}
