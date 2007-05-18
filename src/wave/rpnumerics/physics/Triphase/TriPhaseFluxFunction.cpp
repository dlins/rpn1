#include <math.h>
#include <iostream>

using namespace std;

/*
For TriPhaseFluxParams an array will be created. Each field of TriPhaseFluxParams will be indexed in the array thus:

grg = 0
gro = 1
grw = 2
mug = 3
muo = 4
muw = 5
vel = 6

struct TriPhaseFluxParams{
    double vel, muw, muo, mug, grw, gro, grg;
};

*/

/*

For PermParams an array will be created. Each field of PermParams will be indexed in the array thus:

cng  = 0
cno  = 1
cnw  = 2
epsl = 3
lg   = 4
log  = 5
low  = 6
lw   = 7



// Before using arrays PermParams, kw were defined thus:

struct PermParams{
    double cnw, lw, lg, low, log, cng, cno, epsl;
};

double kw(double sw, double so, double sg, PermParams perm){
    double swcnw = sw - perm.cnw;
    if (swcnw <= 0.) return 0.;
    
    double denkw_ = (perm.lw + (1. - perm.lw) * (1. - perm.cnw)) * (1. - perm.cnw);
    return (perm.lw + (1. - perm.lw) * swcnw) * swcnw / denkw_;
}

        denkw_ = (params_.lw() + (1. - params_.lw()) * (1. - params_.cnw())) * (1. - params_.cnw());
        denkg_ = (params_.lg() + (1. - params_.lg()) * (1. - params_.cng())) * (1. - params_.cng());
        denkow_ = (params_.low() + (1. - params_.low()) * (1. - params_.cno())) * (1. - params_.cno());
        denkog_ = (params_.log() + (1. - params_.log()) * (1. - params_.cno())) * (1. - params_.cno());

*/

double kw(double sw, double so, double sg, double perm[]){

    // Get the values inside perm:
    double cng  = perm[0];
    double cno  = perm[1];
    double cnw  = perm[2];
    double epsl = perm[3];
    double lg   = perm[4];
    double log  = perm[5];
    double low  = perm[6];
    double lw   = perm[7];
    
    double denkw_ = (lw + (1. - lw) * (1. - cnw)) * (1. - cnw);
    double swcnw = sw - cnw;
    if (swcnw <= 0.) return 0.;
    return (lw + (1. - lw) * swcnw) * swcnw / denkw_;
}

double kogden(double sw, double so, double sg, double perm[]) {

    // Get the values inside perm:
    double cng  = perm[0];
    double cno  = perm[1];
    double cnw  = perm[2];
    double epsl = perm[3];
    double lg   = perm[4];
    double log  = perm[5];
    double low  = perm[6];
    double lw   = perm[7];

    double denkog_ = (log + (1. - log) * (1. - cno)) * (1. - cno);
    double sog = 1. - sg - cno;
    if (sog <= 0.0) return 0.0;
    return (lg + (1. - lg) * sog) / denkog_;
}

double kowden(double sw, double so, double sg, double perm[]) {

    // Get the values inside perm:
    double cng  = perm[0];
    double cno  = perm[1];
    double cnw  = perm[2];
    double epsl = perm[3];
    double lg   = perm[4];
    double log  = perm[5];
    double low  = perm[6];
    double lw   = perm[7];

    double denkow_ = (low + (1. - low) * (1. - cno)) * (1. - cno);
    double sow = 1. - sw - cno;
    if (sow <= 0.0) return 0.0;
    return (low + (1. - low) * sow) / denkow_;
}

double ko(double sw, double so, double sg, double perm[]){

    // Get the values inside perm:
    double cng  = perm[0];
    double cno  = perm[1];
    double cnw  = perm[2];
    double epsl = perm[3];
    double lg   = perm[4];
    double log  = perm[5];
    double low  = perm[6];
    double lw   = perm[7];

    double socno = so - cno;
    if (socno <= 0.) return 0.;
    return (epsl * (1. - cno) * kogden(sw, so, sg, perm) * kowden(sw, so, sg, perm) + (1. - epsl) * socno) * socno;
}

