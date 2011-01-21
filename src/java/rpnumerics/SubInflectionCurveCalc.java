/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

public class SubInflectionCurveCalc implements RpCalculation {


    //
    // Constructors/Initializers
    //
    public SubInflectionCurveCalc(){
    }

    public RpSolution recalc() throws RpException {

        return calc();

    }


    public RpSolution calc() throws RpException {


        RpSolution result = nativeCalc();

          if (result == null) {
            throw new RpException("Error in native layer");
        }

        return result;
    }

    private native RpSolution nativeCalc() throws RpException;


}
