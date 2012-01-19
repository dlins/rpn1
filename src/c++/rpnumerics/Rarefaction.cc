#include "Rarefaction.h"

FluxFunction         *Rarefaction::fluxfunction;
AccumulationFunction *Rarefaction::accumulationfunction;
int                   Rarefaction::type;
int                   Rarefaction::family;

double Rarefaction::ddot(int n, double *x, double *y){
    double p = 0.0;

    for (int i = 0; i < n; i++) p += x[i]*y[i];

    return p;
}

// C = A*B
// A = m times p
// B = p times n
// C = m times n
void Rarefaction::matrixmult(int m, int p, int n, double *A, double *B, double *C){
    double sum;

    for (int i = 0; i < m; i++){
        for (int j = 0; j < n; j++){
            sum = 0.0;
            for (int k = 0; k < p; k++) sum += A[i*p + k]*B[k*n + j];
            C[i*n + j] = sum; 
        }
    }
     
    return;
}


void Rarefaction::fill_with_jet(const RpFunction *flux_object, int n, double *in, int degree, double *F, double *J, double *H) {
    RealVector r(n);
    double *rp = r;
    for (int i = 0; i < n; i++) rp[i] = in[i];

    // Will this work? There is a const somewhere in fluxParams.
    //FluxParams fp(r);
    //flux_object->fluxParams(FluxParams(r)); // flux_object->fluxParams(fp);

    WaveState state_c(r);
    JetMatrix c_jet(n);

    flux_object->jet(state_c, c_jet, degree);

    // Fill F
    if (F != 0) for (int i = 0; i < n; i++) F[i] = c_jet(i);

    // Fill J
    if (J != 0) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                J[i * n + j] = c_jet(i, j);
            }
        }
    }

    // Fill H
    if (H != 0) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    //H[(i * n + j) * n + k] = c_jet(i, j, k); // Check this!!!!!!!!
                    H[i*n + j + n*n*k] = c_jet(k, i, j); // This works for the convention adopted in the FluxFunction::jet().
                }
            }
        }
    }

    return;
}

int Rarefaction::flux(int *neq, double *xi, double *in, double *out, int *nparam, double *param){
    // The dimension of the problem:
    int n = *neq;
    
    // The family:
//    int family = (int)param[0];
    
    // The reference eigenvector:
    double rev[n];
    for (int i = 0; i < n; i++) rev[i] = param[1 + i];
    
    int info;
    std::vector<eigenpair> e;

    // Fill the Jacobian
    double FJ[n][n];
    double FH[n][n][n];
    
    fill_with_jet((RpFunction*)Rarefaction::fluxfunction, n, in, 2, 0, &FJ[0][0], &FH[0][0][0]);

    if (type == RAREFACTION_SIMPLE_ACCUMULATION)
        info = Eigen::eig(n, &FJ[0][0], e);
    else {
        double GJ[n][n];
        double GH[n][n][n];

        fill_with_jet((RpFunction*)Rarefaction::accumulationfunction, n, in, 2, 0, &GJ[0][0], &GH[0][0][0]);
        info = Eigen::eig(n, &FJ[0][0], &GJ[0][0], e);
    }

    for (int i = 0; i < n; i++) out[i] = e[Rarefaction::family].vrr[i];
    
    // Check for stop criteria
    // TODO This section is to be tuned.
    if (info == 0) {
        // All eigenvalues must be real
        for (int i = 0; i < e.size(); i++) {
            if (fabs(e[i].i) > 0) {
                return COMPLEX_EIGENVALUE;
            }
        }
    
        // All eigenvalues must be different.
        // This test can be performed thus because the eigencouples are ordered
        // according to the real part of the eigenvalue. Thus, if two eigenvalues
        // are equal, one will come after the other.
        for (int i = 0; i < e.size() - 1; i++) {
            if (e[i].r == e[i + 1].r) {
                return ABORTED_PROCEDURE;
            }
        }
    
    } 
    else {
        return ABORTED_PROCEDURE;
    }
    
    // The eigenvector to be returned is the one whose inner product with the
    // reference vector is positive.
    if (ddot(n, &(e[Rarefaction::family].vrr[0]), &rev[0]) > 0) {
        for (int i = 0; i < n; i++) out[i] = e[Rarefaction::family].vrr[i];
    } 
    else {
        for (int i = 0; i < n; i++) out[i] = -e[Rarefaction::family].vrr[i];
    }
    
//        // STOP CRITERION:
//        // The identity in Proposition 10.11 of
//        // "An Introduction to Conservation Laws:
//        // Theory and Applications to Multi-Phase Flow" must not change
//        // sign (that is, the rarefaction is monotonous).
//    
//        double res[n];
//    
//        applyH(n, &(e[family].vrr[0]), &H[0][0][0], &(e[family].vrr[0]), &res[0]);
//        double dlambda_dtk = ddot(n, &res[0], &(e[family].vlr[0])) /
//                ddot(n, &(e[family].vlr[0]), &(e[family].vrr[0]));
//        if (dlambda_dtk * ref_speed < 0) {
//            return ABORTED_PROCEDURE;
//        }
    return SUCCESSFUL_PROCEDURE;
}

