#include "jetrarefaction.h"

// Pointer to FluxFunction, to be used throughout this file.
// When rarefactioncurve is invoked, it will set this pointer
// with the address of an externally declared object of a 
// FluxFunction-derived class.
//
FluxFunction *flux_object;

// Computes the rarefaction.
// This function receives the following parameters:
//
//        neq: The dimension of the problem, neq == EIG_MAX.
//         xi: The independent variable. Totally useless, it is passed
//             because of the solver.
//         in: The point at which the solution is found.
//        out: A vector where the solution is stored.
//     nparam: The number of elements of param (q.v.)
//      param: A vector of nparam components. This vector contains
//             extra information, such as the family, etc., and
//             should avoid the use of global variables. In particular, 
//             for the rarefaction, we have that:
//               
//             nparam   = 1;
//             param[0] = family index.
//             param[1:n] = reference vector
//
// The function returns:
// ABORTED_PROCEDURE: Something went wrong,
// SUCCESSFUL_PROCEDURE: OK.
int rarefaction(int *neq, double *xi, double *in, double *out, int *nparam, double *param){
    // The dimension of the problem:
    int n = *neq;
    
    // The family:
    int family = (int)param[0];

    // The reference eigenvector:
    double rev[n]; int ii;
    for (ii = 0; ii < n; ii++) rev[ii] = param[1 + ii];

/*    
    printf("@ rarefaction():\n");
    printf("ref = (");
    for (ii = 0; ii < n; ii++) printf("% f, ", rev[ii]);
    printf(")\n");
*/
    
    // Fill the Jacobian
    double J[n][n];
    fill_with_jet(flux_object, n, in, 1, 0, &J[0][0], 0);

    // Find J's eigencouples and sort them.
    int i, j, info;
    vector<eigencouple> e;
    info = Eigen::eig(n, &J[0][0], e);
    
    // Check for stop criteria
    // TODO This section is to be tuned.
    if (info == SUCCESSFUL_PROCEDURE){
        // The eigenvalue of this family must be real
        //for (i = 0; i < n; i++){
            if (fabs(e[family].i) > 1e-10){ // TODO Set a good value for this
                #ifdef TEST_RAREFACTION
                    printf("At rarefaction(): Imaginary eigenvalue!\n");
                    printf("J = [");
                    for (int j = 0; j < n; j++){
                        if (j != 0) printf("     ");
                        for (int k = 0; k < n; k++){
                            printf(" %6.2f ", J[j][k]);
                        }
                        printf(";\n");
                    }
                    printf("     ]\n");
                    printf("Eigenvalue %d = % g %+g*i.\n", i, e[i].r, e[i].i);
                #endif
                return COMPLEX_EIGENVALUE;
            }
        //}
        
        // All eigenvalues must be different.
        // This test can be performed thus because the eigencouples are ordered
        // according to the real part of the eigenvalue. Thus, if two eigenvalues
        // are equal, one will come after the other.
        for (i = 0; i < n - 1; i++){
            if (e[i].r == e[i + 1].r){
                #ifdef TEST_RAREFACTION
                    printf("At rarefaction(): Eigenvalues are equal!\n");
                    printf("Eigenvalue %d = % f %+f*i.\n", i, e[i].r, e[i].i);
                    printf("Eigenvalue %d = % f %+f*i.\n", i + 1, e[i + 1].r, e[i + 1].i);
                #endif
                return ABORTED_PROCEDURE;
            }
        }
        
    }
    else return ABORTED_PROCEDURE;
    
    // The eigenvector to be returned is the one whose inner product with the
    // reference vector is positive.
    if (prodint(n, &(e[family].vrr[0]), &rev[0]) > 0){
        for (i = 0; i < n; i++) out[i] = e[family].vrr[i];
    }
    else {
        for (i = 0; i < n; i++) out[i] = -e[family].vrr[i];
    }
    
    return SUCCESSFUL_PROCEDURE;
}

