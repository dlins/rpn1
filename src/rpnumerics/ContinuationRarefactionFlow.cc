/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) ContinuationRarefactionFlow.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "ContinuationRarefactionFlow.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */

ContinuationRarefactionFlow::ContinuationRarefactionFlow():RarefactionFlow(0,1,RpNumerics::getFlux()){
    
}

int ContinuationRarefactionFlow::flux(const RealVector & input, RealVector &output) const {

    double in[input.size()];
    double lambda;
    double out[input.size()];
    int family = 0; //TODO Hardcoded !!
    int dimension = input.size();
    
    for (int i = 0; i < input.size(); i++) {
        in[i]=input(i);
    }
    
    flux(dimension, family, &in[0], &lambda, &out[0]);
    

    for (int i = 0; i < input.size(); i++) {
        output(i)=out[i];
    }
    
}

int ContinuationRarefactionFlow::fluxDeriv(const RealVector & input, JacobianMatrix & output)const {

}

int ContinuationRarefactionFlow::fluxDeriv2(const RealVector & input, HessianMatrix & output)const {

}

WaveFlow * ContinuationRarefactionFlow::clone()const {

    return new ContinuationRarefactionFlow(*this);
}


int ContinuationRarefactionFlow::rarinit(int n, double *in, int indx, int increase, double deltaxi, double *lambda, double *rev)const{
    // 1. Find the eigenvalue and the eigenvector at in (the initial point):
    if (flux(n, indx, in, lambda, rev) == COMPLEX_EIGENVALUE) return COMPLEX_EIGENVALUE;
    
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
    if (flux(n, indx, inp, &lambdap, &rep[0]) == COMPLEX_EIGENVALUE) return COMPLEX_EIGENVALUE;
    if (flux(n, indx, inm, &lambdam, &rem[0]) == COMPLEX_EIGENVALUE) return COMPLEX_EIGENVALUE;

//    printf("@ rarefactioncurve(), after init.\nl- = % f, l = % f, l+ = % f\n", lambdam, *lambda, lambdap);
//    printf("e = (% f, % f), ", rev[0], rev[1]);
    
    // 4. Find the reference eigenvector.
    if (increase == 1){ // Eigenvalues should increase as the orbit advances
        if (*lambda <= lambdap && *lambda <= lambdam){
            #ifdef TEST_RAREFACTION
//            printf("Inside rarefactioncurve(): Cannot initialize, lambda doesn't increase!\n");
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
//            printf("Inside rarefactioncurve(): Cannot initialize, unexpected!\n");
            #endif
            return LAMBDA_NOT_INCREASING;
        }
    }
    else {              // Eigenvalues should decrease as the orbit advances
        if (*lambda >= lambdap && *lambda >= lambdam){
            #ifdef TEST_RAREFACTION
//            printf("Inside rarefactioncurve(): Cannot initialize, lambda doesn't decrease!\n");
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
//            printf("Inside rarefactioncurve(): Cannot initialize, unexpected!\n");
            #endif
            return LAMBDA_NOT_DECREASING;
        }
    }

    return SUCCESSFUL_PROCEDURE;
}


int ContinuationRarefactionFlow::flux(int n, int family, double *in, double *lambda, double *out)const{
    // Fill the Jacobianflux
    double J[n][n];
    DF(n, in, &J[0][0]);

    // Find J's eigencouples and sort them.
    int info;
    struct eigen e[n];
    info = cdgeev(n, &J[0][0], &e[0]);
    
    if (e[family].i != 0) return COMPLEX_EIGENVALUE;
    else {
        *lambda = e[family].r;
        int i;
        for (i = 0; i < n; i++) out[i] = e[family].vrr[i];
        return SUCCESSFUL_PROCEDURE;
    }
}




