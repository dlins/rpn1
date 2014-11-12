#include "ICDOWHydrodynamics.h"

ICDOWHydrodynamics::ICDOWHydrodynamics(Parameter *muw, Parameter *muo,
                                       Parameter *grw, Parameter *gro,
                                       Parameter *vel,
                                       Parameter *swc, Parameter *lambda){
    muw_parameter    = muw;
    muo_parameter    = muo;
    grw_parameter    = grw;
    gro_parameter    = gro;
    vel_parameter    = vel;
    swc_parameter    = swc;
    lambda_parameter = lambda;
}

ICDOWHydrodynamics::~ICDOWHydrodynamics(){
}

void ICDOWHydrodynamics::water_fractional_flow(double sw, int degree, JetMatrix &fw_jet){
    double muw    = muw_parameter->value();
    double muo    = muo_parameter->value();
    double grw    = grw_parameter->value();
    double gro    = gro_parameter->value();
    double vel    = vel_parameter->value();
    double swc    = swc_parameter->value();
    double lambda = lambda_parameter->value();

    double se;
    if (sw > swc) se = (sw - swc)/(1.0 - swc);
    else          se = 0.0;

    double krw = pow(se, 2.0/lambda + 3.0);
    double kro = (1.0 - se)*(1.0 - se)*(1.0 - pow(se, 2.0/lambda + 1.0));

    fw_jet.resize(1);

    if (degree >= 0){
        double lkw = krw/muw;
        double lko = kro/muo;
        double lk  = lkw + lko;

        fw_jet.set(0, (lkw/lk)*(vel + lko*(grw - gro)));

        if (degree >= 1){
            fw_jet.set(0, 0, (1.0/pow(pow(-(sw-swc)/(swc-1.0),2.0/lambda+3.0)/muw-((pow(-(sw-swc)/(swc-1.0),2.0/lambda+1.0)-1.0)*
((sw-swc)/(swc-1.0)+1.0))/muo,2.0)*pow(-(sw-swc)/(swc-1.0),2.0/lambda+3.0)*((pow(-(sw-swc)/(swc-1.0),2.0/lambda+1.0)-1.0)/
(muo*(swc-1.0))+((2.0/lambda+3.0)*pow(-(sw-swc)/(swc-1.0),2.0/lambda+2.0))/(muw*(swc-1.0))-(((sw-swc)/(swc-1.0)+1.0)*
(2.0/lambda+1.0)*pow(-(sw-swc)/(swc-1.0),2.0/lambda))/(muo*(swc-1.0))))/muw-((2.0/lambda+3.0)*pow(-(sw-swc)/(swc-1.0),2.0/lambda+2.0))/
(muw*(swc-1.0)*(pow(-(sw-swc)/(swc-1.0),2.0/lambda+3.0)/muw-((pow(-(sw-swc)/(swc-1.0),2.0/lambda+1.0)-1.0)*((sw-swc)/(swc-1.0)+1.0))/muo)));

            if (degree >= 2){
                fw_jet.set(0, 0, 0, (1.0/pow(pow(-(sw-swc)/(swc-1.0),2.0/lambda+3.0)/muw-((pow(-(sw-swc)/(swc-1.0),2.0/lambda+1.0)-1.0)*((sw-swc)/
(swc-1.0)+1.0))/muo,2.0)*pow(-(sw-swc)/(swc-1.0),2.0/lambda+3.0)*((pow(-(sw-swc)/(swc-1.0),2.0/lambda+1.0)-1.0)/(muo*(swc-1.0))+
((2.0/lambda+3.0)*pow(-(sw-swc)/(swc-1.0),2.0/lambda+2.0))/(muw*(swc-1.0))-(((sw-swc)/(swc-1.0)+1.0)*(2.0/lambda+1.0)*pow(-(sw-swc)/
(swc-1.0),2.0/lambda))/(muo*(swc-1.0))))/muw-((2.0/lambda+3.0)*pow(-(sw-swc)/(swc-1.0),2.0/lambda+2.0))/(muw*(swc-1.0)*(pow(-(sw-swc)/
(swc-1.0),2.0/lambda+3.0)/muw-((pow(-(sw-swc)/(swc-1.0),2.0/lambda+1.0)-1.0)*((sw-swc)/(swc-1.0)+1.0))/muo)));
            }
        }
    }

    return;
}

