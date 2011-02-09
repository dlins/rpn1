/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

public class CoincidenceCurveCalc implements RpCalculation {


    //
    // Constructors/Initializers
    //
    public CoincidenceCurveCalc(){
    }

    public RpSolution recalc() throws RpException {

        return calc();

    }


    public RpSolution calc() throws RpException {


    CoincidenceCurve result = (CoincidenceCurve) nativeCalc();

          if (result == null) {
            throw new RpException("Error in native layer");
        }

        return result;
    }

    private native RpSolution nativeCalc() throws RpException;


}
