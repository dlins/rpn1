/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) LSODE.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "LSODE.h"
#include "FluxParams.h"

using std::vector;

/*
 * ---------------------------------------------------------------
 * Definitions:
 */

double LSODE::tout_ = 0.01;
double LSODE::t_ = 0;

LSODEProfile * LSODE::profile_ = NULL;

LSODE::LSODE(const LSODEProfile & profile) {
    //setProfile(profile); 
    profile_ = new LSODEProfile(profile);
}

LSODE::LSODE(const LSODE & copy) {
    profile_ = new LSODEProfile((LSODEProfile &) copy.getProfile());
}

LSODE::~LSODE() {
    delete profile_;
}

int LSODE::solve(const RealVector & input, RealVector & output, double & time) const {

    int i;

    double * param;

    double * atol;

    int neq = profile_->numberOfEquations();

    int itol = profile_->absoluteToleranceType();

    int mf = profile_->methodFlag();

    int itask = profile_->task();

    int iopt = profile_->opt();

    int lrw = profile_->lengthRWork();

    int liw = profile_->lengthIWork();

    int istate = 1; //Initial value of istate !

    switch (itol) {

        case 1:

        case 2:

            atol = new double[profile_->numberOfEquations()];
            for (i = 0; i < profile_->numberOfEquations(); i++) {
                atol[i] = profile_->absoluteToleranceComponent(i);
            }
            break;

        default:

            cout << "Error in tolerance choose" << endl;
    }


    double *rwork = new double [profile_->lengthRWork()];

    int * iwork = new int[profile_->lengthIWork()];

    double rtol = profile_->relativeTolerance();

    double * U = new double [input.size()];

    int nparam = profile_->paramLength();

    param = new double[nparam];

    for (i = 0; i < nparam; i++) {

        param[i] = profile_->paramComponent(i);
    }


    for (i = 0; i < input.size(); i++) {

        U[i] = input.component(i);

    }

    int info;

//    cout << "LSODE, before solve" << endl;
//    for (int i = 0; i < neq + 1; i++) cout << "Inside LSODE, refvec[" << i << "] = " << param[i] << endl;

    info = solver(&LSODE::function, &neq, &U[0], &LSODE::t_, &LSODE::tout_, &itol, &rtol, &atol[0], &itask, &istate, &iopt, &rwork[0], &lrw, &iwork[0], &liw, &LSODE::jacrarefaction, &mf, &nparam, &param[0]);
//    cout << "LSODE, after solve" << endl;

    for (i = 0; i < neq; i++) {
        output.component(i) = U[i];
    }

    delete rwork;
    delete iwork;
    delete U;
    delete param;
    delete atol;
    return info;
}

int LSODE::function(int * neq, double * xi, double* U, double * out) {

    int i, status;
    


    RealVector input(*neq, U);
    RealVector output(*neq);

    status = profile_->getFunction().flux(input, output); //This function must return the same codes of LSODE s functions

    for (i = 0; i< *neq; i++) {

        out[i] = output(i);

    }
    return status;

}

const ODESolverProfile & LSODE::getProfile()const {
    return *profile_;
}

void LSODE::setProfile(const LSODEProfile & profile) {

    delete profile_;
    profile_ = new LSODEProfile(profile);
    //update_profile_data();
}

int LSODE::jacrarefaction(int *neq, double *t, double *y, int *ml, int *mu, double *pd, int *nrpd) {
    return SUCCESSFUL_PROCEDURE;
}

int LSODE::solver(int (*f)(int *, double *, double *, double *), int *neq, double *y, double *t, double *tout,
        int *itol, double *rtol, double *atol, int *itask, int *istate, int *iopt, double *rwork, int *lrw,
        int *iwork, int *liw, int(*j)(int *, double *, double *, int *, int *, double *, int *),
        int *mf, int *nparam, double *param) const {

    lsode_(f, neq, y, t, tout, itol, rtol, atol, itask, istate,
            iopt, rwork, lrw, iwork, liw, j, mf, nparam, param);


    if (*istate == 2) {
        return SUCCESSFUL_PROCEDURE;
    } else {

        cout<<"Valor de istate: "<<*istate<<endl;
        return *istate;
    }

}
