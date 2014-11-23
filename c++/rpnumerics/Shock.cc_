#include "Shock.h"

int Shock::reason = 0;

Shock::Shock(){
    
}

double Shock::ddot(int n, double *x, double *y){
    double res = 0.0;
    for (int i = 0; i < n; i++) res += x[i]*y[i];
    return res;
}

// C = A*B
//
// A = m rows, n cols.
// B = n rows, p cols.
// C = m rows, p cols.
//
void Shock::matrixmult(int m, int n, int p, double *A, double *B, double *C){
    for (int i = 0; i < m; i++){
        for (int j = 0; j < p; j++){
            double s = 0.0;
            for (int k = 0; k < n; k++) s += A[i*n + k]*B[k*p + j];
            C[i*p + j] = s;
        }
    }

    return;
}

int Shock::sgn(double x){
    int s = 0;
    if (x < 0.0)      s = -1;
    else if (x > 0.0) s = 1;

    return s;
}

double Shock::dotprod(int n, double x[], double y[]){
    double p = .0;
    for (int i = 0; i < n; i++) p += x[i]*y[i];
    return p;
}

void Shock::normalize(int n, double x[]){
    double norm = .0;
    norm = euclidean_norm(n,&x[0]);
    for (int i = 0; i < n; i++) x[i] /= norm;
    return;
}

double Shock::euclidean_norm(int n, double x[]){
    double norm= .0;
    for (int i = 0; i < n; i++) norm += x[i]*x[i];

    return sqrt(norm);
}


void Shock::fill_with_jet(RpFunction * flux_object, int n, double *in, int degree, double *F, double *J, double *H) {
    RealVector r(n);

    double *rp = r;
    for (int i = 0; i < n; i++) rp[i] = in[i];
    WaveState state_c(r);

    JetMatrix c_jet(n);
    flux_object->jet(state_c, c_jet, degree);

    // Fill F
    if (F != 0) for (int i = 0; i < n; i++) F[i] = c_jet.get(i);

    // Fill J
    if (J != 0) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                J[i * n + j] = c_jet.get(i, j);
            }
        }
    }

    // Fill H
    if (H != 0) {
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    H[k*n*n + (i * n + j)] = c_jet.get(k, i, j); // Check this!!!!!!!!
                }
            }
        }
    }

    return;
}

// FUNCTION shockspeed
//
// This function computes the shock speed, given two points,
// Up and Um
//
// Parameters:
//              n: Dimension of the space.
//    typeofspeed: 0 if Um == Up,
//                 1 otherwise.
//           type: _SHOCK_SIMPLE_ACCUMULATION_ or _SHOCK_GENERAL_ACCUMULATION_.
//             Up: "Plus" point.
//             Um: "Minus" point.
//
// Returns:
//     The speed as a double.
//
double Shock::shockspeed(int n, double Um[], double Up[], FluxFunction *ff, AccumulationFunction *aa) {
    // Compute F(Up), F(Um):
    double Fp[n], Fm[n];

    fill_with_jet((RpFunction*)ff, n, Up, 0, Fp, 0, 0);
    fill_with_jet((RpFunction*)ff, n, Um, 0, Fm, 0, 0);

    double Gp[n], Gm[n];
    fill_with_jet((RpFunction*)aa, n, Up, 0, Gp, 0, 0);
    fill_with_jet((RpFunction*)aa, n, Um, 0, Gm, 0, 0);

    // The speed
    double s = 0.0, den = 0.0;

    for (int i = 0; i < n; i++) {
        s   += (Fp[i] - Fm[i])*(Gp[i] - Gm[i]);
        den += (Gp[i] - Gm[i])*(Gp[i] - Gm[i]);
    }

//    s = Fp[1] - Fm[1];
//    den = Gp[1] - Gm[1];

//    double speed = s/den;

//    int pos = 0;
//    double max_den_abs = 0.0;
//    for (int i = 0; i < n; i++){
//        double max_i = fabs(Gp[i] - Gm[i]);
//        if (max_i > max_den_abs){
//           pos = i;
  //          max_den_abs = max_i;
//        }
//    }

//    s = Fp[0] - Fm[0];
//    den = Gp[0] - Gm[0];

    return s/den;
}

// Function shock_step.
//
// This function tries to find two points such that the Hugoniot function,
// when evaluated in them, displays a change of sign. That being the case,
// a zero of the Hugoniot function can be found using the bisection method.
//
// Four strategies will be carried out, each targeting a possible situation.
//
// The first three assume that it is possible to find two points of the form:
//
//     pp = p + tan_eps*tangent + nrm_eps*normal,
//     pm = p + tan_eps*tangent - nrm_eps*normal,
//
// such that the bisection method can be applied to them.
//
// The fourth one assumes that a change of sign can be found between 
// two consecutive points of the form
//
//     pp = p + tan_eps*tangent + nrm_eps*normal
//
// or
//
//     pm = p + tan_eps*tangent - nrm_eps*normal,
//
// for different values of both tan_eps and nrm_eps.
// Again, if suitable candidates are found, the bisection method is used.
//
// If all fails, an error is returned.
//
//           p: The point in the curve.
//     tangent: Vector that is tangent to the curve in p.
//      normal: Vector that is normal to the curve in p, 
//              but not in the strictest sense (could be "kind of normal").
//     epsilon: The initial length of the step (can be modified adaptively inside the function).
//       pnext: The next point in the curve.
//
int Shock::shock_step(const RealVector &p, const RealVector &Uref, const RealVector &tangent, const RealVector &normal, FluxFunction *ff, AccumulationFunction *aa, double &epsilon, RealVector &pnext){
    // 
    reason = 0;

    int n = p.size();
    pnext.resize(n);
    
    double tan_eps = epsilon;
    double nrm_eps = epsilon/2; // Because. Could be changed later.

    bool proceed_to_bisection = false;
    RealVector pp(n), pm(n); // P = plus, m = minus.

    double pp_sgn, pm_sgn; // To store the value of the Hugoniot function.

    // First try (diminish nrm_eps as necessary).
    // Keep a copy of p + tan_eps*tangent.
    //
    int step = 0;
    double nrm_eps_tmp = nrm_eps;
    RealVector p_tan(n);
    for (int i = 0; i < n; i++) {
        p_tan.component(i) = p.component(i) + tan_eps*tangent.component(i);
    }

    while (step < SHOCK_MAX_NUMBER_STEPS && !proceed_to_bisection){
        for (int i = 0; i < n; i++){
            pp.component(i) = p_tan.component(i) + nrm_eps_tmp*normal.component(i);
            pm.component(i) = p_tan.component(i) - nrm_eps*normal.component(i);
        }

        pp_sgn = hugoniot(pp, Uref, ff, aa);
        pm_sgn = hugoniot(pm, Uref, ff, aa);

        if (pp_sgn*pm_sgn < 0.0) proceed_to_bisection = true;
        else{
            //printf("    Had to diminish nrm_eps\n");
            nrm_eps_tmp *= .5;
        }

        step++;

        if (proceed_to_bisection){
//            printf("    shock_step 1a. steps = %d\n", step);
            reason = 1;
        }
    }
    
    // Second try (diminish nrm_eps as necessary).
    // Keep a copy of p + tan_eps*tangent.
    //
    step = 0;
    nrm_eps_tmp = nrm_eps;
    while (step < SHOCK_MAX_NUMBER_STEPS && !proceed_to_bisection){
        for (int i = 0; i < n; i++){
            pp.component(i) = p_tan.component(i) + nrm_eps*normal.component(i);
            pm.component(i) = p_tan.component(i) - nrm_eps_tmp*normal.component(i);
        }

        pp_sgn = hugoniot(pp, Uref, ff, aa);
        pm_sgn = hugoniot(pm, Uref, ff, aa);

        if (pp_sgn*pm_sgn < 0.0) proceed_to_bisection = true;
        else nrm_eps_tmp *= .5;

        step++;

        if (proceed_to_bisection){
//            printf("    shock_step 1a. steps = %d\n", step);
            reason = 2;
        }
    }

    // Third try (get closer to p, while maintaining the last value of nrm_eps).
    //
    step = 0;
    double tan_eps_tmp = .5*tan_eps;
    while (step < SHOCK_MAX_NUMBER_STEPS && !proceed_to_bisection){
        for (int i = 0; i < n; i++){
            pp.component(i) = p.component(i) + tan_eps_tmp*tangent.component(i) + nrm_eps_tmp*normal.component(i);
            pm.component(i) = p.component(i) + tan_eps_tmp*tangent.component(i) - nrm_eps_tmp*normal.component(i);
        }

        pp_sgn = hugoniot(pp, Uref, ff, aa);
        pm_sgn = hugoniot(pm, Uref, ff, aa);

        if (pp_sgn*pm_sgn < 0.0) proceed_to_bisection = true;
        else tan_eps_tmp *= .5;

        step++;
        if (proceed_to_bisection){
//            printf("    shock_step 1a. steps = %d\n", step);
            reason = 3;
        }
    }

    // Fourth try (the triangle strategy). Convex combinations of the points p, pp and p, pm will be
    // explored in hope that the Hugoniot function evaluated at the points obtained for consecutive
    // values of alpha along each segment display changes of sign.
    // 
    if (!proceed_to_bisection){
        RealVector pp_max(n), pm_max(n), pp_prev(n), pm_prev(n);
        for (int i = 0; i < n; i++){
            pp_max.component(i) = pp_prev.component(i) = pp.component(i);
            pm_max.component(i) = pm_prev.component(i) = pm.component(i);
        }
        double pp_sgn_prev = hugoniot(pp, Uref, ff, aa); // This is the costly part of the process.
        double pm_sgn_prev = hugoniot(pm, Uref, ff, aa);

        step = 0;
        double beta = .5; // No need for beta = 1.0!

        while (step < SHOCK_MAX_NUMBER_STEPS && !proceed_to_bisection){
            double alpha = 1.0 - beta;

            for (int i = 0; i < n; i++){
                pp.component(i) = alpha*p.component(i) + beta*pp_max.component(i);
                pm.component(i) = alpha*p.component(i) + beta*pm_max.component(i);
            }

            pp_sgn = hugoniot(pp, Uref, ff, aa);
            pm_sgn = hugoniot(pm, Uref, ff, aa);

            if (pp_sgn*pp_sgn_prev < 0.0) {
                proceed_to_bisection = true;
                for (int i = 0; i < n; i++) pm.component(i) = pp_prev.component(i);
            }
            else if (pm_sgn*pm_sgn_prev < 0.0) {
                proceed_to_bisection = true;
                for (int i = 0; i < n; i++) pp.component(i) = pm_prev.component(i);
            }
            else {
                beta *= .5;
                for (int i = 0; i < n; i++){
                    pp_prev.component(i) = pp.component(i);
                    pm_prev.component(i) = pm.component(i);
                }

                pp_sgn_prev = pp_sgn;
                pm_sgn_prev = pm_sgn;
            }

            step++;
        }



        if (proceed_to_bisection){
//            printf("    shock_step 1a. steps = %d\n", step);
            reason = 4;
        }
    }

    // Fifth try (increase nrm_eps as necessary).
    //
    step = 0;
    nrm_eps_tmp = nrm_eps*2.0;
//    while (step < Shock_MAX_NUMBER_STEPS && !proceed_to_bisection){
//        for (int i = 0; i < n; i++){
//            pp.component(i) = p_tan.component(i) + nrm_eps_tmp*normal.component(i);
//            pm.component(i) = p_tan.component(i) - nrm_eps_tmp*normal.component(i);
//        }

//        pp_sgn = hugoniot(pp, Uref, ff, aa);
//        pm_sgn = hugoniot(pm, Uref, ff, aa);

//        if (pp_sgn*pm_sgn < 0.0) proceed_to_bisection = true;
//        else nrm_eps_tmp *= 1.5;

//        step++;
//        if (proceed_to_bisection){
//            printf("    shock_step 3. steps = %d\n", step);
//            reason = 5;
//        }
//    }


//    printf("    Reason = %d. Proceed_to_bisection = %d\n", reason, proceed_to_bisection);

    if (!proceed_to_bisection){
//        printf("Error in file \"%s\", line %u\n", __FILE__, __LINE__);
        return SHOCK_ERROR;
    }

    // Two suitable points were found. Compute the bisection.
    //
    epsilon = tan_eps_tmp; // Return this so the calling function knows that epsilon 
                           // was changed and should be used in the next step.

    bisection(pp, pp_sgn, pm, pm_sgn, Uref, ff, aa, pnext);

    return SHOCK_OK;
}

