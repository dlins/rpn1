/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) RarefactionFlowFactory.h
 */

#ifndef _RarefactionFlowFactory_H
#define _RarefactionFlowFactory_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "RarefactionFlow.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */

class RarefactionFlowFactory {

private:

public:
    
    static RarefactionFlow * getRarefactionFlow(const char *,int,int);

};


inline RarefactionFlow * RarefactionFlowFactory::getRarefactionFlow(const char * flowName,int familyIndex,int timeDirection){
    
    
    if (!strcmp(flowName,"RarefactionFlow")){
        
        
        return new RarefactionFlow(familyIndex,timeDirection);
        
    }
    
    
    else{
        
        
        cout <<"Creating another rarefaction flow"<<endl;
        
        
    }
    
}

#endif //! _RarefactionFlowFactory_H
