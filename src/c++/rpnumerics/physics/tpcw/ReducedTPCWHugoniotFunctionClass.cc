#include "ReducedTPCWHugoniotFunctionClass.h"

ReducedTPCWHugoniotFunctionClass::ReducedTPCWHugoniotFunctionClass(const RealVector & U, const Flux2Comp2PhasesAdimensionalized *fluxFunction, const Accum2Comp2PhasesAdimensionalized * accumFunction) :
HugoniotFunctionClass(*fluxFunction),
TPCWFluxAdimensionalized(fluxFunction),
TPCWAccumAdimensionalized(accumFunction) {
    n = U.size();

    Uref.resize(n);
    for (int i = 0; i < n; i++) Uref.component(i) = U.component(i);

    // TODO: The flux object must be initialized somehow (be it created here or outside, etc.)
    RealVector uref(Uref.size() - 1);
    //    printf("Uref.size() = %d\n", Uref.size());
    for (int i = 0; i < Uref.size() - 1; i++) uref.component(i) = Uref.component(i);

    WaveState u(uref); // TODO: Check this.
    //    for (int i = 0; i < uref.size(); i++) printf("u(%d) = %f\n", i, u(i));

    //    printf("here 1\n");

    JetMatrix arefJetMatrix(n);
    JetMatrix brefJetMatrix(n);

    aref_F = new double[n];
    bref_F = new double[n];

    fluxFunction->getReducedFlux()->jet(u, arefJetMatrix, 0);

    accumFunction->getReducedAccumulation()->jet(u, brefJetMatrix, 0);

    for (int i = 0; i < n; i++) {
        aref_F[i] = arefJetMatrix(i);
        bref_F[i] = brefJetMatrix(i);
    }

    //    printf("here 2\n");

    WaveState Ur(Uref);
    JetMatrix ArefJetMatrix(n);
    JetMatrix BrefJetMatrix(n);


    TPCWFluxAdimensionalized->jet(Ur, ArefJetMatrix, 1);
    TPCWAccumAdimensionalized->jet(Ur, BrefJetMatrix, 1);

    double A[n][n];
    double B[n][n];

    for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
            A[i][j] = ArefJetMatrix(i, j);
            B[i][j] = BrefJetMatrix(i, j);
        }
    }
    Eigen::eig(n, &A[0][0], &B[0][0], ve_uref);

    double epsilon = 1.0e-6; // TODO: Tolerance must be created within a class which will be used by everyone. Say, "class Tolerance", or something like that.

    if (fabs(ve_uref[0].i) < epsilon) Uref_is_elliptic = false;
    else Uref_is_elliptic = true;
}

void ReducedTPCWHugoniotFunctionClass::setReferenceVector(const RealVector & refVec) {

    n = refVec.size();

    Uref.resize(n);
    for (int i = 0; i < n; i++) Uref.component(i) = refVec.component(i);


    RealVector uref(Uref.size() - 1);
    printf("Uref.size() = %d\n", Uref.size());


    for (int i = 0; i < Uref.size() - 1; i++) uref.component(i) = Uref.component(i);


    WaveState u(uref); // TODO: Check this.
    for (int i = 0; i < uref.size(); i++) printf("u(%d) = %f\n", i, u(i));

    //    printf("here 1\n");

    JetMatrix arefJetMatrix(n);
    JetMatrix brefJetMatrix(n);

    aref_F = new double[n];
    bref_F = new double[n];

    TPCWFluxAdimensionalized->getReducedFlux()->jet(u, arefJetMatrix, 0);
    TPCWAccumAdimensionalized->getReducedAccumulation()->jet(u, brefJetMatrix, 0);


    for (int i = 0; i < n; i++) {
        aref_F[i] = arefJetMatrix(i);
        bref_F[i] = brefJetMatrix(i);
    }

    //    printf("here 2\n");

    WaveState Ur(Uref);

    JetMatrix ArefJetMatrix(n);
    JetMatrix BrefJetMatrix(n);
    //    TPCWFluxAdimensionalized->jet(Ur, ArefJetMatrix, 1);
    getFluxFunction().jet(Ur, ArefJetMatrix, 1);

//    for (int i = 0; i < n; i++) {
//        for (int j = 0; j < n; j++) {
//            cout << ArefJetMatrix(i, j) << " ";
//
//        }
//
//        cout << endl;
//    }

    TPCWAccumAdimensionalized->jet(Ur, BrefJetMatrix, 1);
    HugoniotFunctionClass::setReferenceVector(refVec);

}

