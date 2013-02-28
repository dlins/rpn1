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

    int family_;
    int characteristic_;

    public EllipticBoundaryExtensionCalc(ContourParams params,int characteristic,int family) {
        super(params);
        characteristic_=characteristic;
        family_=family;

    }

    public RpSolution calc() throws RpException {

        EllipticBoundaryExtension result;

        result = (EllipticBoundaryExtension) nativeCalc(characteristic_,family_);

          if (result == null) {
            throw new RpException("Error in native layer");
        }
        return result;

    }

    public RpSolution recalc() throws RpException {

        return calc();
    }


    private native RpSolution nativeCalc(int where_is_characteristic, int family) throws RpException;


}