void Shock::bisection(const RealVector &pp, double pp_sgn, const RealVector &pm, double pm_sgn, const RealVector &Uref, FluxFunction *F, AccumulationFunction *G, RealVector &pnext){
    double eps = 1e-10;
    int n = pp.size();
    pnext.resize(n);
    RealVector pp_temp(pp), pm_temp(pm);

    // Make sure that sgn(pp) = 1 and sgn(pm) = -1.
    if (pp_sgn < 0.0) {
        double temp;
        for (int i = 0; i < n; i++){
            temp = pp_temp.component(i);
            pp_temp.component(i) = pm_temp.component(i);
            pm_temp.component(i) = temp;
        }
    }

    while (euclidean_norm(n, (pp_temp - pm_temp).components()) > eps){
        for (int i = 0; i < n; i++) pnext.component(i) = .5*(pp_temp.component(i) + pm_temp.component(i));
        double pnext_val = hugoniot(pnext, Uref, F, G);

        if (pnext_val > 0.0) for (int i = 0; i < n; i++) pp_temp.component(i) = pnext.component(i);
        else                 for (int i = 0; i < n; i++) pm_temp.component(i) = pnext.component(i);

    }

    for (int i = 0; i < n; i++) pnext.component(i) = .5*(pp_temp.component(i) + pm_temp.component(i));

    return;
}

double Shock::hugoniot(const RealVector &U, const RealVector &Uref, FluxFunction *F, AccumulationFunction *G){
    int n = U.size(); // = 2

    WaveState Uref_in(Uref);
    WaveState U_in(U);

    JetMatrix Fm0(n);
    JetMatrix Fm(n);
    JetMatrix Gm0(n);
    JetMatrix Gm(n);

    F->jet(Uref_in, Fm0, 1);
    F->jet(U_in, Fm, 1);

    G->jet(Uref_in, Gm0, 1);
    G->jet(U_in, Gm, 1);

//    double RH = (Fm(0) - Fm0(0)) - (Gm(0)- Gm0(0)) * ((Fm(0) - Fm0(0))+(Fm(1) - Fm0(1)))/ ((Gm(0) - Gm0(0))+(Gm(1) - Gm0(1)));
    double RH = (Fm.get(0) - Fm0.get(0))*(Gm.get(1) - Gm0.get(1)) - (Fm.get(1) - Fm0.get(1))*(Gm.get(0) - Gm0.get(0));

    return RH;
}

void Shock::hugoniot_der(const RealVector &U, const RealVector &Uref, FluxFunction *F, AccumulationFunction *G, double *tangent, double *normal){
    int n = U.size(); // = 2

    WaveState Uref_in(Uref); //std::cout << "hugoniot_der, ref.: " << Uref << std::endl;
    WaveState U_in(U);       //std::cout << "hugoniot_der, in.:  " << U << std::endl;

    JetMatrix Fm0(n);
    JetMatrix Fm(n);
    JetMatrix Gm0(n);
    JetMatrix Gm(n);

    F->jet(Uref_in, Fm0, 1);
    F->jet(U_in, Fm, 1);

    G->jet(Uref_in, Gm0, 1);
    G->jet(U_in, Gm, 1);

    double dRH[n];

    for (int j = 0; j < n; j++){
//        dRH[j] = Fm(0, j) + 
//                 (Gm(0)-Gm0(0))*((Fm(0,j)+Fm(1,j))*(Gm(0)-Gm0(0)+Gm(1)-Gm0(1)) - 
//                 (Gm(0,j)+Gm(1,j))*(Fm(0)-Fm0(0)+Fm(1)-Fm0(1))/(Gm(0)-Gm0(0)+Gm(1)-Gm0(1))*(Gm(0)-Gm0(0)+Gm(1)-Gm0(1))) - 
 //                Gm(0,j)*(Fm(0)-Fm0(0)+Fm(1)-Fm0(1))/(Gm(0)-Gm0(0)+Gm(1)-Gm0(1));
        dRH[j] = Gm.get(0, j)*(Fm.get(1) - Fm0.get(1)) + (Gm.get(0) - Gm0.get(0))*Fm.get(1, j) - Fm.get(0, j)*(Gm.get(1) - Gm0.get(1)) - Gm.get(1, j)*(Fm.get(0) - Fm0.get(0));
        //printf("Fm(1) - Fm0(1) = %f\n", Fm(1) - Fm0(1));
   }

   tangent[0] = -dRH[1];
   tangent[1] =  dRH[0];
   
   normal[0]  =  dRH[0];
   normal[1]  =  dRH[1];
   normalize(n, tangent);
   normalize(n, normal);
   return;
}

void Shock::hugoniot_der2(const RealVector &U, const RealVector &Uref, FluxFunction *F, AccumulationFunction *G, double *dRH2, double &curvature){
    int n = U.size(); // = 2

    WaveState Uref_in(Uref);
    WaveState U_in(U);

    JetMatrix Fm0(n);
    JetMatrix Fm(n);
    JetMatrix Gm0(n);
    JetMatrix Gm(n);

    F->jet(Uref_in, Fm0, 2);
    F->jet(U_in, Fm, 2);

    G->jet(Uref_in, Gm0, 2);
    G->jet(U_in, Gm, 2);

    double dRH[n];

    for (int j = 0; j < n; j++){
        dRH[j] = Gm.get(0, j)*(Fm.get(1) - Fm0.get(1)) + (Gm.get(0) - Gm0.get(0))*Fm.get(1, j) - 
                 Fm.get(0, j)*(Gm.get(1) - Gm0.get(1)) - Gm.get(1, j)*(Fm.get(0) - Fm0.get(0));
    }

    for (int i = 0; i < n; i++){
        for (int j = 0; j < n; j++){
            dRH2[i*n + j] = Gm.get(0, i, j)*(Fm.get(1) - Fm0.get(1)) + Gm.get(0, i)*Fm.get(1, j) + 
                            Gm.get(0, j)*Fm.get(1, i) + (Gm.get(0) - Gm0.get(0))*Fm.get(1, i, j) - 
                            Fm.get(0, i, j)*(Gm.get(1) - Gm0.get(1)) - Fm.get(0, i)*Gm.get(1, j) - 
                            Fm.get(0, j)*Gm.get(1, i) - (Fm.get(0) - Fm0.get(0))*Gm.get(1, i, j);
        }
    }

    double tangent[2] = {-dRH[1], dRH[0]};
    double normal[2]  = { dRH[0], dRH[1]};

    double temp1[2];
    matrixmult(2, 2, 1, dRH2, tangent, temp1);
    
    double temp2[1];
    matrixmult(1, 2, 1, tangent, temp1, temp2);

    double tangent_norm = euclidean_norm(n, normal);

    curvature = -temp2[0]/pow(tangent_norm, 3);

    return;
}