ReducedTPCWHugoniotFunctionClass::ReducedTPCWHugoniotFunctionClass(const ReducedTPCWHugoniotFunctionClass & copy) : HugoniotFunctionClass(copy.getFluxFunction()),
TPCWFluxAdimensionalized(copy.TPCWFluxAdimensionalized),
TPCWAccumAdimensionalized(copy.TPCWAccumAdimensionalized),
Uref(copy.Uref),
n(copy.n),
Uref_is_elliptic(copy.Uref_is_elliptic),
ve_uref(copy.ve_uref) {

    aref_F = new double[n];
    bref_F = new double[n];

    for (int i = 0; i < n; i++) {
        aref_F[i] = copy.aref_F[i];
        bref_F[i] = copy.bref_F[i];
    }

}

HugoniotFunctionClass * ReducedTPCWHugoniotFunctionClass::clone()const {
    return new ReducedTPCWHugoniotFunctionClass(*this);
}

ReducedTPCWHugoniotFunctionClass::~ReducedTPCWHugoniotFunctionClass() {
    delete [] bref_F;

    delete [] aref_F;


}

double ReducedTPCWHugoniotFunctionClass::HugoniotFunction(const RealVector &u) {

    JetMatrix aJetMatrix(n); // TODO revisar isto com Rodrigo.
    JetMatrix bJetMatrix(n);

    TPCWFluxAdimensionalized->getReducedFlux()->jet(u, aJetMatrix, 0);
    TPCWAccumAdimensionalized->getReducedAccumulation()->jet(u, bJetMatrix, 0);

    double Hmatrix[n][n];

    for (int i = 0; i < n; i++) {
        Hmatrix[i][0] = bJetMatrix(i) - bref_F[i]; // b=G   
        Hmatrix[i][1] = -aJetMatrix(i); // a=F
        Hmatrix[i][2] = aref_F[i];
    }

    return 1e20 * det(n, &Hmatrix[0][0]);
    //return u.component(0)*u.component(0) + u.component(1)*u.component(1) - .5;
}

void ReducedTPCWHugoniotFunctionClass::completeCurve(vector<RealVector> & curve) {
    //    cout << "Chamando complete curve nao default" << endl;

    for (unsigned int i = 0; i < curve.size(); i++) {
        double darcy_speed;
        CompleteHugoniot(darcy_speed, curve[i]);

        int n = curve[i].size();
        double temp[n];
        for (int j = 0; j < n; j++) temp[j] = curve[i].component(j);

        curve[i].resize(n + 1);

        for (int j = 0; j < n; j++) curve[i].component(j) = temp[j];
        curve[i].component(n) = darcy_speed;
    }
    //    cout << "Saindo de complete curve" << endl;
    return;

}

