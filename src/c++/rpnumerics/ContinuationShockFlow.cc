/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) ContinuationShockFlow.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "ContinuationShockFlow.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */

ContinuationShockFlow::ContinuationShockFlow(const RealVector & startPoint, const int familyIndex, const int direction, const FluxFunction & fluxFunction) :ShockFlow(startPoint,fluxFunction), familyIndex_(familyIndex), direction_(direction),startPoint_(new RealVector (startPoint)),referenceVector_(new RealVector(startPoint)) {
}


ContinuationShockFlow::ContinuationShockFlow(const ContinuationShockFlow & copy):ShockFlow(copy.getReferenceVector(),copy.fluxFunction()),familyIndex_(copy.getFamily()),direction_(copy.direction()),startPoint_(new RealVector(copy.getStartPoint())),referenceVector_(new RealVector(copy.getReferenceVector())) {
}

// Computes the shock.
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
//             for the shock, we have that:
//               
//             nparam   = 1 + 2*neq;
//             param[0] = family index.
//             param[1:n] = Um, the first point of the curve.
//             param[n + 1:2*n] = The reference vector.
//
// The function returns:
// ABORTED_PROCEDURE: Something went wrong,
// SUCCESSFUL_PROCEDURE: OK.
int ContinuationShockFlow::shock(int *neq, double *xi, double *in, double *out, int *nparam, double *param){

    int i;
    // Find the field at Um
    double Um[*neq];
    const RealVector & startPoint = getStartPoint();

    const RealVector & referenceVector = getReferenceVector();
    int familyIndex = getFamily();
    for (i = 0; i < *neq; i++) Um[i] = startPoint(i);//param[i + 1];

    int info = shockfield(*neq, Um, 1, in, familyIndex, out);

    if (info == SUCCESSFUL_PROCEDURE){
        // Check if the field is pointing correctly according to the reference vector
        double ref[*neq];

        for (i = 0; i < *neq; i++) ref[i] = referenceVector(i);// param[(*neq) + i + 1];
        if (inner_product(*neq, ref, out) < 0){
            for (i = 0; i < *neq; i++) out[i] = -out[i];
        }
        // Normalize the field
        normalize(1, *neq, out);
    }
    else printf("Shock failed!\n");

    return info;
}

// FUNCTION normalize
//
// This function normalizes a matrix's rows.
// The list of parameters:
//
//     rows: The number of rows of the matrix.
//     cols: The number of columns of the matrix.
//        v: The matrix.
//
// On exit, the vectors forming the rows of the matrix
// are normalized, that is,
//
//     v[i][j] = v[i][j]/norm[i], 
//
// where i = 0,..., (rows - 1); j = 0,..., (cols - 1) and
//
//     norm[i] = sqrt(v[i][0]^2 + ... + v[i][cols - 1]^2).
//
void ContinuationShockFlow::normalize(int rows, int cols, double *v){
    int i, j;
    for (i = 0; i < rows; i++){
        double norm = 0;
        for (j = 0; j < cols; j++){
            norm += v[i*cols + j]*v[i*cols + j];
        }
        norm = sqrt(norm);
        if (norm == 0) continue;
        
        for (j = 0; j < cols; j++){
            v[i*cols + j] = v[i*cols + j]/norm;
        }
    }
    return;
}


// FUNCTION shockfield
//
// This function computes the shockfield in several points.
// The list of parameters:
//
//         n: The dimension of the space.
//        Um: The initial point.
//         m: The number of points where the field is to be computed.
//        Up: A m*n array that holds the points where the field
//            is to be computed. Said points are to be stored by rows.
//    family: The family. The shockfield DOES NOT depend on the family
//            EXCEPT at Um, in which case the shockfield is the 
//            family-th right-eigenvector of the Jacobian of F. 
//            Elsewhere the shockfield is computed according to the 
//            formulae below.
//      dHdU: A m*n array that holds the derivative of H, by rows.
//

