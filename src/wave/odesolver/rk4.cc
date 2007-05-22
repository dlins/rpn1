#include "rk4.h"

// RK4 Engine. The arguments are:
//
//          n: Dimension of the problem
//          t: The time of this step
//     deltat: Time step
//          p: Pointer to the array of coordinates where the integration takes place
//        out: Pointer to the array of coordinates where the solution lies  
//          f: Pointer to the function that defines the problem, and returns an array of doubles
//             (later used by rk4).
//
// The functions that define the problem must be grouped under f. An array will be given
// as input and another one as output, to be used by the RK4 proper.
// 
// RETURNS:
// ABORTED_PROCEDURE    (default: 1): An internal result met the stop criterions (defined elsewhere).
// SUCCESSFUL_PROCEDURE (default: 0): Everything's OK.
//

int rk4(int n, double t, double deltat, double *p, double *out, int (*f)(int, double, double*, double*)){
    int i, j, flag;
    double k1[n];
    double k2[n];
    double k3[n];
    double k4[n];
    
    double tmp[n];
    
    #ifdef TEST_RK4
        printf("Inside rk4(): received: p = (");
        for (i = 0; i < n; i++){
            printf("% f", p[i]);
            if (i != n - 1) printf(", ");
        }
        printf(")\n");
    #endif


    /* Compute K1--K4 */
    /* K1 */
    for (j = 0; j < n; j++) tmp[j] = p[j];
    flag = (*f)(n, t, &tmp[0], &k1[0]);   
    if (flag == ABORTED_PROCEDURE){
        #ifdef TEST_RK4
            printf("    Inside RK4 (K1): A stop criterion was met by an outside function!\n");
        #endif
        return ABORTED_PROCEDURE;
    }
    
    /* K2 */
    for (j = 0; j < n; j++) tmp[j] = p[j] + deltat*k1[j]/2.0;
    flag = (*f)(n, t, &tmp[0], &k2[0]);
    if (flag == ABORTED_PROCEDURE){
        #ifdef TEST_RK4
            printf("    Inside RK4 (K2): A stop criterion was met by an outside function!\n");
        #endif
        return ABORTED_PROCEDURE;
    }
    
    /* K3 */
    for (j = 0; j < n; j++) tmp[j] = p[j] + deltat*k2[j]/2.0;
    flag = (*f)(n, t, &tmp[0], &k3[0]);
    if (flag == ABORTED_PROCEDURE){
        #ifdef TEST_RK4
            printf("    Inside RK4 (K3): A stop criterion was met by an outside function!\n");
        #endif
        return ABORTED_PROCEDURE;
    }

    /* K4 */
    for (j = 0; j < n; j++) tmp[j] = p[j] + deltat*k3[j];
    flag = (*f)(n, t, &tmp[0], &k4[0]);
    if (flag == ABORTED_PROCEDURE){
        #ifdef TEST_RK4
            printf("    Inside RK4 (K4): A stop criterion was met by an outside function!\n");
        #endif
        return ABORTED_PROCEDURE;
    }

    /* Advance in time */
    for (i = 0; i < n; i++) out[i] = p[i] + deltat*(k1[i] + 2*(k2[i] + k3[i]) + k4[i])/6.0;

    return SUCCESSFUL_PROCEDURE;
}






