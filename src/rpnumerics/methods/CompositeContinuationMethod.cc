/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) CompositeContinuationMethod.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "CompositeContinuationMethod.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */



CompositeContinuationMethod::CompositeContinuationMethod(const ODESolver & solver) : solver_(solver.clone()) {

}

void CompositeContinuationMethod::curve(const RealVector & input, int direction, vector<RealVector> & output) {

    //Versao do dia 10/9/2009

    // Set the flux object that will be used by all the jet-dependent functions.

    CompositeFlow & flow = (CompositeFlow &) solver_->getProfile().getFunction();

    int n = input.size();

    int indx = flow.getFamily();

    double in [n];

    int previous = 0;


    for (int i = 0; i < n; i++) in[i] = input(i);
    
    

    if (previous == INITIAL_CURVE) {
        // TODO: Initialize the composite curve. In rare occasions the composite will be the
        // initial curve.
        // Here the input_ref_vec should be modified, but I may need more than that.
    }

    // Store the first point of the curve...
    double U[2 * n + 1];
    for (int i = 0; i < 2 * n; i++) U[i] = in[i];

    // ...and the speed (eigenvalue) to it associated
    double lambda, rev[n];

    ContinuationRarefactionFlow & contRarefactionFlow = (ContinuationRarefactionFlow &) flow.getRarefactionFlow();
    int info_flux = contRarefactionFlow.flux(n, indx, in, &lambda, rev); // Declared in jetrarefaction.h   int info_flux = flux(n, indx, cmp_flux_object, in, &lambda, rev); 
    U[2 * n] = lambda;
    //    if (info_flux != SUCCESSFUL_PROCEDURE) return info_flux;
    if (info_flux != SUCCESSFUL_PROCEDURE) return;

    //    add(out, U);
    //    add(out, U);

    // Now that U and lambda were stored, numtotal is updated:
    //    (*numtotal) = 1;

    // The parameters are set here (thus avoiding global variables):
    int nparam = 1 + 2 * n;
    double param[nparam];


    //TODO : Como pegar esses parametros ????

    //    param[0] = indx;                                                  // Family
    //    for (int i = 0; i < n; i++) param[1 + i]     = -input_ref_vec[i]; // Rar. ref. vector
    //    for (int i = 0; i < n; i++) param[1 + n + i] =  input_ref_vec[i]; // Shk. ref. vector

    // A vector only for LSODE to work with.
    double Ulsode[2 * n];
//    for (int i = 0; i < 2 * n; i++) Ulsode[i] = in[i];
//    for (int i = 0; i < 2 * n; i++) Ulsode[i] += deltaxi * param[1 + i];

    // Compute the curve proper (BEGIN)
    RealVector outputVector(n);
    RealVector inputVector(n);

    int info = SUCCESSFUL_PROCEDURE;
    int step = 0;
    double testeDouble = 0; //TODO Dummy value !!
    while (step < solver_->getProfile().maxStepNumber() && info == SUCCESSFUL_PROCEDURE) {


        //    while (info == SUCCESSFUL_PROCEDURE && (*numtotal) < nummax){
        // Store a copy of the reference vector, so that it can be compared later with the
        // eigenvector at the point returned by the solver and thus prepare
        // the reference vector for the next iteration
        double old_rar_refvec[n];
        for (int i = 0; i < n; i++) old_rar_refvec[i] = param[1 + i];

        info = solver_->solve(inputVector, outputVector, testeDouble);
        //        cout << "Output: " << outputVector << endl;


        //            
        //        // Invoke the solver
        //        info = solver(&composite, &neq, &Ulsode[0], &t, &tout, &itol, &rtol, &atol[0], 
        //                      &itask, &istate, &iopt, &rwork[0], &lrw, &iwork[0], &liw, 
        //                      //&jacrarefaction, &mf, &nparam, &param[0]);
        //                      0, &mf, &nparam, &param[0]);

        if (info == SUCCESSFUL_PROCEDURE) {
            // Generate the next reference vector (and the lambda to be stored here).
            double rar_ref[n];
            double rar_in[n];
            for (int i = 0; i < n; i++) rar_in[i] = Ulsode[i];
            double lambda_u;

            //            int rar_info = contRarefactionFlow.flux(n, indx, rar_in, &lambda_u, rar_ref);
            contRarefactionFlow.flux(n, indx, rar_in, &lambda_u, rar_ref);

            double p = 0;
            for (int i = 0; i < n; i++) p += rar_ref[i] * old_rar_refvec[i];
            if (p > 0) for (int i = 0; i < n; i++) param[1 + i] = rar_ref[i];
            else for (int i = 0; i < n; i++) param[1 + i] = -rar_ref[i]; // TODO: See that the reference vector for the shock part IS NOT GENERATED

            // Store the point
            for (int i = 0; i < 2 * n; i++) U[i] = Ulsode[i];
            U[2 * n] = lambda_u;
            RealVector outputPoint(2 * n);
            for (int i = 0; i < 2 * n; i++) outputPoint(i) = U[i];
            output.push_back(outputPoint);
//            add(out, U);

            // CHECK MONOTONICITY, DOMAIN

            // Update the counter and the time
//            (*numtotal)++;
            step++;
//            tout += deltaxi;

            LSODE::increaseTime();
        } else {
//            printf("Cmp aborted, numtotal = %d\n", (*numtotal));
            printf("Cmp aborted, numtotal = %d\n", step);
//            return ABORTED_PROCEDURE;
            return;
        }
    }
    // Compute the curve proper (END)
//    return SUCCESSFUL_PROCEDURE;
    return ;


}

