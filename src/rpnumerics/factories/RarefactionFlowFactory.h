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
#define RP_... 1

class RarefactionFlowFactory {

private:

public:
    
    static RarefactionFlow * getRarefactionFlow(const char *,int);

};


inline RarefactionFlow * RarefactionFlowFactory::getRarefactionFlow(const char * flowName,int familyIndex){
    
    
    if (!strcmp(flowName,"RarefactionFlow")){
        
        
        return new RarefactionFlow(1);
        
    }
    
    
    else{
        
        
        cout <<"Creating another rarefaction flow"<<endl;
        
        
    }
    
}

#endif //! _RarefactionFlowFactory_H
