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

double LSODE::tout_=0.05;

LSODEProfile * LSODE::profile_=NULL;

LSODE::LSODE(const LSODEProfile & profile) {
    profile_ = new LSODEProfile(profile);
//    stopGenerator_ = new LSODEStopGenerator(profile);
}


LSODE::LSODE(const LSODE & copy){
    profile_=new LSODEProfile((LSODEProfile &)copy.getProfile());
//    stopGenerator_ = new LSODEStopGenerator((LSODEProfile &)copy.getProfile());
}

LSODE::~LSODE() {
    delete profile_;
//    delete stopGenerator_;
    
}


int LSODE::solve(const RealVector & input, RealVector & output, double  & time) const{

    
    int i;
    
    double xi=0; //HARDCODED Useless variable !!
//    xi=1;       // Only to remove warnings
    
    double t=0;
    
    double * param;
    double  * atol;
    
    int neq = profile_->numberOfEquations();
    
    int itol = profile_->absoluteToleranceType();
    
    int mf = profile_->methodFlag();
    
    int itask = profile_->task();
    
    int iopt= profile_->opt();
    
    int lrw = profile_->lengthRWork();
    int liw = profile_->lengthIWork();
    
    int istate=1;//Initial value of istate !
    
    switch (itol){
        
        case 1:
            
            break;
        case 2:
            
            atol = new double[profile_->numberOfEquations()];
            for (i = 0; i < profile_->numberOfEquations(); i++)
                atol[i] = profile_->absoluteToleranceComponent(i);
            
            break;
        default:
            
            cout << "Error in tolerance choose" <<endl;
    }
    
    
    double *rwork = new double [profile_->lengthRWork()];
    
    int * iwork = new int[profile_->lengthIWork()];
    
    double rtol = profile_->relativeTolerance();
    
    double * U= new double [input.size()];
    
    int nparam=profile_->paramLength();
    
    param = new double[nparam];
    
    for (i=0; i < nparam;i++){
        
        param[i]=profile_->paramComponent(i);
    }
    
    
    for (i=0;i < input.size();i++){
        
        U[i]=input.component(i);
        
    }
    
    int info;
    
    info=solver(&LSODE::function, &neq, &U[0], &t, &LSODE::tout_, &itol, &rtol, &atol[0], &itask, &istate, &iopt, &rwork[0], &lrw, &iwork[0], &liw, &LSODE::jacrarefaction, &mf, &nparam, &param[0]);
    
//    tout_=t+profile_->deltaTime();

    tout_ = tout_ + profile_->deltaTime();
    
    time = tout_;
    
    for (i=0;i< neq;i++){
        
        
        output.component(i)=U[i];
    }
    
    delete rwork;
    delete iwork;
    delete U;
    delete param;
    
    return info;
}




int LSODE::function(int * neq , double * xi , double* U , double * out){
    
    int i, status ;
    
    
//    RealVector  input(*neq,U);
//    RealVector output(*neq);

//    cout <<"Valor da entrada "<< U[0]<<endl;
//    cout << "Valor da entrada " << U[1] << endl;
    
    
    
//    RealVector input(2);
//    input(0)=0.1;
//    input(1)=0.2;
    
    
//    RealVector output(2);


    RealVector input(*neq, U);
    RealVector output(*neq);

//    FluxTeste flux;
//    FlowTeste flow(flux);
//    flow.flux(input, output);
    
    status=profile_->getFunction().flux(input, output); //This function must return the same codes of LSODE s functions
    
    for(i=0;i< *neq;i++){
        
        out[i]=output(i);
        
    }
    return status;
    
}

int LSODE::solve(const RealVector & input, ODESolution & output ) const {
//    cout << "Chamando solve" << endl;
//    int steps =0;
//    
//    RealVector outputVector(input);
//    RealVector inputVector(input);
//    
//    double time;
//    
//    int info = 2;
//    
//    while ( steps < LSODE::profile_->maxStepNumber() && stopGenerator_->check(inputVector)) {
//
//
//        info = solve(inputVector, outputVector, time);
//        
//        if (info==2) {
//            output.addCoords(outputVector);
//            inputVector = outputVector;
//            steps++;
//        }
//        
//    tout_=0;
//    
//    return info;
//    
//}

}

const ODESolverProfile & LSODE::getProfile()const { return *profile_;}


void LSODE::setProfile(const LSODEProfile & profile) {
    
    delete profile_;
    profile_=new LSODEProfile(profile);
}


//void LSODE::setStopGenerator(const LSODEStopGenerator & stopGen) {
//    delete stopGenerator_;
////    stopGenerator_ = new LSODEStopGenerator(stopGen);
//}


int LSODE::jacrarefaction(int *neq, double *t, double *y, int *ml, int *mu, double *pd, int *nrpd){
    return SUCCESSFUL_PROCEDURE;
}

int LSODE::solver(int (*f)(int *, double *, double *, double *), int *neq, double *y, double *t, double *tout,
        int *itol, double *rtol, double *atol, int *itask, int *istate, int *iopt, double *rwork, int *lrw,
        int *iwork, int *liw, int(*j)(int *, double *, double *, int *, int *, double *, int *),
        int *mf, int *nparam, double *param) const{
    
    lsode_(f, neq, y, t, tout, itol, rtol, atol, itask, istate,
            iopt, rwork, lrw, iwork, liw, j, mf, nparam, param);
    
    
    if (*istate == 2){
        return SUCCESSFUL_PROCEDURE;
    }
    else {
        return *istate;
    }
    
}