double kg(double sw, double so, double sg, double perm[]){

    // Get the values inside perm:
    double cng  = perm[0];
    double cno  = perm[1];
    double cnw  = perm[2];
    double epsl = perm[3];
    double lg   = perm[4];
    double log  = perm[5];
    double low  = perm[6];
    double lw   = perm[7];

    double denkg_ = (lg + (1. - lg) * (1. - cng)) * (1. - cng);
    double sgcng = sg - cng;
    if (sgcng <= 0.) return 0.;
    return (lg + (1. - lg) * sgcng) * sgcng / denkg_;
}

void F(double out[], double U[], double params[], double perm[]){

    // Get the values inside params:
    double grg = params[0];
    double gro = params[1];
    double grw = params[2];
    double mug = params[3];
    double muo = params[4];
    double muw = params[5];
    double vel = params[6];

    double sw = U[0];
    double so = U[1];
    double sg = 1. - sw - so;
    
    double lambdaw = kw(sw, so, sg, perm) / muw;
    double lambdao = ko(sw, so, sg, perm) / muo;
    double lambdag = kg(sw, so, sg, perm) / mug;
    double lambda = lambdaw + lambdao + lambdag;
    
    out[0] = (lambdaw / lambda) * (vel + lambdao * (grw - gro) + lambdag *(grw - grg));
    out[1] = (lambdao / lambda) * (vel + lambdaw * (gro - grw) + lambdag *(gro - grg));
	
    return;
}

// Functions used by DF

double dkwdsw(double sw, double so, double sg, double perm[]) {

    // Get the values inside perm:
    double cng  = perm[0];
    double cno  = perm[1];
    double cnw  = perm[2];
    double epsl = perm[3];
    double lg   = perm[4];
    double log  = perm[5];
    double low  = perm[6];
    double lw   = perm[7];

    double denkw_ = (lw + (1. - lw) * (1. - cnw)) * (1. - cnw);
    double swcnw = sw - cnw;
    if (swcnw <= 0.) return 0.;
    return (lw + 2. * (1. - lw) * swcnw) / denkw_;
}

double dkogdendsg(double sw, double so, double sg, double perm[]) {

    // Get the values inside perm:
    double cng  = perm[0];
    double cno  = perm[1];
    double cnw  = perm[2];
    double epsl = perm[3];
    double lg   = perm[4];
    double log  = perm[5];
    double low  = perm[6];
    double lw   = perm[7];

    double denkog_ = (log + (1. - log) * (1. - cno)) * (1. - cno);
    double sog = 1. - sw - cno;
    if (sog <= 0.0) return 0.0;
    return (-1. + log) / denkog_;
}

double dkowdendsw(double sw, double so, double sg, double perm[]) {

    // Get the values inside perm:
    double cng  = perm[0];
    double cno  = perm[1];
    double cnw  = perm[2];
    double epsl = perm[3];
    double lg   = perm[4];
    double log  = perm[5];
    double low  = perm[6];
    double lw   = perm[7];

    double denkow_ = (low + (1. - low) * (1. - cno)) * (1. - cno);
    double sow = 1. - sw - cno;
    if (sow <= 0.0) return 0.0;
    return (-1. + low) / denkow_;
}

double dkodsw(double sw, double so, double sg, double perm[]) {

    // Get the values inside perm:
    double cng  = perm[0];
    double cno  = perm[1];
    double cnw  = perm[2];
    double epsl = perm[3];
    double lg   = perm[4];
    double log  = perm[5];
    double low  = perm[6];
    double lw   = perm[7];

    double socno = so - cno;
    if (socno <= 0.) return 0.;
    return (-dkogdendsg(sw, so, sg, perm) * kowden(sw, so, sg, perm) + kogden(sw, so, sg, perm) * dkowdendsw(sw, so, sg, perm))*socno*(1. - cno)*epsl;
}

double dkgdsw(double sw, double so, double sg, double perm[]) {

    // Get the values inside perm:
    double cng  = perm[0];
    double cno  = perm[1];
    double cnw  = perm[2];
    double epsl = perm[3];
    double lg   = perm[4];
    double log  = perm[5];
    double low  = perm[6];
    double lw   = perm[7];

    double denkg_ = (lg + (1. - lg) * (1. - cng)) * (1. - cng);
    double sgcng = sg - cng;
    if (sgcng <= 0.) return 0.;
    return -(lg + 2. * (1. - lg) * sgcng) / denkg_;
}