void ICDOWHydrodynamics::oil_fractional_flow(double sw, int degree, JetMatrix &fo_jet){
    double muw    = muw_parameter->value();
    double muo    = muo_parameter->value();
    double grw    = grw_parameter->value();
    double gro    = gro_parameter->value();
    double vel    = vel_parameter->value();
    double swc    = swc_parameter->value();
    double lambda = lambda_parameter->value();

    double se;
    if (sw > swc) se = (sw - swc)/(1.0 - swc);
    else          se = 0.0;

    double krw = pow(se, 2.0/lambda + 3.0);
    double kro = (1.0 - se)*(1.0 - se)*(1.0 - pow(se, 2.0/lambda + 1.0));

    fo_jet.resize(1);

    if (degree >= 0){
        double lkw = krw/muw;
        double lko = kro/muo;
        double lk  = lkw + lko;

        fo_jet.set(0, (lko/lk)*(vel + lkw*(gro - grw)));

        if (degree >= 1){
            fo_jet.set(0, 0, -(1.0/pow(pow(-(sw-swc)/(swc-1.0),2.0/lambda+3.0)/muw-((pow(-(sw-swc)/(swc-1.0),2.0/lambda+1.0)-1.0)*((sw-swc)/(swc-1.0)+1.0))/muo,2.0)*pow(-(sw-swc)/(swc-1.0),2.0/lambda+3.0)*((pow(-(sw-swc)/(swc-1.0),2.0/lambda+1.0)-1.0)/(muo*(swc-1.0))+((2.0/lambda+3.0)*pow(-(sw-swc)/(swc-1.0),2.0/lambda+2.0))/(muw*(swc-1.0))-(((sw-swc)/(swc-1.0)+1.0)*(2.0/lambda+1.0)*pow(-(sw-swc)/(swc-1.0),2.0/lambda))/(muo*(swc-1.0))))/
muw+((2.0/lambda+3.0)*pow(-(sw-swc)/(swc-1.0),2.0/lambda+2.0))/(muw*(swc-1.0)*(pow(-(sw-swc)/(swc-1.0),2.0/lambda+3.0)/muw-
((pow(-(sw-swc)/(swc-1.0),2.0/lambda+1.0)-1.0)*((sw-swc)/(swc-1.0)+1.0))/muo)));

            if (degree >= 2){
                fo_jet.set(0, 0, 0, -(1.0/pow(pow(-(sw-swc)/(swc-1.0),2.0/lambda+3.0)/muw-((pow(-(sw-swc)/(swc-1.0),2.0/lambda+1.0)-1.0)*((sw-swc)/(swc-1.0)+1.0))/muo,2.0)*
pow(-(sw-swc)/(swc-1.0),2.0/lambda+3.0)*((pow(-(sw-swc)/(swc-1.0),2.0/lambda+1.0)-1.0)/(muo*(swc-1.0))+((2.0/lambda+3.0)*pow(-(sw-swc)/
(swc-1.0),2.0/lambda+2.0))/(muw*(swc-1.0))-(((sw-swc)/(swc-1.0)+1.0)*(2.0/lambda+1.0)*pow(-(sw-swc)/(swc-1.0),2.0/lambda))/
(muo*(swc-1.0))))/muw+((2.0/lambda+3.0)*pow(-(sw-swc)/(swc-1.0),2.0/lambda+2.0))/(muw*(swc-1.0)*(pow(-(sw-swc)/
(swc-1.0),2.0/lambda+3.0)/muw-((pow(-(sw-swc)/(swc-1.0),2.0/lambda+1.0)-1.0)*((sw-swc)/(swc-1.0)+1.0))/muo)));
            }
        }
    }

    return;
}

