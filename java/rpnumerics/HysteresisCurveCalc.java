/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

public class HysteresisCurveCalc extends ContourCurveCalc {

    private int family_;


    //
    // Constructors/Initializers
    //
    public HysteresisCurveCalc(ContourParams params, int family) {
        super(params);
        family_ = family;
    }

    @Override
    public RpSolution calc() throws RpException {

        HysteresisCurve result = (HysteresisCurve) nativeCalc(family_);

        if (result == null) {
            throw new RpException("Error in native layer");
        }


        return result;
    }

   

    public int getFamily() {
        return family_;
    }

   

    private native RpSolution nativeCalc(int family) throws RpException;
}