int ContinuationShockFlow::shockfield(int n, double Um[], int m, double *Up, int family, double *dHdu) {
    // This will change in time.

//    if (n != 2) return ABORTED_PROCEDURE;

    const FluxFunction & shock_flux_object = fluxFunction();
    
    // The function evaluated at Um
    double fm[n];
    fill_with_jet(shock_flux_object, n, Um, 0, fm, 0, 0); // F(n, Um, fm);
    // For all the points:
    int i, j;
    for (i = 0; i < m; i++) {
        // Extract the i-th row of Up, which is the i-th point where the field
        // will be computed.
        double U[n];
        for (j = 0; j < n; j++) U[j] = Up[i * n + j];

        // If U == Um, the field is the eigenvector, else the field is computed
        // according to the formulae below.

        if (diff(n, U, Um) < 1e-5) {
            //         printf("shockfield: U = Um\n");
            double J[n][n];
            fill_with_jet(shock_flux_object, n, Um, 1, 0, &J[0][0], 0); // DF(n, Um, &J[0][0]);
            // Find the eigenvector
             vector<eigenpair> e;
             int info = Eigen::eig(n, &J[0][0], e);

                if (info == ABORTED_PROCEDURE) {
               printf("Inside shockfield(): cdgeev() aborted!\n");
               return ABORTED_PROCEDURE;
            }

            // STOP CRITERION AT THIS POINT:
            // The i-th eigenvalue must be real.
            if (e[family].i != 0) {
               printf("Inside shockfield(): Init step, eigenvalue %d is complex: % f %+f.\n", family, e[family].r, e[family].i);
                return ABORTED_PROCEDURE;
            }

            // Extract the indx-th right-eigenvector of J:
            for (j = 0; j < n; j++) dHdu[i * n + j] = e[family].vrr[j];
        } else {
            //printf("shockfield: U != Um\n");
            double dHduvec[n];
            dH(n, Um, U, dHduvec);

            dHdu[i * n + 0] = -dHduvec[1];
            dHdu[i * n + 1] = dHduvec[0];
        }
    }

    return SUCCESSFUL_PROCEDURE;
}


int ContinuationShockFlow::flux(const RealVector &input, RealVector & output) {

    double in[input.size()];

    double out[input.size()];

    double xi=0;

    for (int i = 0; i < input.size(); i++) {
        in[i] = input(i);
    }
    int dimension = input.size();
    //Dummy !
    int nparam = 2 ;
    //Dummy !
    double param [nparam];

    shock(&dimension, &xi, in, out, &nparam, param);

     for (int i = 0; i < input.size(); i++) {
        output(i) = out[i];
    }
}

int ContinuationShockFlow::fluxDeriv(const RealVector &, JacobianMatrix &) {

    cout <<"Chamando flux Deriv"<<endl;
    
}
int ContinuationShockFlow::fluxDeriv2(const RealVector &, HessianMatrix &) {
    cout << "Chamando flux Deriv2" << endl;
}

WaveFlow * ContinuationShockFlow::clone()const {

    return new ContinuationShockFlow(*this);
}


// FUNCTION diff
//
// This function returns the absolute value of the difference
// component-wise of two vectors.
//
double ContinuationShockFlow::diff(int n, double x[], double y[]){
    double res = 0;
    int i;
    
    for (i = 0; i < n; i++) res += fabs(x[i] - y[i]);
    
    return res;
}


double  ContinuationShockFlow::inner_product(int n, double x[], double y[]){
    double s = 0; int i;
    for (i = 0; i < n; i++) s+= x[i]*y[i];
    return s;
}


void ContinuationShockFlow::fill_with_jet(const FluxFunction & flux_object, int n, double *in, int degree, double *F, double *J, double *H) {

    RealVector r(n);

    for (int i = 0; i < n; i++){
        r(i) = in[i];
    }

    // Will this work? There is a const somewhere in fluxParams.
    //FluxParams fp(r);
    //flux_object->fluxParams(FluxParams(r)); // flux_object->fluxParams(fp);

    WaveState state_c(r);
    JetMatrix c_jet(n);
    flux_object.jet(state_c, c_jet, degree);

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
                    H[(i * n + j) * n + k] = c_jet(i, j, k); // Check this!!!!!!!!
                }
            }
        }
    }

    return;
}

