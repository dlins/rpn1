#include "eigen.h"

// Initialize the value of the epsilon
double Eigen::epsilon(1e-10);

// Transpose a square matrix, rewriting the original.
//
void Eigen::transpose(int n, double *A){
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

// To fill an array of eigen structs. It is assumed that the matrix containing the
// eigenvectors returned by Lapack was tranposed before being fed to
// this function.
void Eigen::fill_eigen(int n, struct eigencouple e[], double rp[], double ip[], double vl[], double vr[]){
    int i, j;
     
    for (i = 0; i < n; i++){
        if (fabs(ip[i]) < Eigen::epsilon){ // Eigenvalue is real
            e[i].r = rp[i];
            e[i].i = 0;
            for (j = 0; j < n; j++){
                e[i].vlr[j] = vl[j*n + i];
                e[i].vli[j] = 0;
                e[i].vrr[j] = vr[j*n + i];
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
                for (j = 0; j < n; j++){
                    // Real part of the pair of complex eigenvectors:
                    e[i].vr[j]     = v[j*n + i];
                    e[i + 1].vr[j] = v[j*n + i];
                    
                    // For the imaginary part of the pair of complex eigenvectors:
                    temp_vi = v[j*n + i + 1];
                    if (fabs(temp_vi) < EPS) temp_vi = 0;
                    e[i].vi[j]     = temp_vi;
                    e[i + 1].vi[j] = -temp_vi;
                }
*/
                
                // Begin of [4]
                // This code was replaced by the code above, marked as [3].
                // See also blocks [1] and [2].
                for (j = 0; j < n; j++){
                    e[i].vlr[j] = vl[j*n + i];
                    e[i].vli[j] = vl[j*n + i + 1];
                    
                    e[i + 1].vlr[j] = vl[j*n + i];
                    e[i + 1].vli[j] = -vl[j*n + i + 1];
                    
                    e[i].vrr[j] = vr[j*n + i];
                    e[i].vri[j] = vr[j*n + i + 1];
                    
                    e[i + 1].vrr[j] = vr[j*n + i];
                    e[i + 1].vri[j] = -vr[j*n + i + 1];                    
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

// Engine of sort_eigen (below)
int Eigen::eigen_comp(const void *p1, const void *p2){
   eigencouple *sp1 = (eigencouple*)p1;
   eigencouple *sp2 = (eigencouple*)p2;

   double temp = sp1->r - sp2->r;

   if (temp > 0) return 1;
   else if (temp < 0) return -1;
   else return 0;
}

// Sort e[] according to the real part of the eigenvalues
void Eigen::sort_eigen(int n, eigencouple *e){
    qsort(e, n, sizeof(e[0]), eigen_comp);
    return;
}

// Compute all the eigencouples of a matrix and fill a
// vector of said structs.
//
//      n: Dimension of the space
//      A: Square matrix
//     ve: Vector where the eigencouples will be stored.
//
// This function uses LAPACK's dgeev. Eigenvectors will be
// normalized.
//
int Eigen::eig(int n, double *A, std::vector<eigencouple> &ve){
    printf("Eigen: A = [\n");
    for (int i = 0; i < n; i++){
        for (int j = 0; j < n; j++){
            printf(" %6.2f ", A[i*n + j]);
        }
        printf(";\n");
    }
    printf("]\n");

    int lda = n, lwork = 5*n, ldvr = n, ldvl = n;
    int i, j, info;
    double vr[n][n], vl[n][n];
    double work[5*n], wi[n], wr[n];

    // Create a transposed copy of A to be used by LAPACK's dgeev:
    double B[n][n];
    for (i = 0; i < n; i++){
        for (j = 0; j < n; j++) B[j][i] = A[i*n + j];
    }

    dgeev_("V", "V", &n, &B[0][0], &lda, &wr[0], &wi[0], 
           &vl[0][0], &ldvl, &vr[0][0], &ldvr, &work[0], &lwork, 
           &info);

    // Process the results
    if (info != 0) return info;
    else {
        transpose(n, &vl[0][0]); // ...or else...
        transpose(n, &vr[0][0]); // ...or else...

        eigencouple e[n];
        for (int i = 0; i < n; i++){
            e[i].vlr.resize(n);
            e[i].vli.resize(n);
            e[i].vrr.resize(n);
            e[i].vri.resize(n);
        }

        fill_eigen(n, e, &wr[0], &wi[0], &vl[0][0], &vr[0][0]); 
        sort_eigen(n, e); 

        ve.clear();
        ve.resize(n);
        for (int i = 0; i < n; i++) ve[i] = e[i]; 
        return SUCCESSFUL_PROCEDURE;

    }

    return info;
}

void Eigen::print_eigen(const std::vector<eigencouple> &ve){
    for (int i = 0; i < ve.size(); i++){
        printf("Eigencouple #%d\n", i);
        printf("    Eigenvalue = % 6.2f + % 6.2fi\n", ve[i].r, ve[i].i);
        printf("\n");

        printf("    Left eigenvector =\n");
        for (int j = 0; j < ve.size(); j++){
            printf("    [% 6.2f + % 6.2fi]\n", ve[i].vlr[j], ve[i].vli[j]);
        }
        printf("\n");

        printf("    Right eigenvector =\n");
        for (int j = 0; j < ve.size(); j++){
            printf("    [% 6.2f + % 6.2fi]\n", ve[i].vrr[j], ve[i].vri[j]);
        }
        printf("\n");
    }

    return;
}

// Set the threshold used by fill_eigen.
//
void Eigen::eps(double e){
    Eigen::epsilon = e;
    return;
}

// Get the threshold used by fill_eigen.
//
double Eigen::eps(void){
    return Eigen::epsilon;
}
