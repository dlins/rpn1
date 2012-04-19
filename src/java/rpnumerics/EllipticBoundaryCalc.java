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

        result = (EllipticBoundary) nativeCalc(getParams().getResolution());

          if (result == null) {
            throw new RpException("Error in native layer");
        }
        return result;

    }

    public RpSolution recalc() throws RpException {

        return calc();
    }

//    @Override
//    public RpSolution recalc(Area area) throws RpException {
//
//        EllipticBoundary result;
//
//
//        result = (EllipticBoundary) nativeCalc((int) area.getResolution().getElement(0), (int) area.getResolution().getElement(1), area.getTopRight(), area.getDownLeft());
//
//        return result;
//    }

    private native RpSolution nativeCalc(int resolution[]) throws RpException;

//    private native RpSolution nativeCalc(int xRes_, int yRes_, RealVector topR, RealVector dwnL) throws RpException;
}
