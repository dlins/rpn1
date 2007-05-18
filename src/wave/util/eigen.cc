#include "eigen.h"


// To fill an array of eigen structs. It is assumed that the matrix containing the
// eigenvectors returned by Lapack was tranposed before being fed to
// this function.
void fill_eigen(struct eigen e[], double rp[], double ip[], double vl[], double vr[]){
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
                printf("Problem in fill_eigen! i = %d\n", i);
            }
        }
    }
    return;
}

// Engine of print_eigen_struct (below), but it can be used on its own
void print_eigen(struct eigen *e, int j){
    int k;
    
    if (fabs(e->i) < EPS){ // The eigenvalue is real, or its imaginary part is negligible
        printf("Eigenvalue  %d =  % f,\n", j, e->r);
        
        printf("Right-eigenvector %d = \n", j);
        for (k = 0; k < EIG_MAX; k++){
            //if (k != 0) printf("                ");
            printf("                [% f]\n", e->vrr[k]);
        }
    }
    else {                 // The eigenvalue is complex
        printf("Eigenvalue  %d =  % f", j, e->r);
        
        if (e->i >= 0) printf(" + %f*i,\n", e->i);
        else           printf(" - %f*i,\n", -e->i);
        
        printf("Right-eigenvector %d = \n", j);
        for (k = 0; k < EIG_MAX; k++){
            //if (k != 0) printf("                ");
            
            printf("                [% f", e->vrr[k]);
            
            //            double imaginary = e->vri[k];
            printf(" %+f*i]\n", e->vri[k]);
            /*
             if (imaginary > 0) printf(" + %f*i] (%f)\n", abs(imaginary), e->vi[i]);
             else              printf(" - %f*i] (%f)\n", -abs(imaginary), e->vi[i]);
             */
        }
    }
    
    return;
}

// Print an array of struct eigen
void print_eigen_struct(struct eigen e[]){
    int i;
    for (i = 0; i < EIG_MAX; i++){
        print_eigen(&(e[i]), i);
        printf("\n");
    }
    return;
}

// Engine of sort_eigen (below)
int eigen_comp(const void *p1, const void *p2){
    struct eigen *sp1 = (struct eigen *) p1;
    
    struct eigen *sp2 = (struct eigen *) p2;
    
    return ((int)sp1->r) - ((int)sp2->r);
}

// Sort e[] according to the real part of the eigenvalues
void sort_eigen(struct eigen e[]){
    qsort(e, EIG_MAX, sizeof(e[0]), eigen_comp);
    return;
}

// Transpose a square matrix, rewriting the original
void transpose(double A[], int n){
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

// Eigenproblem
// This function computes the eigenvalues and right-eigenvectors of a matrix
// using LAPACK's dgeev and sorts them according to the value of the real part
// of the eigenvalues, in crescent order.
//
// The parameters are:
//
//     n: Dimension of the field and therefore of the matrix (n*n). Probably not necessary.
//     A: The n*n matrix.
//     e: The array of struct eigen where the eigencouples will be stored.
//
// The function returns:
//
//     ABORTED_PROCEDURE:   LAPACK's dgeev returned info != 0, there was a problem. This
//                          should never happen.
//     SUCCESFUL_PROCEDURE: Everything's OK.
//
int eigen_rl(int n, Matrix3 &A, Vector3 &eigenValues, Matrix3 &eigenVectors){
    int lda = n, lwork = 5*n, ldvr = n, ldvl = n;
    int i, j, info;
    double vr[n][n], vl[n][n];
    double work[5*n], wi[n], wr[n];
    struct eigen e[EIG_MAX];
    double B[n][n];
    
    Matrix3 transposedA;
    // Create a transposed copy of A to be used by LAPACK's dgeev:
    transposedA=transpose(A);
    
    for (i = 0; i < n; i++){
        for (j = 0; j < n; j++) B[i][j] = transposedA(i, j);
    }
     dgeev_("V", "V", &n, &B[0][0], &lda, &wr[0], &wi[0],
    &vl[0][0], &ldvl, &vr[0][0], &ldvr, &work[0], &lwork,
    &info);
    
    // Process the results
    if (info != 0) return 1;
    else {
        transpose(&vl[0][0], n); // ...or else...
        transpose(&vr[0][0], n); // ...or else...
        fill_eigen(e, &wr[0], &wi[0], &vl[0][0], &vr[0][0]);
        sort_eigen(e);
        
        
        //Pegar apenas a parte real ????
        
        for (i=0;i < EIG_MAX; i++){
            
            eigenValues(i)=e[i].r;
        }
        
        //Pegar apenas a parte real dos autovalores a direita ????
        
        for (i=0;i < EIG_MAX; i++){
            for (j=0;j< EIG_MAX;j++){
                eigenVectors(i, j)=e[i].vrr[j] ;
            }
        }
        
        return 0;
    }
}
