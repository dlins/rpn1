/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

public class EllipticBoundaryCalc extends ContourCurveCalc {

    //
    // Constructors
    //
    public EllipticBoundaryCalc(ContourParams params) {
        super(params);

    }

    public RpSolution calc() throws RpException {

        EllipticBoundary result;

        result = (EllipticBoundary) nativeCalc();

          if (result == null) {
            throw new RpException("Error in native layer");
        }
        return result;

    }

    public RpSolution recalc() throws RpException {

        return calc();
    }

    private native RpSolution nativeCalc() throws RpException;


}