int Shock::verify_lax_shock(RealVector &ref, double lambda_0_ref, double lambda_1_ref, RealVector &p, int increase, int family, FluxFunction *ff, AccumulationFunction *aa){
    // Find lambda at p.
    int n = p.size();
    
    double FJ[n][n], GJ[n][n];

    fill_with_jet(ff, n, p.components(), 1, 0, &FJ[0][0], 0);
    fill_with_jet(aa, n, p.components(), 1, 0, &GJ[0][0], 0);

    vector<eigenpair> e;
    int info = Eigen::eig(n, &FJ[0][0], &GJ[0][0], e);

    // The i-th eigenvalue must be real. // The eigenvalues must be chosen carefully in the n-dimensional case.
    // ALL eigenvalues must be real. Extend this by using a for cycle.
    if (fabs(e[family].i) > 1e-6){
//        printf("Shock::curve(): Init step, eigenvalue %d is complex: % f %+f.\n", family, e[family].r, e[family].i);
//        printf("Error in file \"%s\", line %u\n", __FILE__, __LINE__);
        return SHOCK_ERROR;    
    }

    // Eigenvalue of the current family (could be the slow or fast one).
    double lambda[2] = {e[0].r, e[1].r};

    // Sigma
    double sigma = shockspeed(n, ref.components(), p.components(), ff, aa);

//    printf("\n============= New test ===============\n\n");
//    printf("    lambda[%d] - sigma = %g\n", family, lambda[family] - sigma);
//    printf("\n============= New test ===============\n\n");

    if (increase == WAVE_FORWARD){
        if (family == 0){
            if ((lambda[0] < sigma && sigma < lambda_0_ref) && sigma < lambda[1])    return LAX_SHOCK;
            else {
                // Por el momento no vamos a calcular el ultimo punto del choque con precision.
                // En caso de que una rarefaccion continue a partir del ultimo punto del choque
                // su punto inicial no sera optimo. Por el momento es suficiente.
                // En caso de cambiar de opinion en el futuro, la variante que tenemos en mente
                // es hacer una biseccion a lo largo del segmento que une el ultimo punto Lax del choque
                // con el primer punto no-Lax del choque, para calcular el punto de parada (aproximadamente),
                // si dos desigualdades cambian de sentido entonces habra que hacer una biseccion alternada
                // (un paso o algunos pasos con cada desigualdad). La opcion optima pareceria ser utilizar
                // el metodo de Newton, pero parece ser muy dificil de implementar, sobre todo cerca
                // de contactos dobles, donde podria converger para un stick mark que no es el deseado.
                // En caso de preocuparnos con esto en el futuro vamos a tener que desglosar cada uno de
                // estos casos por separado, y establecer un codigo de retorno para cada desigualdad violada.
                if (lambda[0] >= sigma)        return NON_LAX_AFTER_BECOMING_RIGHT_CHARACTERISTIC_WITH_CURRENT_FAMILY;
                //else if (sigma > lambda_0_ref) return NON_LAX_AFTER_BECOMING_LEFT_CHARACTERISTIC_WITH_CURRENT_FAMILY;
                else                           return NON_LAX_FOR_OTHER_REASON;
            }
//                                                                     return NON_LAX_SHOCK;
        }
        else if (family == 1){
            if ((lambda[1] < sigma && sigma < lambda_1_ref) && sigma > lambda_0_ref) return LAX_SHOCK;
            else {
                if (lambda[1] >= sigma)        return NON_LAX_AFTER_BECOMING_RIGHT_CHARACTERISTIC_WITH_CURRENT_FAMILY;
                //else if (sigma > lambda_0_ref) return NON_LAX_AFTER_BECOMING_LEFT_CHARACTERISTIC_WITH_CURRENT_FAMILY;
                else                           return NON_LAX_FOR_OTHER_REASON;
            }
        }
        else{
//            printf("Error in file \"%s\", line %u\n", __FILE__, __LINE__);
            return SHOCK_ERROR;
        }
    }
    else if (increase == WAVE_BACKWARD){ 
        if (family == 0){
            if ((lambda_0_ref < sigma && sigma < lambda[0]) && sigma < lambda_1_ref) return LAX_SHOCK;
            else {
                if (lambda[0] <= sigma)        return NON_LAX_AFTER_BECOMING_RIGHT_CHARACTERISTIC_WITH_CURRENT_FAMILY;
                //else if (sigma > lambda_0_ref) return NON_LAX_AFTER_BECOMING_LEFT_CHARACTERISTIC_WITH_CURRENT_FAMILY;
                else                           return NON_LAX_FOR_OTHER_REASON;
            }
        }
        else if (family == 1){
            if ((lambda_1_ref < sigma && sigma < lambda[1]) && sigma > lambda[0])    return LAX_SHOCK;
            else {
                if (lambda[1] <= sigma)        return NON_LAX_AFTER_BECOMING_RIGHT_CHARACTERISTIC_WITH_CURRENT_FAMILY;
                //else if (sigma > lambda_0_ref) return NON_LAX_AFTER_BECOMING_LEFT_CHARACTERISTIC_WITH_CURRENT_FAMILY;
                else                           return NON_LAX_FOR_OTHER_REASON;
            }
        }
        else{
//            printf("Error in file \"%s\", line %u\n", __FILE__, __LINE__);
            return SHOCK_ERROR;
        }
    }
}