// * * * * * * D U M M Y  F U N C T I O N * * * * * * //
// The Jacobian of the rarefaction.
// So far this is a dummy function that is needed by the solver.
//
// The parameters are:
//      neq: Number of equations
//        t: Current time
//        y: Current point
//       ml: If the Jacobian is banded: Number of diagonals below the main diagonal
//           that are relevant
//       mu: If the Jacobian is banded: Number of diagonals above the main diagonal
//           that are relevant
//       pd: Output
//     nrpd: Number of rows of pd
//
// Returns:  SUCCESSFUL_PROCEDURE: OK
//           ABORTED_PROCEDURE: Something's wrong
int jacrarefaction(int *neq, double *t, double *y, int *ml, int *mu, double *pd, int *nrpd, int *nparam,  double *param){
    return SUCCESSFUL_PROCEDURE;
}

// To compute the flux function:
// Given a point (in) the family-th eigencouple of its Jacobian
// will be found (lambda, out).
//
// This function returns:
//    SUCESSFUL_PROCEDURE: Everything's OK
//    COMPLEX_EIGENVALUE:  If the eigenvalue is complex (results should be discarded).
//
int flux(int n, int family, FluxFunction *ff, double *in, double *lambda, double *out){
    // Fill the Jacobian
    double J[n][n];
    // DF(n, in, &J[0][0]); // replaced by the lines below
    fill_with_jet(ff, n, in, 1, 0, &J[0][0], 0);

    // Find J's eigencouples and sort them.
    vector<eigencouple> e;
    int info = Eigen::eig(n, &J[0][0], e);

    //printf("flux: family = %d, ev = %f\n", family, e[family].r);
    
    if (fabs(e[family].i) > 1e-5){
        printf("Complex eigenvalue\n");
        return COMPLEX_EIGENVALUE;
    }
    else {
        *lambda = e[family].r;
        if (out != 0) for (int i = 0; i < n; i++) out[i] = e[family].vrr[i];
        return SUCCESSFUL_PROCEDURE;
    }
}

// Initialize the rarefaction curve.

// To initialize the reference eigenvector (from now on, "rev"):
//
// 1. Find the eigenvalue and the eigenvector at in (the initial point):
//        lambda_in     = eigenvalue(in),
//        rev_in        = eigenvector(in).
// 2. Find the following points and the eigenvectors:
//        in_minus = in - epsilon*eigenvector(in),
//        in_plus  = in + epsilon*eigenvector(in),
//        re_minus = eigenvector(in_minus),
//        re_plus  = eigenvector(in_plus).
// 3. Find the eigenvalues at in, in_minus and in_plus:
//        lambda_minus = eigenvalue(in_minus),
//        lambda_plus  = eigenvalue(in_plus).
//    (It is assumed that lambda_minus != lambda_plus, and both != lambda_in.)
// 4. The user chooses if the eigenvalue should increase or decrease as the orbit
//    is computed. Thus, if lambda is to decrease:
//        IF      lambda_in > lambda_minus THEN rev = re_minus
//        ELSE IF lambda_in > lambda_plus  THEN rev = re_plus
//    If lambda is to increase:
//        IF      lambda_in < lambda_minus THEN rev = re_minus
//        ELSE IF lambda_in < lambda_plus  THEN rev = re_plus

