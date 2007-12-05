
#include "FluxParams.h"


FluxParams::FluxParams(const RealVector & params) {
    
    int i ;
    
    params_= new RealVector(params.size());

    for (i=0; i< params_->size();i++){
        
        params_->component(i)=params.component(i);
    }
    
    
}

FluxParams::FluxParams(const int size,  double * coords):params_(new RealVector(size, coords)){}

FluxParams::FluxParams(const FluxParams &params){//TODO Aqui eh o erro

    set(params.params());
    
//    int i;
//    
//    params_= new RealVector(params.params().size());
//   
//    for (i=0; i < params_->size();i++){
//        params_->component(i)=params.component(i);
//    }
    
}


FluxParams::~FluxParams() {
    delete params_;
    
}

inline  RealVector & FluxParams::params(void) const {
    return *params_;
}

 void FluxParams::set(const RealVector & params){//TODO Create a range check
    
//    int i;
//    delete params_;
    params_= new RealVector(params);
    
//    for (i=0; i < params_->size();i++){
//        
//        params_->component(i)=params.component(i);
//        
//    }
}

double FluxParams::component(int index) const {
    return params_->component(index);
}

void FluxParams::component(int index, double value) {
    params_->component(index) = value;
}

FluxParams & FluxParams::operator=(const FluxParams & source){
    
    if (*this== source)
        return *this;
    
//    int i;
    
    set(source.params());
    
//    params_= new RealVector(source.params().size());
//    
//    for (i=0;i < params_->size();i++){
//        
//        params_->component(i)=source.params().component(i);
//        
//    }
    
    return *this;
    
}

inline bool FluxParams::operator==(const FluxParams & fluxParams) {
    int i ;
    
    for (i=0;i < params_->size();i++){
        
        if (params_->component(i)!=fluxParams.component(i))
            return false;
    }
    return true;
}