int Shock::curve_constructor(Boundary *boundary, bool local_shock, int type_of_shock, int family, int increase, RealVector &ref_point, RealVector &pnext, 
                             RealVector &tangent, RealVector &normal, int number_ignore_doub_contact,
                             FluxFunction *ff, AccumulationFunction *aa, 
                             double lambda, double lambda_0, double lambda_1, 
                             double sigma, double sign_composite, double sign_shock,
                             std::vector<RealVector> &shockcurve){

    // 
    int max_number_after_boundary = 0;


    int reached_double_contacts = 0;                          

    int n = ref_point.size(); 
//    printf("Inside curve_constructor: n = %d\n", n);
//    if (increase == WAVE_FORWARD) printf("    increase = WAVE_FORWARD\n");
//    else if (increase == WAVE_BACKWARD) printf("    increase = WAVE_BACKWARD\n");
//    else printf("    increase = ???\n");
    RealVector old_tangent(tangent);

    // ONLY FOR SHOCK_AS_ENGINE_FOR_COMPOSITE:
    //
    // When the composite curve intersects the double contact manifold, two cases arise:
    //
    //     1. One point on the rarefaction curve and the corresponding point on the composite curve form
    //        a double contact pair for the current family,
    //     2. The intersection point between the composite curve and the double contact manifold has
    //        a corresponding rarefaction point that does not lay on the double contact manifold.
    //
    // We want to describe Case 1. For this case the composite curve must stop on the double contact manifold. 
    // 
    // We know that at the reference point sigma = lambda_left(family). If the speed of the shock as an engine
    // for the composite should increase then sigma initially will increase. At some point
    // it will reach a maximum. Then it will decrease until a point of minimum is reached. This point
    // of minimum (where sigma(pnext) = lambda_right(family, pnext) for the second time) COINCIDES
    // with the point on the double contact manifold (this is only an intuitive idea, not a theorem---Panters).
    // The situation is analogous for the case in which the speed of the shock as 
    // an engine for the composite should decrease.
    //
    // The following code determines when (sigma(pnext) - lambda_right(family, pnext)) changes from
    // positive to negative in order to obtain the point of minimum aforementioned.
    // The shock curve reached the double contact after the second Bethe-Wendroff point is reached.
    // This effectively take care of the gaps in the composite curve that
    // would be otherwise visible.
    //
    // Also, this means that the rarefaction was not exhausted: the rest of the rarefaction
    // must be pushed onto a stack to be used at a later time.
    //
    int Bethe_Wendroff_points = 0;
    double sigma_minus_lambda_r;
    if (increase == WAVE_FORWARD) sigma_minus_lambda_r = -1.0;
    else sigma_minus_lambda_r = 1.0;

    // ONLY FOR SHOCK_AS_ENGINE_FOR_COMPOSITE.

    bool continue_loop = true;

    double inner_sigma, inner_sigma_old = sigma;

    int info_boundary;
    int edge;
    RealVector p_boundary;

    while (continue_loop){
        // Find normal & tangent.
        hugoniot_der(pnext, ref_point, ff, aa, tangent.components(), normal.components());

        if (ddot(n, tangent.components(), old_tangent.components()) < 0.0){
            for (int i = 0; i < n; i++) tangent.component(i) = -tangent.component(i);
        }
       
       // cout << "Point: " << pnext << ", derivative: " << tangent << endl;
        
        // A estimate for eps, which should be the inverse of the curvature of the curve:
        //
        //     eps = 1/(norm(tangent - old_tangent)/eps).
        //        
        RealVector delta_tangent(n);
        delta_tangent = tangent - old_tangent;
//        eps = 1.0/euclidean_norm(n, delta_tangent.components()); 
        
//        if (euclidean_norm(n, delta_tangent.components()) > 1e-2){
////            eps = 1.0/euclidean_norm(n, delta_tangent.components());
//            eps = 1e-4;
//            printf("First if: norm = %f\n", euclidean_norm(n, delta_tangent.components()));
//        }
//        else{
//            eps = 1e-2;
//            //printf("Second if: eps = %f\n", eps);
//        }
//        printf("eps = %f\n", eps);

        double curvature;
        double dRH2[2][2];
        hugoniot_der2(pnext, ref_point, ff, aa, &dRH2[0][0], curvature);
        double eps = min(1e-4, fabs(1.0/curvature));

        eps = 1e-3; // Was: 1e-4


        // Compute pnext.
        RealVector p(pnext);

        // Test eps

        double test_eps = sgn(eps)*1e-3; // Was: 1e-4


//        shock_step(p, in, tangent, normal, ff, aa, eps, pnext);
//        std::cout << "Point: " << p << std::endl;
//        std::cout << "    Tangent: " << tangent << std::endl;
        int info_shock_step = shock_step(p, ref_point, tangent, normal, ff, aa, test_eps, pnext);
        //printf("Curve constructor. After shock_step, pnext = (%lf, %lf), line = %u\n", pnext.component(0), pnext.component(1), __LINE__);
        if (info_shock_step == SHOCK_ERROR){
//            printf("Error in file \"%s\", line %u\n", __FILE__, __LINE__);
            return SHOCK_ERROR;
        }


        // BEGIN TEST
        if (type_of_shock == SHOCK_AS_ENGINE_FOR_COMPOSITE){
            double sigma = shockspeed(n, ref_point.components(), pnext.components(), ff, aa);

            // Find lambda and the eigenvectors.
            double FJ[n][n], GJ[n][n];

            fill_with_jet(ff, n, pnext.components(), 1, 0, &FJ[0][0], 0);
            fill_with_jet(aa, n, pnext.components(), 1, 0, &GJ[0][0], 0);

            std::vector<eigenpair> e;
            int info = Eigen::eig(n, &FJ[0][0], &GJ[0][0], e);

            double lambda_r = e[family].r;

            //printf("    sigma = %g, lambda_r = %g, sigma - lambda_r = %g\n", sigma, lambda_r, sigma - lambda_r);

//            if ((sigma - lambda_r)*sigma_minus_lambda_r < 0.0) Bethe_Wendroff_points++;
            
//            if (Bethe_Wendroff_points >= 2){
            if ( ((sigma - lambda_r)*sigma_minus_lambda_r < 0.0) && 
                 (
                   ((sigma - lambda_r)  < 0.0 && increase == WAVE_FORWARD) ||
                   ((sigma - lambda_r)  > 0.0 && increase == WAVE_BACKWARD)
                 )
               ){
                        // pnext is added to the curve.
                        shockcurve.push_back(pnext);
                        reached_double_contacts++;
//                 printf("sigma-lambda_r %f\n", sigma - lambda_r);
//                 printf("reached_double_contacts %d\n", reached_double_contacts);
                        if (reached_double_contacts > number_ignore_doub_contact) return SHOCK_REACHED_DOUBLE_CONTACT;
            }

            sigma_minus_lambda_r = sigma - lambda_r;
               // printf("sigma-lambda_r sempre%f\n", sigma - lambda_r);
        }
        // END TEST


        for (int i = 0; i < n; i++) old_tangent.component(i) = tangent.component(i);

        if (type_of_shock == SHOCK_AS_ENGINE_FOR_COMPOSITE){
            double inner_sign = (double)sgn(lambda - shockspeed(n, ref_point.components(), pnext.components(), ff, aa));
            continue_loop = sign_composite*inner_sign > 0.0;
            //printf("    sign_composite = %lf, inner_sign = %lf\n", sign_composite, inner_sign);
        //printf("continue_loop = %d, pnext = (%lf, %lf)\n", continue_loop, pnext.component(0), pnext.component(1));
        }
        else {
            int info_verify_lax = verify_lax_shock(ref_point, lambda_0, lambda_1, pnext, increase, family, ff, aa);
//            if (info_verify_lax == NON_LAX_SHOCK || info_verify_lax == SHOCK_ERROR){
            if (info_verify_lax == NON_LAX_AFTER_BECOMING_RIGHT_CHARACTERISTIC_WITH_CURRENT_FAMILY || 
                info_verify_lax == SHOCK_ERROR ||
                info_verify_lax == NON_LAX_FOR_OTHER_REASON){

//                printf("    Inside curve_constructor, line = %u: NON LAX!!!, error code = %d\n", __LINE__, info_verify_lax);
                return info_verify_lax;
            } 
            
            
        /*
            inner_sigma = shockspeed(n, ref_point.components(), pnext.components(), ff, aa);

            double inner_sign[2];
            inner_sign[0] = (double)sgn(lambda_0 - inner_sigma);
            inner_sign[1] = (double)sgn(lambda_1 - inner_sigma);
            continue_loop = sign_shock*inner_sign[0] > 0.0;
            
            // TODO: This is a TEMPORARY measure. The second condition should be verified for correctness. 
            // With this second stop condition sometimes the shock curve fails in one increase direction (Forward or Backward). 
            // More specifically, when the shock is non-local the following condition fails and 
            // the curve is not computed if: (Family 0 AND Backward) OR (Family 1 AND Forward). 
            //
            if (!local_shock) continue_loop = continue_loop && (sign_shock*inner_sign[1] > 0.0);
             
            if (!continue_loop){
                printf("Curve constructor: inner sign failed\n");
                return SHOCK_OK;
            }

            if ((inner_sigma >= inner_sigma_old && increase == WAVE_FORWARD) ||
                (inner_sigma <= inner_sigma_old && increase == WAVE_BACKWARD)) {
                printf("Curve constructor: inner sigma failed\n");
                return SHOCK_OK;
            }
            else inner_sigma_old = inner_sigma;
            */
        }


        // Verify if there is a boundary collision and store the previous point.
        //
        if (type_of_shock == SHOCK_FOR_ITSELF){
            info_boundary = boundary->intersection(shockcurve[shockcurve.size() - 1], pnext, p_boundary, edge);
            if (info_boundary == 1){
                // Both inside.
                shockcurve.push_back(pnext);
            }
            else if (info_boundary == -1){
                // Both outside
//                printf("Error in file \"%s\", line %u\n", __FILE__, __LINE__);
                return SHOCK_ERROR;
            }
            else {
                // One inside, one outside
                shockcurve.push_back(p_boundary);
//                printf("Curve constructor reached boundary in file \"%s\", line %u\n", __FILE__, __LINE__);
                return SHOCK_REACHED_BOUNDARY;
                //return SHOCK_ERROR;
            }
        }
        else if (type_of_shock == SHOCK_AS_ENGINE_FOR_COMPOSITE){
            if (!boundary->inside(pnext)){
//                printf("Curve constructor reached boundary in file \"%s\", line %u\n    pnext = (%lf, %lf)\n", __FILE__, __LINE__, pnext.component(0), pnext.component(1));
                if (max_number_after_boundary >= 100) return SHOCK_REACHED_BOUNDARY;
                else max_number_after_boundary++;
            }
            shockcurve.push_back(pnext);
        }
    }
    
    return SHOCK_OK;
}



void Shock::curveCalc(const RealVector &ref, bool local_shock, const RealVector &in, int increase, int family, int type_of_shock, const RealVector *orig_direction, int number_ignore_doub_contact, FluxFunction *ff, AccumulationFunction *aa, Boundary *boundary,
            std::vector<RealVector> &shockcurve, int &info_shockcurve, std::vector<RealVector> &shockcurve_alt, int &info_shockcurve_alt,double newtonTolerance) {
    curve(ref,local_shock,in,increase,family,type_of_shock,orig_direction,number_ignore_doub_contact,ff,aa,boundary,shockcurve,info_shockcurve,shockcurve_alt,info_shockcurve_alt);
  }

