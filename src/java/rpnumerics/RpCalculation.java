/*
*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package rpnumerics;

import rpn.configuration.Configuration;

public interface RpCalculation {
    RpSolution calc() throws RpException;

    RpSolution recalc() throws RpException;

    RpSolution recalc(Area area) throws RpException;
    
    
    Configuration getConfiguration();


}
