#include "FracFlow2PhasesVerticalAdimensionalized.h"

FracFlow2PhasesVerticalAdimensionalized::FracFlow2PhasesVerticalAdimensionalized(double cnw_, double cng_, double expw_, double expg_, 
                                                 Thermodynamics_SuperCO2_WaterAdimensionalized *TD_){
    cnw  = cnw_; // = 0
    cng  = cng_; // = 0
    expw = expw_; // = 2
    expg = expg_; // = 2

    TD = TD_;
    T_typical_ = TD->T_typical();
}

FracFlow2PhasesVerticalAdimensionalized::~FracFlow2PhasesVerticalAdimensionalized(){
}

int FracFlow2PhasesVerticalAdimensionalized::Diff_FracFlow2PhasesVerticalAdimensionalized(double sw, double Theta, int degree, JetMatrix &m){
    double T = TD->Theta2T(Theta);

    double d2Z_dsw2; // <- Unique to Vertical (only different variable with respect to Horizontal)
    double sg, law, lag; 
    double dlaw_dsw, dlag_dsw;
    double d2law_dsw2, d2lag_dsw2;
    double la, dla_dsw, d2la_dsw2;    
    double dflw_dsw, dflw_dT; // Derivatives of the Fractional Flow of water       
    double d2flw_dsw2 ; // Second derivative of Fractional Flow of water

    double flw; // Fractional Flow for water

    // Some variables to facilitate the operations with powers
    double pow0, pow1, pow2;
       
    double nuw, nug;	       // nuw= 1/muw , nug=1/mug
    double dnuw_dT, d2nuw_dT2; // This values are assigned using TD
                               // dnuw_dT = dnuw_dT(T).
                               // d2nuw_dT2 = d2nuw_dT2(T). 

    double dnug_dT, d2nug_dT2; // This values are assigned using TD
                                // dnug_dT = dnug_dT(T),
                                // d2nug_dT2 = d2nug_dT2(T).

    double dlaw_dT, d2law_dT2; // Temperature derivatives of law (are not passed as parameters).

    double dlag_dT, d2lag_dT2; // Temperature derivatives of lag (are not passed as parameters).

    double d2law_dswdT;        // Mixed derivative (it is not passed as parameter).
    double d2lag_dswdT;        // Mixed derivative (it is not passed as parameter).

    double dla_dT;       
    double d2flw_dswdT;
    double d2la_dswdT;       
    double d2flw_dT2;
    double d2la_dT2;
       
    // Finally we assign sg
    sg = 1. - sw;

    TD->inv_muw(T, nuw, dnuw_dT, d2nuw_dT2);
    TD->inv_mug(T, nug, dnug_dT, d2nug_dT2);


    // Before evaluating proper
    if (sw < cnw){
        law         = 0.;
        dlaw_dsw    = 0.;
        d2law_dsw2  = 0.;

        dlaw_dT     = 0.;
        d2law_dT2   = 0.;

        d2law_dswdT = 0.; // remember that d2law_dswdT is equal to d2law_dTdsw
    }
    else {

	double temp=(sw - cnw)/(1.-cnw-cng);

        pow2 = 0.5*pow( temp, expw - 2.);    // 0.5*( (sw-s_{connate water})/(1-s_{connate water}-s_{con gas}) )^{expw-2}
		        		     // This model coincides with the formulae in Helmut's Thesis. 0.5 is the re-scaling factor.

        pow1 = temp*pow2;                    // 0.5*( (sw-s_{connate water})/(1-s_{connate water}-s_{con gas}) )^{expw-1}  
					     // Observe that pow1 = pow((sw - cnw)/(1.-cnw-cng),expw-1) ;

        pow0 = temp*pow1; 		     // 0.5*( (sw-s_{connate water})/(1-s_{connate water-s_{con gas}}) )^{expw}    
					     // Observe that pow0 = pow((sw - cnw)/(1.-cnw-cng),expw)


        law        = pow0*nuw;               // k_{rw}/mu_{w}(T) This is equivalent to k_{ra}/mu_{a} in Helmut's thesis.
        dlaw_dsw   = expw*pow1*nuw;          // expw*( (sw-s_{connate water })/(1.-cnw-cng) )^{expw-1}/mu_{w}  derivation w.r.t sw
        d2law_dsw2 = (expw - 1.)*expw *pow2*nuw; // (expw - 1.) * expw*( (sw-s_{connate water })/(1.-cnw-cng) )^{expw-2}/mu_{w}

        dlaw_dT   = pow0*dnuw_dT;   // the value of the derivatives of muw 
                                    // will be passed by the viscosity function.
        d2law_dT2 = pow0*d2nuw_dT2; //

        d2law_dswdT = expw*pow1*dnuw_dT; 
    }

    if (sg < cng){
        lag         = 0.;
        dlag_dsw    = 0.;
        d2lag_dsw2  = 0.;

        dlag_dT     = 0.;
        d2lag_dT2   = 0.;

        d2lag_dswdT = 0.;
    }
    else {

        double temp=(sg - cng)/(1.-cnw-cng);
        pow2 = 0.95*pow( temp , expg - 2.); // ( (sg-s_{connate gas})/(1.-cnw-cng) )^{expg-2}
        pow1 = temp*pow2;                   // ( (sg-s_{connate gas})/(1.-cnw-cng) )^{expg-1} Observe that pow1 = pow(temp, expg-1)
        pow0 = temp*pow1;                   // ( (sg-s_{connate gas})/(1.-cnw-cng) )^{expg}   Observe that pow0 = pow(temp, expg) ;

        lag      = pow0*nug;                //  k_{rg}/mu_{g}(T)  this viscosity is function of temperature   ;
        dlag_dsw = - expg * pow1*nug;       //  - expg*( (sg-s_{connate gas})/(1.-cnw-cng) )^{expg-1}/mu_{g}
        d2lag_dsw2   = (expg - 1.)*expg * pow2*nug; // (expg-1)*expg*( (sg-s_{connate gas})/(1.-cnw-cng) )^{expg-2}/mu_{g}

        dlag_dT   = pow0*dnug_dT;          // derivatives given by the viscosity function
        d2lag_dT2 = pow0*d2nug_dT2;        //

        d2lag_dswdT = -expg* pow1*dnug_dT; //
    }

    /* Evaluate sum of la's and its first and second derivatives */
    /* --------------------------------------------------------  */
    la       =  law + lag;
    dla_dsw  =  dlaw_dsw + dlag_dsw ;
    d2la_dsw2    =  d2law_dsw2 + d2lag_dsw2;
    d2la_dswdT = d2law_dswdT + d2lag_dswdT;
    dla_dT = dlaw_dT + dlag_dT;
    d2la_dT2 = d2law_dT2 + d2lag_dT2;

    d2la_dswdT = d2law_dswdT + d2lag_dswdT;

    flw = law/la;

    // Z
    if (degree >= 0){

        m(0, flw*lag);  // double j0 = Z = flw*lag;

        // Jacobian
        if (degree >= 1){
            /* Evaluate first derivative of Z relative to gas saturation */
            /* --------------------------------------------------------- */

            dflw_dsw = (la*dlaw_dsw - law*dla_dsw)/(la*la);      // BE CAREFUL, WE ARE USING THE CHAIN RULE TO PASS THE CORRECT DERIVATIVE
            m(0, 0, -(dflw_dsw*lag + flw*dlag_dsw) );            // this is double j00 = dZ_dsg= -dZ_dsw = -(dflw_dsw*lag + flw*dlag_dsw); NO CHANGE FOR DIM-LESS

            /* Evaluate first derivative of Z relative to temperature */
            dflw_dT  = (la*dlaw_dT - law*dla_dT)/(la*la);

            m(0, 1, (dflw_dT*lag + flw*dlag_dT)*T_typical_ );     // this is double j01 =  dZ_dTheta = dZ_dT*T_typical_  = (dflw_dT*lag + flw*dlag_dT)*T_typical_; 

            // Hessian
            if (degree == 2){
                
                /* Evaluate second derivatives of Z */
                /* ------------------------------------- */
                d2flw_dsw2 = ( (la*d2law_dsw2 - law*d2la_dsw2)/la - 2.*dla_dsw * dflw_dsw ) /la;   

                d2Z_dsw2   = d2flw_dsw2 * lag + 2.*dflw_dsw*dlag_dsw + flw*d2lag_dsw2;

                double j000 = d2Z_dsw2; // Note we are passing d2Z_dsg2 = d2Z_dsw2 NO CHANGE FOR DIM-LESS



                /* Mixed derivative */
                /*
                d2Z_dsgdT = -d2Z_dswdT = -( dflw_dswdT*lag + dflw_dsw*dlag_dT +
                                            dflw_dT*dlag_dsw + flw*d2lag_dswdT  );
                */
        

                d2flw_dT2 = ( la*la*( la*d2law_dT2 - law*d2la_dT2 ) - 2.*la*dla_dT*( la*dlaw_dT - law*dla_dT ) ) /(la*la*la*la);

                d2flw_dswdT= ( la*la*( dla_dT*dlaw_dsw + la*d2law_dswdT - dlaw_dT*dla_dsw - law*d2la_dswdT )  
			       -2.*la*dla_dT*( la*dlaw_dsw - law*dla_dsw) ) /(la*la*la*la);

               double j001 = ( -( d2flw_dswdT*lag + dflw_dsw*dlag_dT +
                                dflw_dT*dlag_dsw + flw*d2lag_dswdT   ) )*T_typical_;  // Observe that we are passing really d2Z_dsgdTheta = d2Z_dsgdT*T_typical_

               double j010 = j001;

               /*
               d2Z_dT2 = d2flw_dT2*lag + dflw_dT*dlag_dT +
                         dflw_dT*dlag_dT + flw*d2lag_dT2;
               */
               
               double j011 = ( d2flw_dT2*lag + dflw_dT*dlag_dT +
                             dflw_dT*dlag_dT + flw*d2lag_dT2 )*T_typical_*T_typical_; // d2Z_dTheta2 = d2Z_dT2 * T_typical_^2

               m(0, 0, 0, j000);
               m(0, 0, 1, j001);
               m(0, 1, 0, j010);
               m(0, 1, 1, j011);
            }
        }
    }
    return 2;
}