double dkwdso(double sw, double so, double sg, double perm[]) {

    // Get the values inside perm:
    double cng  = perm[0];
    double cno  = perm[1];
    double cnw  = perm[2];
    double epsl = perm[3];
    double lg   = perm[4];
    double log  = perm[5];
    double low  = perm[6];
    double lw   = perm[7];

    return 0.;
}

double dkodso(double sw, double so, double sg, double perm[]) {

    // Get the values inside perm:
    double cng  = perm[0];
    double cno  = perm[1];
    double cnw  = perm[2];
    double epsl = perm[3];
    double lg   = perm[4];
    double log  = perm[5];
    double low  = perm[6];
    double lw   = perm[7];

    double socno = so - cno;
    if (socno <= 0.) return 0.;
    return (-dkogdendsg(sw, so, sg, perm) * socno + kogden(sw, so, sg, perm)) * kowden(sw, so, sg, perm) * (1. - cno) *epsl + 2. * (1. - epsl) * socno;
}

double dkgdso(double sw, double so, double sg, double perm[]) {

    // Get the values inside perm:
    double cng  = perm[0];
    double cno  = perm[1];
    double cnw  = perm[2];
    double epsl = perm[3];
    double lg   = perm[4];
    double log  = perm[5];
    double low  = perm[6];
    double lw   = perm[7];

    double denkg_ = (lg + (1. - lg) * (1. - cng)) * (1. - cng);
    double sgcng = sg - cng;
    if (sgcng <= 0.) return 0.;
    return -(lg + 2. * (1. - lg) * sgcng) / denkg_;
}

void DF(double A[2][2], double U[], double params[], double perm[]) {

    // Get the values inside params:
    double grg = params[0];
    double gro = params[1];
    double grw = params[2];
    double mug = params[3];
    double muo = params[4];
    double muw = params[5];
    double vel = params[6];

    double sw = U[0];
    double so = U[1];
    double sg = 1. - sw - so;

    double lambdaw = kw(sw, so, sg, perm) / muw;
    double lambdao = ko(sw, so, sg, perm) / muo;
    double lambdag = kg(sw, so, sg, perm) / mug;
    double lambda = lambdaw + lambdao + lambdag;
    double lambda2 = pow(lambda, 2);
    double dlambdawdsw = dkwdsw(sw, so, sg, perm) / muw; 
    double dlambdaodsw = dkodsw(sw, so, sg, perm) / muo;
    double dlambdagdsw = dkgdsw(sw, so, sg, perm) / mug;
    double dlambdadsw = dlambdawdsw + dlambdaodsw + dlambdagdsw;
    double dlambdawdso = dkwdso(sw, so, sg, perm) / muw;
    double dlambdaodso = dkodso(sw, so, sg, perm) / muo;
    double dlambdagdso = dkgdso(sw, so, sg, perm) / mug;
    double dlambdadso = dlambdawdso + dlambdaodso + dlambdagdso;
    
    A[0][0] = (lambdaw/lambda)*(dlambdaodsw*(grw - gro) + dlambdagdsw*(grw - grg)) + ((lambda*dlambdawdsw - lambdaw*dlambdadsw)/lambda2)*(vel + lambdao*(grw - gro) + lambdag*(grw - grg));
    A[0][1] = (lambdaw/lambda)*(dlambdaodso*(grw - gro) + dlambdagdso*(grw - grg)) + ((lambda*dlambdawdso - lambdaw*dlambdadso)/lambda2)*(vel + lambdao*(grw - gro) + lambdag*(grw - grg));
    A[1][0] = (lambdao/lambda)*(dlambdawdsw*(gro - grw) + dlambdagdsw*(gro - grg)) + ((lambda*dlambdaodsw - lambdao*dlambdadsw)/lambda2)*(vel + lambdaw*(gro - grw) + lambdag*(gro - grg));
    A[1][1] = (lambdao/lambda)*(dlambdawdso*(gro - grw) + dlambdagdso*(gro - grg)) + ((lambda*dlambdaodso - lambdao*dlambdadso)/lambda2)*(vel + lambdaw*(gro - grw) + lambdag*(gro - grg));
    return;
}

