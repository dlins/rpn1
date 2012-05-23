/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

public class PhysicalBoundaryCalc implements RpCalculation {
  
    public RpSolution recalc() throws RpException {
        return calc();
    }

    public RpSolution calc() throws RpException {
        return calcNative();
    }

    public RpSolution recalc(Area area) throws RpException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private native RpSolution calcNative();
}
