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
    
    static RarefactionFlow * createRarefactionFlow(const char *,int,int,const FluxFunction &);

};


inline RarefactionFlow * RarefactionFlowFactory::createRarefactionFlow(const char * flowName,int familyIndex,int timeDirection,const FluxFunction & flux){
    
    
    if (!strcmp(flowName,"RarefactionFlow")){
        
        
        return new RarefactionFlow(familyIndex,timeDirection,flux);
        
    }
    
    
    else{
        
        
        cout <<"Creating another rarefaction flow"<<endl;
        
        
    }
    
}

#endif //! _RarefactionFlowFactory_H
