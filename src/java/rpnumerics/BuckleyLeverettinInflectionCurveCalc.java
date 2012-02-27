/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

public class BuckleyLeverettinInflectionCurveCalc extends ContourCurveCalc {

    //
    // Constructors/Initializers
    //
    public BuckleyLeverettinInflectionCurveCalc(){
        super(new ContourParams());
    }


    public RpSolution calc() throws RpException {


        BuckleyLeverettInflectionCurve result = (BuckleyLeverettInflectionCurve)nativeCalc();

          if (result == null) {
            throw new RpException("Error in native layer");
        }

        return result;
    }

    private native RpSolution nativeCalc() throws RpException;


}