//int Shock::curve(const RealVector &ref, bool local_shock, const RealVector &in, int increase, int family, int type_of_shock, const RealVector *orig_direction, FluxFunction *ff, AccumulationFunction *aa, Boundary *boundary, 
//                 std::vector<RealVector> &shockcurve, std::vector<RealVector> &shockcurve_alt){
void Shock::curve(const RealVector &ref, bool local_shock, const RealVector &in, int increase, int family, int type_of_shock, const RealVector *orig_direction, int number_ignore_doub_contact, FluxFunction *ff, AccumulationFunction *aa, Boundary *boundary, 
                 std::vector<RealVector> &shockcurve, int &info_shockcurve, std::vector<RealVector> &shockcurve_alt, int &info_shockcurve_alt){

    double eps = 1e-3;

    int n = in.size(); // = 2.

    shockcurve.clear();
    shockcurve.push_back(in);

    // Find lambda and the eigenvectors.
    double FJ[n][n], GJ[n][n];

    RealVector ref_point(ref);

    fill_with_jet(ff, n, ref_point.components(), 1, 0, &FJ[0][0], 0);
    fill_with_jet(aa, n, ref_point.components(), 1, 0, &GJ[0][0], 0);

    vector<eigenpair> e;
    int info = Eigen::eig(n, &FJ[0][0], &GJ[0][0], e);

    // The i-th eigenvalue must be real. // The eigenvalues must be chosen carefully in the n-dimensional case.
    // ALL eigenvalues must be real. Extend this by using a for cycle.
    if (fabs(e[family].i) > 1e-6){
//        printf("Shock::curve(): Init step, eigenvalue %d is complex: % f %+f.\n", family, e[family].r, e[family].i);

//        printf("Error in file \"%s\", line %u\n", __FILE__, __LINE__);

        info_shockcurve = info_shockcurve_alt = SHOCK_ERROR;
//        return SHOCK_ERROR;    
        return;
    }

    // Eigenvalue of the current family (could be the slow or fast one).
    double lambda = e[family].r;

    // Eigenvalue of the slow family
    double lambda_0 = e[0].r;
    
    // Eigenvalue of the fast family
    double lambda_1 = e[1].r;

    RealVector normal(n), tangent(n);

    double r[n];

    if (local_shock){
        // TODO: At a coincidence point with equal eigenvectors choose the tangent as
        //       the eigenvector and the normal as its orthogonal vector.
        //
        for (int i = 0; i < n; i++){
            tangent.component(i) = e[family].vrr[i];
            normal.component(i)  = e[!family].vrr[i];
        }
        
        //hugoniot_der(in, ref_point, ff, aa, tangent.components(), normal.components());
    }
    else {
        hugoniot_der(in, ref_point, ff, aa, tangent.components(), normal.components());

//        std::cout << "Init.: " << in << ", ref.: " << ref_point << std::endl;
//        std::cout << "Tanget (hug.): " << tangent << std::endl;
//        std::cout << "Tanget (vec.): (" << e[family].vrr[0] << ", " << e[family].vrr[1] << ")" << std::endl;
    }

    // Initialize.
    RealVector init(in);
    RealVector p_right(n), p_left(n);
    double eps_right, eps_left;
    eps_right = +eps; // Epsilon can be changed by shock_step(), this way
                      // both directions are tried with the same epsilon.
    eps_left  = -eps; 

    int info_right = shock_step(in, ref_point, tangent, normal, ff, aa, eps_right, p_right);
    int info_left  = shock_step(in, ref_point, tangent, normal, ff, aa,  eps_left,  p_left);

    double s_right, s_left;
    s_right = shockspeed(n, ref_point.components(), p_right.components(), ff, aa); 
    s_left  = shockspeed(n, ref_point.components(),  p_left.components(), ff, aa);

    RealVector pnext(n);

//    std::cout << "    ref     = " << ref_point << std::endl;
//    std::cout << "    init    = " << in  << std::endl;
//    std::cout << "    tangent = " << tangent  << std::endl;
//    std::cout << "    p_right = " << p_right << std::endl;
//    std::cout << "    p_left  = " << p_left  << std::endl;
//    printf("Here. orig_direction = %p\n", orig_direction);
//    double orig_direction_double[orig_direction->size()];

//    for (int i = 0; i < orig_direction->size(); i++) orig_direction_double[i] = orig_direction->component(i);
    double orig_direction_double[in.size()];
    //for (int i = 0; i < orig_direction->size(); i++) orig_direction_double[i] = orig_direction->component(i);

    if (local_shock){
        if (increase == WAVE_FORWARD){
            if (type_of_shock == SHOCK_AS_ENGINE_FOR_COMPOSITE){
                for (int i = 0; i < orig_direction->size(); i++) orig_direction_double[i] = orig_direction->component(i);

                double p_diff_right[n], p_diff_left[n];
                for (int i = 0; i < n; i++){
                    p_diff_right[i] = p_right.component(i) - ref_point.component(i);
                    p_diff_left[i]  = p_left.component(i) - ref_point.component(i);
                }
//                printf("p_diff_right = (%lf, %lf)\n", p_diff_right[0],p_diff_right[1]);
//                printf("p_diff_left = (%lf, %lf)\n", p_diff_left[0],p_diff_left[1]);
                if      (ddot(n, orig_direction_double, p_diff_right) > 0.0){
                    for (int i = 0; i < n; i++) pnext.component(i) = p_right.component(i);
                    eps = eps_right;
//                    printf("Right\n");
                }
                else if (ddot(n, orig_direction_double, p_diff_left) > 0.0){
                    for (int i = 0; i < n; i++) pnext.component(i) = p_left.component(i);
                    eps = eps_left;
//                    printf("Left\n");
                }
                else {
//                    printf("******Error init. Engine forward");
//                    printf("Error in file \"%s\", line %u\n", __FILE__, __LINE__);

                    info_shockcurve = info_shockcurve_alt = SHOCK_ERROR;
//                    return SHOCK_ERROR;
                    return;
                }
            } 
             
           else if (type_of_shock == SHOCK_FOR_ITSELF){
                 if      (s_left < lambda && lambda < s_right){
                    for (int i = 0; i < n; i++) pnext.component(i) = p_left.component(i);
                    eps = eps_left;
                 }
                 else if (s_left > lambda && lambda > s_right){
                    for (int i = 0; i < n; i++) pnext.component(i) = p_right.component(i);
                    eps = eps_right;
                 }
                else {
//                    printf("******Error init. Itself, forward. sl = %f, lambda = %f, sr = %f\n", s_left, lambda, s_right);
//                    printf("Error in file \"%s\", line %u\n", __FILE__, __LINE__);

                    info_shockcurve = info_shockcurve_alt = SHOCK_ERROR;
//                    return SHOCK_ERROR;

                    // Add sigma before returning.
                    //add_sigma(ref, (const FluxFunction*)ff, (const AccumulationFunction*)aa, shockcurve);
                    return;
                }
           }
        }
        else if (increase == WAVE_BACKWARD){
              if (type_of_shock == SHOCK_AS_ENGINE_FOR_COMPOSITE){
                for (int i = 0; i < orig_direction->size(); i++) orig_direction_double[i] = orig_direction->component(i);

                double p_diff_right[n], p_diff_left[n];
                for (int i = 0; i < n; i++){
                    p_diff_right[i] = p_right.component(i) - ref_point.component(i);
                    p_diff_left[i]  = p_left.component(i) - ref_point.component(i);
                }

                normalize_vector(n, p_diff_right);
                normalize_vector(n, p_diff_left);

                if      (ddot(n, orig_direction_double, p_diff_right) > 0.0){
                    for (int i = 0; i < n; i++) pnext.component(i) = p_right.component(i);
                    eps = eps_right;
//                    printf("*** Right selected. ddot = %g, eps = %g ***\n", ddot(n, orig_direction_double, p_diff_right), eps);
//                    printf("    p_diff_right = (%g, %g)\n", p_diff_right[0], p_diff_right[1]);
//                    printf("            orig = (%g, %g)\n", orig_direction_double[0], orig_direction_double[1]);
                }
                else if (ddot(n, orig_direction_double, p_diff_left) > 0.0){
                    for (int i = 0; i < n; i++) pnext.component(i) = p_left.component(i);
                    eps = eps_left;
//                    printf("*** Left selected.  ddot = %g, eps = %g ***\n", ddot(n, orig_direction_double, p_diff_left), eps);
//                    printf("    p_diff_left = (%g, %g)\n", p_diff_left[0], p_diff_left[1]);
//                    printf("            orig = (%g, %g)\n", orig_direction_double[0], orig_direction_double[1]);
                }
                else {
//                    printf("******Error init. Engine forward");
//                    printf("Error in file \"%s\", line %u\n", __FILE__, __LINE__);

                    info_shockcurve = info_shockcurve_alt = SHOCK_ERROR;
//                    return SHOCK_ERROR;
                    return;
                }
             }
             else if (type_of_shock == SHOCK_FOR_ITSELF){
                if      (s_left < lambda && lambda < s_right){
                    for (int i = 0; i < n; i++) pnext.component(i) = p_right.component(i);
                    eps = eps_right;
                }
                else if (s_left > lambda && lambda > s_right){
                    for (int i = 0; i < n; i++) pnext.component(i) = p_left.component(i);
                    eps = eps_left;
                }
                else {
//                    printf("******Error init. Itself, backward. sl = %f, lambda = %f, sr = %f\n", s_left, lambda, s_right);
//                    printf("Error in file \"%s\", line %u\n", __FILE__, __LINE__);

                    info_shockcurve = info_shockcurve_alt = SHOCK_ERROR;
//                    return SHOCK_ERROR;

                    // Add sigma before returning.
                    //add_sigma(ref, ff, aa, shockcurve);
                    return;
                }
            }
        }
        else {
//            printf("Error in file \"%s\", line %u\n", __FILE__, __LINE__);
            info_shockcurve = info_shockcurve_alt = SHOCK_ERROR;
//                    return SHOCK_ERROR;

            // COULD BE ELIMINATED
            // Add sigma before returning.
            //add_sigma(ref, ff, aa, shockcurve);
            return;
        }
        
        // While
        //printf("After init.\n");

        // Verify if pnext is contained within the boundary.
        int info_boundary; int edge;
        RealVector p_boundary;
        info_boundary = boundary->intersection(in, pnext, p_boundary, edge);
        if (info_boundary == 1){
            // Both inside. Do nothing, carry on.
        }
        else if (info_boundary == -1){
            // Both outside
//            printf("Error in file \"%s\", line %u\n", __FILE__, __LINE__);
            info_shockcurve = info_shockcurve_alt = SHOCK_ERROR;
//                    return SHOCK_ERROR;

             // Add sigma before returning.
             //add_sigma(ref, ff, aa, shockcurve);

             return;
        }
        else {
            // One inside, one outside
            shockcurve.push_back(p_boundary);

            info_shockcurve = SHOCK_REACHED_BOUNDARY;
            info_shockcurve_alt = SHOCK_ERROR; 

            // Add sigma before returning.
            //add_sigma(ref, ff, aa, shockcurve);

            return;
//            return SHOCK_REACHED_BOUNDARY;
        }

        //printf("after boundary, size = %d\n", shockcurve.size());

        RealVector diff_init_next(2);
        for (int i = 0; i < 2; i++) diff_init_next.component(i) = pnext.component(i) - init.component(i);

//        if (dotprod(n, tangent.components(), (pnext - init).components()) < 0.0) for (int i = 0; i < n; i++) tangent.component(i) = -tangent.component(i);
        if (dotprod(n, tangent.components(), diff_init_next.components()) < 0.0) for (int i = 0; i < n; i++) tangent.component(i) = -
tangent.component(i);
        double sigma = shockspeed(n, ref_point.components(), pnext.components(), ff, aa);
        double sign_composite = (double)sgn(lambda   - sigma);
        double sign_shock     = (double)sgn(lambda_0 - sigma);
        
       // printf("Before curve_constructor(), shockcurve.size() = %d\n", shockcurve.size());

        int info_curve_constructor = curve_constructor(boundary, local_shock, type_of_shock, family, increase, ref_point, pnext, 
                                                   tangent, normal, number_ignore_doub_contact,
                                                   ff, aa, 
                                                   lambda, lambda_0, lambda_1, 
                                                   sigma, sign_composite, sign_shock,
                                                   shockcurve);
                                                
//        printf("After curve_constructor(), info = %d, shockcurve.size() = %d\n", info_curve_constructor, shockcurve.size());
//        printf("Error in file \"%s\", line %u\n", __FILE__, __LINE__);

        info_shockcurve = info_curve_constructor;
        info_shockcurve_alt = -1; //SHOCK_ERROR; 

        // Add sigma before returning.
        //add_sigma(ref, ff, aa, shockcurve);

        return;
//        return info_curve_constructor;
    }
    else { // Non-local. So far, non-local is for itself: SHOCK_FOR_ITSELF (maybe to be modified in the future).
        printf("Shock. Non-local\n");

        int info_lax = verify_lax_shock(ref_point, lambda_0, lambda_1, init, increase, family, ff, aa);
        if (info_lax == NON_LAX_SHOCK){
//            printf("Initial point does not satisfy Lax condition\n");

            info_shockcurve = info_shockcurve_alt = NON_LAX_SHOCK;

            // Add sigma before returning.
            //add_sigma(ref, ff, aa, shockcurve);
            //add_sigma(ref, ff, aa, shockcurve_alt);

            return;

//            return NON_LAX_SHOCK;
        }
//        else printf("Initial point satisfies Lax condition\n"); // TODO: Remove this else and the print above.
    
        double sigma_init = shockspeed(n, ref_point.components(), init.components(), ff, aa);
        
        // Tangents
        RealVector tangent_left(n), tangent_right(n), normal_left(n), normal_right(n);
        
        // Increase
        //int increase_left, increase_right;

        // Right
        hugoniot_der(p_right, ref_point, ff, aa, tangent_right.components(), normal_right.components());
        if (dotprod(n, tangent_right.components(), (p_right - init).components()) < 0.0) for (int i = 0; i < n; i++) tangent_right.component(i) = -tangent_right.component(i);
        
        double sigma_right = shockspeed(n, ref_point.components(), p_right.components(), ff, aa);
        double sign_composite_right = (double)sgn(lambda   - sigma_right);
        double sign_shock_right     = (double)sgn(lambda_0 - sigma_right);

        //if (sigma_right < sigma_init) increase_right  = WAVE_FORWARD;
        //else                          increase_right  = WAVE_BACKWARD;       
  
        shockcurve.clear();
        shockcurve.push_back(init);
        shockcurve.push_back(p_right);
        int info_curve_constructor_right = curve_constructor(boundary, local_shock, type_of_shock, family, increase, ref_point, p_right, 
                                                       tangent_right, normal_right, number_ignore_doub_contact, 
                                                       ff, aa, 
                                                       lambda, lambda_0, lambda_1, 
                                                       sigma_right, sign_composite_right, sign_shock_right,
                                                       shockcurve);
        info_shockcurve = info_curve_constructor_right;

        // Left
        hugoniot_der(p_left, ref_point, ff, aa, tangent_left.components(), normal_left.components());
        if (dotprod(n, tangent_left.components(), (p_left - init).components()) < 0.0) for (int i = 0; i < n; i++) tangent_left.component(i) = -tangent_left.component(i);
        
        double sigma_left = shockspeed(n, ref_point.components(), p_left.components(), ff, aa);
        double sign_composite_left = (double)sgn(lambda   - sigma_left);
        double sign_shock_left     = (double)sgn(lambda_0 - sigma_left);

        //if (sigma_left < sigma_init) increase_left  = WAVE_FORWARD;
        //else                         increase_left  = WAVE_BACKWARD;            
        

        shockcurve_alt.clear();
        shockcurve_alt.push_back(init);
        shockcurve_alt.push_back(p_left);
        int info_curve_constructor_left = curve_constructor(boundary, local_shock, type_of_shock, family, increase, ref_point, p_left, 
                                                       tangent_left, normal_left, number_ignore_doub_contact,
                                                       ff, aa, 
                                                       lambda, lambda_0, lambda_1, 
                                                       sigma_left, sign_composite_left, sign_shock_left,
                                                       shockcurve_alt);

        info_shockcurve_alt = info_curve_constructor_left;

        // Add sigma before returning.
        //add_sigma(ref, ff, aa, shockcurve);
        //add_sigma(ref, ff, aa, shockcurve_alt);

        return;                                                
//        return (info_curve_constructor_right && info_curve_constructor_left);
    }
    
    
/*
    RealVector old_tangent(tangent);

    bool continue_loop = true;

    double inner_sigma, inner_sigma_old = sigma;
    while (continue_loop){
        // Find normal & tangent.
        hugoniot_der(pnext, ref_point, ff, aa, tangent.components(), normal.components());

        if (dotprod(n, tangent.components(), old_tangent.components()) < 0.0){
            for (int i = 0; i < n; i++) tangent.component(i) = -tangent.component(i);
        }
       
        
        // A estimate for eps, which should be the inverse of the curvature of the curve:
        //
        //     eps = 1/(norm(tangent - old_tangent)/eps).
        //        
        RealVector delta_tangent(n);
        delta_tangent = tangent - old_tangent;
//        eps = 1.0/euclidean_norm(n, delta_tangent.components()); 
        
//        if (euclidean_norm(n, delta_tangent.components()) > 1e-2){
////            eps = 1.0/euclidean_norm(n, delta_tangent.components());
//            eps = 1e-4;
//            printf("First if: norm = %f\n", euclidean_norm(n, delta_tangent.components()));
//        }
//        else{
//            eps = 1e-2;
//            //printf("Second if: eps = %f\n", eps);
//        }
//        printf("eps = %f\n", eps);

        double curvature;
        double dRH2[2][2];
        hugoniot_der2(pnext, ref_point, ff, aa, &dRH2[0][0], curvature);
        eps = min(1e-4, fabs(1.0/curvature));
        eps = 1e-4;

        // Compute pnext.
        RealVector p(pnext);

        // Test eps
        double test_eps = sgn(eps)*1e-2;

//        shock_step(p, in, tangent, normal, ff, aa, eps, pnext);
        std::cout << "Point: " << p << std::endl;
        std::cout << "    Tangent: " << tangent << std::endl;
        int info_shock_step = shock_step(p, ref_point, tangent, normal, ff, aa, test_eps, pnext);
        if (info_shock_step == SHOCK_ERROR) return SHOCK_ERROR;

//        it++;
        for (int i = 0; i < n; i++) old_tangent.component(i) = tangent.component(i);

        if (type_of_shock == SHOCK_AS_ENGINE_FOR_COMPOSITE){
            double inner_sign = (double)sgn(lambda - shockspeed(n, ref_point.components(), pnext.components(), ff, aa));
            continue_loop = sign_composite*inner_sign > 0.0;
        }
        else {
            inner_sigma = shockspeed(n, ref_point.components(), pnext.components(), ff, aa);

            double inner_sign[2];
            inner_sign[0] = (double)sgn(lambda_0 - inner_sigma);
            inner_sign[1] = (double)sgn(lambda_1 - inner_sigma);
            continue_loop = (sign_shock*inner_sign[0] > 0.0) && (sign_shock*inner_sign[1] > 0.0);
            if (!continue_loop) continue;

            if ((inner_sigma >= inner_sigma_old && increase == WAVE_FORWARD) ||
                (inner_sigma <= inner_sigma_old && increase == WAVE_BACKWARD)) continue_loop = false;
            else inner_sigma_old = inner_sigma;
        }

        // Verify if there is a boundary collision and store the previous point.
        //
        info_boundary = boundary->intersection(shockcurve[shockcurve.size() - 1], pnext, p_boundary, edge);
        if (info_boundary == 1){
            // Both inside.
            shockcurve.push_back(pnext);
        }
        else if (info_boundary == -1){
            // Both outside
            return SHOCK_ERROR;
        }
        else {
            // One inside, one outside
            shockcurve.push_back(p_boundary);
            return SHOCK_REACHED_BOUNDARY;
        }

    }
       
    return SHOCK_OK;
    
    */
    
}























