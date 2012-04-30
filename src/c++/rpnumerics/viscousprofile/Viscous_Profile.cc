#include "Viscous_Profile.h"

const FluxFunction * Viscous_Profile::f = NULL;
const AccumulationFunction * Viscous_Profile::a = NULL;

Viscosity_Matrix * Viscous_Profile::vmf = NULL;

void Viscous_Profile::critical_points_linearization(const FluxFunction *ff, const AccumulationFunction *aa, 
                                                    Viscosity_Matrix *v,
                                                    double speed, const std::vector<RealVector> &cp, 
                                                    std::vector< std::vector<eigenpair> > &ep){
    ep.clear();

    for (int i = 0; i < cp.size(); i++){
        RealVector p(cp[i]);
        Matrix<double> JF(2, 2), JG(2, 2);
        ff->fill_with_jet(2, p.components(), 1, 0, JF.data(), 0);
        aa->fill_with_jet(2, p.components(), 1, 0, JG.data(), 0);

        // Find the eigenpairs of:
        //
        // [-speed*JG(cp[i]) + JF(cp[i])]*U_mu = mu*D(cp[i])*U_mu.
        //
        Matrix<double> RH(2, 2), viscous(2, 2);
        for (int k = 0; k < 2; k++){
            for (int j = 0; j < 2; j++){
                RH(k, j) = -speed*JG(k, j) + JF(k, j);
            }
        }

        // Fill the viscous matrix
        v->fill_viscous_matrix(cp[i], viscous);

        std::vector<eigenpair> e;
        Eigen::eig(2, RH.data(), viscous.data(), e);
        ep.push_back(e);
    }

    return;
}

int Viscous_Profile::orbit(const FluxFunction *ff, const AccumulationFunction *aa, 
                           Viscosity_Matrix *v,
                           const Boundary *boundary,const RealVector &init, const RealVector &ref, double speed,
                           double deltaxi,
                           int orbit_direction,
                           std::vector<RealVector> &out){
    f = ff;
    a = aa;
    vmf = v;

    out.clear();

    // The vector of parameters holds 6 elements:
    //
    // param[0]   = sigma (speed)
    // param[1:2] = F(Uref)
    // param[3:4] = G(Uref)
    // param[5]   = ORBIT_FORWARD or ORBIT_BACKWARD
    //
    // It will remain invariable through the following computations.
    //
    int nparam = 6;
    double param[nparam];
    
    double Fref[2], Gref[2];
    RealVector tref(ref);
    f->fill_with_jet(2, tref.components(), 0, Fref, 0 , 0);
    a->fill_with_jet(2, tref.components(), 0, Gref, 0 , 0);

    param[0] = speed;
    for (int i = 0; i < 2; i++){
        param[1 + i] = Fref[i];
        param[3 + i] = Gref[i];
    }

    if (orbit_direction == ORBIT_FORWARD) param[5] = 1.0;
    else                                  param[5] = -1.0;

    // BEGIN Prepare the parameters to be passed to LSODE //
    int n = 2;

    int ml; // Not used.
    int mu; // Not used.
        
    // ???
    int nrpd = 4;
        
    // Is the tolerance the same for all the elements of U (1) or not (2)?
    int itol = 2; // 1: atol scalar; 2: atol array.
    double rtol = 1e-4;
    double atol[n]; for (int i = 0; i < n; i++) atol[i] = 1e-6;
        
    // The Jacobian is provided by the user.
    // int mf = 21; 
    // The Jacobian is NOT provided by the user.
    int mf = 22;    
    // Lsode uses rwork to perform its computations.
    // lrw is the declared length of rwork
    int lrw;
    if (mf == 10)                  lrw = 20 + 16*n;
    else if (mf == 21 || mf == 22) lrw = 22 + 9*n + n*n;
    else if (mf == 24 || mf == 25) lrw = 22 + 10*n + (2*ml + mu)*n;
    double rwork[lrw];

    // Normal computation of values at tout.
    int itask = 1; 
        
    // Set to 1 initially.
    // This is where LSODE's info parameter. Must be set to 1 the first time.
    int istate = 1;  
    // No optional inputs
    int iopt = 0;

    // Lsode uses iwork to perform its computations.
    // liw is the declared length of iwork
    int liw;
    if (mf == 10) liw = 20;
    else if (mf == 21 || mf == 22 || mf == 24 || mf == 25) liw = 20 + n;
    int iwork[liw];        
    // END   Prepare the parameters to be passed to LSODE //

    // Current point
    RealVector new_point(2), previous_point(2);

    double p[2];
    for (int i = 0; i < 2; i++) p[i] = new_point.component(i) = init.component(i);
    double xi = 0.0, new_xi = deltaxi;

    out.push_back(new_point);

    // Find the orbit
    while (true){
        for (int i = 0; i <= n; i++) previous_point.component(i) = new_point.component(i);

        lsode_(&orbit_flux, &n, p, &xi, &new_xi, &itol, &rtol, atol, &itask, &istate, &iopt, rwork, &lrw, iwork, &liw, 0, &mf, &nparam, param);

        // Update new_point.
        for (int i = 0; i < n; i++) new_point.component(i) = p[i];

        // BEGIN Check Boundary //
        // Modified RectBoundary so that the intersection can be tested using RealVectors of size
        // greater than the dimension of the space the RectBoundary is in.
        int where_out;
        RealVector r;
        int intersection_info = boundary->intersection(previous_point, new_point, r, where_out);

        if      (intersection_info == 1){
            // Both points inside. Carry on with the rest of the tests, etc.
            out.push_back(new_point);
        }
        else if (intersection_info == 0){
            // One point is inside, the other is outside. 
            // Store the point lying in the domain's border and get out.
            out.push_back(r);

            printf("Reached boundary\n");

            return SUCCESSFUL_PROCEDURE;
        }
        else {
            // Both points lie outside the domain. Something went awfully wrong here.
            printf("Both outside\n");
            printf("previous_point = (");
            for (int i = 0; i < n; i++){
                printf("%g", previous_point.component(i));
                if (i < n - 1) printf(", ");
            }
            printf(")\n");

            printf("new_point      = (");
            for (int i = 0; i < n; i++){
                printf("%g", new_point.component(i));
                if (i < n - 1) printf(", ");
            }
            printf(")\n");

            return ABORTED_PROCEDURE;
        }
        // END   Check Boundary //

        // Update the independent parameters.
        xi      = new_xi;
        new_xi += deltaxi;
    }
}

