/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) RarefactionMethodFactory.h
 */

#ifndef _RarefactionMethodFactory_H
#define _RarefactionMethodFactory_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "RarefactionMethod.h"
#include "ContinuationRarefactionMethod.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */

class RarefactionMethodFactory {
    
private:
    
public:
    
    static RarefactionMethod * getRarefactionMethod(const char *,const RarefactionFlow &);
    
};


inline RarefactionMethod * RarefactionMethodFactory::getRarefactionMethod(const char * methodName,const RarefactionFlow & flow){
    
    if (!strcmp(methodName, "ContinuationRarefactionMethod")){
        
        
        return new ContinuationRarefactionMethod(flow);
        
    }
    
    else {
        
        
        cout <<"Creating another rarefaction method !!"<<endl;
        
        
    }
    
    
    
    
}



#endif //! _RarefactionMethodFactory_H
