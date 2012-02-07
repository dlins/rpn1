/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

public class SubInflectionCurveCalc extends BifurcationCurveCalc {

    //
    // Constructors/Initializers
    //
    public SubInflectionCurveCalc(){
        super(new BifurcationParams());
    }


    public RpSolution calc() throws RpException {


        SubInflectionCurve result = (SubInflectionCurve) nativeCalc();

          if (result == null) {
            throw new RpException("Error in native layer");
        }

        return result;
    }

    private native RpSolution nativeCalc() throws RpException;


}