// Compute the last point of the rarefaction curve when the monotonicity of the eigenvalues
// is violated. This function is totally wrong now.
//
//     previous_point, and new_point
//
// must be of size n + 1. The last component is lambda.
//
int Rarefaction::compute_last_point(const RealVector &previous_point, const RealVector &new_point, RealVector &last_point){
    int n = previous_point.size() - 1;

    //last_point.resize(n + 1);

    int info = rar_last_point(n, previous_point, new_point, last_point);
    printf("Inside compute_last_point:\n");

    printf("previous_point = (");
    for (int i = 0; i < n; i++){
        printf("%g", previous_point.component(i));
        if (i <= (n - 2)) printf(", ");
    }
    printf(")\n");

    printf("new_point = (");
    for (int i = 0; i < n; i++){
        printf("%g", new_point.component(i));
        if (i <= (n - 2)) printf(", ");
    }
    printf(")\n");

    printf("last_point = (");
    if (info == SUCCESSFUL_PROCEDURE){
        for (int i = 0; i < n; i++){
            printf("%g", last_point.component(i));
            if (i <= (n - 2)) printf(", ");
        }
        printf(")\n");
    }
    else printf("GARBAGE HERE)\n");

    return info;
}

// Compute the value of the eigenvalue and right eigenvector at a given point for a given family.
// Normally I would use n = in.size(), but this way I can feed this function with a
// vector that contains > n components and even so use its first n components.
//
void Rarefaction::compute_eigenpair(int n, const RealVector &in, double &lambda, RealVector &eigenvector){
    double p[n];
    for (int i = 0; i < n; i++) p[i] = in.component(i);

    std::vector<eigenpair> e;

    if (type == RAREFACTION_SIMPLE_ACCUMULATION){
        double FJ[n][n];
        fill_with_jet((RpFunction*)Rarefaction::fluxfunction,         n, p, 1, 0, &FJ[0][0], 0);
        Eigen::eig(n, &FJ[0][0], e);
    }
    else {
        double FJ[n][n], FG[n][n];
        fill_with_jet((RpFunction*)Rarefaction::fluxfunction,         n, p, 1, 0, &FJ[0][0], 0);
        fill_with_jet((RpFunction*)Rarefaction::accumulationfunction, n, p, 1, 0, &FG[0][0], 0);
        Eigen::eig(n, &FJ[0][0], &FG[0][0], e);
    } 

    lambda = e[Rarefaction::family].r;

    eigenvector.resize(e[Rarefaction::family].vrr.size());
    for (int i = 0; i < e[Rarefaction::family].vrr.size(); i++) eigenvector.component(i) = e[Rarefaction::family].vrr[i];

    return;
}

// Compute the value of lambda at a given point, a mere wrapper for compute_eigenpair().
// Normally I would use n = in.size(), but this way I can feed this function with a
// vector that contains > n components and even so use its first n components.
//
double Rarefaction::compute_lambda(int n, const RealVector &in){
    double lambda;

    RealVector eigenvector; // Discarded on exit.

    compute_eigenpair(n, in, lambda, eigenvector);

    return lambda;
}

