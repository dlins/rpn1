/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import java.util.List;

public class PhysicalBoundaryCalc implements RpCalculation {
  
    public RpSolution recalc() throws RpException {
        return calc();
    }

    public RpSolution calc() throws RpException {

        PhysicalBoundary curve =  (PhysicalBoundary)calcNative();

        if (curve ==null) throw new RpException("Error in native layer");

        return curve;
    }

    public RpSolution recalc(Area area) throws RpException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private native RpSolution calcNative();

    public RpSolution recalc(List<Area> area) throws RpException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
