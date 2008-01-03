/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) LSODEProfile.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "LSODEProfile.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


//neq  , itol, rtol, itask, istate, iopt , mf,
LSODEProfile::LSODEProfile(const RpFunction & function, const LSODEStopGenerator & sGenerator, int neq , int itol , double rtol, int mf, double deltaxi, int paramLength, const double * param):ODESolverProfile(function, sGenerator){
    
    //Choosing lrw
    
    
    int mu =0; // TODO Used only in mf ==25 or mf == 24 HARDCODED !!
    int ml =0;
    
    int i;
    
    switch (itol){
        
        case 2:
            
            atol_= new double [neq];
            for (i = 0; i < neq; i++)
                atol_[i] = 1e-6; // HARDCODED !!
            break;
            
        default:
            cout << "atol eh um escalar"<< endl;
    }
    
    switch (mf){
        
        case 10:
            
            lrw_= 20 + 16*neq ;
            liw_=20;
            break;
            
        case 21:
            
            lrw_= (int)(22 +  9*neq + (int)pow((double)neq, 2.0));// neq*neq;
            liw_=20+neq;
            break;
            
        case 22:
            
            lrw_= 22 +  9*neq + neq*neq;
            liw_=20+neq;
            break;
            
        case 24:
            
            lrw_= 22 + 10*neq + (2*ml + mu)*neq;
            liw_= 20 + neq;
            
        case 25:
            
            lrw_= 22 + 10*neq + (2*ml + mu)*neq;
            liw_= 20 + neq;
            
        default:
            cout << "Erro in lrw or liw choose"<<endl;
            
    }
    
    rtol_=rtol;
    itask_=1; //HARDCODED for normal computation !!
    
    iopt_=0; //HARDCODED !!!
    
    mf_=mf;
    neq_=neq;
    itol_=itol;
    deltaxi_=deltaxi;
    
    rwork_= new double[lrw_];
    
    iwork_=new int [liw_];
    
    paramLength_=paramLength;
    
    param_= new double[paramLength];
    
    
    for (i=0; i < paramLength;i++){
        
        param_[i]=param[i];
    }
    
}

LSODEProfile:: LSODEProfile(const LSODEProfile & copy):ODESolverProfile(*copy.getFunction(), *copy.getStopGenerator()){
    
    int i;
    
    
//    function_=copy.getFunction()->clone();
//    stopGenerator_=copy.getStopGenerator()->clone();
    
    rwork_= new double[copy.lengthRWork()];
    
    iwork_=new int [copy.lengthIWork()];
    
    param_= new double [copy.paramLength()];
    
    for (i=0; i < copy.paramLength();i++){
        
        param_[i]= copy.paramComponent(i);
    }
    
    for (i=0; i <copy.lengthRWork();i++){
        rwork_[i]=copy.rworkComponent(i);
    }
    
    for (i=0;i<copy.lengthIWork();i++){
        iwork_[i]=copy.iworkComponent(i);
    }
    
    rtol_= copy.relativeTolerance();
    itask_=copy.task();
    deltaxi_=copy.deltaTime();
    iopt_=copy.opt();
    lrw_=copy.lengthRWork();
    liw_=copy.lengthIWork();
    mf_=copy.methodFlag();
    neq_=copy.numberOfEquations();
    itol_=copy.absoluteToleranceType();
    
    if (itol_==2){
        atol_= new double [neq_];
        for (i = 0; i < neq_; i++) atol_[i] = 1e-6; // HARDCODED !!
    }
}

LSODEProfile::~LSODEProfile(){
    
    delete rwork_;
    delete iwork_;
    delete atol_;
    delete param_;
    
    
}

LSODEProfile & LSODEProfile::operator=(const LSODEProfile & source){
    if (this==&source)
        return *this;
    delete rwork_;
    delete iwork_;
    delete function_;
    delete stopGenerator_;
    delete atol_;
    delete param_;
    
    function_=source.getFunction();
    stopGenerator_=source.getStopGenerator();
    neq_=source.numberOfEquations();
    itol_=source.absoluteToleranceType();
    
    int i;
    
    if (itol_==2){
        
        atol_= new double [neq_];
        for (i = 0; i < neq_; i++) atol_[i] = 1e-6; // HARDCODED !!
    }
    
    rwork_= new double[source.lengthRWork()];
    
    iwork_=new int [source.lengthIWork()];
    
    for (i=0; i <source.lengthRWork();i++){
        rwork_[i]=source.rworkComponent(i);
    }
    
    for (i=0;i<source.lengthIWork();i++){
        iwork_[i]=source.iworkComponent(i);
    }
    
    param_= new double [source.paramLength()];
    for (i=0; i < source.paramLength();i++){
        
        param_[i]= source.paramComponent(i);
    }
    
    rtol_= source.relativeTolerance();
    itask_=source.task();
    
    iopt_=source.opt();
    lrw_=source.lengthRWork();
    liw_=source.lengthIWork();
    mf_=source.methodFlag();
    
    return *this;
}






//! Code comes here! daniel@impa.br