// Initialize the rarefaction, or, find the second point in the curve.
// The eigenvalue at said point will be stored in the last component of second_point.
//
int Rarefaction::init(const RealVector &initial_point, int increase, double deltaxi, RealVector &second_point){
    int n = initial_point.size();

    // Eigenvalue and right eigenvector at the initial point.
    double lambda;
    RealVector ev;

    compute_eigenpair(n, initial_point, lambda, ev);

    // Eigenvalues at the candidate points (initial_point +/- deltaxi*ev).
    // Prefixes: m = -, p = +.
    RealVector m(n), p(n);
    for (int i = 0; i < n; i++){
        m.component(i) = initial_point.component(i) - deltaxi*ev.component(i);
        p.component(i) = initial_point.component(i) + deltaxi*ev.component(i);
    }

    double mlambda = compute_lambda(n, m);
    double plambda = compute_lambda(n, p);

    // Fill the second point of the curve and the eigenvalue thereat.
    second_point.resize(n + 1);

    if (increase ==  RAREFACTION_SPEED_INCREASE ) {
        if      (mlambda < lambda && lambda < plambda){
            for (int i = 0; i < n; i++) second_point.component(i) = p.component(i);
            second_point.component(n) = plambda;
            cout<<"Second point: "<<second_point<<endl;
        }
        else if (mlambda > lambda && lambda > plambda){
            for (int i = 0; i < n; i++) second_point.component(i) = m.component(i);
            second_point.component(n) = mlambda;
            cout << "Second point: " << second_point << endl;
        }
        else return RAREFACTION_INIT_FAILURE;
    }
    else if (increase ==  RAREFACTION_SPEED_DECREASE ){
        if      (mlambda < lambda && lambda < plambda){
            for (int i = 0; i < n; i++) second_point.component(i) = m.component(i);
            second_point.component(n) = mlambda;
        }
        else if (mlambda > lambda && lambda > plambda){
            for (int i = 0; i < n; i++) second_point.component(i) = p.component(i);
            second_point.component(n) = plambda;
        }
        else return RAREFACTION_INIT_FAILURE;
    }

    return RAREFACTION_INIT_OK;
}

// Adapted from the FORTRAN original (at eigen.F, code provided by Dan).
//
// Adapted to the GENERALIZED case by Helmut.
//
// This function computes the directional derivatives for
// generalized problem.
//
//          n: Dimension of the space.
//          p: Point where the directional derivative is computed.
//
// The function returns the directional derivative at p, for
// a given flux and accumulation.
//
// Let l and r be the left- and right-eigenvectors of the GENERALIZED
// SYSTEM OF CONSERVATION LAWS. (notice the difference between this and
// the Stone case), at point p. Let lambda be the associated eigenvalue
// (All of them corresponding to the same family).
//
// Let B the Jacobian of the (NON-TRIVIAL) accumulation.
// Let A the Jacobian of the (NON-TRIVIAL) flux.
//
// Let H be the Hessian of said flux, and M the Hessian of the accumulation.
// Then the (GENERALIZED) directional derivative is (see Chapter
// Numerical Methods thesis Helmut):
//
//     dirdrv = ddot( l, H(r,r) - lambda M(r,r) )/ddot(l, Br).
//
// In particular, D = r^T*H is computed thus:
//
//     D[k] = r^T*H[k],     k = 0,..., (n - 1),
//
// where D[k] is the k-th row of n*n matrix D and H[k] is the
// k-th "matrix" of the Hessian.  (Where there are n Hessians.)
// i.e. it is the Hessian of the k-th component function.
// r^T denotes the transpose of r.
//
// In particular, E = r^T*M is computed thus:
//
//     E[k] = r^T*M[k],     k = 0,..., (n - 1),
//
// where E[k] is the k-th row of n*n matrix E and M[k] is the
// i-th "matrix" of the Hessian.  (Where there are n Hessians.)
//

double Rarefaction::dirdrv(int n, const RealVector &p){
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

    for (int i = 0; i < n; i++){
        l[i] = e[family].vlr[i];
        r[i] = e[family].vrr[i];
    }

    // Extract lambda.
    // The i-th eigenvalue must be real. 
    // The eigenvalues must be chosen carefully in the n-dimensional case.
    // ALL eigenvalues must be real. Extend this by using a for cycle.
    //
    double lambda;

    if (e[family].i != 0){
        printf("Inside dirdrv(): Init step, eigenvalue %d is complex: % f %+f.\n", family, e[family].r, e[family].i);
        return ABORTED_PROCEDURE;     
    }
    else lambda = e[family].r;

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

       matrixmult(1, n, n, &r[0], &SubH[0][0], &rowh[n]);
       matrixmult(1, n, n, &r[0], &SubM[0][0], &rowm[n]);

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

    // Compute B*r
    double Btimesr[n];
    matrixmult(n, n, 1, &B[0][0], &r[0], &Btimesr[0]);

    // Result
    double Dtimesr_minus_lambdaEtimesr[n];
    for (int i = 0; i < n; i++) Dtimesr_minus_lambdaEtimesr[i] = Dtimesr[i] - lambda*Etimesr[i];

    //for (int i = 0; i < n; i++) printf("l[%d] = %g, r[%d] = %g, Dtimesr[%d] = %g, lambda = %g, Etimesr[%d] = %g\n", i, l[i], i, r[i], i, Dtimesr[i], lambda, i, Etimesr[i]);

    printf("Dirdrv = %g divided by %g.\n", ddot(n, Dtimesr_minus_lambdaEtimesr,l), ddot(n, Btimesr, l));

    return ddot(n, Dtimesr_minus_lambdaEtimesr, l)/ddot(n, Btimesr, l);
}