int rarinit(int n, double *in, int indx, int increase, double deltaxi, double *lambda, double *rev){
    // 1. Find the eigenvalue and the eigenvector at in (the initial point):
    printf("&rev = %p; (rev == 0 = %d)\n", rev, rev == 0);
    if (flux(n, indx, flux_object, in, lambda, rev) == COMPLEX_EIGENVALUE) return COMPLEX_EIGENVALUE;
    
    // 2. and 3. Find the eigencouples at in_plus and in_minus.
    double epsilon = 10*deltaxi;
    double lambdap, lambdam; // Lambda_plus, lambda_minus
    double inp[n], inm[n];   // In_plus and in_minus
    double rep[n], rem[n];   // Eigenvectors at in_plus and in_minus
    int ii;
    for (ii = 0; ii < n; ii++){
        inp[ii] = in[ii] + epsilon*rev[ii]; 
        inm[ii] = in[ii] - epsilon*rev[ii];
    }
    if (flux(n, indx, flux_object, inp, &lambdap, &rep[0]) == COMPLEX_EIGENVALUE) return COMPLEX_EIGENVALUE;
    if (flux(n, indx, flux_object, inm, &lambdam, &rem[0]) == COMPLEX_EIGENVALUE) return COMPLEX_EIGENVALUE;

    printf("@ rarefactioncurve(), after init.\nl- = % f, l = % f, l+ = % f\n", lambdam, *lambda, lambdap);
    printf("e = (");
    for (int i = 0; i < n; i++) printf("%6.2f, ", rev[i]);
    printf(")\n");
    
    // 4. Find the reference eigenvector.
    if (increase == 1){ // Eigenvalues should increase as the orbit advances
        if (*lambda <= lambdap && *lambda <= lambdam){
            #ifdef TEST_RAREFACTION
            printf("Inside rarefactioncurve(): Cannot initialize, lambda doesn't increase!\n");
            #endif
            return LAMBDA_NOT_INCREASING;
        }
        else if (*lambda < lambdap && *lambda > lambdam){
            // Nothing to do, the eigenvector is rev.
        }
        else if (*lambda > lambdap && *lambda < lambdam){
            for (ii = 0; ii < n; ii++) rev[ii] = -rev[ii];
        }
        else {
            #ifdef TEST_RAREFACTION
            printf("Inside rarefactioncurve(): Cannot initialize, unexpected!\n");
            #endif
            return LAMBDA_NOT_INCREASING;
        }
    }
    else {              // Eigenvalues should decrease as the orbit advances
        if (*lambda >= lambdap && *lambda >= lambdam){
            #ifdef TEST_RAREFACTION
            printf("Inside rarefactioncurve(): Cannot initialize, lambda doesn't decrease!\n");
            #endif
            return LAMBDA_NOT_DECREASING;
        }
        else if (*lambda > lambdap && *lambda < lambdam){
            // Nothing to do, the eigenvector is rev.
        }
        else if (*lambda < lambdap && *lambda > lambdam){
            for (ii = 0; ii < n; ii++) rev[ii] = -rev[ii];
        }
        else {
            #ifdef TEST_RAREFACTION
            printf("Inside rarefactioncurve(): Cannot initialize, unexpected!\n");
            #endif
            return LAMBDA_NOT_DECREASING;
        }
    }

    return SUCCESSFUL_PROCEDURE;
}

// Adapted from the FORTRAN original (at eigen.F, code provided by Dan).
//
// This function computes the directional derivatives of the
// Jacobian.
//
//          n: Dimension of the space.
//          p: Point where the directional derivative is computed.
//     family: The family.
//         ff: The flux.
//
// The function returns the directional derivative at p, for 
// a given flux: ff. 
//
// Let l and r be the left- and right-eigenvectors of the Jacobian
// of the flux at point p. Let H be the Hessian of said flux.
// Then the directional derivative is:
//
//     dirdrv = prodint(l, H*r*r)/prodint(l, r).
//
// In particular, D = H*r is computed thus:
//
//     D[i] = H[i]*r,     i = 0,..., (n - 1),
//
// where D[i] is the i-th row of n*n matrix D and H[i] is the
// i-th "matrix" of the Hessian.
//
double dirdrv(int n, double *p, int family, FluxFunction *ff){
    double J[n][n];
    double H[n][n][n];
    fill_with_jet(ff, n, p, 2, 0, &J[0][0], &H[0][0][0]);

    // Extract the left and right eigenvalues of J
    vector<eigencouple> e;
    int info = Eigen::eig(n, &J[0][0], e);

    double l[n], r[n];
    
    for (int i = 0; i < n; i++){
        l[i] = e[family].vlr[i];
        r[i] = e[family].vrr[i];
    }

    // Compute D
    double D[n][n];
    for (int i = 0; i < n; i++){
        double SubH[n][n];
        for (int j = 0; j < n; j++){
            for (int k = 0; k < n; k++){
                SubH[j][k] = H[i][j][k];
            }
        }
        
        double row[n];
        matrixmult(n, n, 1, &SubH[0][0], &r[0], &row[0]);

        for (int j = 0; j < n; j++) D[i][j] = row[j];
    }

    // Compute D*r
    double Dtimesr[n];
    matrixmult(n, n, 1, &D[0][0], &r[0], &Dtimesr[0]);

    // Result
    return prodint(n, Dtimesr, l)/prodint(n, r, l);
}

