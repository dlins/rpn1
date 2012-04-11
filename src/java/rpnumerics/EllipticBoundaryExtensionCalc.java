/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

public class EllipticBoundaryExtensionCalc extends ContourCurveCalc {

    //
    // Constructors
    //
    public EllipticBoundaryExtensionCalc(ContourParams params) {
        super(params);

    }

    public RpSolution calc() throws RpException {

        EllipticBoundaryExtension result;

        result = (EllipticBoundaryExtension) nativeCalc(getParams().getResolution());

          if (result == null) {
            throw new RpException("Error in native layer");
        }
        return result;

    }

    public RpSolution recalc() throws RpException {

        return calc();
    }


    private native RpSolution nativeCalc(int resolution[]) throws RpException;


}