// Function rar_last_point:
//
// Given two points (and the eigenvalues) p0 and p1, this function computes d0 and d1,
// the directional derivatives at said points for a given family.
// 
// If sign(d0) == -sign(d1) then the function proceeds to compute the point
// where the directional derivative is zero: that is, the point
// where an extremum is reached.
// 
// The resulting point, along with its eigenvalue, is stored in out.
//
// The function returns:
//     -1: d0 and d1 share the same sign, thus the extremum was not found (and out contains garbage).
//      0: Successfuly found the point, out is usable.
//
int Rarefaction::rar_last_point(int n, const RealVector &p0, const RealVector &p1, RealVector &out){
    double d0 = dirdrv(n, p0);
    double d1 = dirdrv(n, p1);

    if (d1*d0 >= 0.0) return ABORTED_PROCEDURE;

    double alpha = d1/(d1 - d0); printf("Inside rar_last_point(): alpha = %g\n", alpha);

    out.resize(n + 1);

    // This way it is possible to compute the eigenvalue for the last point of the curve.
    // If the eigenvalue is computed using compute_lambda, a segfault occurs, because
    // the Jacobian is singular.
    //
    for (int i = 0; i <= n; i++) out.component(i) = alpha*p0.component(i) + (1.0 - alpha)*p1.component(i);

    return SUCCESSFUL_PROCEDURE;
}

