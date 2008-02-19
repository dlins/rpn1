/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) ContinuationRarefationMethod.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "ContinuationRarefactionMethod.h"
#include "RpNumerics.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


//! Code comes here! daniel@impa.br


ContinuationRarefactionMethod::ContinuationRarefactionMethod(const RarefactionFlow & flow ):RarefactionMethod(flow){}

ContinuationRarefactionMethod::ContinuationRarefactionMethod(const ContinuationRarefactionMethod & copy):RarefactionMethod(copy.getFlow()){}

//struct RarefactionCurve ContinuationRarefactionMethod::plot(const RealVector & input){
//    
//    struct RarefactionCurve output;
//    
//    output.coords=curve(input);
//
//    return output;
//    
//    
//    
//};

struct RarefactionCurve ContinuationRarefactionMethod::curve(const RealVector & initialPoint) {
    
    // BEGIN{Initialize the reference eigenvector}
    
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
    
    
    // 1. Find the eigenvalue and the eigenvector at in (the initial point)
    
    RarefactionFlow & rarefactionFlow  =(RarefactionFlow &)getFlow();
    
    rarefactionFlow.setReferenceVector(initialPoint);
    
    int dimensionSize= initialPoint.size();
    
    int i, status;
    
    WaveState input(initialPoint);
    
    JetMatrix output(dimensionSize);
    
    status = rarefactionFlow.jet(input, output, 0);
    
    RealVector refVector(dimensionSize);
    
    for (i=0;i < dimensionSize; i++){
        
        refVector.component(i)=output(i); //e
        
    }
    
//    vector<RealVector> returned;
    
      struct RarefactionCurve  curve;

    double lambda = rarefactionFlow.lambdaCalc(input, rarefactionFlow.getFamilyIndex()); // lambda
    
    if (lambda == COMPLEX_EIGENVALUE) return curve;
    
    // 2. and 3. Find the eigencouples at in_plus and in_minus.
    double epsilon = 1e-5;
    
    double lambdap, lambdam; // Lambda_plus, lambda_minus
    
    WaveState tempWaveState(dimensionSize);
    
    int ii;
    
    for (ii = 0; ii < dimensionSize; ii++){
        
        tempWaveState.operator ()(ii)=initialPoint.operator ()(ii)+epsilon*refVector.component(ii);
    }
    
    lambdap=rarefactionFlow.lambdaCalc(tempWaveState, rarefactionFlow.getFamilyIndex());
    
    if (lambdap == COMPLEX_EIGENVALUE) return curve;
    
    for (ii = 0; ii < dimensionSize; ii++){
        
        tempWaveState.operator ()(ii)=initialPoint.operator ()(ii)-epsilon*rarefactionFlow.getReferenceVector().component(ii);
        
    }
    
    lambdam=rarefactionFlow.lambdaCalc(tempWaveState, rarefactionFlow.getFamilyIndex());
    
    if (lambdam == COMPLEX_EIGENVALUE) return curve;
    
//    // 4. Find the reference eigenvector.
    if (rarefactionFlow.direction() == 1){ // Eigenvalues should increase as the orbit advances
        if (lambda <= lambdap && lambda <= lambdam){
//            #ifdef TEST_RARERFACTION
            printf("Inside rarefactioncurve(): Cannot initialize, lambda doesn't increase!\n");
//            #endif
//            return NULL;//LAMBDA_NOT_INCREASING;
        }
        else if (lambda < lambdap && lambda > lambdam){
            rarefactionFlow.setReferenceVector(refVector);
//            // Nothing to do, the eigenvector is rev.
        }
        else if (lambda > lambdap && lambda < lambdam){
//            for (ii = 0; ii < n; ii++) rev[ii] = -rev[ii];
            
            refVector.negate();
            rarefactionFlow.setReferenceVector(refVector);
            
        }
        else {
//            #ifdef TEST_RAREFACTION
            printf("Inside rarefactioncurve(): Cannot initialize, unexpected!\n");
//                    #endif
//                    return NULL;//LAMBDA_NOT_INCREASING;
        }
    }
    else {              // Eigenvalues should decrease as the orbit advances
        if (lambda >= lambdap && lambda >= lambdam){
            //#ifdef TEST_RARERFACTION
            printf("Inside rarefactioncurve(): Cannot initialize, lambda doesn't decrease!\n");
            //#endif
            //return NULL;//LAMBDA_NOT_DECREASING;
        }
        else if (lambda > lambdap && lambda < lambdam){
            // Nothing to do, the eigenvector is rev.
            rarefactionFlow.setReferenceVector(refVector);
        }
        else if (lambda < lambdap && lambda > lambdam){
            //for (ii = 0; ii < n; ii++) rev[ii] = -rev[ii];
            refVector.negate();
            rarefactionFlow.setReferenceVector(refVector);
            
        }
        else {
            //#ifdef TEST_RAREFACTION
            printf("Inside rarefactioncurve(): Cannot initialize, unexpected!\n");
            //#endif
            //return NULL;//LAMBDA_NOT_DECREASING;
        }
    }
    
    
    const ODESolver & odeSolver = RpNumerics::getODESolver();
    
    RealVector inputPoint(initialPoint);
    
    vector <RealVector> out;
    
  
    
    for (i=0;i < 100;i++){
        
        RealVector coord(dimensionSize);
        
        odeSolver.solve(inputPoint,coord);
        
        inputPoint=coord;
        
        curve.coords.push_back(coord);
//        out.push_back(coord);
    }

    return curve;
}