// TODO: Is this function OK? Where is defined HessianMatrix?
void D2F(double j[2][2][2], double U[]) {
    j[0][0][0] = 0;
    j[0][0][1] = 0;
    j[0][1][0] = 0;
    j[0][1][1] = 0;
    j[1][0][0] = 0;
    j[1][0][1] = 0;
    j[1][1][0] = 0;
    j[1][1][1] = 0;
    return;
}

void balance(double balanceMatrix[2][2], double U[], double params[], double perm[]) {

    // Get the values inside params:
    double grg = params[0];
    double gro = params[1];
    double grw = params[2];
    double mug = params[3];
    double muo = params[4];
    double muw = params[5];
    double vel = params[6];
     
    // Get the values inside perm:
    double cng  = perm[0];
    double cno  = perm[1];
    double cnw  = perm[2];
    double epsl = perm[3];
    double lg   = perm[4];
    double log  = perm[5];
    double low  = perm[6];
    double lw   = perm[7];     

    double sw = U[0];
    double so = U[1];
    double sg = 1. - sw - so;
    
    double lambdaw = kw(sw, so, sg, perm) / muw;
    double lambdao = ko(sw, so, sg, perm) / muo;
    double lambdag = kg(sw, so, sg, perm) / mug;
    double lambda = lambdaw + lambdao + lambdag;
    double fw = lambdaw / lambda;
    double fo = lambdao / lambda;
    
    balanceMatrix[0][0] = lambdaw * (1. - fw);
    balanceMatrix[0][1] = lambdaw * (-fo);
    balanceMatrix[1][0] = balanceMatrix[0][1];
    balanceMatrix[1][1] = lambdao * (1. - fo);
    
    return;
}	

/* These functions to deal with the capillarity BEGIN

For CapilParams an array will be created. Each field of CapilParams will be indexed in the array thus:

    acg = 0
    acw = 1
    lcg = 2
    lcw = 3
*/

double dpcowdsw(double sw, double capil_params[]) {

    // Get the values inside capil_params:
    double acg = capil_params[0];
    double acw = capil_params[1];
    double lcg = capil_params[2];
    double lcw = capil_params[3];

    return -acw*(lcw + (1. - lcw)*2.*(1. - sw));
}

double dpcogdsw(double sg, double capil_params[]) {
    // finds DPcog(1-sw-so)/Dsw
    
    // Get the values inside capil_params:
    double acg = capil_params[0];
    double acw = capil_params[1];
    double lcg = capil_params[2];
    double lcw = capil_params[3];
    return acg*(lcg + (1. - lcg)*2.*(1. - sg));
}

double dpcogdso(double sg, double capil_params[]) {
    // finds DPcog(1-sw-so)/Dso
    
    // Get the values inside capil_params:
    double acg = capil_params[0];
    double acw = capil_params[1];
    double lcg = capil_params[2];
    double lcw = capil_params[3];    
    return acg*(lcg + (1. - lcg)*2.*(1. - sg));
}

void capil_jacobian(double capillarity_jacobian[2][2], double U[], double capil_params[]) {
        double sw = U[0];
        double so = U[1];
        double sg = 1. - sw - so;

        capillarity_jacobian[0][0] = -dpcowdsw(sw, capil_params) + dpcogdsw(sg, capil_params);
        capillarity_jacobian[0][1] = dpcogdso(sg, capil_params);
        capillarity_jacobian[1][0] = dpcogdsw(sg, capil_params);
        capillarity_jacobian[1][1] = dpcogdso(sg, capil_params);
        return;
}



/* These functions to deal with the capillarity END */

/*
For ViscosityParams an array will be created. Each field of ViscosityParams will be indexed in the array thus:
    eps = 0
*/

void viscosity(double bal[2][2], double U[], double params[], double perm[], double capil_params[], double visc_params[]) {
    // Get the values inside visc_params: 
    double epsl = visc_params[0];
     
    balance(bal, U, params, perm);
    double sw = U[0];
    double so = U[1];
    double sg = 1. - sw - so;
        
    double capillarity_jacobian[2][2];    
    capil_jacobian(capillarity_jacobian, U, capil_params);
    
    //bal.mul(capillarity_jacobian); /* TODO: mul is pre or post */
    //bal.scale(epsl); /* TODO: what's scale??? */
    return;
}

int main(int argc, const char* argv[]){
    cout << "\n****** TriPhaseFluxFunction ******\n" << endl;
    return 0;
}
