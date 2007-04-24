/*
*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package rpnumerics;

public interface RpCalculation {
    RpSolution calc() throws RpException;

    RpSolution recalc() throws RpException;
}