void ReducedTPCWHugoniotFunctionClass::CompleteHugoniot(double &darcy_speedplus, const RealVector &uplus) {
    double G1minus = bref_F[0];
    double G2minus = bref_F[1];
    double G3minus = bref_F[2];

    double F1minus = aref_F[0];
    double F2minus = aref_F[1];
    double F3minus = aref_F[2];

    JetMatrix aJetMatrix(n); // TODO revisar isto com Rodrigo.
    JetMatrix bJetMatrix(n);

    //  create wave state for using uplus in the jets
    WaveState up(uplus); // TODO: Check this.    

    TPCWFluxAdimensionalized->getReducedFlux()->jet(up, aJetMatrix, 0);
    TPCWAccumAdimensionalized->getReducedAccumulation()->jet(up, bJetMatrix, 0);

    double G1plus = bJetMatrix(0);
    double G2plus = bJetMatrix(1);
    double G3plus = bJetMatrix(2);

    double F1plus = aJetMatrix(0);
    double F2plus = aJetMatrix(1);
    double F3plus = aJetMatrix(2);

    double G1quad = G1plus - G1minus;
    double G2quad = G2plus - G2minus;
    double G3quad = G3plus - G3minus;

    /* {j,k} = {1,2} {1,3} {2,3}

    Xjkminus = Fjminus*Gkquad - Fkminus*Gjquad ;
    Xjkplus  = Fjplus*Gkquad  - Fkplus*Gjquad  ;
    Yjk      = Fjplus*Fkminus - Fjminus*Fkplus ;
     */

    ///// CORRECTIONS HAVE BEEN PERFORMED AT THIS STAGE  CORRECTIONS BY HELMUT 05/01/2010.

    double X12minus = F1minus * G2quad - F2minus*G1quad;
    double X13minus = F3minus * G1quad - F1minus*G3quad;
    double X23minus = F2minus * G3quad - F3minus*G2quad;

    double X12plus = F1plus * G2quad - F2plus*G1quad;
    double X13plus = F3plus * G1quad - F1plus*G3quad;
    double X23plus = F2plus * G3quad - F3plus*G2quad;

    //    double Y12 = F1minus * F2plus - F1plus*F2minus;
    //    double Y13 = F1plus * F3minus - F1minus*F3plus;
    //    double Y23 = F2minus * F3plus - F2plus*F3minus;

    double den = X12plus * X12plus + X13plus * X13plus + X23plus*X23plus;

    // shock_speed    = Uref(2)*(Y12*X12plus + Y13*X13plus + Y23*X23plus)/den ; Calculated in the post-processing of the coloring.

    darcy_speedplus = Uref(2)*(X12minus * X12plus + X13minus * X13plus + X23minus * X23plus) / den;

    ///// CORRECTIONS.

    return;
}

double ReducedTPCWHugoniotFunctionClass::det(int nn, double *A) {
    double determinant;

    switch (nn) {
        case 1:
            determinant = A[0];
            return determinant;
            break;

        case 2:
            determinant = A[0] * A[3] - A[1] * A[2];
            return determinant;
            break;

        case 3:
            determinant = A[0]*(A[4] * A[8] - A[5] * A[7])
                    - A[3]*(A[1] * A[8] - A[7] * A[2])
                    + A[6]*(A[1] * A[5] - A[4] * A[2]);

            return determinant;
            break;

        default: // 4 or greater
            //TODO:     COMO SE ACHA O DETERMINANTE COM LAPACK PARA DIMENSAO 4:  FATORIZACOES?

            /*
                        int nrhs = 1;
                        int lda = nn;
                        int ipiv[nn];
                        int ldb = nn;
                        int info;
             */

            /*
                        // Create a transposed copy of A to be used by LAPACK's dgesv:
                        double B[nn][nn];
                        for (i = 0; i < nn; i++){
                            for (j = 0; j < nn; j++) B[j][i] = A[i*nn + j];
                        }
    
                        // Create a copy of b to be used by LAPACK's dgesv:
                        double bb[nn];
                        for (i = 0; i < nn; i++) bb[i] = b[i];
    
                        dgesv_(&dim, &nrhs, &B[0][0], &lda, &ipiv[0], &bb[0], &ldb, &info);
    
                        if (info == 0){
                            for (i = 0; i < nn; i++) x[i] = bb[i];
                            return SUCCESSFUL_PROCEDURE;
                        }
                        else return ABORTED_PROCEDURE;
             */
            return 0.0; // TODO: FIX THIS
            break;
    }

}

