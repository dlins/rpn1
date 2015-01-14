/*
*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package rpnumerics;


import java.util.List;

import rpn.configuration.Configuration;


public interface RpCalculation {

    RpSolution calc() throws RpException;

    RpSolution recalc() throws RpException;
    
    void setReferencePoint(OrbitPoint referencePoint);

    RpSolution recalc(List<Area> area) throws RpException;
    
    Configuration getConfiguration();



}