// For the moment being, ONLY FOR 2D.
//
double Shock::find_initial_epsilon(std::vector<RealVector> &right_eigenvector, std::vector<RealVector> &r_dot, double cos_alpha){
    int n = r_dot.size();

    // Compute the norm for each family's r_dot.
    //
    std::vector<double> norm_r_dot(n);
    for (int family = 0; family < n; family++){
        norm_r_dot[family] = 0.0;
        for (int j = 0; j < n; j++) norm_r_dot[family] += r_dot[family].component(j)*r_dot[family].component(j);
        norm_r_dot[family] = sqrt(norm_r_dot[family]);
//        printf("norm_r_dot[%d] = %lf\n", family, norm_r_dot[family]);
    }

    double k = pow(norm_r_dot[0]/norm_r_dot[1], 1.0/3.0);
    k = 50000.0;
    //printf("k = %lf\n", k);

    while(
          sqrt((2*norm_r_dot[0])/(k*k*k*norm_r_dot[1]))*(1.0 + k*cos_alpha*.5) <= (1.0 + cos_alpha/k)
         ){
        k *= .5;
        //printf("    Iterating... k = %lg\n", k);
    }

    // Definition of the initial epsilon:
    // TEST
//    printf("Test epsilon = %lf\n", pow(1.0/(norm_r_dot[0]*norm_r_dot[0]*norm_r_dot[1]), 1.0/3.0));
    // TEST

    //printf("<r, r_dot> = %lf\n", ddot(n, right_eigenvector[1].components(), r_dot[0].components()));
//    printf("<r_1, r_dot_0> = %lg\n", ddot(n, right_eigenvector[1].components(), r_dot[0].components()));
//    printf("<r_0, r_dot_1> = %lg\n", ddot(n, right_eigenvector[0].components(), r_dot[1].components()));

    double init_epsilon = k*ddot(n, right_eigenvector[1].components(), r_dot[0].components())/(norm_r_dot[0]*norm_r_dot[0]*(1.0 + k*cos_alpha*.5)*(1.0 + k*cos_alpha*.5));
    return init_epsilon;
}




