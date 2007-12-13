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
LSODEProfile::LSODEProfile(const RpFunction & function, const LSODEStopGenerator & sGenerator, int neq , int itol , double rtol, int itask,
        
        int  iopt,  int mf):ODESolverProfile(function, sGenerator){
            
            //Choosing lrw
            
            int mu =0; // TODO  How to choose these values ???
            int ml =0;
            
            
            switch (mf){
                
                case 10:
                    
                    lrw_= 20 + 16*neq ;
                    liw_=20;
                    break;
                    
                case 21:
                    
                    lrw_= 22 +  9*neq + neq*neq;
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
            itask_=itask;
            
            iopt_=iopt ;
            
            mf_=mf;
            neq_=neq;
            itol_=itol;
            
            rwork_= new double[lrw_];
            
            iwork_=new int [liw_];
        }
        
        LSODEProfile:: LSODEProfile(const LSODEProfile & copy){
            
            int i;
            
            function_=copy.getFunction();
            stopGenerator_=copy.getStopGenerator();
            
            rwork_= new double[copy.lengthRWork()];
            
            iwork_=new int [copy.lengthIWork()];
            
            for (i=0; i <copy.lengthRWork();i++){
                rwork_[i]=copy.rworkComponent(i);
            }
            
            for (i=0;i<copy.lengthIWork();i++){
                iwork_[i]=copy.iworkComponent(i);
            }
            
            rtol_= copy.relativeTolerance();
            itask_=copy.task();
            
            iopt_=copy.opt();
            lrw_=copy.lengthRWork();
            liw_=copy.lengthIWork();
            mf_=copy.methodFlag();
            neq_=copy.numberOfEquations();
            itol_=copy.absoluteTolerance();
            
        }
        
        LSODEProfile::~LSODEProfile(){
            delete rwork_;
            delete iwork_;
        }
        
        LSODEProfile & LSODEProfile::operator=(const LSODEProfile & source){
            if (this==&source)
                return *this;
            delete rwork_;
            delete iwork_;
            delete function_;
            delete stopGenerator_;
            
            function_=source.getFunction();
            stopGenerator_=source.getStopGenerator();
            
            int i;
            rwork_= new double[source.lengthRWork()];
            
            iwork_=new int [source.lengthIWork()];
            
            for (i=0; i <source.lengthRWork();i++){
                rwork_[i]=source.rworkComponent(i);
            }
            
            for (i=0;i<source.lengthIWork();i++){
                iwork_[i]=source.iworkComponent(i);
            }
            
            rtol_= source.relativeTolerance();
            itask_=source.task();
            
            iopt_=source.opt();
            lrw_=source.lengthRWork();
            liw_=source.lengthIWork();
            mf_=source.methodFlag();
            neq_=source.numberOfEquations();
            itol_=source.absoluteTolerance();
            
            
            return *this;
            
            
            
        }
        
        
        int LSODEProfile::numberOfEquations()const{return neq_;}
        int LSODEProfile::absoluteTolerance()const{return itol_;}
        double LSODEProfile::relativeTolerance()const{return rtol_;}
        int LSODEProfile::task()const{return itask_;}
        int LSODEProfile::opt()const{return iopt_;}
        
        double  LSODEProfile::rworkComponent(const int i) const {return rwork_[i];}
        int  LSODEProfile::iworkComponent(const int i) const {return iwork_[i];}
        
        int LSODEProfile::lengthRWork()const {return lrw_;}
        int LSODEProfile::lengthIWork() const{return liw_;}
        
        int LSODEProfile::methodFlag() const{return mf_;}
        
        
        
        
        
        
//! Code comes here! daniel@impa.br
        
