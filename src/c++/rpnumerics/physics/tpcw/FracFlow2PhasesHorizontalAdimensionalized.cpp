#include "FracFlow2PhasesHorizontalAdimensionalized.h"

FracFlow2PhasesHorizontalAdimensionalized::FracFlow2PhasesHorizontalAdimensionalized(double cnw_, double cng_, double expw_, double expg_,
        Thermodynamics_SuperCO2_WaterAdimensionalized * TD_) : cnw(cnw_),
cng(cng_),
expw(expw_),
expg(expg_),
TD(TD_) {
    T_typical_ = TD->T_typical();
}

FracFlow2PhasesHorizontalAdimensionalized::~FracFlow2PhasesHorizontalAdimensionalized() {

}

int FracFlow2PhasesHorizontalAdimensionalized::Diff_FracFlow2PhasesHorizontalAdimensionalized(double sw, double Theta, int degree, JetMatrix &m) {
    // Esta funcao retorna a funcao de fluxo da agua e seu jato; 
    // esta correta para uso do Helmut apenas
    // ja tiramos grw, grg de todos os lugares e as variaveis que as usavam    

    double T = TD->Theta2T(Theta);

    // Auxiliary variables
    double sg, law, lag;
    double dlaw_dsw, dlag_dsw;
    double d2law_dsw2, d2lag_dsw2;
    double la, dla_dsw, d2la_dsw2;
    double dflw_dsw, dflw_dT; // Derivatives of the Fractional Flow of water       
    double d2flw_dsw2; // d2flw_dsw2 // Second derivative of Fractional Flow of water

    double flw; // Fractional Flow for water

    // Some variables to facilitate the operations with powers
    double pow0, pow1, pow2;

    double nuw, nug; // nuw= 1/muw , nug=1/mug
    double dnuw_dT, d2nuw_dT2; // This values are assigned using TD
    // dnuw_dT = dnuw_dT(T).
    // d2nuw_dT2 = d2nuw_dT2(T).

    double dnug_dT, d2nug_dT2; // This values are assigned using TD
    // dnug_dT = dnug_dT(T),
    // d2nug_dT2 = d2nug_dT2(T).

    double muw, dmuw_dT, d2muw_dT2;
    double mug, dmug_dT, d2mug_dT2;

    double dlaw_dT, d2law_dT2; // Temperature derivatives of law (are not passed as parameters).

    double dlag_dT, d2lag_dT2; // Temperature derivatives of lag (are not passed as parameters).

    double d2law_dswdT; // Mixed derivative (it is not passed as parameter).
    double d2lag_dswdT; // Mixed derivative (it is not passed as parameter).

    double dla_dT;
    double d2flw_dswdT;
    double d2la_dswdT;
    double d2flw_dT2;
    double d2la_dT2;

    // Finally we assign sg
    sg = 1. - sw;

    TD->inv_muw(T, nuw, dnuw_dT, d2nuw_dT2);
    TD->inv_mug(T, nug, dnug_dT, d2nug_dT2);

    TD->muw(T, muw, dmuw_dT, d2muw_dT2);
    TD->mug(T, mug, dmug_dT, d2mug_dT2);


    // Before evaluating proper
    if (sw < cnw) {
        law = 0.;
        dlaw_dsw = 0.;
        d2law_dsw2 = 0.;

        dlaw_dT = 0.;
        d2law_dT2 = 0.;

        d2law_dswdT = 0.; // remember that d2law_dswdT is equal to d2law_dTdsw
    } else {

        double temp = (sw - cnw) / (1. - cnw - cng);

        pow2 = 0.5 * pow(temp, expw - 2.); // 0.5*( (sw-s_{connate water})/(1-s_{connate water}-s_{con gas}) )^{expw-2}
        // This model coincides with the formulae in Helmut's Thesis. 0.5 is the re-scaling factor.

        pow1 = temp*pow2; // 0.5*( (sw-s_{connate water})/(1-s_{connate water}-s_{con gas}) )^{expw-1}
        // Observe that pow1 = pow((sw - cnw)/(1.-cnw-cng),expw-1) ;

        pow0 = temp*pow1; // 0.5*( (sw-s_{connate water})/(1-s_{connate water-s_{con gas}}) )^{expw}
        // Observe that pow0 = pow((sw - cnw)/(1.-cnw-cng),expw)


        law = pow0*nuw; // k_{rw}/mu_{w}(T) This is equivalent to k_{ra}/mu_{a} in Helmut's thesis.
        dlaw_dsw = expw * pow1*nuw; // expw*( (sw-s_{connate water })/(1.-cnw-cng) )^{expw-1}/mu_{w}  derivation w.r.t sw
        d2law_dsw2 = (expw - 1.) * expw * pow2*nuw; // (expw - 1.) * expw*( (sw-s_{connate water })/(1.-cnw-cng) )^{expw-2}/mu_{w}

        dlaw_dT = pow0*dnuw_dT; // the value of the derivatives of muw
        // will be passed by the viscosity function.
        d2law_dT2 = pow0*d2nuw_dT2; //

        d2law_dswdT = expw * pow1*dnuw_dT;
    }

    if (sg < cng) {
        lag = 0.;
        dlag_dsw = 0.;
        d2lag_dsw2 = 0.;

        dlag_dT = 0.;
        d2lag_dT2 = 0.;

        d2lag_dswdT = 0.;
    } else {

        double temp = (sg - cng) / (1. - cnw - cng);
        pow2 = 0.95 * pow(temp, expg - 2.); // ( (sg-s_{connate gas})/(1.-cnw-cng) )^{expg-2}
        pow1 = temp*pow2; // ( (sg-s_{connate gas})/(1.-cnw-cng) )^{expg-1} Observe that pow1 = pow(temp, expg-1)
        pow0 = temp*pow1; // ( (sg-s_{connate gas})/(1.-cnw-cng) )^{expg}   Observe that pow0 = pow(temp, expg) ;

        lag = pow0*nug; //  k_{rg}/mu_{g}(T)  this viscosity is function of temperature   ;
        dlag_dsw = -expg * pow1*nug; //  - expg*( (sg-s_{connate gas})/(1.-cnw-cng) )^{expg-1}/mu_{g}
        d2lag_dsw2 = (expg - 1.) * expg * pow2*nug; // (expg-1)*expg*( (sg-s_{connate gas})/(1.-cnw-cng) )^{expg-2}/mu_{g}

        dlag_dT = pow0*dnug_dT; // derivatives given by the viscosity function
        d2lag_dT2 = pow0*d2nug_dT2; //

        d2lag_dswdT = -expg * pow1*dnug_dT; //
    }

    /* Evaluate sum of la's and its first and second derivatives */
    /* --------------------------------------------------------  */
    la = law + lag;
    dla_dsw = dlaw_dsw + dlag_dsw;
    d2la_dsw2 = d2law_dsw2 + d2lag_dsw2;
    d2la_dswdT = d2law_dswdT + d2lag_dswdT;
    dla_dT = dlaw_dT + dlag_dT;
    d2la_dT2 = d2law_dT2 + d2lag_dT2;

    d2la_dswdT = d2law_dswdT + d2lag_dswdT;

    // Evaluate the Flux Function
    // tw = (vel + lag*(grw - grg)); 

    flw = law / la;

    // F
    if (degree >= 0) {

        m(0, (1. - flw)); // We are giving the value of flg=1-flw, the fractional flow for gas, as required in Flux2Comp2PhasesAdimensionalized as
        // the output of the jet!!! IMPORTANT!

        // Jacobian
        if (degree >= 1) {
            /* Evaluate first derivatives of fluxes relative to gas saturation */
            /* ------------------------------------------------------- */
            dflw_dsw = (la * dlaw_dsw - law * dla_dsw) / (la * la);
            m(0, 0, dflw_dsw); /* Now observe that from the chain rule dflg_dsg=dflw_dsw HERE WE DO NOT NEED TO WORRY ABOUT THE DIMENSION */

            /* Evaluate first derivatives of fluxes relative to temperature */
            dflw_dT = (la * dlaw_dT - law * dla_dT) / (la * la);
            m(0, 1, (-dflw_dT) * T_typical_); // We are giving dflg_dTheta = (  -dflw_dT( T (Theta) )  )*T_typical_
            // Hessian
            if (degree == 2) {
                /* Evaluate second derivatives of fluxes */
                /* ------------------------------------- */
                d2flw_dsw2 = ((la * d2law_dsw2 - law * d2la_dsw2) / la - 2. * dla_dsw * dflw_dsw) / la;

                d2flw_dT2 = (la * la * (la * d2law_dT2 - law * d2la_dT2) - 2. * la * dla_dT * (la * dlaw_dT - law * dla_dT)) / (la * la * la * la);

                /* Mixed derivative */
                /*
                d2flw_dswdT=d2flg_dswdT ;
                 */
                d2flw_dswdT = (la * la * (dla_dT * dlaw_dsw + la * d2law_dswdT - dlaw_dT * dla_dsw - law * d2la_dswdT)
                        - 2. * la * dla_dT * (la * dlaw_dsw - law * dla_dsw)) / (la * la * la * la);

                //               m(0, 0, 0, j000);
                //               m(0, 0, 1, j001);
                //               m(0, 1, 0, j010);
                //               m(0, 1, 1, j011);

                m(0, 0, 0, -d2flw_dsw2); // We are passing d2fg_dsg2  NO CHANGE!
                m(0, 0, 1, (d2flw_dswdT) * T_typical_); // double j001 = d2flg_dsgdTheta = d2flg_dsgdT(T(Theta))*T_typical_  = d2flg_dswdT*T_typical_ = d2flw_dswdT*T_typical_;
                m(0, 1, 0, (d2flw_dswdT) * T_typical_); // double j010 = j001;
                m(0, 1, 1, (-d2flw_dT2) * T_typical_ * T_typical_); // double j011 = d2flg_dTheta2 = d2flg_dT2*T_typical_^2 = -d2flw_dsw2*T_typical_^2 // CHECKED BY HELMUT 21/10/2010

            }
        }
    }
    return 2;
}