// FUNCTION dH
//
// This function computes the vector of derivatives of H
//

void ContinuationShockFlow::dH(int n, double Um[], double U[], double *dHdu) {
    // The function evaluated at Um
    double fm[n];

    const FluxFunction & shock_flux_object = fluxFunction();
    
    fill_with_jet(shock_flux_object, n, Um, 0, fm, 0, 0); // Previously: F(n, Um, fm);

    // The function and the Jacobian evaluated at U
    double fp[n], J[n][n];
    fill_with_jet(shock_flux_object, n, U, 1, fp, &J[0][0], 0); //F(n, U, fp); DF(n, U, &J[0][0]);

    // Compute A and B:
    double A, B, h2, du, dv;
    du = U[0] - Um[0];
    dv = U[1] - Um[1];
    h2 = du * du + dv*dv;

    // This field corresponds to Section 3.2 "Second Approach"

    A = fp[1] - fm[1] + du * J[1][0] - dv * J[0][0];
    B = fm[0] - fp[0] + du * J[1][1] - dv * J[0][1];

    // This field corresponds to Section 3.3 "Alternative H"
    /*
    A = (   J[1][0]*du + (fp[1] - fm[1]) - J[0][0]*dv   )/h2 - 
        2*du*(  (fp[1] - fm[1])*du - (fp[0] - fm[0])*dv  )/(h2*h2);
    B = (   J[1][1]*du - (fp[0] - fm[0]) - J[0][1]*dv   )/h2 - 
        2*dv*(  (fp[1] - fm[1])*du - (fp[0] - fm[0])*dv  )/(h2*h2);
     */

    // Store it
    dHdu[0] = A;
    dHdu[1] = B;

    return;
}


// FUNCTION shockspeed
//
// This function computes the shock speed, given two points,
// Up and Um, as in Eq. (3.2).
//
// Parameters:
//              n: Dimension of the space
//         family: The family
//    typeofspeed: 0 if Um == Up,
//                 1 otherwise
//             Up: "Plus" point
//             Um: "Minus" point
//
// Returns:
//     The speed as a double.
//

double ContinuationShockFlow::shockspeed(int n, int family, int typeofspeed, double Um[], double Up[]) {
    // If Up and Um are the same:  
    const FluxFunction & shock_flux_object = fluxFunction();
    if (typeofspeed == 0) {
//        struct eigen e[n];
        vector<eigenpair> e;
        double J[n][n];
        fill_with_jet(shock_flux_object, n, Um, 1, 0, &J[0][0], 0);
        Eigen::eig(n, &J[0][0], e);

        return e[family].r;
    }// If Up != Um
    else {
        // Compute F(Up), F(Um):
        double fp[n], fm[n];

        // The next two lines were replaced:
        //F(n, Up, fp);
        //F(n, Um, fm);
        fill_with_jet(shock_flux_object, n, Up, 0, fp, 0, 0);
        fill_with_jet(shock_flux_object, n, Um, 0, fm, 0, 0);

        // The speed
        double s = 0, den = 0;
        int i;

        for (i = 0; i < n; i++) {
            s += (fp[i] - fm[i])*(Up[i] - Um[i]);
            den += (Up[i] - Um[i])*(Up[i] - Um[i]);
        }

        return s / den;
    }
}

const RealVector & ContinuationShockFlow::getStartPoint()const {
    return *startPoint_;
}

const RealVector & ContinuationShockFlow::getReferenceVector()const {
    return *referenceVector_;
}

void ContinuationShockFlow::setReferenceVector(const RealVector & newReferenceVector) {

    for (int i = 0; i < newReferenceVector.size(); i++) {

        referenceVector_->operator()(i) = newReferenceVector(i);
    }

}

ContinuationShockFlow::~ContinuationShockFlow() {

    delete referenceVector_;
    delete startPoint_;
}