// This function must be called for every family.
//
// When accumulation is taken into account, MAYBE a d2accum term will
// be added to this function's signature as another output.
//
int Shock::compute_r_dot(int n, int fam, const RealVector &p, FluxFunction *fluxfunction, AccumulationFunction *accumulationfunction, 
                         std::vector<RealVector> &right_eigenvector, std::vector<RealVector> &r_dot, double &cos_alpha){
    r_dot.resize(n);

    double point[n];
    for (int i = 0; i < n; i++) point[i] = p.component(i);
    double A[n][n];
    double B[n][n];

    double H[n][n][n];
    double M[n][n][n];

    fill_with_jet((RpFunction*)accumulationfunction, n, point, 2, 0, &B[0][0], &M[0][0][0]);
    fill_with_jet((RpFunction*)fluxfunction,         n, point, 2, 0, &A[0][0], &H[0][0][0]);

    // Extract the left and right eigenvalues of the generalized system.
    std::vector<eigenpair> e;
    int info = Eigen::eig(n, &A[0][0], &B[0][0], e);

    // Extract the indx-th left and right-eigenvector of the GENERALIZED
    // PROBLEM (A - lambda B)r=0  and  l(A - lambda B)=0

    double l[n], r[n];

    std::vector<RealVector> d2flux(n);
    r_dot.resize(n);
    right_eigenvector.resize(n);

    int family_index[2];
    if (fam == 0){
        family_index[0] = 0;
        family_index[1] = 1;
    }
    else {
        family_index[0] = 1;
        family_index[1] = 0;
    }

//    printf("Family = %d\n", fam);
//    for (int i = 0; i < n; i++) printf("    family_index[%d] = %d\n", i, family_index[i]);

for (int family = 0; family < n; family++){
    d2flux[family_index[family]].resize(n);

    for (int i = 0; i < n; i++){
        l[i] = e[family_index[family]].vlr[i];
        r[i] = e[family_index[family]].vrr[i];
    }

//    for (int i = 0; i < n; i++) printf("    l[%d] = %f\n", i, l[i]);
//    for (int i = 0; i < n; i++) printf("    r[%d] = %f\n", i, r[i]);

    // Extract lambda.
    // The i-th eigenvalue must be real. 
    // The eigenvalues must be chosen carefully in the n-dimensional case.
    // ALL eigenvalues must be real. Extend this by using a for cycle.
    //
    double lambda;

    if (e[family_index[family]].i != 0){
        //printf("Inside dirdrv(): Init step, eigenvalue %d is complex: % f %+f.\n", family, e[family].r, e[family].i);
        return ABORTED_PROCEDURE;     
    }
    else lambda = e[family_index[family]].r;

    // Compute D and E
    double D[n][n];
    double E[n][n];
    for (int k = 0; k < n; k++){
       double SubH[n][n];
       double SubM[n][n];

       for (int i = 0; i < n; i++){
           for (int j = 0; j < n; j++){
               SubH[i][j] = H[k][i][j];
               SubM[i][j] = M[k][i][j];
           }
       }

       double rowh[n];
       double rowm[n];

       matrixmult(1, n, n, &r[0], &SubH[0][0], &rowh[0]);
       matrixmult(1, n, n, &r[0], &SubM[0][0], &rowm[0]);

/*     matrixmult(n, n, 1, &SubH[0][0], &r[0], &rowh[n]);
       matrixmult(n, n, 1, &SubK[0][0], &r[0], &rowk[n]); */

       for (int j = 0; j < n; j++) D[k][j] = rowh[j];             
       for (int j = 0; j < n; j++) E[k][j] = rowm[j];         
    }

    // Compute D*r and E*r
    double Dtimesr[n];
    double Etimesr[n];

    matrixmult(n, n, 1, &D[0][0], &r[0], &Dtimesr[0]);
    matrixmult(n, n, 1, &E[0][0], &r[0], &Etimesr[0]);

    // Using LAPACK's least square method returns different solutions
    // for the same input, therefore this method is not used here.
    //
    // The system to be solved:
    //
    //     (lambda_k*I - DF)*dr_k = D2F(r_k, r_k) - c*r_k,    (1)
    //     
    //     r_k^T*dr_k = 0,                                    (2), orthogonality condition
    //
    // for k = 0, 1, where k is the family.
    //
    // Since matrix (lambda_k*I - DF) is singular and (lambda_k*I - DF)*r_k = 0 the system (1)-(2)
    // has rank = 2, so any of the equations in (1) must be dropped, to obtain a 2 times 2 system.
    // The second equation will be dropped here.
    //
    // Care should be taken when generalizing to space dimension greater than 2
    // because it is not known a priori which equations can be dropped.
    //
    // System of equations to be solved by least-squares using LAPACK.
//    double AA[n + 1][n];
//    for (int i = 0; i < n; i++){
//        for (int j = 0; j < n; j++){
//            AA[i][j] = -A[i][j];
//        }
//        AA[i][i] += lambda;

//        AA[n][i] = r[i];
//    }

    // Compute B*r
    double Btimesr[n];
    matrixmult(n, n, 1, &B[0][0], &r[0], &Btimesr[0]);

    // Directional derivative
    double Dtimesr_minus_lambdaEtimesr[n];
    for (int i = 0; i < n; i++) Dtimesr_minus_lambdaEtimesr[i] = Dtimesr[i] - lambda*Etimesr[i];

    // c = grad(lambda_k)*r_k, the directional derivative.
    //
    double c = ddot(n, Dtimesr, l)/ddot(n, r, l); // This equation does not consider the case when accumulation is non-trivial.
                                                  // The line above do consider such case but even there is a problem somewhere.

    double AA[n][n], BB[n], x[n];
    for (int i = 0; i < n; i++){
        for (int j = 0; j < n; j++){
            AA[i][j] = -A[i][j];
        }
        AA[i][i] -= lambda;

        BB[i] = Dtimesr[i] - c*r[i];
    }
    for (int i = 0; i < n; i++) AA[n - 1][i] = r[i];
    BB[n - 1] = 0.0;

//    printf("Jac = \n");
//    for (int i = 0; i < n; i++){
//        printf("    | ");
//        for (int j = 0; j < n; j++){
//            printf(" %lf ", A[i][j]);
//        }
//        printf("|\n");
//    }

//    printf("D = \n");
//    for (int i = 0; i < n; i++){
//        printf("    | ");
//        for (int j = 0; j < n; j++){
//            printf(" %lf ", D[i][j]);
//        }
//        printf("|\n");
//    }

//    printf("AA = \n");
//    for (int i = 0; i < n; i++){
//        printf("    | ");
//        for (int j = 0; j < n; j++){
//            printf(" %lf ", AA[i][j]);
//        }
//        printf("|\n");
//    }

//    printf("BB = \n");
//    for (int i = 0; i < n; i++){
//        printf("    | %lf |\n", BB[i]);
//    }


    int info = cdgesv(n, &AA[0][0], BB, x);
    r_dot[family].resize(n);
    for (int i = 0; i < n; i++) r_dot[family].component(i) = x[i];

        // Copy this family's right eigenvector.
        //
        for (int i = 0; i < n; i++) right_eigenvector[family].component(i) = r[i];
    }

    // Compute the cosine of the angle formed by the two right-eigenvectors.
    // The case where n > 2 is not accounted for yet.
    //
    double r0[n], r1[n];
    for (int i = 0; i < n; i++){
        r0[i] = e[family_index[0]].vrr[i];
        r1[i] = e[family_index[1]].vrr[i];
    }
    cos_alpha = ddot(n, r0, r1);

    return info;
}