// Function rar_last_point:
//
// Given two points p0 and p1, this function computes d0 and d1,
// the directional derivatives at said points for a given family.
// 
// If sign(d0) == -sign(d1) then the function proceeds to compute the point
// where the directional derivative is zero: that is, the point
// where an extremum is reached.
// 
// The resulting point is stored in out.
//
// The function returns:
//     -1: d0 and d1 share the same sign, thus the extremum was not found (and out contains garbage).
//      0: Successfuly found the point, out is usable.
//
int rar_last_point(int n, double *p0, double *p1, int family, FluxFunction *ff, double *out){
    double d0 = dirdrv(n, p0, family, ff);
    double d1 = dirdrv(n, p1, family, ff);

    if (d1*d0 > 0) return ABORTED_PROCEDURE;

    double alpha = d1/(d1 - d0);

    for (int i = 0; i < n; i++) out[i] = alpha*p0[i] + (1 - alpha)*p1[i];

    return SUCCESSFUL_PROCEDURE;
}

// Normalize a vector
//
void normalize(int n, double *v){
    double norm = 0;
    for (int i = 0; i < n; i++) norm += v[i]*v[i];
    norm = sqrt(norm);
    for (int i = 0; i < n; i++) v[i] /= norm;

    return;
}

//                              FUNCTION rarefactioncurve
//
// This function computes the rarefaction curve.
// The list of parameters:
//
//    *numtotal (OUTPUT): The number of points computed before a stop criterion was met.
//     *xifinal (OUTPUT): The value of xi when the computation of the orbit stops.
//         *out (OUTPUT): A pointer of type struct store (see alloc.c, alloc.h), 
//                        to store the results.
//
//                        The user will reserve enough space in out for storing the
//                        results.
//
//                        By columns, the array contains the coordinates of the points
//                        being computed and any other data deemed of interest. 
//                        As the bare minimum, the number of rows of out must be n, since
//                        every point computed must be stored, and the dimension of the
//                        space is n. However, the speed (a scalar), an eigenvector, etc., 
//                        could also be recorded. The user will modify the code accordingly.
//                        Comments of the form
//
//                        /************* DATA STORAGE BEGINS HERE *************/
//
//                        and
//
//                        /*************  DATA STORAGE ENDS HERE  *************/
//
//                        will mark the places where storing of data occurs,
//                        so the user will find such places easily.
//             n (INPUT): The dimension of the problem.
//           *in (INPUT): An array of n elements containing the initial point, i.e., U-.
//        nummax (INPUT): The maximum number of points that will be computed.
//          indx (INPUT): The "index" of the family. To comply with the C/C++ standard,
//                        indx can be as small as 0 and as great as (n - 1). 
//                        If indx lies outside that range, the function aborts.
//           xi0 (INPUT): The initial value of xi (the independent variable).
//       deltaxi (INPUT): The step for xi (the independent variable). 
//      increase (INPUT): 1 if the speed will increase as the orbit advances, -1 contrariwise. 
//                domain: Domain where the curve lives.
//       the_flux_object: A pointer to a FluxFunction-derived object. This object will
//                        perform all the jet-related operations.
//               ref_vec: An array where the last reference vector will be stored,
//                        to be later used by the composite curve.
//
// The function returns an integer, whose value is:
//
//     SUCCESSFUL_PROCEDURE: The computation was completed correctly,
//        ABORTED_PROCEDURE: An error occurred.
//
int rarefactioncurve(int *numtotal, double *xifinal, struct store *out, int n, 
                     double *in, int nummax, int indx, double xi0, double deltaxi, 
                     int increase,
                     Domain *domain,
                     FluxFunction *the_flux_object,
                     double *ref_vec){

    printf("Begining of rarefaction\n");

    // The flux object
    flux_object = the_flux_object;

    /********************* FOR THE SOLVER *********************/ 
    // The dimension of the space
    int neq = n;
    
    // ???
    int ml;
    int mu;
    
    // ???
    int nrpd = 4;
    
    // Initial and final times
    double t = xi0;
    double tout = t + deltaxi;
    
    // Is the tolerance the same for all the elements of U (1) or not (2)?
    int itol = 2; // 1: atol scalar; 2: atol array.
    double rtol = 1e-4;
    double atol[neq]; for (int i = 0; i < neq; i++) atol[i] = 1e-6;
    
    // The Jacobian is provided by the user.
    // int mf = 21; 
    // The Jacobian is NOT provided by the user.
    int mf = 22; 
    
    // Lsode uses rwork to perform its computations.
    // lrw is the declared length of rwork
    int lrw;
    if (mf == 10)                  lrw = 20 + 16*neq;
    else if (mf == 21 || mf == 22) lrw = 22 + 9*neq + neq*neq;
    else if (mf == 24 || mf == 25) lrw = 22 + 10*neq + (2*ml + mu)*neq;
    double rwork[lrw];

    // Normal computation of values at tout.
    int itask = 1; 
    
    // Set to 1 initially.
    int istate = 1;
    
    // No optional inputs
    int iopt = 0;

    // Lsode uses iwork to perform its computations.
    // liw is the declared length of iwork
    int liw;
    if (mf == 10) liw = 20;
    else if (mf == 21 || mf == 22 || mf == 24 || mf == 25) liw = 20 + neq;
    int iwork[liw];        
    /********************* FOR THE SOLVER *********************/
    
    // Lsode returns an integer (that serves as a flag), to tell if the computation
    // process was successful or not. Refer to clsode.c for details.
    int info = SUCCESSFUL_PROCEDURE;

    // Initialize the reference vector
    double lambda, rev[n];  // Lambda, eigenvectorreference 
    int info_rarinit = rarinit(n, in, indx, increase, deltaxi, &lambda, &rev[0]);
    if (info_rarinit != SUCCESSFUL_PROCEDURE) return info_rarinit;

    // Two points, for current and previous calculations
    double U[n], Uprev[n];
    for (int i = 0; i < n; i++) U[i] = in[i]; // Initialized here
    
    // Two speeds, etc.
    double speed, prev_speed;

    // One storage point:
    //     Ustore[0:n - 1] = point,
    //     Ustore[n]       = speed.
    double Ustore[n + 1];

    // Store the first point and the speed thereat, and update the counter
    for (int i = 0; i < n; i++) Ustore[i] = in[i];
    flux(n, indx, flux_object, in, &(Ustore[n]), 0);
    add(out, Ustore);
    (*numtotal) = 1;
   
    // Update the speed:
    speed = Ustore[n];

    // The parameters are set here (thus avoiding global variables):
    // param[0] = family,
    // param[1:n - 1] = reference eigenvector.
    int nparam = 1 + n; double param[nparam];
    
    param[0] = (double)indx; // Family
    for (int i = 0; i < n; i++) param[1 + i] = rev[i]; // Reference eigenvector

    // Vectors that will be used when checking if a point belongs or not
    // to the domain where the integration takes place.
    vector<double> p, q, r;
    p.resize(n);
    q.resize(n);
    r.resize(n);

    // Computation of the curve proper
    while (info == SUCCESSFUL_PROCEDURE && *numtotal < nummax){
        // Update the previous point & speed
        for (int i = 0; i < n; i++) Uprev[i] = U[i];
        prev_speed = speed;

        // Invoke the solver
        info = solver(&rarefaction, &neq, &U[0], &t, &tout, &itol, &rtol, &atol[0], 
                      &itask, &istate, &iopt, &rwork[0], &lrw, &iwork[0], &liw, 
                      0, &mf, &nparam, &param[0]);

        // Stop criteria
        if (info == SUCCESSFUL_PROCEDURE){
            // Find the speed (the eigenvalue should be real):
            int info_speed = flux(n, indx, flux_object, U, &speed, 0);
            if (info_speed != SUCCESSFUL_PROCEDURE) {
                printf("Complex eigenvalue\n");
                return info_speed;
            }

            // Speed should be monotonic. If not, the last point in the curve
            // is the one where the directional derivative is zero.
            //
            if ((speed >= prev_speed && increase == -1) ||
                (speed <= prev_speed && increase ==  1)){
                //
                double last_point[n];
                int rar_last_point_info = rar_last_point(n, Uprev, U, indx, flux_object, last_point);
                if (rar_last_point_info == 0){
                    for (int i = 0; i < n; i++) Ustore[i] = last_point[i];
                    flux(n, indx, flux_object, last_point, &(Ustore[n]), 0);

                    // Store, update the counter and the time
                    add(out, Ustore);
                    (*numtotal)++;
                    tout = t + deltaxi;
                    
                    // Reference vector for the next curve (a composite):
                    for (int i = 0; i < n; i++) ref_vec[i] = last_point[i] - Uprev[i];
                    normalize(n, ref_vec);

                    
                }
                else {} // TODO: What happens here???
                printf("Speed not monotonic. Speed = %f, prev_speed = %f\n", speed, prev_speed);
                return ABORTED_PROCEDURE;
            }

            // The new point must belong to the domain. If not, find the last point
            // that does and the speed thereat.
            //
            for (int i = 0; i < n; i++){
                p[i] = Uprev[i];
                q[i] = U[i];
            }

            // Check if the segment formed by the last two points is inside/outside/across 
            // the domain. If inside, carry on. If outside, abort. If across, find
            // the intersection point, add it to the curve and abort.                
            int cllsn = domain->intersection(p, q, r);
            if (cllsn == -1){
                // Both outside
                printf("Domain: Both outside\n");
                return ABORTED_PROCEDURE;
            }
            else if (cllsn == 1){
                // Both inside
                for (int i = 0; i < n; i++) Ustore[i] = U[i];
                Ustore[n] = speed;
            }
            else if (cllsn == 0){
                // One inside, one outside: store and get out
                double ppnt[n + 1];
                for (int i = 0; i < n; i++) ppnt[i] = r[i];
                flux(n, indx, flux_object, ppnt, &(ppnt[n]), 0);

                // Store, update the counter and the time
                add(out, ppnt);
                (*numtotal)++;
                tout = t + deltaxi;

                // Reference vector for the next curve (a composite):
                for (int i = 0; i < n; i++) ref_vec[i] = ppnt[i] - Uprev[i];
                normalize(n, ref_vec);

                printf("Domain: One inside, one outside. Speed = %f, prev_speed = %f\n", ppnt[n], prev_speed);
                return ABORTED_PROCEDURE;
            }

            // Store, update the counter and the time
            add(out, Ustore);
            (*numtotal)++;
            tout = t + deltaxi;

            // Update the param vector for the next iteration:
            for (int i = 0; i < n; i++) param[1 + i] = U[i] - Uprev[i];

            // Reference vector for the next curve (a composite): // TODO: Should be out of the if{}?
            for (int i = 0; i < n; i++) ref_vec[i] = U[i] - Uprev[i];
            normalize(n, ref_vec);
        }
    }

    printf("End of rarefaction\n");
    return info;
}