int ContinuationRarefactionFlow::rarefaction(int *neq, double *xi, double *in, double *out, int *nparam, double *param){
    // The dimension of the problem:
    int n = *neq;
    
    // The family:
    int family = (int)param[0];

    // The reference eigenvector:
    double rev[n]; int ii;
    for (ii = 0; ii < n; ii++) rev[ii] = param[1 + ii];
    
    // Fill the Jacobian
    double J[n][n];
    DF(n, in, &J[0][0]);

    // Find J's eigencouples and sort them.
    int i,info;
    struct eigen e[n]; for (i = 0; i < n; i++) out[i] = e[family].vrr[i];
    info = cdgeev(n, &J[0][0], &e[0]);
    
    // Check for stop criteria
    // TODO This section is to be tuned.
    if (info == SUCCESSFUL_PROCEDURE){
        // All eigenvalues must be real
        for (i = 0; i < n; i++){
            if (fabs(e[i].i) > 0){
                #ifdef TEST_RAREFACTION
                    printf("At rarefaction(): Imaginary eigenvalue!\n");
                    printf("Eigenvalue %d = % g %+g*i.\n", i, e[i].r, e[i].i);
                #endif
                return COMPLEX_EIGENVALUE;
            }
        }
        
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
    
    // STOP CRITERION:
    // The identity in Proposition 10.11 of 
    // "An Introduction to Conservation Laws: 
    // Theory and Applications to Multi-Phase Flow" must not change
    // sign (that is, the rarefaction is monotonous).
    double H[EIG_MAX][EIG_MAX][EIG_MAX];
    double res[EIG_MAX];
    D2F(EIG_MAX, (double*)in, (double*)&H[0][0][0]);
    applyH(EIG_MAX, &(e[family].vrr[0]), &H[0][0][0], &(e[family].vrr[0]), &res[0]);
    double dlambda_dtk = prodint(EIG_MAX, &res[0], &(e[family].vlr[0]))/
        prodint(EIG_MAX, &(e[family].vlr[0]), &(e[family].vrr[0]));
    if (dlambda_dtk*ref_speed < 0){
        #ifdef TEST_RAREFACTION
            printf("At rarefaction(): rarefaction not monotonous!\n");
            printf("At rarefaction(): dlambda_dtk = % f\n", dlambda_dtk);
        #endif
        return ABORTED_PROCEDURE;
    }
        
    // Set the value of the last viable eigenvalue (the shock speed):
    lasteigenvalue = e[family].r;

    // Update the value of the reference eigenvector:
    for (i = 0; i < n; i++) {
        re[i] = out[i]; //printf("re[%d] = % f\n", i, re[i]);
    }
    return SUCCESSFUL_PROCEDURE;
}

int ContinuationRarefactionFlow::D2F(int n, double *in, double *out)const{
    out[0] =  1;
    out[1] =  1;
    out[2] =  1;
    out[3] =  0;
    out[4] =  0;
    out[5] =  -1;
    out[6] =  -1;
    out[7] =  1;    
//    int i;
    //for (i = 0; i < n*n*n; i++) out[i] = 0;
    return SUCCESSFUL_PROCEDURE;
}

int ContinuationRarefactionFlow::DF(int neq, double *y, double *pd)const{
//    pd[0] =  y[0] + y[1];
//    pd[1] =  y[0];
//    pd[2] =        -y[1];
//    pd[3] = -y[0] + y[1];
    pd[0] =  3*y[0];
    pd[1] =  y[1];
    pd[2] =  y[1];
    pd[3] =  y[0];
//    pd[0] = 1; pd[1] = 1; pd[2] = 0; pd[3] = -1;
    return SUCCESSFUL_PROCEDURE;
}


int ContinuationRarefactionFlow::cdgeev(int n, double *A, struct eigen *e)const{
    int lda = n, lwork = 5*n, ldvr = n, ldvl = n;
    int i, j, info;
    double vr[n][n], vl[n][n];
    double work[5*n], wi[n], wr[n];

    if (n == 1){
        double Delta = (A[0] - A[3])*(A[0] - A[3]) + 4*A[1]*A[2];
        if (Delta >= 0){
            // Eigenvalues and eigenvectors are real.

            // Eigenvalues
            double sqrtDelta = sqrt(Delta);
            double bminus = A[0] + A[3];
            
            if (bminus > 0){
                wr[0] = .5*(bminus + sqrtDelta);
                wr[1] = .5*(bminus*bminus - Delta)/(bminus + sqrtDelta);
            }
            else{
                wr[0] = .5*(bminus - sqrtDelta);
                wr[1] = .5*(bminus*bminus - Delta)/(bminus - sqrtDelta);
            }
            
            //wr[0] = (A[0] + A[3] - sqrtDelta)/2;
            //wr[1] = (A[0] + A[3] + sqrtDelta)/2;
            
            wi[0] = 0; 
            wi[1] = 0;
            
            // First right-eigenvector
            if (A[0] == wr[0]){
                vr[0][0] = 1;
                vr[0][1] = 0;
            }
            else {
                vr[0][0] = A[1]/(wr[0] - A[0]);
                vr[0][1] = 1;
            }
            // Second right-eigenvector
            if (A[3] == wr[1]){
                vr[1][0] = 0;
                vr[1][1] = 1;
            }
            else {
                vr[1][0] = 1;
                vr[1][1] = A[2]/(wr[1] - A[3]);
            }
            
            // First left-eigenvector
            if (A[0] == wr[0]){
                vl[0][0] = 1;
                vl[0][1] = 0;
            }
            else {
                vl[0][0] = A[2]/(wr[0] - A[0]);
                vl[0][1] = 1;
            }
            // Second left-eigenvector
            if (A[3] == wr[1]){
                vl[1][0] = 0;
                vl[1][1] = 1;
            }
            else {
                vl[1][0] = 1;
                vl[1][1] = A[1]/(wr[1] - A[3]);
            }                        
            
            // Normalize
            for (i = 0; i < 2; i++){
                double sqrtlength;
                
                // Right-eigenvectors
                sqrtlength = sqrt(vr[i][0]*vr[i][0] + vr[i][1]*vr[i][1]);
                if (sqrtlength != 0){
                    vr[i][0] = vr[i][0]/sqrtlength;
                    vr[i][1] = vr[i][1]/sqrtlength;
                }
                
                // Left-eigenvectors
                sqrtlength = sqrt(vl[i][0]*vl[i][0] + vl[i][1]*vl[i][1]);
                if (sqrtlength != 0){
                    vl[i][0] = vl[i][0]/sqrtlength;
                    vl[i][1] = vl[i][1]/sqrtlength;
                }
            }
        }
        else {
            // Eigenvalues and eigenvectors are complex.
            wr[0] = (A[0] + A[3])/2;
            wr[1] = wr[0];
            
            wi[0] = fabs(-sqrt(-Delta)/2);
            wi[1] = -wi[0];
            
            // Eigenvectors will not be computed because
            // they will not be used in this case anyway.
        }
        info = 0;
    }
    else {
        // Create a transposed copy of A to be used by LAPACK's dgeev:
        double B[n][n];
        for (i = 0; i < n; i++){
            for (j = 0; j < n; j++) B[j][i] = A[i*n + j];
        }

        dgeev_("V", "V", &n, &B[0][0], &lda, &wr[0], &wi[0], 
               &vl[0][0], &ldvl, &vr[0][0], &ldvr, &work[0], &lwork, 
               &info); 
    } 
    
    // Process the results
    if (info != 0) return ABORTED_PROCEDURE;
    else {
        transpose(&vl[0][0], n); // ...or else...
        transpose(&vr[0][0], n); // ...or else...
        fill_eigen(e, &wr[0], &wi[0], &vl[0][0], &vr[0][0]);
        sort_eigen(e); 
        return SUCCESSFUL_PROCEDURE;
    }
}



double ContinuationRarefactionFlow::prodint(int n, double *a, double *b)const {
    extern double ddot_(int *, double *, int *, double *, int *);
    int incx = 1, incy = 1;
    return ddot_(&n, a, &incx, b, &incy);
}

void ContinuationRarefactionFlow::transpose(double A[], int n)const{
    int i, j;
    double temp;
    
    for (i = 0; i < n; i++){
        for (j = i; j < n; j++){
            temp = A[i*n + j];
            A[i*n + j] = A[j*n + i];
            A[j*n + i] = temp;
        }
    }
    return;
}


void ContinuationRarefactionFlow::fill_eigen(struct eigen e[], double rp[], double ip[], double vl[], double vr[])const {
    int i, j;
     
    for (i = 0; i < EIG_MAX; i++){
        if (fabs(ip[i]) < EPS){ // Eigenvalue is real
            e[i].r = rp[i];
            e[i].i = 0;
            for (j = 0; j < EIG_MAX; j++){
                e[i].vlr[j] = vl[j*EIG_MAX + i];
                e[i].vli[j] = 0;
                e[i].vrr[j] = vr[j*EIG_MAX + i];
                e[i].vri[j] = 0;                
            }
        }
        else{                   // Eigenvalue is complex
            if (ip[i] > 0){     // Make sure the eigenvalue's imaginary part is positive.
                                // In this case the i-th column of v contains the real part
                                // of the eigenvector and the (i + 1)-th column contains the
                                // imaginary part of the eigenvector. 
                               
                // If the eigenvalues are complex they are returned by Lapack in conjugated pairs,
                // the first of the pair having positive imaginary part
                e[i].r = rp[i];
                e[i + 1].r = rp[i];
/*                
                // Begin of [1] 
                // This code replaces the block below marked as [2].
                // See also blocks [3] and [4].
                // It should be better than the former, since it thresholds very small values of the
                // imaginary part of the eigenvector.
                double temp_i = ip[i];
                if (fabs(temp_i) < EPS) temp_i = 0;
                e[i].i = temp_i;
                e[i + 1].i = -temp_i;
                // End of [1]
*/                
                
                // Begin of [2]
                // This code was replaced by the code above, marked as [1].
                // See also blocks [3] and [4].
                e[i].i = ip[i];
                e[i + 1].i = -ip[i];
                // End of [2] 
                
                
/*
                // Begin of [3] 
                // This code replaces the block below marked as [4].
                // See also blocks [1] and [2].
                // It should be better than the former, since it thresholds very small values of the
                // imaginary part of the eigenvector.
                double temp_vi;
                for (j = 0; j < EIG_MAX; j++){
                    // Real part of the pair of complex eigenvectors:
                    e[i].vr[j]     = v[j*EIG_MAX + i];
                    e[i + 1].vr[j] = v[j*EIG_MAX + i];
                    
                    // For the imaginary part of the pair of complex eigenvectors:
                    temp_vi = v[j*EIG_MAX + i + 1];
                    if (fabs(temp_vi) < EPS) temp_vi = 0;
                    e[i].vi[j]     = temp_vi;
                    e[i + 1].vi[j] = -temp_vi;
                }
*/
                
                // Begin of [4]
                // This code was replaced by the code above, marked as [3].
                // See also blocks [1] and [2].
                for (j = 0; j < EIG_MAX; j++){
                    e[i].vlr[j] = vl[j*EIG_MAX + i];
                    e[i].vli[j] = vl[j*EIG_MAX + i + 1];
                    
                    e[i + 1].vlr[j] = vl[j*EIG_MAX + i];
                    e[i + 1].vli[j] = -vl[j*EIG_MAX + i + 1];
                    
                    e[i].vrr[j] = vr[j*EIG_MAX + i];
                    e[i].vri[j] = vr[j*EIG_MAX + i + 1];
                    
                    e[i + 1].vrr[j] = vr[j*EIG_MAX + i];
                    e[i + 1].vri[j] = -vr[j*EIG_MAX + i + 1];                    
                }
                // End of [4]
                
                
                i++;
            }
            else{              // This should never happen, but just in case...
//                printf("Problem in fill_eigen! i = %d\n", i);
            }
        }
    }    
    return;
}


// Engine of sort_eigen (below)
int ContinuationRarefactionFlow::eigen_comp(const void *p1, const void *p2){
   struct eigen *sp1 = (struct eigen *) p1;

   struct eigen *sp2 = (struct eigen *) p2;

   return sp1->r - sp2->r;
}

// Sort e[] according to the real part of the eigenvalues
void ContinuationRarefactionFlow::sort_eigen(struct eigen e[])const{
    qsort(e, EIG_MAX, sizeof(e[0]), eigen_comp);
    return;
}

void ContinuationRarefactionFlow::applyH(int n, double *xi, double *H, double *eta, double *out)const {
    int i, j, k;
    
    // Temporary matrix
    double Htemp[n][n];
    
    // Temporary vector
    double vtemp[n];
    
    for (k = 0; k < n; k++){
        for (i = 0; i < n; i++){
            for (j = 0; j < n; j++){
                // Fill the temporary matrix with the k-th layer of H,
                // i.e., with H[k][:][:]
                Htemp[i][j] = H[(k*n + i)*n + j];
                // vtemp = transpose(xi)*H[k][:][:]
                matrixmult(1, n, n, xi, &Htemp[0][0], &vtemp[0]);
                // out[k] = vtemp*eta
                matrixmult(1, n, 1, eta, &vtemp[0], &out[k]);
            }
        }
    }
    
    return;
}

void ContinuationRarefactionFlow::matrixmult(int m, int p, int n, double *A, double *B, double *C)const{
    extern double ddot_(int *, double *, int *, double *, int *);
    int i, j, k, incx = 1, incy = 1, pp = p;
    double sum;

    double arow[p], bcol[p];
    
    for (i = 0; i < m; i++){
        for (j = 0; j < n; j++){
            sum = 0;
            for (k = 0; k < p; k++) sum += A[i*p + k]*B[k*n + j];
            //C[i*n + j] = sum;
            
            // Alternate
            for (k = 0; k < p; k++){
                arow[k] = A[i*p + k];
                bcol[k] = B[k*n + j];
            }
            C[i*n + j] = ddot_(&pp, &arow[0], &incx, &bcol[0], &incy);
            
            //printf("C[%d][%d]: Conventional = % f; ddot_ = % f\n", i, j, sum, C[i*n + j]);
            
        }
    }
     
    return;
}