//      SUBROUTINE DGELS( TRANS, M, N, NRHS, A, LDA, B, LDB, WORK, LWORK,
//     $                  INFO )
//*
//*  -- LAPACK driver routine (version 3.3.1) --
//*  -- LAPACK is a software package provided by Univ. of Tennessee,    --
//*  -- Univ. of California Berkeley, Univ. of Colorado Denver and NAG Ltd..--
//*  -- April 2011                                                      --
//*
//*     .. Scalar Arguments ..
//      CHARACTER          TRANS
//      INTEGER            INFO, LDA, LDB, LWORK, M, N, NRHS
//*     ..
//*     .. Array Arguments ..
//      DOUBLE PRECISION   A( LDA, * ), B( LDB, * ), WORK( * )
//*     ..
//*
//*  Purpose
//*  =======
//*
//*  DGELS solves overdetermined or underdetermined real linear systems
//*  involving an M-by-N matrix A, or its transpose, using a QR or LQ
//*  factorization of A.  It is assumed that A has full rank.
//*
//*  The following options are provided:
//*
//*  1. If TRANS = 'N' and m >= n:  find the least squares solution of
//*     an overdetermined system, i.e., solve the least squares problem
//*                  minimize || B - A*X ||.
//*
//*  2. If TRANS = 'N' and m < n:  find the minimum norm solution of
//*     an underdetermined system A * X = B.
//*
//*  3. If TRANS = 'T' and m >= n:  find the minimum norm solution of
//*     an undetermined system A**T * X = B.
//*
//*  4. If TRANS = 'T' and m < n:  find the least squares solution of
//*     an overdetermined system, i.e., solve the least squares problem
//*                  minimize || B - A**T * X ||.
//*
//*  Several right hand side vectors b and solution vectors x can be
//*  handled in a single call; they are stored as the columns of the
//*  M-by-NRHS right hand side matrix B and the N-by-NRHS solution
//*  matrix X.
//*
//*  Arguments
//*  =========
//*
//*  TRANS   (input) CHARACTER*1
//*          = 'N': the linear system involves A;
//*          = 'T': the linear system involves A**T.
//*
//*  M       (input) INTEGER
//*          The number of rows of the matrix A.  M >= 0.
//*
//*  N       (input) INTEGER
//*          The number of columns of the matrix A.  N >= 0.
//*
//*  NRHS    (input) INTEGER
//*          The number of right hand sides, i.e., the number of
//*          columns of the matrices B and X. NRHS >=0.
//*
//*  A       (input/output) DOUBLE PRECISION array, dimension (LDA,N)
//*          On entry, the M-by-N matrix A.
//*          On exit,
//*            if M >= N, A is overwritten by details of its QR
//*                       factorization as returned by DGEQRF;
//*            if M <  N, A is overwritten by details of its LQ
//*                       factorization as returned by DGELQF.
//*
//*  LDA     (input) INTEGER
//*          The leading dimension of the array A.  LDA >= max(1,M).
//*
//*  B       (input/output) DOUBLE PRECISION array, dimension (LDB,NRHS)
//*          On entry, the matrix B of right hand side vectors, stored
//*          columnwise; B is M-by-NRHS if TRANS = 'N', or N-by-NRHS
//*          if TRANS = 'T'.
//*          On exit, if INFO = 0, B is overwritten by the solution
//*          vectors, stored columnwise:
//*          if TRANS = 'N' and m >= n, rows 1 to n of B contain the least
//*          squares solution vectors; the residual sum of squares for the
//*          solution in each column is given by the sum of squares of
//*          elements N+1 to M in that column;
//*          if TRANS = 'N' and m < n, rows 1 to N of B contain the
//*          minimum norm solution vectors;
//*          if TRANS = 'T' and m >= n, rows 1 to M of B contain the
//*          minimum norm solution vectors;
//*          if TRANS = 'T' and m < n, rows 1 to M of B contain the
//*          least squares solution vectors; the residual sum of squares
//*          for the solution in each column is given by the sum of
//*          squares of elements M+1 to N in that column.
//*
//*  LDB     (input) INTEGER
//*          The leading dimension of the array B. LDB >= MAX(1,M,N).
//*
//*  WORK    (workspace/output) DOUBLE PRECISION array, dimension (MAX(1,LWORK))
//*          On exit, if INFO = 0, WORK(1) returns the optimal LWORK.
//*
//*  LWORK   (input) INTEGER
//*          The dimension of the array WORK.
//*          LWORK >= max( 1, MN + max( MN, NRHS ) ).
//*          For optimal performance,
//*          LWORK >= max( 1, MN + max( MN, NRHS )*NB ).
//*          where MN = min(M,N) and NB is the optimum block size.
//*
//*          If LWORK = -1, then a workspace query is assumed; the routine
//*          only calculates the optimal size of the WORK array, returns
//*          this value as the first entry of the WORK array, and no error
//*          message related to LWORK is issued by XERBLA.
//*
//*  INFO    (output) INTEGER
//*          = 0:  successful exit
//*          < 0:  if INFO = -i, the i-th argument had an illegal value
//*          > 0:  if INFO =  i, the i-th diagonal element of the
//*                triangular factor of A is zero, so that A does not have
//*                full rank; the least squares solution could not be
//*                computed.

int Shock::cdgels(double *AA, double *BB, double *x){
    int info;

    int m = 3, n = 2, nrhs = 1;

    double A[n][m];
    for (int i = 0; i < n; i++){
        for (int j = 0; j < m; j++) A[i][j] = AA[j*n + i];
    }

    int lda = m;

    double B[m];
    for (int j = 0; j < m; j++) B[j] = BB[j];

    int ldb = m;

    int lwork = 100*m;
    double work[lwork];

    dgels_("N", &m, &n, &nrhs, &A[0][0], &lda, B, &ldb, work, &lwork, &info);

    for (int j = 0; j < n; j++) x[j] = B[j];

    return info;
}

int Shock::cdgesv(int n, double *A, double *b, double *x) {
    int i, j;
    int dim = n;
    int nrhs = 1;
    int lda = n;
    int ipiv[n];
    int ldb = n;
    int info;

    // Create a transposed copy of A to be used by LAPACK's dgesv:
    double B[n][n];
    for (i = 0; i < n; i++) {
        for (j = 0; j < n; j++) B[j][i] = A[i * n + j];
    }

    // Create a copy of b to be used by LAPACK's dgesv:
    double bb[n];
    for (i = 0; i < n; i++) bb[i] = b[i];

    dgesv_(&dim, &nrhs, &B[0][0], &lda, &ipiv[0], &bb[0], &ldb, &info);

    if (info == 0) {
        for (i = 0; i < n; i++) x[i] = bb[i];
        return SUCCESSFUL_PROCEDURE;
    } else return ABORTED_PROCEDURE;
}

void Shock::normalize_vector(int n, double *v){
    double norm_orig = 0.0;
    for (int j = 0; j < n; j++) norm_orig += v[j]*v[j];
    norm_orig = 1.0/sqrt(norm_orig);
    for (int j = 0; j < n; j++) v[j] *= norm_orig;

    return;
}

void Shock::add_sigma(const RealVector &ref, const FluxFunction *ff, const AccumulationFunction *aa, std::vector<RealVector> &curve){
    RealVector temp_ref(ref);

    // Check this initial point
    for (int i = 0; i < curve.size(); i++){
        int n = curve[i].size();

        RealVector p_sigma(curve[i]);
        //for (int j = 0; j < n; j++) p_sigma.component(j) = curve[i].component(j);
        double sigma = shockspeed(n, temp_ref.components(), curve[i].components(), (FluxFunction*)ff, (AccumulationFunction*)aa);

        curve[i].resize(n + 1);
        for (int j = 0; j < n; j++) curve[i].component(j) = p_sigma.component(j);
        curve[i].component(n) = sigma;
    }

    return;
}

