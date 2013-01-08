/*
*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package rpnumerics;

import java.util.List;

public interface RpCalculation {
    RpSolution calc() throws RpException;

    RpSolution recalc() throws RpException;

    RpSolution recalc(List<Area> area) throws RpException;


}
