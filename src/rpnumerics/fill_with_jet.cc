#include "fill_with_jet.h"

// This function that fills F, J and H using jets.
//
// Arguments:
//
//    flux_object: Pointer to an object of a FluxFunction-derived class. This
//                 object will perform the jet-related operations.
//              n: Dimension of the space.
//             in: Array that contains the point where F, J and H will be computed.
//         degree: 0: Compute F,
//                 1: Compute F and J,
//                 2: Compute F, J and H.
//              F: An array, externally reserved, where the value of the function
//                 at in is stored.
//              J: A matrix, externally reserved, where the value of the Jacobian
//                 of the function at in is stored. Should be 0 if degree == 0.
//              H: An array, externally reserved, where the value of the Hessian
//                 of the function at in is stored. Should be 0 if degree == 1.
//
// The user MUST reserve the space needed for F, J and H. If some of these are not needed, they
// should be set to zero. For example, the rarefaction only uses J. Therefore, the
// user should reserve an array of n*n doubles for J, and invoke this function passing 0 for F and H.
//
void fill_with_jet(FluxFunction *flux_object, int n, double *in, int degree, double *F, double *J, double *H){
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
    if (J != 0){
        for (int i = 0; i < n; i++){
            for (int j = 0; j < n; j++){
                J[i*n + j] = c_jet(i, j);
            }
        }
    }
    
    // Fill H
    if (H != 0){
        for (int i = 0; i < n; i++){
            for (int j = 0; j < n; j++){
                for (int k = 0; k < n; k++){
                    H[(i*n + j)*n + k] = c_jet(i, j, k); // Check this!!!!!!!!
                }
            }
        }    
    }

    return;
}