// For LSODE
//
// nparam = 1 + 2 + 2 + 1
// param = [sigma; Fref; Gref; orbit_direction]
//
int Viscous_Profile::orbit_flux(int *neq, double *xi, double *in, double *out, int *nparam, double *param){
    // Compute the viscous matrix for the current point and invert it
    RealVector p(2, in);
    Matrix<double> vm(2, 2);
    vmf->fill_viscous_matrix(p, vm);

    double inv_det = 1.0/(vm(0, 0)*vm(1, 1) - vm(0, 1)*vm(1, 0));
    
    Matrix<double> inv_D(2, 2);
    inv_D(0, 0) =  vm(1, 1)*inv_det;
    inv_D(0, 1) = -vm(0, 1)*inv_det;
    inv_D(1, 0) = -vm(1, 0)*inv_det;
    inv_D(1, 1) =  vm(0, 0)*inv_det;

    // Fill some stuff
    //
    double F[2], G[2];
    f->fill_with_jet(2, in, 0, F, 0 , 0);
    a->fill_with_jet(2, in, 0, G, 0 , 0);

    // Sigma
    double sigma = param[0];

    // Fref & Gref
    double Fref[2], Gref[2];
    for (int i = 0; i < 2; i++){
        Fref[i] = param[1 + i];
        Gref[i] = param[3 + i];
    }

    double direction = param[5];

    // The field proper
    for (int i = 0; i < 2; i++){
        out[i] = 0.0;
        for (int j = 0; j < 2; j++) out[i] += direction*inv_D(i, j)*( -sigma*(G[j] - Gref[j]) + (F[j] - Fref[j]) );
    }

    return SUCCESSFUL_PROCEDURE;
}