int Rarefaction::curve(const RealVector &initial_point, 
                       int initialize,
                       const RealVector *initial_direction,
                       int curve_family, 
                       int increase,
        int check_monotony,
                       double deltaxi,
                       const FluxFunction *ff, const AccumulationFunction *aa,
                       int type_of_accumulation,
                       Boundary *boundary,
                       std::vector<RealVector> &rarcurve){

    // Set the static parameters that will be used throughout.
    // TODO: Decide if increase and deltaxi should be static.
    Rarefaction::fluxfunction         = (FluxFunction*)ff;
    Rarefaction::accumulationfunction = (AccumulationFunction*)aa;
    Rarefaction::type                 = type_of_accumulation;
    Rarefaction::family               = curve_family;

    // Space dimension.
    int n = initial_point.size();

    // These vectors will be used everywhere by the Boundary, and to check the monotonicity of the speed.
    RealVector previous_point(n + 1), new_point(n + 1);

    // Same as previous_point.component(n) and new_point.component(n),
    // but it adds to the legibility of the code.
    double previous_lambda, new_lambda;

    // Clean the output...
    rarcurve.clear();

    // ...and store the initial point
    for (int i = 0; i < n; i++) new_point.component(i) = initial_point.component(i); 
    new_point.component(n) = compute_lambda(n, initial_point);
    rarcurve.push_back(new_point);

    // Initialize the rarefaction and store the second point (lambda is added by init()).
    if (initialize == RAREFACTION_INITIALIZE_YES){
        int init_info = init(initial_point, increase, deltaxi, new_point);
        if (init_info != RAREFACTION_INIT_OK) return init_info;
    }
    else {
        RealVector tempev(n);
        double templambda;
        compute_eigenpair(n, new_point, templambda, tempev);

        double d = 0;
        for (int i = 0; i < n; i++) d += tempev.component(i)*initial_direction->component(i);

        printf("d = %f\n", d);
        printf("Eigenvector = (");
        for (int i = 0; i < n; i++){
            printf("%g", tempev.component(i));
            if (i < n - 1) printf(", ");
        }
        printf(")\n");

        if (d >= 0.0) for (int i = 0; i < n; i++) new_point.component(i) += deltaxi*tempev.component(i);
        else          for (int i = 0; i < n; i++) new_point.component(i) -= deltaxi*tempev.component(i);

        new_point.component(n) = compute_lambda(n, new_point);

        printf("New point   = (");
        for (int i = 0; i < n; i++){
            printf("%g", new_point.component(i));
            if (i < n - 1) printf(", ");
        }
        printf(")\n");
    }

    rarcurve.push_back(new_point);
    new_lambda = new_point.component(n);

    // BEGIN Prepare the parameters to be passed to LSODE //
    int ml; // Not used.
    int mu; // Not used.
        
    // ???
    int nrpd = 4;
        
    // Is the tolerance the same for all the elements of U (1) or not (2)?
    int itol = 2; // 1: atol scalar; 2: atol array.
    double rtol = 1e-5;
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

    // The point LSODE uses.
    double p[n];
    for (int i = 0; i < n; i++) p[i] = new_point.component(i);

    // Independent parameter. Not used by flux(), but needed by LSODE.
    double xi = 0.0, new_xi = deltaxi;

    // Reference vector (passed as param).
    int nparam = n + 1;
    double param[nparam];
    param[0] = (int)family;
    for (int i = 0; i < n; i++) param[1 + i] = new_point.component(i) - initial_point.component(i);

    //int info = SUCCESSFUL_PROCEDURE;

    // Compute the curve.
    while (true){

//        cout<<"Dentro do while"<<endl;
        // Update the previous point & lambda
        for (int i = 0; i < n; i++) previous_point.component(i) = new_point.component(i);
        previous_point.component(n) = previous_lambda = new_point.component(n);

        // Invoke LSODE.
        lsode_(&flux, &n, p, &xi, &new_xi, &itol, &rtol, atol, &itask, &istate, &iopt, rwork, &lrw, iwork, &liw, 0, &mf, &nparam, param);

        // Update new_point.
        for (int i = 0; i < n; i++) new_point.component(i) = p[i];
        new_point.component(n) = new_lambda = compute_lambda(n, new_point);

        // BEGIN Check Boundary //
        // Modified RectBoundary so that the intersection can be tested using RealVectors of size
        // greater than the dimension of the space the RectBoundary is in.
        int where_out;
        RealVector r;
        int intersection_info = boundary->intersection(previous_point, new_point, r, where_out);

//        printf("Inside while. previous_point = (");
//        for (int i = 0; i < n; i++){
//            printf("%g", previous_point.component(i));
//            if (i < n - 1) printf(", ");
//        }
//        printf(")\n");
//        printf("Inside while.      new_point = (");
//        for (int i = 0; i < n; i++){
//            printf("%g", new_point.component(i));
//            if (i < n - 1) printf(", ");
//        }
//        printf(")\n");

//        cout << "intersection_info = " << intersection_info << endl;

        if      (intersection_info == 1){
            // Both points inside. Carry on with the rest of the tests, etc.
        }
        else if (intersection_info == 0){
            // One point is inside, the other is outside.
            // Store the point lying in the domain's border and get out.
            r.component(n) = compute_lambda(n, r);
            rarcurve.push_back(r);

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

//        // END   Check Boundary //

        // BEGIN Check for monotonicity //
        if (check_monotony == CHECK_RAREFACTION_MONOTONY_TRUE){
           // cout << "new_lambda = " << new_lambda << ", previous_lambda = " << previous_lambda << endl;
            if ((new_lambda >= previous_lambda && increase == RAREFACTION_SPEED_DECREASE) ||
                (new_lambda <= previous_lambda && increase == RAREFACTION_SPEED_INCREASE)){
                cout<<"Valor de increase: "<<increase<<endl;
                // Find the point where lambda reaches a minimum, store it and get out.
                RealVector last_point;
                int info_compute_last_point = compute_last_point(previous_point, new_point, last_point);
                if (info_compute_last_point == SUCCESSFUL_PROCEDURE) rarcurve.push_back(last_point);
                else printf("Last point discarded.\n");

                printf("RAREFACTION_NOT_MONOTONOUS\n");
                return RAREFACTION_NOT_MONOTONOUS;
            }
            else {
                // Store the point and the eigenvalue and continue.
                rarcurve.push_back(new_point);
            }
        }
        else rarcurve.push_back(new_point);
        // END   Check for monotonicity //

        // Update the independent parameters.
        xi      = new_xi;
        new_xi += deltaxi;

        // Update the reference vector.
        for (int i = 0; i < n; i++) param[1 + i] = new_point.component(i) - previous_point.component(i); 
    }

    return SUCCESSFUL_PROCEDURE;
}

